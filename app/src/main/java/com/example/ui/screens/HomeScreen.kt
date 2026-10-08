package com.example.ui.screens
import com.example.ui.theme.spacing
import com.example.ui.theme.diajakGlassButton
import com.example.ui.theme.diajakGlassHeaderEffect
import com.example.ui.theme.HeaderUnderlayState
import com.example.ui.theme.GlassmorphismTheme
import com.example.ui.theme.HazeConfig
import com.example.ui.theme.HazeMode
import com.example.ui.theme.LocalHazeConfig
import com.example.ui.theme.ProvideHazeConfig
import androidx.compose.animation.animateContentSize
import androidx.compose.animation.animateColorAsState
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.tween
import androidx.compose.animation.core.LinearOutSlowInEasing
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.material3.MaterialTheme

import com.example.ui.components.SkeletonActivityCarousel
import com.example.ui.components.SkeletonBanner
import com.example.ui.components.DiajakGlassHeader
import com.example.ui.components.DiajakGlassConfig
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.automirrored.outlined.*
import androidx.compose.material.icons.outlined.*

import androidx.compose.foundation.pager.HorizontalPager
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.ui.graphics.Brush

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.ui.graphics.SolidColor
import androidx.compose.material.icons.Icons
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.foundation.layout.ExperimentalLayoutApi
import androidx.compose.foundation.gestures.detectTapGestures
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.drawWithContent
import androidx.compose.ui.graphics.BlendMode
import androidx.compose.ui.graphics.CompositingStrategy
import androidx.compose.ui.graphics.graphicsLayer
import dev.chrisbanes.haze.HazeState
import dev.chrisbanes.haze.HazeStyle
import dev.chrisbanes.haze.HazeTint
import dev.chrisbanes.haze.hazeSource
import dev.chrisbanes.haze.hazeEffect
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.focus.onFocusChanged
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.zIndex
import com.example.data.DiajakRepository
import com.example.model.ActivityModel
import com.example.model.CategoryItem
import com.example.model.PromoModel
import com.example.model.DiajakNotification
import com.example.ui.theme.DiajakOrange
import com.example.ui.theme.DiajakOrangeLight
import com.example.ui.theme.DiajakTextDark
import com.example.ui.theme.DiajakTextMuted
import com.example.ui.theme.RatingGold
import androidx.compose.ui.platform.LocalContext
import android.widget.Toast
import coil.compose.rememberAsyncImagePainter

data class HomeCityBannerItem(
  val id: String,
  val title: String,
  val subtitle: String,
  val buttonText: String,
  val imageResId: Int,
  val imageUrl: String,
  val filterValue: String
)

val homeCityBannersList = listOf(
  HomeCityBannerItem(
    id = "b_yogya",
    title = "Yogyakarta",
    subtitle = "Kota budaya dengan kuliner legendaris dan petualangan alam yang asri",
    buttonText = "Main ke Jogja",
    imageResId = com.example.R.drawable.diajak_onboarding_hero_1783253536983,
    imageUrl = "https://images.unsplash.com/photo-1584810359583-96fc3448beaa?w=600&auto=format&fit=crop",
    filterValue = "Yogyakarta"
  ),
  HomeCityBannerItem(
    id = "b_jkt",
    title = "Jakarta",
    subtitle = "Metropolitan dinamis dengan ribuan aktivitas modern dan hiburan seru",
    buttonText = "Jelajahi Jakarta",
    imageResId = com.example.R.drawable.diajak_banner_workshop_1783253562818,
    imageUrl = "https://images.unsplash.com/photo-1555899434-94d1368aa7af?w=600&auto=format&fit=crop",
    filterValue = "Jakarta"
  ),
  HomeCityBannerItem(
    id = "b_bdg",
    title = "Bandung",
    subtitle = "Surga kreativitas dengan udara sejuk, keindahan alam, dan kuliner khas",
    buttonText = "Jalan ke Bandung",
    imageResId = com.example.R.drawable.diajak_banner_outdoor_1783253550783,
    imageUrl = "https://images.unsplash.com/photo-1589308078059-be1415eab4c3?w=600&auto=format&fit=crop",
    filterValue = "Bandung"
  ),
  HomeCityBannerItem(
    id = "b_bali",
    title = "Bali",
    subtitle = "Pantai eksotis, yoga sunset menenangkan, dan festival seni budaya",
    buttonText = "Liburan ke Bali",
    imageResId = com.example.R.drawable.img_sunset_yoga_1783584057064,
    imageUrl = "https://images.unsplash.com/photo-1537996194471-e657df975ab4?w=600&auto=format&fit=crop",
    filterValue = "Bali"
  ),
  HomeCityBannerItem(
    id = "b_sby",
    title = "Surabaya",
    subtitle = "Kota pahlawan dengan aktivitas olahraga dinamis dan komunitas seru",
    buttonText = "Eksplor Surabaya",
    imageResId = com.example.R.drawable.img_sport_running_1783584004743,
    imageUrl = "https://images.unsplash.com/photo-1603813876022-7994689cb510?w=600&auto=format&fit=crop",
    filterValue = "Surabaya"
  )
)


fun getActivityLatLng(locationName: String): Pair<Double, Double> {
  return when {
    locationName.contains("Yogyakarta", ignoreCase = true) || locationName.contains("Jogja", ignoreCase = true) -> Pair(-7.7956, 110.3695)
    locationName.contains("Bali", ignoreCase = true) || locationName.contains("Kuta", ignoreCase = true) || locationName.contains("Legian", ignoreCase = true) || locationName.contains("Ubud", ignoreCase = true) || locationName.contains("Sanur", ignoreCase = true) -> Pair(-8.4095, 115.1889)
    locationName.contains("Bandung", ignoreCase = true) -> Pair(-6.9175, 107.6191)
    locationName.contains("Jakarta", ignoreCase = true) || locationName.contains("JKT", ignoreCase = true) || locationName.contains("Senopati", ignoreCase = true) || locationName.contains("Menteng", ignoreCase = true) -> Pair(-6.2088, 106.8456)
    locationName.contains("Bogor", ignoreCase = true) -> Pair(-6.5971, 106.8060)
    locationName.contains("Surabaya", ignoreCase = true) -> Pair(-7.2575, 112.7521)
    locationName.contains("Tangerang", ignoreCase = true) || locationName.contains("BSD", ignoreCase = true) -> Pair(-6.1783, 106.6319)
    else -> Pair(-7.7956, 110.3695) // Fallback center
  }
}

fun calculateDistanceKm(lat1: Double, lon1: Double, lat2: Double, lon2: Double): Double {
  val r = 6371.0 // Earth radius in km
  val dLat = Math.toRadians(lat2 - lat1)
  val dLon = Math.toRadians(lon2 - lon1)
  val a = Math.sin(dLat / 2) * Math.sin(dLat / 2) +
          Math.cos(Math.toRadians(lat1)) * Math.cos(Math.toRadians(lat2)) *
          Math.sin(dLon / 2) * Math.sin(dLon / 2)
  val c = 2 * Math.atan2(Math.sqrt(a), Math.sqrt(1 - a))
  return r * c
}

data class MapsCategory(
  val id: String,
  val name: String,
  val icon: androidx.compose.ui.graphics.vector.ImageVector
)

val mapsCategoriesList = listOf(
  MapsCategory("all", "Rumah", Icons.Outlined.Home),
  MapsCategory("culinary", "Restoran", Icons.Outlined.Restaurant),
  MapsCategory("workshop", "Belanja", Icons.Outlined.ShoppingBag),
  MapsCategory("outdoor", "Hotel", Icons.Outlined.LocalHotel),
  MapsCategory("coffee", "Kopi", Icons.Outlined.LocalCafe),
  MapsCategory("sport", "Olahraga", Icons.AutoMirrored.Outlined.DirectionsRun),
  MapsCategory("wellness", "Wellness", Icons.Outlined.Spa),
  MapsCategory("music", "Musik", Icons.Outlined.MusicNote),
  MapsCategory("gaming", "Gaming", Icons.Outlined.SportsEsports),
  MapsCategory("hobby", "Komunitas", Icons.Outlined.People),
  MapsCategory("education", "Seminar", Icons.Outlined.School),
  MapsCategory("nightlife", "Party", Icons.Outlined.Celebration)
)

