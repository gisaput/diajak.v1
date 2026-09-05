package com.example.ui.theme

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.ColumnScope
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Card
import androidx.compose.runtime.Composable
import androidx.compose.runtime.staticCompositionLocalOf
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.TextUnit
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

/**
 * Standardized Card component for the entire Diajak application.
 * Provides modern aesthetic styling:
 * - Corner Radius: 16.dp to 20.dp
 * - Background Color: Surface white with soft elevation
 * - Border/Outline: Crisp soft border or brand active outline
 * - Elevation: 2.dp default elevation
 */
@Composable
fun DiajakCard(
    modifier: Modifier = Modifier,
    isSelected: Boolean = false,
    onClick: (() -> Unit)? = null,
    shape: androidx.compose.ui.graphics.Shape = RoundedCornerShape(16.dp),
    containerColor: Color = Color.White,
    elevationDp: Dp = 0.dp,
    content: @Composable ColumnScope.() -> Unit
) {
    val borderStroke = if (isSelected) {
        BorderStroke(2.dp, DiajakOrange)
    } else {
        BorderStroke(0.dp, Color.Transparent)
    }

    if (onClick != null) {
        Card(
            onClick = onClick,
            modifier = modifier,
            shape = shape,
            colors = CardDefaults.cardColors(containerColor = containerColor),
            border = borderStroke,
            elevation = CardDefaults.cardElevation(defaultElevation = elevationDp),
            content = content
        )
    } else {
        Card(
            modifier = modifier,
            shape = shape,
            colors = CardDefaults.cardColors(containerColor = containerColor),
            border = borderStroke,
            elevation = CardDefaults.cardElevation(defaultElevation = elevationDp),
            content = content
        )
    }
}

/**
 * Standardized color tokens for Diajak application.
 * Tegas System: High-contrast typography & distinct card borders.
 */
object AppColors {
    val TextPrimary: Color = DiajakTextDark // Color(0xFF111827) Deep Black
    val TextSecondary: Color = GoogleTextSecondary // Color(0xFF374151) Dark Slate
    val TextMuted: Color = DiajakTextMuted // Color(0xFF4B5563) Legible Slate Gray
    val Primary: Color = DiajakOrange
    val Border: Color = TegasBorder // Color(0xFFDADCE0) 1dp Crisp Outline
    val SelectedBorder: Color = DiajakOrange // 2dp Active Orange Outline
    val CardBackground: Color = Color.White
    val Background: Color = Color(0xFFE5E7EB) // Light gray for screen roots
    val Surface: Color = Color.White // Pure white for cards and sheets
}

/**
 * Centralized layout spacing and padding tokens for Diajak application.
 * Standardized based on Beranda (HomeScreen) and Detail (DetailScreen) layouts.
 */
object AppDimens {
    // Standard Material Design Spacing Scale (4dp/8dp grid)
    val SpacingXSmall: Dp = 4.dp     // 4dp
    val SpacingSmall: Dp = 8.dp      // 8dp
    val SpacingMedium: Dp = 12.dp    // 12dp
    val SpacingLarge: Dp = 16.dp     // 16dp
    val SpacingXLarge: Dp = 20.dp    // 20dp
    val SpacingXXLarge: Dp = 24.dp   // 24dp
    val SpacingHuge: Dp = 32.dp      // 32dp
    val SpacingGiant: Dp = 48.dp     // 48dp
    
    // Standard Screen Margins & Layout Padding
    val ScreenPaddingHorizontal: Dp = 20.dp
    val ScreenPaddingVertical: Dp = 12.dp
    
    // Component Padding & Radius
    val CardPaddingHorizontal: Dp = 20.dp
    val CardPaddingVertical: Dp = 12.dp
    val CardPadding: Dp = 16.dp
    val CardCornerRadius: Dp = 16.dp
    val ButtonCornerRadius: Dp = 24.dp
    val InputFieldCornerRadius: Dp = 100.dp
    val TouchTargetMin: Dp = 48.dp
    
    // App Bar & Bottom Navigation Overlays
    val AppBarHorizontalPadding: Dp = 20.dp
    val AppBarVerticalPadding: Dp = 12.dp
    val BottomNavContentPadding: Dp = 100.dp
}

/**
 * Centralized specification for iOS-Style Progressive Glass Header layout and parameters.
 */
object AppHeaderSystem {
    // Locked Dimension Specifications
    val HeaderContentHeight: Dp = 56.dp
    val HeaderScrollGap: Dp = 24.dp
    val HeaderTotalTopPadding: Dp = HeaderContentHeight + HeaderScrollGap // 80.dp

    // Standard Top Spacings for Screens:
    // 1. Screens WITH Back Button (sub-screens, booking, etc): 80.dp (56.dp bar + 24.dp content gap)
    val SubScreenTopPadding: Dp = 80.dp
    // 2. Root Tabs WITHOUT Back Button (Favorites, Bookings, Messages, Profile): 16.dp from status bar
    val RootTabTopPadding: Dp = 16.dp

    // Locked Glassmorphism Configuration
    val GlassBaseColor: Color = Color(0xFFE5E7EB)
    val GlassTintAlpha: Float = 0.7f
    val GlassBlurRadius: Dp = 24.dp

