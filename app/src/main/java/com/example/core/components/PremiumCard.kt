package com.example.core.components

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.ui.theme.CyanAccent
import com.example.ui.theme.DeepNavy

@Composable
fun PremiumCard(
    onClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    Row(
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.SpaceBetween,
        modifier = modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(24.dp))
            .background(DeepNavy)
            .clickable { onClick() }
            .padding(16.dp)
    ) {
        Row(
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            Box(
                modifier = Modifier
                    .size(32.dp)
                    .background(Color(0xFFFBBF24), RoundedCornerShape(16.dp)),
                contentAlignment = Alignment.Center
            ) {
                Text(
                    text = "★",
                    style = MaterialTheme.typography.labelSmall.copy(fontSize = 12.sp),
                    color = DeepNavy,
                    fontWeight = FontWeight.Black
                )
            }
            Column {
                Text(
                    text = "ScanX Premium",
                    style = MaterialTheme.typography.labelLarge.copy(fontSize = 12.sp),
                    fontWeight = FontWeight.Bold,
                    color = Color.White
                )
                Text(
                    text = "Unlock OCR & Cloud Save",
                    style = MaterialTheme.typography.labelSmall.copy(fontSize = 10.sp),
                    color = Color.White.copy(alpha = 0.5f)
                )
            }
        }
        
        Box(
            modifier = Modifier
                .border(BorderStroke(1.dp, CyanAccent.copy(alpha = 0.3f)), RoundedCornerShape(16.dp))
                .padding(horizontal = 12.dp, vertical = 6.dp)
        ) {
            Text(
                text = "UPGRADE",
                style = MaterialTheme.typography.labelSmall.copy(fontSize = 10.sp),
                color = CyanAccent,
                fontWeight = FontWeight.Bold
            )
        }
    }
}
