package com.example.ui.screens
import com.example.ui.theme.spacing
import androidx.compose.material3.MaterialTheme

import androidx.compose.material.icons.automirrored.outlined.*
import androidx.compose.material.icons.outlined.*

import androidx.compose.animation.*
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.spring
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.gestures.detectTransformGestures
import androidx.compose.foundation.gestures.detectTapGestures
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.pager.HorizontalPager
import androidx.compose.foundation.pager.PageSize
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import dev.chrisbanes.haze.HazeState
import dev.chrisbanes.haze.hazeSource
import dev.chrisbanes.haze.hazeEffect
import com.example.ui.components.DiajakGlassHeader
import com.example.ui.components.DiajakGlassConfig
import com.example.ui.components.DiajakGlassBottomSheet
import com.example.ui.components.DiajakFlowRow
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.ui.graphics.SolidColor
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.*
import androidx.compose.material.icons.outlined.*
import androidx.compose.material.icons.outlined.*
import androidx.compose.material.icons.outlined.*
import androidx.compose.material.icons.outlined.*
import androidx.compose.material.icons.outlined.*
import androidx.compose.material.icons.outlined.*
import androidx.compose.material.icons.outlined.*
import androidx.compose.material.icons.outlined.*
import androidx.compose.material.icons.outlined.*

import androidx.compose.material.icons.outlined.*
import androidx.compose.material.icons.outlined.*
import androidx.compose.material.icons.outlined.*
import androidx.compose.material.icons.outlined.*
import androidx.compose.material.icons.outlined.*
import androidx.compose.material.icons.outlined.*
import androidx.compose.material.icons.outlined.*
import androidx.compose.material.icons.outlined.*
import androidx.compose.material.icons.outlined.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.focus.onFocusChanged
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Path
import androidx.compose.ui.graphics.TransformOrigin
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.zIndex
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.layout.layout
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.ui.theme.diajakGlassButton
import com.example.data.DiajakRepository
import com.example.model.ActivityModel
import com.example.model.CategoryItem
import com.example.ui.theme.DiajakOrange
import com.example.ui.theme.DiajakTextDark
import com.example.ui.theme.RatingGold
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.animation.core.Spring
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.ui.platform.LocalContext
import android.widget.Toast
import coil.compose.rememberAsyncImagePainter

import com.example.ui.utils.getCategoryIconVector

private data class MapExploreCategory(
  val id: String,
  val name: String,
  val icon: androidx.compose.ui.graphics.vector.ImageVector
)

