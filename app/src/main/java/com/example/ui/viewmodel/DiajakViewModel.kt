package com.example.ui.viewmodel

import android.content.Context
import android.annotation.SuppressLint
import android.location.Location
import com.google.android.gms.location.LocationServices
import com.google.android.gms.location.Priority
import com.google.android.gms.tasks.CancellationTokenSource
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.data.DiajakRepository
import com.example.model.ActivityModel
import com.example.model.AcaraModel
import com.example.model.BookingModel
import com.example.model.DiajakNotification
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import java.util.UUID
import androidx.compose.ui.graphics.Color
import com.example.model.MessageThread

class DiajakViewModel : ViewModel() {

  private val _isLoading = MutableStateFlow(false)
  val isLoading: StateFlow<Boolean> = _isLoading.asStateFlow()

  private val _isDetailLoading = MutableStateFlow(false)
  val isDetailLoading: StateFlow<Boolean> = _isDetailLoading.asStateFlow()

  private val _isBookingsLoading = MutableStateFlow(false)
  val isBookingsLoading: StateFlow<Boolean> = _isBookingsLoading.asStateFlow()

  fun refreshContent() {
    viewModelScope.launch {
      _isLoading.value = true
      kotlinx.coroutines.delay(500)
      _isLoading.value = false
    }
  }

  fun triggerCategoryLoading() {
    viewModelScope.launch {
      _isLoading.value = true
      kotlinx.coroutines.delay(350)
      _isLoading.value = false
    }
  }

  private val _isKtpUploaded = MutableStateFlow(false)
  val isKtpUploaded: StateFlow<Boolean> = _isKtpUploaded.asStateFlow()

  private val _isBukuTabunganUploaded = MutableStateFlow(false)
  val isBukuTabunganUploaded: StateFlow<Boolean> = _isBukuTabunganUploaded.asStateFlow()

  fun setKtpUploaded(uploaded: Boolean) {
    _isKtpUploaded.value = uploaded
  }

  fun setBukuTabunganUploaded(uploaded: Boolean) {
    _isBukuTabunganUploaded.value = uploaded
  }

  // User profile states matching the profile edit screen
  private val _profileName = MutableStateFlow("Anggi Saputro")
  val profileName: StateFlow<String> = _profileName.asStateFlow()

  private val _profileUsername = MutableStateFlow("gisaput")
  val profileUsername: StateFlow<String> = _profileUsername.asStateFlow()

  private val _profileImageUri = MutableStateFlow<String?>(null)
  val profileImageUri: StateFlow<String?> = _profileImageUri.asStateFlow()

  private val _profileImageRes = MutableStateFlow<Int>(com.example.R.drawable.img_profile_cat_1783601304885)
  val profileImageRes: StateFlow<Int> = _profileImageRes.asStateFlow()

  private val _profileBio = MutableStateFlow("Enjoy fun run")
  val profileBio: StateFlow<String> = _profileBio.asStateFlow()

  private val _profileGender = MutableStateFlow("Pria")
  val profileGender: StateFlow<String> = _profileGender.asStateFlow()

  private val _profileBirthDate = MutableStateFlow("12/08/1987")
  val profileBirthDate: StateFlow<String> = _profileBirthDate.asStateFlow()

  private val _profilePhone = MutableStateFlow("081234567890")
  val profilePhone: StateFlow<String> = _profilePhone.asStateFlow()

  private val _profileEmail = MutableStateFlow("gisaput@diajak.com")
  val profileEmail: StateFlow<String> = _profileEmail.asStateFlow()

  private val _isEditingProfile = MutableStateFlow(false)
  val isEditingProfile: StateFlow<Boolean> = _isEditingProfile.asStateFlow()

  private val _isAboutDiajakOpen = MutableStateFlow(false)
  val isAboutDiajakOpen: StateFlow<Boolean> = _isAboutDiajakOpen.asStateFlow()

  private val _isPrivacyPolicyOpen = MutableStateFlow(false)
  val isPrivacyPolicyOpen: StateFlow<Boolean> = _isPrivacyPolicyOpen.asStateFlow()

  private val _isTermsAndConditionsOpen = MutableStateFlow(false)
  val isTermsAndConditionsOpen: StateFlow<Boolean> = _isTermsAndConditionsOpen.asStateFlow()

  private val _isHelpCenterOpen = MutableStateFlow(false)
  val isHelpCenterOpen: StateFlow<Boolean> = _isHelpCenterOpen.asStateFlow()

  fun setEditingProfile(active: Boolean) {
    _isEditingProfile.value = active
  }

  fun setAboutDiajakOpen(active: Boolean) {
    _isAboutDiajakOpen.value = active
  }

  fun setPrivacyPolicyOpen(active: Boolean) {
    _isPrivacyPolicyOpen.value = active
  }

  fun setTermsAndConditionsOpen(active: Boolean) {
    _isTermsAndConditionsOpen.value = active
  }

  fun setHelpCenterOpen(active: Boolean) {
    _isHelpCenterOpen.value = active
  }

  private fun saveProfileToDatabase() {
    val email = _profileEmail.value
    if (email.isNotBlank()) {
      viewModelScope.launch(kotlinx.coroutines.Dispatchers.IO) {
        DiajakRepository.saveUserProfile(
          com.example.data.database.UserProfileEntity(
            email = email,
            name = _profileName.value,
            username = _profileUsername.value,
            bio = _profileBio.value,
            gender = _profileGender.value,
            birthDate = _profileBirthDate.value,
            phone = _profilePhone.value,
            imageUri = _profileImageUri.value,
            imageRes = _profileImageRes.value
          )
        )
      }
    }
  }

  fun updateProfileName(name: String) {
    _profileName.value = name
    saveProfileToDatabase()
  }

  fun updateProfileUsername(username: String) {
    _profileUsername.value = username
    saveProfileToDatabase()
  }

  fun updateProfileImageUri(uri: String?) {
    _profileImageUri.value = uri
    saveProfileToDatabase()
  }

  fun updateProfileImageRes(resId: Int) {
    _profileImageRes.value = resId
    _profileImageUri.value = null
    saveProfileToDatabase()
  }

  fun updateProfileBio(bio: String) {
    _profileBio.value = bio
    saveProfileToDatabase()
  }

  fun updateProfileGender(gender: String) {
    _profileGender.value = gender
    saveProfileToDatabase()
  }

