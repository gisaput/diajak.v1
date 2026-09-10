package com.example.ui.screens

import com.example.ui.theme.AppSpacing

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.*
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.foundation.lazy.grid.rememberLazyGridState
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.GridItemSpan
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.zIndex
import com.example.model.ActivityModel
import com.example.ui.theme.DiajakOrange
import com.example.ui.theme.DiajakTextDark
import com.example.ui.theme.RatingGold
import com.example.ui.theme.spacing
import com.example.ui.theme.DiajakDesignSystem
import com.example.ui.theme.HeaderUnderlayState
import androidx.compose.ui.platform.LocalDensity
import com.example.ui.components.SkeletonActivityCardHorizontal
import com.example.ui.components.DiajakGlassHeader
import dev.chrisbanes.haze.HazeState
import dev.chrisbanes.haze.hazeSource

@Composable
fun FavoritesScreen(
  favoriteActivities: List<ActivityModel>,
  onActivityClick: (ActivityModel) -> Unit,
  onToggleFavorite: (ActivityModel) -> Unit,
  onExploreClick: () -> Unit,
  isLoading: Boolean = false,
  userCoordinates: Pair<Double, Double>? = null
) {
  val hazeState = remember { HazeState() }
  val gridState = rememberLazyGridState()
  val density = LocalDensity.current
  val isScrolled by remember {
    derivedStateOf {
      gridState.firstVisibleItemIndex > 0 || gridState.firstVisibleItemScrollOffset > 0
    }
  }

  Box(
    modifier = Modifier
      .fillMaxSize()
      .background(Color(0xFFE5E7EB))
  ) {
    // 1. Content Layer (Always hazeSource)
    if (isLoading) {
      LazyVerticalGrid(
        columns = GridCells.Fixed(2),
        state = gridState,
        contentPadding = PaddingValues(start = 20.dp, end = 20.dp, top = 0.dp, bottom = 124.dp),
        horizontalArrangement = Arrangement.spacedBy(MaterialTheme.spacing.medium),
        verticalArrangement = Arrangement.spacedBy(AppSpacing.Medium),
        modifier = Modifier.fillMaxSize().hazeSource(state = hazeState)
      ) {
        item(span = { GridItemSpan(2) }, contentType = HeaderUnderlayState.TEXT) {
          Column(modifier = Modifier.fillMaxWidth().statusBarsPadding().padding(top = 16.dp, bottom = 0.dp)) {
            Text(
              text = "Aktivitas Favorit",
              style = DiajakDesignSystem.Typography.Headline,
              color = MaterialTheme.colorScheme.onSurface
            )
          }
        }
        items(4, contentType = { HeaderUnderlayState.PHOTO }) {
          SkeletonActivityCardHorizontal()
        }
      }
    } else if (favoriteActivities.isEmpty()) {
      Box(
        modifier = Modifier
          .fillMaxSize()
          .hazeSource(state = hazeState),
        contentAlignment = Alignment.Center
      ) {
        Column(
          horizontalAlignment = Alignment.CenterHorizontally,
          modifier = Modifier.padding(horizontal = 30.dp)
        ) {
          Icon(
            imageVector = Icons.Outlined.FavoriteBorder,
            contentDescription = null,
            modifier = Modifier.size(72.dp),
            tint = MaterialTheme.colorScheme.onSurface
          )
          Spacer(modifier = Modifier.height(MaterialTheme.spacing.medium))
          Text(
            text = "Belum Ada Aktivitas Favorit",
            style = DiajakDesignSystem.Typography.TitleBold,
            color = MaterialTheme.colorScheme.onSurface
          )
          Spacer(modifier = Modifier.height(MaterialTheme.spacing.small))
          Text(
            text = "Ketuk ikon hati pada aktivitas yang menarik minatmu agar tersimpan di sini!",
            style = DiajakDesignSystem.Typography.Body,
            color = MaterialTheme.colorScheme.onSurfaceVariant,
            textAlign = TextAlign.Center
          )
          Spacer(modifier = Modifier.height(MaterialTheme.spacing.medium))
          Button(
            onClick = onExploreClick,
            colors = ButtonDefaults.buttonColors(containerColor = DiajakOrange),
            shape = RoundedCornerShape(16.dp),
            contentPadding = PaddingValues(horizontal = MaterialTheme.spacing.screenMargin, vertical = MaterialTheme.spacing.medium)
          ) {
            Icon(Icons.Outlined.Search, contentDescription = null, modifier = Modifier.size(18.dp))
            Spacer(modifier = Modifier.width(MaterialTheme.spacing.small))
            Text("Temukan Aktivitas", style = DiajakDesignSystem.Typography.TitleBold)
          }
        }
      }
    } else {
      LazyVerticalGrid(
        columns = GridCells.Fixed(2),
        state = gridState,
        contentPadding = PaddingValues(start = 20.dp, end = 20.dp, top = 0.dp, bottom = 124.dp),
        horizontalArrangement = Arrangement.spacedBy(MaterialTheme.spacing.medium),
        verticalArrangement = Arrangement.spacedBy(AppSpacing.Medium),
        modifier = Modifier.fillMaxSize().hazeSource(state = hazeState)
      ) {
        item(span = { GridItemSpan(2) }, contentType = HeaderUnderlayState.TEXT) {
          Column(modifier = Modifier.fillMaxWidth().statusBarsPadding().padding(top = 16.dp, bottom = 0.dp)) {
            Text(
              text = "Aktivitas Favorit",
              style = DiajakDesignSystem.Typography.Headline,
              color = MaterialTheme.colorScheme.onSurface
            )
          }
        }
        items(
          favoriteActivities,
          key = { it.id },
          contentType = { HeaderUnderlayState.PHOTO }
        ) { activity ->
          FavoriteActivityGridCard(
            activity = activity,
            onClick = { onActivityClick(activity) },
            onUnfavorite = { onToggleFavorite(activity) },
            userCoordinates = userCoordinates
          )
        }
      }
    }

    // 2. Glass Header Layer (Appears instantly as soon as screen scrolls, identical to HomeScreen)
    if (isScrolled) {
      val showHeaderTitle by remember {
        derivedStateOf {
          gridState.firstVisibleItemIndex > 0 || gridState.firstVisibleItemScrollOffset > with(density) { 48.dp.toPx() }
        }
      }

      DiajakGlassHeader(
        hazeState = hazeState,
        lazyGridState = gridState,
        modifier = Modifier.align(Alignment.TopCenter).zIndex(10f)
      ) {
        // Content Layer (Title) MUST be 56.dp height
        Box(
          modifier = Modifier
            .fillMaxWidth()
            .statusBarsPadding()
            .height(56.dp)
            .padding(horizontal = 20.dp),
          contentAlignment = Alignment.Center
        ) {
          // Dynamic WhatsApp/Instagram style title: only show when scrolled past the content title!
          androidx.compose.animation.AnimatedVisibility(
            visible = showHeaderTitle,
            enter = androidx.compose.animation.fadeIn(),
            exit = androidx.compose.animation.fadeOut(),
            modifier = Modifier.align(Alignment.Center)
          ) {
            // Title (STRICTLY CENTERED WITH HEADLINE TYPOGRAPHY)
            Text(
              text = "Aktivitas Favorit",
              style = DiajakDesignSystem.Typography.Headline,
              color = MaterialTheme.colorScheme.onSurface,
              textAlign = TextAlign.Center,
              modifier = Modifier.fillMaxWidth()
            )
          }
        }
      }
    }
  }
}

