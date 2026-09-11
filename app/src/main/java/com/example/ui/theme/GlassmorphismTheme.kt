package com.example.ui.theme

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.ProvidableCompositionLocal
import androidx.compose.runtime.compositionLocalOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.drawWithContent
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.BlendMode
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.CompositingStrategy
import androidx.compose.ui.graphics.Shape
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.graphics.luminance
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import dev.chrisbanes.haze.HazeState
import dev.chrisbanes.haze.HazeStyle
import dev.chrisbanes.haze.HazeTint
import dev.chrisbanes.haze.hazeEffect

/**
 * Centralized Glassmorphism Design System and UI Utilities for Diajak Application.
 *
 * Standardizes Haze blur intensity, tint colors, alpha levels, and progressive gradients
 * to ensure that all future and current UI elements automatically inherit the exact
 * design system values established in AGENTS.md and DetailScreen.
 */
object GlassmorphismTheme {

    /**
     * Standard Header Glass Specifications (iOS-Style Crystal Frosted Glass)
     */
    object Header {
        val ContentHeight: Dp = 56.dp
        val ScrollGap: Dp = 24.dp
        val TotalTopPadding: Dp = ContentHeight + ScrollGap // 80.dp

        val BaseColor: Color = Color(0xFFE5E7EB)
        const val TintAlpha: Float = 0.0f
        val BlurRadius: Dp = 18.dp
        const val NoiseFactor: Float = 0f

        // Universal Pure Crystal Optical Glass Tint (Tipis, jernih, kilau kaca asli)
        const val UniversalGlassAlpha: Float = 0.20f

        /**
         * Universal Tinder & iOS Style Frosted Glass Header:
         * Uses 24.dp pure optical blur with completely transparent background (NO double white paint).
         * Zero tint (0.0f) ensures search bars, chips, and white containers match footer card softness
         * without creating chalky, milky layers.
         */
        val UniversalStyle: HazeStyle = HazeStyle(
            backgroundColor = Color.Transparent,
            tint = HazeTint(Color.White.copy(alpha = 0f)),
            blurRadius = 24.dp,
            noiseFactor = 0f
        )

        // The base style for pure optical blur over photos (zero milky fog, 100% crystal)
        val OpticalStyle: HazeStyle = HazeStyle(
            backgroundColor = Color.Transparent,
            tint = HazeTint(Color.Transparent),
            blurRadius = 24.dp,
            noiseFactor = 0f
        )

        /**
         * Tinder/iOS uses a single continuous glass material rather than abruptly switching paints.
         * For PHOTO underlay, the glass is purely optical (transparent paint).
         * For non-photo underlay, no hard-edged color spray is applied; the HazeStyle's frosted tint
         * and the 3-Tier ProgressiveGradientBrush naturally handle the smooth melting.
         */
        fun progressiveDissolverBrush(underlayState: HeaderUnderlayState): Brush {
            // Keep completely transparent: the Haze frosted style and ProgressiveGradientBrush
            // perform the natural dissolving without abrupt blocky paint bands!
            return Brush.verticalGradient(
                0.0f to Color.Transparent,
                1.0f to Color.Transparent
            )
        }

        fun tintFor(underlayState: HeaderUnderlayState): Color = Color.Transparent

        /**
         * Dynamic Optical & Frosted Style:
         * Uses HazeTint with canvas gray BaseColor Color(0xFFE5E7EB).
         * - Over PHOTO & CONTAINER (Search bar, Chip, Card, Footer): pure optical blur (tint alpha 0.0f)
         *   matching footer card smoothness and eliminating milky chalk blocks.
         * - Over TEXT (Standalone text on gray canvas): gentle gray canvas tint to smoothly dissolve dark
         *   letters directly into the background gray canvas without any chalky white blocks (iOS Settings & Tinder style).
         */
        fun dynamicStyle(
            photoRatio: Float = 0f,
            textMeltingRatio: Float = 0f,
            baseColor: Color = BaseColor
        ): HazeStyle {
            val textBoost = (0.65f * textMeltingRatio * (1f - photoRatio)).coerceIn(0f, 0.65f)

            return HazeStyle(
                backgroundColor = Color.Transparent,
                tint = HazeTint(baseColor.copy(alpha = textBoost)),
                blurRadius = 24.dp,
                noiseFactor = 0f
            )
        }

