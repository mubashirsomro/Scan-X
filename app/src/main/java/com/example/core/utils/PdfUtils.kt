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
}