    // Circular Button Glass Configuration
    val ButtonBaseColor: Color = Color(0xFFF5F5F5)
    val ButtonTintAlpha: Float = 0.6f
    val ButtonBlurRadius: Dp = 12.dp
    
    // Title Configuration Reference
    // The title text style must strictly use DiajakDesignSystem.Typography.Headline
}

/**
 * Centralized icon configuration and sizing standards for Diajak application.
 * Enforces a consistent 24.dp size for all icon components across the application.
 */
object AppIcons {
    val DefaultSize: Dp = 24.dp
    val LargeSize: Dp = 24.dp
    val MediumSize: Dp = 24.dp
    val SmallSize: Dp = 24.dp

    /**
     * Standard modifier for icon components enforcing consistent 24.dp sizing.
     */
    val DefaultModifier: Modifier
        get() = Modifier.size(DefaultSize)
}

/**
 * CompositionLocal providing standardized icon size across the application theme.
 */
val LocalAppIconSize = staticCompositionLocalOf { AppIcons.DefaultSize }

/**
 * Centralized typography and line spacing styles for Diajak application.
 * Standardized strictly to 3 font sizes (20 sp, 16 sp, 14 sp) and proportional line heights.
 */
object AppTypography {
    val FontFamily = AppFontFamily
    val HeadlineFont = AppFontFamily
    val ScriptFont = AppFontFamily
    
    // Standardized Font Sizes across the Hierarchy
    val SizeDisplay: TextUnit = 24.sp   // Large Hero Display / Titles
    val SizeHeadline: TextUnit = 20.sp  // Section Headline / Screen Header
    val SizeTitle: TextUnit = 16.sp     // Card Titles / Subtitles
    val SizeBody: TextUnit = 14.sp      // Standard Body Text / Descriptions
    val SizeLabel: TextUnit = 12.sp     // Badges, Metadata, Buttons
    val SizeCaption: TextUnit = 11.sp   // Footers, Helper Text
    
    // Standardized Font Weights
    val WeightExtraBold = FontWeight.ExtraBold
    val WeightBold = FontWeight.Bold
    val WeightSemiBold = FontWeight.SemiBold
    val WeightMedium = FontWeight.Medium
    val WeightNormal = FontWeight.Normal
    
    // Pre-configured Text Styles with distinct font weights & heights
    
    // [HEADINGS] - Outfit Headline Font with ExtraBold/Bold weights
    val Display = TextStyle(
        fontFamily = HeadlineFont,
        fontWeight = WeightExtraBold,
        fontSize = SizeDisplay,
        lineHeight = 30.sp
    )

    val Headline = TextStyle(
        fontFamily = HeadlineFont,
        fontWeight = WeightBold,
        fontSize = SizeHeadline,
        lineHeight = 26.sp
    )
    
    val TitleBold = TextStyle(
        fontFamily = HeadlineFont,
        fontWeight = WeightBold,
        fontSize = 18.sp,
        lineHeight = 24.sp
    )

    val TitleMedium = TextStyle(
        fontFamily = HeadlineFont,
        fontWeight = WeightSemiBold,
        fontSize = SizeTitle,
        lineHeight = 22.sp
    )

    val Title = TextStyle(
        fontFamily = HeadlineFont,
        fontWeight = WeightMedium,
        fontSize = SizeTitle,
        lineHeight = 22.sp
    )
    
    // [BODY TEXT] - Inter System Font with Normal/Medium/SemiBold weights
    val BodyLarge = TextStyle(
        fontFamily = FontFamily,
        fontWeight = WeightNormal,
        fontSize = 16.sp,
        lineHeight = 24.sp
    )

    val Body = TextStyle(
        fontFamily = FontFamily,
        fontWeight = WeightNormal,
        fontSize = SizeBody,
        lineHeight = 20.sp
    )
    
    val BodyBold = TextStyle(
        fontFamily = FontFamily,
        fontWeight = WeightSemiBold,
        fontSize = SizeBody,
        lineHeight = 20.sp
    )
    
    // [LABELS & CAPTIONS] - Inter System Font with crisp medium/semibold weights
    val LabelBold = TextStyle(
        fontFamily = FontFamily,
        fontWeight = WeightSemiBold,
        fontSize = SizeLabel,
        lineHeight = 16.sp
    )

    val Label = TextStyle(
        fontFamily = FontFamily,
        fontWeight = WeightMedium,
        fontSize = SizeLabel,
        lineHeight = 16.sp
    )

    val Caption = TextStyle(
        fontFamily = FontFamily,
        fontWeight = WeightNormal,
        fontSize = SizeCaption,
        lineHeight = 14.sp
    )
    
    // Classic script font option for decorative logos
    val ScriptLogo = TextStyle(
        fontFamily = ScriptFont,
        fontSize = 28.sp,
        fontWeight = FontWeight.Normal
    )
}

/**
 * Master Theme Object to access layout tokens cleanly inside Composables
 */
object DiajakDesignSystem {
    val Dimens = AppDimens
    val Typography = AppTypography
    val Colors = AppColors
    val Icons = AppIcons
    val Header = AppHeaderSystem
    val Glass = GlassmorphismTheme
}

