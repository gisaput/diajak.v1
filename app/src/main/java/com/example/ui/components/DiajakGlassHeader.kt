package com.example.ui.components

import com.example.ui.theme.AppSpacing

import androidx.compose.animation.animateColorAsState
import androidx.compose.animation.core.LinearOutSlowInEasing
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.tween
import androidx.compose.foundation.ScrollState
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyListState
import androidx.compose.foundation.lazy.grid.LazyGridState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.outlined.ArrowBack
import androidx.compose.material.icons.outlined.Close
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.SheetState
import androidx.compose.material3.Text
import androidx.compose.material3.rememberModalBottomSheetState
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.zIndex
import dev.chrisbanes.haze.HazeState
import dev.chrisbanes.haze.HazeStyle
import dev.chrisbanes.haze.HazeTint
import dev.chrisbanes.haze.hazeSource

import com.example.ui.theme.DiajakDesignSystem
import com.example.ui.theme.GlassmorphismTheme
import com.example.ui.theme.HazeConfig
import com.example.ui.theme.HazeMode
import com.example.ui.theme.HeaderUnderlayState
import com.example.ui.theme.LocalHazeConfig
import com.example.ui.theme.ProvideHazeConfig
import com.example.ui.theme.diajakGlassButton
import com.example.ui.theme.diajakGlassHeaderEffect

/**
 * Centralized configuration for the glassmorphism effect to ensure
 * consistency across all screens (Explore, Detail, Privacy Policy, etc.).
 * Delegates directly to GlassmorphismTheme.
 */
object DiajakGlassConfig {
    // Main Header Glass Configuration
    val BaseColor = GlassmorphismTheme.Header.BaseColor
    val TintAlpha = GlassmorphismTheme.Header.TintAlpha
    val SolidTintOverlayAlpha = GlassmorphismTheme.Header.TintAlpha
    val BlurRadius = GlassmorphismTheme.Header.BlurRadius

    val Style = GlassmorphismTheme.Header.Style

    // Circular Button Glass Configuration (Back / Share buttons)
    val ButtonBaseColor = GlassmorphismTheme.Button.BaseColor
    val ButtonTintAlpha = GlassmorphismTheme.Button.TintAlpha
    val ButtonBlurRadius = GlassmorphismTheme.Button.BlurRadius

    val ButtonStyle = GlassmorphismTheme.Button.Style
}

/**
 * A reusable glassmorphism header container implementing the iOS-Style Glass Header.
 * Uses dev.chrisbanes.haze to dynamically blur content scrolling underneath it,
 * adapting its tint color based on background luminance with 3-tier gentle progressive curve.
 *
 * Can automatically detect underlay state if lazyListState or scrollState is provided!
 */
@Composable
fun DiajakGlassHeader(
    hazeState: HazeState,
    modifier: Modifier = Modifier,
    lazyListState: LazyListState? = null,
    lazyGridState: LazyGridState? = null,
    scrollState: ScrollState? = null,
    containerColor: Color = GlassmorphismTheme.Header.BaseColor,
    style: HazeStyle? = null,
    textMeltingFactor: Float? = null,
    photoGlowFactor: Float? = null,
    containerMeltingFactor: Float? = null,
    meltingColor: Color? = null,
    content: @Composable BoxScope.() -> Unit
) {
    val effectiveBaseColor = meltingColor ?: containerColor
    val effectiveStyle = style ?: GlassmorphismTheme.Header.OpticalStyle

    Box(modifier = modifier.fillMaxWidth()) {
        // Layer 1: Universal Pure Optical Glass Header
        Box(
            modifier = Modifier
                .matchParentSize()
                .diajakGlassHeaderEffect(
                    hazeState = hazeState,
                    style = effectiveStyle,
                    containerColor = effectiveBaseColor
                )
        )
        
        // Layer 2: Content Layer
        content()
    }
}

/**
 * Universal iOS-Style Glass Header with standard Back button, Title, and Actions.
 * Drop-in replacement for any screen header to enforce 100% design consistency globally.
 */
