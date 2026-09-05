package com.example.ui.screens

import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Brush
import androidx.compose.foundation.background

fun testBrush() {
    val mod = Modifier.background(
        brush = Brush.verticalGradient(
            colors = listOf(
                Color(0xFFE5E7EB),
                Color(0xFFE5E7EB).copy(alpha = 0.95f),
                Color.Transparent
            )
        )
    )
}
