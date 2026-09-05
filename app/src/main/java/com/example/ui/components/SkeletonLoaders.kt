package com.example.ui.components

import androidx.compose.animation.core.FastOutSlowInEasing
import androidx.compose.animation.core.RepeatMode
import androidx.compose.animation.core.animateFloat
import androidx.compose.animation.core.infiniteRepeatable
import androidx.compose.animation.core.rememberInfiniteTransition
import androidx.compose.animation.core.tween
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Card
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.composed
import androidx.compose.ui.draw.clip
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Shape
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp

/**
 * Animated Shimmer Modifier for Skeleton Loading.
 * Creates a smooth, infinite sliding highlight over a soft neutral background.
 */
fun Modifier.shimmerEffect(
    shape: Shape = RoundedCornerShape(8.dp),
    shimmerColors: List<Color> = listOf(
        Color(0xFFE2E8F0),
        Color(0xFFF1F5F9),
        Color(0xFFE2E8F0)
    )
): Modifier = composed {
    val transition = rememberInfiniteTransition(label = "shimmerTransition")
    val translateAnim by transition.animateFloat(
        initialValue = 0f,
        targetValue = 1000f,
        animationSpec = infiniteRepeatable(
            animation = tween(durationMillis = 1200, easing = FastOutSlowInEasing),
            repeatMode = RepeatMode.Restart
        ),
        label = "shimmerTranslation"
    )

    val brush = Brush.linearGradient(
        colors = shimmerColors,
        start = Offset(translateAnim - 300f, translateAnim - 300f),
        end = Offset(translateAnim, translateAnim)
    )

    this
        .clip(shape)
        .background(brush)
}

/**
 * Basic Skeleton Box primitive.
 */
@Composable
fun SkeletonBox(
    modifier: Modifier = Modifier,
    shape: Shape = RoundedCornerShape(8.dp)
) {
    Box(
        modifier = modifier.shimmerEffect(shape = shape)
    )
}

/**
 * Basic Skeleton Text Line primitive.
 */
@Composable
fun SkeletonText(
    modifier: Modifier = Modifier,
    width: Dp = 120.dp,
    height: Dp = 14.dp,
    shape: Shape = RoundedCornerShape(4.dp)
) {
    Box(
        modifier = modifier
            .width(width)
            .height(height)
            .shimmerEffect(shape = shape)
    )
}

/**
 * Category Chips Row Skeleton.
 * Matches exact chip padding and height for layout stability.
 */
@Composable
fun SkeletonCategoryChipsRow(
    modifier: Modifier = Modifier,
    itemCount: Int = 5
) {
    LazyRow(
        modifier = modifier.fillMaxWidth(),
        contentPadding = PaddingValues(horizontal = 20.dp),
        horizontalArrangement = Arrangement.spacedBy(8.dp)
    ) {
        items(itemCount) { index ->
            val chipWidth = when (index % 3) {
                0 -> 90.dp
                1 -> 110.dp
                else -> 80.dp
            }
            SkeletonBox(
                modifier = Modifier
                    .width(chipWidth)
                    .height(36.dp),
                shape = RoundedCornerShape(16.dp)
            )
        }
    }
}

/**
 * Hero Banner Skeleton Loader.
 * Matches top promo banner in HomeScreen.
 */
@Composable
fun SkeletonBanner(
    modifier: Modifier = Modifier
) {
    Card(
        modifier = modifier
            .fillMaxWidth()
            .height(160.dp),
        shape = RoundedCornerShape(16.dp),
        colors = CardDefaults.cardColors(containerColor = Color.White),
        border = BorderStroke(0.dp, Color.Transparent),
        elevation = CardDefaults.cardElevation(defaultElevation = 0.dp)
    ) {
        Box(
            modifier = Modifier.fillMaxWidth().height(160.dp).shimmerEffect(shape = RoundedCornerShape(16.dp))
        )
    }
}

/**
 * Horizontal Activity Card Skeleton (for 160.dp wide carousels).
 * Strictly follows AGENTS.md card guidelines for 16.dp corner radius and consistent border/elevation.
 */