  fun updateProfileBirthDate(birthDate: String) {
    _profileBirthDate.value = birthDate
    saveProfileToDatabase()
  }

  fun updateProfilePhone(phone: String) {
    _profilePhone.value = phone
    saveProfileToDatabase()
  }

  fun updateProfileEmail(email: String) {
    _profileEmail.value = email
    saveProfileToDatabase()
  }

  private val _isOnboardingCompleted = MutableStateFlow(false)
  val isOnboardingCompleted: StateFlow<Boolean> = _isOnboardingCompleted.asStateFlow()

  private val _selectedTab = MutableStateFlow(0) // 0: Home, 1: Map, 2: Bookings, 3: Messages, 4: Profile
  val selectedTab: StateFlow<Int> = _selectedTab.asStateFlow()

  private val _selectedCategory = MutableStateFlow("all")
  val selectedCategory: StateFlow<String> = _selectedCategory.asStateFlow()

  private val _searchQuery = MutableStateFlow("")
  val searchQuery: StateFlow<String> = _searchQuery.asStateFlow()

  private val _priceFilter = MutableStateFlow("all")
  val priceFilter: StateFlow<String> = _priceFilter.asStateFlow()

  private val _ratingFilter = MutableStateFlow("all")
  val ratingFilter: StateFlow<String> = _ratingFilter.asStateFlow()

  private val _cityFilter = MutableStateFlow("all")
  val cityFilter: StateFlow<String> = _cityFilter.asStateFlow()

  fun updatePriceFilter(filter: String) {
    _priceFilter.value = filter
  }

  fun updateRatingFilter(filter: String) {
    _ratingFilter.value = filter
  }

  fun updateCityFilter(filter: String) {
    _cityFilter.value = filter
  }

  fun navigateToPromo(promoId: String) {
    _selectedTab.value = 5 // Go directly to Map/Explore tab
    when (promoId) {
      "p4" -> {
        _priceFilter.value = "under100k"
        _selectedCategory.value = "all"
        _searchQuery.value = ""
        _ratingFilter.value = "all"
        _cityFilter.value = "all"
      }
      "p3" -> {
        _priceFilter.value = "free"
        _selectedCategory.value = "all"
        _searchQuery.value = ""
        _ratingFilter.value = "all"
        _cityFilter.value = "all"
      }
      "p1" -> {
        _priceFilter.value = "all"
        _selectedCategory.value = "outdoor"
        _searchQuery.value = ""
        _ratingFilter.value = "all"
        _cityFilter.value = "all"
      }
      "p2" -> {
        _priceFilter.value = "all"
        _selectedCategory.value = "coffee"
        _searchQuery.value = ""
        _ratingFilter.value = "all"
        _cityFilter.value = "all"
      }
    }
  }

  private val _selectedActivity = MutableStateFlow<ActivityModel?>(null)
  val selectedActivity: StateFlow<ActivityModel?> = _selectedActivity.asStateFlow()
  val selectedAcara: StateFlow<ActivityModel?> get() = selectedActivity

  private val _selectedActivityStack = MutableStateFlow<List<ActivityModel>>(emptyList())
  val selectedActivityStack: StateFlow<List<ActivityModel>> = _selectedActivityStack.asStateFlow()
  val selectedAcaraStack: StateFlow<List<ActivityModel>> get() = selectedActivityStack

  private val _isPushTransition = MutableStateFlow(true)
  val isPushTransition: StateFlow<Boolean> = _isPushTransition.asStateFlow()

  private val _selectedBookingDetail = MutableStateFlow<BookingModel?>(null)
  val selectedBookingDetail: StateFlow<BookingModel?> = _selectedBookingDetail.asStateFlow()

  fun openBookingDetail(booking: BookingModel) {
    _selectedBookingDetail.value = booking
  }

  fun closeBookingDetail() {
    _selectedBookingDetail.value = null
  }

  private val _isCreateModalOpen = MutableStateFlow(false)
  val isCreateModalOpen: StateFlow<Boolean> = _isCreateModalOpen.asStateFlow()

  private val _isBookingModalOpen = MutableStateFlow(false)
  val isBookingModalOpen: StateFlow<Boolean> = _isBookingModalOpen.asStateFlow()

  private val _selectedLocation = MutableStateFlow("Yogyakarta & Sekitarnya")
  val selectedLocation: StateFlow<String> = _selectedLocation.asStateFlow()

  private val _isLocating = MutableStateFlow(false)
  val isLocating: StateFlow<Boolean> = _isLocating.asStateFlow()

  private val _userCoordinates = MutableStateFlow<Pair<Double, Double>?>(Pair(-7.7956, 110.3695))
  val userCoordinates: StateFlow<Pair<Double, Double>?> = _userCoordinates.asStateFlow()

  private val _isLoggedIn = MutableStateFlow(false)
  val isLoggedIn: StateFlow<Boolean> = _isLoggedIn.asStateFlow()

  private val _isAuthDialogOpen = MutableStateFlow(false)
  val isAuthDialogOpen: StateFlow<Boolean> = _isAuthDialogOpen.asStateFlow()

  private val _favoriteActivityIds = MutableStateFlow<Set<String>>(emptySet())
  val favoriteActivityIds: StateFlow<Set<String>> = _favoriteActivityIds.asStateFlow()
  val favoriteAcaraIds: StateFlow<Set<String>> get() = favoriteActivityIds

  private val _pendingTabAfterLogin = MutableStateFlow<Int?>(null)
  private var pendingActionAfterLogin: (() -> Unit)? = null

  private val _isKreatorMode = MutableStateFlow(false)
  val isKreatorMode: StateFlow<Boolean> = _isKreatorMode.asStateFlow()

  private val _activeChatPartner = MutableStateFlow<String?>(null)
  val activeChatPartner: StateFlow<String?> = _activeChatPartner.asStateFlow()

  private val _activeChatActivityTitle = MutableStateFlow<String?>(null)
  val activeChatActivityTitle: StateFlow<String?> = _activeChatActivityTitle.asStateFlow()
  val activeChatAcaraTitle: StateFlow<String?> get() = activeChatActivityTitle

  private val _activityBeforeChat = MutableStateFlow<ActivityModel?>(null)
  val activityBeforeChat: StateFlow<ActivityModel?> = _activityBeforeChat.asStateFlow()

  private val _tabBeforeChat = MutableStateFlow<Int>(0)
  val tabBeforeChat: StateFlow<Int> = _tabBeforeChat.asStateFlow()