data class PopularCityItem(
  val id: String,
  val cityName: String,
  val filterValue: String,
  val imageResId: Int,
  val imageUrl: String
)

val popularCitiesList = listOf(
  PopularCityItem("c_yogya", "Yogyakarta", "Yogyakarta", com.example.R.drawable.diajak_onboarding_hero_1783253536983, "https://images.unsplash.com/photo-1584810359583-96fc3448beaa?w=300&auto=format&fit=crop"),
  PopularCityItem("c_jkt", "Jakarta", "Jakarta", com.example.R.drawable.diajak_banner_workshop_1783253562818, "https://images.unsplash.com/photo-1555899434-94d1368aa7af?w=300&auto=format&fit=crop"),
  PopularCityItem("c_bdg", "Bandung", "Bandung", com.example.R.drawable.diajak_banner_outdoor_1783253550783, "https://images.unsplash.com/photo-1589308078059-be1415eab4c3?w=300&auto=format&fit=crop"),
  PopularCityItem("c_bali", "Bali", "Bali", com.example.R.drawable.img_sunset_yoga_1783584057064, "https://images.unsplash.com/photo-1537996194471-e657df975ab4?w=300&auto=format&fit=crop"),
  PopularCityItem("c_sby", "Surabaya", "Surabaya", com.example.R.drawable.img_sport_running_1783584004743, "https://images.unsplash.com/photo-1603813876022-7994689cb510?w=300&auto=format&fit=crop"),
  PopularCityItem("c_smg", "Semarang", "Semarang", com.example.R.drawable.diajak_activity_coffee_1783253596378, "https://images.unsplash.com/photo-1596402184320-417e7178b2cd?w=300&auto=format&fit=crop"),
  PopularCityItem("c_solo", "Solo", "Solo", com.example.R.drawable.img_culinary_baking_1783584043430, "https://images.unsplash.com/photo-1578469550956-0e16b69c6a3d?w=300&auto=format&fit=crop"),
  PopularCityItem("c_mlg", "Malang", "Malang", com.example.R.drawable.diajak_activity_glamping_1783253582807, "https://images.unsplash.com/photo-1588668214407-6ea9a6d8c272?w=300&auto=format&fit=crop"),
  PopularCityItem("c_bgr", "Bogor", "Bogor", com.example.R.drawable.img_padel_tennis_1783584027406, "https://images.unsplash.com/photo-1507525428034-b723cf961d3e?w=300&auto=format&fit=crop")
)