private val mapExploreCategoriesList = listOf(
  MapExploreCategory("all", "Semua", Icons.Outlined.Explore)
) + DiajakRepository.categories.filter { it.id != "all" }.map {
  MapExploreCategory(it.id, it.name, getCategoryIconVector(it.name))
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MapExploreScreen(
  activities: List<ActivityModel>,
  categories: List<CategoryItem>,
  selectedCategory: String,
  searchQuery: String,
  onSelectCategory: (String) -> Unit,
  onSearchChange: (String) -> Unit,
  onSelectActivity: (ActivityModel) -> Unit,
  onBack: () -> Unit = {},
  onDetectLocation: () -> Unit = {},
  isLocating: Boolean = false,
  favoriteActivityIds: Set<String> = emptySet(),
  favoriteAcaraIds: Set<String> = favoriteActivityIds,
  onToggleFavorite: (ActivityModel) -> Unit = {},
  priceFilter: String = "all",
  onPriceFilterChange: (String) -> Unit = {},
  ratingFilter: String = "all",
  onRatingFilterChange: (String) -> Unit = {},
  cityFilter: String = "all",
  onCityFilterChange: (String) -> Unit = {},
  userCoordinates: Pair<Double, Double>? = null,
  profileImageUri: String? = null,
  profileImageRes: Int? = null,
  onProfileClick: () -> Unit = {}
) {
  val context = LocalContext.current
  val focusManager = LocalFocusManager.current
  var selectedPin by remember { mutableStateOf<ActivityModel?>(activities.firstOrNull()) }
  val pagerState = rememberPagerState(pageCount = { activities.size })
  var isProgrammaticScroll by remember { mutableStateOf(false) }
  val activePageIndex by remember {
    derivedStateOf {
      val layoutInfo = pagerState.layoutInfo
      val visiblePages = layoutInfo.visiblePagesInfo
      if (visiblePages.isEmpty()) {
        pagerState.currentPage
      } else {
        val viewportWidth = layoutInfo.viewportSize.width
        if (viewportWidth <= 0) {
          pagerState.currentPage
        } else {
          val firstPage = visiblePages.firstOrNull()
          val lastPage = visiblePages.lastOrNull()
          if (firstPage != null && firstPage.index == 0 && firstPage.offset >= 0) {
            0
          } else if (lastPage != null && lastPage.index == activities.size - 1 && (lastPage.offset + layoutInfo.pageSize) <= viewportWidth + 4) {
            activities.size - 1
          } else {
            val viewportCenter = viewportWidth / 2f
            visiblePages.minByOrNull { pageInfo ->
              val pageCenter = pageInfo.offset + (layoutInfo.pageSize / 2f)
              kotlin.math.abs(pageCenter - viewportCenter)
            }?.index ?: pagerState.currentPage
          }
        }
      }
    }
  }
  var isSearchFocused by remember { mutableStateOf(false) }
  var localSearchQuery by remember(searchQuery) { mutableStateOf(searchQuery) }
  val hazeState = remember { HazeState() }

  // Interactive Zoom & Panning states (targets)
  var targetScale by remember { mutableStateOf(1.2f) }
  var targetOffset by remember { mutableStateOf(Offset.Zero) }

  // Animated Zoom & Offset for buttery-smooth visual transitions
  val animatedScale by animateFloatAsState(targetValue = targetScale, label = "scale", animationSpec = spring())
  val animatedOffsetX by animateFloatAsState(targetValue = targetOffset.x, label = "offsetX", animationSpec = spring())
  val animatedOffsetY by animateFloatAsState(targetValue = targetOffset.y, label = "offsetY", animationSpec = spring())

  // Track previous isLocating state to run selection when location is freshly detected
  var wasLocating by remember { mutableStateOf(false) }
  LaunchedEffect(isLocating) {
    if (wasLocating && !isLocating) {
      userCoordinates?.let { coords ->
        if (activities.isNotEmpty()) {
          val closest = activities.minByOrNull { act ->
            val actCoords = getActivityCoordinates(act)
            val dx = coords.first - actCoords.first
            val dy = coords.second - actCoords.second
            dx * dx + dy * dy
          }
          if (closest != null) {
            selectedPin = closest
          }
        }
      }
    }
    wasLocating = isLocating
  }

  // Also select the closest activity to the user's location on startup or when userCoordinates updates
  LaunchedEffect(userCoordinates) {
    userCoordinates?.let { coords ->
      if (activities.isNotEmpty()) {
        val closest = activities.minByOrNull { act ->
          val actCoords = getActivityCoordinates(act)
          val dx = coords.first - actCoords.first
          val dy = coords.second - actCoords.second
          dx * dx + dy * dy
        }
        if (closest != null) {
          selectedPin = closest
        }
      }
    }
  }

  // Reset pager and selected pin to avoid freeze/stuck states when query/list changes
  LaunchedEffect(activities) {
    if (activities.isNotEmpty()) {
      val initialPin = if (userCoordinates != null) {
        activities.minByOrNull { act ->
          val actCoords = getActivityCoordinates(act)
          val dx = userCoordinates.first - actCoords.first
          val dy = userCoordinates.second - actCoords.second
          dx * dx + dy * dy
        } ?: activities.firstOrNull()
      } else {
        activities.firstOrNull()
      }
      selectedPin = initialPin
      targetScale = 1.2f // reset zoom slightly
      try {
        val pageIndex = initialPin?.let { pin -> activities.indexOfFirst { it.id == pin.id } } ?: 0
        pagerState.scrollToPage(if (pageIndex >= 0) pageIndex else 0)
      } catch (e: Exception) {
        // Safe guard against pageCount mismatch before layout pass
      }
    } else {
      selectedPin = null
    }
  }

  LaunchedEffect(activePageIndex) {
    if (pagerState.isScrollInProgress && !isProgrammaticScroll && activities.isNotEmpty() && activePageIndex >= 0 && activePageIndex < activities.size) {
      selectedPin = activities[activePageIndex]
    }
  }

  LaunchedEffect(pagerState.isScrollInProgress) {
    if (pagerState.isScrollInProgress) {
      focusManager.clearFocus()
    }
  }

  LaunchedEffect(selectedPin, activities) {
    selectedPin?.let { pin ->
      val index = activities.indexOfFirst { it.id == pin.id }
      if (index >= 0 && index != pagerState.currentPage && index < activities.size && !pagerState.isScrollInProgress) {
        try {
          isProgrammaticScroll = true
          pagerState.animateScrollToPage(index)
        } finally {
          isProgrammaticScroll = false
        }
      }
    }
  }

  Box(
    modifier = Modifier
      .fillMaxSize()
      .pointerInput(Unit) {
        detectTapGestures(onTap = {
          focusManager.clearFocus()
        })
      }
  ) {
    // Zoomable & Pannable Map Container
    BoxWithConstraints(
      modifier = Modifier
        .fillMaxSize()
        .hazeSource(state = hazeState)
        .pointerInput(Unit) {
          detectTransformGestures { _, pan, zoom, _ ->
            focusManager.clearFocus()
            targetScale = (targetScale * zoom).coerceIn(0.6f, 4.0f)
            targetOffset = targetOffset + pan
          }
        }
    ) {
      val mapWidth = maxWidth
      val mapHeight = maxHeight
      val density = LocalDensity.current
      val widthPx = with(density) { mapWidth.toPx() }
      val heightPx = with(density) { mapHeight.toPx() }

      val topOverlayHeightPx = with(density) { 144.dp.toPx() }
      val bottomOverlayHeightPx = with(density) { 294.dp.toPx() }
      val verticalCenterShift = (topOverlayHeightPx - bottomOverlayHeightPx) / 2f

      // Smooth auto-centering on the active selected pin
      LaunchedEffect(selectedPin, targetScale, widthPx, heightPx) {
        selectedPin?.let { pin ->
          val pinX = widthPx * pin.mapX
          val pinY = heightPx * pin.mapY
          targetOffset = Offset(
            x = -(pinX - widthPx / 2f) * targetScale,
            y = -(pinY - heightPx / 2f) * targetScale + verticalCenterShift
          )
        }
      }

      // Inner container carrying map graphic & pins, transformed dynamically
      Box(
        modifier = Modifier
          .fillMaxSize()
          .graphicsLayer(
            scaleX = animatedScale,
            scaleY = animatedScale,
            translationX = animatedOffsetX,
            translationY = animatedOffsetY,
            transformOrigin = TransformOrigin(0.5f, 0.5f)
          )) {
        // Stylized Canvas Map Background
        Canvas(
          modifier = Modifier
            .fillMaxSize()
            .clickable(
              interactionSource = remember { MutableInteractionSource() },
              indication = null
            ) {
              selectedPin = null
              focusManager.clearFocus()
            }
        ) {
          val w = size.width
          val h = size.height

          // Base color: Land
          drawRect(color = com.example.ui.theme.BackgroundLight)

          // Forest/Green Park Canvas Representation
          val parkPath = Path().apply {
            moveTo(0f, 0f)
            lineTo(w * 0.35f, 0f)
            quadraticTo(w * 0.25f, h * 0.25f, w * 0.15f, h * 0.35f)
            quadraticTo(w * 0.05f, h * 0.45f, 0f, h * 0.5f)
            close()
          }
          drawPath(parkPath, Color(0xFFDDC3A5)) // Sand beach
          drawCircle(Color(0xFFDCFCE7), radius = w * 0.18f, center = Offset(w * 0.15f, h * 0.15f)) // Park

          // Water Reservoir
          val lakePath = Path().apply {
            moveTo(w * 0.75f, h)
            quadraticTo(w * 0.7f, h * 0.75f, w * 0.85f, h * 0.65f)
            quadraticTo(w * 0.95f, h * 0.55f, w, h * 0.5f)
            lineTo(w, h)
            close()
          }
          drawPath(lakePath, Color(0xFFE0F2FE)) // Lake blue

          // Grid Roads / Highways
          val gridColor = Color.White
          drawLine(gridColor, Offset(0f, h * 0.4f), Offset(w, h * 0.45f), strokeWidth = 16f)
          drawLine(gridColor, Offset(0f, h * 0.75f), Offset(w, h * 0.7f), strokeWidth = 14f)
          drawLine(gridColor, Offset(w * 0.45f, 0f), Offset(w * 0.52f, h), strokeWidth = 18f)
          drawLine(gridColor, Offset(w * 0.8f, 0f), Offset(w * 0.75f, h), strokeWidth = 12f)
        }

        // Interactive & Scale-Invariant Map Pins (Individual Markers without Clustering)
        activities.forEach { act ->
          val xOffset = mapWidth * act.mapX
          val yOffset = mapHeight * act.mapY
          val isSelected = selectedPin?.id == act.id

          val categoryId = categories.find { cat ->
            act.category.contains(cat.name.substringBefore(" &"), ignoreCase = true)
          }?.id ?: when {
            act.category.contains("Kopi", true) -> "coffee"
            act.category.contains("Outdoor", true) -> "outdoor"
            act.category.contains("Seni", true) -> "art"
            act.category.contains("Olahraga", true) -> "sport"
            act.category.contains("Kuliner", true) -> "culinary"
            act.category.contains("Wellness", true) -> "wellness"
            act.category.contains("Musik", true) -> "music"
            act.category.contains("Gaming", true) -> "gaming"
            else -> "unknown"
          }

          Box(
            modifier = Modifier
              .alignPinAt(xOffset, yOffset)
              .zIndex(if (isSelected) 10f else 1f)
              // Counter-act the parent scale so that pins remain perfectly sharp, readable, and standard size!
              .graphicsLayer(
                scaleX = 1f / animatedScale,
                scaleY = 1f / animatedScale,
                transformOrigin = TransformOrigin(0.5f, 1f) // Anchor bottom-center of the pin
              )
          ) {
            CustomMapPinMarker(
              imageResId = act.imageResId,
              emojiIcon = categories.find { it.id == categoryId }?.icon,
              categoryIcon = getCategoryIcon(categoryId),
              isSelected = isSelected,
              title = act.title,
              onClick = {
                if (isSelected) {
                  onSelectActivity(act)
                } else {
                  selectedPin = act
                  focusManager.clearFocus()
                }
              }
            )
          }
        }
      }
    }

    // Top Filter & Search Bar
    var showFilterDialog by remember { mutableStateOf(false) }

    DiajakGlassHeader(
      hazeState = hazeState,
      modifier = Modifier
        .align(Alignment.TopCenter)
        .zIndex(10f)
        .fillMaxWidth()
    ) {
      Box(
        modifier = Modifier
          .fillMaxWidth()
          .statusBarsPadding()
          .padding(top = 16.dp, bottom = 12.dp)
      ) {
        Surface(
          modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 20.dp)
            .height(48.dp),
          shape = RoundedCornerShape(24.dp),
          color = Color.White,
          shadowElevation = 0.dp
        ) {
          Row(
            modifier = Modifier
              .fillMaxSize()
              .padding(start = 6.dp, end = 6.dp),
            verticalAlignment = Alignment.CenterVertically
          ) {
            // Circular Back Button inside the search bar
            IconButton(
              onClick = { onBack() },
              modifier = Modifier.size(38.dp)
            ) {
              Icon(
                imageVector = Icons.AutoMirrored.Outlined.ArrowBack,
                contentDescription = "Kembali ke Beranda",
                tint = Color(0xFF0F172A),
                modifier = Modifier.size(20.dp)
              )
            }

            Spacer(modifier = Modifier.width(4.dp))

            Box(
              modifier = Modifier
                .weight(1f)
                .fillMaxHeight(),
              contentAlignment = Alignment.CenterStart
            ) {
              if (localSearchQuery.isEmpty()) {
                Text(
                  text = "Cari aktivitas, hobi, komunitas, kota",
                  style = TextStyle(
                    fontSize = 13.5.sp,
                    color = Color(0xFF0F172A),
                    fontWeight = FontWeight.Normal
                  ),
                  maxLines = 1,
                  overflow = TextOverflow.Ellipsis
                )
              }
              BasicTextField(
                value = localSearchQuery,
                onValueChange = {
                  localSearchQuery = it
                  onSearchChange(it)
                },
                textStyle = TextStyle(
                  fontSize = 13.5.sp,
                  color = Color(0xFF0F172A),
                  fontWeight = FontWeight.Medium
                ),
                singleLine = true,
                cursorBrush = SolidColor(DiajakOrange),
                keyboardOptions = KeyboardOptions(imeAction = ImeAction.Search),
                keyboardActions = KeyboardActions(onSearch = {
                  onSearchChange(localSearchQuery)
                  focusManager.clearFocus()
                }),
                modifier = Modifier
                  .fillMaxWidth()
                  .onFocusChanged { state ->
                    isSearchFocused = state.isFocused
                    if (state.isFocused) {
                      onPriceFilterChange("all")
                      onRatingFilterChange("all")
                      onCityFilterChange("all")
                      onSelectCategory("all")
                    }
                  }
              )
            }

            if (localSearchQuery.isNotEmpty()) {
              IconButton(
                onClick = {
                  localSearchQuery = ""
                  onSearchChange("")
                  onPriceFilterChange("all")
                  onRatingFilterChange("all")
                  onCityFilterChange("all")
                  onSelectCategory("all")
                  focusManager.clearFocus()
                },
                modifier = Modifier.size(32.dp)
              ) {
                Icon(
                  imageVector = Icons.Outlined.Close,
                  contentDescription = "Hapus",
                  tint = Color(0xFF64748B),
                  modifier = Modifier.size(16.dp)
                )
              }
            }

            // Filter icon inside the bar with active highlight state matching Google Maps
            val hasActiveFilters = priceFilter != "all" || ratingFilter != "all" || cityFilter != "all" || selectedCategory != "all"
            IconButton(
              onClick = {
                focusManager.clearFocus()
                showFilterDialog = true
              },
              modifier = Modifier.size(38.dp)
            ) {
              Box(modifier = Modifier.size(24.dp), contentAlignment = Alignment.Center) {
                Icon(
                  imageVector = Icons.Outlined.Tune,
                  contentDescription = "Filter",
                  tint = if (hasActiveFilters) DiajakOrange else Color(0xFF0F172A),
                  modifier = Modifier.size(20.dp)
                )
                if (hasActiveFilters) {
                  Box(
                    modifier = Modifier
                      .size(6.dp)
                      .clip(CircleShape)
                      .background(DiajakOrange)
                      .align(Alignment.TopEnd)
                  )
                }
              }
            }
          }
        }
      }
    }

    Box(
      modifier = Modifier
        .align(Alignment.TopEnd)
        .statusBarsPadding()
        .padding(top = 80.dp)
        .padding(horizontal = 20.dp)
        .zIndex(10f)
      ) {
      Surface(
        shape = CircleShape,
        color = Color.White,
        shadowElevation = 0.dp,
        modifier = Modifier
          .size(44.dp)
          .clickable(
            interactionSource = remember { MutableInteractionSource() },
            indication = null,
            enabled = !isLocating
          ) {
            focusManager.clearFocus()
            localSearchQuery = ""
            onSearchChange("")
            onSelectCategory("all")
            onPriceFilterChange("all")
            onRatingFilterChange("all")
            onCityFilterChange("all")
            onDetectLocation()
               
            // Select the closest one immediately if coordinates are already detected
            userCoordinates?.let { coords ->
              if (activities.isNotEmpty()) {
                val closest = activities.minByOrNull { act ->
                  val actCoords = getActivityCoordinates(act)
                  val dx = coords.first - actCoords.first
                  val dy = coords.second - actCoords.second
                  dx * dx + dy * dy
                }
                if (closest != null) {
                  selectedPin = closest
                }
              }
            }
          }
      ) {
        Box(contentAlignment = Alignment.Center) {
          if (isLocating) {
            CircularProgressIndicator(
              modifier = Modifier.size(20.dp),
              strokeWidth = 2.dp,
              color = DiajakOrange
            )
          } else {
            Icon(
              imageVector = Icons.Outlined.MyLocation,
              contentDescription = "Cari Lokasi Saya",
              tint = DiajakOrange,
              modifier = Modifier.size(22.dp)
            )
          }
        }
      }
    }

    // Bottom Carousel of Activities (Slidable left & right)
    AnimatedVisibility(
      visible = selectedPin != null && activities.isNotEmpty(),
      enter = fadeIn() + slideInVertically(initialOffsetY = { it }),
      exit = fadeOut() + slideOutVertically(targetOffsetY = { it }),
      modifier = Modifier
        .align(Alignment.BottomCenter)
        .navigationBarsPadding()
        .padding(bottom = MaterialTheme.spacing.medium)
      ) {
      HorizontalPager(
        state = pagerState,
        pageSize = PageSize.Fixed(186.dp),
        contentPadding = PaddingValues(horizontal = MaterialTheme.spacing.extraLarge, vertical = MaterialTheme.spacing.small),
        pageSpacing = 12.dp,
        beyondViewportPageCount = 1,
        modifier = Modifier.fillMaxWidth() 
      ) { page ->
        val pinAct = activities[page]
        val isActive = pinAct.id == selectedPin?.id
        val baseToday = remember {
          java.time.LocalDate.now().let {
            if (it.isBefore(java.time.LocalDate.of(2026, 7, 12))) {
              java.time.LocalDate.of(2026, 7, 12)
            } else {
              it
            }
          }
        }
        val displaySchedule = remember(pinAct.schedule, baseToday) {
          getHomeScreenDisplaySchedule(pinAct.schedule, baseToday)
        }

        com.example.ui.theme.DiajakCard(
          onClick = {
            focusManager.clearFocus()
            if (isActive) {
              onSelectActivity(pinAct)
            } else {
              selectedPin = pinAct
            }
          },
          isSelected = isActive,
          modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = MaterialTheme.spacing.extraSmall)
        ) {
          Column {
            Box(
              modifier = Modifier
                .fillMaxWidth()
                .height(140.dp)
                .clip(RoundedCornerShape(topStart = 16.dp, topEnd = 16.dp))
            ) {
              Image(
                painter = painterResource(id = pinAct.imageResId),
                contentDescription = pinAct.title,
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

              // Favorite button on top-right in white circle
              val isFav = favoriteActivityIds.contains(pinAct.id) || favoriteAcaraIds.contains(pinAct.id)
              Box(
                modifier = Modifier
                  .align(Alignment.TopEnd)
                  .padding(MaterialTheme.spacing.small)
                  .size(34.dp)
                  .clip(CircleShape)
                  .background(Color.White.copy(alpha = 0.92f))
                  .clickable { onToggleFavorite(pinAct) },
                contentAlignment = Alignment.Center
              ) {
                Icon(
                  imageVector = if (isFav) Icons.Outlined.Favorite else Icons.Outlined.FavoriteBorder,
                  contentDescription = "Favorit",
                  tint = if (isFav) DiajakOrange else Color(0xFF64748B),
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
                text = pinAct.title,
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
                  text = pinAct.locationName.split(",").firstOrNull()?.trim() ?: pinAct.locationName,
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
                val formattedPriceText = if (pinAct.priceValue == 0) "Gratis" else "Rp " + String.format("%,d", pinAct.priceValue).replace(',', '.')
                Text(
                  text = formattedPriceText,
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
                    text = "${pinAct.rating}",
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
    }
    
    if (showFilterDialog) {
      DiajakGlassBottomSheet(
        onDismissRequest = { showFilterDialog = false },
        title = "Filter Pencarian"
      ) {
        SlideInFilterScreen(
          priceFilter = priceFilter,
          onPriceFilterChange = onPriceFilterChange,
          ratingFilter = ratingFilter,
          onRatingFilterChange = onRatingFilterChange,
          cityFilter = cityFilter,
          onCityFilterChange = onCityFilterChange,
          categoryFilter = selectedCategory,
          onCategoryFilterChange = onSelectCategory,
          onApply = { showFilterDialog = false },
          onBack = { showFilterDialog = false }
        )
      }
    }
  }
}

@OptIn(ExperimentalMaterial3Api::class, ExperimentalLayoutApi::class)
@Composable
fun SlideInFilterScreen(
  priceFilter: String,
  onPriceFilterChange: (String) -> Unit,
  ratingFilter: String,
  onRatingFilterChange: (String) -> Unit,
  cityFilter: String,
  onCityFilterChange: (String) -> Unit,
  categoryFilter: String,
  onCategoryFilterChange: (String) -> Unit,
  onApply: () -> Unit,
  onBack: () -> Unit
) {
  var tempPriceFilter by remember(priceFilter) { mutableStateOf(priceFilter) }
  var tempRatingFilter by remember(ratingFilter) { mutableStateOf(ratingFilter) }
  var tempCityFilter by remember(cityFilter) { mutableStateOf(cityFilter) }
  var tempCategoryFilter by remember(categoryFilter) { mutableStateOf(categoryFilter) }

  Box(modifier = Modifier.fillMaxWidth().fillMaxHeight(0.85f)) {
    Column(
      modifier = Modifier
        .fillMaxWidth()
        .fillMaxHeight()
        .verticalScroll(rememberScrollState())
        .padding(start = 20.dp, top = 72.dp, end = 20.dp, bottom = 120.dp)
    ) {

      Text(
        text = "Tampilkan aktivitas sesuai biaya",
        style = TextStyle(
          fontSize = 16.sp,
          fontWeight = FontWeight.Bold,
          fontFamily = com.example.ui.theme.AppFontFamily
        ),
        color = MaterialTheme.colorScheme.onSurface
      )
      Spacer(modifier = Modifier.height(MaterialTheme.spacing.medium))

      val options = listOf(
        Pair("all", "Semua Biaya"),
        Pair("free", "Gratis"),
        Pair("under50k", "Di bawah Rp 50rb"),
        Pair("under100k", "Di bawah Rp 100rb"),
        Pair("under150k", "Di bawah Rp 150rb"),
        Pair("under200k", "Di bawah Rp 200rb"),
        Pair("above200k", "Di atas Rp 200rb")
      )

      DiajakFlowRow(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.spacedBy(MaterialTheme.spacing.small),
        verticalArrangement = Arrangement.spacedBy(MaterialTheme.spacing.small)
      ) {
        options.forEach { (key, label) ->
          val isSelected = tempPriceFilter == key
          Surface(
            onClick = { tempPriceFilter = if (isSelected) "all" else key },
            shape = RoundedCornerShape(16.dp),
            color = if (isSelected) DiajakOrange else Color.White,
            shadowElevation = 0.dp
          ) {
            Row(
              modifier = Modifier.padding(horizontal = 16.dp, vertical = 10.dp),
              verticalAlignment = Alignment.CenterVertically
            ) {
              if (isSelected) {
                Icon(
                  imageVector = Icons.Outlined.Check,
                  contentDescription = null,
                  tint = Color.White,
                  modifier = Modifier.size(16.dp)
                )
                Spacer(modifier = Modifier.width(6.dp))
              }
              Text(
                text = label,
                fontWeight = if (isSelected) FontWeight.Bold else FontWeight.Medium,
                style = com.example.ui.theme.DiajakDesignSystem.Typography.Body,
                color = if (isSelected) Color.White else Color(0xFF262626)
              )
            }
          }
        }
      }

      Spacer(modifier = Modifier.height(MaterialTheme.spacing.medium))

      Text(
        text = "Tampilkan aktivitas berdasarkan ulasan",
        style = TextStyle(
          fontSize = 16.sp,
          fontWeight = FontWeight.Bold,
          fontFamily = com.example.ui.theme.AppFontFamily
        ),
        color = MaterialTheme.colorScheme.onSurface
      )
      Spacer(modifier = Modifier.height(MaterialTheme.spacing.medium))

      val ratingOptions = listOf(
        Pair("3_up", "3.0+"),
        Pair("3_5_up", "3.5+"),
        Pair("4_up", "4.0+"),
        Pair("4_5_up", "4.5+")
      )

      DiajakFlowRow(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.spacedBy(MaterialTheme.spacing.small),
        verticalArrangement = Arrangement.spacedBy(MaterialTheme.spacing.small)
      ) {
        ratingOptions.forEach { (key, label) ->
          val isSelected = tempRatingFilter == key
          Surface(
            onClick = { tempRatingFilter = if (isSelected) "all" else key },
            shape = RoundedCornerShape(16.dp),
            color = if (isSelected) DiajakOrange else Color.White,
            shadowElevation = 0.dp
          ) {
            Row(
              modifier = Modifier.padding(horizontal = 16.dp, vertical = 10.dp),
              verticalAlignment = Alignment.CenterVertically
            ) {
              if (isSelected) {
                Icon(
                  imageVector = Icons.Outlined.Check,
                  contentDescription = null,
                  tint = Color.White,
                  modifier = Modifier.size(16.dp)
                )
                Spacer(modifier = Modifier.width(6.dp))
              }
              Icon(
                imageVector = Icons.Outlined.Star,
                contentDescription = null,
                tint = if (isSelected) Color.White else RatingGold,
                modifier = Modifier.size(16.dp)
              )
              Spacer(modifier = Modifier.width(MaterialTheme.spacing.extraSmall))
              Text(
                text = label,
                fontWeight = if (isSelected) FontWeight.Bold else FontWeight.Medium,
                style = com.example.ui.theme.DiajakDesignSystem.Typography.Body,
                color = if (isSelected) Color.White else Color(0xFF262626)
              )
            }
          }
        }
      }

      Spacer(modifier = Modifier.height(MaterialTheme.spacing.medium))

      Text(
        text = "Tampilkan aktivitas di kota tertentu",
        style = TextStyle(
          fontSize = 16.sp,
          fontWeight = FontWeight.Bold,
          fontFamily = com.example.ui.theme.AppFontFamily
        ),
        color = MaterialTheme.colorScheme.onSurface
      )
      Spacer(modifier = Modifier.height(MaterialTheme.spacing.medium))

      val cityOptions = listOf(
        Pair("all", "Semua Kota"),
        Pair("Jakarta", "Jakarta"),
        Pair("Bandung", "Bandung"),
        Pair("Semarang", "Semarang"),
        Pair("Magelang", "Magelang"),
        Pair("Yogyakarta", "Yogyakarta"),
        Pair("Surabaya", "Surabaya"),
        Pair("Denpasar", "Denpasar"),
        Pair("Medan", "Medan"),
        Pair("Makassar", "Makassar"),
        Pair("Palembang", "Palembang"),
        Pair("Malang", "Malang"),
        Pair("Bogor", "Bogor"),
        Pair("Batam", "Batam"),
        Pair("Padang", "Padang"),
        Pair("Pekanbaru", "Pekanbaru"),
        Pair("Balikpapan", "Balikpapan"),
        Pair("Solo", "Solo")
      )

      DiajakFlowRow(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.spacedBy(MaterialTheme.spacing.small),
        verticalArrangement = Arrangement.spacedBy(MaterialTheme.spacing.small)
      ) {
        cityOptions.forEach { (key, label) ->
          val isSelected = tempCityFilter == key
          Surface(
            onClick = { tempCityFilter = if (isSelected) "all" else key },
            shape = RoundedCornerShape(16.dp),
            color = if (isSelected) DiajakOrange else Color.White,
            shadowElevation = 0.dp
          ) {
            Row(
              modifier = Modifier.padding(horizontal = 16.dp, vertical = 10.dp),
              verticalAlignment = Alignment.CenterVertically
            ) {
              if (isSelected) {
                Icon(
                  imageVector = Icons.Outlined.Check,
                  contentDescription = null,
                  tint = Color.White,
                  modifier = Modifier.size(16.dp)
                )
                Spacer(modifier = Modifier.width(6.dp))
              }
              Text(
                text = label,
                fontWeight = if (isSelected) FontWeight.Bold else FontWeight.Medium,
                style = com.example.ui.theme.DiajakDesignSystem.Typography.Body,
                color = if (isSelected) Color.White else Color(0xFF262626)
              )
            }
          }
        }
      }

      Spacer(modifier = Modifier.height(MaterialTheme.spacing.medium))

      Text(
        text = "Tampilkan kategori aktivitas",
        style = TextStyle(
          fontSize = 16.sp,
          fontWeight = FontWeight.Bold,
          fontFamily = com.example.ui.theme.AppFontFamily
        ),
        color = MaterialTheme.colorScheme.onSurface
      )
      Spacer(modifier = Modifier.height(MaterialTheme.spacing.medium))

      val categoryOptions = listOf(Pair("all", "Semua Kategori")) + DiajakRepository.categories.filter { it.id != "all" }.map {
        Pair(it.id, it.name)
      }

      DiajakFlowRow(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.spacedBy(MaterialTheme.spacing.small),
        verticalArrangement = Arrangement.spacedBy(MaterialTheme.spacing.small)
      ) {
        categoryOptions.forEach { (key, label) ->
          val isSelected = tempCategoryFilter == key
          Surface(
            onClick = { tempCategoryFilter = if (isSelected) "all" else key },
            shape = RoundedCornerShape(16.dp),
            color = if (isSelected) DiajakOrange else Color.White,
            shadowElevation = 0.dp
          ) {
            Row(
              modifier = Modifier.padding(horizontal = 16.dp, vertical = 10.dp),
              verticalAlignment = Alignment.CenterVertically
            ) {
              if (isSelected) {
                Icon(
                  imageVector = Icons.Outlined.Check,
                  contentDescription = null,
                  tint = Color.White,
                  modifier = Modifier.size(16.dp)
                )
                Spacer(modifier = Modifier.width(6.dp))
              }
              Text(
                text = label,
                fontWeight = if (isSelected) FontWeight.Bold else FontWeight.Medium,
                style = com.example.ui.theme.DiajakDesignSystem.Typography.Body,
                color = if (isSelected) Color.White else Color(0xFF262626)
              )
            }
          }
        }
      }
    }

    // Floating Transparent Bottom Bar
    Box(
      modifier = Modifier
        .align(Alignment.BottomCenter)
        .fillMaxWidth()
        .background(Color.Transparent)
        .navigationBarsPadding()
        .padding(horizontal = 20.dp, vertical = MaterialTheme.spacing.medium)
    ) {
      Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.spacedBy(MaterialTheme.spacing.medium)
      ) {
        OutlinedButton(
          onClick = {
            tempPriceFilter = "all"
            tempRatingFilter = "all"
            tempCityFilter = "all"
            tempCategoryFilter = "all"
          },
          modifier = Modifier.weight(1f).height(52.dp),
          border = BorderStroke(1.5.dp, DiajakOrange),
          colors = ButtonDefaults.outlinedButtonColors(containerColor = Color.White, contentColor = DiajakOrange),
          elevation = ButtonDefaults.buttonElevation(defaultElevation = 0.dp),
          shape = RoundedCornerShape(16.dp)
        ) {
          Text("Reset", style = androidx.compose.ui.text.TextStyle(fontSize = 16.sp, fontWeight = androidx.compose.ui.text.font.FontWeight.Bold, fontFamily = com.example.ui.theme.AppFontFamily), color = DiajakOrange)
        }

        Button(
          onClick = {
            onPriceFilterChange(tempPriceFilter)
            onRatingFilterChange(tempRatingFilter)
            onCityFilterChange(tempCityFilter)
            onCategoryFilterChange(tempCategoryFilter)
            onApply()
          },
          modifier = Modifier.weight(1f).height(52.dp),
          colors = ButtonDefaults.buttonColors(containerColor = DiajakOrange),
          elevation = ButtonDefaults.buttonElevation(defaultElevation = 0.dp),
          shape = RoundedCornerShape(16.dp)
        ) {
          Text("Terapkan", color = Color.White, style = androidx.compose.ui.text.TextStyle(fontSize = 16.sp, fontWeight = androidx.compose.ui.text.font.FontWeight.Bold, fontFamily = com.example.ui.theme.AppFontFamily))
        }
      }
    }
  }
}

fun Modifier.alignPinAt(xDp: Dp, yDp: Dp) = this.layout { measurable, constraints ->
  val placeable = measurable.measure(constraints)
  val xPx = xDp.roundToPx()
  val yPx = yDp.roundToPx()
  layout(placeable.width, placeable.height) {
    placeable.placeRelative(
      x = (xPx - placeable.width / 2f).toInt(),
      y = (yPx - placeable.height).toInt()
    )
  }
}

@Composable
fun getCategoryIcon(id: String): androidx.compose.ui.graphics.vector.ImageVector {
  return when(id) {
    "all" -> Icons.Outlined.Star
    "coffee" -> Icons.Outlined.LocalCafe
    "outdoor" -> Icons.Outlined.Park
    "art" -> Icons.Outlined.Palette
    "sport" -> Icons.AutoMirrored.Outlined.DirectionsRun
    "culinary" -> Icons.Outlined.Restaurant
    "wellness" -> Icons.Outlined.Spa
    "music" -> Icons.Outlined.MusicNote
    "gaming" -> Icons.Outlined.SportsEsports
    else -> Icons.Outlined.Explore
  }
}

@Composable
fun CustomMapPinMarker(
  imageResId: Int? = null,
  categoryIcon: androidx.compose.ui.graphics.vector.ImageVector? = null,
  emojiIcon: String? = null,
  isSelected: Boolean = false,
  title: String = "",
  onClick: (() -> Unit)? = null
) {
  val animatedSize by animateFloatAsState(
    targetValue = if (isSelected) 56f else 46f,
    animationSpec = spring(dampingRatio = Spring.DampingRatioMediumBouncy, stiffness = Spring.StiffnessMediumLow),
    label = "pinSize"
  )
  val pinSizeDp = animatedSize.dp
  val borderColor = if (isSelected) DiajakOrange else Color.White
  val borderWidth = if (isSelected) 3.dp else 2.5.dp

  val clickableModifier = if (onClick != null) {
    Modifier.clickable(
      interactionSource = remember { MutableInteractionSource() },
      indication = null,
      onClick = onClick
    )
  } else Modifier

  Column(
    horizontalAlignment = Alignment.CenterHorizontally,
    modifier = clickableModifier
  ) {
    // Floating Title Bubble when Selected
    if (isSelected && title.isNotEmpty()) {
      Surface(
        shape = RoundedCornerShape(16.dp),
        color = DiajakOrange,
        shadowElevation = 4.dp,
        modifier = Modifier.padding(bottom = 6.dp)
      ) {
        Row(
          modifier = Modifier.padding(horizontal = 10.dp, vertical = 5.dp),
          verticalAlignment = Alignment.CenterVertically
        ) {
          if (emojiIcon != null) {
            Text(text = emojiIcon, fontSize = 12.sp)
            Spacer(modifier = Modifier.width(4.dp))
          } else if (categoryIcon != null) {
            Icon(
              imageVector = categoryIcon,
              contentDescription = null,
              tint = Color.White,
              modifier = Modifier.size(14.dp)
            )
            Spacer(modifier = Modifier.width(4.dp))
          }
          Text(
            text = if (title.length > 22) title.take(22) + "..." else title,
            style = com.example.ui.theme.DiajakDesignSystem.Typography.TitleBold,
            color = Color.White,
            fontSize = 12.sp,
            maxLines = 1
          )
        }
      }
    }

    // Photo Pin Marker (Squircle container + Downward pointer tip + Ground Shadow)
    Column(
      horizontalAlignment = Alignment.CenterHorizontally
    ) {
      // Photo Card Frame with Drop Shadow
      Surface(
        modifier = Modifier
          .size(pinSizeDp)
          .shadow(
            elevation = if (isSelected) 8.dp else 4.dp,
            shape = RoundedCornerShape(14.dp),
            spotColor = Color.Black.copy(alpha = if (isSelected) 0.35f else 0.2f),
            ambientColor = Color.Black.copy(alpha = 0.1f)
          ),
        shape = RoundedCornerShape(14.dp),
        color = Color.White,
        border = BorderStroke(borderWidth, borderColor)
      ) {
        Box(
          modifier = Modifier
            .fillMaxSize()
            .padding(if (isSelected) 1.5.dp else 1.dp)
            .clip(RoundedCornerShape(11.dp)),
          contentAlignment = Alignment.Center
        ) {
          if (imageResId != null) {
            Image(
              painter = painterResource(id = imageResId),
              contentDescription = title,
              contentScale = ContentScale.Crop,
              modifier = Modifier.fillMaxSize()
            )
          } else if (categoryIcon != null) {
            Icon(
              imageVector = categoryIcon,
              contentDescription = null,
              tint = if (isSelected) DiajakOrange else Color(0xFF64748B),
              modifier = Modifier.size(22.dp)
            )
          } else {
            Icon(
              imageVector = Icons.Outlined.Place,
              contentDescription = null,
              tint = DiajakOrange,
              modifier = Modifier.size(22.dp)
            )
          }
        }
      }

      // Downward pointer arrow triangle
      Canvas(
        modifier = Modifier
          .size(width = 14.dp, height = 7.dp)
          .offset(y = (-1).dp)
      ) {
        val trianglePath = Path().apply {
          moveTo(0f, 0f)
          lineTo(size.width, 0f)
          lineTo(size.width / 2f, size.height)
          close()
        }
        drawPath(path = trianglePath, color = borderColor)
      }

      // Ground anchor shadow
      Canvas(
        modifier = Modifier
          .size(width = 18.dp, height = 4.dp)
          .offset(y = (-1).dp)
      ) {
        drawOval(
          color = Color.Black.copy(alpha = 0.22f),
          topLeft = Offset.Zero,
          size = size
        )
      }
    }
  }
}

private fun getActivityCoordinates(activity: ActivityModel): Pair<Double, Double> {
  val loc = activity.locationName.lowercase()
  return when {
    loc.contains("yogyakarta") || loc.contains("jogja") -> Pair(-7.7956, 110.3695)
    loc.contains("solo") -> Pair(-7.5755, 110.8243)
    loc.contains("semarang") -> Pair(-6.9667, 110.4167)
    loc.contains("magelang") -> Pair(-7.4722, 110.2197)
    loc.contains("bandung") -> Pair(-6.9175, 107.6191)
    loc.contains("bogor") -> Pair(-5.5972, 106.7992)
    loc.contains("surabaya") -> Pair(-7.2575, 112.7521)
    loc.contains("malang") -> Pair(-7.9785, 112.6560)
    loc.contains("bali") || loc.contains("denpasar") || loc.contains("badung") || loc.contains("sanur") || loc.contains("ubud") || loc.contains("kuta") -> Pair(-8.4095, 115.1889)
    loc.contains("medan") -> Pair(3.5952, 98.6722)
    loc.contains("batam") -> Pair(1.1301, 104.0531)
    loc.contains("padang") -> Pair(-0.9471, 100.4172)
    loc.contains("pekanbaru") -> Pair(0.5071, 101.4478)
    loc.contains("palembang") -> Pair(-2.9761, 104.7754)
    loc.contains("makassar") -> Pair(-5.1477, 119.4327)
    loc.contains("balikpapan") -> Pair(-1.2654, 116.8312)
    else -> Pair(-6.2088, 106.8456)
  }
}
