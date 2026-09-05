package com.example.ui.screens
import com.example.ui.theme.spacing
import androidx.compose.material3.MaterialTheme

import android.Manifest
import android.widget.Toast
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.animation.*
import androidx.compose.animation.core.FastOutSlowInEasing
import androidx.compose.animation.core.tween
import androidx.compose.foundation.background
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.*
import androidx.compose.material.icons.filled.*
import coil.compose.rememberAsyncImagePainter
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.scale
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.layout.ContentScale
import androidx.compose.foundation.Image
import androidx.compose.ui.res.painterResource
import com.example.R
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.foundation.gestures.detectTapGestures
import androidx.compose.ui.unit.sp
import androidx.compose.ui.zIndex
import com.example.data.DiajakRepository
import com.example.ui.theme.*
import com.example.ui.viewmodel.DiajakViewModel

@Composable
fun MainScreen(viewModel: DiajakViewModel) {
  val context = LocalContext.current
  val focusManager = LocalFocusManager.current
  var showSplash by remember { mutableStateOf(true) }
  
  // Start the background real-time simulation for organizer/new-activity alerts
  LaunchedEffect(Unit) {
    viewModel.startSimulationIfNeeded(context)
  }

  val selectedTab by viewModel.selectedTab.collectAsState()
  val selectedCategory by viewModel.selectedCategory.collectAsState()
  val searchQuery by viewModel.searchQuery.collectAsState()
  val priceFilter by viewModel.priceFilter.collectAsState()
  val ratingFilter by viewModel.ratingFilter.collectAsState()
  val cityFilter by viewModel.cityFilter.collectAsState()
  val filteredActivities by viewModel.filteredActivities.collectAsState()
  val selectedActivity by viewModel.selectedActivity.collectAsState()
  val isPushTransition by viewModel.isPushTransition.collectAsState()
  val isCreateModalOpen by viewModel.isCreateModalOpen.collectAsState()
  val isBookingModalOpen by viewModel.isBookingModalOpen.collectAsState()
  val selectedLocation by viewModel.selectedLocation.collectAsState()
  val isKreatorMode by viewModel.isKreatorMode.collectAsState()
  val bookings by viewModel.userBookings.collectAsState()
  val notifications by viewModel.notifications.collectAsState()
  val threads by viewModel.threads.collectAsState()
  val isLocating by viewModel.isLocating.collectAsState()
  val isLoading by viewModel.isLoading.collectAsState()
  val isDetailLoading by viewModel.isDetailLoading.collectAsState()
  val isBookingsLoading by viewModel.isBookingsLoading.collectAsState()
  
  val hasUnreadNotifications = notifications.any { !it.isRead && it.role == "peserta" }
  val hasUnreadChats = threads.any { it.unreadCount > 0 && it.role == "peserta" }
  val showPesanBadge = hasUnreadNotifications || hasUnreadChats
  val isLoggedIn by viewModel.isLoggedIn.collectAsState()
  val isAuthDialogOpen by viewModel.isAuthDialogOpen.collectAsState()
  val favoriteActivityIds by viewModel.favoriteActivityIds.collectAsState()
  val allActivities by viewModel.allActivities.collectAsState()
  val profileName by viewModel.profileName.collectAsState()
  val profileUsername by viewModel.profileUsername.collectAsState()
  val profileImageUri by viewModel.profileImageUri.collectAsState()
  val profileImageRes by viewModel.profileImageRes.collectAsState()
  val profileBio by viewModel.profileBio.collectAsState()
  val profileGender by viewModel.profileGender.collectAsState()
  val profileBirthDate by viewModel.profileBirthDate.collectAsState()
  val profilePhone by viewModel.profilePhone.collectAsState()
  val profileEmail by viewModel.profileEmail.collectAsState()
  val isEditingProfile by viewModel.isEditingProfile.collectAsState()
  val isAboutDiajakOpen by viewModel.isAboutDiajakOpen.collectAsState()
  val isPrivacyPolicyOpen by viewModel.isPrivacyPolicyOpen.collectAsState()
  val isTermsAndConditionsOpen by viewModel.isTermsAndConditionsOpen.collectAsState()
  val isHelpCenterOpen by viewModel.isHelpCenterOpen.collectAsState()
  val selectedBookingDetail by viewModel.selectedBookingDetail.collectAsState()
  val userCoordinates by viewModel.userCoordinates.collectAsState()

  val permissionLauncher = rememberLauncherForActivityResult(
    contract = ActivityResultContracts.RequestMultiplePermissions()
  ) { permissions ->
    val fineGranted = permissions[Manifest.permission.ACCESS_FINE_LOCATION] ?: false
    val coarseGranted = permissions[Manifest.permission.ACCESS_COARSE_LOCATION] ?: false
    if (fineGranted || coarseGranted) {
      viewModel.detectUserLocation(
        context = context,
        onSuccess = { detected ->
          Toast.makeText(context, "📍 Lokasi terdeteksi: $detected", Toast.LENGTH_LONG).show()
        },
        onFailure = { err ->
          Toast.makeText(context, err, Toast.LENGTH_LONG).show()
        }
      )
    } else {
      Toast.makeText(context, "Izin lokasi ditolak. Aktifkan di pengaturan.", Toast.LENGTH_LONG).show()
    }
  }

  val onDetectLocation: () -> Unit = {
    if (!isLocating) {
      viewModel.detectUserLocation(
        context = context,
        onSuccess = { detected ->
          Toast.makeText(context, "📍 Lokasi terdeteksi: $detected", Toast.LENGTH_LONG).show()
        },
        onFailure = { err ->
          Toast.makeText(context, err, Toast.LENGTH_LONG).show()
        }
      )
    }
  }

  // Automatically detect location on startup without requiring permission dialog
  LaunchedEffect(Unit) {
    viewModel.detectUserLocation(
      context = context,
      onSuccess = { _ -> }
    )
  }

  LaunchedEffect(selectedTab) {
    focusManager.clearFocus()
  }

  LaunchedEffect(selectedActivity) {
    focusManager.clearFocus()
  }

  LaunchedEffect(isEditingProfile, isAboutDiajakOpen, isPrivacyPolicyOpen, isTermsAndConditionsOpen, isHelpCenterOpen, isKreatorMode) {
    focusManager.clearFocus()
  }

  Box(
    modifier = Modifier
      .fillMaxSize()
      .pointerInput(Unit) {
        detectTapGestures(
          onTap = {
            focusManager.clearFocus()
          }
        )
      }
  ) {
    if (!showSplash) {
      if (isKreatorMode) {
        CreatorDashboardScreen(
          viewModel = viewModel,
          onToggleKreatorMode = { viewModel.toggleKreatorMode() }
        )
      } else {
        Scaffold(
          modifier = Modifier.fillMaxSize(),
          containerColor = androidx.compose.ui.graphics.Color(0xFFE5E7EB)
        ) { innerPadding ->
          Box(modifier = Modifier.fillMaxSize()) {
            when (selectedTab) {
              0 -> HomeScreen(
                activities = allActivities,
                categories = DiajakRepository.categories,
                selectedCategory = selectedCategory,
                searchQuery = searchQuery,
                selectedLocation = selectedLocation,
                isKreatorMode = isKreatorMode,
                notifications = notifications,
                userName = profileName,
                onSelectCategory = { viewModel.selectCategory(it) },
                onSearchChange = { viewModel.updateSearch(it) },
                onSearchFocus = { viewModel.resetFilters() },
                onActivityClick = { viewModel.openActivityDetail(it) },
                onOpenCreate = { viewModel.openCreateModal() },
                onToggleKreatorMode = { viewModel.toggleKreatorMode() },
                onNavigateToExplore = { viewModel.setTab(5) },
                onSelectCityFilter = { cityName ->
                  viewModel.updateCityFilter(cityName)
                  viewModel.setTab(5)
                },
                onNavigateToNotifications = { viewModel.setTab(3) },
                onPromoClick = { viewModel.navigateToPromo(it) },
                onMarkAllAsRead = { viewModel.markAllNotificationsAsRead() },
                onClearAll = { viewModel.clearAllNotifications() },
                onDetectLocation = onDetectLocation,
                isLocating = isLocating,
                onNotificationClick = { notif ->
                  if (notif.type == "booking") {
                    viewModel.setTab(2)
                  } else if (notif.type == "message") {
                    viewModel.setTab(3)
                  } else if (notif.type == "activity" && notif.relatedId != null) {
                    val target = DiajakRepository.activities.find { it.id == notif.relatedId }
                    if (target != null) {
                      viewModel.openActivityDetail(target)
                    }
                  }
                },
                favoriteAcaraIds = favoriteActivityIds,
                onToggleFavorite = { viewModel.toggleFavorite(it.id) },
                userCoordinates = userCoordinates,
                profileImageUri = profileImageUri,
                profileImageRes = profileImageRes,
                onProfileClick = { viewModel.setTab(4) },
                isLoading = isLoading
              )
              1 -> FavoritesScreen(
                favoriteActivities = allActivities.filter { favoriteActivityIds.contains(it.id) },
                onActivityClick = { viewModel.openActivityDetail(it) },
                onToggleFavorite = { viewModel.toggleFavorite(it.id) },
                onExploreClick = { viewModel.setTab(0) },
                isLoading = isLoading,
                userCoordinates = userCoordinates
              )
              2 -> BookingsScreen(
                bookings = bookings,
                onExploreClick = { viewModel.setTab(0) },
                onBookingClick = { viewModel.openBookingDetail(it) },
                isLoading = isBookingsLoading
              )
              3 -> {
                val activeChatPartner by viewModel.activeChatPartner.collectAsState()
                val activeChatActivityTitle by viewModel.activeChatActivityTitle.collectAsState()
                MessagesScreen(
                  onExploreClick = { viewModel.setTab(0) },
                  initialChatPartner = activeChatPartner,
                  initialActivityTitle = activeChatActivityTitle,
                  onChatOpened = { viewModel.clearActiveChatPartner() },
                  onChatClosed = { viewModel.closeChatAndReturn() },
                  isKreatorMode = isKreatorMode,
                  notifications = notifications,
                  onToggleNotificationRead = { viewModel.toggleNotificationRead(it) },
                  onToggleNotificationArchive = { viewModel.toggleNotificationArchive(it) },
                  onDeleteNotification = { viewModel.deleteNotification(it) },
                  onMarkAllNotificationsAsRead = { viewModel.markAllNotificationsAsRead() },
                  onClearAllNotifications = { viewModel.clearAllNotifications() },
                  threads = threads,
                  onDeleteThread = { viewModel.deleteThread(it) },
                  onToggleThreadRead = { viewModel.toggleThreadRead(it) },
                  onToggleThreadArchive = { viewModel.toggleThreadArchive(it) },
                  onAddThread = { viewModel.addThread(it) }
                )
              }
              4 -> {
                val currentProfileSubScreen = when {
                  isEditingProfile -> "edit_profile"
                  isAboutDiajakOpen -> "about"
                  isPrivacyPolicyOpen -> "privacy"
                  isTermsAndConditionsOpen -> "terms"
                  isHelpCenterOpen -> "help"
                  else -> "main"
                }

                AnimatedContent(
                  targetState = currentProfileSubScreen,
                  modifier = Modifier.fillMaxSize(),
                  transitionSpec = {
                    if (targetState != "main") {
                      (slideInHorizontally(
                        initialOffsetX = { fullWidth -> fullWidth },
                        animationSpec = tween(350, easing = FastOutSlowInEasing)
                      ) + fadeIn(animationSpec = tween(300))) togetherWith
                          (slideOutHorizontally(
                            targetOffsetX = { fullWidth -> -fullWidth / 3 },
                            animationSpec = tween(350, easing = FastOutSlowInEasing)
                          ) + fadeOut(animationSpec = tween(200)))
                    } else {
                      (slideInHorizontally(
                        initialOffsetX = { fullWidth -> -fullWidth / 3 },
                        animationSpec = tween(350, easing = FastOutSlowInEasing)
                      ) + fadeIn(animationSpec = tween(300))) togetherWith
                          (slideOutHorizontally(
                            targetOffsetX = { fullWidth -> fullWidth },
                            animationSpec = tween(350, easing = FastOutSlowInEasing)
                          ) + fadeOut(animationSpec = tween(200)))
                    }
                  },
                  label = "ProfileSubScreenTransition"
                ) { screen ->
                  when (screen) {
                    "edit_profile" -> {
                      EditProfileScreen(
                        profileName = profileName,
                        profileUsername = profileUsername,
                        profileBio = profileBio,
                        profileGender = profileGender,
                        profileBirthDate = profileBirthDate,
                        profilePhone = profilePhone,
                        profileEmail = profileEmail,
                        profileImageUri = profileImageUri,
                        profileImageRes = profileImageRes,
                        onUpdateName = { viewModel.updateProfileName(it) },
                        onUpdateUsername = { viewModel.updateProfileUsername(it) },
                        onUpdateBio = { viewModel.updateProfileBio(it) },
                        onUpdateGender = { viewModel.updateProfileGender(it) },
                        onUpdateBirthDate = { viewModel.updateProfileBirthDate(it) },
                        onUpdatePhone = { viewModel.updateProfilePhone(it) },
                        onUpdateEmail = { viewModel.updateProfileEmail(it) },
                        onUpdateImageUri = { viewModel.updateProfileImageUri(it) },
                        onUpdateImageRes = { viewModel.updateProfileImageRes(it) },
                        onBack = { viewModel.setEditingProfile(false) }
                      )
                    }
                    "about" -> {
                      AboutDiajakScreen(
                        onBack = { viewModel.setAboutDiajakOpen(false) }
                      )
                    }
                    "privacy" -> {
                      PrivacyPolicyScreen(
                        onBack = { viewModel.setPrivacyPolicyOpen(false) }
                      )
                    }
                    "terms" -> {
                      TermsAndConditionsScreen(
                        onBack = { viewModel.setTermsAndConditionsOpen(false) }
                      )
                    }
                    "help" -> {
                      HelpCenterScreen(
                        onBack = { viewModel.setHelpCenterOpen(false) }
                      )
                    }
                    else -> {
                      ProfileScreen(
                        viewModel = viewModel,
                        isLoggedIn = isLoggedIn,
                        isKreatorMode = isKreatorMode,
                        profileName = profileName,
                        profileEmail = profileEmail,
                        profileUsername = profileUsername,
                        profileImageUri = profileImageUri,
                        profileImageRes = profileImageRes,
                        onToggleKreatorMode = { viewModel.toggleKreatorMode() },
                        onOpenCreateActivity = { viewModel.openCreateModal() },
                        onLoginSuccess = { viewModel.loginOrRegisterUser() },
                        onLogoutClick = { viewModel.logoutUser() },
                        onEditProfileClick = { viewModel.setEditingProfile(true) },
                        onAboutDiajakClick = { viewModel.setAboutDiajakOpen(true) },
                        onPrivacyPolicyClick = { viewModel.setPrivacyPolicyOpen(true) },
                        onTermsAndConditionsClick = { viewModel.setTermsAndConditionsOpen(true) },
                        onHelpCenterClick = { viewModel.setHelpCenterOpen(true) }
                      )
                    }
                  }
                }
              }
              5 -> MapExploreScreen(
                activities = filteredActivities,
                categories = DiajakRepository.categories,
                selectedCategory = selectedCategory,
                searchQuery = searchQuery,
                onSelectCategory = { viewModel.selectCategory(it) },
                onSearchChange = { viewModel.updateSearch(it) },
                onSelectActivity = { viewModel.openActivityDetail(it) },
                onBack = { viewModel.setTab(0) },
                onDetectLocation = onDetectLocation,
                isLocating = isLocating,
                favoriteAcaraIds = favoriteActivityIds,
                onToggleFavorite = { viewModel.toggleFavorite(it.id) },
                priceFilter = priceFilter,
                onPriceFilterChange = { viewModel.updatePriceFilter(it) },
                ratingFilter = ratingFilter,
                onRatingFilterChange = { viewModel.updateRatingFilter(it) },
                cityFilter = cityFilter,
                onCityFilterChange = { viewModel.updateCityFilter(it) },
                userCoordinates = userCoordinates,
                profileImageUri = profileImageUri,
                profileImageRes = profileImageRes,
                onProfileClick = { viewModel.setTab(4) }
              )
            }

            if (!(selectedTab == 5 || (selectedTab == 4 && (!isLoggedIn || isEditingProfile || isAboutDiajakOpen || isPrivacyPolicyOpen || isTermsAndConditionsOpen || isHelpCenterOpen)))) {
              Box(
                modifier = Modifier
                  .align(Alignment.BottomCenter)
                  .zIndex(99f)
              ) {
                DiajakBottomNav(
                  selectedTab = selectedTab,
                  showPesanBadge = showPesanBadge,
                  showFavoritBadge = favoriteActivityIds.isNotEmpty(),
                  showUndanganBadge = bookings.isNotEmpty(),
                  profileImageUri = profileImageUri,
                  profileImageRes = profileImageRes,
                  onTabSelected = { viewModel.setTab(it) }
                )
              }
            }
          }
        }
      }

      // Activity Detail Modal overlay
      if (selectedActivity != null) {
        androidx.activity.compose.BackHandler {
          viewModel.closeActivityDetail()
        }
      }

      AnimatedContent(
        targetState = selectedActivity,
        transitionSpec = {
          if (isPushTransition) {
            (slideInHorizontally(
              initialOffsetX = { fullWidth -> fullWidth },
              animationSpec = tween(350, easing = FastOutSlowInEasing)
            ) + fadeIn(animationSpec = tween(300))) togetherWith
                (slideOutHorizontally(
                  targetOffsetX = { fullWidth -> -fullWidth / 3 },
                  animationSpec = tween(350, easing = FastOutSlowInEasing)
                ) + fadeOut(animationSpec = tween(200)))
          } else {
            (slideInHorizontally(
              initialOffsetX = { fullWidth -> -fullWidth / 3 },
              animationSpec = tween(350, easing = FastOutSlowInEasing)
            ) + fadeIn(animationSpec = tween(300))) togetherWith
                (slideOutHorizontally(
                  targetOffsetX = { fullWidth -> fullWidth },
                  animationSpec = tween(350, easing = FastOutSlowInEasing)
                ) + fadeOut(animationSpec = tween(200)))
          }
        },
        contentKey = { it?.id },
        label = "DetailScreenTransition"
      ) { act ->
        act?.let { nonNullAct ->
          DetailScreen(
            activity = nonNullAct,
            onBack = { viewModel.closeActivityDetail() },
            onBookClick = { viewModel.openBookingModal() },
            onChatWithKreator = { partnerName ->
              viewModel.startChatWith(partnerName, nonNullAct.title)
            },
            isFavorite = favoriteActivityIds.contains(nonNullAct.id),
            onToggleFavorite = { viewModel.toggleFavorite(nonNullAct.id) },
            isLoggedIn = isLoggedIn,
            onRequireLogin = { viewModel.openAuthDialog() },
            isLoading = isDetailLoading,
            onActivityClick = { viewModel.openActivityDetail(it) },
            favoriteAcaraIds = favoriteActivityIds,
            onToggleFavoriteActivity = { viewModel.toggleFavorite(it.id) }
          )
        }
      }

      // Create Activity Modal
      if (isCreateModalOpen) {
        CreateActivityDialog(
          onDismiss = { viewModel.closeCreateModal() },
          onSubmit = { title, cat, loc, addr, sched, priceStr, priceVal, desc, quota, img, benefits, mX, mY, isOngoing, isRec, recDays ->
            viewModel.createNewActivity(context, title, cat, loc, addr, sched, priceStr, priceVal, desc, quota, img, benefits, mX, mY, isOngoing, isRec, recDays)
          }
        )
      }

      // Booking Flow Overlay Screen
      AnimatedVisibility(
        visible = isBookingModalOpen && selectedActivity != null,
        enter = slideInHorizontally(
          initialOffsetX = { fullWidth -> fullWidth },
          animationSpec = tween(350, easing = FastOutSlowInEasing)
        ) + fadeIn(animationSpec = tween(300)),
        exit = slideOutHorizontally(
          targetOffsetX = { fullWidth -> fullWidth },
          animationSpec = tween(350, easing = FastOutSlowInEasing)
        ) + fadeOut(animationSpec = tween(200))
      ) {
        selectedActivity?.let { activityItem ->
          BookingFlowScreen(
            activity = activityItem,
            onDismiss = { viewModel.closeBookingModal() },
            onBookingConfirmed = { count, name, email, phone, bookingId, selDate ->
              viewModel.bookActivity(context, activityItem, count, false, name, email, phone, bookingId, selDate)
            },
            onViewAllUndangans = {
              viewModel.closeBookingModal()
              viewModel.closeActivityDetail()
              viewModel.setTab(2)
            },
            initialName = profileName,
            initialEmail = profileEmail,
            initialPhone = profilePhone
          )
        }
      }

      // Airbnb-style Auth Dialog
      if (isAuthDialogOpen) {
        AuthDialog(
          viewModel = viewModel,
          onDismiss = { viewModel.closeAuthDialog() },
          onLoginSuccess = { viewModel.loginOrRegisterUser() }
        )
      }

      // Full-screen Undangan Voucher detail overlay
      AnimatedVisibility(
        visible = selectedBookingDetail != null,
        enter = slideInHorizontally(
          initialOffsetX = { fullWidth -> fullWidth },
          animationSpec = tween(350, easing = FastOutSlowInEasing)
        ) + fadeIn(animationSpec = tween(300)),
        exit = slideOutHorizontally(
          targetOffsetX = { fullWidth -> fullWidth },
          animationSpec = tween(350, easing = FastOutSlowInEasing)
        ) + fadeOut(animationSpec = tween(200))
      ) {
        selectedBookingDetail?.let { b ->
          InvitationDetailOverlay(
            booking = b,
            onDismiss = { viewModel.closeBookingDetail() },
            onChatWithKreator = { activityTitle ->
              viewModel.closeBookingDetail()
              val kreator = allActivities.find { it.id == b.activityId }?.kreatorName ?: "Kreator"
              viewModel.startChatWith(kreator, activityTitle)
            }
          )
        }
      }
    } else {
      SplashScreen(onAnimationFinished = { showSplash = false })
    }
  }
}