        /**
         * Returns the clean optical HazeStyle (zero flat paint spray).
         */
        fun styleFor(underlayState: HeaderUnderlayState): HazeStyle = OpticalStyle

        /**
         * Preserves backwards compatibility while returning the clean optical style.
         */
        fun adaptiveStyle(
            containerColor: Color = BaseColor,
            blurRadius: Dp = BlurRadius,
            noiseFactor: Float = NoiseFactor
        ): HazeStyle = OpticalStyle

        val Style: HazeStyle
            get() = OpticalStyle

        /**
         * Precision 3-Tier Gentle Progressive Gradient Mask (BlendMode.DstIn).
         * Harmonized across Background abu-abu, Kontainer putih, dan Foto:
         * - Tier 1 (Bagian Atas / Status bar 0.00f - 0.40f): TEBAL (0.90f - 0.82f) agar jam & status bar kontras & terbaca.
         * - Tier 2 (Bagian Tengah / Kontrol & Judul 0.40f - 0.70f): SEMI-TEBAL (0.80f turun melandai ke 0.45f).
         * - Tier 3 (Bagian Bawah / Bibir Kaca 0.70f - 1.00f): LANDAI & TIPIS (0.45f turun bertahap 0.20f -> 0.05f -> 0f),
         *   sehingga konten yang masuk tidak terpotong garis tegas dan melebur mulus.
         */
        val ProgressiveGradientBrush: Brush = Brush.verticalGradient(
            0.00f to Color.Black.copy(alpha = 0.90f),
            0.40f to Color.Black.copy(alpha = 0.82f),
            0.55f to Color.Black.copy(alpha = 0.65f),
            0.70f to Color.Black.copy(alpha = 0.45f),
            0.82f to Color.Black.copy(alpha = 0.22f),
            0.92f to Color.Black.copy(alpha = 0.07f),
            0.98f to Color.Black.copy(alpha = 0.015f),
            1.00f to Color.Transparent
        )

        /**
         * Deprecated placeholder kept for backward compatibility (evaluates to fully transparent).
         */
        fun dynamicTextDiffusionBrush(
            containerColor: Color = BaseColor,
            alphaMultiplier: Float = 0f
        ): Brush = Brush.verticalGradient(
            0.0f to Color.Transparent,
            1.0f to Color.Transparent
        )

        /**
         * Standard bottom progressive vertical fade gradient stops for floating footers/bars.
         */
        val BottomProgressiveGradientBrush: Brush = Brush.verticalGradient(
            0.0f to Color.Transparent,
            0.15f to Color.Black.copy(alpha = 0.15f),
            0.3f to Color.Black.copy(alpha = 0.4f),
            0.5f to Color.Black.copy(alpha = 0.7f),
            0.7f to Color.Black.copy(alpha = 0.9f),
            1.0f to Color.Black
        )
    }

    /**
     * Standard Circular Button Glass Specifications (Glossy Frosted White Glass Buttons)
     */
    object Button {
        val Size: Dp = 40.dp
        val BaseColor: Color = Color.White
        val OverlayBackground: Color = Color.White.copy(alpha = 0.90f)
        val BorderColor: Color = Color.White.copy(alpha = 0.85f)
        val BorderWidth: Dp = 0.75.dp
        const val TintAlpha: Float = 0.80f
        val BlurRadius: Dp = 16.dp
        const val NoiseFactor: Float = 0f

        val Style: HazeStyle = HazeStyle(
            backgroundColor = BaseColor.copy(alpha = 0.85f),
            tint = HazeTint(Color.White.copy(alpha = TintAlpha)),
            blurRadius = BlurRadius,
            noiseFactor = NoiseFactor
        )
    }

    /**
     * Standard Floating Chip / Tab Glass Specifications
     */
    object Chip {
        val Height: Dp = 36.dp
        val CornerRadius: Dp = 24.dp
        val BaseColor: Color = Color.White
        val InactiveBackground: Color = Color.Black.copy(alpha = 0.03f)
        val BorderColor: Color = Color.Black.copy(alpha = 0.08f)
        val BorderWidth: Dp = 0.5.dp
        const val TintAlpha: Float = 0.25f
        val BlurRadius: Dp = 16.dp

        val Style: HazeStyle = HazeStyle(
            backgroundColor = BaseColor,
            tint = HazeTint(Color.White.copy(alpha = TintAlpha)),
            blurRadius = BlurRadius,
            noiseFactor = 0f
        )
    }

