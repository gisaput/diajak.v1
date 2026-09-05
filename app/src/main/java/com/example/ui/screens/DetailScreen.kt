package com.example.ui.screens
import androidx.compose.ui.graphics.CompositingStrategy
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.graphics.BlendMode
import androidx.compose.ui.draw.drawWithContent
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.RectangleShape
import dev.chrisbanes.haze.HazeState
import dev.chrisbanes.haze.HazeStyle
import dev.chrisbanes.haze.HazeTint
import dev.chrisbanes.haze.hazeSource
import dev.chrisbanes.haze.hazeEffect
import com.example.ui.components.DiajakGlassHeader
import com.example.ui.components.DiajakGlassBottomSheet
import com.example.ui.theme.spacing
import com.example.ui.theme.diajakGlassButton
import com.example.ui.theme.diajakGlassHeaderEffect
import androidx.compose.material3.MaterialTheme

import com.example.R
import androidx.compose.material.icons.automirrored.outlined.*
import androidx.compose.material.icons.outlined.*

import androidx.compose.foundation.Image
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.material.icons.outlined.*
import androidx.compose.material.icons.outlined.*
import androidx.compose.material.icons.outlined.*

import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.foundation.pager.HorizontalPager
import androidx.compose.foundation.pager.rememberPagerState
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
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.graphics.TransformOrigin
import androidx.compose.ui.layout.layout
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.gestures.detectTransformGestures
import androidx.compose.foundation.gestures.detectTapGestures
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.animateContentSize
import androidx.compose.animation.expandVertically
import androidx.compose.animation.shrinkVertically
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.slideInVertically
import androidx.compose.animation.slideOutVertically
import androidx.compose.animation.core.*
import androidx.activity.compose.BackHandler
import kotlin.math.roundToInt
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.withStyle
import androidx.compose.material.icons.outlined.*
import androidx.compose.material.icons.outlined.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Path
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.data.DiajakRepository
import com.example.model.ActivityModel
import com.example.ui.theme.DiajakOrange
import com.example.ui.theme.RatingGold
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.ui.layout.onSizeChanged
import androidx.compose.ui.layout.onGloballyPositioned
import androidx.compose.ui.layout.positionInRoot
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.isImeVisible
import androidx.compose.foundation.layout.imePadding
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.ExperimentalLayoutApi
import kotlinx.coroutines.launch
import kotlinx.coroutines.delay
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalDensity
import android.content.Intent

import androidx.compose.ui.draw.shadow
import androidx.compose.ui.platform.testTag
import com.example.ui.components.SkeletonDetailContent
import com.example.ui.components.DiajakGlassHeader
import com.example.ui.theme.diajakGlassButton
import com.example.ui.theme.diajakGlassHeaderEffect
import androidx.compose.ui.zIndex