  private val _isKreatorVerified = MutableStateFlow(false)
  val isKreatorVerified: StateFlow<Boolean> = _isKreatorVerified.asStateFlow()

  fun requestKreatorVerification(context: Context) {
    val threadExists = _threads.value.any { it.senderName == "Tim Verifikasi Diajak" }
    if (!threadExists) {
      val newThread = MessageThread(
        id = "thread_verify_${UUID.randomUUID()}",
        senderName = "Tim Verifikasi Diajak",
        senderRole = "Tim Support Diajak",
        lastMessage = "Silakan upload foto KTP dan Buku Tabungan untuk verifikasi Kreator.",
        time = "Baru saja",
        unreadCount = 1,
        avatarColor = Color(0xFF2B6CB0),
        activityTitle = "Pendaftaran Kreator",
        role = "kreator"
      )
      _threads.value = listOf(newThread) + _threads.value
    }
    
    triggerNotification(
      context = context,
      title = "Verifikasi Kreator Diperlukan",
      body = "Silakan upload foto KTP dan Buku Tabungan di menu Pesan agar aktivitas Anda dapat diterbitkan.",
      type = "system",
      role = "kreator"
    )
    android.widget.Toast.makeText(context, "Verifikasi diperlukan! Cek pesan Anda.", android.widget.Toast.LENGTH_LONG).show()
  }

  // Real-time notification system data flow
  private val _notifications = MutableStateFlow<List<DiajakNotification>>(
    listOf(
      // Peserta notifications
      DiajakNotification(
        id = "n_peserta_1",
        title = "Pemesanan Berhasil! 🎟️",
        body = "Undangan untuk 'Barista Experience & Latte Art 101' telah dikonfirmasi oleh kreator.",
        type = "booking",
        timestamp = "2 jam lalu",
        isRead = false,
        relatedId = "act_1",
        role = "peserta"
      ),
      DiajakNotification(
        id = "n_peserta_2",
        title = "Promo Spesial Weekend! ☕",
        body = "Dapatkan diskon 20% untuk semua aktivitas kategori Kopi. Gunakan kode: DIAJAKKOPI20",
        type = "promo",
        timestamp = "4 jam lalu",
        isRead = false,
        role = "peserta"
      ),
      DiajakNotification(
        id = "n_peserta_3",
        title = "Aktivitas Populer Dekat Anda 🎒",
        body = "Glamping & Sunrise Di Ranca Upas sedang ramai dicari! Ikut gabung sekarang.",
        type = "activity",
        timestamp = "5 jam lalu",
        isRead = true,
        relatedId = "act_2",
        role = "peserta"
      ),
      DiajakNotification(
        id = "n_peserta_4",
        title = "Pesan baru dari Kak Rina (Kreator) 💬",
        body = "Halo! Jangan lupa bawa jaket tebal ya, suhu di Kintamani saat malam cukup dingin 😊",
        type = "message",
        timestamp = "Kemarin",
        isRead = true,
        role = "peserta"
      ),

      // Kreator notifications
      DiajakNotification(
        id = "n_kreator_1",
        title = "Order Masuk Baru! 📥",
        body = "Pengguna 'Anggi Saputro' memesan 2 undangan untuk 'Glamping Kintamani View Batur'. Segera konfirmasi!",
        type = "booking",
        timestamp = "10 menit lalu",
        isRead = false,
        role = "kreator"
      ),
      DiajakNotification(
        id = "n_kreator_2",
        title = "Pembayaran Berhasil! 💰",
        body = "Pembayaran sebesar Rp 750.000 dari booking #BK-9823 telah diteruskan ke saldo Anda.",
        type = "booking",
        timestamp = "1 jam lalu",
        isRead = false,
        role = "kreator"
      ),
      DiajakNotification(
        id = "n_kreator_3",
        title = "Pendaftaran Aktivitas Disetujui 🚀",
        body = "Aktivitas baru Anda 'Latte Art 101' telah disetujui oleh tim kurator kami dan kini tayang!",
        type = "activity",
        timestamp = "1 hari lalu",
        isRead = true,
        role = "kreator"
      ),
      DiajakNotification(
        id = "n_kreator_4",
        title = "Chat Baru dari Calon Peserta 💬",
        body = "Wayan: Halo kak, untuk anak di bawah 5 tahun apakah dikenakan biaya penuh?",
        type = "message",
        timestamp = "2 hari lalu",
        isRead = true,
        role = "kreator"
      )
    )
  )
  val notifications: StateFlow<List<DiajakNotification>> = _notifications.asStateFlow()

  fun triggerNotification(context: Context, title: String, body: String, type: String, relatedId: String? = null, role: String = "peserta") {
    val newNotif = DiajakNotification(
      id = "n_" + System.currentTimeMillis(),
      title = title,
      body = body,
      type = type,
      timestamp = "Baru saja",
      isRead = false,
      relatedId = relatedId,
      role = role,
      isArchived = false
    )
    _notifications.value = listOf(newNotif) + _notifications.value
  }

  fun markAllNotificationsAsRead() {
    _notifications.value = _notifications.value.map { it.copy(isRead = true) }
  }

  fun toggleNotificationRead(id: String) {
    _notifications.value = _notifications.value.map {
      if (it.id == id) it.copy(isRead = !it.isRead) else it
    }
  }

  fun toggleNotificationArchive(id: String) {
    _notifications.value = _notifications.value.map {
      if (it.id == id) it.copy(isArchived = !it.isArchived) else it
    }
  }

  fun deleteNotification(id: String) {
    _notifications.value = _notifications.value.filter { it.id != id }
  }

  fun clearAllNotifications() {
    _notifications.value = emptyList()
  }

  private var isSimulationStarted = false
  fun startSimulationIfNeeded(context: Context) {
    // Disabled simulation per user request
  }

  val allActivities = DiajakRepository.activitiesFlow
  val allAcara get() = allActivities
  val userBookings = DiajakRepository.bookingsFlow