    /**
     * Standard Card / Surface Glass Specifications
     */
    object Surface {
        val CornerRadius: Dp = 16.dp
        val BaseColor: Color = Color.White
        const val TintAlpha: Float = 0.25f
        val BlurRadius: Dp = 20.dp

        val Style: HazeStyle = HazeStyle(
            backgroundColor = BaseColor,
            tint = HazeTint(Color.White.copy(alpha = TintAlpha)),
            blurRadius = BlurRadius,
            noiseFactor = 0f
        )
    }

    /**
     * Standard Instagram / iOS Floating Glass Bottom Navigation Specifications
     */
    object BottomNav {
        val Height: Dp = 62.dp
        val CornerRadius: Dp = 32.dp
        val BaseColor: Color = Color.White
        val BorderColor: Color = Color.Black.copy(alpha = 0.08f)
        val BorderWidth: Dp = 0.5.dp
        const val TintAlpha: Float = 0.35f
        val BlurRadius: Dp = 24.dp

        val Style: HazeStyle = HazeStyle(
            backgroundColor = BaseColor.copy(alpha = 0.85f),
            tint = HazeTint(Color.White.copy(alpha = TintAlpha)),
            blurRadius = BlurRadius,
            noiseFactor = 0f
        )
    }
}

/**
 * Represents the type of content currently passing under the dynamic glass header.
 * Controls the 3 adaptive transitions:
 * 1. TEXT: Text directly on background -> melts text into canvas color (#E5E7EB)
 * 2. PHOTO: Photos, banners, images -> pure optical blur + rim specular sheen ("kilap transisi", ZERO FOG)
 * 3. CONTAINER: White surfaces/cards -> smooth edge melting
 */
enum class HeaderUnderlayState {
    TEXT,
    PHOTO,
    CONTAINER
}

/**
 * Unified Haze Mode for the application:
 * - PHOTO_MODE: 100% pure optical blur (HazeTint 0.0f), crystal clear, airy, ZERO fog/chalk.
 *   Used for images, banners, card footers, and white containers.
 * - CONTENT_MODE: Frosted blur (HazeTint 0.78f) designed specifically to absorb dark text
 *   over gray backgrounds into the glass canvas.
 */
enum class HazeMode {
    PHOTO_MODE,
    CONTENT_MODE
}

/**
 * Configuration holder for unified Haze styling.
 */
data class HazeConfig(
    val mode: HazeMode = HazeMode.CONTENT_MODE,
    val photoRatio: Float = 0f,
    val textMeltingRatio: Float = 1f,
    val baseColor: Color = GlassmorphismTheme.Header.BaseColor
) {
    /**
     * Resolves the effective HazeStyle based on the unified mode and ratios.
     */
    val effectiveStyle: HazeStyle
        get() = GlassmorphismTheme.Header.dynamicStyle(
            photoRatio = photoRatio,
            textMeltingRatio = textMeltingRatio,
            baseColor = baseColor
        )

    companion object {
        val Default = HazeConfig()
        val Photo = HazeConfig(mode = HazeMode.PHOTO_MODE, photoRatio = 1f, textMeltingRatio = 0f)
        val Content = HazeConfig(mode = HazeMode.CONTENT_MODE, photoRatio = 0f, textMeltingRatio = 1f)

        /**
         * Resolves HazeMode and ratios from the underlying HeaderUnderlayState.
         */
        fun fromUnderlayState(
            underlayState: HeaderUnderlayState,
            photoRatio: Float = if (underlayState == HeaderUnderlayState.PHOTO) 1f else 0f,
            textMeltingRatio: Float = if (underlayState == HeaderUnderlayState.TEXT) 1f else 0f,
            baseColor: Color = GlassmorphismTheme.Header.BaseColor
        ): HazeConfig {
            val mode = if (underlayState == HeaderUnderlayState.PHOTO) HazeMode.PHOTO_MODE else HazeMode.CONTENT_MODE
            return HazeConfig(
                mode = mode,
                photoRatio = photoRatio,
                textMeltingRatio = textMeltingRatio,
                baseColor = baseColor
            )
        }
    }
}

/**
 * Unified CompositionLocal for ambient Haze configuration across the app hierarchy.
 */
val LocalHazeConfig: ProvidableCompositionLocal<HazeConfig> = compositionLocalOf {
    HazeConfig.Default
}

/**
 * Helper to provide a unified HazeConfig to a Composable sub-tree.
 */