@Composable
fun DiajakBottomNav(
  selectedTab: Int,
  showPesanBadge: Boolean,
  showFavoritBadge: Boolean,
  showUndanganBadge: Boolean,
  profileImageUri: String? = null,
  profileImageRes: Int? = null,
  onTabSelected: (Int) -> Unit
) {
  Box(
    modifier = Modifier
      .fillMaxWidth()
      .navigationBarsPadding()
      .padding(horizontal = 20.dp, vertical = 10.dp),
    contentAlignment = Alignment.Center
  ) {
    Surface(
      modifier = Modifier
        .fillMaxWidth()
        .height(64.dp)
        .shadow(
          elevation = 10.dp,
          shape = CircleShape,
          spotColor = Color.Black.copy(alpha = 0.09f),
          ambientColor = Color.Black.copy(alpha = 0.04f)
        )
        .border(
          width = 0.75.dp,
          color = Color.White.copy(alpha = 0.85f),
          shape = CircleShape
        ),
      shape = CircleShape,
      color = Color.White.copy(alpha = 0.94f)
    ) {
      Row(
        modifier = Modifier
          .fillMaxSize()
          .padding(horizontal = 12.dp),
        horizontalArrangement = Arrangement.SpaceAround,
        verticalAlignment = Alignment.CenterVertically
      ) {
        BottomNavItem(
          label = "Beranda",
          selected = selectedTab == 0,
          icon = if (selectedTab == 0) Icons.Filled.Home else Icons.Outlined.Home,
          onClick = { onTabSelected(0) }
        )

        BottomNavItem(
          label = "Favorit",
          selected = selectedTab == 1,
          icon = if (selectedTab == 1) Icons.Filled.Favorite else Icons.Outlined.FavoriteBorder,
          showBadge = showFavoritBadge,
          onClick = { onTabSelected(1) }
        )

        BottomNavItem(
          label = "Invitasi",
          selected = selectedTab == 2,
          icon = if (selectedTab == 2) Icons.Filled.ConfirmationNumber else Icons.Outlined.ConfirmationNumber,
          showBadge = showUndanganBadge,
          onClick = { onTabSelected(2) }
        )

        BottomNavItem(
          label = "Pesan",
          selected = selectedTab == 3,
          icon = if (selectedTab == 3) Icons.Filled.ChatBubble else Icons.Outlined.ChatBubbleOutline,
          showBadge = showPesanBadge,
          onClick = { onTabSelected(3) }
        )

        BottomNavItem(
          label = "Profil",
          selected = selectedTab == 4,
          icon = if (selectedTab == 4) Icons.Filled.AccountCircle else Icons.Outlined.AccountCircle,
          profileImageUri = profileImageUri,
          profileImageRes = profileImageRes,
          onClick = { onTabSelected(4) }
        )
      }
    }
  }
}