  val filteredActivities = combine(
    allActivities,
    combine(_selectedCategory, _searchQuery, _priceFilter) { cat, query, price -> Triple(cat, query, price) },
    combine(_ratingFilter, _cityFilter) { rating, city -> Pair(rating, city) }
  ) { acts, filters1, filters2 ->
    val (cat, query, price) = filters1
    val (rating, city) = filters2
    acts.filter { act ->
      val catMatch = if (cat == "all") true else {
        val catItem = DiajakRepository.categories.find { it.id.equals(cat, ignoreCase = true) || it.name.equals(cat, ignoreCase = true) }
        val targetName = catItem?.name ?: cat
        act.category.contains(targetName, ignoreCase = true) ||
        act.category.contains(cat, ignoreCase = true) ||
        when (cat.lowercase()) {
          "kopi", "coffee" -> act.category.contains("Kopi", ignoreCase = true)
          "alam", "outdoor", "kemah", "pendakian", "petualangan" -> act.category.contains("Outdoor", ignoreCase = true) || act.category.contains("Alam", ignoreCase = true)
          "seni", "workshop", "kriya", "lokakarya" -> act.category.contains("Seni", ignoreCase = true) || act.category.contains("Workshop", ignoreCase = true)
          "olahraga", "sport", "lari", "kebugaran", "sepeda" -> act.category.contains("Olahraga", ignoreCase = true) || act.category.contains("Run", ignoreCase = true) || act.category.contains("Lari", ignoreCase = true)
          "kuliner", "culinary", "makanan" -> act.category.contains("Kuliner", ignoreCase = true) || act.category.contains("Trip", ignoreCase = true)
          "wellness", "yoga", "meditasi", "relaksasi" -> act.category.contains("Wellness", ignoreCase = true) || act.category.contains("Yoga", ignoreCase = true)
          "musik", "music", "konser" -> act.category.contains("Musik", ignoreCase = true) || act.category.contains("Karaoke", ignoreCase = true)
          "gaming", "esport" -> act.category.contains("Gaming", ignoreCase = true) || act.category.contains("E-Sports", ignoreCase = true)
          "hobi", "hobby", "komunitas" -> act.category.contains("Hobi", ignoreCase = true) || act.category.contains("Komunitas", ignoreCase = true)
          "seminar", "education", "gelarwicara", "pelatihan", "pendidikan" -> act.category.contains("Seminar", ignoreCase = true) || act.category.contains("Talkshow", ignoreCase = true)
          "pesta", "nightlife", "sosial", "social" -> act.category.contains("Social", ignoreCase = true) || act.category.contains("Party", ignoreCase = true)
          "nongkrong" -> act.category.contains("Kopi", ignoreCase = true) || act.category.contains("Nongkrong", ignoreCase = true)
          else -> act.category.contains(targetName, ignoreCase = true)
        }
      }
      val queryMatch = if (query.isBlank()) true else {
        val q = query.trim()
        act.title.contains(q, ignoreCase = true) ||
        act.locationName.contains(q, ignoreCase = true) ||
        act.address.contains(q, ignoreCase = true) ||
        act.overview.contains(q, ignoreCase = true) ||
        act.kreatorName.contains(q, ignoreCase = true) ||
        act.category.contains(q, ignoreCase = true)
      }
      val priceMatch = when (price) {
        "free" -> act.priceValue == 0
        "under50k" -> act.priceValue in 1..50000
        "under100k" -> act.priceValue in 1..100000
        "under150k" -> act.priceValue in 1..150000
        "under200k" -> act.priceValue in 1..200000
        "above200k" -> act.priceValue > 200000
        else -> true
      }
      val ratingMatch = when (rating) {
        "3_up" -> act.rating >= 3.0
        "3_5_up" -> act.rating >= 3.5
        "4_up" -> act.rating >= 4.0
        "4_5_up" -> act.rating >= 4.5
        else -> true
      }
      val cityMatch = if (city == "all") true else {
        act.locationName.contains(city, ignoreCase = true)
      }
      catMatch && queryMatch && priceMatch && ratingMatch && cityMatch
    }
  }.stateIn(viewModelScope, SharingStarted.Lazily, DiajakRepository.activitiesFlow.value)
  val filteredAcara get() = filteredActivities

  fun finishOnboarding() {
    _isOnboardingCompleted.value = true
  }

  fun setTab(tabIndex: Int) {
    if (!_isLoggedIn.value && (tabIndex == 1 || tabIndex == 2 || tabIndex == 3)) {
      _pendingTabAfterLogin.value = tabIndex
      _selectedTab.value = 4 // Redirect to Profile tab
      return
    }
    if (tabIndex == 0) {
      _selectedCategory.value = "all"
      _searchQuery.value = ""
      _priceFilter.value = "all"
      _ratingFilter.value = "all"
      _cityFilter.value = "all"
    }
    _selectedTab.value = tabIndex
  }

  fun resetFilters() {
    _selectedCategory.value = "all"
    _priceFilter.value = "all"
    _ratingFilter.value = "all"
    _cityFilter.value = "all"
  }

  fun resetDatabase(context: Context? = null) {
    DiajakRepository.resetDatabase()
    _favoriteActivityIds.value = emptySet()
    context?.let {
      android.widget.Toast.makeText(it, "Database berhasil di-reset ke data awal!", android.widget.Toast.LENGTH_SHORT).show()
    }
  }

  fun selectCategory(catId: String) {
    if (_selectedCategory.value != catId) {
      triggerCategoryLoading()
    }
    _selectedCategory.value = catId
    if (catId == "all") {
      _searchQuery.value = ""
    }
  }

  fun updateSearch(query: String) {
    _searchQuery.value = query
    if (query.isNotBlank()) {
      _selectedCategory.value = "all"
      _priceFilter.value = "all"
      _ratingFilter.value = "all"
      _cityFilter.value = "all"
    }
  }

  fun openActivityDetail(activity: ActivityModel) {
    _isPushTransition.value = true
    _selectedActivityStack.value = listOf(activity)
    _selectedActivity.value = activity
    _isDetailLoading.value = false
  }

  fun closeActivityDetail() {
    closeActivityDetail(clearAll = true)
  }

  fun closeActivityDetail(clearAll: Boolean = true) {
    _isPushTransition.value = false
    _selectedActivityStack.value = emptyList()
    _selectedActivity.value = null
  }

  fun openCreateModal() {
    _isCreateModalOpen.value = true
  }

  fun closeCreateModal() {
    _isCreateModalOpen.value = false
  }

  fun openBookingModal() {
    if (!_isLoggedIn.value) {
      val lastActivity = _selectedActivity.value
      val lastTab = _selectedTab.value
      pendingActionAfterLogin = {
        _selectedTab.value = lastTab
        _isPushTransition.value = true
        _selectedActivityStack.value = if (lastActivity != null) listOf(lastActivity) else emptyList()
        _selectedActivity.value = lastActivity
      }
      _selectedActivity.value = null // Close the Detail screen so they see the Profile tab!
      _selectedActivityStack.value = emptyList()
      _selectedTab.value = 4 // Redirect to Profile tab
      return
    }
    _isBookingModalOpen.value = true
  }

