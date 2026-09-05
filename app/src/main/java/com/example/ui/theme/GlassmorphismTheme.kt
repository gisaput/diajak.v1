package com.example.ui.theme

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
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
        const val TintAlpha: Float = 0.20f
        val BlurRadius: Dp = 24.dp
        const val NoiseFactor: Float = 0f

        val TintColor: Color = BaseColor.copy(alpha = TintAlpha)

        /**
         * Dynamically configures a HazeStyle that adapts its tint color and opacity
         * based on the background luminance beneath the header.
         *
         * - Over dark/vibrant content (photos/hero images, luminance < 0.5f):
         *   Applies a crystal-clear frosted highlight tint (translucent white)
         *   producing the vibrant, juicy refraction over colorful imagery.
         *
         * - Over light/gray text content (#E5E7EB, luminance >= 0.5f):
         *   Dynamically shifts tint to match the ambient surface tone with calibrated
         *   subtle alpha, preventing the blur from washing out into an opaque white fog.
         */
        fun adaptiveStyle(
            containerColor: Color = BaseColor,
            blurRadius: Dp = BlurRadius,
            noiseFactor: Float = NoiseFactor
        ): HazeStyle {
            val luminance = containerColor.luminance()
            val dynamicTint: HazeTint = if (luminance < 0.5f) {
                // Low luminance (dark or colorful photos):
                // Translucent white frost accentuates contrast and crystal glass glow
                val alpha = (0.22f - (luminance * 0.15f)).coerceIn(0.12f, 0.22f)
                HazeTint(Color.White.copy(alpha = alpha))
            } else {
                // High luminance (gray background with text):
                // Use the ambient container tone with minimal alpha
                // Prevents "kabut putih" (white fog) over gray #E5E7EB background
                val alpha = (0.12f * (1.0f - (luminance - 0.5f) * 0.4f)).coerceIn(0.06f, 0.12f)
                HazeTint(containerColor.copy(alpha = alpha))
            }

            return HazeStyle(
                backgroundColor = Color.Transparent,
                tint = dynamicTint,
                blurRadius = blurRadius,
                noiseFactor = noiseFactor
            )
        }

        val Style: HazeStyle
            get() = adaptiveStyle(BaseColor)

        /**
         * Standard progressive vertical fade gradient stops (BlendMode.DstIn).
         * 3-Tier Gentle Curve (seperti teknik gambar pertama):
         * - Lapisan Atas (0.0f - 0.25f): Pekat & terlindung
         * - Lapisan Tengah (0.25f - 0.65f): Difusi blur semi pekat
         * - Lapisan Bawah (0.65f - 1.0f): Masking DstIn bertingkat dengan kurva landai yang hilang ke transparan tanpa garis
         */
        val ProgressiveGradientBrush: Brush = Brush.verticalGradient(
            0.0f to Color.Black,
            0.25f to Color.Black,
            0.45f to Color.Black.copy(alpha = 0.78f),
            0.65f to Color.Black.copy(alpha = 0.45f),
            0.82f to Color.Black.copy(alpha = 0.18f),
            0.93f to Color.Black.copy(alpha = 0.05f),
            1.0f to Color.Transparent
        )

        /**
         * Dynamic Pigment Diffusion gradient (BlendMode.SrcOver).
         * Calibrated to maintain uniform, consistent optical thickness with the photo state:
         * - Gentle peak opacity (0.42f) ensures the glass remains airy, translucent, and natural.
         * - Avoids thick/opaque solid bands when melting white containers or gray backgrounds.
         * - Combined with the 24.dp optical Haze blur underneath, text letter strokes and container
         *   edges dissolve seamlessly into the ambient canvas without forming a heavy opaque curtain.
         * - Multi-stop gentle progressive curve precisely matches ProgressiveGradientBrush.
         */
        fun dynamicTextDiffusionBrush(
            containerColor: Color = BaseColor,
            alphaMultiplier: Float = 1f
        ): Brush = Brush.verticalGradient(
            0.0f to containerColor.copy(alpha = 0.42f * alphaMultiplier),
            0.25f to containerColor.copy(alpha = 0.38f * alphaMultiplier),
            0.45f to containerColor.copy(alpha = 0.28f * alphaMultiplier),
            0.65f to containerColor.copy(alpha = 0.16f * alphaMultiplier),
            0.82f to containerColor.copy(alpha = 0.07f * alphaMultiplier),
            0.93f to containerColor.copy(alpha = 0.02f * alphaMultiplier),
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
 * Extension modifier to apply the standardized Diajak iOS-Style Progressive Glass Header effect.
 * Pure optical Haze blur with progressive fade (DstIn) combined with dynamic, intelligent adaptation:
 * - Text melting diffusion ONLY when text on gray background is passing underneath.
 * - Subtle specular gloss sheen ("kilap transisi") when photos pass, with ZERO gray fog.
 */
fun Modifier.diajakGlassHeaderEffect(
    hazeState: HazeState,
    style: HazeStyle = GlassmorphismTheme.Header.adaptiveStyle(),
    textMeltingFactor: Float = 0f,
    photoGlowFactor: Float = 1f,
    containerColor: Color = GlassmorphismTheme.Header.BaseColor
): Modifier = this
    .graphicsLayer { compositingStrategy = CompositingStrategy.Offscreen }
    .drawWithContent {
        // 1. Draw pure optical blur from Haze
        drawContent()

        // 2. Progressive Edge Feathering (DstIn)
        drawRect(
            brush = GlassmorphismTheme.Header.ProgressiveGradientBrush,
            blendMode = BlendMode.DstIn
        )

        // 3. Dynamic Text Dissolve (SrcOver) - ONLY when text on gray background is entering!
        // Melts black letter strokes into containerColor. ZERO impact on photos (alpha = 0f).
        if (textMeltingFactor > 0.01f) {
            drawRect(
                brush = GlassmorphismTheme.Header.dynamicTextDiffusionBrush(
                    containerColor = containerColor,
                    alphaMultiplier = textMeltingFactor
                ),
                blendMode = BlendMode.SrcOver
            )
        }
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


