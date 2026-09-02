package com.example.core.navigation

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.toRoute
import com.example.features.main.MainScreen

@Composable
fun AppNavigation(
    isDarkTheme: Boolean,
    onThemeToggle: () -> Unit,
    modifier: Modifier = Modifier
) {
    val navController = rememberNavController()

    NavHost(
        navController = navController,
        startDestination = MainRoute,
        modifier = modifier
    ) {
        composable<MainRoute> {
            MainScreen(
                isDarkTheme = isDarkTheme,
                onThemeToggle = onThemeToggle,
                onNavigateOuter = { route ->
                    navController.navigate(route)
                }
            )
        }
        
        composable<QrScannerRoute> {
            com.example.features.scanner.ScannerScreen(
                title = "QR Scanner",
                barcodeFormats = intArrayOf(com.google.mlkit.vision.barcode.common.Barcode.FORMAT_QR_CODE, com.google.mlkit.vision.barcode.common.Barcode.FORMAT_DATA_MATRIX),
                onNavigateResult = { content, type -> 
                    navController.navigate(ScanResultRoute(content, type))
                },
                onNavigateBack = { navController.navigateUp() }
            )
        }
        
        composable<BarcodeScannerRoute> {
            com.example.features.scanner.ScannerScreen(
                title = "Barcode Scanner",
                barcodeFormats = intArrayOf(
                    com.google.mlkit.vision.barcode.common.Barcode.FORMAT_EAN_13,
                    com.google.mlkit.vision.barcode.common.Barcode.FORMAT_EAN_8,
                    com.google.mlkit.vision.barcode.common.Barcode.FORMAT_UPC_A,
                    com.google.mlkit.vision.barcode.common.Barcode.FORMAT_UPC_E,
                    com.google.mlkit.vision.barcode.common.Barcode.FORMAT_CODE_128,
                    com.google.mlkit.vision.barcode.common.Barcode.FORMAT_CODE_39
                ),
                onNavigateResult = { content, type -> 
                    navController.navigate(ScanResultRoute(content, type))
                },
                onNavigateBack = { navController.navigateUp() }
            )
        }
        
        composable<ScanResultRoute> { backStackEntry ->
            val route = backStackEntry.toRoute<ScanResultRoute>()
            com.example.features.scanner.ScanResultScreen(
                content = route.content,
                type = route.type,
                onNavigateBack = { navController.navigateUp() }
            )
        }
        
        composable<DocumentScannerRoute> {
            com.example.features.scanner.DocumentScannerScreen(
                onNavigateBack = { navController.navigateUp() },
                onNavigateResult = { pdfUri, pages ->
                    navController.navigate(DocumentResultRoute(pdfUri, pages.joinToString(",")))
                }
            )
        }
        
        composable<DocumentResultRoute> { backStackEntry ->
            val route = backStackEntry.toRoute<DocumentResultRoute>()
            com.example.features.scanner.DocumentResultScreen(
                pdfUri = route.pdfUri,
                imageUris = route.imageUris,
                onNavigateBack = { navController.navigateUp() }
            )
        }
        
        composable<OcrRoute> {
            com.example.features.scanner.OcrScreen(
                onNavigateResult = { content, type -> 
                    navController.navigate(ScanResultRoute(content, type))
                },
                onNavigateBack = { navController.navigateUp() }
            )
        }
        
        composable<WifiScannerRoute> {
            // Placeholder
        }
        
        composable<PdfToolsRoute> {
            com.example.features.pdf.PdfToolsScreen(
                onNavigateBack = { navController.navigateUp() }
            )
        }
    }
}