  fun closeBookingModal() {
    _isBookingModalOpen.value = false
  }

  fun toggleKreatorMode() {
    if (!_isLoggedIn.value) {
      pendingActionAfterLogin = {
        _isKreatorMode.value = !_isKreatorMode.value
        if (_isKreatorMode.value) {
          updateAllKreatorActivitiesActive()
        }
      }
      _selectedTab.value = 4 // Redirect to Profile tab
      return
    }
    _isKreatorMode.value = !_isKreatorMode.value
    if (_isKreatorMode.value) {
      updateAllKreatorActivitiesActive()
    }
  }

  fun createNewActivity(
    context: Context,
    title: String,
    category: String,
    locationName: String,
    address: String,
    schedule: String,
    priceFormatted: String,
    priceValue: Int,
    overview: String,
    maxPeserta: Int,
    imageResId: Int,
    detailsList: List<String> = emptyList(),
    mapX: Float = 0.5f,
    mapY: Float = 0.5f,
    isOngoing: Boolean = false,
    isRecurring: Boolean = false,
    recurringDays: String = "",
    id: String? = null,
    selectAfterCreate: Boolean = true
  ): ActivityModel {
    val finalDetails = if (detailsList.isEmpty()) {
      listOf(
        "Fasilitas lengkap disediakan kreator",
        "Pemandu & tim pendamping profesional",
        "Sesi networking komunitas",
        "Dokumentasi foto seru"
      )
    } else {
      detailsList
    }
    val newAct = ActivityModel(
      id = id ?: ("act_" + UUID.randomUUID().toString().take(6)),
      title = title,
      category = category,
      locationName = locationName,
      address = address,
      schedule = schedule,
      rating = 5.0,
      reviewsCount = 1,
      priceFormatted = priceFormatted,
      priceValue = priceValue,
      imageResId = imageResId,
      overview = overview,
      detailsList = finalDetails,
      galleryImages = listOf(imageResId),
      kreatorName = _profileName.value + " (Kreator)",
      kreatorVerified = _isKreatorVerified.value,
      maxPeserta = maxPeserta,
      currentPeserta = 1,
      mapX = mapX,
      mapY = mapY,
      isOngoing = isOngoing,
      isRecurring = isRecurring,
      recurringDays = recurringDays,
      isPublished = _isKreatorVerified.value
    )
    DiajakRepository.addActivity(newAct)
    _isCreateModalOpen.value = false
    if (selectAfterCreate) {
      _isPushTransition.value = true
      _selectedActivityStack.value = listOf(newAct)
      _selectedActivity.value = newAct
    }

    // Trigger local push notification for newly published activities
    triggerNotification(
      context = context,
      title = "Aktivitas Berhasil Diterbitkan! 🚀",
      body = "Aktivitas baru '$title' Anda telah online di Jelajah.",
      type = "activity",
      relatedId = newAct.id
    )
    
    if (!_isKreatorVerified.value) {
      requestKreatorVerification(context)
    }
    
    return newAct
  }

  fun updateActivityKreatorActive(activityId: String, newSchedule: String? = null) {
    viewModelScope.launch {
      val existing = DiajakRepository.activitiesFlow.value.find { it.id == activityId }
      if (existing != null) {
        val updated = existing.copy(
          kreatorLastActiveDaysAgo = 0,
          schedule = newSchedule ?: existing.schedule
        )
        DiajakRepository.addActivity(updated)
        if (_selectedActivity.value?.id == activityId) {
          _selectedActivity.value = updated
          _selectedActivityStack.value = _selectedActivityStack.value.map { if (it.id == activityId) updated else it }
        }
      }
    }
  }

  fun updateAcaraKreatorActive(activityId: String, newSchedule: String? = null) = updateActivityKreatorActive(activityId, newSchedule)

  fun updateAllKreatorActivitiesActive() {
    viewModelScope.launch {
      DiajakRepository.activitiesFlow.value.forEach { act ->
        if (act.kreatorName.contains("Komunitas") || act.kreatorName.contains(_profileName.value) || act.id.startsWith("act_")) {
          if (act.kreatorLastActiveDaysAgo > 0) {
            val updated = act.copy(kreatorLastActiveDaysAgo = 0)
            DiajakRepository.addActivity(updated)
            if (_selectedActivity.value?.id == act.id) {
              _selectedActivity.value = updated
              _selectedActivityStack.value = _selectedActivityStack.value.map { if (it.id == act.id) updated else it }
            }
          }
        }
      }
    }
  }

  fun bookActivity(
    context: Context,
    activity: ActivityModel,
    undanganCount: Int,
    closeDialog: Boolean = false,
    customName: String? = null,
    customEmail: String? = null,
    customPhone: String? = null,
    customBookingId: String? = null,
    customSelectedDate: String? = null
  ) {
    val totalPrice = if (activity.priceValue == 0) "Gratis" else "Rp ${String.format("%,d", activity.priceValue * undanganCount).replace(',', '.')}"
    val finalName = if (!customName.isNullOrBlank()) customName else _profileName.value
    val finalEmail = if (!customEmail.isNullOrBlank()) customEmail else _profileEmail.value
    val finalPhone = if (!customPhone.isNullOrBlank()) customPhone else _profilePhone.value
    val finalBookingId = if (!customBookingId.isNullOrBlank()) customBookingId else ("DJK-" + UUID.randomUUID().toString().substring(0, 8).uppercase())
    val finalSchedule = if (!customSelectedDate.isNullOrBlank()) customSelectedDate else activity.schedule

    val newBooking = BookingModel(
      id = finalBookingId,
      activityId = activity.id,
      activityTitle = activity.title,
      locationName = activity.address,
      schedule = finalSchedule,
      undanganCount = undanganCount,
      totalPriceFormatted = totalPrice,
      bookingTimestamp = "Hari ini",
      imageResId = activity.imageResId,
      status = "Terkonfirmasi",
      userName = finalName,
      userEmail = finalEmail,
      userPhone = finalPhone,
      selectedDate = customSelectedDate ?: ""
    )
    DiajakRepository.addBooking(newBooking)
    if (closeDialog) {
      _isBookingModalOpen.value = false
    }
    // Refresh selected activity participants
    val updated = DiajakRepository.activitiesFlow.value.find { it.id == activity.id }
    if (updated != null) {
      _selectedActivity.value = updated
      _selectedActivityStack.value = _selectedActivityStack.value.map { if (it.id == activity.id) updated else it }
    }

    // Trigger local push notification for booking success
    triggerNotification(
      context = context,
      title = "Pemesanan Terkonfirmasi! 🎉",
      body = "Pemesanan $undanganCount undangan untuk '${activity.title}' berhasil dilakukan.",
      type = "booking",
      relatedId = activity.id
    )
  }