@OptIn(ExperimentalLayoutApi::class, ExperimentalMaterial3Api::class)
@Composable
fun HomeScreen(
  activities: List<ActivityModel>,
  categories: List<CategoryItem>,
  selectedCategory: String,
  searchQuery: String,
  selectedLocation: String,
  isKreatorMode: Boolean,
  notifications: List<DiajakNotification> = emptyList(),
  userName: String = "Anggi Saputro",
  onSelectCategory: (String) -> Unit,
  onSearchChange: (String) -> Unit,
  onSearchFocus: () -> Unit = {},
  onActivityClick: (ActivityModel) -> Unit,
  onOpenCreate: () -> Unit,
  onToggleKreatorMode: () -> Unit,
  onNavigateToExplore: () -> Unit = {},
  onSelectCityFilter: (String) -> Unit = {},
  onNavigateToNotifications: () -> Unit = {},
  onPromoClick: (String) -> Unit = {},
  onMarkAllAsRead: () -> Unit = {},
  onClearAll: () -> Unit = {},
  onDetectLocation: () -> Unit = {},
  isLocating: Boolean = false,
  onNotificationClick: (DiajakNotification) -> Unit = {},
  favoriteActivityIds: Set<String> = emptySet(),
  favoriteAcaraIds: Set<String> = favoriteActivityIds,
  onToggleFavorite: (ActivityModel) -> Unit = {},
  userCoordinates: Pair<Double, Double>? = null,
  profileImageUri: String? = null,
  profileImageRes: Int? = null,
  onProfileClick: () -> Unit = {},
  isLoading: Boolean = false
) {
  val context = LocalContext.current
  val focusManager = LocalFocusManager.current
  var isNotificationSheetOpen by remember { mutableStateOf(false) }
  var isLocationSheetOpen by remember { mutableStateOf(false) }

  val greetingText = remember {
    val hour = java.util.Calendar.getInstance().get(java.util.Calendar.HOUR_OF_DAY)
    when (hour) {
      in 4..10 -> "Selamat Pagi"
      in 11..14 -> "Selamat Siang"
      in 15..18 -> "Selamat Sore"
      else -> "Selamat Malam"
    }
  }
  val displayName = remember(userName) {
    userName.trim().split(" ").firstOrNull()?.replaceFirstChar { if (it.isLowerCase()) it.titlecase() else it.toString() } ?: userName
  }

  val hasUnreadNotifications = remember(notifications, isKreatorMode) {
    notifications.any { !it.isRead && (if (isKreatorMode) it.role == "kreator" else it.role == "peserta") }
  }

  val seruActivities = remember(activities) {
    activities.sortedWith(
      compareByDescending<ActivityModel> { it.currentPeserta }
        .thenByDescending { it.reviewsCount }
    )
  }
  val terdekatActivities = remember(activities, selectedLocation, userCoordinates) {
    if (userCoordinates != null) {
      activities.sortedBy { activity ->
        val actCoords = getActivityLatLng(activity.locationName)
        calculateDistanceKm(userCoordinates.first, userCoordinates.second, actCoords.first, actCoords.second)
      }
    } else {
      activities.sortedWith(
        compareByDescending<ActivityModel> { activity ->
          val isMatch = if (selectedLocation.contains("Yogyakarta", ignoreCase = true)) {
            activity.locationName.contains("Yogyakarta", ignoreCase = true) || activity.locationName.contains("Jogja", ignoreCase = true)
          } else if (selectedLocation.contains("Bali", ignoreCase = true)) {
            activity.locationName.contains("Bali", ignoreCase = true) || activity.locationName.contains("Kuta", ignoreCase = true) || activity.locationName.contains("Legian", ignoreCase = true) || activity.locationName.contains("Ubud", ignoreCase = true) || activity.locationName.contains("Sanur", ignoreCase = true)
          } else if (selectedLocation.contains("Bandung", ignoreCase = true)) {
            activity.locationName.contains("Bandung", ignoreCase = true)
          } else if (selectedLocation.contains("Jakarta", ignoreCase = true)) {
            activity.locationName.contains("Jakarta", ignoreCase = true) || activity.locationName.contains("JKT", ignoreCase = true)
          } else {
            activity.locationName.contains(selectedLocation, ignoreCase = true)
          }
          isMatch
        }.thenByDescending { it.rating }
      )
    }
  }
  val terbaruActivities = remember(activities) {
    val customActivities = activities.filter { it.id.length > 5 }
    val standardActivities = activities.filter { it.id.length <= 5 }.reversed()
    customActivities + standardActivities
  }
  val pilihanActivities = remember(activities) {
    activities.sortedWith(
      compareByDescending<ActivityModel> { it.rating }
        .thenByDescending { it.reviewsCount }
    )
  }

  // Urutan kategori berdasarkan yang paling banyak dibuat / digunakan oleh kreator
  val sortedCategories = remember(categories, activities) {
    categories.filter { it.id != "all" }.sortedWith(
      compareByDescending<CategoryItem> { cat ->
        val catName = cat.name.lowercase()
        val catId = cat.id.lowercase()
        activities.count { act ->
          val actCat = act.category.lowercase()
          actCat.contains(catName) || actCat.contains(catId) ||
          when (catId) {
            "kopi" -> actCat.contains("kopi") || actCat.contains("coffee") || actCat.contains("cupping")
            "alam" -> actCat.contains("alam") || actCat.contains("outdoor") || actCat.contains("kemah") || actCat.contains("pendakian") || actCat.contains("beach") || actCat.contains("glamping")
            "kuliner" -> actCat.contains("kuliner") || actCat.contains("cooking") || actCat.contains("baking") || actCat.contains("food")
            "olahraga" -> actCat.contains("olahraga") || actCat.contains("sport") || actCat.contains("tennis") || actCat.contains("padel") || actCat.contains("lari") || actCat.contains("run")
            "lari" -> actCat.contains("lari") || actCat.contains("run")
            "kebugaran" -> actCat.contains("kebugaran") || actCat.contains("gym") || actCat.contains("fitness") || actCat.contains("yoga")
            "meditasi", "kesejahteraan" -> actCat.contains("yoga") || actCat.contains("meditasi") || actCat.contains("wellness")
            "fotografi" -> actCat.contains("foto") || actCat.contains("photo")
            "kriya", "lokakarya" -> actCat.contains("kriya") || actCat.contains("workshop") || actCat.contains("clay") || actCat.contains("keramik") || actCat.contains("craft")
            "seni" -> actCat.contains("seni") || actCat.contains("art") || actCat.contains("lukis")
            "musik" -> actCat.contains("musik") || actCat.contains("music") || actCat.contains("konser")
            else -> false
          }
        }
      }.thenBy { it.name }
    )
  }

  val density = androidx.compose.ui.platform.LocalDensity.current
  val statusBarHeightDp = with(density) {
    WindowInsets.statusBars.getTop(this).toDp()
  }

  val screenWidthDp = androidx.compose.ui.platform.LocalConfiguration.current.screenWidthDp.dp
  val carouselCardSpacing = 14.dp
  val carouselCardWidth = (screenWidthDp - 40.dp - carouselCardSpacing) / 2

  val lazyListState = rememberLazyListState()

  LaunchedEffect(lazyListState.isScrollInProgress) {
    if (lazyListState.isScrollInProgress) {
      focusManager.clearFocus()
    }
  }

  val hazeState = remember { HazeState() }

  Box(
    modifier = Modifier
      .fillMaxSize()
      .background(androidx.compose.ui.graphics.Color(0xFFE5E7EB))
      .pointerInput(Unit) {
        detectTapGestures(onTap = {
          focusManager.clearFocus()
        })
      }
  ) {
    LazyColumn(
      state = lazyListState,
      modifier = Modifier
        .fillMaxSize()
        .hazeSource(state = hazeState),
      contentPadding = PaddingValues(bottom = 124.dp)
    ) {
      // 1. Kolom Pencarian Putih Bersih (Langsung di atas, dengan safe status bar spacing)
      item(contentType = HeaderUnderlayState.CONTAINER) {
        Spacer(modifier = Modifier.statusBarsPadding().height(20.dp))
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
              .padding(horizontal = 14.dp),
            verticalAlignment = Alignment.CenterVertically
          ) {
            Icon(
              imageVector = Icons.Outlined.Search,
              contentDescription = "Cari",
              tint = Color(0xFF0F172A),
              modifier = Modifier
                .size(20.dp)
                .clickable {
                  focusManager.clearFocus()
                  if (searchQuery.isNotBlank()) {
                    onNavigateToExplore()
                  }
                }
            )
            Spacer(modifier = Modifier.width(10.dp))
            Box(
              modifier = Modifier
                .weight(1f)
                .fillMaxHeight(),
              contentAlignment = Alignment.CenterStart
            ) {
              if (searchQuery.isEmpty()) {
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
                value = searchQuery,
                onValueChange = onSearchChange,
                singleLine = true,
                keyboardOptions = KeyboardOptions(
                  keyboardType = androidx.compose.ui.text.input.KeyboardType.Text,
                  imeAction = ImeAction.Search
                ),
                keyboardActions = KeyboardActions(
                  onSearch = {
                    focusManager.clearFocus()
                    if (searchQuery.isNotBlank()) {
                      onNavigateToExplore()
                    }
                  }
                ),
                textStyle = TextStyle(
                  fontSize = 13.5.sp,
                  color = Color(0xFF0F172A),
                  fontWeight = FontWeight.Medium
                ),
                cursorBrush = SolidColor(DiajakOrange),
                modifier = Modifier.fillMaxWidth()
              )
            }
            if (searchQuery.isNotEmpty()) {
              IconButton(
                onClick = { onSearchChange("") },
                modifier = Modifier.size(28.dp)
              ) {
                Icon(
                  imageVector = Icons.Outlined.Close,
                  contentDescription = "Hapus Pencarian",
                  tint = Color(0xFF64748B),
                  modifier = Modifier.size(16.dp)
                )
              }
            }
          }
        }
        Spacer(modifier = Modifier.height(20.dp))
      }

      // 2. Sliding Banners - Berada tepat di bawah Kolom Pencarian
      item(contentType = HeaderUnderlayState.PHOTO) {
        HomeSlidingBanners(
          onSelectCityFilter = onSelectCityFilter,
          statusBarHeightDp = statusBarHeightDp
        )
      }

      // 3. Kategori Kapsul (Pills dengan Ikon Berwarna) - Berada di Bawah Banner
      item(contentType = HeaderUnderlayState.CONTAINER) {
        Spacer(modifier = Modifier.height(20.dp))
        LazyRow(
          modifier = Modifier.fillMaxWidth(),
          contentPadding = PaddingValues(horizontal = 20.dp),
          horizontalArrangement = Arrangement.spacedBy(MaterialTheme.spacing.medium)
        ) {
          items(sortedCategories, key = { cat -> cat.id }) { cat ->
            val isSelected = selectedCategory == cat.id
            CategoryCapsuleCard(
              category = cat,
              isSelected = isSelected,
              onClick = {
                onSelectCategory(cat.id)
                onNavigateToExplore()
              }
            )
          }
        }
      }



    // Activities Section Header
    item(contentType = HeaderUnderlayState.TEXT) {
      Column {
        Row(
          modifier = Modifier
            .fillMaxWidth()
            .padding(start = 20.dp, end = 20.dp, top = 20.dp, bottom = 20.dp),
          horizontalArrangement = Arrangement.SpaceBetween,
          verticalAlignment = Alignment.CenterVertically
        ) {
          Text(
            text = "Seru di Sekitarmu",
            style = com.example.ui.theme.DiajakDesignSystem.Typography.Title.copy(
                fontWeight = androidx.compose.ui.text.font.FontWeight.Bold,
                fontSize = 16.sp
            ),
            color = MaterialTheme.colorScheme.onSurface
          )
        }
      }
    }

    item(contentType = HeaderUnderlayState.PHOTO) {
      if (isLoading) {
        SkeletonActivityCarousel()
      } else {
        val sidePadding = 20.dp
        val cardSpacing = 12.dp
        val calculatedCardWidth = (screenWidthDp - (sidePadding * 2) - cardSpacing) / 2
        LazyRow(
          contentPadding = PaddingValues(horizontal = sidePadding),
          horizontalArrangement = Arrangement.spacedBy(cardSpacing),
          modifier = Modifier.fillMaxWidth()
        ) {
          items(terdekatActivities, key = { act -> act.id }) { activity ->
            ActivityScheduleCard(
              activity = activity,
              onClick = { onActivityClick(activity) },
              favoriteActivityIds = favoriteActivityIds,
              favoriteAcaraIds = favoriteAcaraIds,
              onToggleFavorite = onToggleFavorite,
              cardWidth = calculatedCardWidth
            )
          }
        }
      }
    }

    item(contentType = HeaderUnderlayState.TEXT) {
      Column {
        Row(
          modifier = Modifier
            .fillMaxWidth()
            .padding(start = 20.dp, end = 20.dp, top = 20.dp, bottom = 20.dp),
          horizontalArrangement = Arrangement.SpaceBetween,
          verticalAlignment = Alignment.CenterVertically
        ) {
          Text(
            text = "Lagi Ramai Diikuti",
            style = com.example.ui.theme.DiajakDesignSystem.Typography.Title.copy(
                fontWeight = androidx.compose.ui.text.font.FontWeight.Bold,
                fontSize = 16.sp
            ),
            color = MaterialTheme.colorScheme.onSurface
          )
        }
      }
    }

    item(contentType = HeaderUnderlayState.PHOTO) {
      if (isLoading) {
        SkeletonActivityCarousel()
      } else {
        val sidePadding = 20.dp
        val cardSpacing = 12.dp
        val calculatedCardWidth = (screenWidthDp - (sidePadding * 2) - cardSpacing) / 2
        LazyRow(
          contentPadding = PaddingValues(horizontal = sidePadding),
          horizontalArrangement = Arrangement.spacedBy(cardSpacing),
          modifier = Modifier.fillMaxWidth()
        ) {
          items(seruActivities, key = { act -> act.id }) { activity ->
            ActivityCarouselCard(
              activity = activity,
              onClick = { onActivityClick(activity) },
              favoriteActivityIds = favoriteActivityIds,
              favoriteAcaraIds = favoriteAcaraIds,
              onToggleFavorite = onToggleFavorite,
              cardWidth = calculatedCardWidth
            )
          }
        }
      }
    }

    // Aktivitas Rekomendasi Carousel
    item(contentType = HeaderUnderlayState.TEXT) {
      Column {
        Row(
          modifier = Modifier
            .fillMaxWidth()
            .padding(start = 20.dp, end = 20.dp, top = 20.dp, bottom = 20.dp),
          horizontalArrangement = Arrangement.SpaceBetween,
          verticalAlignment = Alignment.CenterVertically
        ) {
          Text(
            text = "Ratingnya Juara",
            style = com.example.ui.theme.DiajakDesignSystem.Typography.Title.copy(
                fontWeight = androidx.compose.ui.text.font.FontWeight.Bold,
                fontSize = 16.sp
            ),
            color = MaterialTheme.colorScheme.onSurface
          )
        }
      }
    }

    item(contentType = HeaderUnderlayState.PHOTO) {
      if (isLoading) {
        SkeletonActivityCarousel()
      } else {
        val sidePadding = 20.dp
        val cardSpacing = 12.dp
        val calculatedCardWidth = (screenWidthDp - (sidePadding * 2) - cardSpacing) / 2
        LazyRow(
          contentPadding = PaddingValues(horizontal = sidePadding),
          horizontalArrangement = Arrangement.spacedBy(cardSpacing),
          modifier = Modifier.fillMaxWidth()
        ) {
          items(pilihanActivities, key = { act -> act.id }) { activity ->
            ActivityCarouselCard(
              activity = activity,
              onClick = { onActivityClick(activity) },
              favoriteActivityIds = favoriteActivityIds,
              favoriteAcaraIds = favoriteAcaraIds,
              onToggleFavorite = onToggleFavorite,
              cardWidth = calculatedCardWidth
            )
          }
        }
      }
    }

    item(contentType = HeaderUnderlayState.TEXT) {
      Column {
        Row(
          modifier = Modifier
            .fillMaxWidth()
            .padding(start = 20.dp, end = 20.dp, top = 20.dp, bottom = 20.dp),
          horizontalArrangement = Arrangement.SpaceBetween,
          verticalAlignment = Alignment.CenterVertically
        ) {
          Text(
            text = "Ada Yang Baru Loh",
            style = com.example.ui.theme.DiajakDesignSystem.Typography.Title.copy(
                fontWeight = androidx.compose.ui.text.font.FontWeight.Bold,
                fontSize = 16.sp
            ),
            color = MaterialTheme.colorScheme.onSurface
          )
        }
      }
    }

    item(contentType = HeaderUnderlayState.PHOTO) {
      if (isLoading) {
        SkeletonActivityCarousel()
      } else {
        val sidePadding = 20.dp
        val cardSpacing = 12.dp
        val calculatedCardWidth = (screenWidthDp - (sidePadding * 2) - cardSpacing) / 2
        LazyRow(
          contentPadding = PaddingValues(horizontal = sidePadding),
          horizontalArrangement = Arrangement.spacedBy(cardSpacing),
          modifier = Modifier.fillMaxWidth()
        ) {
          items(terbaruActivities, key = { act -> act.id }) { activity ->
            ActivityNewestCard(
              activity = activity,
              onClick = { onActivityClick(activity) },
              favoriteActivityIds = favoriteActivityIds,
              favoriteAcaraIds = favoriteAcaraIds,
              onToggleFavorite = onToggleFavorite,
              cardWidth = calculatedCardWidth
            )
          }
        }
      }
    }
  }

  val density = LocalDensity.current

  val isScrolled by remember {
    derivedStateOf {
      lazyListState.firstVisibleItemIndex > 0 || lazyListState.firstVisibleItemScrollOffset > 0
    }
  }

  // 2. Pure Optical Glass Header Layer (100% Pure Optical Haze - iOS & Tinder Style)
  if (isScrolled) {
    DiajakGlassHeader(
      hazeState = hazeState,
      lazyListState = lazyListState,
      containerColor = Color(0xFFE5E7EB),
      modifier = Modifier
        .align(Alignment.TopCenter)
        .zIndex(10f)
    ) {
      Box(
        modifier = Modifier
          .fillMaxWidth()
          .statusBarsPadding()
          .height(56.dp)
      )
    }
  }

  // Location Selection Modal Bottom Sheet
  if (isLocationSheetOpen) {
    ModalBottomSheet(
      onDismissRequest = { isLocationSheetOpen = false },
      containerColor = Color(0xFFF3F4F6),
      dragHandle = {
        BottomSheetDefaults.DragHandle(
          color = Color(0xFFCBD5E1)
        )
      }
    ) {
      Column(
        modifier = Modifier
          .fillMaxWidth()
          .padding(horizontal = 20.dp)
          .padding(bottom = MaterialTheme.spacing.medium)
      ) {
        Text(
          text = "Pilih Lokasi",
          style = com.example.ui.theme.DiajakDesignSystem.Typography.Headline,
          color = MaterialTheme.colorScheme.onSurface
        )
        Text(
          text = "Temukan berbagai aktivitas & komunitas seru di sekitarmu",
          style = com.example.ui.theme.DiajakDesignSystem.Typography.Body.copy(
            fontSize = 13.sp,
            color = DiajakTextMuted
          ),
          modifier = Modifier.padding(top = 4.dp, bottom = 16.dp)
        )

        // GPS Auto Detection Button
        Surface(
          modifier = Modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(16.dp))
            .clickable {
              onDetectLocation()
              isLocationSheetOpen = false
            },
          shape = RoundedCornerShape(16.dp),
          color = Color.White,
          shadowElevation = 0.dp
        ) {
          Row(
            modifier = Modifier
              .fillMaxWidth()
              .padding(horizontal = 16.dp, vertical = 14.dp),
            verticalAlignment = Alignment.CenterVertically
          ) {
            Box(
              modifier = Modifier
                .size(36.dp)
                .clip(CircleShape)
                .background(DiajakOrange.copy(alpha = 0.12f)),
              contentAlignment = Alignment.Center
            ) {
              if (isLocating) {
                CircularProgressIndicator(
                  modifier = Modifier.size(18.dp),
                  color = DiajakOrange,
                  strokeWidth = 2.dp
                )
              } else {
                Icon(
                  imageVector = Icons.Outlined.MyLocation,
                  contentDescription = null,
                  tint = DiajakOrange,
                  modifier = Modifier.size(20.dp)
                )
              }
            }
            Spacer(modifier = Modifier.width(12.dp))
            Column(modifier = Modifier.weight(1f)) {
              Text(
                text = "Gunakan Lokasi Saat Ini (GPS)",
                style = com.example.ui.theme.DiajakDesignSystem.Typography.BodyBold,
                color = MaterialTheme.colorScheme.onSurface
              )
              Text(
                text = if (isLocating) "Mendeteksi posisi..." else "Deteksi lokasi otomatis via GPS perangkat",
                style = com.example.ui.theme.DiajakDesignSystem.Typography.Body.copy(
                  fontSize = 12.sp,
                  color = DiajakTextMuted
                )
              )
            }
            Icon(
              imageVector = Icons.AutoMirrored.Outlined.ArrowForwardIos,
              contentDescription = null,
              tint = Color(0xFF94A3B8),
              modifier = Modifier.size(14.dp)
            )
          }
        }

        Spacer(modifier = Modifier.height(MaterialTheme.spacing.medium))

        Text(
          text = "Kota Populer",
          style = com.example.ui.theme.DiajakDesignSystem.Typography.BodyBold.copy(fontSize = 14.sp),
          color = MaterialTheme.colorScheme.onSurface,
          modifier = Modifier.padding(bottom = 8.dp)
        )

        // List of popular cities
        Surface(
          modifier = Modifier.fillMaxWidth(),
          shape = RoundedCornerShape(16.dp),
          color = Color.White,
          shadowElevation = 0.dp
        ) {
          Column(modifier = Modifier.fillMaxWidth()) {
            popularCitiesList.forEachIndexed { index, city ->
              val isSelected = selectedLocation.contains(city.cityName, ignoreCase = true)
              Row(
                modifier = Modifier
                  .fillMaxWidth()
                  .clickable {
                    onSelectCityFilter(city.filterValue)
                    isLocationSheetOpen = false
                  }
                  .padding(horizontal = 16.dp, vertical = 12.dp),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.SpaceBetween
              ) {
                Row(verticalAlignment = Alignment.CenterVertically) {
                  Icon(
                    imageVector = Icons.Outlined.LocationCity,
                    contentDescription = null,
                    tint = if (isSelected) DiajakOrange else Color(0xFF94A3B8),
                    modifier = Modifier.size(18.dp)
                  )
                  Spacer(modifier = Modifier.width(12.dp))
                  Text(
                    text = city.cityName,
                    style = if (isSelected) com.example.ui.theme.DiajakDesignSystem.Typography.BodyBold else com.example.ui.theme.DiajakDesignSystem.Typography.Body,
                    color = if (isSelected) DiajakOrange else MaterialTheme.colorScheme.onSurface
                  )
                }
                if (isSelected) {
                  Icon(
                    imageVector = Icons.Outlined.Check,
                    contentDescription = "Terpilih",
                    tint = DiajakOrange,
                    modifier = Modifier.size(18.dp)
                  )
                }
              }
              if (index < popularCitiesList.size - 1) {
                HorizontalDivider(
                  color = Color(0xFFF1F5F9),
                  thickness = 1.dp,
                  modifier = Modifier.padding(horizontal = 16.dp)
                )
              }
            }
          }
        }
      }
    }
  }
}
}

