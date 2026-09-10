package com.example.ui.theme

import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.ReadOnlyComposable
import androidx.compose.runtime.compositionLocalOf
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp

/**
 * Centralized spacing values for the Diajak application.
 * Adheres to Material Design 3 guidelines and an 8dp grid system.
 * Use these constants to ensure consistent padding and margins across all screens.
 */
data class Spacing(
    // Core Spacing Scale
    val none: Dp = 0.dp,
    val extraSmall: Dp = 4.dp,
    val small: Dp = 8.dp,
    val medium: Dp = 16.dp,
    val large: Dp = 24.dp,
    val extraLarge: Dp = 32.dp,
    val huge: Dp = 48.dp,

    // Semantic Spacing
    /** Jarak antar elemen kecil / komponen internal form (8.dp) */
    val element: Dp = 8.dp,
    
    /** Jarak antara judul section dan konten di bawahnya (8.dp) */
    val sectionContent: Dp = 8.dp,
    
    /** Jarak antar section besar/utama (16.dp - harmonis dengan margin layar) */
    val section: Dp = 16.dp,
    
    /** Margin standar untuk sisi kiri dan kanan layar (16.dp) */
    val screenMargin: Dp = 16.dp
)

val LocalSpacing = compositionLocalOf { Spacing() }

val MaterialTheme.spacing: Spacing
    @Composable
    @ReadOnlyComposable
    get() = LocalSpacing.current

// Retained for backward compatibility if any files are currently referencing it directly.
object ThemeSpacing {
    val None: Dp = 0.dp
    val ExtraSmall: Dp = 4.dp
    val Small: Dp = 8.dp
    val Medium: Dp = 16.dp
    val Large: Dp = 16.dp // Aligned with Medium (16.dp) for uniform vertical-horizontal harmony
    val ExtraLarge: Dp = 32.dp
    val Huge: Dp = 48.dp
    val Element: Dp = Small
    val SectionContent: Dp = Medium
    val Section: Dp = Medium
    val ScreenMargin: Dp = Medium
}

/**
 * Standardized alias for application spacing, matching ThemeSpacing.
 */
typealias AppSpacing = ThemeSpacing