  fun startChatWith(partnerName: String, activityTitle: String) {
    _activityBeforeChat.value = _selectedActivity.value
    _tabBeforeChat.value = _selectedTab.value

    if (!_isLoggedIn.value) {
      pendingActionAfterLogin = {
        _activeChatPartner.value = partnerName
        _activeChatActivityTitle.value = activityTitle
        setTab(3) // 3 is Messages tab
        _selectedActivity.value = null // Close the activity detail screen
        _selectedActivityStack.value = emptyList()
      }
      _selectedActivity.value = null // Close detail screen immediately so they see the Profile tab!
      _selectedActivityStack.value = emptyList()
      _selectedTab.value = 4 // Redirect to Profile tab
      return
    }
    _activeChatPartner.value = partnerName
    _activeChatActivityTitle.value = activityTitle
    setTab(3) // 3 is Messages tab
    _selectedActivity.value = null // Close the activity detail screen
    _selectedActivityStack.value = emptyList()
  }

  fun closeChatAndReturn() {
    val previousActivity = _activityBeforeChat.value
    val previousTab = _tabBeforeChat.value
    if (previousActivity != null) {
      _isPushTransition.value = true
      _selectedActivityStack.value = listOf(previousActivity)
      _selectedActivity.value = previousActivity
      _selectedTab.value = previousTab
      _activityBeforeChat.value = null
    }
  }

  fun clearActiveChatPartner() {
    _activeChatPartner.value = null
    _activeChatActivityTitle.value = null
  }

  @SuppressLint("MissingPermission")
  fun detectUserLocation(
    context: Context,
    onSuccess: (String) -> Unit = {},
    onFailure: (String) -> Unit = {}
  ) {
    _isLocating.value = true
    viewModelScope.launch {
      useFallbackLocation(onSuccess)
    }
  }

  private fun useFallbackLocation(onSuccess: (String) -> Unit) {
    val mockLat = -7.7956
    val mockLng = 110.3695
    _userCoordinates.value = Pair(mockLat, mockLng)
    val (nearestRegion, _) = getNearestRegion(mockLat, mockLng)
    _selectedLocation.value = nearestRegion
    _isLocating.value = false
    onSuccess(nearestRegion + " (Simulasi GPS)")
  }

  private fun getNearestRegion(lat: Double, lng: Double): Pair<String, Double> {
    val regions = listOf(
      Pair("Yogyakarta & Sekitarnya", Pair(-7.7956, 110.3695)),
      Pair("Bali, INA", Pair(-8.4095, 115.1889)),
      Pair("Bandung, INA", Pair(-6.9175, 107.6191)),
      Pair("Jakarta Selatan, INA", Pair(-6.2088, 106.8456))
    )

    var minDistance = Double.MAX_VALUE
    var nearest = "Yogyakarta & Sekitarnya"

    for (region in regions) {
      val rLat = region.second.first
      val rLng = region.second.second
      val distance = Math.sqrt(Math.pow(lat - rLat, 2.0) + Math.pow(lng - rLng, 2.0))
      if (distance < minDistance) {
        minDistance = distance
        nearest = region.first
      }
    }
    return Pair(nearest, minDistance)
  }

  fun openAuthDialog() {
    val lastActivity = _selectedActivity.value
    val lastTab = _selectedTab.value
    pendingActionAfterLogin = {
      _selectedTab.value = lastTab
      _isPushTransition.value = true
      _selectedActivityStack.value = if (lastActivity != null) listOf(lastActivity) else emptyList()
      _selectedActivity.value = lastActivity
    }
    _selectedActivity.value = null // Close the Detail screen so they see the Profile tab!
    _selectedActivityStack.value = emptyList()
    _selectedTab.value = 4 // Redirect to Profile tab
  }

  fun closeAuthDialog() {
    _isAuthDialogOpen.value = false
    _pendingTabAfterLogin.value = null
    pendingActionAfterLogin = null
  }

  fun loginOrRegisterUser() {
    _isLoggedIn.value = true
    _isAuthDialogOpen.value = false

    // Execute pending tab if any
    _pendingTabAfterLogin.value?.let { tabIndex ->
      _selectedTab.value = tabIndex
      _pendingTabAfterLogin.value = null
    }

    // Execute pending action if any
    pendingActionAfterLogin?.invoke()
    pendingActionAfterLogin = null
  }

  fun loginWithCustomProfile(name: String, email: String, phone: String, imageRes: Int) {
    viewModelScope.launch {
      val profile = kotlinx.coroutines.withContext(kotlinx.coroutines.Dispatchers.IO) {
        DiajakRepository.getUserProfile(email)
      }
      if (profile != null) {
        _profileName.value = profile.name
        _profileEmail.value = profile.email
        _profilePhone.value = profile.phone
        _profileImageRes.value = profile.imageRes
        _profileImageUri.value = profile.imageUri
        _profileUsername.value = profile.username
        _profileBio.value = profile.bio
        _profileGender.value = profile.gender
        _profileBirthDate.value = profile.birthDate
      } else {
        val finalUsername = if (email == "gisaput@diajak.com" || email == "gisaput@gmail.com") "gisaput" else email.substringBefore("@")
        val finalName = if (email == "gisaput@diajak.com" || email == "gisaput@gmail.com") "Anggi Saputro" else name
        val finalBio = if (email == "gisaput@diajak.com" || email == "gisaput@gmail.com") "Enjoy fun run" else name
        val finalBirthDate = if (email == "gisaput@diajak.com" || email == "gisaput@gmail.com") "12/08/1987" else "12/08/1987"
        val finalPhone = if (email == "gisaput@diajak.com" || email == "gisaput@gmail.com") "081234567890" else phone
        val initialEntity = com.example.data.database.UserProfileEntity(
          email = email,
          name = finalName,
          username = finalUsername,
          bio = finalBio,
          gender = "Pria",
          birthDate = finalBirthDate,
          phone = finalPhone,
          imageUri = null,
          imageRes = imageRes
        )
        kotlinx.coroutines.withContext(kotlinx.coroutines.Dispatchers.IO) {
          DiajakRepository.saveUserProfile(initialEntity)
        }
        _profileName.value = finalName
        _profileEmail.value = email
        _profilePhone.value = finalPhone
        _profileImageRes.value = imageRes
        _profileImageUri.value = null
        _profileUsername.value = finalUsername
        _profileBio.value = finalBio
        _profileGender.value = "Pria"
        _profileBirthDate.value = finalBirthDate
      }

      val dbFavs = kotlinx.coroutines.withContext(kotlinx.coroutines.Dispatchers.IO) {
        DiajakRepository.getFavoriteActivityIds(email)
      }
      
      // Merge with currently active in-memory favorites so they are not lost!
      val currentInMem = _favoriteActivityIds.value
      val mergedFavs = dbFavs.toSet() + currentInMem
      
      // Persist any new in-memory favorites to the DB for this logged-in email
      val newFavsToSave = currentInMem - dbFavs.toSet()
      if (newFavsToSave.isNotEmpty()) {
        kotlinx.coroutines.withContext(kotlinx.coroutines.Dispatchers.IO) {
          newFavsToSave.forEach { favId ->
            DiajakRepository.addFavorite(email, favId)
          }
        }
      }
      
      _favoriteActivityIds.value = mergedFavs

      loginOrRegisterUser()
    }
  }