@Composable
fun PromoCard(promo: PromoModel, onClick: () -> Unit = {}) {
  Card(
    onClick = onClick,
    modifier = Modifier
      .width(356.dp)
      .height(160.dp),
    shape = RoundedCornerShape(16.dp),
    colors = CardDefaults.cardColors(containerColor = Color.White),
    border = BorderStroke(0.dp, Color.Transparent),
    elevation = CardDefaults.cardElevation(defaultElevation = 0.dp)
  ) {
    Box(modifier = Modifier.fillMaxSize()) {
      Image(
        painter = painterResource(id = promo.imageResId),
        contentDescription = promo.title,
        contentScale = ContentScale.Crop,
        modifier = Modifier.fillMaxSize()
      )
      // Dark overlay
      Box(
        modifier = Modifier
          .fillMaxSize()
          .background(androidx.compose.material3.MaterialTheme.colorScheme.onSurface.copy(alpha = 0.45f))
      )
      Column(
        modifier = Modifier
          .fillMaxSize()
          .padding(MaterialTheme.spacing.medium),
        verticalArrangement = Arrangement.SpaceBetween
      ) {
        Row(
          modifier = Modifier.fillMaxWidth(),
          horizontalArrangement = Arrangement.SpaceBetween,
          verticalAlignment = Alignment.CenterVertically
        ) {
          Surface(
            color = DiajakOrange,
            shape = RoundedCornerShape(16.dp)
          ) {
            Text(
              text = promo.badge,
              color = Color.White,
              style = com.example.ui.theme.DiajakDesignSystem.Typography.Body,
              
              modifier = Modifier.padding(horizontal = MaterialTheme.spacing.small, vertical = MaterialTheme.spacing.extraSmall)
            )
          }
        }
        Column {
          Text(
            text = promo.title,
            color = Color.White,
            style = com.example.ui.theme.DiajakDesignSystem.Typography.Body.copy(
              fontSize = 16.sp,
              fontWeight = FontWeight.Bold
            ),
            maxLines = 1,
            overflow = TextOverflow.Ellipsis
          )
          Spacer(modifier = Modifier.height(MaterialTheme.spacing.extraSmall))
          Text(
            text = promo.subtitle,
            style = com.example.ui.theme.DiajakDesignSystem.Typography.Body,
            color = Color.White.copy(alpha = 0.9f),
            maxLines = 2,
            overflow = TextOverflow.Ellipsis,
            lineHeight = 18.sp
          )
        }
      }
    }
  }
}