@Composable
fun SkeletonActivityCardHorizontal(
    modifier: Modifier = Modifier,
    cardWidth: androidx.compose.ui.unit.Dp? = null
) {
    val screenWidthDp = androidx.compose.ui.platform.LocalConfiguration.current.screenWidthDp.dp
    val effectiveWidth = cardWidth ?: ((screenWidthDp - 40.dp - 14.dp) / 2)
    Column(
        modifier = modifier.width(effectiveWidth)
    ) {
        // Card Image Container
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .height(130.dp)
                .background(Color.White, RoundedCornerShape(16.dp))
                .border(BorderStroke(0.dp, Color.Transparent), RoundedCornerShape(16.dp))
                .clip(RoundedCornerShape(16.dp))
        ) {
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(130.dp)
                    .shimmerEffect(shape = RoundedCornerShape(16.dp))
            )
        }

        Spacer(modifier = Modifier.height(8.dp))

        // Title Line 1
        SkeletonText(
            width = 130.dp,
            height = 14.dp,
            shape = RoundedCornerShape(4.dp)
        )

        Spacer(modifier = Modifier.height(4.dp))

        // Title Line 2
        SkeletonText(
            width = 90.dp,
            height = 14.dp,
            shape = RoundedCornerShape(4.dp)
        )

        Spacer(modifier = Modifier.height(6.dp))

        // Location Line
        SkeletonText(
            width = 80.dp,
            height = 12.dp,
            shape = RoundedCornerShape(4.dp)
        )

        Spacer(modifier = Modifier.height(8.dp))

        // Price & Rating Row
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            SkeletonText(width = 56.dp, height = 14.dp)
            SkeletonText(width = 30.dp, height = 12.dp)
        }
    }
}

/**
 * Horizontal Activity Cards Carousel Skeleton.
 */
@Composable
fun SkeletonActivityCarousel(
    modifier: Modifier = Modifier,
    itemCount: Int = 2
) {
    val screenWidthDp = androidx.compose.ui.platform.LocalConfiguration.current.screenWidthDp.dp
    val cardSpacing = 14.dp
    val cardWidth = (screenWidthDp - 40.dp - cardSpacing) / 2
    LazyRow(
        modifier = modifier.fillMaxWidth(),
        contentPadding = PaddingValues(horizontal = 20.dp),
        horizontalArrangement = Arrangement.spacedBy(cardSpacing)
    ) {
        items(itemCount) {
            SkeletonActivityCardHorizontal(cardWidth = cardWidth)
        }
    }
}

/**
 * Vertical Full-Width Activity Card Skeleton.
 * Strictly adheres to AGENTS.md card rules with RoundedCornerShape(16.dp) and Color(0xFFDADCE0) border.
 */
@Composable
fun SkeletonActivityCardVertical(
    modifier: Modifier = Modifier
) {
    Card(
        modifier = modifier.fillMaxWidth(),
        shape = RoundedCornerShape(16.dp),
        colors = CardDefaults.cardColors(containerColor = Color.White),
        border = BorderStroke(0.dp, Color.Transparent),
        elevation = CardDefaults.cardElevation(defaultElevation = 0.dp)
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(12.dp)
        ) {
            // Top Image Box
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(160.dp)
                    .clip(RoundedCornerShape(12.dp))
                    .shimmerEffect(shape = RoundedCornerShape(12.dp))
            )

            Spacer(modifier = Modifier.height(12.dp))

            // Title Skeleton
            SkeletonText(width = 220.dp, height = 20.dp)

            Spacer(modifier = Modifier.height(6.dp))

            // Location & Date Skeleton
            SkeletonText(width = 160.dp, height = 12.dp)

            Spacer(modifier = Modifier.height(12.dp))

            // Footer Price & Action Row
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                SkeletonText(width = 80.dp, height = 20.dp)
                SkeletonBox(
                    modifier = Modifier.size(width = 76.dp, height = 32.dp),
                    shape = RoundedCornerShape(16.dp)
                )
            }
        }
    }
}

/**
 * List of Vertical Activity Card Skeletons.
 */
@Composable
fun SkeletonActivityList(
    modifier: Modifier = Modifier,
    itemCount: Int = 3
) {
    Column(
        modifier = modifier.fillMaxWidth(),
        verticalArrangement = Arrangement.spacedBy(20.dp)
    ) {
        repeat(itemCount) {
            SkeletonActivityCardVertical()
        }
    }
}