  fun logoutUser() {
    _isLoggedIn.value = false
    _isKreatorMode.value = false
    _favoriteActivityIds.value = emptySet()
    _selectedTab.value = 0 // Go back to Beranda (Home)
  }

  fun toggleFavorite(activityId: String) {
    if (!_isLoggedIn.value) {
      val lastActivity = _selectedActivity.value
      val lastTab = _selectedTab.value
      pendingActionAfterLogin = {
        _selectedTab.value = lastTab
        _isPushTransition.value = true
        _selectedActivityStack.value = if (lastActivity != null) listOf(lastActivity) else emptyList()
        _selectedActivity.value = lastActivity
        toggleFavorite(activityId)
      }
      _selectedActivity.value = null // Close detail screen if open so they see Profile
      _selectedActivityStack.value = emptyList()
      _selectedTab.value = 4 // Redirect to Profile tab
      return
    }

    val current = _favoriteActivityIds.value
    val isFav = current.contains(activityId)
    val next = if (isFav) current - activityId else current + activityId
    _favoriteActivityIds.value = next

    val email = _profileEmail.value
    if (email.isNotBlank()) {
      viewModelScope.launch(kotlinx.coroutines.Dispatchers.IO) {
        if (isFav) {
          DiajakRepository.removeFavorite(email, activityId)
        } else {
          DiajakRepository.addFavorite(email, activityId)
        }
      }
    }
  }

  // --- KREATOR DASHBOARD STATES & ACTIONS (SHOPEE STYLE) ---
  private val _kreatorBalance = MutableStateFlow(1000000) // Rp 1.000.000
  val kreatorBalance: StateFlow<Int> = _kreatorBalance.asStateFlow()

  private val _kreatorVouchers = MutableStateFlow<List<VoucherModel>>(
    listOf(
      VoucherModel("v1", "KREATORHEMAT", 10, "Percentage", 50000),
      VoucherModel("v2", "KREATORUNTUNG", 20000, "Fixed", 100000)
    )
  )
  val kreatorVouchers: StateFlow<List<VoucherModel>> = _kreatorVouchers.asStateFlow()

  private val _kreatorTransactions = MutableStateFlow<List<TransactionModel>>(
    listOf(
      TransactionModel("t1", "Pendapatan Undangan: Artisan Bakery Tour", 320000, "Kredit", "Hari ini"),
      TransactionModel("t2", "Pencairan Saldo Bank BCA", 500000, "Debit", "Kemarin"),
      TransactionModel("t3", "Pendapatan Undangan: Barista Experience", 150000, "Kredit", "2 hari lalu")
    )
  )
  val kreatorTransactions: StateFlow<List<TransactionModel>> = _kreatorTransactions.asStateFlow()

  private val _pesertaPendaftaran = MutableStateFlow<List<PesertaPendaftaranModel>>(
    listOf(
      PesertaPendaftaranModel(
        id = "cb1",
        pesertaName = "Gisa Putra",
        pesertaAvatarRes = com.example.R.drawable.img_profile_cat_1783601304885,
        activityTitle = "Artisan Bakery & Pastry Hopping Tour",
        undanganCount = 2,
        totalPrice = 320000,
        date = "Hari ini, 10:30 WIB",
        status = "Menunggu Verifikasi"
      ),
      PesertaPendaftaranModel(
        id = "cb2",
        pesertaName = "Kak Rina",
        pesertaAvatarRes = com.example.R.drawable.img_sunset_yoga_1783584057064,
        activityTitle = "Barista Experience & Latte Art 101",
        undanganCount = 1,
        totalPrice = 150000,
        date = "Kemarin, 14:00 WIB",
        status = "Selesai"
      ),
      PesertaPendaftaranModel(
        id = "cb3",
        pesertaName = "Amelia",
        pesertaAvatarRes = com.example.R.drawable.img_culinary_baking_1783584043430,
        activityTitle = "Glamping & Sunrise Di Ranca Upas",
        undanganCount = 3,
        totalPrice = 750000,
        date = "3 hari lalu",
        status = "Terkonfirmasi"
      )
    )
  )
  val pesertaPendaftaran: StateFlow<List<PesertaPendaftaranModel>> = _pesertaPendaftaran.asStateFlow()

  fun confirmPesertaPendaftaran(id: String) {
    _pesertaPendaftaran.value = _pesertaPendaftaran.value.map {
      if (it.id == id) {
        it.copy(status = "Terkonfirmasi")
      } else it
    }
  }

  fun completePesertaPendaftaran(id: String) {
    _pesertaPendaftaran.value = _pesertaPendaftaran.value.map {
      if (it.id == id) {
        val updated = it.copy(status = "Selesai")
        // Add to balance
        _kreatorBalance.value += it.totalPrice
        // Add transaction log
        val newTx = TransactionModel(
          id = "tx_" + UUID.randomUUID().toString().take(6),
          title = "Pendapatan Undangan: ${it.activityTitle}",
          amount = it.totalPrice,
          type = "Kredit",
          date = "Baru saja"
        )
        _kreatorTransactions.value = listOf(newTx) + _kreatorTransactions.value
        updated
      } else it
    }
  }