@Composable
fun DiajakUniversalHeader(
    title: String? = null,
    onBack: (() -> Unit)? = null,
    hazeState: HazeState,
    modifier: Modifier = Modifier,
    lazyListState: LazyListState? = null,
    lazyGridState: LazyGridState? = null,
    scrollState: ScrollState? = null,
    textMeltingFactor: Float? = null,
    photoGlowFactor: Float? = null,
    containerMeltingFactor: Float? = null,
    containerColor: Color = GlassmorphismTheme.Header.BaseColor,
    actions: @Composable (RowScope.() -> Unit)? = null
) {
    DiajakGlassHeader(
        hazeState = hazeState,
        modifier = modifier.fillMaxWidth().zIndex(10f),
        lazyListState = lazyListState,
        lazyGridState = lazyGridState,
        scrollState = scrollState,
        containerColor = containerColor,
        textMeltingFactor = textMeltingFactor,
        photoGlowFactor = photoGlowFactor,
        containerMeltingFactor = containerMeltingFactor
    ) {
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .statusBarsPadding()
                .height(DiajakDesignSystem.Header.HeaderContentHeight)
                .padding(horizontal = 20.dp)
        ) {
            if (onBack != null) {
                IconButton(
                    onClick = onBack,
                    modifier = Modifier
                        .size(40.dp)
                        .align(Alignment.CenterStart)
                        .diajakGlassButton(hazeState)
                ) {
                    Icon(
                        imageVector = Icons.AutoMirrored.Outlined.ArrowBack,
                        contentDescription = "Kembali",
                        tint = MaterialTheme.colorScheme.onSurface,
                        modifier = Modifier.size(24.dp)
                    )
                }
            }

            if (title != null) {
                Text(
                    text = title,
                    style = DiajakDesignSystem.Typography.Headline,
                    color = MaterialTheme.colorScheme.onSurface,
                    textAlign = TextAlign.Center,
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis,
                    modifier = Modifier
                        .align(Alignment.Center)
                        .fillMaxWidth()
                        .padding(horizontal = if (onBack != null || actions != null) 48.dp else 0.dp)
                )
            }

            if (actions != null) {
                Row(
                    modifier = Modifier.align(Alignment.CenterEnd),
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.spacedBy(AppSpacing.Small)
                ) {
                    actions()
                }
            }
        }
    }
}

/**
 * Universal Glass Bottom Sheet wrapper.
 * Automatically wraps bottom sheet content in hazeSource with an optical DiajakGlassHeader on top!
 */
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun DiajakGlassBottomSheet(
    onDismissRequest: () -> Unit,
    title: String? = null,
    sheetState: SheetState = rememberModalBottomSheetState(skipPartiallyExpanded = true),
    modifier: Modifier = Modifier,
    containerColor: Color = Color(0xFFE5E7EB),
    actions: @Composable (RowScope.() -> Unit)? = null,
    content: @Composable BoxScope.(HazeState) -> Unit
) {
    val hazeState = remember { HazeState() }

    ModalBottomSheet(
        onDismissRequest = onDismissRequest,
        sheetState = sheetState,
        containerColor = containerColor,
        contentWindowInsets = { WindowInsets(0, 0, 0, 0) },
        dragHandle = null,
        modifier = modifier
    ) {
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .fillMaxHeight(0.88f)
        ) {
            // Scrollable content layer connected to hazeSource
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .hazeSource(state = hazeState)
            ) {
                content(hazeState)
            }

            // Glass Header with Drag Handle & Title
            DiajakGlassHeader(
                hazeState = hazeState,
                modifier = Modifier
                    .align(Alignment.TopCenter)
                    .zIndex(10f)
            ) {
                Column(modifier = Modifier.fillMaxWidth()) {
                    // Sleek drag handle pill
                    Box(
                        modifier = Modifier
                            .align(Alignment.CenterHorizontally)
                            .padding(top = 10.dp, bottom = 4.dp)
                            .width(36.dp)
                            .height(4.dp)
                            .background(Color(0xFFCBD5E1), CircleShape)
                    )

                    Box(
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(52.dp)
                            .padding(horizontal = 20.dp)
                    ) {
                        if (title != null) {
                            Text(
                                text = title,
                                style = DiajakDesignSystem.Typography.Headline,
                                color = MaterialTheme.colorScheme.onSurface,
                                textAlign = TextAlign.Center,
                                modifier = Modifier
                                    .align(Alignment.Center)
                                    .fillMaxWidth()
                            )
                        }

                        // Close button on right
                        IconButton(
                            onClick = onDismissRequest,
                            modifier = Modifier
                                .size(36.dp)
                                .align(Alignment.CenterEnd)
                                .diajakGlassButton(hazeState)
                        ) {
                            Icon(
                                imageVector = Icons.Outlined.Close,
                                contentDescription = "Tutup",
                                tint = MaterialTheme.colorScheme.onSurface,
                                modifier = Modifier.size(20.dp)
                            )
                        }

                        if (actions != null) {
                            Row(
                                modifier = Modifier.align(Alignment.CenterStart),
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                actions()
                            }
                        }
                    }
                }
            }
        }
    }
}