@Composable
fun FavoriteActivityGridCard(
  activity: ActivityModel,
  onClick: () -> Unit,
  onUnfavorite: () -> Unit,
  userCoordinates: Pair<Double, Double>? = null
) {
  val baseToday = remember {
    java.time.LocalDate.now().let {
      if (it.isBefore(java.time.LocalDate.of(2026, 7, 12))) {
        java.time.LocalDate.of(2026, 7, 12)
      } else {
        it
      }
    }
  }
  val displaySchedule = remember(activity.schedule, baseToday) {
    getHomeScreenDisplaySchedule(activity.schedule, baseToday)
  }

  Surface(
    modifier = Modifier
      .fillMaxWidth()
      .clickable(
        interactionSource = remember { MutableInteractionSource() },
        indication = null,
        onClick = onClick
      ),
    shape = RoundedCornerShape(16.dp), // FIXED: to 16.dp according to flat design rules
    color = Color.White,
    shadowElevation = 0.dp,
    // FIXED: removed border as per flat design rules
  ) {
    Column {
      Box(
        modifier = Modifier
          .fillMaxWidth()
          .height(140.dp)
          .clip(RoundedCornerShape(topStart = 16.dp, topEnd = 16.dp)) // Sync corner radius
      ) {
        Image(
          painter = painterResource(id = activity.imageResId),
          contentDescription = activity.title,
          contentScale = ContentScale.Crop,
          modifier = Modifier.fillMaxSize()
        )
        
        // Date badge on top-left of photo (stacked Day & Month)
        DateBadgeOverlay(
          displaySchedule = displaySchedule,
          modifier = Modifier
            .align(Alignment.TopStart)
            .padding(MaterialTheme.spacing.small)
        )

        // Favorite button top-right in white circle
        Box(
          modifier = Modifier
            .align(Alignment.TopEnd)
            .padding(MaterialTheme.spacing.small)
            .size(34.dp)
            .clip(CircleShape)
            .background(Color.White.copy(alpha = 0.92f))
            .clickable { onUnfavorite() },
          contentAlignment = Alignment.Center
        ) {
          Icon(
            imageVector = Icons.Outlined.Favorite,
            contentDescription = "Favorit",
            tint = DiajakOrange,
            modifier = Modifier.size(19.dp)
          )
        }
      }

      Column(
        modifier = Modifier
          .fillMaxWidth()
          .padding(horizontal = 14.dp, vertical = MaterialTheme.spacing.medium)
      ) {
        // Title
        Text(
          text = activity.title,
          style = TextStyle(
            fontSize = 13.5.sp,
            fontWeight = FontWeight.Bold,
            color = DiajakTextDark
          ),
          maxLines = 2,
          minLines = 2,
          overflow = TextOverflow.Ellipsis,
          lineHeight = 18.sp,
          modifier = Modifier.fillMaxWidth()
        )

        Spacer(modifier = Modifier.height(MaterialTheme.spacing.small))

        // Subtitle / Location Row with Location Pin Icon
        Row(verticalAlignment = Alignment.CenterVertically) {
          Icon(
            imageVector = Icons.Outlined.Place,
            contentDescription = "Lokasi",
            tint = DiajakOrange,
            modifier = Modifier.size(14.dp)
          )
          Spacer(modifier = Modifier.width(3.dp))
          Text(
            text = activity.locationName.split(",").firstOrNull()?.trim() ?: activity.locationName,
            style = TextStyle(
              fontSize = 11.5.sp,
              fontWeight = FontWeight.Medium,
              color = Color(0xFF64748B)
            ),
            maxLines = 1,
            overflow = TextOverflow.Ellipsis
          )
        }

        Spacer(modifier = Modifier.height(MaterialTheme.spacing.medium))

        // Price & Rating Row (Price on left, Rating on right with yellow badge)
        Row(
          modifier = Modifier.fillMaxWidth(),
          verticalAlignment = Alignment.CenterVertically,
          horizontalArrangement = Arrangement.SpaceBetween
        ) {
          val priceText = if (activity.priceValue == 0) "Gratis" else "Rp " + String.format("%,d", activity.priceValue).replace(',', '.')
          Text(
            text = priceText,
            style = TextStyle(
              fontSize = 14.sp,
              fontWeight = FontWeight.ExtraBold,
              color = DiajakOrange
            )
          )
          Row(
            verticalAlignment = Alignment.CenterVertically,
            modifier = Modifier
              .background(Color(0xFFFFF8E1), RoundedCornerShape(6.dp))
              .padding(horizontal = 5.dp, vertical = 2.dp)
          ) {
            Icon(
              imageVector = Icons.Outlined.Star,
              contentDescription = "Rating",
              tint = RatingGold,
              modifier = Modifier.size(13.dp)
            )
            Spacer(modifier = Modifier.width(2.dp))
            Text(
              text = "${activity.rating}",
              style = TextStyle(
                fontSize = 12.sp,
                fontWeight = FontWeight.Bold,
                color = DiajakTextDark
              )
            )
          }
        }
      }
    }
  }
}