  fun addKreatorVoucher(code: String, discount: Int, minPurchase: Int) {
    val newVoucher = VoucherModel(
      id = "v_" + UUID.randomUUID().toString().take(6),
      code = code.uppercase(),
      discountValue = discount,
      discountType = if (discount <= 100) "Percentage" else "Fixed",
      minPurchase = minPurchase
    )
    _kreatorVouchers.value = _kreatorVouchers.value + listOf(newVoucher)
  }

  fun withdrawKreatorBalance(amount: Int, bankName: String, accountNumber: String): Boolean {
    if (amount <= _kreatorBalance.value) {
      _kreatorBalance.value -= amount
      val newTx = TransactionModel(
        id = "tx_" + UUID.randomUUID().toString().take(6),
        title = "Pencairan Saldo Bank $bankName",
        amount = amount,
        type = "Debit",
        date = "Baru saja"
      )
      _kreatorTransactions.value = listOf(newTx) + _kreatorTransactions.value
      return true
    }
    return false
  }

  // State for message threads
  private val _threads = MutableStateFlow<List<MessageThread>>(
    listOf(
      // Peserta Threads
      MessageThread(
        id = "m1",
        senderName = "Kak Rina",
        senderRole = "Kreator",
        lastMessage = "Halo! Jangan lupa bawa jaket tebal ya, suhu di Kintamani saat malam cukup dingin 😊",
        time = "10:45",
        unreadCount = 2,
        avatarColor = Color(0xFF4A90E2),
        activityTitle = "Glamping Kintamani View Batur",
        role = "peserta"
      ),
      MessageThread(
        id = "m2",
        senderName = "Beli Wayan",
        senderRole = "Kreator",
        lastMessage = "Titik kumpul kita di pintu masuk Pura Uluwatu jam 16:30 WITA ya kak.",
        time = "Kemarin",
        unreadCount = 1,
        avatarColor = Color(0xFFE28A2B),
        activityTitle = "Tari Kecak Sunset Uluwatu",
        role = "peserta"
      ),
      MessageThread(
        id = "m3",
        senderName = "Komunitas Camping Bali",
        senderRole = "Kreator",
        lastMessage = "Dimas: Ada yang berangkat bareng dari Denpasar Selatan?",
        time = "Kemarin",
        unreadCount = 0,
        avatarColor = Color(0xFF50E3C2),
        activityTitle = "Camping Pantai Buyan",
        role = "peserta"
      ),
      MessageThread(
        id = "m4",
        senderName = "Admin Support",
        senderRole = "Customer Service 24/7",
        lastMessage = "Selamat datang! Jika ada kendala pemesanan undangan, silakan chat kami.",
        time = "3 Hari lalu",
        unreadCount = 0,
        avatarColor = Color(0xFF9013FE),
        activityTitle = "Bantuan & Layanan",
        role = "peserta"
      ),
      MessageThread(
        id = "m5",
        senderName = "Chef Andrea",
        senderRole = "Kreator",
        lastMessage = "Resep rahasia sambal matah sudah dikirim via email ya kak, terima kasih sudah hadir!",
        time = "1 Jul",
        unreadCount = 0,
        avatarColor = Color(0xFFF5A623),
        activityTitle = "Balinese Cooking Class",
        role = "peserta"
      ),

      // Kreator Threads
      MessageThread(
        id = "m_kreator_1",
        senderName = "Dimas",
        senderRole = "Peserta",
        lastMessage = "Halo kak, apakah boleh membawa hewan peliharaan (anjing ras kecil) di glamping?",
        time = "15:30",
        unreadCount = 1,
        avatarColor = Color(0xFFF5A623),
        activityTitle = "Glamping Kintamani View Batur",
        role = "kreator"
      ),
      MessageThread(
        id = "m_kreator_2",
        senderName = "Siti Rahma",
        senderRole = "Peserta",
        lastMessage = "Saya sudah melakukan pembayaran ya kak, mohon konfirmasi jadwal barunya.",
        time = "Kemarin",
        unreadCount = 0,
        avatarColor = Color(0xFF4A90E2),
        activityTitle = "Barista Experience & Latte Art",
        role = "kreator"
      ),
      MessageThread(
        id = "m_kreator_3",
        senderName = "Andi Wijaya",
        senderRole = "Peserta",
        lastMessage = "Kak, kalau cuaca hujan besok sore apakah pertunjukan tari kecak tetap berlangsung?",
        time = "2 Hari lalu",
        unreadCount = 0,
        avatarColor = Color(0xFF50E3C2),
        activityTitle = "Tari Kecak Sunset Uluwatu",
        role = "kreator"
      )
    )
  )
  val threads: StateFlow<List<MessageThread>> = _threads.asStateFlow()

  fun addThread(thread: MessageThread) {
    _threads.value = listOf(thread) + _threads.value
  }

  fun deleteThread(threadId: String) {
    _threads.value = _threads.value.filter { it.id != threadId }
  }

  fun toggleThreadRead(threadId: String) {
    _threads.value = _threads.value.map {
      if (it.id == threadId) {
        it.copy(unreadCount = if (it.unreadCount > 0) 0 else 1)
      } else {
        it
      }
    }
  }

  fun toggleThreadArchive(threadId: String) {
    _threads.value = _threads.value.map {
      if (it.id == threadId) {
        it.copy(isArchived = !it.isArchived)
      } else {
        it
      }
    }
  }
}

// --- KREATOR DASHBOARD MODEL CLASSES ---
data class VoucherModel(
  val id: String,
  val code: String,
  val discountValue: Int,
  val discountType: String, // "Percentage" or "Fixed"
  val minPurchase: Int,
  val isActive: Boolean = true
)

data class TransactionModel(
  val id: String,
  val title: String,
  val amount: Int,
  val type: String, // "Kredit" or "Debit"
  val date: String,
  val status: String = "Selesai"
)

data class PesertaPendaftaranModel(
  val id: String,
  val pesertaName: String,
  val pesertaAvatarRes: Int,
  val activityTitle: String,
  val undanganCount: Int,
  val totalPrice: Int,
  val date: String,
  val status: String // "Menunggu Verifikasi", "Terkonfirmasi", "Selesai"
) {
  val acaraTitle: String get() = activityTitle
}