/**
 * CompositionLocal untuk HazeState aktif agar tombol dan komponen anak
 * dapat menggunakan modifier .diajakGlassButton() tanpa passing parameter manual.
 */
val LocalDiajakHazeState: ProvidableCompositionLocal<HazeState?> = compositionLocalOf { null }

/**
 * Universal Glass Scaffold:
 * Solusi arsitektur untuk menerapkan iOS-Style Pure Optical Glass Header
 * ke SELURUH LAYAR aplikasi secara otomatis tanpa perlu implementasi manual satu per satu.
 *
 * Keunggulan:
 * 1. Tanpa Manual Satu per Satu: Mengelola HazeState dan .hazeSource() secara otomatis di balik layar.
 * 2. Selalu Aktif di Layar Statis: Header kaca tetap hadir dan mengaburkan latar belakang
 *    meskipun halaman tersebut statis, pendek, kosong (empty state), atau belum di-scroll.
 * 3. Konsistensi Global: Memastikan tinggi header (56.dp), statusBarsPadding(), headline typography,
 *    dan tombol kaca bulat (.diajakGlassButton) 100% konsisten dengan standar desain Diajak.
 * 4. Fleksibel: Mendukung judul tengah, tombol kembali otomatis, aksi di kanan, custom header,
 *    dan bottom bar.
 */
@Composable
fun DiajakGlassScaffold(
    modifier: Modifier = Modifier,
    title: String? = null,
    onBack: (() -> Unit)? = null,
    actions: @Composable (RowScope.() -> Unit)? = null,
    customHeader: (@Composable BoxScope.(HazeState) -> Unit)? = null,
    bottomBar: @Composable () -> Unit = {},
    containerColor: Color = Color(0xFFE5E7EB),
    content: @Composable BoxScope.(HazeState) -> Unit
) {
    val hazeState = remember { HazeState() }

    CompositionLocalProvider(LocalDiajakHazeState provides hazeState) {
        Box(
            modifier = modifier
                .fillMaxSize()
                .background(containerColor)
        ) {
            // Layer 1: Content Layer (Otomatis terkoneksi ke hazeSource)
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .hazeSource(state = hazeState)
            ) {
                content(hazeState)
            }

            // Layer 2: Universal Pure Optical Glass Header Layer (Selalu Aktif bahkan di layar statis!)
            if (customHeader != null) {
                DiajakGlassHeader(
                    hazeState = hazeState,
                    modifier = Modifier
                        .align(Alignment.TopCenter)
                        .zIndex(10f)
                ) {
                    customHeader(hazeState)
                }
            } else if (title != null || onBack != null || actions != null) {
                DiajakUniversalHeader(
                    title = title,
                    onBack = onBack,
                    hazeState = hazeState,
                    containerColor = containerColor,
                    actions = actions,
                    modifier = Modifier.align(Alignment.TopCenter)
                )
            } else {
                // Default Minimal Transparent Glass Header (Status bar blur overlay - seperti Beranda)
                DiajakGlassHeader(
                    hazeState = hazeState,
                    modifier = Modifier
                        .align(Alignment.TopCenter)
                        .zIndex(10f)
                ) {
                    Box(
                        modifier = Modifier
                            .fillMaxWidth()
                            .statusBarsPadding()
                            .height(DiajakDesignSystem.Header.HeaderContentHeight)
                    )
                }
            }

            // Layer 3: Bottom Bar
            Box(
                modifier = Modifier
                    .align(Alignment.BottomCenter)
                    .zIndex(99f)
            ) {
                bottomBar()
            }
        }
    }
}