@OptIn(ExperimentalMaterial3Api::class, ExperimentalLayoutApi::class)
@Composable
fun DetailScreen(
  activity: ActivityModel,
  onBack: () -> Unit,
  onBookClick: () -> Unit,
  onChatWithKreator: (String) -> Unit,
  isFavorite: Boolean = false,
  onToggleFavorite: (() -> Unit)? = null,
  isLoggedIn: Boolean = false,
  onRequireLogin: () -> Unit = {},
  isLoading: Boolean = false,
  onActivityClick: ((ActivityModel) -> Unit)? = null,
  favoriteActivityIds: Set<String> = emptySet(),
  favoriteAcaraIds: Set<String> = favoriteActivityIds,
  onToggleFavoriteActivity: ((ActivityModel) -> Unit)? = null
) {
  var localFavorite by remember { mutableStateOf(false) }
  val currentFavorite = if (onToggleFavorite != null) isFavorite else localFavorite
  val handleToggleFavorite = {
    if (onToggleFavorite != null) {
      onToggleFavorite()
    } else {
      localFavorite = !localFavorite
    }
  }
  var isChatSheetOpen by remember { mutableStateOf(false) }
  var isMapSheetOpen by remember { mutableStateOf(false) }
  var chatInput by remember { mutableStateOf("") }
  var chatMessages by remember {
    mutableStateOf(
      listOf(
        "Halo kak, selamat siang!",
        "Apakah ada hal yang perlu disiapkan sebelum keberangkatan?"
      )
    )
  }
  
  val context = LocalContext.current
  val density = LocalDensity.current
  val coroutineScope = rememberCoroutineScope()
  var scrollColumnTopInRoot by remember { mutableFloatStateOf(0f) }
  var locationContainerScrollTarget by remember { mutableFloatStateOf(0f) }
  var reviewsContainerScrollTarget by remember { mutableFloatStateOf(0f) }
  var isLoadingReviews by remember { mutableStateOf(false) }
  var isReviewsSheetOpen by remember { mutableStateOf(false) }
  val allReviewsList = remember {
    listOf(
      com.example.model.ReviewModel("r1", "Nadia Putri", 5, "Seru banget! Kreator super ramah dan penjelasannya mudah dipahami. Rekomendasi banget buat yang cari teman hobi baru.", "2 hari lalu"),
      com.example.model.ReviewModel("r2", "Bima Arya", 5, "Fasilitas lengkap dan tempatnya estetik. Senang banget bisa gabung lewat aplikasi ini!", "1 minggu lalu"),
      com.example.model.ReviewModel("r3", "Clarissa Devika", 4, "Aktivitas tepat waktu, peserta lain juga asik-asik. Next time bakal ikut aktivitas lainnya lagi.", "2 minggu lalu"),
      com.example.model.ReviewModel("r4", "Stephanie", 5, "Kami menikmati makanan yang enak dan tur berjalan kaki bersama Kreator! Dia sangat ramah dan merekomendasikan banyak tempat lokal menarik.", "2 minggu lalu"),
      com.example.model.ReviewModel("r5", "Ari", 5, "Ari punya banyak hal untuk diceritakan dan dibagikan tentang segalanya. Sangat merekomendasikan aktivitas ini, tidak hanya melihat hal menarik tetapi juga mengenal budaya lokal.", "3 minggu lalu"),
      com.example.model.ReviewModel("r6", "Rizky Ramadhan", 5, "Pengalaman yang luar biasa! Lokasinya sangat nyaman dan atmosfernya sangat mendukung untuk berkenalan.", "1 bulan lalu"),
      com.example.model.ReviewModel("r7", "Dita Lestari", 5, "Sangat terorganisir dengan baik. Host sangat perhatian dengan detail dan keamanan semua peserta.", "1 bulan lalu"),
      com.example.model.ReviewModel("r8", "Fajar Nugraha", 4, "Sangat worth it! Dapat teman-teman baru dan pengalaman baru yang tidak terlupakan.", "2 bulan lalu")
    )
  }
  val scrollState = rememberScrollState()

  val images = remember(activity) {
    (listOf(activity.imageResId) + activity.galleryImages).distinct().filter { it != 0 }
  }
  val pagerState = rememberPagerState(pageCount = { images.size })

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

  var currentTime by remember { mutableStateOf(java.time.LocalTime.now()) }
  LaunchedEffect(Unit) {
    while (true) {
      kotlinx.coroutines.delay(10000L)
      currentTime = java.time.LocalTime.now()
    }
  }

  val allParsedDates = remember(activity.schedule) {
    parseMultipleSchedulesToLocalDates(activity.schedule).sorted()
  }

  val endTime = remember(activity.schedule) {
    val endTimePart = activity.schedule.split("•").getOrNull(1)?.trim() ?: ""
    val endTimeStrRaw = if (endTimePart.contains("-")) {
      endTimePart.split("-")[1]
    } else {
      ""
    }
    val endTimeStr = endTimeStrRaw.replace(Regex("[a-zA-Z]"), "").trim()
    try {
      if (endTimeStr.isNotEmpty()) {
        val cleanStr = endTimeStr.replace(".", ":")
        if (cleanStr.length >= 5) java.time.LocalTime.parse(cleanStr.take(5)) else java.time.LocalTime.MAX
      } else {
        java.time.LocalTime.MAX
      }
    } catch (e: Exception) {
      java.time.LocalTime.MAX
    }
  }

  val validUpcomingDates = remember(allParsedDates, baseToday, currentTime, endTime) {
    allParsedDates.filter { date ->
      date.isAfter(baseToday) || (date.isEqual(baseToday) && currentTime.isBefore(endTime.plusMinutes(1)))
    }
  }
  val closestValidDate = validUpcomingDates.firstOrNull()

  val isSoldOut = activity.currentPeserta >= activity.maxPeserta

  val buttonState = when {
    isSoldOut -> "penuh"
    allParsedDates.isNotEmpty() && closestValidDate == null -> "terlaksana"
    else -> "ikuti"
  }

  val isButtonEnabled = buttonState == "ikuti"
  val buttonText = when (buttonState) {
    "penuh" -> "Penuh"
    "terlaksana" -> "Terlaksana"
    else -> "Ikuti"
  }

  var isOverviewExpanded by remember { mutableStateOf(false) }

  val hazeState = remember { HazeState() }

  Box(modifier = Modifier.fillMaxSize().background(androidx.compose.ui.graphics.Color(0xFFE5E7EB))) {
    if (isLoading) {
      SkeletonDetailContent(modifier = Modifier.fillMaxSize())
    } else {
      // Scrollable Content
      Column(
        modifier = Modifier
          .fillMaxSize()
          .hazeSource(state = hazeState)
          .verticalScroll(scrollState)
          .onGloballyPositioned { coords ->
            scrollColumnTopInRoot = coords.positionInRoot().y
          }
          .padding(bottom = 140.dp)
      ) {
        // Section 1: Hero Image Gallery (Full-Bleed Edge-to-Edge to Top)
        Box(
          modifier = Modifier
            .fillMaxWidth()
            .height(360.dp)
            .background(Color(0xFFE2E8F0))
        ) {
          HorizontalPager(
            state = pagerState,
            modifier = Modifier.fillMaxSize()
          ) { page ->
            Image(
              painter = painterResource(id = images[page]),
              contentDescription = activity.title,
              contentScale = ContentScale.Crop,
              modifier = Modifier.fillMaxSize()
            )
          }

          // Bottom subtle scrim for page indicators & badge contrast
          Box(
            modifier = Modifier
              .align(Alignment.BottomCenter)
              .fillMaxWidth()
              .height(100.dp)
              .background(
                Brush.verticalGradient(
                  colors = listOf(Color.Transparent, Color.Black.copy(alpha = 0.45f))
                )
              )
          )

          // Pager Indicators (Pill shape capsule with dots matching reference)
          if (images.size > 1) {
            Surface(
              modifier = Modifier
                .align(Alignment.BottomCenter)
                .padding(bottom = 32.dp),
              shape = RoundedCornerShape(12.dp),
              color = Color.White.copy(alpha = 0.75f),
              shadowElevation = 0.dp
            ) {
              Row(
                modifier = Modifier.padding(horizontal = 10.dp, vertical = 6.dp),
                horizontalArrangement = Arrangement.spacedBy(6.dp),
                verticalAlignment = Alignment.CenterVertically
              ) {
                images.indices.forEach { index ->
                  val isSelected = pagerState.currentPage == index
                  if (isSelected) {
                    Box(
                      modifier = Modifier
                        .size(width = 18.dp, height = 6.dp)
                        .clip(RoundedCornerShape(3.dp))
                        .background(Color(0xFF1E293B))
                    )
                  } else {
                    Box(
                      modifier = Modifier
                        .size(6.dp)
                        .clip(CircleShape)
                        .background(Color(0xFF94A3B8))
                    )
                  }
                }
              }
            }
          }
        }

        // Section 2: Title, Schedule, Overview & Facilities (Floating Modern Overlapping White Card)
        Surface(
          modifier = Modifier
            .fillMaxWidth()
            .layout { measurable, constraints ->
              val placeable = measurable.measure(constraints)
              val overlapPx = 24.dp.roundToPx()
              layout(placeable.width, placeable.height - overlapPx) {
                placeable.placeRelative(0, -overlapPx)
              }
            }
            .padding(horizontal = 20.dp),
          shape = RoundedCornerShape(topStart = 24.dp, topEnd = 24.dp, bottomStart = 18.dp, bottomEnd = 18.dp),
          color = Color.White,
          shadowElevation = 0.dp
        ) {
          Column(
            modifier = Modifier
              .fillMaxWidth()
              .padding(horizontal = 20.dp, vertical = 22.dp)
          ) {
            // Judul Aktivitas
            Text(
              text = activity.title,
              style = com.example.ui.theme.DiajakDesignSystem.Typography.Title.copy(
                fontSize = 20.sp,
                fontWeight = FontWeight.Bold,
                lineHeight = 28.sp
              ),
              color = MaterialTheme.colorScheme.onSurface,
              textAlign = TextAlign.Center,
              modifier = Modifier.fillMaxWidth()
            )

            Spacer(modifier = Modifier.height(12.dp))

            // Tanggal & Jam info (Sesuai Desain Tanggal & Jam dengan Tahun Lengkap)
            val datePart = getDisplayedScheduleDateText(activity.schedule, baseToday)
            val timePartRaw = displaySchedule.split("•").getOrNull(1)?.trim() ?: activity.schedule.split("•").getOrNull(1)?.trim() ?: ""
            val timeFormatted = timePartRaw
              .replace("(?i)\\s*WIB".toRegex(), "")
              .replace("(?i)\\s*WITA".toRegex(), "")
              .replace("(?i)\\s*WIT".toRegex(), "")
              .trim()

            Row(
              modifier = Modifier.fillMaxWidth(),
              verticalAlignment = Alignment.CenterVertically,
              horizontalArrangement = Arrangement.Center
            ) {
              // 1. Date Item
              Row(
                verticalAlignment = Alignment.CenterVertically,
                modifier = Modifier.padding(vertical = 4.dp)
              ) {
                Icon(
                  imageVector = Icons.Outlined.Event,
                  contentDescription = "Tanggal Aktivitas",
                  tint = DiajakOrange,
                  modifier = Modifier.size(16.dp)
                )
                Spacer(modifier = Modifier.width(6.dp))
                Text(
                  text = datePart,
                  style = com.example.ui.theme.DiajakDesignSystem.Typography.Body.copy(
                    fontSize = 13.5.sp,
                    fontWeight = FontWeight.Medium,
                    color = Color(0xFF64748B)
                  )
                )
              }

              if (timeFormatted.isNotEmpty()) {
                Spacer(modifier = Modifier.width(16.dp))

                // 2. Time Item
                Row(
                  verticalAlignment = Alignment.CenterVertically,
                  modifier = Modifier.padding(vertical = 4.dp)
                ) {
                  Icon(
                    imageVector = Icons.Outlined.Schedule,
                    contentDescription = "Jam Aktivitas",
                    tint = DiajakOrange,
                    modifier = Modifier.size(16.dp)
                  )
                  Spacer(modifier = Modifier.width(6.dp))
                  Text(
                    text = timeFormatted,
                    style = com.example.ui.theme.DiajakDesignSystem.Typography.Body.copy(
                      fontSize = 13.5.sp,
                      fontWeight = FontWeight.Medium,
                      color = Color(0xFF64748B)
                    )
                  )
                }
              }
            }

            Spacer(modifier = Modifier.height(18.dp))
            HorizontalDivider(color = Color(0xFFE2E8F0), thickness = 1.dp)
            Spacer(modifier = Modifier.height(18.dp))

            // 1. Tentang Aktivitas
            Text(
              text = "Tentang Aktivitas",
              style = com.example.ui.theme.DiajakDesignSystem.Typography.BodyBold.copy(
                fontSize = 16.sp,
                fontWeight = FontWeight.SemiBold
              ),
              color = MaterialTheme.colorScheme.onSurface
            )
            Spacer(modifier = Modifier.height(10.dp))

            // Overview Text with Soft Gradient Fade on lines 3-4 when collapsed
            Box(
              modifier = Modifier
                .fillMaxWidth()
                .animateContentSize(
                  animationSpec = spring(
                    dampingRatio = Spring.DampingRatioNoBouncy,
                    stiffness = Spring.StiffnessMediumLow
                  )
                )
            ) {
              Text(
                text = activity.overview,
                style = com.example.ui.theme.DiajakDesignSystem.Typography.Body.copy(
                  fontSize = 14.5.sp,
                  lineHeight = 23.sp
                ),
                color = Color(0xFF334155),
                maxLines = if (isOverviewExpanded) Int.MAX_VALUE else 4,
                overflow = TextOverflow.Clip
              )

              if (!isOverviewExpanded) {
                // Soft gradient fade: lines 1-2 sharp, line 3 soft fade, line 4 faded but still visible/readable
                Box(
                  modifier = Modifier
                    .matchParentSize()
                    .background(
                      Brush.verticalGradient(
                        0.0f to Color.White.copy(alpha = 0.0f),
                        0.45f to Color.White.copy(alpha = 0.0f),
                        0.70f to Color.White.copy(alpha = 0.40f),
                        1.0f to Color.White.copy(alpha = 0.78f)
                      )
                    )
                )
              }
            }

            // Expandable content (Yang Didapat)
            AnimatedVisibility(
              visible = isOverviewExpanded,
              enter = fadeIn(animationSpec = tween(280)) + expandVertically(animationSpec = tween(280)),
              exit = fadeOut(animationSpec = tween(180)) + shrinkVertically(animationSpec = tween(180))
            ) {
              Column(modifier = Modifier.fillMaxWidth()) {
                Spacer(modifier = Modifier.height(18.dp))
                HorizontalDivider(color = Color(0xFFF1F5F9), thickness = 1.dp)
                Spacer(modifier = Modifier.height(18.dp))

                // 2. Yang Didapat
                Text(
                  text = "Yang Didapat",
                  style = com.example.ui.theme.DiajakDesignSystem.Typography.BodyBold.copy(
                    fontSize = 16.sp,
                    fontWeight = FontWeight.SemiBold
                  ),
                  color = MaterialTheme.colorScheme.onSurface
                )
                Spacer(modifier = Modifier.height(12.dp))
                Column(
                  verticalArrangement = Arrangement.spacedBy(10.dp)
                ) {
                  activity.detailsList.forEach { detail ->
                    Row(
                      verticalAlignment = Alignment.Top,
                      modifier = Modifier.fillMaxWidth()
                    ) {
                      Box(
                        modifier = Modifier
                          .padding(top = 2.dp)
                          .size(20.dp)
                          .clip(CircleShape)
                          .background(MaterialTheme.colorScheme.surfaceVariant),
                        contentAlignment = Alignment.Center
                      ) {
                        Icon(
                          imageVector = Icons.Outlined.Check,
                          contentDescription = null,
                          tint = MaterialTheme.colorScheme.onSurface,
                          modifier = Modifier.size(12.dp)
                        )
                      }
                      Spacer(modifier = Modifier.width(10.dp))
                      Text(
                        text = detail,
                        style = com.example.ui.theme.DiajakDesignSystem.Typography.Body.copy(
                          fontSize = 14.sp,
                          lineHeight = 22.sp
                        ),
                        color = Color(0xFF334155)
                      )
                    }
                  }
                }
              }
            }

            Spacer(modifier = Modifier.height(if (isOverviewExpanded) 18.dp else 8.dp))

            // Center Pill Button: "Selengkapnya ▾" / "Sembunyikan ▴"
            Surface(
              onClick = { isOverviewExpanded = !isOverviewExpanded },
              shape = CircleShape,
              color = Color.White,
              border = BorderStroke(1.dp, DiajakOrange),
              modifier = Modifier.align(Alignment.CenterHorizontally)
            ) {
              Row(
                modifier = Modifier.padding(horizontal = 16.dp, vertical = 7.dp),
                verticalAlignment = Alignment.CenterVertically
              ) {
                Text(
                  text = if (isOverviewExpanded) "Sembunyikan" else "Selengkapnya",
                  style = com.example.ui.theme.DiajakDesignSystem.Typography.BodyBold.copy(
                    fontSize = 13.sp,
                    fontWeight = FontWeight.Medium,
                    color = DiajakOrange
                  )
                )
                Spacer(modifier = Modifier.width(4.dp))
                Icon(
                  imageVector = if (isOverviewExpanded) Icons.Outlined.KeyboardArrowUp else Icons.Outlined.KeyboardArrowDown,
                  contentDescription = null,
                  tint = DiajakOrange,
                  modifier = Modifier.size(16.dp)
                )
              }
            }
          }
        }

        // Section Spacer showing app gray background
        Spacer(modifier = Modifier.height(com.example.ui.theme.ThemeSpacing.Medium))

        // Group 2: Lokasi, Profil & User Joined (Flat White Card on Gray Canvas)
        val onKreatorClick = {
          if (isLoggedIn) {
            isChatSheetOpen = true
          } else {
            onRequireLogin()
          }
        }
        Surface(
          modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 20.dp)
            .onGloballyPositioned { coords ->
              locationContainerScrollTarget = coords.positionInRoot().y - scrollColumnTopInRoot + scrollState.value
            },
          shape = RoundedCornerShape(16.dp),
          color = Color.White,
          shadowElevation = 0.dp
        ) {
          Column(modifier = Modifier.fillMaxWidth().padding(18.dp)) {
            // 1. User Joined (Atas)
            Row(
              modifier = Modifier.fillMaxWidth(),
              verticalAlignment = Alignment.CenterVertically
            ) {
              JoinedPesertaStack(
                currentPeserta = activity.currentPeserta,
                avatarSize = 40.dp
              )
              Spacer(modifier = Modifier.width(10.dp))
              val sisa = (activity.maxPeserta - activity.currentPeserta).coerceAtLeast(0)
              Text(
                text = if (sisa > 0) "Kuota Tersisa $sisa" else "Kuota penuh",
                style = com.example.ui.theme.DiajakDesignSystem.Typography.Body.copy(
                  fontSize = 13.sp,
                  fontWeight = FontWeight.Medium,
                  color = Color(0xFF64748B)
                )
              )
            }

            Spacer(modifier = Modifier.height(14.dp))
            HorizontalDivider(color = Color(0xFFF1F5F9))
            Spacer(modifier = Modifier.height(14.dp))

            // 2. Lokasi (Tengah)
            Row(
              modifier = Modifier.fillMaxWidth().clickable { isMapSheetOpen = true },
              verticalAlignment = Alignment.CenterVertically
            ) {
              Box(
                modifier = Modifier.size(40.dp)
                  .background(MaterialTheme.colorScheme.surfaceVariant, CircleShape),
                contentAlignment = Alignment.Center
              ) {
                Icon(
                  imageVector = Icons.Outlined.Place,
                  contentDescription = null,
                  tint = MaterialTheme.colorScheme.onSurface,
                  modifier = Modifier.size(20.dp)
                )
              }
              Spacer(modifier = Modifier.width(MaterialTheme.spacing.medium))
              Column(modifier = Modifier.weight(1f)) {
                Text(
                  text = buildAnnotatedString {
                    withStyle(style = com.example.ui.theme.DiajakDesignSystem.Typography.BodyBold.toSpanStyle()) {
                      append(activity.locationName)
                    }
                    append("\n")
                    withStyle(style = com.example.ui.theme.DiajakDesignSystem.Typography.Body.toSpanStyle().copy(color = Color(0xFF64748B))) {
                      append(activity.address)
                    }
                  },
                  style = com.example.ui.theme.DiajakDesignSystem.Typography.Body,
                  color = MaterialTheme.colorScheme.onSurface,
                  maxLines = 2,
                  overflow = TextOverflow.Ellipsis
                )
              }
            }

            Spacer(modifier = Modifier.height(14.dp))
            HorizontalDivider(color = Color(0xFFF1F5F9))
            Spacer(modifier = Modifier.height(14.dp))

            // 3. Profile (Bawah)
            Row(
              modifier = Modifier.fillMaxWidth().clickable { onKreatorClick() },
              verticalAlignment = Alignment.CenterVertically
            ) {
              Box(
                modifier = Modifier.size(40.dp),
                contentAlignment = Alignment.Center
              ) {
                Image(
                  painter = painterResource(id = R.drawable.img_profile_cat_1783601304885),
                  contentDescription = "Foto Kreator",
                  contentScale = androidx.compose.ui.layout.ContentScale.Crop,
                  modifier = Modifier
                    .size(40.dp)
                    .clip(CircleShape)
                )
              }
              Spacer(modifier = Modifier.width(MaterialTheme.spacing.medium))
              Column(modifier = Modifier.weight(1f)) {
                Text(
                  text = buildAnnotatedString {
                    withStyle(style = com.example.ui.theme.DiajakDesignSystem.Typography.BodyBold.toSpanStyle()) {
                      append(activity.kreatorName)
                    }
                    append("\n")
                    withStyle(style = com.example.ui.theme.DiajakDesignSystem.Typography.Body.toSpanStyle().copy(color = Color(0xFF64748B))) {
                      append("Kreator Terverifikasi")
                    }
                  },
                  style = com.example.ui.theme.DiajakDesignSystem.Typography.Body,
                  color = MaterialTheme.colorScheme.onSurface
                )
              }
            }
          }
        }

        // Section Spacer showing app gray background
        Spacer(modifier = Modifier.height(com.example.ui.theme.ThemeSpacing.Medium))

        // Group 3: Ulasan (Flat White Card on Gray Canvas)
        Surface(
          modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 20.dp)
            .onGloballyPositioned { coords ->
              reviewsContainerScrollTarget = coords.positionInRoot().y - scrollColumnTopInRoot + scrollState.value
            },
          shape = RoundedCornerShape(16.dp),
          color = Color.White,
          shadowElevation = 0.dp
        ) {
          Column(modifier = Modifier.fillMaxWidth().padding(18.dp)) {
            Row(
              modifier = Modifier.fillMaxWidth(),
              horizontalArrangement = Arrangement.SpaceBetween,
              verticalAlignment = Alignment.CenterVertically
            ) {
              Column {
                Text(
                  text = "Ulasan Peserta",
                  style = com.example.ui.theme.DiajakDesignSystem.Typography.BodyBold.copy(fontSize = 16.sp),
                  color = MaterialTheme.colorScheme.onSurface
                )
                Row(
                  verticalAlignment = Alignment.CenterVertically,
                  modifier = Modifier.padding(top = MaterialTheme.spacing.extraSmall)
                ) {
                  Icon(
                    imageVector = Icons.Outlined.Star,
                    contentDescription = null,
                    tint = RatingGold,
                    modifier = Modifier.size(16.dp)
                  )
                  Spacer(modifier = Modifier.width(MaterialTheme.spacing.extraSmall))
                  Text(
                    text = "4.9",
                    style = com.example.ui.theme.DiajakDesignSystem.Typography.Body.copy(fontSize = 14.sp, fontWeight = FontWeight.Bold),
                    color = MaterialTheme.colorScheme.onSurface
                  )
                  Spacer(modifier = Modifier.width(MaterialTheme.spacing.small))
                  Text(
                    text = "(${activity.reviewsCount} ulasan)",
                    style = com.example.ui.theme.DiajakDesignSystem.Typography.Body.copy(color = Color(0xFF64748B)),
                  )
                }
              }
            }

            Spacer(modifier = Modifier.height(16.dp))

            val displayedReviews = allReviewsList.take(2)
            displayedReviews.forEachIndexed { idx, rev ->
              Column(modifier = Modifier.fillMaxWidth()) {
                Row(
                  modifier = Modifier.fillMaxWidth(),
                  horizontalArrangement = Arrangement.SpaceBetween,
                  verticalAlignment = Alignment.CenterVertically
                ) {
                  Row(verticalAlignment = Alignment.CenterVertically) {
                    Box(
                      modifier = Modifier
                        .size(32.dp)
                        .clip(CircleShape)
                        .background(DiajakOrange.copy(alpha = 0.1f)),
                      contentAlignment = Alignment.Center
                    ) {
                      Text(
                        text = rev.userName.take(1).uppercase(),
                        color = DiajakOrange,
                        style = com.example.ui.theme.DiajakDesignSystem.Typography.BodyBold
                      )
                    }
                    Spacer(modifier = Modifier.width(MaterialTheme.spacing.small))
                    Text(
                      text = rev.userName,
                      style = com.example.ui.theme.DiajakDesignSystem.Typography.BodyBold,
                      color = MaterialTheme.colorScheme.onSurface
                    )
                  }
                  Row(horizontalArrangement = Arrangement.spacedBy(2.dp)) {
                    repeat(rev.rating) {
                      Icon(
                        imageVector = Icons.Outlined.Star,
                        contentDescription = null,
                        tint = RatingGold,
                        modifier = Modifier.size(14.dp)
                      )
                    }
                  }
                }
                Spacer(modifier = Modifier.height(MaterialTheme.spacing.small))
                Text(
                  text = rev.comment,
                  style = com.example.ui.theme.DiajakDesignSystem.Typography.Body,
                  color = MaterialTheme.colorScheme.onSurface,
                  lineHeight = 22.sp
                )
                Spacer(modifier = Modifier.height(MaterialTheme.spacing.small))
                Text(
                  text = rev.date,
                  style = com.example.ui.theme.DiajakDesignSystem.Typography.Body.copy(fontSize = 12.sp, color = Color(0xFF64748B))
                )
              }
              if (idx < displayedReviews.lastIndex) {
                Spacer(modifier = Modifier.height(14.dp))
                HorizontalDivider(color = Color(0xFFF1F5F9))
                Spacer(modifier = Modifier.height(14.dp))
              }
            }

            Spacer(modifier = Modifier.height(16.dp))
            Button(
              onClick = { isReviewsSheetOpen = true },
              colors = ButtonDefaults.buttonColors(containerColor = MaterialTheme.colorScheme.surfaceVariant),
              shape = RoundedCornerShape(12.dp),
              modifier = Modifier.fillMaxWidth()
            ) {
              Text(
                text = "Tampilkan semua ulasan",
                color = MaterialTheme.colorScheme.onSurface,
                style = com.example.ui.theme.DiajakDesignSystem.Typography.BodyBold.copy(fontSize = 14.sp)
              )
            }
          }
        }

        // --- RECOMMENDED ACTIVITIES ("Mungkin Kamu Suka") ---
        val allActivities = DiajakRepository.activitiesFlow.collectAsState().value
        val otherActivities = remember(allActivities, activity) {
          allActivities.filter { it.id != activity.id }
            .map { other ->
              val otherCity = getNormalizedCity(other.locationName)
              val currentCity = getNormalizedCity(activity.locationName)
              val sameCity = otherCity == currentCity
              
              val dx = other.mapX - activity.mapX
              val dy = other.mapY - activity.mapY
              val distance = Math.sqrt((dx * dx + dy * dy).toDouble())
              Pair(other, sameCity to distance)
            }
            .sortedWith(
              compareByDescending<Pair<ActivityModel, Pair<Boolean, Double>>> { it.second.first }
                .thenBy { it.second.second }
            )
            .map { it.first }
            .take(6)
        }

        if (otherActivities.isNotEmpty()) {
          Column(
            modifier = Modifier
              .fillMaxWidth()
              .padding(vertical = MaterialTheme.spacing.section)
          ) {
            Text(
              text = "Mungkin Kamu Suka",
              style = com.example.ui.theme.DiajakDesignSystem.Typography.Title.copy(
                fontWeight = FontWeight.Bold,
                fontSize = 18.sp
              ),
              color = MaterialTheme.colorScheme.onSurface,
              modifier = Modifier.padding(start = 20.dp, end = 20.dp, bottom = MaterialTheme.spacing.medium)
            )

            LazyRow(
              horizontalArrangement = Arrangement.spacedBy(MaterialTheme.spacing.medium),
              contentPadding = PaddingValues(horizontal = 20.dp),
              modifier = Modifier.fillMaxWidth()
            ) {
              items(otherActivities, key = { it.id }) { act ->
                ActivityCarouselCard(
                  activity = act,
                  onClick = { onActivityClick?.invoke(act) },
                  favoriteActivityIds = favoriteActivityIds,
                  favoriteAcaraIds = favoriteAcaraIds,
                  onToggleFavorite = { targetAct -> onToggleFavoriteActivity?.invoke(targetAct) }
                )
              }
            }
          }
        }
      }
    }

    // Top Floating Header Layer (Circular Floating Back, Favorite & Share Buttons)
    val density = LocalDensity.current
    val headerAlpha by remember {
      derivedStateOf {
        ((scrollState.value - with(density) { 240.dp.toPx() }) / with(density) { 100.dp.toPx() })
          .coerceIn(0f, 1f)
      }
    }

    val underlayState by remember {
      derivedStateOf {
        if (scrollState.value < with(density) { 340.dp.toPx() }) {
          com.example.ui.theme.HeaderUnderlayState.PHOTO
        } else {
          com.example.ui.theme.HeaderUnderlayState.CONTAINER
        }
      }
    }

    val textMeltingFactor by androidx.compose.animation.core.animateFloatAsState(
      targetValue = if (underlayState == com.example.ui.theme.HeaderUnderlayState.CONTAINER) 0.35f else 0f,
      animationSpec = androidx.compose.animation.core.tween(durationMillis = 180, easing = androidx.compose.animation.core.LinearOutSlowInEasing),
      label = "detailTextMelting"
    )

    val photoGlowFactor by androidx.compose.animation.core.animateFloatAsState(
      targetValue = if (underlayState == com.example.ui.theme.HeaderUnderlayState.PHOTO) 1f else 0.4f,
      animationSpec = androidx.compose.animation.core.tween(durationMillis = 180, easing = androidx.compose.animation.core.LinearOutSlowInEasing),
      label = "detailPhotoGlow"
    )

    Box(
      modifier = Modifier
        .align(Alignment.TopCenter)
        .fillMaxWidth()
        .zIndex(10f)
    ) {
      // Glass Header Background (Fades in smoothly when scrolling past hero image)
      if (headerAlpha > 0f) {
        Box(
          modifier = Modifier
            .matchParentSize()
            .graphicsLayer { alpha = headerAlpha }
            .diajakGlassHeaderEffect(
              hazeState = hazeState,
              textMeltingFactor = textMeltingFactor,
              photoGlowFactor = photoGlowFactor
            )
        )
      }

      Box(
        modifier = Modifier
          .fillMaxWidth()
          .statusBarsPadding()
          .height(56.dp)
          .padding(horizontal = 20.dp)
      ) {
        // Circular Floating Back Button (Left)
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
            modifier = Modifier.size(20.dp)
          )
        }

        // Dynamic Header Title (Fade-in on scroll)
        val showTitle by remember { 
          derivedStateOf { scrollState.value > with(density) { 300.dp.toPx() } } 
        }

        AnimatedVisibility(
          visible = showTitle,
          enter = fadeIn(),
          exit = fadeOut(),
          modifier = Modifier.align(Alignment.Center)
        ) {
          Text(
            text = activity.title,
            style = com.example.ui.theme.DiajakDesignSystem.Typography.Headline,
            textAlign = androidx.compose.ui.text.style.TextAlign.Center,
            maxLines = 1,
            overflow = TextOverflow.Ellipsis,
            color = MaterialTheme.colorScheme.onSurface,
            modifier = Modifier.padding(horizontal = 96.dp).fillMaxWidth()
          )
        }

        // Circular Floating Action Buttons (Right: Share & Favorite Love)
        Row(
          modifier = Modifier.align(Alignment.CenterEnd),
          horizontalArrangement = Arrangement.spacedBy(10.dp),
          verticalAlignment = Alignment.CenterVertically
        ) {
          // Circular Share Button
          IconButton(
            onClick = {
              val shareIntent = Intent(Intent.ACTION_SEND).apply {
                type = "text/plain"
                putExtra(Intent.EXTRA_SUBJECT, activity.title)
                putExtra(Intent.EXTRA_TEXT, "Ayo ikutan aktivitas ${activity.title} di ${activity.locationName} bareng ${activity.kreatorName}! Download Diajak sekarang.")
              }
              context.startActivity(Intent.createChooser(shareIntent, "Bagikan Aktivitas"))
            },
            modifier = Modifier
              .size(40.dp)
              .diajakGlassButton(hazeState)
          ) {
            Icon(
              imageVector = Icons.Outlined.Share,
              contentDescription = "Bagikan",
              tint = MaterialTheme.colorScheme.onSurface,
              modifier = Modifier.size(20.dp)
            )
          }

          // Circular Favorite Heart Button (Love)
          IconButton(
            onClick = { handleToggleFavorite() },
            modifier = Modifier
              .size(40.dp)
              .diajakGlassButton(hazeState)
          ) {
            Icon(
              imageVector = if (currentFavorite) Icons.Outlined.Favorite else Icons.Outlined.FavoriteBorder,
              contentDescription = "Favorit",
              tint = if (currentFavorite) DiajakOrange else MaterialTheme.colorScheme.onSurface,
              modifier = Modifier.size(20.dp)
            )
          }
        }
      }
    }

    // Floating Action Bar (Floating Join & Save Action)
    Box(
      modifier = Modifier
        .align(Alignment.BottomCenter)
        .fillMaxWidth()
        .navigationBarsPadding()
        .padding(horizontal = 20.dp, vertical = 14.dp)
        .zIndex(15f)
    ) {
      Surface(
        shape = RoundedCornerShape(20.dp),
        color = Color.White,
        shadowElevation = 8.dp,
        modifier = Modifier
          .fillMaxWidth()
          .testTag("floating_action_join_save")
      ) {
        Row(
          modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp, vertical = 10.dp),
          horizontalArrangement = Arrangement.SpaceBetween,
          verticalAlignment = Alignment.CenterVertically
        ) {
          // Price & Label Info
          Column(
            modifier = Modifier.weight(1f),
            horizontalAlignment = Alignment.Start,
            verticalArrangement = Arrangement.Center
          ) {
            Text(
              text = if (activity.priceValue == 0) "Gratis" else "Rp ${String.format("%,d", activity.priceValue).replace(',', '.')}",
              style = com.example.ui.theme.DiajakDesignSystem.Typography.Headline.copy(
                fontSize = 18.sp,
                fontWeight = FontWeight.Bold,
                color = DiajakOrange
              )
            )
            Text(
              text = "per orang",
              style = com.example.ui.theme.DiajakDesignSystem.Typography.Body.copy(
                fontSize = 12.sp,
                color = Color(0xFF64748B)
              )
            )
          }

          // Floating Join Button
          Button(
            onClick = { if (isButtonEnabled) onBookClick() },
            enabled = isButtonEnabled,
            shape = RoundedCornerShape(14.dp),
            colors = ButtonDefaults.buttonColors(
              containerColor = DiajakOrange,
              contentColor = Color.White,
              disabledContainerColor = Color(0xFFE2E8F0),
              disabledContentColor = Color(0xFF94A3B8)
            ),
            contentPadding = PaddingValues(horizontal = 24.dp, vertical = 12.dp),
            modifier = Modifier
              .height(46.dp)
              .testTag("fab_join_action")
          ) {
            Icon(
              imageVector = Icons.Outlined.CheckCircle,
              contentDescription = null,
              tint = Color.White,
              modifier = Modifier.size(16.dp)
            )
            Spacer(modifier = Modifier.width(6.dp))
            Text(
              text = buttonText,
              style = androidx.compose.ui.text.TextStyle(
                fontSize = 15.sp,
                fontWeight = FontWeight.Bold,
                fontFamily = com.example.ui.theme.AppFontFamily
              )
            )
          }
        }
      }
    }

    if (isChatSheetOpen) {
      ModalBottomSheet(
        onDismissRequest = { isChatSheetOpen = false },
        sheetState = rememberModalBottomSheetState(skipPartiallyExpanded = true),
        containerColor = androidx.compose.ui.graphics.Color(0xFFE5E7EB),
        contentWindowInsets = { WindowInsets(0, 0, 0, 0) }
      ) {
        val coroutineScope = rememberCoroutineScope()
        val listState = rememberLazyListState()
        val chatHazeState = remember { HazeState() }
        var listHeight by remember { mutableStateOf(0) }

        val reversedMessages = remember(chatMessages) {
          chatMessages.asReversed()
        }

        val isKeyboardVisible = WindowInsets.isImeVisible
        LaunchedEffect(chatMessages.size, isKeyboardVisible, isChatSheetOpen) {
          if (chatMessages.isNotEmpty()) {
            kotlinx.coroutines.delay(150)
            listState.animateScrollToItem(0)
          }
        }

        val focusManager = androidx.compose.ui.platform.LocalFocusManager.current

        Box(
          modifier = Modifier
            .fillMaxWidth()
            .fillMaxHeight(0.88f)
            .navigationBarsPadding()
            .imePadding()
            .pointerInput(Unit) {
              detectTapGestures(
                onTap = {
                  focusManager.clearFocus()
                }
              )
            }
        ) {
          // Chat content layer
          Column(
            modifier = Modifier
              .fillMaxSize()
              .hazeSource(state = chatHazeState)
              .padding(top = 68.dp)
              .padding(bottom = MaterialTheme.spacing.medium)
          ) {
            // Pinned Context Banner (Shopee style)
          Box(
            modifier = Modifier
              .fillMaxWidth()
              .background(Color.Transparent)
              .padding(horizontal = 20.dp, vertical = MaterialTheme.spacing.small)
          ) {
            com.example.ui.theme.DiajakCard(
              modifier = Modifier.fillMaxWidth()
            ) {
              Row(
                modifier = Modifier
                  .fillMaxWidth()
                  .padding(20.dp),
              verticalAlignment = Alignment.CenterVertically
            ) {
              Icon(
                imageVector = Icons.Outlined.Event,
                contentDescription = "Aktivitas",
                tint = DiajakOrange,
                modifier = Modifier.size(24.dp)
              )
              Spacer(modifier = Modifier.width(20.dp))
              Column(modifier = Modifier.weight(1f)) {
                Text(
                  text = "Terkait Aktivitas:",
                  style = com.example.ui.theme.DiajakDesignSystem.Typography.Body,
                  color = MaterialTheme.colorScheme.onSurface,
                  
                )
                Text(
                  text = activity.title,
                  style = com.example.ui.theme.DiajakDesignSystem.Typography.Body,
                  color = MaterialTheme.colorScheme.onSurface,
                  maxLines = 1,
                  overflow = TextOverflow.Ellipsis
                )
              }
            }
            }
          }

          // Chat Body
          LazyColumn(
            state = listState,
            reverseLayout = true,
            modifier = Modifier
              .fillMaxWidth()
              .weight(1f)
              .background(Color.Transparent)
              .onSizeChanged { size ->
                val currentHeight = size.height
                if (listHeight > 0 && currentHeight < listHeight) {
                  coroutineScope.launch {
                    kotlinx.coroutines.delay(100)
                    listState.scrollToItem(0)
                  }
                }
                listHeight = currentHeight
              }
              .padding(20.dp),
            verticalArrangement = Arrangement.spacedBy(20.dp)
          ) {
            // Include an initial message at the beginning of the chat (bottom of reversed list)
            val combinedMessages = listOf("Halo! Senang bisa terhubung. Ada yang bisa saya bantu terkait aktivitas ini? 😊") + chatMessages
            val combinedReversed = combinedMessages.reversed()
            
            itemsIndexed(
              items = combinedReversed,
              key = { index, _ -> combinedReversed.size - 1 - index }
            ) { index, msg ->
              val isFromPartner = (index == combinedReversed.lastIndex)
              
              Box(
                modifier = Modifier.fillMaxWidth(),
                contentAlignment = if (isFromPartner) Alignment.CenterStart else Alignment.CenterEnd
              ) {
                Column(
                  horizontalAlignment = if (isFromPartner) Alignment.Start else Alignment.End
                ) {
                  Box(
                    modifier = Modifier
                      .clip(
                        if (isFromPartner) RoundedCornerShape(topStart = 4.dp, topEnd = 16.dp, bottomEnd = 16.dp, bottomStart = 16.dp)
                        else RoundedCornerShape(topStart = 16.dp, topEnd = 4.dp, bottomEnd = 16.dp, bottomStart = 16.dp)
                      )
                      .background(if (isFromPartner) Color.White else DiajakOrange)
                      .border(
                        width = if (isFromPartner) 1.dp else 0.dp,
                        color = if (isFromPartner) MaterialTheme.colorScheme.outlineVariant else Color.Transparent,
                        shape = if (isFromPartner) RoundedCornerShape(topStart = 4.dp, topEnd = 16.dp, bottomEnd = 16.dp, bottomStart = 16.dp)
                                else RoundedCornerShape(topStart = 16.dp, topEnd = 4.dp, bottomEnd = 16.dp, bottomStart = 16.dp)
                      )
                      .padding(20.dp)
                  ) {
                    Text(
                      text = msg,
                      style = com.example.ui.theme.DiajakDesignSystem.Typography.Body,
                      color = if (isFromPartner) MaterialTheme.colorScheme.onSurface else Color.White
                    )
                  }
                  
                  Spacer(modifier = Modifier.height(2.dp))
                  
                  // Timestamp
                  Text(
                    text = "Baru saja",
                    style = com.example.ui.theme.DiajakDesignSystem.Typography.Body, color = MaterialTheme.colorScheme.onSurface,
                    modifier = Modifier.padding(horizontal = 4.dp)
                  )
                }
              }
            }
          }

          // Input Box
          val onSendMessage = {
            if (chatInput.isNotBlank()) {
              val inputMsg = chatInput
              chatMessages = chatMessages + inputMsg
              chatInput = ""
            }
          }

          Row(
            modifier = Modifier
              .fillMaxWidth()
              .padding(horizontal = 20.dp, vertical = MaterialTheme.spacing.small),
            verticalAlignment = Alignment.CenterVertically
          ) {
            OutlinedTextField(
              value = chatInput,
              onValueChange = { chatInput = it },
              placeholder = { Text("Tulis pesan...", style = com.example.ui.theme.DiajakDesignSystem.Typography.Body, color = MaterialTheme.colorScheme.onSurface) },
              textStyle = com.example.ui.theme.DiajakDesignSystem.Typography.Body.copy(color = MaterialTheme.colorScheme.onSurface),
              shape = RoundedCornerShape(16.dp),
              modifier = Modifier.weight(1f),
              singleLine = true,
              keyboardOptions = KeyboardOptions(imeAction = ImeAction.Send),
              keyboardActions = KeyboardActions(onSend = { onSendMessage() }),
              colors = OutlinedTextFieldDefaults.colors(
                focusedTextColor = MaterialTheme.colorScheme.onSurface,
                unfocusedTextColor = MaterialTheme.colorScheme.onSurface,
                focusedContainerColor = Color.White,
                unfocusedContainerColor = Color.White,
                focusedBorderColor = DiajakOrange,
                unfocusedBorderColor = Color.Transparent
              )
            )
            Spacer(modifier = Modifier.width(MaterialTheme.spacing.small))
            IconButton(
              onClick = { onSendMessage() },
              modifier = Modifier
                .size(48.dp)
                .clip(CircleShape)
                .background(DiajakOrange)
            ) {
              Icon(
                imageVector = Icons.AutoMirrored.Outlined.Send,
                contentDescription = "Kirim",
                tint = Color.White,
                modifier = Modifier.size(20.dp)
              )
            }
          }
        }

        // Top iOS Glass Header for Chat Sheet
        DiajakGlassHeader(
          hazeState = chatHazeState,
          modifier = Modifier
            .align(Alignment.TopCenter)
            .zIndex(10f)
        ) {
          Column(modifier = Modifier.fillMaxWidth()) {
            Box(
              modifier = Modifier
                .align(Alignment.CenterHorizontally)
                .padding(top = 10.dp, bottom = 4.dp)
                .width(36.dp)
                .height(4.dp)
                .background(Color(0xFFCBD5E1), CircleShape)
            )
            Row(
              modifier = Modifier
                .fillMaxWidth()
                .height(52.dp)
                .padding(horizontal = 20.dp),
              verticalAlignment = Alignment.CenterVertically
            ) {
              Box(
                modifier = Modifier
                  .size(36.dp)
                  .clip(CircleShape)
                  .background(DiajakOrange),
                contentAlignment = Alignment.Center
              ) {
                Text(
                  text = activity.kreatorName.take(1).uppercase(),
                  color = Color.White,
                  style = com.example.ui.theme.DiajakDesignSystem.Typography.Body
                )
              }
              Spacer(modifier = Modifier.width(12.dp))
              Column(modifier = Modifier.weight(1f)) {
                Text(
                  text = activity.kreatorName,
                  style = com.example.ui.theme.DiajakDesignSystem.Typography.BodyBold,
                  color = MaterialTheme.colorScheme.onSurface
                )
                Text(
                  text = "Kreator",
                  style = com.example.ui.theme.DiajakDesignSystem.Typography.Body.copy(fontSize = 12.sp),
                  color = DiajakOrange,
                  fontWeight = FontWeight.Medium
                )
              }
              Box(
                modifier = Modifier
                  .size(8.dp)
                  .background(Color(0xFF10B981), CircleShape)
              )
              Spacer(modifier = Modifier.width(12.dp))
              IconButton(
                onClick = { isChatSheetOpen = false },
                modifier = Modifier
                  .size(36.dp)
                  .diajakGlassButton(chatHazeState)
              ) {
                Icon(
                  imageVector = Icons.Outlined.Close,
                  contentDescription = "Tutup",
                  tint = MaterialTheme.colorScheme.onSurface,
                  modifier = Modifier.size(20.dp)
                )
              }
            }
          }
        }
      }
    }

    if (isMapSheetOpen) {
      ModalBottomSheet(
        onDismissRequest = { isMapSheetOpen = false },
        sheetState = rememberModalBottomSheetState(skipPartiallyExpanded = true),
        containerColor = androidx.compose.ui.graphics.Color(0xFFE5E7EB),
        contentWindowInsets = { WindowInsets(0, 0, 0, 0) },
        dragHandle = null
      ) {
        DetailMapBottomSheet(
          activity = activity,
          onClose = { isMapSheetOpen = false }
        )
      }
    }

    if (isReviewsSheetOpen) {
      DiajakGlassBottomSheet(
        onDismissRequest = { isReviewsSheetOpen = false },
        title = "Semua Ulasan"
      ) {
        Column(
          modifier = Modifier
            .fillMaxWidth()
            .fillMaxHeight()
            .padding(top = 64.dp)
            .navigationBarsPadding()
        ) {

          // Header
          Row(
            modifier = Modifier
              .fillMaxWidth()
              .padding(horizontal = 20.dp, vertical = MaterialTheme.spacing.medium),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
          ) {
            Column {
              Text(
                text = "Semua Ulasan",
                style = com.example.ui.theme.DiajakDesignSystem.Typography.Display.copy(fontSize = 20.sp),
                color = MaterialTheme.colorScheme.onSurface
              )
              Row(
                verticalAlignment = Alignment.CenterVertically,
                modifier = Modifier.padding(top = MaterialTheme.spacing.extraSmall)
              ) {
                Icon(
                  imageVector = Icons.Outlined.Star,
                  contentDescription = null,
                  tint = RatingGold,
                  modifier = Modifier.size(18.dp)
                )
                Spacer(modifier = Modifier.width(MaterialTheme.spacing.extraSmall))
                Text(
                  text = "4.9",
                  style = com.example.ui.theme.DiajakDesignSystem.Typography.BodyBold.copy(fontSize = 14.sp),
                  color = MaterialTheme.colorScheme.onSurface
                )
                Spacer(modifier = Modifier.width(6.dp))
                Text(
                  text = "(${activity.reviewsCount} ulasan)",
                  style = com.example.ui.theme.DiajakDesignSystem.Typography.Body.copy(fontSize = 13.sp),
                  color = MaterialTheme.colorScheme.onSurfaceVariant
                )
              }
            }
            IconButton(
              onClick = { isReviewsSheetOpen = false },
              modifier = Modifier.size(36.dp)
            ) {
              Icon(
                imageVector = Icons.Outlined.Close,
                contentDescription = "Tutup",
                tint = MaterialTheme.colorScheme.onSurface
              )
            }
          }

          HorizontalDivider(color = MaterialTheme.colorScheme.outlineVariant)

          // Rating filter tabs
          var selectedRatingFilter by remember { mutableIntStateOf(0) }
          val ratingFilters = listOf("Semua", "5 ★", "4 ★", "3 ★")

          LazyRow(
            modifier = Modifier
              .fillMaxWidth()
              .padding(horizontal = 20.dp, vertical = MaterialTheme.spacing.medium),
            horizontalArrangement = Arrangement.spacedBy(MaterialTheme.spacing.small)
          ) {
            itemsIndexed(ratingFilters) { index, filterText ->
              val isSelected = selectedRatingFilter == index
              FilterChip(
                selected = isSelected,
                onClick = { selectedRatingFilter = index },
                label = {
                  Text(
                    text = filterText,
                    style = com.example.ui.theme.DiajakDesignSystem.Typography.Body.copy(
                      fontWeight = if (isSelected) FontWeight.Bold else FontWeight.Medium,
                      fontSize = 13.sp
                    ),
                    color = if (isSelected) Color.White else MaterialTheme.colorScheme.onSurface
                  )
                },
                shape = RoundedCornerShape(16.dp),
                colors = FilterChipDefaults.filterChipColors(
                  selectedContainerColor = DiajakOrange,
                  containerColor = MaterialTheme.colorScheme.surfaceVariant
                ),
                border = null
              )
            }
          }

          val filteredReviews = remember(selectedRatingFilter, allReviewsList) {
            when (selectedRatingFilter) {
              1 -> allReviewsList.filter { it.rating == 5 }
              2 -> allReviewsList.filter { it.rating == 4 }
              3 -> allReviewsList.filter { it.rating == 3 }
              else -> allReviewsList
            }
          }

          LazyColumn(
            modifier = Modifier
              .fillMaxWidth()
              .weight(1f)
              .padding(horizontal = 20.dp),
            verticalArrangement = Arrangement.spacedBy(MaterialTheme.spacing.medium),
            contentPadding = PaddingValues(top = MaterialTheme.spacing.medium, bottom = MaterialTheme.spacing.section)
          ) {
            itemsIndexed(filteredReviews) { _, rev ->
              Column(
                modifier = Modifier.fillMaxWidth()
              ) {
                Row(
                  modifier = Modifier.fillMaxWidth(),
                  horizontalArrangement = Arrangement.SpaceBetween,
                  verticalAlignment = Alignment.CenterVertically
                ) {
                  Row(verticalAlignment = Alignment.CenterVertically) {
                    Box(
                      modifier = Modifier
                        .size(32.dp)
                        .clip(CircleShape)
                        .background(DiajakOrange.copy(alpha = 0.1f)),
                      contentAlignment = Alignment.Center
                    ) {
                      Text(
                        text = rev.userName.take(1).uppercase(),
                        color = DiajakOrange,
                        style = com.example.ui.theme.DiajakDesignSystem.Typography.Body
                      )
                    }
                    Spacer(modifier = Modifier.width(MaterialTheme.spacing.small))
                    Text(
                      text = rev.userName,
                      style = com.example.ui.theme.DiajakDesignSystem.Typography.BodyBold,
                      color = MaterialTheme.colorScheme.onSurface
                    )
                  }
                  Row(
                    horizontalArrangement = Arrangement.spacedBy(2.dp)
                  ) {
                    repeat(rev.rating) {
                      Icon(
                        imageVector = Icons.Outlined.Star,
                        contentDescription = null,
                        tint = RatingGold,
                        modifier = Modifier.size(20.dp)
                      )
                    }
                  }
                }
                Spacer(modifier = Modifier.height(MaterialTheme.spacing.small))
                Text(
                  text = rev.comment,
                  style = com.example.ui.theme.DiajakDesignSystem.Typography.Body,
                  color = MaterialTheme.colorScheme.onSurface,
                  lineHeight = 22.sp
                )
                Spacer(modifier = Modifier.height(MaterialTheme.spacing.small))
                Text(
                  text = rev.date,
                  style = com.example.ui.theme.DiajakDesignSystem.Typography.Body,
                  color = MaterialTheme.colorScheme.onSurface
                )
              }
            }

            item {
              Box(
                modifier = Modifier
                  .fillMaxWidth()
                  .padding(vertical = MaterialTheme.spacing.medium),
                contentAlignment = Alignment.Center
              ) {
                BouncingDotsLoading()
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
fun InfoChipItem(icon: androidx.compose.ui.graphics.vector.ImageVector, label: String, valText: String, modifier: Modifier = Modifier) {
  Card(
    modifier = modifier,
    shape = RoundedCornerShape(16.dp),
    colors = CardDefaults.cardColors(containerColor = Color.White),
    border = BorderStroke(0.dp, Color.Transparent),
    elevation = CardDefaults.cardElevation(defaultElevation = 0.dp)
  ) {
    Column(
      horizontalAlignment = Alignment.CenterHorizontally,
      verticalArrangement = Arrangement.Center,
      modifier = Modifier.padding(vertical = MaterialTheme.spacing.medium, horizontal = 4.dp).fillMaxWidth()
    ) {
      Icon(
        imageVector = icon,
        contentDescription = label,
        modifier = Modifier.size(22.dp),
        tint = DiajakOrange
      )
      Spacer(modifier = Modifier.height(MaterialTheme.spacing.small))
      Text(
        text = label,
        style = com.example.ui.theme.DiajakDesignSystem.Typography.Body.copy(fontSize = 13.sp),
        color = MaterialTheme.colorScheme.onSurface
      )
      Spacer(modifier = Modifier.height(MaterialTheme.spacing.extraSmall))
      Text(
        text = valText,
        style = com.example.ui.theme.DiajakDesignSystem.Typography.BodyBold.copy(fontSize = 14.sp),
        color = MaterialTheme.colorScheme.onSurface,
        textAlign = androidx.compose.ui.text.style.TextAlign.Center,
        lineHeight = 18.sp
      )
    }
  }
}

@Composable
fun RowScope.TabButton(text: String, isSelected: Boolean, onClick: () -> Unit) {
  Surface(
    shape = RoundedCornerShape(16.dp),
    color = if (isSelected) DiajakOrange else Color.Transparent,
    modifier = Modifier.weight(1f).clickable { onClick() }
  ) {
    Text(
      text = text,
      textAlign = androidx.compose.ui.text.style.TextAlign.Center,
      fontWeight = if (isSelected) FontWeight.Bold else FontWeight.Medium,
      color = if (isSelected) Color.White else MaterialTheme.colorScheme.onSurface,
      style = com.example.ui.theme.DiajakDesignSystem.Typography.Body,
      modifier = Modifier.padding(vertical = MaterialTheme.spacing.small)
    )
  }
}

@Composable
fun BouncingDotsLoading() {
  val infiniteTransition = rememberInfiniteTransition(label = "bouncing_dots")
  
  val dot1Y by infiniteTransition.animateFloat(
    initialValue = 0f,
    targetValue = -12f,
    animationSpec = infiniteRepeatable(
      animation = keyframes {
        durationMillis = 600
        0.0f at 0 using LinearOutSlowInEasing
        -12.0f at 150 using LinearOutSlowInEasing
        0.0f at 300 using LinearOutSlowInEasing
      },
      repeatMode = RepeatMode.Restart
    ),
    label = "dot1"
  )
  val dot2Y by infiniteTransition.animateFloat(
    initialValue = 0f,
    targetValue = -12f,
    animationSpec = infiniteRepeatable(
      animation = keyframes {
        durationMillis = 600
        0.0f at 100 using LinearOutSlowInEasing
        -12.0f at 250 using LinearOutSlowInEasing
        0.0f at 400 using LinearOutSlowInEasing
      },
      repeatMode = RepeatMode.Restart
    ),
    label = "dot2"
  )
  val dot3Y by infiniteTransition.animateFloat(
    initialValue = 0f,
    targetValue = -12f,
    animationSpec = infiniteRepeatable(
      animation = keyframes {
        durationMillis = 600
        0.0f at 200 using LinearOutSlowInEasing
        -12.0f at 350 using LinearOutSlowInEasing
        0.0f at 500 using LinearOutSlowInEasing
      },
      repeatMode = RepeatMode.Restart
    ),
    label = "dot3"
  )

  Row(
    modifier = Modifier
      .fillMaxWidth()
      .padding(vertical = 20.dp),
    horizontalArrangement = Arrangement.Center,
    verticalAlignment = Alignment.CenterVertically
  ) {
    val dotSize = 8.dp
    val dotColor = Color(0xFFCBD5E0) // Warm gray/slate for loading dots
    
    Box(
      modifier = Modifier
        .size(dotSize)
        .graphicsLayer(translationY = dot1Y)
        .background(color = dotColor, shape = CircleShape)
    )
    Spacer(modifier = Modifier.width(MaterialTheme.spacing.small))
    Box(
      modifier = Modifier
        .size(dotSize)
        .graphicsLayer(translationY = dot2Y)
        .background(color = dotColor, shape = CircleShape)
    )
    Spacer(modifier = Modifier.width(MaterialTheme.spacing.small))
    Box(
      modifier = Modifier
        .size(dotSize)
        .graphicsLayer(translationY = dot3Y)
        .background(color = dotColor, shape = CircleShape)
    )
  }
}

private fun parseScheduleToLocalDate(schedule: String): java.time.LocalDate? {
  return parseSingleDateToLocalDate(schedule.split("•").firstOrNull()?.trim() ?: "")
}
private fun parseSingleDateToLocalDate(dateStr: String): java.time.LocalDate? {
  if (dateStr.isBlank()) return null
  try {
    // Remove day name if it contains comma (e.g. "Sabtu, 12 Ags 2026" -> "12 Ags 2026")
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
private fun findClosestUpcomingDate(schedule: String, baseToday: java.time.LocalDate): java.time.LocalDate? {
  val dates = parseMultipleSchedulesToLocalDates(schedule)
  if (dates.isEmpty()) return null
  val upcomingDates = dates.filter { !it.isBefore(baseToday) }
  return if (upcomingDates.isNotEmpty()) {
    upcomingDates.minOrNull()
  } else {
    null
  }
}
private fun getDisplayedScheduleDateText(schedule: String, baseToday: java.time.LocalDate): String {
  if (schedule.isBlank()) return "Tidak tersedia"
  val dates = parseMultipleSchedulesToLocalDates(schedule)
  if (dates.isEmpty()) {
    val raw = schedule.split("•").firstOrNull()?.trim() ?: schedule
    return if (raw.contains(",")) raw.substringAfter(",").trim() else raw
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
  
  return "${targetDate.dayOfMonth} $shortMonthName ${targetDate.year}"
}
private fun getNormalizedCity(locationName: String): String {
  val clean = locationName.lowercase()
  return when {
    clean.contains("bali") || clean.contains("badung") || clean.contains("kuta") || clean.contains("sanur") -> "bali"
    clean.contains("bandung") || clean.contains("ciwidey") || clean.contains("dago") -> "bandung"
    clean.contains("jakarta") || clean.contains("senopati") || clean.contains("kebayoran") -> "jakarta"
    clean.contains("bogor") || clean.contains("suryakencana") -> "bogor"
    clean.contains("surabaya") || clean.contains("bungkul") -> "surabaya"
    clean.contains("yogyakarta") || clean.contains("jogja") -> "yogyakarta"
    else -> locationName.split(",").firstOrNull()?.trim()?.lowercase() ?: ""
  }
}

@Composable
fun DetailMapBottomSheet(
  activity: com.example.model.ActivityModel,
  onClose: (() -> Unit)? = null
) {
  val density = LocalDensity.current
  val coroutineScope = rememberCoroutineScope()
  val mapHazeState = remember { HazeState() }

  // Match preset location if applicable, or use activity.mapX / mapY
  val matchedPreset = remember(activity.locationName) {
    creatorPresetLocations.find {
      it.name.contains(activity.locationName, ignoreCase = true) ||
      activity.locationName.contains(it.name, ignoreCase = true)
    }
  }
  val targetPinX = matchedPreset?.x ?: if (activity.mapX in 0.05f..0.95f) activity.mapX else 0.5f
  val targetPinY = matchedPreset?.y ?: if (activity.mapY in 0.05f..0.95f) activity.mapY else 0.5f

  var mapScale by remember { mutableFloatStateOf(1.2f) }
  var mapOffset by remember { mutableStateOf(Offset.Zero) }
  var isFirstLayout by remember { mutableStateOf(true) }

  val activityEmoji = remember(activity.category, activity.locationName) {
    when {
      activity.category.contains("kopi", ignoreCase = true) || activity.category.contains("coffee", ignoreCase = true) || activity.category.contains("cafe", ignoreCase = true) -> "☕"
      activity.category.contains("kuliner", ignoreCase = true) || activity.category.contains("makan", ignoreCase = true) || activity.category.contains("food", ignoreCase = true) -> "🍜"
      activity.category.contains("olahraga", ignoreCase = true) || activity.category.contains("lari", ignoreCase = true) -> "🏃"
      activity.category.contains("musik", ignoreCase = true) || activity.category.contains("konser", ignoreCase = true) -> "🎵"
      activity.category.contains("seni", ignoreCase = true) || activity.category.contains("workshop", ignoreCase = true) || activity.category.contains("craft", ignoreCase = true) -> "🎨"
      activity.category.contains("alam", ignoreCase = true) || activity.category.contains("outdoor", ignoreCase = true) || activity.category.contains("hiking", ignoreCase = true) -> "⛰️"
      activity.locationName.contains("coffee", ignoreCase = true) || activity.locationName.contains("starbucks", ignoreCase = true) || activity.locationName.contains("kopi", ignoreCase = true) -> "☕"
      else -> "📍"
    }
  }

  Column(
    modifier = Modifier
      .fillMaxWidth()
      .fillMaxHeight(0.85f)
      .navigationBarsPadding()
  ) {
    BoxWithConstraints(
      modifier = Modifier
        .fillMaxWidth()
        .weight(1f)
        .background(Color(0xFFE3F4F4))
    ) {
    val mapWidth = maxWidth
    val mapHeight = maxHeight
    val widthPx = with(density) { mapWidth.toPx() }
    val heightPx = with(density) { mapHeight.toPx() }

    LaunchedEffect(widthPx, heightPx) {
      if (widthPx > 0 && heightPx > 0 && isFirstLayout) {
        val targetX = -(widthPx * targetPinX - widthPx / 2f) * mapScale
        val targetY = -(heightPx * targetPinY - heightPx / 2f) * mapScale
        mapOffset = Offset(targetX, targetY)
        isFirstLayout = false
      }
    }

    val animatedMapScale by animateFloatAsState(targetValue = mapScale, label = "mapScale", animationSpec = spring())
    val animatedMapOffsetX by animateFloatAsState(targetValue = mapOffset.x, label = "mapOffsetX", animationSpec = spring())
    val animatedMapOffsetY by animateFloatAsState(targetValue = mapOffset.y, label = "mapOffsetY", animationSpec = spring())

    // 1. Interactive Panning & Zooming Map Canvas Container
    Box(
      modifier = Modifier
        .fillMaxSize()
        .hazeSource(state = mapHazeState)
        .pointerInput(Unit) {
          detectTransformGestures { _, pan, zoom, _ ->
            mapScale = (mapScale * zoom).coerceIn(0.6f, 4.0f)
            mapOffset += pan
          }
        }
        .graphicsLayer(
          scaleX = animatedMapScale,
          scaleY = animatedMapScale,
          translationX = animatedMapOffsetX,
          translationY = animatedMapOffsetY,
          transformOrigin = TransformOrigin(0.5f, 0.5f)
        )
    ) {
      Canvas(modifier = Modifier.fillMaxSize()) {
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

      // Surrounding Landmarks / POIs
      creatorPresetLocations.forEach { loc ->
        val isCurrentActivityLocation = (loc.x == targetPinX && loc.y == targetPinY) ||
            loc.name.equals(activity.locationName, ignoreCase = true)
        if (!isCurrentActivityLocation) {
          val lx = widthPx * loc.x
          val ly = heightPx * loc.y
          Box(
            modifier = Modifier
              .offset(
                x = with(density) { lx.toDp() } - 14.dp,
                y = with(density) { ly.toDp() } - 26.dp
              )
              .size(28.dp)
              .clickable(
                interactionSource = remember { MutableInteractionSource() },
                indication = null
              ) {
                val targetX = -(widthPx * loc.x - widthPx / 2f) * mapScale
                val targetY = -(heightPx * loc.y - heightPx / 2f) * mapScale
                mapOffset = Offset(targetX, targetY)
              },
            contentAlignment = Alignment.Center
          ) {
            Column(horizontalAlignment = Alignment.CenterHorizontally) {
              Box(
                modifier = Modifier
                  .size(22.dp)
                  .background(Color.White, CircleShape)
                  .border(1.dp, Color(0xFF94A3B8), CircleShape),
                contentAlignment = Alignment.Center
              ) {
                Text(
                  text = loc.icon,
                  style = TextStyle(fontSize = 12.sp, fontWeight = FontWeight.Bold, fontFamily = com.example.ui.theme.AppFontFamily)
                )
              }
              Icon(
                imageVector = Icons.Outlined.Place,
                contentDescription = null,
                tint = Color(0xFF64748B),
                modifier = Modifier.size(18.dp)
              )
            }
          }
        }
      }

      // Main Activity Pin and Badge
      val pinX = widthPx * targetPinX
      val pinY = heightPx * targetPinY
      Box(
        modifier = Modifier
          .layout { measurable, constraints ->
            val placeable = measurable.measure(constraints)
            layout(placeable.width, placeable.height) {
              val x = (pinX - placeable.width / 2f).roundToInt()
              val y = (pinY - placeable.height).roundToInt()
              placeable.placeRelative(x, y)
            }
          }
      ) {
        Column(horizontalAlignment = Alignment.CenterHorizontally) {
          Surface(
            shape = RoundedCornerShape(16.dp),
            color = DiajakOrange,
            shadowElevation = 0.dp
          ) {
            Row(
              modifier = Modifier.padding(horizontal = MaterialTheme.spacing.small, vertical = MaterialTheme.spacing.extraSmall),
              verticalAlignment = Alignment.CenterVertically
            ) {
              Text(
                text = activityEmoji,
                style = TextStyle(fontSize = 15.sp, fontWeight = FontWeight.Bold, fontFamily = com.example.ui.theme.AppFontFamily)
              )
              Spacer(modifier = Modifier.width(MaterialTheme.spacing.extraSmall))
              Text(
                text = activity.locationName,
                style = TextStyle(fontSize = 14.sp, fontWeight = FontWeight.Bold, fontFamily = com.example.ui.theme.AppFontFamily),
                color = Color.White,
                maxLines = 1,
                overflow = TextOverflow.Ellipsis
              )
            }
          }
          Icon(
            imageVector = Icons.Outlined.Place,
            contentDescription = "Main Location Pin",
            tint = DiajakOrange,
            modifier = Modifier.size(30.dp)
          )
        }
      }
    }

    // Top iOS Glass Header over Map
    DiajakGlassHeader(
      hazeState = mapHazeState,
      modifier = Modifier
        .align(Alignment.TopCenter)
        .zIndex(10f)
    ) {
      Column(modifier = Modifier.fillMaxWidth()) {
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
          Text(
            text = "Peta Lokasi & Titik Temu",
            style = com.example.ui.theme.DiajakDesignSystem.Typography.Headline,
            color = MaterialTheme.colorScheme.onSurface,
            textAlign = androidx.compose.ui.text.style.TextAlign.Center,
            modifier = Modifier
              .align(Alignment.Center)
              .fillMaxWidth()
          )
          if (onClose != null) {
            IconButton(
              onClick = onClose,
              modifier = Modifier
                .size(36.dp)
                .align(Alignment.CenterEnd)
                .diajakGlassButton(mapHazeState)
            ) {
              Icon(
                imageVector = Icons.Outlined.Close,
                contentDescription = "Tutup",
                tint = MaterialTheme.colorScheme.onSurface,
                modifier = Modifier.size(20.dp)
              )
            }
          }
        }
      }
    }

    // 2. Floating Zoom In / Zoom Out and My Location Floating Controls
    Column(
      modifier = Modifier
        .align(Alignment.CenterEnd)
        .padding(end = 16.dp)
        .zIndex(10f),
      verticalArrangement = Arrangement.spacedBy(8.dp)
    ) {
      IconButton(
        onClick = {
          coroutineScope.launch {
            val targetX = -(widthPx * targetPinX - widthPx / 2f) * mapScale
            val targetY = -(heightPx * targetPinY - heightPx / 2f) * mapScale
            mapOffset = Offset(targetX, targetY)
          }
        },
        modifier = Modifier
          .size(40.dp)
          .diajakGlassButton(mapHazeState)
      ) {
        Icon(
          imageVector = Icons.Outlined.MyLocation,
          contentDescription = "Pusatkan Lokasi",
          tint = DiajakOrange,
          modifier = Modifier.size(20.dp)
        )
      }

      IconButton(
        onClick = {
          mapScale = (mapScale * 1.3f).coerceIn(0.6f, 4.0f)
        },
        modifier = Modifier
          .size(40.dp)
          .diajakGlassButton(mapHazeState)
      ) {
        Icon(
          imageVector = Icons.Outlined.Add,
          contentDescription = "Zoom In",
          tint = MaterialTheme.colorScheme.onSurface,
          modifier = Modifier.size(20.dp)
        )
      }

      IconButton(
        onClick = {
          mapScale = (mapScale / 1.3f).coerceIn(0.6f, 4.0f)
        },
        modifier = Modifier
          .size(40.dp)
          .diajakGlassButton(mapHazeState)
      ) {
        Icon(
          imageVector = Icons.Outlined.Remove,
          contentDescription = "Zoom Out",
          tint = MaterialTheme.colorScheme.onSurface,
          modifier = Modifier.size(20.dp)
        )
      }
    }
  }
}
}