@Composable
fun CategoryCapsuleCard(
  category: com.example.model.CategoryItem,
  isSelected: Boolean = false,
  onClick: () -> Unit
) {
  Surface(
    modifier = Modifier
      .height(42.dp)
      .clip(RoundedCornerShape(21.dp))
      .clickable(
        interactionSource = remember { MutableInteractionSource() },
        indication = null,
        onClick = onClick
      ),
    shape = RoundedCornerShape(21.dp),
    color = if (isSelected) Color(0xFF0F172A) else Color.White,
    shadowElevation = 0.dp,
    border = BorderStroke(0.dp, Color.Transparent)
  ) {
    Row(
      modifier = Modifier
        .fillMaxHeight()
        .padding(start = 5.dp, end = 16.dp),
      verticalAlignment = Alignment.CenterVertically,
      horizontalArrangement = Arrangement.spacedBy(MaterialTheme.spacing.small)
    ) {
      // Small circular container keeping the category icon colorful
      Box(
        modifier = Modifier
          .size(32.dp)
          .clip(CircleShape)
          .background(
            if (isSelected) Color.White.copy(alpha = 0.2f) else Color(0xFFFFF7ED)
          ),
        contentAlignment = Alignment.Center
      ) {
        Text(
          text = category.icon,
          fontSize = 16.sp,
          textAlign = TextAlign.Center
        )
      }

      Text(
        text = category.name,
        style = TextStyle(
          fontSize = 13.sp,
          fontWeight = FontWeight.Bold,
          color = if (isSelected) Color.White else Color(0xFF0F172A)
        ),
        maxLines = 1,
        overflow = TextOverflow.Ellipsis
      )
    }
  }
}

@Composable
fun CategoryCircleCard(
  category: com.example.model.CategoryItem,
  onClick: () -> Unit
) {
  Column(
    horizontalAlignment = Alignment.CenterHorizontally,
    modifier = Modifier
      .clickable(
        interactionSource = remember { MutableInteractionSource() },
        indication = null,
        onClick = onClick
      )
      .width(78.dp)
  ) {
    Surface(
      modifier = Modifier.size(66.dp),
      shape = RoundedCornerShape(22.dp),
      color = Color.White,
      shadowElevation = 0.dp,
      border = BorderStroke(0.dp, Color.Transparent)
    ) {
      Box(
        modifier = Modifier
          .fillMaxSize()
          .background(
            Brush.verticalGradient(
              colors = listOf(Color.White, Color(0xFFFFF4F0))
            )
          ),
        contentAlignment = Alignment.Center
      ) {
        Text(
          text = category.icon,
          fontSize = 28.sp
        )
      }
    }

    Spacer(modifier = Modifier.height(MaterialTheme.spacing.small))

    Text(
      text = category.name,
      style = TextStyle(
        fontSize = 12.5.sp,
        fontWeight = FontWeight.Bold,
        color = MaterialTheme.colorScheme.onSurface
      ),
      maxLines = 1,
      overflow = TextOverflow.Ellipsis,
      textAlign = androidx.compose.ui.text.style.TextAlign.Center
    )
  }
}

