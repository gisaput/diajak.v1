package com.example.ui.components

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
import dev.chrisbanes.haze.hazeSource

import com.example.ui.theme.DiajakDesignSystem
import com.example.ui.theme.GlassmorphismTheme
import com.example.ui.theme.HeaderUnderlayState
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
    meltingColor: Color? = null,
    content: @Composable BoxScope.() -> Unit
) {
    val density = LocalDensity.current

    // Auto-detect underlay state from scroll state, lazy list state, or lazy grid state
    val autoUnderlayState by remember(lazyListState, lazyGridState, scrollState) {
        derivedStateOf {
            if (lazyListState != null) {
                val visibleItems = lazyListState.layoutInfo.visibleItemsInfo
                if (visibleItems.isEmpty() || (lazyListState.firstVisibleItemIndex == 0 && lazyListState.firstVisibleItemScrollOffset == 0)) {
                    HeaderUnderlayState.PHOTO
                } else {
                    val headerHeightPx = with(density) { 80.dp.toPx() }
                    val headerItem = visibleItems.firstOrNull { item ->
                        item.offset <= headerHeightPx && (item.offset + item.size) > 0
                    } ?: visibleItems.first()
                    val detectedType = (headerItem.contentType as? HeaderUnderlayState)
                        ?: if (headerItem.index == 0) HeaderUnderlayState.TEXT else HeaderUnderlayState.PHOTO
                    // Keep TEXT state (glossy sheen) until the photo or container item's top edge touches the top of the screen
                    if ((detectedType == HeaderUnderlayState.PHOTO || detectedType == HeaderUnderlayState.CONTAINER) && headerItem.offset > 0) {
                        HeaderUnderlayState.TEXT
                    } else {
                        detectedType
                    }
                }
            } else if (lazyGridState != null) {
                val visibleItems = lazyGridState.layoutInfo.visibleItemsInfo
                if (visibleItems.isEmpty() || (lazyGridState.firstVisibleItemIndex == 0 && lazyGridState.firstVisibleItemScrollOffset == 0)) {
                    HeaderUnderlayState.PHOTO
                } else {
                    val headerHeightPx = with(density) { 80.dp.toPx() }
                    val headerItem = visibleItems.firstOrNull { item ->
                        item.offset.y <= headerHeightPx && (item.offset.y + item.size.height) > 0
                    } ?: visibleItems.first()
                    val detectedType = (headerItem.contentType as? HeaderUnderlayState)
                        ?: if (headerItem.index == 0) HeaderUnderlayState.TEXT else HeaderUnderlayState.PHOTO
                    // Keep TEXT state (glossy sheen) until the photo or container item's top edge touches the top of the screen
                    if ((detectedType == HeaderUnderlayState.PHOTO || detectedType == HeaderUnderlayState.CONTAINER) && headerItem.offset.y > 0) {
                        HeaderUnderlayState.TEXT
                    } else {
                        detectedType
                    }
                }
            } else if (scrollState != null) {
                val scrollVal = scrollState.value
                val textThresholdPx = with(density) { 90.dp.toPx() }
                val containerThresholdPx = with(density) { 340.dp.toPx() }
                if (scrollVal in 1..(textThresholdPx.toInt())) {
                    HeaderUnderlayState.TEXT
                } else if (scrollVal > containerThresholdPx) {
                    HeaderUnderlayState.CONTAINER
                } else {
                    HeaderUnderlayState.PHOTO
                }
            } else {
                HeaderUnderlayState.PHOTO
            }
        }
    }

    val animatedTextMelting by animateFloatAsState(
        targetValue = when (autoUnderlayState) {
            HeaderUnderlayState.TEXT -> 1f
            HeaderUnderlayState.CONTAINER -> 0.18f
            HeaderUnderlayState.PHOTO -> 0f
        },
        animationSpec = tween(durationMillis = 180, easing = LinearOutSlowInEasing),
        label = "autoTextMelting"
    )

    val animatedMeltingColor by animateColorAsState(
        targetValue = when (autoUnderlayState) {
            HeaderUnderlayState.CONTAINER -> Color.White
            HeaderUnderlayState.TEXT -> containerColor
            HeaderUnderlayState.PHOTO -> containerColor
        },
        animationSpec = tween(durationMillis = 180, easing = LinearOutSlowInEasing),
        label = "autoMeltingColor"
    )

    val animatedPhotoGlow by animateFloatAsState(
        targetValue = when (autoUnderlayState) {
            HeaderUnderlayState.PHOTO -> 1f
            HeaderUnderlayState.CONTAINER -> 0.45f
            HeaderUnderlayState.TEXT -> 0f
        },
        animationSpec = tween(durationMillis = 180, easing = LinearOutSlowInEasing),
        label = "autoPhotoGlow"
    )

    val effectiveTextMelting = textMeltingFactor ?: animatedTextMelting
    val effectivePhotoGlow = photoGlowFactor ?: animatedPhotoGlow
    val effectiveMeltingColor = meltingColor ?: animatedMeltingColor

    val effectiveStyle = style ?: remember(effectiveMeltingColor) {
        GlassmorphismTheme.Header.adaptiveStyle(containerColor = effectiveMeltingColor)
    }

    Box(modifier = modifier.fillMaxWidth()) {
        // Layer 1: Dynamic Haze Optical Glass Effect with 3 Adaptive Transitions & 3-Tier Gentle Curve
        Box(
            modifier = Modifier
                .matchParentSize()
                .diajakGlassHeaderEffect(
                    hazeState = hazeState,
                    style = effectiveStyle,
                    textMeltingFactor = effectiveTextMelting,
                    photoGlowFactor = effectivePhotoGlow,
                    containerColor = effectiveMeltingColor
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
        photoGlowFactor = photoGlowFactor
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
                    horizontalArrangement = Arrangement.spacedBy(8.dp)
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