@Composable
fun ProvideHazeConfig(
    config: HazeConfig,
    content: @Composable () -> Unit
) {
    CompositionLocalProvider(LocalHazeConfig provides config) {
        content()
    }
}

/**
 * Extension modifier to apply the standardized Diajak 100% Pure Optical Progressive Glass Header effect.
 * Pure optical Haze blur with progressive fade (DstIn) without ANY manual paint spray or color overlay layers.
 */
fun Modifier.diajakGlassHeaderEffect(
    hazeState: HazeState,
    style: HazeStyle = GlassmorphismTheme.Header.UniversalStyle,
    underlayState: HeaderUnderlayState = HeaderUnderlayState.TEXT,
    textMeltingFactor: Float = 0f,
    photoGlowFactor: Float = 0f,
    containerMeltingFactor: Float = 0f,
    containerColor: Color = GlassmorphismTheme.Header.BaseColor
): Modifier = this
    .graphicsLayer { compositingStrategy = CompositingStrategy.Offscreen }
    .drawWithContent {
        // 1. Render pure optical Haze blur from content passing underneath
        drawContent()

        // 2. Coupled Progressive Dissolver (Dissolves text & containers along the exact same gradient slope)
        // When over PHOTO: the brush is completely transparent, giving 100% crystal clear blur without any fog!
        drawRect(
            brush = GlassmorphismTheme.Header.progressiveDissolverBrush(underlayState)
        )

        // 3. Precision bottom edge feathering (DstIn) without harsh cutoff
        drawRect(
            brush = GlassmorphismTheme.Header.ProgressiveGradientBrush,
            blendMode = BlendMode.DstIn
        )
    }
    .hazeEffect(
        state = hazeState,
        style = style
    )

/**
 * Extension modifier to apply the standardized Frosted Glass Circular/Pill Button styling.
 */
fun Modifier.diajakGlassButton(
    hazeState: HazeState,
    shape: Shape = CircleShape,
    elevation: Dp = 2.dp
): Modifier = this
    .shadow(
        elevation = elevation,
        shape = shape,
        clip = false,
        ambientColor = Color.Black.copy(alpha = 0.08f),
        spotColor = Color.Black.copy(alpha = 0.12f)
    )
    .clip(shape)
    .background(GlassmorphismTheme.Button.OverlayBackground)
    .hazeEffect(
        state = hazeState,
        style = GlassmorphismTheme.Button.Style
    )
    .border(
        width = GlassmorphismTheme.Button.BorderWidth,
        color = GlassmorphismTheme.Button.BorderColor,
        shape = shape
    )

/**
 * Extension modifier to apply the standardized Glass Surface effect to arbitrary shapes.
 */
fun Modifier.diajakGlassSurface(
    hazeState: HazeState,
    style: HazeStyle = GlassmorphismTheme.Surface.Style,
    shape: Shape = RoundedCornerShape(16.dp)
): Modifier = this
    .clip(shape)
    .hazeEffect(
        state = hazeState,
        style = style
    )

/**
 * Extension modifier to apply the standardized Diajak iOS-Style Progressive Glass Bottom Bar effect.
 * Includes the smooth progressive fade gradient from bottom (DstIn) and Haze blur.
 */
fun Modifier.diajakGlassBottomEffect(hazeState: HazeState): Modifier = this
    .graphicsLayer { compositingStrategy = CompositingStrategy.Offscreen }
    .drawWithContent {
        drawContent()
        drawRect(
            brush = GlassmorphismTheme.Header.BottomProgressiveGradientBrush,
            blendMode = BlendMode.DstIn
        )
    }
    .hazeEffect(
        state = hazeState,
        style = GlassmorphismTheme.Header.Style
    )

/**
 * Extension modifier to softly dissolve / fade out content near the top edge.
 * Prevents scrolling text from colliding harshly with header text while keeping
 * the glass header 100% crystal clear (low tint).
 */
fun Modifier.diajakTopContentFade(
    fadeHeight: Dp = 48.dp
): Modifier = this
    .graphicsLayer { compositingStrategy = CompositingStrategy.Offscreen }
    .drawWithContent {
        drawContent()
        val fadePx = fadeHeight.toPx()
        drawRect(
            brush = Brush.verticalGradient(
                0.0f to Color.Transparent,
                fadePx to Color.Black,
                startY = 0f,
                endY = fadePx
            ),
            blendMode = BlendMode.DstIn
        )
    }


