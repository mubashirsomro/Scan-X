package com.example.core.components

import androidx.compose.animation.core.*
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.CornerRadius
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Rect
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.BlendMode
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Path
import androidx.compose.ui.graphics.drawscope.Stroke

@Composable
fun ScannerOverlay(
    modifier: Modifier = Modifier,
    scanAreaSize: Float = 0.6f, // 60% of the smaller dimension
    borderColor: Color = Color(0xFF00E5FF)
) {
    val infiniteTransition = rememberInfiniteTransition(label = "scanLine")
    val lineOffset by infiniteTransition.animateFloat(
        initialValue = 0f,
        targetValue = 1f,
        animationSpec = infiniteRepeatable(
            animation = tween(2000, easing = LinearEasing),
            repeatMode = RepeatMode.Reverse
        ),
        label = "lineOffset"
    )

    Canvas(modifier = modifier.fillMaxSize()) {
        val canvasWidth = size.width
        val canvasHeight = size.height

        val scanBoxSize = minOf(canvasWidth, canvasHeight) * scanAreaSize
        val left = (canvasWidth - scanBoxSize) / 2
        val top = (canvasHeight - scanBoxSize) / 2
        val right = left + scanBoxSize
        val bottom = top + scanBoxSize

        val rect = Rect(left, top, right, bottom)
        val cornerRadius = CornerRadius(24f, 24f)

        val path = Path().apply {
            addRoundRect(
                androidx.compose.ui.geometry.RoundRect(
                    rect = rect,
                    cornerRadius = cornerRadius
                )
            )
        }

        // Draw semi-transparent background
        drawRect(
            color = Color(0x99000000), // Semi-transparent black
            size = Size(canvasWidth, canvasHeight)
        )

        // Clear the center area
        drawPath(
            path = path,
            color = Color.Transparent,
            blendMode = BlendMode.Clear
        )

        // Draw the border
        drawPath(
            path = path,
            color = borderColor,
            style = Stroke(width = 4f)
        )

        // Draw the animated scan line
        val lineY = top + (scanBoxSize * lineOffset)
        drawLine(
            color = borderColor,
            start = Offset(left, lineY),
            end = Offset(right, lineY),
            strokeWidth = 4f
        )
    }
}
