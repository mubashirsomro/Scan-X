package com.example.core.utils

import android.content.Context
import android.net.Uri
import com.tom_roush.pdfbox.multipdf.PDFMergerUtility
import com.tom_roush.pdfbox.multipdf.Splitter
import com.tom_roush.pdfbox.pdmodel.PDDocument
import java.io.File
import java.io.FileInputStream
import java.io.FileOutputStream
import java.util.zip.ZipEntry
import java.util.zip.ZipOutputStream

object PdfUtils {
    
    fun mergePdfs(context: Context, uris: List<Uri>, outputUri: Uri): Result<Unit> {
        return try {
            val merger = PDFMergerUtility()
            context.contentResolver.openOutputStream(outputUri)?.use { out ->
                merger.destinationStream = out
                for (uri in uris) {
                    val input = context.contentResolver.openInputStream(uri)
                    if (input != null) {
                        merger.addSource(input)
                    }
                }
                merger.mergeDocuments(null)
            }
            Result.success(Unit)
        } catch (e: Exception) {
            e.printStackTrace()
            Result.failure(e)
        }
    }

    fun splitPdfToZip(context: Context, inputUri: Uri, outputZipUri: Uri): Result<Unit> {
        return try {
            context.contentResolver.openInputStream(inputUri)?.use { inputStream ->
                val document = PDDocument.load(inputStream)
                val splitter = Splitter()
                val splitDocuments = splitter.split(document)
                
                context.contentResolver.openOutputStream(outputZipUri)?.use { zipOutStream ->
                    ZipOutputStream(zipOutStream).use { zos ->
                        for ((index, doc) in splitDocuments.withIndex()) {
                            val tempFile = File.createTempFile("split_page_${index + 1}", ".pdf", context.cacheDir)
                            doc.save(tempFile)
                            doc.close()
                            
                            val zipEntry = ZipEntry("page_${index + 1}.pdf")
                            zos.putNextEntry(zipEntry)
                            
                            FileInputStream(tempFile).use { fis ->
                                fis.copyTo(zos)
                            }
                            zos.closeEntry()
                            tempFile.delete()
                        }
                    }
                }
                document.close()
            }
            Result.success(Unit)
        } catch (e: Exception) {
            e.printStackTrace()
            Result.failure(e)
        }
    }

    fun imagesToPdf(context: Context, imageUris: List<Uri>, outputUri: Uri): Result<Unit> {
        return try {
            val document = android.graphics.pdf.PdfDocument()
            for ((index, uri) in imageUris.withIndex()) {
                val bitmap = if (android.os.Build.VERSION.SDK_INT >= 28) {
                    val source = android.graphics.ImageDecoder.createSource(context.contentResolver, uri)
                    val decoded = android.graphics.ImageDecoder.decodeBitmap(source)
                    decoded.copy(android.graphics.Bitmap.Config.ARGB_8888, false)
                } else {
                    @Suppress("DEPRECATION")
                    android.provider.MediaStore.Images.Media.getBitmap(context.contentResolver, uri)
                }
                
                val pageInfo = android.graphics.pdf.PdfDocument.PageInfo.Builder(bitmap.width, bitmap.height, index + 1).create()
                val page = document.startPage(pageInfo)
                page.canvas.drawBitmap(bitmap, 0f, 0f, null)
                document.finishPage(page)
            }
            context.contentResolver.openOutputStream(outputUri)?.use { out ->
                document.writeTo(out)
            }
            document.close()
            Result.success(Unit)
        } catch (e: Exception) {
            e.printStackTrace()
            Result.failure(e)
        }
    }

    fun pdfToImagesZip(context: Context, pdfUri: Uri, outputZipUri: Uri): Result<Unit> {
        return try {
            val fd = context.contentResolver.openFileDescriptor(pdfUri, "r") ?: return Result.failure(Exception("Cannot open file"))
            val renderer = android.graphics.pdf.PdfRenderer(fd)
            
            context.contentResolver.openOutputStream(outputZipUri)?.use { zipOutStream ->
                ZipOutputStream(zipOutStream).use { zos ->
                    for (i in 0 until renderer.pageCount) {
                        val page = renderer.openPage(i)
                        
                        val bitmap = android.graphics.Bitmap.createBitmap(page.width * 2, page.height * 2, android.graphics.Bitmap.Config.ARGB_8888)
                        val canvas = android.graphics.Canvas(bitmap)
                        canvas.drawColor(android.graphics.Color.WHITE)
                        
                        val matrix = android.graphics.Matrix()
                        matrix.postScale(2f, 2f)
                        
                        page.render(bitmap, null, matrix, android.graphics.pdf.PdfRenderer.Page.RENDER_MODE_FOR_DISPLAY)
                        
                        val tempFile = File.createTempFile("page_img_${i + 1}", ".png", context.cacheDir)
                        val out = FileOutputStream(tempFile)
                        bitmap.compress(android.graphics.Bitmap.CompressFormat.PNG, 100, out)
                        out.close()
                        page.close()
                        
                        val zipEntry = ZipEntry("page_${i + 1}.png")
                        zos.putNextEntry(zipEntry)
                        
                        FileInputStream(tempFile).use { fis ->
                            fis.copyTo(zos)
                        }
                        zos.closeEntry()
                        tempFile.delete()
                    }
                }
            }
            renderer.close()
            fd.close()
            Result.success(Unit)
        } catch (e: Exception) {
            e.printStackTrace()
            Result.failure(e)
        }
    }
}