@Composable
fun CityCircleCard(
  city: PopularCityItem,
  onClick: () -> Unit
) {
  Column(
    horizontalAlignment = Alignment.CenterHorizontally,
    modifier = Modifier
      .clickable(
        interactionSource = remember { MutableInteractionSource() },
        indication = null,
        onClick = onClick
      )
      .width(72.dp)
  ) {
    Surface(
      modifier = Modifier.size(64.dp),
      shape = CircleShape,
      color = Color.White,
      shadowElevation = 0.dp,
      border = BorderStroke(0.dp, Color.Transparent)
    ) {
      Box(
        modifier = Modifier
          .fillMaxSize()
          .padding(1.dp)
          .clip(CircleShape)
      ) {
        Image(
          painter = rememberAsyncImagePainter(
            model = city.imageUrl,
            error = painterResource(id = city.imageResId),
            placeholder = painterResource(id = city.imageResId)
          ),
          contentDescription = city.cityName,
          contentScale = ContentScale.Crop,
          modifier = Modifier.fillMaxSize()
        )
      }
    }

    Spacer(modifier = Modifier.height(MaterialTheme.spacing.small))

    Text(
      text = city.cityName,
      style = com.example.ui.theme.DiajakDesignSystem.Typography.BodyBold.copy(
        fontSize = 12.sp,
        fontWeight = FontWeight.SemiBold
      ),
      color = MaterialTheme.colorScheme.onSurface,
      maxLines = 1,
      overflow = TextOverflow.Ellipsis
    )
  }
}

@Composable
fun ActivityCardItem(activity: ActivityModel, onClick: () -> Unit) {
  Card(
    onClick = onClick,
    modifier = Modifier
      .fillMaxWidth()
      .padding(horizontal = 20.dp, vertical = 6.dp),
    shape = RoundedCornerShape(16.dp),
    colors = CardDefaults.cardColors(containerColor = Color.White),
    border = BorderStroke(0.dp, Color.Transparent),
    elevation = CardDefaults.cardElevation(defaultElevation = 0.dp)
  ) {
    Row(
      modifier = Modifier
        .fillMaxWidth()
        .padding(MaterialTheme.spacing.medium),
      verticalAlignment = Alignment.CenterVertically
    ) {
      Box(
        modifier = Modifier
          .size(104.dp)
          .clip(RoundedCornerShape(18.dp))
      ) {
        Image(
          painter = painterResource(id = activity.imageResId),
          contentDescription = activity.title,
          contentScale = ContentScale.Crop,
          modifier = Modifier.fillMaxSize()
        )
        // Rating Badge
        Surface(
          color = androidx.compose.material3.MaterialTheme.colorScheme.onSurface.copy(alpha = 0.75f),
          shape = RoundedCornerShape(bottomEnd = 10.dp),
          modifier = Modifier.align(Alignment.TopStart)
        ) {
          Row(
            modifier = Modifier.padding(horizontal = MaterialTheme.spacing.small, vertical = MaterialTheme.spacing.extraSmall),
            verticalAlignment = Alignment.CenterVertically
          ) {
            Icon(Icons.Outlined.Star, contentDescription = "Rating", tint = RatingGold, modifier = Modifier.size(13.dp))
            Spacer(modifier = Modifier.width(3.dp))
            Text(text = "${activity.rating}", color = Color.White, style = com.example.ui.theme.DiajakDesignSystem.Typography.Body.copy(fontSize = 13.sp, fontWeight = FontWeight.Bold))
          }
        }
      }

      Spacer(modifier = Modifier.width(MaterialTheme.spacing.medium))

      Column(modifier = Modifier.weight(1f)) {
        Row(
          modifier = Modifier.fillMaxWidth(),
          horizontalArrangement = Arrangement.SpaceBetween,
          verticalAlignment = Alignment.CenterVertically
        ) {
          Surface(
            color = DiajakOrangeLight,
            shape = RoundedCornerShape(8.dp)
          ) {
            Text(
              text = activity.category,
              color = DiajakOrange,
              fontSize = 11.sp,
              fontWeight = FontWeight.Bold,
              modifier = Modifier.padding(horizontal = MaterialTheme.spacing.small, vertical = 3.dp)
            )
          }
          Row(verticalAlignment = Alignment.CenterVertically) {
            Icon(Icons.Outlined.Group, contentDescription = null, modifier = Modifier.size(12.dp), tint = MaterialTheme.colorScheme.onSurface)
            Spacer(modifier = Modifier.width(MaterialTheme.spacing.extraSmall))
            Text(
              text = "${activity.currentPeserta}/${activity.maxPeserta}",
              color = MaterialTheme.colorScheme.onSurface,
              style = com.example.ui.theme.DiajakDesignSystem.Typography.Body.copy(fontSize = 12.sp, fontWeight = FontWeight.SemiBold)
            )
          }
        }

        Spacer(modifier = Modifier.height(MaterialTheme.spacing.small))

        Text(
          text = activity.title,
          style = com.example.ui.theme.DiajakDesignSystem.Typography.BodyBold,
          color = MaterialTheme.colorScheme.onSurface,
          maxLines = 2,
          overflow = TextOverflow.Ellipsis,
          lineHeight = 20.sp
        )

        Spacer(modifier = Modifier.height(MaterialTheme.spacing.small))

        Row(verticalAlignment = Alignment.CenterVertically) {
          Icon(Icons.Outlined.Place, contentDescription = "Loc", tint = DiajakOrange, modifier = Modifier.size(14.dp))
          Spacer(modifier = Modifier.width(MaterialTheme.spacing.extraSmall))
          Text(
            text = activity.locationName.split(",").firstOrNull()?.trim() ?: activity.locationName,
            style = TextStyle(
              fontSize = 12.sp,
              fontWeight = FontWeight.Medium,
              color = DiajakTextMuted
            ),
            maxLines = 1,
            overflow = TextOverflow.Ellipsis
          )
        }

        Spacer(modifier = Modifier.height(MaterialTheme.spacing.small))

        Row(
          modifier = Modifier.fillMaxWidth(),
          horizontalArrangement = Arrangement.SpaceBetween,
          verticalAlignment = Alignment.CenterVertically
        ) {
          val displayPrice = if (activity.priceValue == 0) "Gratis" else "Rp " + String.format("%,d", activity.priceValue).replace(',', '.')
          Text(
            text = displayPrice,
            style = TextStyle(
              fontSize = 14.5.sp,
              fontWeight = FontWeight.Bold,
              color = DiajakOrange
            )
          )
          Text(
            text = "Kreator: ${activity.kreatorName.take(12)}...",
            style = com.example.ui.theme.DiajakDesignSystem.Typography.Body.copy(fontSize = 11.5.sp, color = DiajakTextMuted),
            color = MaterialTheme.colorScheme.onSurface
          )
        }
      }
    }
  }
}

@Composable
fun DateBadgeOverlay(
  displaySchedule: String,
  modifier: Modifier = Modifier
) {
  val cleanStr = displaySchedule.trim()
  val parts = cleanStr.split(" ")
  val dayText = if (parts.isNotEmpty()) parts[0] else cleanStr
  val monthText = if (parts.size >= 2) parts[1] else ""

  Card(
    shape = RoundedCornerShape(12.dp),
    colors = CardDefaults.cardColors(containerColor = Color.White),
    elevation = CardDefaults.cardElevation(defaultElevation = 0.dp),
    border = BorderStroke(0.dp, Color.Transparent),
    modifier = modifier
  ) {
    Column(
      horizontalAlignment = Alignment.CenterHorizontally,
      verticalArrangement = Arrangement.Center,
      modifier = Modifier.padding(horizontal = 9.dp, vertical = 5.dp)
    ) {
      Text(
        text = dayText,
        style = TextStyle(
          fontSize = 13.sp,
          fontWeight = FontWeight.ExtraBold,
          color = Color(0xFF0F172A)
        )
      )
      if (monthText.isNotEmpty()) {
        Text(
          text = monthText,
          style = TextStyle(
            fontSize = 10.sp,
            fontWeight = FontWeight.Bold,
            color = DiajakOrange
          )
        )
      }
    }
  }
}

