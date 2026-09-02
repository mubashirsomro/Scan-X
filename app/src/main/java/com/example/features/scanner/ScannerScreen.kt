package com.example.features.scanner

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.FlashOff
import androidx.compose.material.icons.filled.FlashOn
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import com.example.core.components.ScannerCameraPreview
import com.example.core.components.ScannerOverlay
import com.example.core.utils.vibrateOnScan
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ScannerScreen(
    title: String,
    barcodeFormats: IntArray,
    onNavigateResult: (String, Int) -> Unit,
    onNavigateBack: () -> Unit,
    modifier: Modifier = Modifier
) {
    var flashEnabled by remember { mutableStateOf(false) }
    var hasScanned by remember { mutableStateOf(false) }
    val context = LocalContext.current
    val coroutineScope = rememberCoroutineScope()

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text(title, color = Color.White) },
                navigationIcon = {
                    IconButton(onClick = onNavigateBack) {
                        Icon(Icons.AutoMirrored.Filled.ArrowBack, contentDescription = "Back", tint = Color.White)
                    }
                },
                actions = {
                    IconButton(onClick = { flashEnabled = !flashEnabled }) {
                        Icon(
                            imageVector = if (flashEnabled) Icons.Default.FlashOn else Icons.Default.FlashOff,
                            contentDescription = "Toggle Flash",
                            tint = Color.White
                        )
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = Color.Transparent
                )
            )
        },
        containerColor = Color.Black
    ) { innerPadding ->
        Box(
            modifier = modifier
                .fillMaxSize()
                .padding(innerPadding)
        ) {
            ScannerCameraPreview(
                barcodeFormats = barcodeFormats,
                flashEnabled = flashEnabled,
                onBarcodesDetected = { barcodes ->
                    if (!hasScanned && barcodes.isNotEmpty()) {
                        val barcode = barcodes.first()
                        val value = barcode.rawValue
                        if (value != null) {
                            hasScanned = true
                            vibrateOnScan(context)
                            // Allow slight delay so user sees they hit it
                            coroutineScope.launch {
                                delay(300)
                                onNavigateResult(value, barcode.valueType)
                                // Reset scan state so they can scan again if they pop back
                                hasScanned = false 
                            }
                        }
                    }
                },
                modifier = Modifier.fillMaxSize()
            )
            
            ScannerOverlay()
            
            Text(
                text = "Align $title within the frame to scan",
                color = Color.White,
                modifier = Modifier
                    .align(Alignment.BottomCenter)
                    .padding(bottom = 64.dp),
                style = MaterialTheme.typography.bodyMedium
            )
        }
    }
}