/**
 * DetailScreen Skeleton Loader.
 * Matches full layout of DetailScreen for zero layout shift during loading.
 */
@Composable
fun SkeletonDetailContent(
    modifier: Modifier = Modifier
) {
    Column(
        modifier = modifier.fillMaxWidth()
    ) {
        // Top Banner Skeleton (Full-bleed edge-to-edge)
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .height(360.dp)
                .shimmerEffect(shape = RoundedCornerShape(0.dp))
        )

        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 20.dp, vertical = 20.dp)
        ) {
            // Category Badge Skeleton
            SkeletonBox(
                modifier = Modifier.size(width = 80.dp, height = 24.dp),
                shape = RoundedCornerShape(12.dp)
            )

            Spacer(modifier = Modifier.height(12.dp))

            // Title Line 1
            SkeletonText(width = 280.dp, height = 22.dp)

            Spacer(modifier = Modifier.height(6.dp))

            // Title Line 2
            SkeletonText(width = 180.dp, height = 22.dp)

            Spacer(modifier = Modifier.height(20.dp))

            // Creator Row Skeleton
            Row(
                verticalAlignment = Alignment.CenterVertically
            ) {
                SkeletonBox(
                    modifier = Modifier.size(40.dp),
                    shape = CircleShape
                )
                Spacer(modifier = Modifier.width(12.dp))
                Column {
                    SkeletonText(width = 120.dp, height = 14.dp)
                    Spacer(modifier = Modifier.height(4.dp))
                    SkeletonText(width = 80.dp, height = 12.dp)
                }
            }

            Spacer(modifier = Modifier.height(20.dp))

            // Detail Accordion Title
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                SkeletonText(width = 80.dp, height = 18.dp)
                SkeletonBox(modifier = Modifier.size(24.dp), shape = CircleShape)
            }

            Spacer(modifier = Modifier.height(10.dp))

            // Detail Body Paragraph Skeleton
            Column(verticalArrangement = Arrangement.spacedBy(6.dp)) {
                SkeletonText(modifier = Modifier.fillMaxWidth(), height = 14.dp)
                SkeletonText(modifier = Modifier.fillMaxWidth(), height = 14.dp)
                SkeletonText(width = 220.dp, height = 14.dp)
            }

            Spacer(modifier = Modifier.height(20.dp))

            // Benefit Accordion Title
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                SkeletonText(width = 90.dp, height = 18.dp)
                SkeletonBox(modifier = Modifier.size(24.dp), shape = CircleShape)
            }

            Spacer(modifier = Modifier.height(10.dp))

            // Benefit Items Skeleton
            repeat(3) {
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    modifier = Modifier.padding(vertical = 4.dp)
                ) {
                    SkeletonBox(modifier = Modifier.size(20.dp), shape = CircleShape)
                    Spacer(modifier = Modifier.width(8.dp))
                    SkeletonText(width = 180.dp, height = 14.dp)
                }
            }
        }
    }
}

/**
 * Booking Item Skeleton Loader.
 * Matches Booking Card design strictly adhering to AGENTS.md.
 */
@Composable
fun SkeletonBookingCard(
    modifier: Modifier = Modifier
) {
    Card(
        modifier = modifier.fillMaxWidth(),
        shape = RoundedCornerShape(16.dp),
        colors = CardDefaults.cardColors(containerColor = Color.White),
        border = BorderStroke(0.dp, Color.Transparent),
        elevation = CardDefaults.cardElevation(defaultElevation = 0.dp)
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(12.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            SkeletonBox(
                modifier = Modifier
                    .size(80.dp)
                    .clip(RoundedCornerShape(12.dp)),
                shape = RoundedCornerShape(12.dp)
            )
            Spacer(modifier = Modifier.width(12.dp))
            Column(
                modifier = Modifier.weight(1f)
            ) {
                SkeletonText(width = 140.dp, height = 20.dp)
                Spacer(modifier = Modifier.height(6.dp))
                SkeletonText(width = 100.dp, height = 12.dp)
                Spacer(modifier = Modifier.height(6.dp))
                SkeletonText(width = 80.dp, height = 14.dp)
            }
        }
    }
}