@Composable
fun ActivityCarouselCard(
  activity: ActivityModel,
  onClick: () -> Unit,
  favoriteActivityIds: Set<String> = emptySet(),
  favoriteAcaraIds: Set<String> = favoriteActivityIds,
  onToggleFavorite: (ActivityModel) -> Unit = {},
  modifier: Modifier = Modifier,
  cardWidth: androidx.compose.ui.unit.Dp? = null
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

  val screenWidthDp = androidx.compose.ui.platform.LocalConfiguration.current.screenWidthDp.dp
  val defaultWidth = (screenWidthDp - 40.dp - 12.dp) / 2
  val effectiveWidth = cardWidth ?: defaultWidth

  Card(
    modifier = modifier
      .width(effectiveWidth)
      .clickable(
        interactionSource = remember { MutableInteractionSource() },
        indication = null,
        onClick = onClick
      ),
    shape = RoundedCornerShape(16.dp),
    colors = CardDefaults.cardColors(containerColor = Color.White),
    border = BorderStroke(0.dp, Color.Transparent),
    elevation = CardDefaults.cardElevation(defaultElevation = 0.dp)
  ) {
    Column {
      Box(
        modifier = Modifier
          .fillMaxWidth()
          .height(130.dp)
          .clip(RoundedCornerShape(topStart = 16.dp, topEnd = 16.dp))
      ) {
        Image(
          painter = painterResource(id = activity.imageResId),
          contentDescription = activity.title,
          contentScale = ContentScale.Crop,
          modifier = Modifier.fillMaxSize()
        )
        
        // Date badge on top-left of photo
        DateBadgeOverlay(
          displaySchedule = displaySchedule,
          modifier = Modifier
            .align(Alignment.TopStart)
            .padding(MaterialTheme.spacing.small)
        )

        // Favorite Button on top-right
        val isFav = favoriteActivityIds.contains(activity.id) || favoriteAcaraIds.contains(activity.id)
        Box(
          modifier = Modifier
            .align(Alignment.TopEnd)
            .padding(MaterialTheme.spacing.small)
            .size(34.dp)
            .clip(CircleShape)
            .background(Color.White.copy(alpha = 0.92f))
            .clickable { onToggleFavorite(activity) },
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
          .padding(horizontal = MaterialTheme.spacing.screenMargin, vertical = 10.dp)
      ) {
        // Title
        Text(
          text = activity.title,
          style = TextStyle(
            fontSize = 13.sp,
            fontWeight = FontWeight.Bold,
            color = DiajakTextDark
          ),
          maxLines = 2,
          minLines = 2,
          overflow = TextOverflow.Ellipsis,
          lineHeight = 17.sp,
          modifier = Modifier.fillMaxWidth()
        )

        Spacer(modifier = Modifier.height(MaterialTheme.spacing.extraSmall))

        // Subtitle / Location Row
        Row(verticalAlignment = Alignment.CenterVertically) {
          Icon(
            imageVector = Icons.Outlined.Place,
            contentDescription = "Lokasi",
            tint = DiajakOrange,
            modifier = Modifier.size(13.dp)
          )
          Spacer(modifier = Modifier.width(3.dp))
          Text(
            text = activity.locationName.split(",").firstOrNull()?.trim() ?: activity.locationName,
            style = TextStyle(
              fontSize = 11.sp,
              fontWeight = FontWeight.Medium,
              color = Color(0xFF64748B)
            ),
            maxLines = 1,
            overflow = TextOverflow.Ellipsis
          )
        }

        Spacer(modifier = Modifier.height(MaterialTheme.spacing.small))

        // Price & Rating Row (Price on left, Rating on right)
        Row(
          modifier = Modifier.fillMaxWidth(),
          verticalAlignment = Alignment.CenterVertically,
          horizontalArrangement = Arrangement.SpaceBetween
        ) {
          val priceText = if (activity.priceValue == 0) "Gratis" else "Rp " + String.format("%,d", activity.priceValue).replace(',', '.')
          Text(
            text = priceText,
            style = TextStyle(
              fontSize = 13.5.sp,
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
              modifier = Modifier.size(12.dp)
            )
            Spacer(modifier = Modifier.width(2.dp))
            Text(
              text = "${activity.rating}",
              style = TextStyle(
                fontSize = 11.5.sp,
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

@Composable
fun ActivityScheduleCard(
  activity: ActivityModel,
  onClick: () -> Unit,
  favoriteActivityIds: Set<String> = emptySet(),
  favoriteAcaraIds: Set<String> = favoriteActivityIds,
  onToggleFavorite: (ActivityModel) -> Unit = {},
  cardWidth: androidx.compose.ui.unit.Dp? = null
) {
  ActivityCarouselCard(
    activity = activity,
    onClick = onClick,
    favoriteActivityIds = favoriteActivityIds,
    favoriteAcaraIds = favoriteAcaraIds,
    onToggleFavorite = onToggleFavorite,
    cardWidth = cardWidth
  )
}

@Composable
fun ActivityNewestCard(
  activity: ActivityModel,
  onClick: () -> Unit,
  favoriteActivityIds: Set<String> = emptySet(),
  favoriteAcaraIds: Set<String> = favoriteActivityIds,
  onToggleFavorite: (ActivityModel) -> Unit = {},
  cardWidth: androidx.compose.ui.unit.Dp? = null
) {
  ActivityCarouselCard(
    activity = activity,
    onClick = onClick,
    favoriteActivityIds = favoriteActivityIds,
    favoriteAcaraIds = favoriteAcaraIds,
    onToggleFavorite = onToggleFavorite,
    cardWidth = cardWidth
  )
}

@Composable
fun JoinedPesertaStack(
  currentPeserta: Int,
  modifier: Modifier = Modifier,
  avatarSize: androidx.compose.ui.unit.Dp = 34.dp
) {
  if (currentPeserta <= 0) return

  val avatarImages = listOf(
    com.example.R.drawable.img_profile_cat_1783601304885,
    com.example.R.drawable.img_profile_dog_1783603245364,
    com.example.R.drawable.img_profile_panda_1783603225548,
    com.example.R.drawable.img_sport_running_1783584004743,
    com.example.R.drawable.img_padel_tennis_1783584027406,
    com.example.R.drawable.img_sunset_yoga_1783584057064,
    com.example.R.drawable.img_culinary_baking_1783584043430
  )

  // Max 4 icons shown in total.
  // If currentPeserta <= 4, show currentPeserta avatars.
  // If currentPeserta > 4, show 3 avatars and the 4th icon is a "+N" badge.
  val displayCount = if (currentPeserta > 4) 3 else currentPeserta
  val hasMoreBadge = currentPeserta > 4

  val overlap = (avatarSize * 0.65f)
  val totalItems = displayCount + (if (hasMoreBadge) 1 else 0)
  val containerWidth = if (totalItems > 0) {
    ((totalItems - 1) * overlap.value + avatarSize.value).dp
  } else {
    0.dp
  }

  Box(
    modifier = modifier
      .width(containerWidth)
      .height(avatarSize),
    contentAlignment = Alignment.CenterStart
  ) {
    for (i in 0 until displayCount) {
      val imageRes = avatarImages[i % avatarImages.size]
      Image(
        painter = painterResource(id = imageRes),
        contentDescription = "Joined player ${i + 1}",
        contentScale = ContentScale.Crop,
        modifier = Modifier
          .offset(x = (i * overlap.value).dp)
          .size(avatarSize)
          .clip(CircleShape)
          .border(1.5.dp, Color.White, CircleShape)
      )
    }

    if (hasMoreBadge) {
      val extraCount = currentPeserta - 3
      Box(
        modifier = Modifier
          .offset(x = (3 * overlap.value).dp)
          .size(avatarSize)
          .clip(CircleShape)
          .background(Color(0xFFF1F5F9))
          .border(1.5.dp, Color.White, CircleShape),
        contentAlignment = Alignment.Center
      ) {
        Text(
          text = "+$extraCount",
          color = Color(0xFF0F172A),
          style = com.example.ui.theme.DiajakDesignSystem.Typography.BodyBold.copy(
            fontSize = if (avatarSize >= 40.dp) 13.sp else 12.sp,
            platformStyle = androidx.compose.ui.text.PlatformTextStyle(includeFontPadding = false)
          ),
          textAlign = androidx.compose.ui.text.style.TextAlign.Center,
          maxLines = 1
        )
      }
    }
  }
}

private fun parseSingleDateToLocalDate(dateStr: String): java.time.LocalDate? {
  if (dateStr.isBlank()) return null
  try {
    val cleanDatePart = if (dateStr.contains(",")) {
      dateStr.substringAfter(",").trim()
    } else {
      dateStr
    }
    
    val tokens = cleanDatePart.split(" ")
    if (tokens.size == 3) {
      val dayStr = tokens[0].trim().toIntOrNull() ?: return null
      val monthStr = tokens[1].trim().lowercase().replace(".", "")
      val yearStr = tokens[2].trim().toIntOrNull() ?: return null
      
      val monthVal = when {
        monthStr.startsWith("jan") -> 1
        monthStr.startsWith("feb") || monthStr.startsWith("peb") -> 2
        monthStr.startsWith("mar") -> 3
        monthStr.startsWith("apr") -> 4
        monthStr.startsWith("mei") || monthStr == "may" -> 5
        monthStr.startsWith("jun") -> 6
        monthStr.startsWith("jul") -> 7
        monthStr.startsWith("agu") || monthStr.startsWith("ags") || monthStr.startsWith("agt") -> 8
        monthStr.startsWith("sep") -> 9
        monthStr.startsWith("okt") || monthStr.startsWith("oct") -> 10
        monthStr.startsWith("nov") -> 11
        monthStr.startsWith("des") || monthStr.startsWith("dec") -> 12
        else -> return null
      }
      return java.time.LocalDate.of(yearStr, monthVal, dayStr)
    }
  } catch (e: Exception) {
    // ignore
  }
  return null
}

private fun parseMultipleSchedulesToLocalDates(schedule: String): List<java.time.LocalDate> {
  if (schedule.isBlank()) return emptyList()
  try {
    val datePartRaw = schedule.split("•").firstOrNull()?.trim() ?: return emptyList()
    val dateStrings = datePartRaw.split(";")
    val list = mutableListOf<java.time.LocalDate>()
    for (dateStr in dateStrings) {
      val parsed = parseSingleDateToLocalDate(dateStr.trim())
      if (parsed != null) {
        list.add(parsed)
      }
    }
    return list
  } catch (e: Exception) {
    // ignore
  }
  return emptyList()
}

fun getHomeScreenDisplaySchedule(schedule: String, baseToday: java.time.LocalDate): String {
  if (schedule.isBlank()) return "Tidak tersedia"
  val dates = parseMultipleSchedulesToLocalDates(schedule)
  if (dates.isEmpty()) {
    val clean = schedule
      .replace(" 2026", "")
      .replace(" 2027", "")
      .substringBefore(" - ")
      .trim()
    return if (clean.contains(", ")) {
      clean.substringAfter(", ")
    } else {
      clean
    }
  }
  
  val upcomingDates = dates.filter { !it.isBefore(baseToday) }
  val targetDate = if (upcomingDates.isNotEmpty()) {
    upcomingDates.minOrNull()!!
  } else {
    dates.maxOrNull()!!
  }
  
  val monthNamesIndo = listOf(
    "Jan", "Feb", "Mar", "Apr", "Mei", "Jun",
    "Jul", "Ags", "Sep", "Okt", "Nov", "Des"
  )
  val monthName = monthNamesIndo[targetDate.monthValue - 1]
  val shortMonthName = when (targetDate.monthValue) {
    1 -> "Jan"
    2 -> "Feb"
    3 -> "Mar"
    4 -> "Apr"
    5 -> "Mei"
    6 -> "Jun"
    7 -> "Jul"
    8 -> "Ags"
    9 -> "Sep"
    10 -> "Okt"
    11 -> "Nov"
    12 -> "Des"
    else -> monthName
  }
  
  return "${targetDate.dayOfMonth} $shortMonthName"
}

@Composable
fun HomeSlidingBanners(
  onSelectCityFilter: (String) -> Unit,
  statusBarHeightDp: androidx.compose.ui.unit.Dp
) {
  val pagerState = rememberPagerState(pageCount = { homeCityBannersList.size })
  
  Box(
    modifier = Modifier.fillMaxWidth()
  ) {
    HorizontalPager(
      state = pagerState,
      contentPadding = PaddingValues(horizontal = 20.dp),
      pageSpacing = 12.dp,
      modifier = Modifier.fillMaxWidth()
    ) { page ->
      val banner = homeCityBannersList[page]
      Box(
        modifier = Modifier
          .fillMaxWidth()
          .aspectRatio(2.05f)
          .clip(RoundedCornerShape(22.dp))
          .clickable {
            onSelectCityFilter(banner.filterValue)
          }
      ) {
        Image(
          painter = rememberAsyncImagePainter(
            model = banner.imageUrl,
            error = painterResource(id = banner.imageResId),
            placeholder = painterResource(id = banner.imageResId)
          ),
          contentDescription = banner.title,
          contentScale = ContentScale.Crop,
          modifier = Modifier.fillMaxSize()
        )
        
        // Multi-stop gradient overlay to ensure high contrast text on left side
        Box(
          modifier = Modifier
            .fillMaxSize()
            .background(
              Brush.horizontalGradient(
                colors = listOf(
                  Color.Black.copy(alpha = 0.82f),
                  Color.Black.copy(alpha = 0.55f),
                  Color.Black.copy(alpha = 0.15f),
                  Color.Transparent
                )
              )
            )
        )


        // Content container inside banner
        Column(
          modifier = Modifier
            .align(Alignment.CenterStart)
            .padding(start = 16.dp, end = 90.dp, top = 10.dp, bottom = 10.dp),
          verticalArrangement = Arrangement.Center
        ) {
          Text(
            text = banner.title,
            style = com.example.ui.theme.DiajakDesignSystem.Typography.Title.copy(
              fontSize = 20.sp,
              fontWeight = FontWeight.ExtraBold,
              color = Color.White
            ),
            maxLines = 1,
            overflow = TextOverflow.Ellipsis
          )
          Spacer(modifier = Modifier.height(MaterialTheme.spacing.extraSmall))
          Text(
            text = banner.subtitle,
            style = com.example.ui.theme.DiajakDesignSystem.Typography.Body.copy(
              fontSize = 11.5.sp,
              fontWeight = FontWeight.Normal,
              color = Color.White.copy(alpha = 0.92f)
            ),
            maxLines = 2,
            overflow = TextOverflow.Ellipsis,
            lineHeight = 15.sp
          )
          Spacer(modifier = Modifier.height(MaterialTheme.spacing.small))

          Surface(
            shape = RoundedCornerShape(14.dp),
            color = DiajakOrange,
            shadowElevation = 0.dp,
            modifier = Modifier
              .clip(RoundedCornerShape(14.dp))
              .clickable { onSelectCityFilter(banner.filterValue) }
          ) {
            Text(
              text = banner.buttonText,
              style = TextStyle(
                fontSize = 11.sp,
                fontWeight = FontWeight.Bold,
                color = Color.White
              ),
              modifier = Modifier.padding(horizontal = 12.dp, vertical = 5.5.dp)
            )
          }
        }

        // Custom Page indicators at bottom right of each card
        Row(
          modifier = Modifier
            .align(Alignment.BottomEnd)
            .padding(end = 14.dp, bottom = 12.dp),
          horizontalArrangement = Arrangement.spacedBy(MaterialTheme.spacing.extraSmall),
          verticalAlignment = Alignment.CenterVertically
        ) {
          homeCityBannersList.indices.forEach { index ->
            val isSelected = pagerState.currentPage == index
            Box(
              modifier = Modifier
                .height(4.5.dp)
                .width(if (isSelected) 16.dp else 4.5.dp)
                .clip(RoundedCornerShape(8.dp))
                .background(if (isSelected) Color.White else Color.White.copy(alpha = 0.45f))
            )
          }
        }
      }
    }
  }
}