@Composable
fun BottomNavItem(
  label: String,
  selected: Boolean,
  icon: androidx.compose.ui.graphics.vector.ImageVector,
  profileImageUri: String? = null,
  profileImageRes: Int? = null,
  showBadge: Boolean = false,
  onClick: () -> Unit
) {
  val activeColor = DiajakOrange
  val inactiveColor = Color(0xFF6B7280)

  val animScale by androidx.compose.animation.core.animateFloatAsState(
    targetValue = if (selected) 1.05f else 1.0f,
    animationSpec = androidx.compose.animation.core.spring(
      dampingRatio = androidx.compose.animation.core.Spring.DampingRatioLowBouncy,
      stiffness = androidx.compose.animation.core.Spring.StiffnessMedium
    )
  )

  Column(
    horizontalAlignment = Alignment.CenterHorizontally,
    verticalArrangement = Arrangement.Center,
    modifier = Modifier
      .scale(animScale)
      .clip(RoundedCornerShape(12.dp))
      .clickable(
        interactionSource = remember { MutableInteractionSource() },
        indication = null,
        onClick = onClick
      )
      .padding(horizontal = 8.dp, vertical = 4.dp)
  ) {
    Box(contentAlignment = Alignment.TopEnd) {
      if (profileImageUri != null || profileImageRes != null) {
        Box(
          modifier = Modifier
            .size(24.dp)
            .clip(CircleShape)
            .border(
              width = if (selected) 2.dp else 0.75.dp,
              color = if (selected) DiajakOrange else Color.Black.copy(alpha = 0.12f),
              shape = CircleShape
            ),
          contentAlignment = Alignment.Center
        ) {
          Image(
            painter = if (profileImageUri != null) {
              rememberAsyncImagePainter(model = profileImageUri)
            } else {
              rememberAsyncImagePainter(model = profileImageRes)
            },
            contentDescription = label,
            modifier = Modifier.fillMaxSize(),
            contentScale = ContentScale.Crop
          )
        }
      } else {
        Icon(
          imageVector = icon,
          contentDescription = label,
          tint = if (selected) activeColor else inactiveColor,
          modifier = Modifier.size(24.dp)
        )
      }
      if (showBadge) {
        Box(
          modifier = Modifier
            .offset(x = 4.dp, y = (-2).dp)
            .size(8.dp)
            .background(DiajakOrange, CircleShape)
            .border(1.5.dp, Color.White, CircleShape)
        )
      }
    }
    Spacer(modifier = Modifier.height(3.dp))
    Text(
      text = label,
      fontSize = 11.sp,
      fontWeight = if (selected) FontWeight.SemiBold else FontWeight.Medium,
      color = if (selected) activeColor else inactiveColor,
      maxLines = 1
    )
  }
}

