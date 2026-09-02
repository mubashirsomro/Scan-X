package com.example.core.navigation

import kotlinx.serialization.Serializable

@Serializable
object HomeRoute

@Serializable
object HistoryRoute

@Serializable
object DocumentsRoute

@Serializable
object SettingsRoute

@Serializable
object MainRoute

@Serializable
object QrScannerRoute

@Serializable
object BarcodeScannerRoute

@Serializable
object DocumentScannerRoute

@Serializable
object OcrRoute

@Serializable
object WifiScannerRoute

@Serializable
object QrGeneratorRoute

@Serializable
object ImageToPdfRoute

@Serializable
object PdfToolsRoute

@Serializable
data class ScanResultRoute(val content: String, val type: Int)

@Serializable
data class DocumentResultRoute(val pdfUri: String, val imageUris: String) // comma separated string
