package com.example.ui.theme

import android.os.Build
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.foundation.IndicationNodeFactory
import androidx.compose.foundation.interaction.InteractionSource
import androidx.compose.foundation.LocalIndication
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.LocalRippleConfiguration
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.darkColorScheme
import androidx.compose.material3.dynamicDarkColorScheme
import androidx.compose.material3.dynamicLightColorScheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.node.DelegatableNode
import androidx.compose.ui.platform.LocalContext

private object NoIndication : IndicationNodeFactory {
    override fun create(interactionSource: InteractionSource): DelegatableNode {
        return object : Modifier.Node() {}
    }
    override fun hashCode(): Int = -1
    override fun equals(other: Any?): Boolean = other === this
}

private val DarkColorScheme = darkColorScheme(
    primary = DiajakOrange,
    onPrimary = Color.White,
    primaryContainer = DiajakOrangeDark,
    onPrimaryContainer = Color.White,
    secondary = SlateLight,
    onSecondary = SlateDark,
    background = Color(0xFF111418),
    surface = Color(0xFF1B2027),
    onBackground = Color(0xFFF7F9FC),
    onSurface = Color(0xFFF7F9FC)
)

private val LightColorScheme = lightColorScheme(
    primary = DiajakOrange,
    onPrimary = Color.White,
    primaryContainer = DiajakOrangeLight,
    onPrimaryContainer = DiajakOrangeDark,
    secondary = SlateMedium,
    onSecondary = Color.White,
    background = BackgroundLight,
    surface = BackgroundLight,
    surfaceContainerLowest = Color(0xFFFFFFFF),
    surfaceContainerLow = Color(0xFFF8FAFC),
    surfaceContainer = Color(0xFFF1F5F9),
    surfaceContainerHigh = Color(0xFFE2E8F0),
    surfaceContainerHighest = Color.Transparent,
    onBackground = GoogleTextPrimary,
    onSurface = GoogleTextPrimary,
    surfaceVariant = GoogleGreyLight,
    onSurfaceVariant = GoogleTextSecondary,
    outline = GoogleGreyBorder,
    outlineVariant = GoogleGreyLight
)

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MyApplicationTheme(
    darkTheme: Boolean = false, // Force Light Theme for signature Diajak brand identity consistency and high contrast
    dynamicColor: Boolean = false, // Keep false to showcase Diajak signature Orange brand
    content: @Composable () -> Unit,
) {
    val colorScheme = when {
        dynamicColor && Build.VERSION.SDK_INT >= Build.VERSION_CODES.S -> {
            val context = LocalContext.current
            if (darkTheme) dynamicDarkColorScheme(context) else dynamicLightColorScheme(context)
        }
        darkTheme -> DarkColorScheme
        else -> LightColorScheme
    }

    CompositionLocalProvider(
        LocalAppIconSize provides AppIcons.DefaultSize,
        LocalSpacing provides Spacing(),
        LocalRippleConfiguration provides null,
        LocalIndication provides NoIndication
    ) {
        MaterialTheme(
            colorScheme = colorScheme,
            typography = Typography,
            content = content
        )
    }
}