@Composable
fun SplashScreen(onAnimationFinished: () -> Unit) {
  var startAnim by remember { mutableStateOf(false) }

  // Scale and Alpha animations
  val scale by androidx.compose.animation.core.animateFloatAsState(
    targetValue = if (startAnim) 1f else 0.5f,
    animationSpec = androidx.compose.animation.core.spring(
      dampingRatio = androidx.compose.animation.core.Spring.DampingRatioMediumBouncy,
      stiffness = androidx.compose.animation.core.Spring.StiffnessLow
    )
  )
  val alpha by androidx.compose.animation.core.animateFloatAsState(
    targetValue = if (startAnim) 1f else 0f,
    animationSpec = androidx.compose.animation.core.tween(durationMillis = 1000)
  )

  var exitAnim by remember { mutableStateOf(false) }
  val exitScale by androidx.compose.animation.core.animateFloatAsState(
    targetValue = if (exitAnim) 1.5f else 1f,
    animationSpec = androidx.compose.animation.core.tween(durationMillis = 500, easing = androidx.compose.animation.core.FastOutSlowInEasing)
  )
  val exitAlpha by androidx.compose.animation.core.animateFloatAsState(
    targetValue = if (exitAnim) 0f else 1f,
    animationSpec = androidx.compose.animation.core.tween(durationMillis = 500)
  )

  LaunchedEffect(Unit) {
    startAnim = true
    kotlinx.coroutines.delay(300) // Quick splash presentation
    exitAnim = true
    kotlinx.coroutines.delay(150)  // Fast exit transition
    onAnimationFinished()
  }

  Box(
    modifier = Modifier
      .fillMaxSize()
      .background(com.example.ui.theme.DiajakOrange)
      .graphicsLayer(alpha = exitAlpha),
    contentAlignment = Alignment.Center
  ) {
    Column(
      horizontalAlignment = Alignment.CenterHorizontally,
      modifier = Modifier
        .scale(scale * exitScale)
        .graphicsLayer(alpha = alpha)
    ) {
      Image(
        painter = painterResource(id = R.drawable.ic_diajak_logo),
        contentDescription = "Logo",
        modifier = Modifier.size(100.dp),
        contentScale = ContentScale.Fit
      )
    }
  }
}
