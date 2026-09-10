package com.example.ui.screens
import com.example.ui.theme.spacing
import com.example.ui.theme.diajakGlassButton
import com.example.ui.utils.getCategoryIconVector
import com.example.data.DiajakRepository
import androidx.compose.material3.MaterialTheme

import android.widget.Toast
import androidx.compose.animation.*
import androidx.compose.animation.core.*
import androidx.compose.foundation.*
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.layout.ExperimentalLayoutApi
import androidx.compose.foundation.layout.isImeVisible
import com.example.ui.components.DiajakFlowRow
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.*
import androidx.compose.material.icons.outlined.*
import androidx.compose.material.icons.outlined.*
import androidx.compose.material.icons.automirrored.outlined.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.BiasAlignment
import androidx.compose.ui.geometry.Offset
import androidx.compose.foundation.gestures.detectDragGestures
import androidx.compose.foundation.gestures.detectTapGestures
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.graphics.drawscope.translate
import kotlinx.coroutines.launch
import kotlinx.coroutines.flow.distinctUntilChanged
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.onFocusChanged
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.draw.scale
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Path
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.graphics.TransformOrigin
import androidx.compose.foundation.gestures.detectTransformGestures
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.withStyle
import androidx.compose.ui.text.SpanStyle

import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.zIndex
import dev.chrisbanes.haze.HazeState
import dev.chrisbanes.haze.hazeSource
import dev.chrisbanes.haze.hazeEffect
import com.example.ui.components.DiajakGlassHeader
import coil.compose.rememberAsyncImagePainter
import com.example.R
import com.example.model.ActivityModel
import com.example.ui.theme.DiajakOrange
import com.example.ui.theme.AppSpacing
import com.example.ui.theme.DiajakDesignSystem
import com.example.ui.theme.DiajakOrangeLight
import com.example.ui.theme.DiajakOrangeDark
import com.example.ui.theme.RatingGold
import com.example.ui.viewmodel.DiajakViewModel
import com.example.ui.viewmodel.VoucherModel
import com.example.ui.viewmodel.TransactionModel
import com.example.ui.viewmodel.PesertaPendaftaranModel

enum class SlideInType { NONE, KATEGORI, BENEFIT, WAKTU, LOKASI }



@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CreatorDashboardScreen(
  viewModel: DiajakViewModel,
  onToggleKreatorMode: () -> Unit
) {
  val context = LocalContext.current
  val screenWidth = androidx.compose.ui.platform.LocalConfiguration.current.screenWidthDp.dp
  val favoriteImageSize = (screenWidth - 60.dp) / 2
  val targetImageSize = favoriteImageSize / 2

  var currentSubScreen by remember { mutableStateOf("main") } // main, activities, create_activity, bookings, finance, withdraw, performance, vouchers, create_voucher

  // Observe viewModel states
  val profileName by viewModel.profileName.collectAsState()
  val profileUsername by viewModel.profileUsername.collectAsState()
  val profileImageRes by viewModel.profileImageRes.collectAsState()
  val profileImageUri by viewModel.profileImageUri.collectAsState()
  val profileBio by viewModel.profileBio.collectAsState()
  val profileGender by viewModel.profileGender.collectAsState()
  val profileBirthDate by viewModel.profileBirthDate.collectAsState()
  val profilePhone by viewModel.profilePhone.collectAsState()
  val profileEmail by viewModel.profileEmail.collectAsState()
  val isKreatorVerified by viewModel.isKreatorVerified.collectAsState()
  val allActivities by viewModel.allActivities.collectAsState()
  val pesertaPendaftaran by viewModel.pesertaPendaftaran.collectAsState()
  val kreatorBalance by viewModel.kreatorBalance.collectAsState()
  val kreatorVouchers by viewModel.kreatorVouchers.collectAsState()
  val kreatorTransactions by viewModel.kreatorTransactions.collectAsState()
  val notifications by viewModel.notifications.collectAsState()
  val threads by viewModel.threads.collectAsState()
  val isKtpUploaded by viewModel.isKtpUploaded.collectAsState()
  val isBukuTabunganUploaded by viewModel.isBukuTabunganUploaded.collectAsState()
  val showPesanBadge = notifications.any { !it.isRead && it.role == "kreator" } || threads.any { it.unreadCount > 0 && it.role == "kreator" }
  val showDokumenBadge = !isKtpUploaded || !isBukuTabunganUploaded

  // Kreator activities filter (all activities created/hosted by this kreator or simply all for interaction)
  val kreatorActivities = remember(allActivities) {
    allActivities.filter { it.kreatorName.contains("Komunitas") || it.kreatorName.contains("Anggi") || it.id.startsWith("act_") }
  }

  // Calculate stats
  val newPendaftaranCount = pesertaPendaftaran.count { it.status == "Menunggu Verifikasi" }
  val confirmedCount = pesertaPendaftaran.count { it.status == "Terkonfirmasi" }
  val completedCount = pesertaPendaftaran.count { it.status == "Selesai" }

  // Safe area structure
  Box(
    modifier = Modifier
      .fillMaxSize().background(androidx.compose.ui.graphics.Color(0xFFE5E7EB)) ){
    AnimatedContent(
      targetState = currentSubScreen,
      modifier = Modifier.fillMaxSize(),
      transitionSpec = {
        if (targetState != "main") {
          (slideInHorizontally(
            initialOffsetX = { width -> width },
            animationSpec = tween(350, easing = FastOutSlowInEasing)
          ) + fadeIn(animationSpec = tween(300))) togetherWith
              (slideOutHorizontally(
                targetOffsetX = { width -> -width / 3 },
                animationSpec = tween(350, easing = FastOutSlowInEasing)
              ) + fadeOut(animationSpec = tween(200)))
        } else {
          (slideInHorizontally(
            initialOffsetX = { width -> -width / 3 },
            animationSpec = tween(350, easing = FastOutSlowInEasing)
          ) + fadeIn(animationSpec = tween(300))) togetherWith
              (slideOutHorizontally(
                targetOffsetX = { width -> width },
                animationSpec = tween(350, easing = FastOutSlowInEasing)
              ) + fadeOut(animationSpec = tween(200)))
        }
      },
      label = "KreatorNavigation"
    ) { screen ->
      when (screen) {
        "main" -> {
          val newReviewsCount = 2 // Mocking new reviews count
          KreatorMainDashboard(
          kreatorName = profileName,
          kreatorUsername = profileUsername,
          profileImageRes = profileImageRes,
          profileImageUri = profileImageUri,
          kreatorBio = profileBio,
          kreatorGender = profileGender,
          kreatorBirthDate = profileBirthDate,
          kreatorPhone = profilePhone,
          kreatorEmail = profileEmail,
          balance = kreatorBalance,
          newPendaftaran = newPendaftaranCount,
          confirmed = confirmedCount,
          completed = completedCount,
          newReviews = newReviewsCount,
          hasUnreadMessages = showPesanBadge,
          showDokumenBadge = showDokumenBadge,
          onNavigate = { currentSubScreen = it },
          onToggleKreatorMode = onToggleKreatorMode
        )
        }

        "activities" -> KreatorActivitiesScreen(
          activities = kreatorActivities,
          onBack = { currentSubScreen = "main" },
          onNavigateToCreate = { currentSubScreen = "create_activity" },
          onUpdateActivityActive = { id, newSched -> viewModel.updateActivityKreatorActive(id, newSched) }
        )

        "create_activity" -> KreatorCreateActivityScreen(
          isKreatorVerified = isKreatorVerified,
          onRequestVerification = { viewModel.requestKreatorVerification(context) },
          onBack = { currentSubScreen = "activities" },
          onSave = { id, title, cat, loc, addr, sched, priceFormatted, priceValue, overview, quota, imageRes, benefits, mapX, mapY ->
            viewModel.createNewActivity(
              context, title, cat, loc, addr, sched, priceFormatted, priceValue, overview, quota, imageRes, detailsList = benefits, mapX = mapX, mapY = mapY, id = id, selectAfterCreate = false
            )
          },
          onSubmit = { createdActivity ->
            viewModel.openActivityDetail(createdActivity)
            Toast.makeText(context, "Aktivitas berhasil diterbitkan! 🚀", Toast.LENGTH_SHORT).show()
          }
        )

        "pendaftaran" -> KreatorPendaftaranScreen(
          pendaftaranList = pesertaPendaftaran,
          onBack = { currentSubScreen = "main" },
          onConfirm = { id ->
            viewModel.confirmPesertaPendaftaran(id)
            Toast.makeText(context, "Pendaftaran berhasil dikonfirmasi!", Toast.LENGTH_SHORT).show()
          },
          onComplete = { id ->
            viewModel.completePesertaPendaftaran(id)
            Toast.makeText(context, "Pendaftaran diselesaikan! Pendapatan masuk saldo.", Toast.LENGTH_LONG).show()
          }
        )

        "messages" -> {
          val activeChatPartner by viewModel.activeChatPartner.collectAsState()
          val activeChatActivityTitle by viewModel.activeChatActivityTitle.collectAsState()
          
          MessagesScreen(
            onExploreClick = { currentSubScreen = "main" },
            initialChatPartner = activeChatPartner,
            initialActivityTitle = activeChatActivityTitle,
            onChatOpened = { viewModel.clearActiveChatPartner() },
            onChatClosed = { viewModel.closeChatAndReturn() },
            isKreatorMode = true,
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

        "finance" -> KreatorFinanceScreen(
          balance = kreatorBalance,
          transactions = kreatorTransactions,
          onBack = { currentSubScreen = "main" },
          onNavigateToWithdraw = { currentSubScreen = "withdraw" }
        )

        "withdraw" -> KreatorWithdrawScreen(
          balance = kreatorBalance,
          onBack = { currentSubScreen = "finance" },
          onWithdraw = { amount, bank, account ->
            val success = viewModel.withdrawKreatorBalance(amount, bank, account)
            if (success) {
              Toast.makeText(context, "Pencairan Rp ${String.format("%,d", amount).replace(',', '.')} berhasil diproses!", Toast.LENGTH_LONG).show()
              currentSubScreen = "finance"
            } else {
              Toast.makeText(context, "Pencairan gagal. Saldo tidak mencukupi.", Toast.LENGTH_SHORT).show()
            }
          }
        )

        "performance" -> KreatorPerformanceScreen(
          activities = kreatorActivities,
          onBack = { currentSubScreen = "main" }
        )

        "vouchers" -> KreatorVouchersScreen(
          vouchers = kreatorVouchers,
          onBack = { currentSubScreen = "main" },
          onNavigateToCreate = { currentSubScreen = "create_voucher" }
        )

        "create_voucher" -> KreatorCreateVoucherScreen(
          onBack = { currentSubScreen = "vouchers" },
          onSubmit = { code, discount, minPurchase ->
            viewModel.addKreatorVoucher(code, discount, minPurchase)
            Toast.makeText(context, "Voucher berhasil ditambahkan!", Toast.LENGTH_SHORT).show()
            currentSubScreen = "vouchers"
          }
        )
        
        "document" -> DocumentUploadScreen(
          viewModel = viewModel,
          onBack = { currentSubScreen = "main" }
        )
      }
    }
  }
}

// ==========================================
// 1. MAIN KREATOR DASHBOARD ("TOKO SAYA")
// ==========================================
@Composable
fun KreatorMainDashboard(
  kreatorName: String,
  kreatorUsername: String,
  profileImageRes: Int,
  profileImageUri: String?,
  kreatorBio: String,
  kreatorGender: String,
  kreatorBirthDate: String,
  kreatorPhone: String,
  kreatorEmail: String,
  balance: Int,
  newPendaftaran: Int,
  confirmed: Int,
  completed: Int,
  newReviews: Int,
  hasUnreadMessages: Boolean,
  showDokumenBadge: Boolean,
  onNavigate: (String) -> Unit,
  onToggleKreatorMode: () -> Unit
) {
  val hasNewPendaftaran = newPendaftaran > 0
  val hasNewReviews = newReviews > 0
  val scrollState = rememberScrollState()
  val hazeState = remember { HazeState() }

  Box(modifier = Modifier.fillMaxSize().background(Color(0xFFE5E7EB))) {
    Column(
      modifier = Modifier
        .fillMaxSize()
        .hazeSource(state = hazeState)
        .verticalScroll(scrollState)
        .padding(bottom = AppSpacing.Medium) ) {
      // Top Cover & Header (Vibrant Sunset Gradient)
      Box(
        modifier = Modifier
          .fillMaxWidth()
          .background(
            brush = Brush.verticalGradient(
              colors = listOf(Color(0xFFFF5722), Color(0xFFFF7043))
            )
          )
          .padding(bottom = MaterialTheme.spacing.section)
      ) {
        Column(modifier = Modifier.padding(horizontal = 20.dp, vertical = MaterialTheme.spacing.medium)) {
          // App bar with standard 40.dp frosted glass back button and white title
          Row(
            modifier = Modifier.fillMaxWidth().statusBarsPadding(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
          ) {
            Row(verticalAlignment = Alignment.CenterVertically) {
              IconButton(
                onClick = onToggleKreatorMode,
                modifier = Modifier
                  .size(40.dp)
                  .diajakGlassButton(hazeState)
              ) {
                Icon(
                  imageVector = Icons.AutoMirrored.Outlined.ArrowBack,
                  contentDescription = "Kembali ke Peserta",
                  tint = MaterialTheme.colorScheme.onSurface,
                  modifier = Modifier.size(24.dp)
                )
              }
              Spacer(modifier = Modifier.width(MaterialTheme.spacing.medium))
              Text(
                text = "Halaman Kreator",
                style = androidx.compose.ui.text.TextStyle(
                  fontSize = 22.sp,
                  fontWeight = FontWeight.Bold,
                  color = Color.White
                )
              )
            }
          }
          
          Spacer(modifier = Modifier.height(MaterialTheme.spacing.medium))
          
          // Kreator profile details
          Row(
            modifier = Modifier.fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically
          ) {
          Box(
            modifier = Modifier
              .size(72.dp)
              .clip(CircleShape)
              .background(Color.White.copy(alpha = 0.3f))
              .border(2.5.dp, Color.White, CircleShape),
            contentAlignment = Alignment.Center
          ) {
            Image(
              painter = if (profileImageUri != null) {
                rememberAsyncImagePainter(model = profileImageUri)
              } else {
                rememberAsyncImagePainter(model = profileImageRes)
              },
              contentDescription = "Foto Profil",
              modifier = Modifier.fillMaxSize(),
              contentScale = ContentScale.Crop
            )
          }

          Spacer(modifier = Modifier.width(MaterialTheme.spacing.medium))

          Column(modifier = Modifier.weight(1f)) {
            Text(
              text = kreatorName,
              style = TextStyle(
                fontSize = 18.sp,
                fontWeight = FontWeight.Bold,
                color = Color.White
              ),
              maxLines = 1,
              overflow = TextOverflow.Ellipsis
            )
            Spacer(modifier = Modifier.height(MaterialTheme.spacing.extraSmall))
            Surface(
              color = Color.White.copy(alpha = 0.25f),
              shape = RoundedCornerShape(12.dp)
            ) {
              Row(
                modifier = Modifier.padding(horizontal = MaterialTheme.spacing.small, vertical = 3.dp),
                verticalAlignment = Alignment.CenterVertically
              ) {
                Icon(
                  imageVector = Icons.Outlined.CheckCircle,
                  contentDescription = "Verified",
                  tint = Color.White,
                  modifier = Modifier.size(13.dp)
                )
                Spacer(modifier = Modifier.width(MaterialTheme.spacing.extraSmall))
                Text(
                  text = "Kreator Terverifikasi",
                  style = TextStyle(
                    fontSize = 11.sp,
                    fontWeight = FontWeight.SemiBold,
                    color = Color.White
                  )
                )
              }
            }
          }

          // Balance Quick Badge
          Surface(
            onClick = { onNavigate("finance") },
            color = Color.White,
            shape = RoundedCornerShape(16.dp),
            shadowElevation = 0.dp
          ) {
            Column(
              modifier = Modifier.padding(horizontal = 14.dp, vertical = MaterialTheme.spacing.small),
              horizontalAlignment = Alignment.End
            ) {
              Text(
                text = "Saldo Kreator",
                style = TextStyle(fontSize = 10.sp, fontWeight = FontWeight.Medium, color = Color(0xFF64748B))
              )
              Text(
                text = "Rp " + String.format("%,d", balance).replace(',', '.'),
                style = TextStyle(fontSize = 14.sp, fontWeight = FontWeight.Bold, color = DiajakOrange)
              )
            }
          }
        }
      }
    }

    // Modern Elevated Metrics Card ("Status Pendaftaran Kreator")
    Surface(
      modifier = Modifier
        .fillMaxWidth()
        .padding(horizontal = 20.dp)
        .offset(y = (-16).dp),
      shape = RoundedCornerShape(20.dp),
      color = Color.White,
      shadowElevation = 0.dp,
      border = BorderStroke(0.dp, Color.Transparent)
    ) {
      Column(
        modifier = Modifier.padding(vertical = AppSpacing.Medium, horizontal = AppSpacing.ScreenMargin)
      ) {
        Row(
          modifier = Modifier.fillMaxWidth(),
          horizontalArrangement = Arrangement.SpaceAround
        ) {
          MetricItem(count = newPendaftaran, label = "Pendaftaran", onClick = { onNavigate("pendaftaran") })
          MetricItem(count = confirmed, label = "Konfirmasi", onClick = { onNavigate("pendaftaran") })
          MetricItem(count = completed, label = "Selesai", onClick = { onNavigate("pendaftaran") })
          MetricItem(count = newReviews, label = "Ulasan", onClick = { onNavigate("performance") })
        }
      }
    }

    // Services Grid Section Card
    Surface(
      modifier = Modifier
        .fillMaxWidth()
        .padding(horizontal = 20.dp, vertical = MaterialTheme.spacing.extraSmall),
      shape = RoundedCornerShape(20.dp),
      color = Color.White,
      shadowElevation = 0.dp,
      border = BorderStroke(0.dp, Color.Transparent)
    ) {
      Column(modifier = Modifier.padding(vertical = MaterialTheme.spacing.medium, horizontal = MaterialTheme.spacing.screenMargin)) {
        Row(
          modifier = Modifier.fillMaxWidth(),
          horizontalArrangement = Arrangement.SpaceBetween
        ) {
          ServiceGridItem(icon = Icons.Outlined.Festival, label = "Aktivitas", onClick = { onNavigate("activities") })
          ServiceGridItem(icon = Icons.AutoMirrored.Outlined.EventNote, label = "Pendaftaran", onClick = { onNavigate("pendaftaran") }, showBadge = hasNewPendaftaran)
          ServiceGridItem(icon = Icons.AutoMirrored.Outlined.Chat, label = "Pesan", onClick = { onNavigate("messages") }, showBadge = hasUnreadMessages)
        }
        Spacer(modifier = Modifier.height(MaterialTheme.spacing.medium))
        Row(
          modifier = Modifier.fillMaxWidth(),
          horizontalArrangement = Arrangement.SpaceBetween
        ) {
          ServiceGridItem(icon = Icons.Outlined.Wallet, label = "Saldo", onClick = { onNavigate("finance") })
          ServiceGridItem(icon = Icons.Outlined.StarBorder, label = "Ulasan", onClick = { onNavigate("performance") }, showBadge = hasNewReviews)
          ServiceGridItem(icon = Icons.Outlined.DocumentScanner, label = "Dokumen", onClick = { onNavigate("document") }, showBadge = showDokumenBadge)
        }
      }
    }
  }

    val isScrolled by remember { 
      derivedStateOf { scrollState.value > 0 } 
    }

    // 2. Glass Header Layer (DiajakGlassHeader - appears instantly on scroll, identical to HomeScreen)
    if (isScrolled) {
      DiajakGlassHeader(
        hazeState = hazeState,
        scrollState = scrollState,
        modifier = Modifier.align(Alignment.TopCenter).zIndex(10f)
      ) {
        Box(
          modifier = Modifier
            .fillMaxWidth()
            .statusBarsPadding()
            .height(DiajakDesignSystem.Header.HeaderContentHeight)
            .padding(horizontal = 20.dp)
        ) {
          // Frosted Glass Circular Back Button (Left)
          IconButton(
            onClick = onToggleKreatorMode,
            modifier = Modifier
              .size(40.dp)
              .align(Alignment.CenterStart)
              .diajakGlassButton(hazeState)
          ) {
            Icon(
              imageVector = Icons.AutoMirrored.Outlined.ArrowBack,
              contentDescription = "Kembali ke Peserta",
              tint = MaterialTheme.colorScheme.onSurface,
              modifier = Modifier.size(24.dp)
            )
          }

          // Title (STRICTLY CENTERED WITH HEADLINE TYPOGRAPHY)
          Text(
            text = "Halaman Kreator",
            style = DiajakDesignSystem.Typography.Headline,
            color = MaterialTheme.colorScheme.onSurface,
            textAlign = TextAlign.Center,
            modifier = Modifier
              .align(Alignment.Center)
              .fillMaxWidth()
          )
        }
      }
    }
  }
}

@Composable
fun KreatorProfileDetailRow(label: String, value: String) {
  Row(
    modifier = Modifier
      .fillMaxWidth()
      .padding(vertical = MaterialTheme.spacing.small),
    horizontalArrangement = Arrangement.SpaceBetween,
    verticalAlignment = Alignment.CenterVertically
  ) {
    Text(
      text = label,
      style = com.example.ui.theme.DiajakDesignSystem.Typography.Body,
      color = MaterialTheme.colorScheme.onSurface,
      textAlign = androidx.compose.ui.text.style.TextAlign.Center,
      maxLines = 2,
      lineHeight = 22.sp
    )
    Text(
      text = value,
      style = androidx.compose.ui.text.TextStyle(fontSize = 16.sp, fontWeight = androidx.compose.ui.text.font.FontWeight.Bold, fontFamily = com.example.ui.theme.AppFontFamily),
      color = MaterialTheme.colorScheme.onSurface,
      
      textAlign = TextAlign.End,
      modifier = Modifier.weight(1f).padding(start = 20.dp)
    )
  }
}

@Composable
fun MetricItem(count: Int, label: String, onClick: () -> Unit) {
  Column(
    horizontalAlignment = Alignment.CenterHorizontally,
    modifier = Modifier
      .clickable(
        interactionSource = remember { MutableInteractionSource() },
        indication = null,
        onClick = onClick
      )
      .padding(horizontal = MaterialTheme.spacing.small, vertical = MaterialTheme.spacing.extraSmall)
  ) {
    Text(
      text = count.toString(),
      style = TextStyle(
        fontSize = 20.sp,
        fontWeight = FontWeight.Bold,
        color = DiajakOrange
      )
    )
    Spacer(modifier = Modifier.height(MaterialTheme.spacing.extraSmall))
    Text(
      text = label,
      style = TextStyle(
        fontSize = 12.sp,
        fontWeight = FontWeight.Medium,
        color = MaterialTheme.colorScheme.onSurface
      ),
      textAlign = TextAlign.Center,
      maxLines = 1,
      overflow = TextOverflow.Ellipsis
    )
  }
}

@Composable
fun ServiceGridItem(
  icon: androidx.compose.ui.graphics.vector.ImageVector,
  label: String,
  onClick: () -> Unit,
  showBadge: Boolean = false
) {
  Column(
    horizontalAlignment = Alignment.CenterHorizontally,
    modifier = Modifier
      .width(96.dp)
      .clickable(
        interactionSource = remember { MutableInteractionSource() },
        indication = null,
        onClick = onClick
      )
      .padding(MaterialTheme.spacing.extraSmall)
  ) {
    Box(
      modifier = Modifier
        .size(54.dp)
        .clip(RoundedCornerShape(18.dp))
        .background(Color(0xFFFFF0EC))
        .border(0.dp, Color.Transparent, RoundedCornerShape(18.dp)),
      contentAlignment = Alignment.Center
    ) {
      Icon(
        imageVector = icon,
        contentDescription = label,
        modifier = Modifier.size(24.dp),
        tint = DiajakOrange
      )
      if (showBadge) {
        Box(
          modifier = Modifier
            .align(Alignment.TopEnd)
            .offset(x = 2.dp, y = (-2).dp)
            .size(11.dp)
            .background(DiajakOrange, CircleShape)
            .border(2.dp, Color.White, CircleShape)
        )
      }
    }
    Spacer(modifier = Modifier.height(MaterialTheme.spacing.small))
    Text(
      text = label,
      style = TextStyle(
        fontSize = 12.sp,
        fontWeight = FontWeight.Bold,
        color = Color(0xFF0F172A)
      ),
      textAlign = TextAlign.Center,
      maxLines = 2,
      lineHeight = 16.sp
    )
  }
}

// ==========================================
// 2. KELOLA AKTIVITAS (MANAGE ACTIVITIES)
// ==========================================
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun KreatorActivitiesScreen(
  activities: List<ActivityModel>,
  onBack: () -> Unit,
  onNavigateToCreate: () -> Unit,
  onUpdateActivityActive: (String, String?) -> Unit
) {
  val context = LocalContext.current
  val screenWidth = androidx.compose.ui.platform.LocalConfiguration.current.screenWidthDp.dp
  val favoriteImageSize = (screenWidth - 60.dp) / 2
  val targetImageSize = favoriteImageSize / 2
  val hazeState = remember { HazeState() }

  var scheduleToEditId by remember { mutableStateOf<String?>(null) }
  var scheduleTextToEdit by remember { mutableStateOf("") }

  Box(
    modifier = Modifier
      .fillMaxSize()
      .background(Color(0xFFE5E7EB))
  ) {
    // 1. Content Layer (hazeSource)
    if (activities.isEmpty()) {
      Box(
        modifier = Modifier
          .fillMaxSize()
          .hazeSource(state = hazeState)
          .padding(bottom = 80.dp),
        contentAlignment = Alignment.Center
      ) {
        Column(
          horizontalAlignment = Alignment.CenterHorizontally,
          modifier = Modifier.padding(horizontal = MaterialTheme.spacing.extraLarge)
        ) {
          Spacer(modifier = Modifier.statusBarsPadding().height(DiajakDesignSystem.Header.HeaderTotalTopPadding))
          Icon(
            imageVector = Icons.Outlined.Festival,
            contentDescription = null,
            modifier = Modifier.size(72.dp),
            tint = MaterialTheme.colorScheme.onSurface
          )
          Spacer(modifier = Modifier.height(MaterialTheme.spacing.medium))
          Text(
            text = "Belum Ada Aktivitas",
            style = com.example.ui.theme.DiajakDesignSystem.Typography.Body,
            color = MaterialTheme.colorScheme.onSurface
          )
          Spacer(modifier = Modifier.height(MaterialTheme.spacing.small))
          Text(
            text = "Aktivitas yang kamu buat akan muncul di sini.",
            style = com.example.ui.theme.DiajakDesignSystem.Typography.Body,
            color = MaterialTheme.colorScheme.onSurface,
            textAlign = androidx.compose.ui.text.style.TextAlign.Center
          )
        }
      }
    } else {
      LazyColumn(
        modifier = Modifier
          .fillMaxSize()
          .hazeSource(state = hazeState),
        contentPadding = PaddingValues(start = 20.dp, end = 20.dp, bottom = 100.dp)
      ) {
        item {
          Spacer(modifier = Modifier.statusBarsPadding().height(DiajakDesignSystem.Header.HeaderTotalTopPadding))
        }
        items(activities.size, key = { activities[it].id }) { index ->
          val activity = activities[index]
          Surface(
            modifier = Modifier
              .fillMaxWidth()
              .padding(vertical = MaterialTheme.spacing.small),
            shape = RoundedCornerShape(16.dp),
            color = Color.White,
            shadowElevation = 0.dp,
            border = BorderStroke(0.dp, Color.Transparent)
          ) {
            Column(
              modifier = Modifier
                .fillMaxWidth()
                .padding(20.dp)
            ) {
              // Header Row (Status & ID/Slot)
              Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
              ) {
                Row(verticalAlignment = Alignment.CenterVertically) {
                  if (activity.kreatorLastActiveDaysAgo > 7) {
                    Icon(Icons.Outlined.Cancel, contentDescription = "Inaktif", tint = Color(0xFFC53030), modifier = Modifier.size(20.dp))
                    Spacer(modifier = Modifier.width(MaterialTheme.spacing.extraSmall))
                    Text(
                      text = "INAKTIF (> 1 MINGGU)",
                      color = Color(0xFFC53030),
                      style = androidx.compose.ui.text.TextStyle(fontSize = 16.sp, fontWeight = androidx.compose.ui.text.font.FontWeight.Bold, fontFamily = com.example.ui.theme.AppFontFamily)
                    )
                  } else {
                    Icon(Icons.Outlined.CheckCircle, contentDescription = "Aktif", tint = Color(0xFF2B6CB0), modifier = Modifier.size(20.dp))
                    Spacer(modifier = Modifier.width(MaterialTheme.spacing.extraSmall))
                    Text(
                      text = "AKTIF",
                      color = Color(0xFF2B6CB0),
                      style = androidx.compose.ui.text.TextStyle(fontSize = 16.sp, fontWeight = androidx.compose.ui.text.font.FontWeight.Bold, fontFamily = com.example.ui.theme.AppFontFamily)
                    )
                  }
                }
                Text(
                  text = "ID: #${activity.id.take(8)}",
                  style = com.example.ui.theme.DiajakDesignSystem.Typography.Body,
                  color = MaterialTheme.colorScheme.onSurface
                )
              }

              Spacer(modifier = Modifier.height(MaterialTheme.spacing.medium))

              // Image & Info Row
              Row(verticalAlignment = Alignment.Top) {
                Box(
                  modifier = Modifier
                    .size(targetImageSize)
                    .clip(RoundedCornerShape(12.dp))
                ) {
                  Image(
                    painter = painterResource(id = activity.imageResId),
                    contentDescription = activity.title,
                    contentScale = ContentScale.Crop,
                    modifier = Modifier.fillMaxSize()
                  )
                }
                Spacer(modifier = Modifier.width(36.dp))
                Column(
                  modifier = Modifier.weight(1f).height(targetImageSize),
                  verticalArrangement = Arrangement.SpaceBetween
                ) {
                  Text(
                    text = activity.title,
                    style = com.example.ui.theme.DiajakDesignSystem.Typography.BodyBold,
                    color = MaterialTheme.colorScheme.onSurface,
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis
                  )
                  Row(verticalAlignment = Alignment.Top) {
                    Icon(Icons.Outlined.Place, contentDescription = null, tint = MaterialTheme.colorScheme.onSurface, modifier = Modifier.padding(top = 2.dp).size(18.dp))
                    Spacer(modifier = Modifier.width(MaterialTheme.spacing.extraSmall))
                    Text(
                      text = activity.address,
                      style = com.example.ui.theme.DiajakDesignSystem.Typography.Body,
                      color = MaterialTheme.colorScheme.onSurface,
                      maxLines = 2,
                      overflow = TextOverflow.Ellipsis
                    )
                  }
                  Row(verticalAlignment = Alignment.CenterVertically) {
                    Icon(Icons.Outlined.AccessTime, contentDescription = null, tint = MaterialTheme.colorScheme.onSurface, modifier = Modifier.size(18.dp))
                    Spacer(modifier = Modifier.width(MaterialTheme.spacing.extraSmall))
                    Text(
                      text = activity.schedule,
                      style = com.example.ui.theme.DiajakDesignSystem.Typography.Body,
                      color = MaterialTheme.colorScheme.onSurface,
                      maxLines = 1,
                      overflow = TextOverflow.Ellipsis
                    )
                  }
                }
              }

              Spacer(modifier = Modifier.height(MaterialTheme.spacing.medium))

              // Footer Row
              Row(
                modifier = Modifier
                  .fillMaxWidth()
                  .padding(bottom = MaterialTheme.spacing.small),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
              ) {
                Row(verticalAlignment = Alignment.CenterVertically) {
                  Column(horizontalAlignment = Alignment.Start) {
                    Text("Slot", style = com.example.ui.theme.DiajakDesignSystem.Typography.Body)
                    Text("Biaya", style = com.example.ui.theme.DiajakDesignSystem.Typography.Body, color = MaterialTheme.colorScheme.onSurface)
                  }
                  Spacer(modifier = Modifier.width(MaterialTheme.spacing.extraSmall))
                  Column(horizontalAlignment = Alignment.Start) {
                    Text(": ${activity.currentPeserta}/${activity.maxPeserta}", style = com.example.ui.theme.DiajakDesignSystem.Typography.Body)
                    Row {
                      Text(": ", style = com.example.ui.theme.DiajakDesignSystem.Typography.Body, color = MaterialTheme.colorScheme.onSurface)
                      Text(activity.priceFormatted, style = com.example.ui.theme.DiajakDesignSystem.Typography.Body, color = DiajakOrange)
                    }
                  }
                }
                androidx.compose.material3.Surface(
                  shape = RoundedCornerShape(16.dp),
                  color = DiajakOrange,
                  modifier = Modifier.clickable {
                    scheduleToEditId = activity.id
                    scheduleTextToEdit = activity.schedule
                  }
                ) {
                  Row(
                    modifier = Modifier.padding(horizontal = 20.dp, vertical = MaterialTheme.spacing.small),
                    verticalAlignment = Alignment.CenterVertically
                  ) {
                    Icon(
                      imageVector = Icons.Outlined.Edit,
                      contentDescription = "Edit",
                      tint = Color.White,
                      modifier = Modifier.size(20.dp)
                    )
                    Spacer(modifier = Modifier.width(MaterialTheme.spacing.small))
                    Text(
                      text = "Jadwal",
                      color = Color.White,
                      style = androidx.compose.ui.text.TextStyle(fontSize = 16.sp, fontWeight = androidx.compose.ui.text.font.FontWeight.Bold, fontFamily = com.example.ui.theme.AppFontFamily)
                    )
                  }
                }
              }
            }
          }
        }
      }
    }

    // 2. Glass Header Layer (DiajakGlassHeader)
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
          .padding(horizontal = 20.dp)
      ) {
        // Frosted Glass Circular Back Button (Left)
        IconButton(
          onClick = {
            if (scheduleToEditId != null) {
              scheduleToEditId = null
            } else {
              onBack()
            }
          },
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

        // Title (Strictly Centered with Headline Typography)
        Text(
          text = if (scheduleToEditId != null) "Pilih Jadwal" else "Kelola Aktivitas",
          style = DiajakDesignSystem.Typography.Headline,
          color = MaterialTheme.colorScheme.onSurface,
          textAlign = TextAlign.Center,
          modifier = Modifier
            .align(Alignment.Center)
            .fillMaxWidth()
            .padding(horizontal = 48.dp),
          maxLines = 1,
          overflow = TextOverflow.Ellipsis
        )
      }
    }

    // 3. Sticky Bottom Button (Matches premium floating card style)
    Box(
      modifier = Modifier
        .align(Alignment.BottomCenter)
        .fillMaxWidth()
        .background(Color.Transparent)
        .navigationBarsPadding()
        .padding(horizontal = AppSpacing.ScreenMargin, vertical = AppSpacing.Medium)
        .zIndex(5f)
    ) {
      Button(
        onClick = onNavigateToCreate,
        shape = RoundedCornerShape(16.dp),
        colors = ButtonDefaults.buttonColors(containerColor = DiajakOrange),
        elevation = ButtonDefaults.buttonElevation(defaultElevation = 0.dp),
        modifier = Modifier
          .fillMaxWidth()
          .height(52.dp)
      ) {
        Text(
          text = "Tambah Aktivitas",
          style = androidx.compose.ui.text.TextStyle(fontSize = 16.sp, fontWeight = androidx.compose.ui.text.font.FontWeight.Bold, fontFamily = com.example.ui.theme.AppFontFamily),
          color = Color.White
        )
      }
    }

    // 4. Slide In Edit Schedule Overlay
    if (scheduleToEditId != null) {
      Box(
        modifier = Modifier
          .fillMaxSize()
          .background(androidx.compose.ui.graphics.Color(0xFFE5E7EB))
          .zIndex(20f)
      ) {
        SlideInWaktuScreen(
          schedule = scheduleTextToEdit,
          onScheduleChange = { newSched ->
            onUpdateActivityActive(scheduleToEditId!!, newSched)
            Toast.makeText(context, "Jadwal & tanggal aktivitas diperbarui! 🚀", Toast.LENGTH_SHORT).show()
            scheduleToEditId = null
          },
          onBack = { scheduleToEditId = null },
          showTopBar = false
        )
      }
    }
  }
}

// ========================================// 3. TAMBAH/BUAT AKTIVITAS BARU (FULL SCREEN)
// ==========================================
data class GrabLandmark(
  val name: String,
  val address: String,
  val mapPos: Offset,
  val emoji: String
)

@OptIn(ExperimentalMaterial3Api::class, ExperimentalLayoutApi::class)
@Composable
fun KreatorCreateActivityScreen(
  isKreatorVerified: Boolean,
  onRequestVerification: () -> Unit,
  onBack: () -> Unit,
  onSave: (id: String?, title: String, category: String, location: String, address: String, schedule: String, priceFormatted: String, priceValue: Int, overview: String, quota: Int, imageRes: Int, benefits: List<String>, mapX: Float, mapY: Float) -> ActivityModel,
  onSubmit: (ActivityModel) -> Unit
) {
  val screenWidth = androidx.compose.ui.platform.LocalConfiguration.current.screenWidthDp.dp
  val favoriteImageSize = (screenWidth - 60.dp) / 2
  val targetImageSize = favoriteImageSize / 2

  var isSaved by remember { mutableStateOf(false) }
  var savedActivity by remember { mutableStateOf<ActivityModel?>(null) }
  val focusManager = androidx.compose.ui.platform.LocalFocusManager.current
  var title by remember { mutableStateOf("") }
  var selectedCategory by remember { mutableStateOf("") }
  var locationName by remember { mutableStateOf("Yogyakarta & Sekitarnya") }
  var address by remember { mutableStateOf("") }
  var pinX by remember { mutableStateOf(0.5f) }
  var pinY by remember { mutableStateOf(0.5f) }
  var schedule by remember { mutableStateOf("") }
  var priceString by remember { mutableStateOf("") }
  var isFree by remember { mutableStateOf(false) }
  var quotaString by remember { mutableStateOf("") }
  var overview by remember { mutableStateOf("") }
  val standardBenefits = remember {
    listOf(
      "Free Welcome Drink / Snack",
      "E-Certificate Resmi",
      "Peralatan Lengkap Disediakan",
      "Dokumentasi Foto & Video HD",
      "Pemandu Berlisensi",
      "Transportasi PP",
      "Merchandise Eksklusif",
      "Koneksi Wi-Fi Cepat"
    )
  }
  val selectedBenefits = remember { mutableStateListOf<String>() }
  var customBenefitInput by remember { mutableStateOf("") }
  var isLocating by remember { mutableStateOf(false) }
  var showMapDialog by remember { mutableStateOf(false) }
  var activeSlideInScreen by remember { mutableStateOf<SlideInType>(SlideInType.NONE) }
  val scrollState = rememberScrollState()
  val coroutineScope = rememberCoroutineScope()
  var isTitleFocused by remember { mutableStateOf(false) }
  var isOverviewFocused by remember { mutableStateOf(false) }
  var isPriceFocused by remember { mutableStateOf(false) }
  var isQuotaFocused by remember { mutableStateOf(false) }

  LaunchedEffect(activeSlideInScreen) {
    focusManager.clearFocus()
  }

  // Available sample images that user can tap to select
  val sampleImages = listOf(
    R.drawable.diajak_activity_coffee_1783253596378,
    R.drawable.diajak_activity_glamping_1783253582807,
    R.drawable.diajak_banner_workshop_1783253562818,
    R.drawable.img_sport_running_1783584004743,
    R.drawable.img_culinary_baking_1783584043430,
    R.drawable.img_sunset_yoga_1783584057064,
    R.drawable.diajak_banner_outdoor_1783253550783
  )
  var selectedImageRes by remember { mutableStateOf(sampleImages[0]) }
  var customImageUri by remember { mutableStateOf<android.net.Uri?>(null) }
  val galleryLauncher = androidx.activity.compose.rememberLauncherForActivityResult(
    contract = androidx.activity.result.contract.ActivityResultContracts.GetContent()
  ) { uri ->
    if (uri != null) {
      customImageUri = uri
    }
  }



  val categories = DiajakRepository.categoryNames

  val context = LocalContext.current

  Box(
    modifier = Modifier
      .fillMaxSize().background(androidx.compose.ui.graphics.Color(0xFFE5E7EB))
      .imePadding()
      .pointerInput(Unit) {
        detectTapGestures(
          onTap = {
            focusManager.clearFocus()
          }
        )
      }
  ) boxScope@{
    val hazeState = remember { HazeState() }

    // 1. Content Layer
    Column(
      modifier = Modifier
        .fillMaxSize()
        .hazeSource(state = hazeState)
        .verticalScroll(scrollState)
        .background(Color.Transparent)
        .padding(horizontal = 20.dp),
      verticalArrangement = Arrangement.spacedBy(AppSpacing.Medium)
    ) {
      Spacer(modifier = Modifier.statusBarsPadding().height(DiajakDesignSystem.Header.HeaderTotalTopPadding))

      com.example.ui.theme.DiajakCard(modifier = Modifier.fillMaxWidth()) {
        Column(
          modifier = Modifier.fillMaxWidth().padding(vertical = AppSpacing.Medium),
          verticalArrangement = Arrangement.spacedBy(AppSpacing.Medium)
        ) {
        // SECTION 1: MEDIA & COVER PHOTO (FLATTENED)
        Column(
          modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = DiajakDesignSystem.Dimens.ScreenPaddingHorizontal)
        ) {
          Row(verticalAlignment = Alignment.CenterVertically) {
            Text(
              text = buildAnnotatedString {
                append("Foto")
                withStyle(SpanStyle(color = Color.Red)) {
                  append(" *")
                }
              },
              style = com.example.ui.theme.DiajakDesignSystem.Typography.Body,
              color = MaterialTheme.colorScheme.onSurface
            )
            Spacer(modifier = Modifier.width(MaterialTheme.spacing.extraSmall))
            Text(text = "(Pilih Cover)", style = androidx.compose.ui.text.TextStyle(fontSize = 16.sp, fontWeight = androidx.compose.ui.text.font.FontWeight.Bold, fontFamily = com.example.ui.theme.AppFontFamily), color = Color(0xFF262626))
          }
          Spacer(modifier = Modifier.height(MaterialTheme.spacing.medium))

          Row(
            modifier = Modifier
              .fillMaxWidth()
              .horizontalScroll(rememberScrollState()),
            horizontalArrangement = Arrangement.spacedBy(MaterialTheme.spacing.small) ) {
                          // Dotted / Styled Add Box
            Box(
              modifier = Modifier
                .size(targetImageSize)
                .clip(RoundedCornerShape(12.dp))
                .background(Color(0xFFFFF5F5))
                .clickable { galleryLauncher.launch("image/*") }
                .border(BorderStroke(1.dp, DiajakOrange), RoundedCornerShape(16.dp)),
              contentAlignment = Alignment.Center
            ) {
              if (customImageUri != null) {
                 Image(
                   painter = coil.compose.rememberAsyncImagePainter(customImageUri),
                   contentDescription = null,
                   contentScale = ContentScale.Crop,
                   modifier = Modifier.fillMaxSize()
                 )
                 Box(
                   modifier = Modifier
                     .size(20.dp)
                     .align(Alignment.TopEnd)
                     .background(DiajakOrange, RoundedCornerShape(bottomStart = 16.dp)),
                   contentAlignment = Alignment.Center
                 ) {
                   Icon(Icons.Outlined.Check, null, tint = Color.White, modifier = Modifier.size(12.dp))
                 }
              } else {
                 Column(horizontalAlignment = Alignment.CenterHorizontally) {
                   Icon(Icons.Outlined.AddAPhoto, contentDescription = null, tint = DiajakOrange, modifier = Modifier.size(24.dp))
                   Spacer(modifier = Modifier.height(MaterialTheme.spacing.medium))
                   Text("Tambah", style = com.example.ui.theme.DiajakDesignSystem.Typography.Body, color = DiajakOrange, fontWeight = FontWeight.Medium)
                 }
              }
            }

            // Predefined Selection list
            sampleImages.forEach { imgId ->
              val isSelected = selectedImageRes == imgId && customImageUri == null
              Box(
                modifier = Modifier
                  .size(targetImageSize)
                  .clip(RoundedCornerShape(12.dp))
                  .clickable {
                    focusManager.clearFocus()
                    selectedImageRes = imgId
                    customImageUri = null
                  }
                  .border(
                    BorderStroke(if (isSelected) 3.dp else 1.dp, if (isSelected) DiajakOrange else MaterialTheme.colorScheme.outlineVariant),
                    RoundedCornerShape(16.dp) )){
                Image(
                  painter = painterResource(id = imgId),
                  contentDescription = null,
                  contentScale = ContentScale.Crop,
                  modifier = Modifier.fillMaxSize()
                )
                if (isSelected) {
                  Box(
                    modifier = Modifier
                      .fillMaxSize()
                      .background(androidx.compose.material3.MaterialTheme.colorScheme.onSurface.copy(alpha = 0.15f))
                  )
                  Box(
                    modifier = Modifier
                      .size(20.dp)
                      .align(Alignment.TopEnd)
                      .background(DiajakOrange, RoundedCornerShape(bottomStart = 16.dp)),
                    contentAlignment = Alignment.Center
                  ) {
                    Icon(Icons.Outlined.Check, null, tint = Color.White, modifier = Modifier.size(12.dp))
                  }
                }
              }
            }
          }
        }


        // SECTION 2: PRODUCT DETAIL (FLATTENED)
        Column(
          modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = DiajakDesignSystem.Dimens.ScreenPaddingHorizontal),
          verticalArrangement = Arrangement.spacedBy(AppSpacing.Small)
        ) {
          Box(modifier = Modifier.fillMaxWidth()) {
            OutlinedTextField(
              value = title,
              onValueChange = { if (it.length <= 255) title = it },
              placeholder = { Text("Buat judul menarik untuk aktivitas kamu", style = com.example.ui.theme.DiajakDesignSystem.Typography.Body) },
              modifier = Modifier
                .fillMaxWidth()
                .padding(top = 10.dp)
                .onFocusChanged { isTitleFocused = it.isFocused },
              maxLines = 2,
              shape = RoundedCornerShape(16.dp),
              colors = OutlinedTextFieldDefaults.colors(
                focusedTextColor = MaterialTheme.colorScheme.onSurface,
                unfocusedTextColor = MaterialTheme.colorScheme.onSurface,
                focusedBorderColor = DiajakOrange,
                unfocusedBorderColor = Color.Transparent,
                cursorColor = DiajakOrange,
                focusedPlaceholderColor = MaterialTheme.colorScheme.onSurface,
                unfocusedPlaceholderColor = MaterialTheme.colorScheme.onSurface
              )
            )
            // Always-Floating Label on Border
            Box(
              modifier = Modifier
                .offset(x = 12.dp, y = 1.dp)
                .background(androidx.compose.ui.graphics.Color(0xFFE5E7EB))
                .padding(horizontal = 4.dp)
            ) {
              Text(
                text = buildAnnotatedString {
                  append("Judul")
                  withStyle(SpanStyle(color = Color.Red)) {
                    append(" *")
                  }
                },
                style = com.example.ui.theme.DiajakDesignSystem.Typography.Body,
                color = if (isTitleFocused) DiajakOrange else MaterialTheme.colorScheme.onSurface
              )
            }
          }
          
          Box(modifier = Modifier.fillMaxWidth()) {
            OutlinedTextField(
              value = overview,
              onValueChange = { if (it.length <= 3000) overview = it },
              placeholder = { Text("Jelaskan aktivitas kamu secara detail supaya peserta tertarik untuk mengikutinya", style = com.example.ui.theme.DiajakDesignSystem.Typography.Body) },
              modifier = Modifier
                .fillMaxWidth()
                .padding(top = 10.dp)
                .onFocusChanged { isOverviewFocused = it.isFocused },
              minLines = 6,
              maxLines = 10,
              shape = RoundedCornerShape(16.dp),
              colors = OutlinedTextFieldDefaults.colors(
                focusedTextColor = MaterialTheme.colorScheme.onSurface,
                unfocusedTextColor = MaterialTheme.colorScheme.onSurface,
                focusedBorderColor = DiajakOrange,
                unfocusedBorderColor = Color.Transparent,
                cursorColor = DiajakOrange,
                focusedPlaceholderColor = MaterialTheme.colorScheme.onSurface,
                unfocusedPlaceholderColor = MaterialTheme.colorScheme.onSurface
              )
            )
            // Always-Floating Label on Border
            Box(
              modifier = Modifier
                .offset(x = 12.dp, y = 1.dp)
                .background(androidx.compose.ui.graphics.Color(0xFFE5E7EB))
                .padding(horizontal = 4.dp)
            ) {
              Text(
                text = buildAnnotatedString {
                  append("Deskripsi")
                  withStyle(SpanStyle(color = Color.Red)) {
                    append(" *")
                  }
                },
                style = com.example.ui.theme.DiajakDesignSystem.Typography.Body,
                color = if (isOverviewFocused) DiajakOrange else MaterialTheme.colorScheme.onSurface
              )
            }
          }
        }
        // DETAIL & PENGATURAN AKTIVITAS (FLATTENED)
        Column(
          modifier = Modifier
            .fillMaxWidth()
        ) {
          val categoryIcon = getCategoryIconVector(selectedCategory)
          ShopeeFormRow(
            icon = categoryIcon,
            iconColor = MaterialTheme.colorScheme.onSurface,
            label = "Kategori",
            isRequired = true,
            valueText = if (selectedCategory.isBlank()) "Tentukan Kategori" else selectedCategory,
            onClick = { activeSlideInScreen = SlideInType.KATEGORI }
          )

          val benefitCountText = if (selectedBenefits.isEmpty()) {
            "Tentukan Benefit"
          } else {
            selectedBenefits.map { benefit ->
              val firstLetterIdx = benefit.indexOfFirst { it.isLetterOrDigit() }
              if (firstLetterIdx != -1) benefit.substring(firstLetterIdx) else benefit
            }.joinToString(", ")
          }
          ShopeeFormRow(
            icon = Icons.Outlined.CardGiftcard,
            iconColor = MaterialTheme.colorScheme.onSurface,
            label = "Benefit",
            valueText = benefitCountText,
            onClick = { activeSlideInScreen = SlideInType.BENEFIT }
          )

          val scheduleSummary = if (schedule.isBlank()) "Tentukan Jadwal" else schedule
          ShopeeFormRow(
            icon = Icons.Outlined.CalendarToday,
            iconColor = MaterialTheme.colorScheme.onSurface,
            label = "Jadwal",
            isRequired = true,
            valueText = scheduleSummary,
            onClick = { activeSlideInScreen = SlideInType.WAKTU }
          )

          val locationSummary = if (address.isBlank()) "Tentukan Lokasi" else (address)
          ShopeeFormRow(
            icon = Icons.Outlined.Place,
            iconColor = MaterialTheme.colorScheme.onSurface,
            label = "Lokasi",
            isRequired = true,
            valueText = locationSummary,
            onClick = { activeSlideInScreen = SlideInType.LOKASI }
          )
        }

        // SECTION 5: PRICING & SLOTS (FLATTENED)
        Column(
          modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = DiajakDesignSystem.Dimens.ScreenPaddingHorizontal),
          verticalArrangement = Arrangement.spacedBy(AppSpacing.Small)
        ) {
          Row(horizontalArrangement = Arrangement.spacedBy(MaterialTheme.spacing.medium)) {
            // Biaya
            Column(modifier = Modifier.weight(1f)) {
              val displayPrice = if (priceString.isNotEmpty()) {
                String.format("%,d", priceString.toLongOrNull() ?: 0L).replace(',', '.')
              } else ""
              Box(modifier = Modifier.fillMaxWidth()) {
                OutlinedTextField(
                  value = if (isFree) "0" else displayPrice,
                  onValueChange = { if (!isFree) priceString = it.filter { ch -> ch.isDigit() } },
                  placeholder = { Text("100.000", style = com.example.ui.theme.DiajakDesignSystem.Typography.Body) },
                  enabled = !isFree,
                  singleLine = true,
                  keyboardOptions = KeyboardOptions(
                    keyboardType = KeyboardType.Number,
                    imeAction = androidx.compose.ui.text.input.ImeAction.Done
                  ),
                  keyboardActions = androidx.compose.foundation.text.KeyboardActions(
                    onDone = { focusManager.clearFocus() }
                  ),
                  shape = RoundedCornerShape(16.dp),
                  modifier = Modifier
                    .fillMaxWidth()
                    .padding(top = 10.dp)
                    .onFocusChanged { focusState ->
                      isPriceFocused = focusState.isFocused
                      if (focusState.isFocused) {
                        coroutineScope.launch {
                          for (i in 1..5) {
                            kotlinx.coroutines.delay(100)
                            scrollState.animateScrollTo(scrollState.maxValue)
                          }
                        }
                      }
                    },
                  colors = OutlinedTextFieldDefaults.colors(
                    focusedTextColor = MaterialTheme.colorScheme.onSurface,
                    unfocusedTextColor = MaterialTheme.colorScheme.onSurface,
                    focusedBorderColor = DiajakOrange,
                    unfocusedBorderColor = Color.Transparent,
                    cursorColor = DiajakOrange,
                    focusedPlaceholderColor = MaterialTheme.colorScheme.onSurface,
                    unfocusedPlaceholderColor = MaterialTheme.colorScheme.onSurface
                  )
                )
                // Always-Floating Label on Border
                Box(
                  modifier = Modifier
                    .offset(x = 12.dp, y = 1.dp)
                    .background(androidx.compose.ui.graphics.Color(0xFFE5E7EB))
                    .padding(horizontal = 4.dp)
                ) {
                  Text(
                    text = buildAnnotatedString {
                      append("Biaya")
                      if (!isFree) {
                        withStyle(SpanStyle(color = Color.Red)) {
                          append(" *")
                        }
                      }
                    },
                    style = com.example.ui.theme.DiajakDesignSystem.Typography.Body,
                    color = if (isPriceFocused) DiajakOrange else MaterialTheme.colorScheme.onSurface
                  )
                }
              }
            }

            // Kuota / Slot
            Column(modifier = Modifier.weight(1f)) {
              Box(modifier = Modifier.fillMaxWidth()) {
                OutlinedTextField(
                  value = quotaString,
                  onValueChange = { quotaString = it.filter { ch -> ch.isDigit() } },
                  placeholder = { Text("10", style = com.example.ui.theme.DiajakDesignSystem.Typography.Body) },
                  singleLine = true,
                  keyboardOptions = KeyboardOptions(
                    keyboardType = KeyboardType.Number,
                    imeAction = androidx.compose.ui.text.input.ImeAction.Done
                  ),
                  keyboardActions = androidx.compose.foundation.text.KeyboardActions(
                    onDone = { focusManager.clearFocus() }
                  ),
                  shape = RoundedCornerShape(16.dp),
                  modifier = Modifier
                    .fillMaxWidth()
                    .padding(top = 10.dp)
                    .onFocusChanged { focusState ->
                      isQuotaFocused = focusState.isFocused
                      if (focusState.isFocused) {
                        coroutineScope.launch {
                          for (i in 1..5) {
                            kotlinx.coroutines.delay(100)
                            scrollState.animateScrollTo(scrollState.maxValue)
                          }
                        }
                      }
                    },
                  colors = OutlinedTextFieldDefaults.colors(
                    focusedTextColor = MaterialTheme.colorScheme.onSurface,
                    unfocusedTextColor = MaterialTheme.colorScheme.onSurface,
                    focusedBorderColor = DiajakOrange,
                    unfocusedBorderColor = Color.Transparent,
                    cursorColor = DiajakOrange,
                    focusedPlaceholderColor = MaterialTheme.colorScheme.onSurface,
                    unfocusedPlaceholderColor = MaterialTheme.colorScheme.onSurface
                  )
                )
                // Always-Floating Label on Border
                Box(
                  modifier = Modifier
                    .offset(x = 12.dp, y = 1.dp)
                    .background(androidx.compose.ui.graphics.Color(0xFFE5E7EB))
                    .padding(horizontal = 4.dp)
                ) {
                  Text(
                    text = buildAnnotatedString {
                      append("Kuota")
                      withStyle(SpanStyle(color = Color.Red)) {
                        append(" *")
                      }
                    },
                    style = com.example.ui.theme.DiajakDesignSystem.Typography.Body,
                    color = if (isQuotaFocused) DiajakOrange else MaterialTheme.colorScheme.onSurface
                  )
                }
              }
            }
          }

          // Checkbox Gratis
          Row(
            verticalAlignment = Alignment.CenterVertically,
            modifier = Modifier
              .offset(x = (-12).dp)
              .clickable { 
                isFree = !isFree
                focusManager.clearFocus()
              }
              .padding(top = 0.dp, bottom = MaterialTheme.spacing.extraSmall, end = 20.dp)
          ) {
            Checkbox(
              checked = isFree,
              onCheckedChange = { 
                isFree = it
                focusManager.clearFocus()
              },
              colors = CheckboxDefaults.colors(checkedColor = DiajakOrange)
            )
            Spacer(modifier = Modifier.width(MaterialTheme.spacing.extraSmall))
            Text("Centang Jika Aktivitas Kamu Gratis", style = androidx.compose.ui.text.TextStyle(fontSize = 16.sp, fontWeight = androidx.compose.ui.text.font.FontWeight.Bold, fontFamily = com.example.ui.theme.AppFontFamily), color = MaterialTheme.colorScheme.onSurface)
          }
        }
      } // Closes DiajakCard Column
      } // Closes DiajakCard

      Spacer(modifier = Modifier.height(140.dp)) // Extra space to scroll past the floating bottom bar
    } // Closes scrollable Column

    if (!WindowInsets.isImeVisible && activeSlideInScreen == SlideInType.NONE) {
      // Sticky BOTTOM BAR with premium frosted white background, truly floating over the form content
      Box(
        modifier = Modifier
          .align(Alignment.BottomCenter)
          .fillMaxWidth()
          .background(Color.Transparent)
          .navigationBarsPadding()
          .padding(horizontal = AppSpacing.ScreenMargin, vertical = AppSpacing.Medium)
          .zIndex(5f)
      ) {
      Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.spacedBy(AppSpacing.Medium)
      ) {
        // BUTTON 1: SIMPAN KE DRAFT (FULLY TRANSPARENT BACKGROUND, NO SHADOW)
        OutlinedButton(
          onClick = {
            if (title.isBlank() ){
              Toast.makeText(context, "Harap isi nama aktivitas!", Toast.LENGTH_SHORT).show()
              return@OutlinedButton
            }
            val pVal = if (isFree) 0 else priceString.toIntOrNull() ?: 100000
            val pFormatted = if (pVal == 0) "Gratis" else "Rp ${String.format("%,d", pVal).replace(',', '.')}"
            val quotaVal = quotaString.toIntOrNull() ?: 10
            val detailsOverview = if (overview.isBlank()) "Bergabunglah untuk aktivitas seru dan asik bersama komunitas kami!" else overview

            val newSaved = onSave(savedActivity?.id, title, selectedCategory, locationName, address, schedule, pFormatted, pVal, detailsOverview, quotaVal, selectedImageRes, selectedBenefits.toList(), pinX, pinY)
            savedActivity = newSaved
            Toast.makeText(context, "Aktivitas berhasil disimpan! 💾", Toast.LENGTH_SHORT).show()
            isSaved = true
          },
          shape = RoundedCornerShape(16.dp),
          colors = ButtonDefaults.outlinedButtonColors(containerColor = Color.White, contentColor = DiajakOrange),
          border = BorderStroke(1.5.dp, DiajakOrange),
          elevation = ButtonDefaults.buttonElevation(defaultElevation = 0.dp),
          modifier = Modifier
            .weight(1f)
            .height(52.dp)
        ) {
          Text("Simpan", style = androidx.compose.ui.text.TextStyle(fontSize = 16.sp, fontWeight = androidx.compose.ui.text.font.FontWeight.Bold, fontFamily = com.example.ui.theme.AppFontFamily), color = DiajakOrange)
        }

        // BUTTON 2: TAMPILKAN / PUBLISH LIVE (SOLID ORANGE, LIKE TERAPKAN BUTTON IN FILTER)
        Button(
          onClick = {
            if (title.isBlank() ){
              Toast.makeText(context, "Harap isi nama aktivitas!", Toast.LENGTH_SHORT).show()
              return@Button
            }
            if (selectedCategory.isBlank() ){
              Toast.makeText(context, "Harap pilih kategori!", Toast.LENGTH_SHORT).show()
              return@Button
            }
            if (locationName.isBlank() ){
              Toast.makeText(context, "Harap isi lokasi!", Toast.LENGTH_SHORT).show()
              return@Button
            }
            if (schedule.isBlank() ){
              Toast.makeText(context, "Harap isi waktu & jadwal!", Toast.LENGTH_SHORT).show()
              return@Button
            }
            val pVal = if (isFree) 0 else priceString.toIntOrNull() ?: 100000
            val pFormatted = if (pVal == 0) "Gratis" else "Rp ${String.format("%,d", pVal).replace(',', '.')}"
            val quotaVal = quotaString.toIntOrNull() ?: 10
            val detailsOverview = if (overview.isBlank()) "Bergabunglah untuk aktivitas seru dan asik bersama komunitas kami!" else overview

            val targetSaved = savedActivity ?: onSave(null, title, selectedCategory, locationName, address, schedule, pFormatted, pVal, detailsOverview, quotaVal, selectedImageRes, selectedBenefits.toList(), pinX, pinY)
            savedActivity = targetSaved
            onSubmit(targetSaved)
          },
          shape = RoundedCornerShape(16.dp),
          colors = ButtonDefaults.buttonColors(containerColor = DiajakOrange, contentColor = Color.White),
          elevation = ButtonDefaults.buttonElevation(defaultElevation = 0.dp),
          modifier = Modifier
            .weight(1f)
            .height(52.dp)
        ) {
          Text("Tampilkan", style = androidx.compose.ui.text.TextStyle(fontSize = 16.sp, fontWeight = androidx.compose.ui.text.font.FontWeight.Bold, fontFamily = com.example.ui.theme.AppFontFamily), color = Color.White)
        }
      }
    }
  }

  // 2. Glass Header Layer (Only shown when not showing slide-in screen)
  if (activeSlideInScreen == SlideInType.NONE) {
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
          .padding(horizontal = 20.dp)
      ) {
        // Frosted Glass Circular Back Button
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

        // Title (Strictly Centered with Headline Typography)
        Text(
          text = "Tambah Aktivitas",
          style = DiajakDesignSystem.Typography.Headline,
          color = MaterialTheme.colorScheme.onSurface,
          textAlign = TextAlign.Center,
          modifier = Modifier
            .align(Alignment.Center)
            .fillMaxWidth()
            .padding(horizontal = 48.dp),
          maxLines = 1,
          overflow = TextOverflow.Ellipsis
        )
      }
    }
  }
  if (showMapDialog) {
      AlertDialog(
        onDismissRequest = { showMapDialog = false },
        title = { Text("Pilih Titik Lokasi", fontWeight = FontWeight.Medium) },
        text = {
          Column {
            Text("Silakan geser pin pada peta untuk menentukan lokasi kumpul secara akurat.", style = com.example.ui.theme.DiajakDesignSystem.Typography.Body, color = MaterialTheme.colorScheme.onSurface)
            Spacer(modifier = Modifier.height(MaterialTheme.spacing.medium))
            Box(
              modifier = Modifier
                .fillMaxWidth()
                .height(200.dp)
                .clip(RoundedCornerShape(12.dp))
                .background(androidx.compose.ui.graphics.Color(0xFFE5E7EB)),
              contentAlignment = Alignment.Center
            ) {
              Text("Map Simulation", color = Color(0xFF262626), fontWeight = FontWeight.Medium)
              CustomMapPinMarker(
                categoryIcon = Icons.Outlined.Place,
                isSelected = true,
                title = "Titik Kumpul"
              )
            }
          }
        },
        confirmButton = {
          Button(
            onClick = { showMapDialog = false },
            colors = ButtonDefaults.buttonColors(containerColor = DiajakOrange)
          ) {
            Text("Simpan Titik")
          }
        }
      )
    }

  // SLIDE IN SCREENS
  AnimatedVisibility(
    visible = activeSlideInScreen != SlideInType.NONE,
    enter = slideInHorizontally(initialOffsetX = { it }),
    exit = slideOutHorizontally(targetOffsetX = { it }),
    modifier = Modifier
      .fillMaxSize()
      .zIndex(20f)
  ) {
    when (activeSlideInScreen) {
      SlideInType.KATEGORI -> {
        SlideInKategoriScreen(
          categories = categories,
          selectedCategory = selectedCategory,
          onSelect = { 
            selectedCategory = it
            activeSlideInScreen = SlideInType.NONE 
          },
          onBack = { activeSlideInScreen = SlideInType.NONE }
        )
      }
      SlideInType.BENEFIT -> {
        SlideInBenefitScreen(
          standardBenefits = standardBenefits,
          selectedBenefits = selectedBenefits,
          onBack = { activeSlideInScreen = SlideInType.NONE }
        )
      }
      SlideInType.WAKTU -> {
        SlideInWaktuScreen(
          schedule = schedule,
          onScheduleChange = { schedule = it },
          onBack = { activeSlideInScreen = SlideInType.NONE }
        )
      }
      SlideInType.LOKASI -> {
        SlideInLokasiScreen(
          context = context,
          coroutineScope = coroutineScope,
          isLocating = isLocating,
          locationName = locationName,
          address = address,
          onLocateStart = { isLocating = true },
          onLocateEnd = { name, addr ->
            isLocating = false
            if (name.isNotEmpty()) locationName = name
            if (addr.isNotEmpty()) address = addr
          },
          onAddressChange = { address = it },
          onLocationNameChange = { locationName = it },
          pinX = pinX,
          pinY = pinY,
          onPinChange = { x, y ->
            pinX = x
            pinY = y
          },
          onBack = { activeSlideInScreen = SlideInType.NONE }
        )
      }
      else -> {}
    }
  }
  }
}



fun getBenefitsForCategory(category: String): List<String> {
  val clean = category.trim().lowercase()
  return when {
    clean == "kopi" || clean.contains("kopi") -> listOf(
      "Free Welcome Drink & Snack",
      "Voucher Diskon Pembelian Kopi",
      "Buku Panduan Brewing Dasar",
      "Cicip Varian Biji Kopi Spesial",
      "Goodie Bag Biji Kopi Pilihan"
    )
    clean in listOf("alam", "kemah", "pendakian", "petualangan", "piknik") || clean.contains("alam") || clean.contains("outdoor") -> listOf(
      "Tenda & Peralatan Camping Lengkap",
      "Makan Malam BBQ & Sarapan",
      "Tiket Masuk Kawasan Wisata",
      "Pemandu Outdoor Profesional",
      "Dokumentasi Foto & Video Alam HD"
    )
    clean in listOf("seni", "kriya", "lokakarya", "fotografi", "menulis") || clean.contains("seni") || clean.contains("kriya") || clean.contains("workshop") -> listOf(
      "Bahan & Alat Karya Seni Lengkap",
      "Hasil Karya Bisa Dibawa Pulang",
      "Free Teh/Kopi & Snack Ringan",
      "Bimbingan dari Seniman Berpengalaman",
      "Dokumentasi Foto Estetik HD"
    )
    clean in listOf("olahraga", "lari", "sepeda", "kebugaran") || clean.contains("olahraga") || clean.contains("lari") || clean.contains("fitness") -> listOf(
      "Minuman Isotonik & Air Mineral Dingin",
      "Jersey/Kaos Aktivitas Eksklusif",
      "Tas Serut & Merchandise Olahraga",
      "Pelatih/Instruktur Berlisensi",
      "Foto Aksi Berkualitas Tinggi"
    )
    clean in listOf("kuliner", "wisata", "perjalanan", "bazar") || clean.contains("kuliner") || clean.contains("trip") || clean.contains("food") -> listOf(
      "Makan & Cicip Kuliner Khas Sepuasnya",
      "Transportasi PP dengan Driver Profesional",
      "Tiket Masuk Seluruh Destinasi Wisata",
      "Tour Guide/Pemandu Lokal Ramah",
      "Oleh-oleh Khas Eksklusif"
    )
    clean in listOf("yoga", "meditasi", "kesejahteraan", "relaksasi", "kesehatan") || clean.contains("yoga") || clean.contains("wellness") || clean.contains("meditasi") -> listOf(
      "Matras Yoga Higienis Disediakan",
      "Jus Sehat/Detox Drink & Buah Segar",
      "Terapi Suara & Aromaterapi Relaksasi",
      "Instruktur Yoga Bersertifikat",
      "Voucher Diskon Kelas Lanjutan"
    )
    clean in listOf("musik", "konser", "menari", "hiburan", "teater", "film") || clean.contains("musik") || clean.contains("konser") -> listOf(
      "Akses Area VIP Dekat Panggung",
      "Free Welcome Drink & Snack",
      "Gelang Konser & Poster Eksklusif",
      "Sesi Foto Bareng Pengisi Aktivitas",
      "Merchandise Band/Kreator Eksklusif"
    )
    clean in listOf("esport", "permainan") || clean.contains("esport") || clean.contains("game") || clean.contains("gaming") -> listOf(
      "Akses PC/Konsol Performa Tinggi",
      "Snack & Minuman Energi Gratis",
      "E-Certificate Turnamen Resmi",
      "Plakat/Medali untuk Juara",
      "Merchandise Gaming Eksklusif"
    )
    clean in listOf("komunitas", "hobi", "sosial", "jejaring", "relawan") || clean.contains("komunitas") || clean.contains("hobi") -> listOf(
      "Akses Komunitas Eksklusif",
      "Kopi & Snack Selama Sesi Sharing",
      "E-Certificate Keanggotaan",
      "Materi/E-Book Panduan Hobi",
      "Goodie Bag Komunitas Menarik"
    )
    clean in listOf("seminar", "gelarwicara", "pelatihan", "pendidikan", "bisnis", "karier", "keuangan", "investasi", "teknologi", "sains") || clean.contains("seminar") || clean.contains("wicara") || clean.contains("talkshow") -> listOf(
      "E-Certificate Resmi Pembicara",
      "Modul Materi & Blocknote Eksklusif",
      "Makan Siang & Coffee Break",
      "Sesi Tanya Jawab Langsung & Networking",
      "Souvenir Seminar Eksklusif"
    )
    clean in listOf("pesta", "festival") || clean.contains("party") || clean.contains("pesta") -> listOf(
      "Welcome Drink & Free-flow Softdrink",
      "Prasmanan BBQ & Camilan Lezat",
      "Hiburan Live DJ / Acoustic Band",
      "Photobooth & Cetak Foto Gratis",
      "Merchandise Party Lucu"
    )
    else -> listOf(
      "Free Welcome Drink & Snack",
      "E-Certificate Resmi",
      "Merchandise Eksklusif",
      "Dokumentasi Foto & Video HD",
      "Pemandu/Fasilitator Berpengalaman"
    )
  }
}

@OptIn(ExperimentalMaterial3Api::class, ExperimentalLayoutApi::class)
@Composable
fun SlideInBenefitScreen(
  standardBenefits: List<String>,
  selectedBenefits: androidx.compose.runtime.snapshots.SnapshotStateList<String>,
  onBack: () -> Unit
) {
  val focusManager = androidx.compose.ui.platform.LocalFocusManager.current
  var customInput by remember { mutableStateOf("") }
  val scrollState = rememberScrollState()
  val hazeState = remember { HazeState() }
  val coroutineScope = rememberCoroutineScope()

  // Auto-scroll to bottom when new benefits are added so they are visible
  LaunchedEffect(selectedBenefits.size) {
    if (selectedBenefits.isNotEmpty()) {
      scrollState.animateScrollTo(scrollState.maxValue)
    }
  }

  Box(
    modifier = Modifier
      .fillMaxSize().background(androidx.compose.ui.graphics.Color(0xFFE5E7EB))
      .imePadding()
      .pointerInput(Unit) {
        detectTapGestures(
          onTap = {
            focusManager.clearFocus()
          }
        )
      }
  ) {
    // 1. Content Layer (Always hazeSource)
    Column(
      modifier = Modifier
        .fillMaxSize()
        .hazeSource(state = hazeState)
        .verticalScroll(scrollState)
    ) {
      Spacer(modifier = Modifier.statusBarsPadding().height(80.dp))

      DiajakFlowRow(
        modifier = Modifier
          .fillMaxWidth()
          .padding(horizontal = 20.dp),
        horizontalArrangement = Arrangement.spacedBy(MaterialTheme.spacing.small),
        verticalArrangement = Arrangement.spacedBy(AppSpacing.Small)
      ) {
          // Render standard benefits
          standardBenefits.forEach { benefit ->
            val isChecked = selectedBenefits.contains(benefit)

            Row(
              modifier = Modifier
                .clip(RoundedCornerShape(8.dp))
                .background(if (isChecked) Color(0xFFFFF5F5) else Color(0xFFF8FAFC))
                .clickable {
                  focusManager.clearFocus()
                  if (isChecked) selectedBenefits.remove(benefit)
                  else selectedBenefits.add(benefit)
                }
                .padding(horizontal = 20.dp, vertical = MaterialTheme.spacing.small),
              verticalAlignment = Alignment.CenterVertically
            ) {
              if (isChecked) {
                Icon(
                  imageVector = Icons.Outlined.Check,
                  contentDescription = null,
                  tint = DiajakOrange,
                  modifier = Modifier.size(20.dp)
                )
                Spacer(modifier = Modifier.width(MaterialTheme.spacing.small))
              }

              Text(
                text = benefit,
                fontWeight = if (isChecked) FontWeight.Bold else FontWeight.Medium,
                style = com.example.ui.theme.DiajakDesignSystem.Typography.Body,
                color = if (isChecked) DiajakOrange else MaterialTheme.colorScheme.onSurface
              )
            }
          }

          // Render custom benefits at the same top flow list
          selectedBenefits.filter { !standardBenefits.contains(it) }.forEach { customBen ->
            Row(
              modifier = Modifier
                .clip(RoundedCornerShape(8.dp))
                .background(Color(0xFFFFF5F5))
                .clickable {
                  focusManager.clearFocus()
                  selectedBenefits.remove(customBen)
                }
                .padding(horizontal = 20.dp, vertical = MaterialTheme.spacing.small),
              verticalAlignment = Alignment.CenterVertically
            ) {
              Icon(
                imageVector = Icons.Outlined.Check,
                contentDescription = null,
                tint = DiajakOrange,
                modifier = Modifier.size(20.dp)
              )
              Spacer(modifier = Modifier.width(MaterialTheme.spacing.small))

              Text(
                text = customBen,
                style = com.example.ui.theme.DiajakDesignSystem.Typography.Body,
                color = DiajakOrange
              )
              Spacer(modifier = Modifier.width(MaterialTheme.spacing.small))
              Icon(
                imageVector = Icons.Outlined.Close,
                contentDescription = "Hapus",
                tint = DiajakOrange,
                modifier = Modifier.size(20.dp)
              )
            }
          }
        }

        Spacer(modifier = Modifier.height(120.dp))
      }

    // 2. Glass Header Layer
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
          .padding(horizontal = 20.dp)
      ) {
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

        Text(
          text = "Tentukan Benefit",
          style = DiajakDesignSystem.Typography.Headline,
          color = MaterialTheme.colorScheme.onSurface,
          textAlign = TextAlign.Center,
          modifier = Modifier
            .align(Alignment.Center)
            .fillMaxWidth()
            .padding(horizontal = 48.dp),
          maxLines = 1,
          overflow = TextOverflow.Ellipsis
        )
      }
    }

    // Sticky BOTTOM BAR with Solid White Background for Custom Benefit Input & Simpan Button
    Surface(
      modifier = Modifier
        .align(Alignment.BottomCenter)
        .fillMaxWidth()
        .zIndex(5f),
      color = Color.Transparent,
      shadowElevation = 0.dp
    ) {
      Column(
        modifier = Modifier
          .fillMaxWidth()
          .padding(start = 20.dp, end = 20.dp, top = 20.dp, bottom = 20.dp)
          .navigationBarsPadding()
          .imePadding()
      ) {
        Row(
          modifier = Modifier.fillMaxWidth(),
          verticalAlignment = Alignment.CenterVertically,
          horizontalArrangement = Arrangement.spacedBy(MaterialTheme.spacing.small)
        ) {
          OutlinedTextField(
            value = customInput,
            onValueChange = { customInput = it },
            placeholder = { Text("Misal Gratis Dokumentasi", style = androidx.compose.ui.text.TextStyle(fontSize = 16.sp, fontWeight = androidx.compose.ui.text.font.FontWeight.Bold, fontFamily = com.example.ui.theme.AppFontFamily)) },
            modifier = Modifier.weight(1f),
            singleLine = true,
            shape = RoundedCornerShape(16.dp),
            colors = OutlinedTextFieldDefaults.colors(
              focusedTextColor = MaterialTheme.colorScheme.onSurface,
              unfocusedTextColor = MaterialTheme.colorScheme.onSurface,
              focusedBorderColor = DiajakOrange,
              unfocusedBorderColor = Color.Transparent,
              cursorColor = DiajakOrange,
              focusedPlaceholderColor = MaterialTheme.colorScheme.onSurface,
              unfocusedPlaceholderColor = MaterialTheme.colorScheme.onSurface,
              focusedContainerColor = Color.White,
              unfocusedContainerColor = Color.White
            )
          )
          OutlinedButton(
            onClick = {
              if (customInput.isNotBlank()) {
                selectedBenefits.add(customInput.trim())
                customInput = ""
                coroutineScope.launch {
                  kotlinx.coroutines.delay(100)
                  scrollState.animateScrollTo(scrollState.maxValue)
                }
              }
            },
            colors = ButtonDefaults.outlinedButtonColors(containerColor = Color.Transparent),
            border = BorderStroke(1.5.dp, DiajakOrange),
            modifier = Modifier.height(50.dp),
            shape = RoundedCornerShape(16.dp)
          ) {
            Text("Tambah", color = DiajakOrange, style = androidx.compose.ui.text.TextStyle(fontSize = 16.sp, fontWeight = androidx.compose.ui.text.font.FontWeight.Bold, fontFamily = com.example.ui.theme.AppFontFamily))
          }
        }

        // Simpan button is only visible when keyboard is NOT open
        if (!WindowInsets.isImeVisible) {
          Spacer(modifier = Modifier.height(MaterialTheme.spacing.medium))
          Button(
            onClick = onBack,
            colors = ButtonDefaults.buttonColors(containerColor = DiajakOrange),
            modifier = Modifier
              .fillMaxWidth()
              .height(52.dp),
            shape = RoundedCornerShape(16.dp)
          ) {
            Text("Simpan", color = Color.White, style = androidx.compose.ui.text.TextStyle(fontSize = 16.sp, fontWeight = androidx.compose.ui.text.font.FontWeight.Bold, fontFamily = com.example.ui.theme.AppFontFamily))
          }
        }
      }
    }
  }
}

data class CreatorPresetLocation(
  val name: String,
  val cityName: String,
  val address: String,
  val x: Float,
  val y: Float,
  val icon: String = "📍"
)

val creatorPresetLocations = listOf(
  CreatorPresetLocation(
    name = "Java Chicken Purworejo",
    cityName = "Purworejo, Jawa Tengah",
    address = "Jl. Ir. H Juanda, Baledono, Purworejo",
    x = 0.52f,
    y = 0.42f,
    icon = "🍗"
  ),
  CreatorPresetLocation(
    name = "Mie Ayam Pak Asep",
    cityName = "Purworejo, Jawa Tengah",
    address = "Jl. Ir. H Juanda, Purworejo, Purworejo",
    x = 0.45f,
    y = 0.55f,
    icon = "🍜"
  ),
  CreatorPresetLocation(
    name = "Toko SRC Budi Baledono",
    cityName = "Purworejo, Jawa Tengah",
    address = "Jl. Ir. H Juanda No. 25, Baledono, Purworejo",
    x = 0.38f,
    y = 0.60f,
    icon = "🏪"
  ),
  CreatorPresetLocation(
    name = "Pintu Masuk Makam Brengkelan",
    cityName = "Purworejo, Jawa Tengah",
    address = "Jl. Kyai Brengkel, Purworejo, Purworejo",
    x = 0.30f,
    y = 0.40f,
    icon = "⛰️"
  ),
  CreatorPresetLocation(
    name = "Agen Shuttle Sumber Alam Brengkelan",
    cityName = "Purworejo, Jawa Tengah",
    address = "Jl. Kyai Brengkel No.12, Purworejo, Jawa Tengah",
    x = 0.70f,
    y = 0.65f,
    icon = "🚌"
  ),
  CreatorPresetLocation(
    name = "Nox Coffee Boutique Sleman",
    cityName = "Sleman, D.I. Yogyakarta",
    address = "Jl. Kaliurang KM 5, Sleman, D.I. Yogyakarta",
    x = 0.60f,
    y = 0.30f,
    icon = "☕"
  ),
  CreatorPresetLocation(
    name = "Starbucks Malioboro Mall",
    cityName = "Kota Yogyakarta, DIY",
    address = "Malioboro Mall Ground Floor, Jl. Malioboro, DIY",
    x = 0.50f,
    y = 0.50f,
    icon = "☕"
  )
)

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SlideInLokasiScreen(
  context: android.content.Context,
  coroutineScope: kotlinx.coroutines.CoroutineScope,
  isLocating: Boolean,
  locationName: String,
  address: String,
  onLocateStart: () -> Unit,
  onLocateEnd: (String, String) -> Unit,
  onAddressChange: (String) -> Unit,
  onLocationNameChange: (String) -> Unit,
  pinX: Float,
  pinY: Float,
  onPinChange: (Float, Float) -> Unit,
  onBack: () -> Unit
) {
  val focusManager = androidx.compose.ui.platform.LocalFocusManager.current
  var mapScale by remember { mutableStateOf(1.2f) }
  var mapOffset by remember { mutableStateOf(Offset.Zero) }
  var isFirstLayout by remember { mutableStateOf(true) }
  var isSimulatingNearMe by remember { mutableStateOf(false) }

  var localPinX by remember { mutableStateOf(pinX) }
  var localPinY by remember { mutableStateOf(pinY) }
  val hazeState = remember { HazeState() }

  LaunchedEffect(pinX, pinY) {
    localPinX = pinX
    localPinY = pinY
  }

  val nearestPreset = remember(localPinX, localPinY) {
    creatorPresetLocations.minByOrNull { preset ->
      val dx = preset.x - localPinX
      val dy = preset.y - localPinY
      dx * dx + dy * dy
    } ?: creatorPresetLocations.first()
  }

  LaunchedEffect(nearestPreset) {
    onLocationNameChange(nearestPreset.cityName)
    onAddressChange(nearestPreset.address)
  }

  BoxWithConstraints(
    modifier = Modifier
      .fillMaxSize()
      .background(Color(0xFFE3F4F4))
  ) {
    val mapWidth = maxWidth
    val mapHeight = maxHeight
    val density = LocalDensity.current
    val widthPx = with(density) { mapWidth.toPx() }
    val heightPx = with(density) { mapHeight.toPx() }

    LaunchedEffect(widthPx, heightPx) {
      if (widthPx > 0 && heightPx > 0 && isFirstLayout) {
        val targetX = -(widthPx * localPinX - widthPx / 2f) * mapScale
        val targetY = -(heightPx * localPinY - heightPx / 2f) * mapScale
        mapOffset = Offset(targetX, targetY)
        isFirstLayout = false
      }
    }

    val animatedMapScale by animateFloatAsState(targetValue = mapScale, label = "mapScale", animationSpec = spring())
    val animatedMapOffsetX by animateFloatAsState(targetValue = mapOffset.x, label = "mapOffsetX", animationSpec = spring())
    val animatedMapOffsetY by animateFloatAsState(targetValue = mapOffset.y, label = "mapOffsetY", animationSpec = spring())

    Box(
      modifier = Modifier
        .fillMaxSize()
        .hazeSource(state = hazeState)
        .pointerInput(Unit) {
          detectTransformGestures { _, pan, zoom, _ ->
            mapScale = (mapScale * zoom).coerceIn(0.6f, 4.0f)
            mapOffset += pan

            if (widthPx > 0 && heightPx > 0) {
              val newX = (0.5f - mapOffset.x / (widthPx * mapScale)).coerceIn(0.02f, 0.98f)
              val newY = (0.5f - mapOffset.y / (heightPx * mapScale)).coerceIn(0.02f, 0.98f)
              localPinX = newX
              localPinY = newY
              onPinChange(newX, newY)
            }
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

      creatorPresetLocations.forEach { loc ->
        val lx = widthPx * loc.x
        val ly = heightPx * loc.y
        val isSelected = (nearestPreset == loc)

        Box(
          modifier = Modifier
            .offset(
              x = with(density) { lx.toDp() } - 20.dp,
              y = with(density) { ly.toDp() } - 36.dp
            )
            .size(28.dp)
            .clickable(
              interactionSource = remember { MutableInteractionSource() },
              indication = null
            ) {
              localPinX = loc.x
              localPinY = loc.y
              onPinChange(loc.x, loc.y)
              onLocationNameChange(loc.cityName)
              onAddressChange("${loc.name}, ${loc.address}")

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
                .border(
                  width = if (isSelected) 1.5.dp else 1.dp,
                  color = if (isSelected) DiajakOrange else Color(0xFF94A3B8),
                  shape = CircleShape
                ),
              contentAlignment = Alignment.Center
            ) {
              Text(loc.icon, style = androidx.compose.ui.text.TextStyle(fontSize = 16.sp, fontWeight = androidx.compose.ui.text.font.FontWeight.Bold, fontFamily = com.example.ui.theme.AppFontFamily))
            }
            Icon(
              imageVector = Icons.Outlined.Place,
              contentDescription = null,
              tint = if (isSelected) DiajakOrange else Color(0xFF64748B),
              modifier = Modifier.size(20.dp)
            )
          }
        }
      }
    }

    Column(
      modifier = Modifier
        .align(Alignment.Center)
        .offset(y = (-26).dp),
      horizontalAlignment = Alignment.CenterHorizontally
    ) {
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
            text = nearestPreset.icon,
            style = androidx.compose.ui.text.TextStyle(fontSize = 16.sp, fontWeight = androidx.compose.ui.text.font.FontWeight.Bold, fontFamily = com.example.ui.theme.AppFontFamily)
          )
          Spacer(modifier = Modifier.width(MaterialTheme.spacing.extraSmall))
          Text(
            text = nearestPreset.name,
            style = androidx.compose.ui.text.TextStyle(fontSize = 16.sp, fontWeight = androidx.compose.ui.text.font.FontWeight.Bold, fontFamily = com.example.ui.theme.AppFontFamily),
            
            color = Color.White
          )
        }
      }
      Icon(
        imageVector = Icons.Outlined.Place,
        contentDescription = "Main Location Pin",
        tint = DiajakOrange,
        modifier = Modifier.size(28.dp)
      )
    }



    // Top Header Layer with Back Button, Centered Headline, and GPS button using DiajakGlassHeader
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
          .padding(horizontal = 20.dp)
      ) {
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

        Text(
          text = "Tentukan Lokasi",
          style = DiajakDesignSystem.Typography.Headline,
          color = MaterialTheme.colorScheme.onSurface,
          textAlign = TextAlign.Center,
          modifier = Modifier
            .align(Alignment.Center)
            .fillMaxWidth()
            .padding(horizontal = 48.dp),
          maxLines = 1,
          overflow = TextOverflow.Ellipsis
        )

        IconButton(
          onClick = {
            coroutineScope.launch {
              isSimulatingNearMe = true
              Toast.makeText(context, "Mendeteksi sinyal GPS terdekat...", Toast.LENGTH_SHORT).show()
              kotlinx.coroutines.delay(1000)
              
              val preset = creatorPresetLocations[5]
              localPinX = preset.x
              localPinY = preset.y
              onPinChange(preset.x, preset.y)
              onLocationNameChange(preset.cityName)
              onAddressChange("${preset.name}, ${preset.address}")

              isSimulatingNearMe = false
              Toast.makeText(context, "GPS Terpusat: Sleman, Nox Coffee Boutique 📍", Toast.LENGTH_LONG).show()

              val targetX = -(widthPx * preset.x - widthPx / 2f) * mapScale
              val targetY = -(heightPx * preset.y - heightPx / 2f) * mapScale
              mapOffset = Offset(targetX, targetY)
            }
          },
          modifier = Modifier
            .size(40.dp)
            .align(Alignment.CenterEnd)
            .diajakGlassButton(hazeState)
        ) {
          if (isSimulatingNearMe) {
            CircularProgressIndicator(color = DiajakOrange, modifier = Modifier.size(20.dp), strokeWidth = 2.dp)
          } else {
            Icon(Icons.Outlined.MyLocation, contentDescription = "Near Me", tint = DiajakOrange, modifier = Modifier.size(20.dp))
          }
        }
      }
    }

    // Sticky BOTTOM BAR with Solid White Background for Simpan Lokasi
    Surface(
      modifier = Modifier
        .align(Alignment.BottomCenter)
        .fillMaxWidth()
        .zIndex(5f),
      color = Color.Transparent,
      shadowElevation = 0.dp
    ) {
      Button(
        onClick = {
          onBack()
          Toast.makeText(context, "Titik kumpul berhasil disimpan! 📍", Toast.LENGTH_SHORT).show()
        },
        colors = ButtonDefaults.buttonColors(containerColor = DiajakOrange),
        modifier = Modifier
          .fillMaxWidth()
          .padding(start = 20.dp, end = 20.dp, top = MaterialTheme.spacing.medium, bottom = 20.dp)
          .navigationBarsPadding()
          .height(52.dp),
        shape = RoundedCornerShape(16.dp)
      ) {
        Text("Simpan", color = Color.White, style = androidx.compose.ui.text.TextStyle(fontSize = 16.sp, fontWeight = androidx.compose.ui.text.font.FontWeight.Bold, fontFamily = com.example.ui.theme.AppFontFamily))
      }
    }
  }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SlideInWaktuScreen(
  schedule: String,
  onScheduleChange: (String) -> Unit,
  onBack: () -> Unit,
  showTopBar: Boolean = true
) {
  val today = java.time.LocalDate.now().let {
    if (it.isBefore(java.time.LocalDate.of(2026, 7, 12)) ){
      java.time.LocalDate.of(2026, 7, 12)
    } else {
      it
    }
  }

  // Parse the existing schedule string to initialize the selectedDates and times:
  val parsedInitialDates = remember(schedule) {
    val list = parseMultipleSchedulesToLocalDates(schedule)
    if (list.isNotEmpty()) list.toSet() else setOf(today)
  }
  
  var selectedDates by remember { mutableStateOf(parsedInitialDates) }

  val parsedTimes = remember(schedule) {
    val parts = schedule.split("•")
    if (parts.size > 1) {
      val timeRange = parts[1].trim().replace("", "")
      val times = timeRange.split("-")
      if (times.size == 2) {
        val startParts = times[0].trim().split(":")
        val endParts = times[1].trim().split(":")
        val sh = startParts.firstOrNull()?.toIntOrNull() ?: 9
        val sm = startParts.getOrNull(1)?.toIntOrNull() ?: 0
        val eh = endParts.firstOrNull()?.toIntOrNull() ?: 12
        val em = endParts.getOrNull(1)?.toIntOrNull() ?: 0
        Triple(sh, sm, Pair(eh, em))
      } else {
        Triple(9, 0, Pair(12, 0))
      }
    } else {
      Triple(9, 0, Pair(12, 0))
    }
  }

  var startHour by remember { mutableStateOf(parsedTimes.first) }
  var startMinute by remember { mutableStateOf(parsedTimes.second) }
  var endHour by remember { mutableStateOf(parsedTimes.third.first) }
  var endMinute by remember { mutableStateOf(parsedTimes.third.second) }

  var currentYearMonth by remember {
    mutableStateOf(java.time.YearMonth.of(
      (selectedDates.minOrNull() ?: today).year,
      (selectedDates.minOrNull() ?: today).monthValue
    ))
  }

  val dayNamesIndo = listOf("Senin", "Selasa", "Rabu", "Kamis", "Jumat", "Sabtu", "Minggu")
  val monthNamesIndo = listOf(
    "Jan", "Feb", "Mar", "Apr", "Mei", "Jun",
    "Jul", "Ags", "Sep", "Okt", "Nov", "Des"
  )

  // Live preview calculations
  val formattedTimeStr = "${startHour.toString().padStart(2, '0')}:${startMinute.toString().padStart(2, '0')} - ${endHour.toString().padStart(2, '0')}:${endMinute.toString().padStart(2, '0')}"
  val liveScheduleString = remember(selectedDates, startHour, startMinute, endHour, endMinute) {
    if (selectedDates.isEmpty() ){
      "Belum ada tanggal dipilih • $formattedTimeStr"
    } else {
      val formattedDates = selectedDates.sorted().map { date ->
        val dayOfWeekVal = date.dayOfWeek.value
        val dayName = dayNamesIndo[dayOfWeekVal - 1]
        val monthName = monthNamesIndo[date.monthValue - 1]
        "${date.dayOfMonth} $monthName ${date.year}"
      }
      val datePartRaw = formattedDates.joinToString("; ")
      "$datePartRaw • $formattedTimeStr"
    }
  }

  val focusManager = androidx.compose.ui.platform.LocalFocusManager.current
  val hazeState = remember { HazeState() }
  val scrollState = rememberScrollState()

  Box(
    modifier = Modifier
      .fillMaxSize().background(androidx.compose.ui.graphics.Color(0xFFE5E7EB))
      .imePadding()
      .pointerInput(Unit) {
        detectTapGestures(
          onTap = {
            focusManager.clearFocus()
          }
        )
      }
  ) {
    val hoursList = remember { (0..23).map { it.toString().padStart(2, '0') } }
    val minutesList = remember { (0..59).map { it.toString().padStart(2, '0') } }

    // 1. Content Layer
    Column(
      modifier = Modifier
        .fillMaxSize()
        .hazeSource(state = hazeState)
        .verticalScroll(scrollState)
        .padding(bottom = 100.dp)
    ) {
      if (showTopBar) {
        Spacer(modifier = Modifier.statusBarsPadding().height(80.dp))
      }

      AturWaktuContent(
        currentYearMonth = currentYearMonth,
        onCurrentYearMonthChange = { currentYearMonth = it },
        selectedDates = selectedDates,
        onSelectedDatesChange = { selectedDates = it },
        startHour = startHour,
        onStartHourChange = { startHour = it },
        startMinute = startMinute,
        onStartMinuteChange = { startMinute = it },
        endHour = endHour,
        onEndHourChange = { endHour = it },
        endMinute = endMinute,
        onEndMinuteChange = { endMinute = it },
        today = today,
        monthNamesIndo = monthNamesIndo,
        hoursList = hoursList,
        minutesList = minutesList,
        showSpacers = false,
        columnModifier = Modifier
          .fillMaxWidth()
          .padding(horizontal = 20.dp)
      )
    }

    // 2. Glass Header Layer
    if (showTopBar) {
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
            .padding(horizontal = 20.dp)
        ) {
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

          Text(
            text = "Tentukan Jadwal",
            style = DiajakDesignSystem.Typography.Headline,
            color = MaterialTheme.colorScheme.onSurface,
            textAlign = TextAlign.Center,
            modifier = Modifier
              .align(Alignment.Center)
              .fillMaxWidth()
              .padding(horizontal = 48.dp),
            maxLines = 1,
            overflow = TextOverflow.Ellipsis
          )
        }
      }
    }

    // Sticky BOTTOM BAR with Solid White Background for Simpan Jadwal
    Surface(
      modifier = Modifier
        .align(Alignment.BottomCenter)
        .fillMaxWidth()
        .zIndex(5f),
      color = Color.Transparent,
      shadowElevation = 0.dp
    ) {
      Button(
        onClick = {
          onScheduleChange(liveScheduleString)
          onBack()
        },
        colors = ButtonDefaults.buttonColors(containerColor = DiajakOrange),
        modifier = Modifier
          .fillMaxWidth()
          .padding(start = 20.dp, end = 20.dp, top = MaterialTheme.spacing.medium, bottom = 20.dp)
          .navigationBarsPadding()
          .height(52.dp),
        shape = RoundedCornerShape(16.dp)
      ) {
        Text("Simpan", color = Color.White, style = androidx.compose.ui.text.TextStyle(fontSize = 16.sp, fontWeight = androidx.compose.ui.text.font.FontWeight.Bold, fontFamily = com.example.ui.theme.AppFontFamily))
      }
    }
  }
}

@Composable
fun AturWaktuContent(
  currentYearMonth: java.time.YearMonth,
  onCurrentYearMonthChange: (java.time.YearMonth) -> Unit,
  selectedDates: Set<java.time.LocalDate>,
  onSelectedDatesChange: (Set<java.time.LocalDate>) -> Unit,
  startHour: Int,
  onStartHourChange: (Int) -> Unit,
  startMinute: Int,
  onStartMinuteChange: (Int) -> Unit,
  endHour: Int,
  onEndHourChange: (Int) -> Unit,
  endMinute: Int,
  onEndMinuteChange: (Int) -> Unit,
  today: java.time.LocalDate,
  monthNamesIndo: List<String>,
  hoursList: List<String>,
  minutesList: List<String>,
  showSpacers: Boolean = false,
  columnModifier: Modifier = Modifier
) {
  Column(
    modifier = columnModifier,
    horizontalAlignment = Alignment.CenterHorizontally
  ) {
    // Calendar Container Card
    Surface(
      modifier = Modifier
        .fillMaxWidth(),
      shape = RoundedCornerShape(20.dp),
      color = Color.White,
      shadowElevation = 0.dp,
      border = BorderStroke(0.dp, Color.Transparent)
    ) {
      Column(modifier = Modifier.padding(20.dp)
              ) {
        // Month/Year Switcher Header
        Row(
          modifier = Modifier.fillMaxWidth(),
          horizontalArrangement = Arrangement.SpaceBetween,
          verticalAlignment = Alignment.CenterVertically
        ) {
          val canGoToPrevMonth = currentYearMonth.isAfter(java.time.YearMonth.of(today.year, today.monthValue))
          IconButton(
            onClick = { if (canGoToPrevMonth) onCurrentYearMonthChange(currentYearMonth.minusMonths(1)) },
            enabled = canGoToPrevMonth
          ) {
            Icon(
              imageVector = Icons.AutoMirrored.Outlined.KeyboardArrowLeft,
              contentDescription = "Bulan Sebelumnya",
              tint = if (canGoToPrevMonth) MaterialTheme.colorScheme.onSurface else Color(0xFFCBD5E0)
            )
          }

          Column(horizontalAlignment = Alignment.CenterHorizontally) {
            Text(
              text = "${monthNamesIndo[currentYearMonth.monthValue - 1]} ${currentYearMonth.year}",
              style = com.example.ui.theme.DiajakDesignSystem.Typography.Body,
              color = MaterialTheme.colorScheme.onSurface
            )
            if (selectedDates.isNotEmpty() ){
              Text(
                text = "${selectedDates.size} Tanggal Terpilih",
                style = androidx.compose.ui.text.TextStyle(fontSize = 16.sp, fontWeight = androidx.compose.ui.text.font.FontWeight.Bold, fontFamily = com.example.ui.theme.AppFontFamily),
                color = DiajakOrange,
                
              )
            }
          }

          IconButton(
            onClick = { onCurrentYearMonthChange(currentYearMonth.plusMonths(1)) }
          ) {
            Icon(
              imageVector = Icons.AutoMirrored.Outlined.KeyboardArrowRight,
              contentDescription = "Bulan Berikutnya",
              tint = MaterialTheme.colorScheme.onSurface
            )
          }
        }

        Spacer(modifier = Modifier.height(MaterialTheme.spacing.medium))

        // Days of Week Header
        Row(
          modifier = Modifier.fillMaxWidth(),
          horizontalArrangement = Arrangement.SpaceBetween
        ) {
          val daysOfWeek = listOf("Sen", "Sel", "Rab", "Kam", "Jum", "Sab", "Min")
          daysOfWeek.forEach { dayNameStr ->
            Text(
              text = dayNameStr,
              style = com.example.ui.theme.DiajakDesignSystem.Typography.Body,
              color = MaterialTheme.colorScheme.onSurface,
              modifier = Modifier.weight(1f),
              textAlign = androidx.compose.ui.text.style.TextAlign.Center
            )
          }
        }

        Spacer(modifier = Modifier.height(MaterialTheme.spacing.small))

        // Calendar Grid (Always exactly 6 weeks to maintain fixed height)
        val firstDayOfWeek = currentYearMonth.atDay(1).dayOfWeek.value // 1 = Monday, ..., 7 = Sunday
        val daysInMonth = currentYearMonth.lengthOfMonth()

        Column(verticalArrangement = Arrangement.spacedBy(AppSpacing.ExtraSmall) ){
          for (week in 0 until 6) {
            Row(
              modifier = Modifier.fillMaxWidth(),
              horizontalArrangement = Arrangement.SpaceBetween
            ) {
              for (dayIdx in 0 until 7) {
                val slotIdx = week * 7 + dayIdx
                val dayNum = slotIdx - (firstDayOfWeek - 2)

                if (dayNum in 1..daysInMonth) {
                  val cellDate = currentYearMonth.atDay(dayNum)
                  val isSelectable = !cellDate.isBefore(today)
                  val isSelected = selectedDates.contains(cellDate)

                  Box(
                    modifier = Modifier
                      .weight(1f)
                      .aspectRatio(1f)
                      .padding(2.dp)
                      .clip(CircleShape)
                      .background(
                        when {
                          isSelected -> DiajakOrange
                          else -> Color.Transparent
                        }
                      )
                      .clickable(enabled = isSelectable) {
                        if (selectedDates.contains(cellDate)) {
                          onSelectedDatesChange(selectedDates - cellDate)
                        } else {
                          onSelectedDatesChange(selectedDates + cellDate)
                        }
                      },
                    contentAlignment = Alignment.Center
                  ) {
                    Text(
                      text = dayNum.toString(),
                      style = com.example.ui.theme.DiajakDesignSystem.Typography.Body,
                      color = when {
                        isSelected -> Color.White
                        !isSelectable -> Color(0xFFCBD5E0)
                        cellDate.dayOfWeek.value >= 6 -> DiajakOrange.copy(alpha = 0.8f) // highlight weekends
                        else -> MaterialTheme.colorScheme.onSurface
                      }
                    )
                  }
                } else {
                  Spacer(modifier = Modifier.weight(1f).aspectRatio(1f))
                }
              }
            }
          }
        }
      }
    }

    Spacer(modifier = Modifier.weight(1f))

    // Time Adjuster Container
    Column(
      modifier = Modifier.fillMaxWidth(),
      verticalArrangement = Arrangement.spacedBy(AppSpacing.Medium)
    ) {
        Row(
          modifier = Modifier.fillMaxWidth(),
          horizontalArrangement = Arrangement.spacedBy(MaterialTheme.spacing.medium)
        ) {
          // Jam Mulai Section
          Column(
            modifier = Modifier.weight(1f),
            horizontalAlignment = Alignment.CenterHorizontally
          ) {
            Row(
              verticalAlignment = Alignment.CenterVertically,
              horizontalArrangement = Arrangement.Center,
              modifier = Modifier
                .fillMaxWidth()
                .padding(bottom = MaterialTheme.spacing.small)
            ) {
              Icon(
                imageVector = Icons.Outlined.AccessTime,
                contentDescription = null,
                tint = MaterialTheme.colorScheme.onSurface,
                modifier = Modifier.size(18.dp)
              )
              Spacer(modifier = Modifier.width(6.dp))
              Text(
                text = "Jam Mulai",
                style = com.example.ui.theme.DiajakDesignSystem.Typography.Body,
                color = MaterialTheme.colorScheme.onSurface,
                fontWeight = FontWeight.Medium
              )
            }

            Row(
              verticalAlignment = Alignment.CenterVertically,
              horizontalArrangement = Arrangement.Center,
              modifier = Modifier
                .fillMaxWidth()
                .height(110.dp)
            ) {
              Box(
                modifier = Modifier.width(60.dp),
                contentAlignment = Alignment.Center
              ) {
                WheelPicker(
                  items = hoursList,
                  initialIndex = startHour,
                  onItemSelected = onStartHourChange,
                  modifier = Modifier.fillMaxSize(),
                  visibleItemsCount = 3,
                  backgroundColor = androidx.compose.ui.graphics.Color(0xFFE5E7EB)
                )
              }

              Text(
                text = ":",
                style = com.example.ui.theme.DiajakDesignSystem.Typography.Body,
                color = DiajakOrange,
                modifier = Modifier.padding(horizontal = MaterialTheme.spacing.small)
              )

              Box(
                modifier = Modifier.width(60.dp),
                contentAlignment = Alignment.Center
              ) {
                WheelPicker(
                  items = minutesList,
                  initialIndex = startMinute,
                  onItemSelected = onStartMinuteChange,
                  modifier = Modifier.fillMaxSize(),
                  visibleItemsCount = 3,
                  backgroundColor = androidx.compose.ui.graphics.Color(0xFFE5E7EB)
                )
              }
            }
          }

          // Jam Selesai Section
          Column(
            modifier = Modifier.weight(1f),
            horizontalAlignment = Alignment.CenterHorizontally
          ) {
            Row(
              verticalAlignment = Alignment.CenterVertically,
              horizontalArrangement = Arrangement.Center,
              modifier = Modifier
                .fillMaxWidth()
                .padding(bottom = MaterialTheme.spacing.small)
            ) {
              Icon(
                imageVector = Icons.Outlined.AccessTime,
                contentDescription = null,
                tint = MaterialTheme.colorScheme.onSurface,
                modifier = Modifier.size(18.dp)
              )
              Spacer(modifier = Modifier.width(6.dp))
              Text(
                text = "Jam Selesai",
                style = com.example.ui.theme.DiajakDesignSystem.Typography.Body,
                color = MaterialTheme.colorScheme.onSurface,
                fontWeight = FontWeight.Medium
              )
            }

            Row(
              verticalAlignment = Alignment.CenterVertically,
              horizontalArrangement = Arrangement.Center,
              modifier = Modifier
                .fillMaxWidth()
                .height(110.dp)
            ) {
              Box(
                modifier = Modifier.width(60.dp),
                contentAlignment = Alignment.Center
              ) {
                WheelPicker(
                  items = hoursList,
                  initialIndex = endHour,
                  onItemSelected = onEndHourChange,
                  modifier = Modifier.fillMaxSize(),
                  visibleItemsCount = 3,
                  backgroundColor = androidx.compose.ui.graphics.Color(0xFFE5E7EB)
                )
              }

              Text(
                text = ":",
                style = com.example.ui.theme.DiajakDesignSystem.Typography.Body,
                color = DiajakOrange,
                modifier = Modifier.padding(horizontal = MaterialTheme.spacing.small)
              )

              Box(
                modifier = Modifier.width(60.dp),
                contentAlignment = Alignment.Center
              ) {
                WheelPicker(
                  items = minutesList,
                  initialIndex = endMinute,
                  onItemSelected = onEndMinuteChange,
                  modifier = Modifier.fillMaxSize(),
                  visibleItemsCount = 3,
                  backgroundColor = androidx.compose.ui.graphics.Color(0xFFE5E7EB)
                )
              }
            }
          }
        }
      }

    Spacer(modifier = Modifier.weight(1f))
  }
}

@Composable
fun WheelPicker(
  items: List<String>,
  initialIndex: Int,
  onItemSelected: (Int) -> Unit,
  modifier: Modifier = Modifier,
  visibleItemsCount: Int = 3,
  itemHeight: androidx.compose.ui.unit.Dp = 36.dp,
  backgroundColor: Color = androidx.compose.ui.graphics.Color(0xFFE5E7EB)
) {
  val paddedItems = remember(items) {
    val padding = List(visibleItemsCount / 2) { "" }
    padding + items + padding
  }
  
  val listState = rememberLazyListState(initialFirstVisibleItemIndex = initialIndex)
  val coroutineScope = rememberCoroutineScope()
  
  var selectedIndex by remember { mutableStateOf(initialIndex) }
  
  LaunchedEffect(initialIndex) {
    if (!listState.isScrollInProgress && selectedIndex != initialIndex) {
      selectedIndex = initialIndex
      listState.scrollToItem(initialIndex)
    }
  }
  
  LaunchedEffect(listState) {
    androidx.compose.runtime.snapshotFlow { 
      val firstVisible = listState.firstVisibleItemIndex
      val offset = listState.firstVisibleItemScrollOffset
      val layoutInfo = listState.layoutInfo
      val visibleItems = layoutInfo.visibleItemsInfo
      if (visibleItems.isNotEmpty()) {
        val itemHeightPx = visibleItems.first().size
        if (itemHeightPx > 0) {
          val added = if (offset > itemHeightPx / 2) 1 else 0
          (firstVisible + added).coerceIn(0, items.size - 1)
        } else {
          firstVisible.coerceIn(0, items.size - 1)
        }
      } else {
        firstVisible.coerceIn(0, items.size - 1)
      }
    }
    .distinctUntilChanged()
    .collect { index ->
      selectedIndex = index
      onItemSelected(index)
    }
  }
  
  LaunchedEffect(listState.isScrollInProgress) {
    if (!listState.isScrollInProgress) {
      listState.animateScrollToItem(selectedIndex)
    }
  }
  
  Box(
    modifier = modifier
      .height(itemHeight * visibleItemsCount)
      .background(backgroundColor, RoundedCornerShape(12.dp)),
    contentAlignment = Alignment.Center
  ) {
    // Highlight indicator
    Box(
      modifier = Modifier
        .fillMaxWidth()
        .height(itemHeight)
        .background(MaterialTheme.colorScheme.onSurface.copy(alpha = 0.05f), RoundedCornerShape(8.dp))
        .border(0.dp, Color.Transparent, RoundedCornerShape(8.dp))
    )
    
    LazyColumn(
      state = listState,
      modifier = Modifier.fillMaxSize(),
      horizontalAlignment = Alignment.CenterHorizontally
    ) {
      items(paddedItems.size) { index ->
        val itemOffset = visibleItemsCount / 2
        val isSelected = (selectedIndex + itemOffset) == index
        Box(
          modifier = Modifier
            .fillMaxWidth()
            .height(itemHeight)
            .clickable(
              interactionSource = remember { androidx.compose.foundation.interaction.MutableInteractionSource() },
              indication = null
            ) {
              if (index >= itemOffset && index < paddedItems.size - itemOffset) {
                val targetIndex = index - itemOffset
                coroutineScope.launch {
                  listState.animateScrollToItem(targetIndex)
                  selectedIndex = targetIndex
                  onItemSelected(targetIndex)
                }
              }
            },
          contentAlignment = Alignment.Center
        ) {
          val text = paddedItems[index]
          if (text.isNotEmpty()) {
            Text(
              text = text,
              style = com.example.ui.theme.DiajakDesignSystem.Typography.Body,
              color = if (isSelected) DiajakOrange else MaterialTheme.colorScheme.onSurface
            )
          }
        }
      }
    }
  }
}

private fun parseScheduleToLocalDate(schedule: String): java.time.LocalDate? {
  return parseSingleDateToLocalDate(schedule.split("•").firstOrNull()?.trim() ?: "")
}

private fun parseSingleDateToLocalDate(dateStr: String): java.time.LocalDate? {
  if (dateStr.isBlank()) return null
  try {
    // Remove day name if it contains comma (e.g. "Sabtu, 12 Ags 2026" -> "12 Ags 2026")
    val cleanDatePart = if (dateStr.contains(",") ){
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

@Composable
fun KreatorServiceMenuItem(icon: androidx.compose.ui.graphics.vector.ImageVector, label: String, onClick: () -> Unit) {
  Surface(
    modifier = Modifier,
    shape = RoundedCornerShape(20.dp),
    color = Color.White,
    shadowElevation = 0.dp,
    border = BorderStroke(0.dp, Color.Transparent)
  ) {
    Row(
      modifier = Modifier
        .fillMaxWidth()
        .clickable { onClick() }
        .padding(20.dp),
      verticalAlignment = Alignment.CenterVertically
    ) {
      Icon(
        imageVector = icon,
        contentDescription = label,
        tint = MaterialTheme.colorScheme.onSurface
      )
      Spacer(modifier = Modifier.width(20.dp))
      Text(
        text = label,
        style = com.example.ui.theme.DiajakDesignSystem.Typography.Body,
        color = MaterialTheme.colorScheme.onSurface
      )
    }
  }
}


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SlideInKategoriScreen(
  categories: List<String>,
  selectedCategory: String,
  onSelect: (String) -> Unit,
  onBack: () -> Unit
) {
  val focusManager = androidx.compose.ui.platform.LocalFocusManager.current
  var tempSelectedCategory by remember { mutableStateOf(selectedCategory) }
  val hazeState = remember { HazeState() }
  val listState = rememberLazyListState()

  Box(
    modifier = Modifier
      .fillMaxSize()
      .imePadding()
      .pointerInput(Unit) {
        detectTapGestures(
          onTap = {
            focusManager.clearFocus()
          }
        )
      }
      .background(androidx.compose.ui.graphics.Color(0xFFE5E7EB))
  ) {
    // 1. Content Layer
    LazyColumn(
      state = listState,
      modifier = Modifier
        .fillMaxSize()
        .hazeSource(state = hazeState),
      contentPadding = PaddingValues(bottom = 100.dp)
    ) {
      item {
        Spacer(modifier = Modifier.statusBarsPadding().height(80.dp))
      }
      items(categories) { cat ->
        val isSelected = cat == tempSelectedCategory
        Column(
          modifier = Modifier
            .fillMaxWidth()
            .background(if (isSelected) Color(0xFFFFF5F5) else Color.Transparent)
            .clickable {
              focusManager.clearFocus()
              tempSelectedCategory = cat
            }
        ) {
          Row(
            modifier = Modifier
              .fillMaxWidth()
              .padding(vertical = AppSpacing.Medium, horizontal = AppSpacing.ScreenMargin),
            verticalAlignment = Alignment.CenterVertically
          ) {
            val categoryIcon = getCategoryIconVector(cat)
            Icon(
              imageVector = categoryIcon,
              contentDescription = cat,
              tint = if (isSelected) DiajakOrange else MaterialTheme.colorScheme.onSurface,
              modifier = Modifier.size(20.dp)
            )
            Spacer(modifier = Modifier.width(20.dp))
            Text(
              text = cat,
              style = com.example.ui.theme.DiajakDesignSystem.Typography.Body,
              fontWeight = if (isSelected) FontWeight.Bold else FontWeight.Medium,
              color = if (isSelected) DiajakOrange else MaterialTheme.colorScheme.onSurface,
              modifier = Modifier.weight(1f)
            )
            if (isSelected) {
               Icon(Icons.Outlined.Check, contentDescription = null, tint = DiajakOrange, modifier = Modifier.size(20.dp))
            }
          }
        }
      }
    }

    // 2. Glass Header Layer
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
          .padding(horizontal = 20.dp)
      ) {
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

        Text(
          text = "Tentukan Kategori",
          style = DiajakDesignSystem.Typography.Headline,
          color = MaterialTheme.colorScheme.onSurface,
          textAlign = TextAlign.Center,
          modifier = Modifier
            .align(Alignment.Center)
            .fillMaxWidth()
            .padding(horizontal = 48.dp),
          maxLines = 1,
          overflow = TextOverflow.Ellipsis
        )
      }
    }

    // Sticky BOTTOM BAR with Solid White Background to prevent overlap and make button borders fully visible
    Surface(
      modifier = Modifier
        .align(Alignment.BottomCenter)
        .fillMaxWidth()
        .zIndex(5f),
      color = Color.Transparent,
      shadowElevation = 0.dp
    ) {
      Button(
        onClick = {
          focusManager.clearFocus()
          onSelect(tempSelectedCategory)
        },
        colors = ButtonDefaults.buttonColors(containerColor = DiajakOrange),
        modifier = Modifier
          .fillMaxWidth()
          .padding(start = 20.dp, end = 20.dp, top = MaterialTheme.spacing.medium, bottom = 20.dp)
          .navigationBarsPadding()
          .height(52.dp),
        shape = RoundedCornerShape(16.dp)
      ) {
        Text("Simpan", color = Color.White, style = androidx.compose.ui.text.TextStyle(fontSize = 16.sp, fontWeight = androidx.compose.ui.text.font.FontWeight.Bold, fontFamily = com.example.ui.theme.AppFontFamily))
      }
    }
  }
}


@Composable
fun ShopeeFormRow(
  icon: androidx.compose.ui.graphics.vector.ImageVector,
  iconColor: androidx.compose.ui.graphics.Color,
  label: String,
  isRequired: Boolean = false,
  valueText: String,
  onClick: () -> Unit
) {
  Column {
    Row(
      modifier = androidx.compose.ui.Modifier
        .fillMaxWidth()
        .clickable { onClick() }
        .padding(horizontal = 20.dp, vertical = MaterialTheme.spacing.medium),
      verticalAlignment = Alignment.CenterVertically
    ) {
      Row(modifier = androidx.compose.ui.Modifier.weight(1f), verticalAlignment = Alignment.CenterVertically) {
        Icon(
          imageVector = icon,
          contentDescription = label,
          tint = iconColor,
          modifier = androidx.compose.ui.Modifier.size(22.dp)
        )
        Spacer(modifier = androidx.compose.ui.Modifier.width(20.dp))
        Text(
          text = label,
          style = com.example.ui.theme.DiajakDesignSystem.Typography.Body,
          color = MaterialTheme.colorScheme.onSurface
        )
        if (isRequired) {
          Spacer(modifier = androidx.compose.ui.Modifier.width(4.dp))
          Text("*", color = androidx.compose.ui.graphics.Color.Red, style = androidx.compose.ui.text.TextStyle(fontSize = 16.sp, fontWeight = androidx.compose.ui.text.font.FontWeight.Bold, fontFamily = com.example.ui.theme.AppFontFamily))
        }
      }
      Spacer(modifier = androidx.compose.ui.Modifier.width(20.dp))
      Box(modifier = androidx.compose.ui.Modifier.weight(1f)) {
        Text(
          text = valueText,
          style = com.example.ui.theme.DiajakDesignSystem.Typography.Body,
          color = if (valueText.startsWith("Atur") || valueText.startsWith("Pilih") || valueText.startsWith("Tentukan") || valueText == "Pilih Kategori" || valueText == "Pilih Tempat" || valueText == "Pilih Benefit" || valueText == "Pilih Jadwal") MaterialTheme.colorScheme.onSurface else DiajakOrange,
          fontWeight = FontWeight.Medium,
          maxLines = 1,
          overflow = androidx.compose.ui.text.style.TextOverflow.Ellipsis
        )
      }
    }
  }
}


@Composable
fun ShopeeFormGridItem(
  icon: androidx.compose.ui.graphics.vector.ImageVector,
  iconColor: androidx.compose.ui.graphics.Color,
  label: String,
  isRequired: Boolean = false,
  valueText: String,
  onClick: () -> Unit,
  modifier: Modifier = Modifier
) {
  Surface(
    modifier = modifier
      .clickable { onClick() },
    shape = RoundedCornerShape(20.dp),
    color = Color.White,
    shadowElevation = 0.dp,
    border = BorderStroke(0.dp, Color.Transparent)
  ) {
    Column(
      modifier = Modifier
        .fillMaxWidth()
        .padding(horizontal = 20.dp, vertical = MaterialTheme.spacing.medium),
      verticalArrangement = Arrangement.spacedBy(AppSpacing.Small)
    ) {
      Row(
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.spacedBy(MaterialTheme.spacing.small)
      ) {
        Icon(
          imageVector = icon,
          contentDescription = label,
          tint = iconColor,
          modifier = Modifier.size(20.dp)
        )
        Text(
          text = label,
          style = androidx.compose.ui.text.TextStyle(fontSize = 16.sp, fontWeight = androidx.compose.ui.text.font.FontWeight.Bold, fontFamily = com.example.ui.theme.AppFontFamily),
          
          color = MaterialTheme.colorScheme.onSurface
        )
        if (isRequired) {
          Text("*", color = Color.Red, style = androidx.compose.ui.text.TextStyle(fontSize = 16.sp, fontWeight = androidx.compose.ui.text.font.FontWeight.Bold, fontFamily = com.example.ui.theme.AppFontFamily))
        }
      }
      Text(
        text = valueText,
        style = com.example.ui.theme.DiajakDesignSystem.Typography.Body,
        color = if (valueText.startsWith("Atur") || valueText.startsWith("Pilih")) {
          MaterialTheme.colorScheme.onSurface
        } else {
          DiajakOrange
        },
        fontWeight = if (valueText.startsWith("Atur") || valueText.startsWith("Pilih")) {
          FontWeight.Medium
        } else {
          FontWeight.Medium
        },
        maxLines = 1,
        overflow = androidx.compose.ui.text.style.TextOverflow.Ellipsis
      )
    }
  }
}


@OptIn(androidx.compose.material3.ExperimentalMaterial3Api::class)
@Composable
fun KreatorPendaftaranScreen(
  pendaftaranList: List<com.example.ui.viewmodel.PesertaPendaftaranModel>,
  onBack: () -> Unit,
  onConfirm: (String) -> Unit,
  onComplete: (String) -> Unit
) {
  val hazeState = remember { HazeState() }
  val listState = rememberLazyListState()

  Box(
    modifier = Modifier
      .fillMaxSize()
      .background(Color(0xFFE5E7EB))
  ) {
    // 1. Content Layer
    LazyColumn(
      state = listState,
      modifier = Modifier
        .fillMaxSize()
        .hazeSource(state = hazeState),
      contentPadding = PaddingValues(start = 20.dp, end = 20.dp, bottom = 24.dp),
      verticalArrangement = Arrangement.spacedBy(AppSpacing.Medium)
    ) {
      item {
        Spacer(modifier = Modifier.statusBarsPadding().height(80.dp))
      }
      if (pendaftaranList.isEmpty()) {
        item {
          Card(
            modifier = Modifier.fillMaxWidth(),
            shape = RoundedCornerShape(16.dp),
            colors = CardDefaults.cardColors(containerColor = Color.White),
            elevation = CardDefaults.cardElevation(0.dp)
          ) {
            Box(
              modifier = Modifier
                .fillMaxWidth()
                .padding(32.dp),
              contentAlignment = Alignment.Center
            ) {
              Text(
                "Belum ada pendaftaran masuk",
                style = com.example.ui.theme.DiajakDesignSystem.Typography.Body,
                color = MaterialTheme.colorScheme.onSurfaceVariant
              )
            }
          }
        }
      } else {
        itemsIndexed(pendaftaranList) { idx, p ->
          Card(
            modifier = Modifier.fillMaxWidth(),
            shape = RoundedCornerShape(16.dp),
            colors = CardDefaults.cardColors(containerColor = Color.White),
            elevation = CardDefaults.cardElevation(0.dp)
          ) {
            Column(
              modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp)
            ) {
              Text(p.pesertaName, style = com.example.ui.theme.DiajakDesignSystem.Typography.BodyBold)
              Spacer(modifier = Modifier.height(MaterialTheme.spacing.small))
              Text(p.activityTitle, style = com.example.ui.theme.DiajakDesignSystem.Typography.Body, color = Color(0xFF262626))
              Spacer(modifier = Modifier.height(MaterialTheme.spacing.small))
              Text(
                "Status: ${p.status}",
                style = com.example.ui.theme.DiajakDesignSystem.Typography.Body,
                color = if (p.status == "Selesai") Color(0xFF16A34A) else DiajakOrange,
                fontWeight = FontWeight.Medium
              )
              if (p.status == "Menunggu Verifikasi") {
                Spacer(modifier = Modifier.height(MaterialTheme.spacing.medium))
                Button(
                  onClick = { onConfirm(p.id) },
                  colors = ButtonDefaults.buttonColors(containerColor = DiajakOrange),
                  modifier = Modifier.fillMaxWidth().height(42.dp),
                  shape = RoundedCornerShape(12.dp)
                ) {
                  Text("Konfirmasi", style = androidx.compose.ui.text.TextStyle(fontSize = 15.sp, fontWeight = FontWeight.Bold, fontFamily = com.example.ui.theme.AppFontFamily))
                }
              } else if (p.status == "Terkonfirmasi") {
                Spacer(modifier = Modifier.height(MaterialTheme.spacing.medium))
                Button(
                  onClick = { onComplete(p.id) },
                  colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF4CAF50)),
                  modifier = Modifier.fillMaxWidth().height(42.dp),
                  shape = RoundedCornerShape(12.dp)
                ) {
                  Text("Selesaikan", style = androidx.compose.ui.text.TextStyle(fontSize = 15.sp, fontWeight = FontWeight.Bold, fontFamily = com.example.ui.theme.AppFontFamily))
                }
              }
            }
          }
        }
      }
    }

    // 2. Glass Header Layer
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
          .padding(horizontal = 20.dp)
      ) {
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

        Text(
          text = "Kelola Pendaftaran",
          style = DiajakDesignSystem.Typography.Headline,
          color = MaterialTheme.colorScheme.onSurface,
          textAlign = TextAlign.Center,
          modifier = Modifier
            .align(Alignment.Center)
            .fillMaxWidth()
            .padding(horizontal = 48.dp),
          maxLines = 1,
          overflow = TextOverflow.Ellipsis
        )
      }
    }
  }
}

@OptIn(androidx.compose.material3.ExperimentalMaterial3Api::class)
@Composable
fun KreatorFinanceScreen(
  balance: Int,
  transactions: List<com.example.ui.viewmodel.TransactionModel>,
  onBack: () -> Unit,
  onNavigateToWithdraw: () -> Unit
) {
  val hazeState = remember { HazeState() }
  val scrollState = rememberScrollState()

  Box(
    modifier = Modifier
      .fillMaxSize()
      .background(Color(0xFFE5E7EB))
  ) {
    // 1. Content Layer
    Column(
      modifier = Modifier
        .fillMaxSize()
        .hazeSource(state = hazeState)
        .verticalScroll(scrollState)
        .padding(horizontal = 20.dp),
      verticalArrangement = Arrangement.spacedBy(AppSpacing.Medium)
    ) {
      Spacer(modifier = Modifier.statusBarsPadding().height(80.dp))

      Card(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(16.dp),
        colors = CardDefaults.cardColors(containerColor = DiajakOrange),
        elevation = CardDefaults.cardElevation(0.dp)
      ) {
        Column(
          modifier = Modifier
            .fillMaxWidth()
            .padding(MaterialTheme.spacing.large)
        ) {
          Text("Total Pendapatan", color = Color.White, style = com.example.ui.theme.DiajakDesignSystem.Typography.Body)
          Spacer(modifier = Modifier.height(MaterialTheme.spacing.extraSmall))
          Text(
            "Rp ${String.format("%,d", balance).replace(',', '.')}",
            color = Color.White,
            style = androidx.compose.ui.text.TextStyle(fontSize = 22.sp, fontWeight = FontWeight.Bold, fontFamily = com.example.ui.theme.AppFontFamily)
          )
          Spacer(modifier = Modifier.height(MaterialTheme.spacing.medium))
          OutlinedButton(
            onClick = onNavigateToWithdraw,
            colors = ButtonDefaults.outlinedButtonColors(containerColor = Color.White),
            border = BorderStroke(1.5.dp, DiajakOrange),
            shape = RoundedCornerShape(16.dp)
          ) {
            Text("Tarik Saldo", color = DiajakOrange, style = androidx.compose.ui.text.TextStyle(fontSize = 15.sp, fontWeight = FontWeight.Bold, fontFamily = com.example.ui.theme.AppFontFamily))
          }
        }
      }

      Text("Riwayat Transaksi", style = DiajakDesignSystem.Typography.TitleBold, color = MaterialTheme.colorScheme.onSurface)

      Card(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(16.dp),
        colors = CardDefaults.cardColors(containerColor = Color.White),
        elevation = CardDefaults.cardElevation(0.dp)
      ) {
        Column(
          modifier = Modifier
            .fillMaxWidth()
            .padding(16.dp),
          verticalArrangement = Arrangement.spacedBy(AppSpacing.Medium)
        ) {
          if (transactions.isEmpty()) {
            Text(
              "Belum ada riwayat transaksi",
              style = com.example.ui.theme.DiajakDesignSystem.Typography.Body,
              color = MaterialTheme.colorScheme.onSurfaceVariant
            )
          } else {
            transactions.forEachIndexed { idx, t ->
              Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
              ) {
                Column(modifier = Modifier.weight(1f)) {
                  Text(t.title, style = com.example.ui.theme.DiajakDesignSystem.Typography.BodyBold, maxLines = 1, overflow = TextOverflow.Ellipsis)
                  Spacer(modifier = Modifier.height(MaterialTheme.spacing.extraSmall))
                  Text(t.date, style = com.example.ui.theme.DiajakDesignSystem.Typography.Body, color = Color(0xFF64748B), fontSize = 13.sp)
                }
                Spacer(modifier = Modifier.width(12.dp))
                Text(
                  if (t.type == "Income") "+ Rp ${t.amount}" else "- Rp ${t.amount}",
                  color = if (t.type == "Income") Color(0xFF16A34A) else Color.Red,
                  style = com.example.ui.theme.DiajakDesignSystem.Typography.BodyBold
                )
              }
              if (idx < transactions.lastIndex) {
                HorizontalDivider(color = Color(0xFFF1F5F9), thickness = 1.dp)
              }
            }
          }
        }
      }

      Spacer(modifier = Modifier.navigationBarsPadding().height(24.dp))
    }

    // 2. Glass Header Layer
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
          .padding(horizontal = 20.dp)
      ) {
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

        Text(
          text = "Kelola Saldo",
          style = DiajakDesignSystem.Typography.Headline,
          color = MaterialTheme.colorScheme.onSurface,
          textAlign = TextAlign.Center,
          modifier = Modifier
            .align(Alignment.Center)
            .fillMaxWidth()
            .padding(horizontal = 48.dp),
          maxLines = 1,
          overflow = TextOverflow.Ellipsis
        )
      }
    }
  }
}

@OptIn(androidx.compose.material3.ExperimentalMaterial3Api::class)
@Composable
fun KreatorWithdrawScreen(
  balance: Int,
  onBack: () -> Unit,
  onWithdraw: (Int, String, String) -> Unit
) {
  var amount by remember { mutableStateOf("") }
  var bank by remember { mutableStateOf("") }
  var account by remember { mutableStateOf("") }
  val hazeState = remember { HazeState() }
  val scrollState = rememberScrollState()

  Box(
    modifier = Modifier
      .fillMaxSize()
      .background(Color(0xFFE5E7EB))
  ) {
    // 1. Content Layer
    Column(
      modifier = Modifier
        .fillMaxSize()
        .hazeSource(state = hazeState)
        .verticalScroll(scrollState)
        .padding(horizontal = 20.dp),
      verticalArrangement = Arrangement.spacedBy(AppSpacing.Medium)
    ) {
      Spacer(modifier = Modifier.statusBarsPadding().height(80.dp))

      Card(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(16.dp),
        colors = CardDefaults.cardColors(containerColor = Color.White),
        elevation = CardDefaults.cardElevation(0.dp)
      ) {
        Column(
          modifier = Modifier
            .fillMaxWidth()
            .padding(16.dp),
          verticalArrangement = Arrangement.spacedBy(AppSpacing.Medium)
        ) {
          OutlinedTextField(
            value = amount,
            onValueChange = { amount = it },
            label = { Text("Jumlah Penarikan") },
            modifier = Modifier.fillMaxWidth(),
            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
            shape = RoundedCornerShape(12.dp),
            colors = OutlinedTextFieldDefaults.colors(
              focusedTextColor = MaterialTheme.colorScheme.onSurface,
              unfocusedTextColor = MaterialTheme.colorScheme.onSurface,
              focusedBorderColor = DiajakOrange,
              unfocusedBorderColor = Color(0xFFE2E8F0),
              cursorColor = DiajakOrange
            )
          )
          OutlinedTextField(
            value = bank,
            onValueChange = { bank = it },
            label = { Text("Nama Bank") },
            modifier = Modifier.fillMaxWidth(),
            shape = RoundedCornerShape(12.dp),
            colors = OutlinedTextFieldDefaults.colors(
              focusedTextColor = MaterialTheme.colorScheme.onSurface,
              unfocusedTextColor = MaterialTheme.colorScheme.onSurface,
              focusedBorderColor = DiajakOrange,
              unfocusedBorderColor = Color(0xFFE2E8F0),
              cursorColor = DiajakOrange
            )
          )
          OutlinedTextField(
            value = account,
            onValueChange = { account = it },
            label = { Text("Nomor Rekening") },
            modifier = Modifier.fillMaxWidth(),
            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
            shape = RoundedCornerShape(12.dp),
            colors = OutlinedTextFieldDefaults.colors(
              focusedTextColor = MaterialTheme.colorScheme.onSurface,
              unfocusedTextColor = MaterialTheme.colorScheme.onSurface,
              focusedBorderColor = DiajakOrange,
              unfocusedBorderColor = Color(0xFFE2E8F0),
              cursorColor = DiajakOrange
            )
          )
          Spacer(modifier = Modifier.height(MaterialTheme.spacing.extraSmall))
          Button(
            onClick = { onWithdraw(amount.toIntOrNull() ?: 0, bank, account) },
            modifier = Modifier.fillMaxWidth().height(50.dp),
            colors = ButtonDefaults.buttonColors(containerColor = DiajakOrange),
            shape = RoundedCornerShape(12.dp)
          ) {
            Text("Tarik Sekarang", style = androidx.compose.ui.text.TextStyle(fontSize = 15.sp, fontWeight = FontWeight.Bold, fontFamily = com.example.ui.theme.AppFontFamily))
          }
        }
      }

      Spacer(modifier = Modifier.navigationBarsPadding().height(24.dp))
    }

    // 2. Glass Header Layer
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
          .padding(horizontal = 20.dp)
      ) {
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

        Text(
          text = "Tarik Saldo",
          style = DiajakDesignSystem.Typography.Headline,
          color = MaterialTheme.colorScheme.onSurface,
          textAlign = TextAlign.Center,
          modifier = Modifier
            .align(Alignment.Center)
            .fillMaxWidth()
            .padding(horizontal = 48.dp),
          maxLines = 1,
          overflow = TextOverflow.Ellipsis
        )
      }
    }
  }
}

@OptIn(androidx.compose.material3.ExperimentalMaterial3Api::class)
@Composable
fun KreatorPerformanceScreen(
  activities: List<com.example.model.ActivityModel>,
  onBack: () -> Unit
) {
  val hazeState = remember { HazeState() }
  val listState = rememberLazyListState()

  Box(
    modifier = Modifier
      .fillMaxSize()
      .background(Color(0xFFE5E7EB))
  ) {
    // 1. Content Layer
    LazyColumn(
      state = listState,
      modifier = Modifier
        .fillMaxSize()
        .hazeSource(state = hazeState),
      contentPadding = PaddingValues(start = 20.dp, end = 20.dp, bottom = 24.dp),
      verticalArrangement = Arrangement.spacedBy(AppSpacing.Medium)
    ) {
      item {
        Spacer(modifier = Modifier.statusBarsPadding().height(80.dp))
      }
      if (activities.isEmpty()) {
        item {
          Card(
            modifier = Modifier.fillMaxWidth(),
            shape = RoundedCornerShape(16.dp),
            colors = CardDefaults.cardColors(containerColor = Color.White),
            elevation = CardDefaults.cardElevation(0.dp)
          ) {
            Box(
              modifier = Modifier
                .fillMaxWidth()
                .padding(32.dp),
              contentAlignment = Alignment.Center
            ) {
              Text(
                "Belum ada ulasan aktivitas",
                style = com.example.ui.theme.DiajakDesignSystem.Typography.Body,
                color = MaterialTheme.colorScheme.onSurfaceVariant
              )
            }
          }
        }
      } else {
        itemsIndexed(activities) { idx, a ->
          Card(
            modifier = Modifier.fillMaxWidth(),
            shape = RoundedCornerShape(16.dp),
            colors = CardDefaults.cardColors(containerColor = Color.White),
            elevation = CardDefaults.cardElevation(0.dp)
          ) {
            Column(
              modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp)
            ) {
              Text(a.title, style = com.example.ui.theme.DiajakDesignSystem.Typography.BodyBold)
              Spacer(modifier = Modifier.height(MaterialTheme.spacing.small))
              Row(
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically,
                modifier = Modifier.fillMaxWidth()
              ) {
                Text("Peserta: Aktif", style = com.example.ui.theme.DiajakDesignSystem.Typography.Body, color = Color(0xFF64748B))
                Row(verticalAlignment = Alignment.CenterVertically) {
                  Icon(Icons.Outlined.Star, contentDescription = "Star", tint = Color(0xFFFFB300), modifier = Modifier.size(20.dp))
                  Spacer(modifier = Modifier.width(MaterialTheme.spacing.extraSmall))
                  Text(
                    "4.8/5.0",
                    style = androidx.compose.ui.text.TextStyle(fontSize = 15.sp, fontWeight = FontWeight.Bold, fontFamily = com.example.ui.theme.AppFontFamily),
                    color = MaterialTheme.colorScheme.onSurface
                  )
                }
              }
            }
          }
        }
      }
    }

    // 2. Glass Header Layer
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
          .padding(horizontal = 20.dp)
      ) {
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

        Text(
          text = "Kelola Ulasan",
          style = DiajakDesignSystem.Typography.Headline,
          color = MaterialTheme.colorScheme.onSurface,
          textAlign = TextAlign.Center,
          modifier = Modifier
            .align(Alignment.Center)
            .fillMaxWidth()
            .padding(horizontal = 48.dp),
          maxLines = 1,
          overflow = TextOverflow.Ellipsis
        )
      }
    }
  }
}

@OptIn(androidx.compose.material3.ExperimentalMaterial3Api::class)
@Composable
fun KreatorVouchersScreen(
  vouchers: List<com.example.ui.viewmodel.VoucherModel>,
  onBack: () -> Unit,
  onNavigateToCreate: () -> Unit
) {
  val hazeState = remember { HazeState() }
  val listState = rememberLazyListState()

  Box(
    modifier = Modifier
      .fillMaxSize()
      .background(Color(0xFFE5E7EB))
  ) {
    // 1. Content Layer
    LazyColumn(
      state = listState,
      modifier = Modifier
        .fillMaxSize()
        .hazeSource(state = hazeState),
      contentPadding = PaddingValues(start = 20.dp, end = 20.dp, bottom = 24.dp),
      verticalArrangement = Arrangement.spacedBy(AppSpacing.Medium)
    ) {
      item {
        Spacer(modifier = Modifier.statusBarsPadding().height(80.dp))
      }
      item {
        Button(
          onClick = onNavigateToCreate,
          modifier = Modifier.fillMaxWidth().height(50.dp),
          colors = ButtonDefaults.buttonColors(containerColor = DiajakOrange),
          shape = RoundedCornerShape(16.dp)
        ) {
          Text("Buat Voucher Baru", style = androidx.compose.ui.text.TextStyle(fontSize = 15.sp, fontWeight = FontWeight.Bold, fontFamily = com.example.ui.theme.AppFontFamily))
        }
      }
      if (vouchers.isEmpty()) {
        item {
          Card(
            modifier = Modifier.fillMaxWidth(),
            shape = RoundedCornerShape(16.dp),
            colors = CardDefaults.cardColors(containerColor = Color.White),
            elevation = CardDefaults.cardElevation(0.dp)
          ) {
            Box(
              modifier = Modifier
                .fillMaxWidth()
                .padding(32.dp),
              contentAlignment = Alignment.Center
            ) {
              Text(
                "Belum ada voucher",
                style = com.example.ui.theme.DiajakDesignSystem.Typography.Body,
                color = MaterialTheme.colorScheme.onSurfaceVariant
              )
            }
          }
        }
      } else {
        itemsIndexed(vouchers) { idx, v ->
          Card(
            modifier = Modifier.fillMaxWidth(),
            shape = RoundedCornerShape(16.dp),
            colors = CardDefaults.cardColors(containerColor = Color.White),
            elevation = CardDefaults.cardElevation(0.dp)
          ) {
            Column(
              modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp)
            ) {
              Text("Kode: ${v.code}", style = com.example.ui.theme.DiajakDesignSystem.Typography.BodyBold, color = DiajakOrange)
              Spacer(modifier = Modifier.height(MaterialTheme.spacing.extraSmall))
              Text("Diskon: Rp ${v.discountValue}", style = androidx.compose.ui.text.TextStyle(fontSize = 16.sp, fontWeight = FontWeight.Bold, fontFamily = com.example.ui.theme.AppFontFamily))
              Spacer(modifier = Modifier.height(MaterialTheme.spacing.extraSmall))
              Text("Min. Belanja: Rp ${v.minPurchase}", style = com.example.ui.theme.DiajakDesignSystem.Typography.Body, color = Color(0xFF64748B), fontSize = 13.sp)
            }
          }
        }
      }
    }

    // 2. Glass Header Layer
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
          .padding(horizontal = 20.dp)
      ) {
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

        Text(
          text = "Voucher Diskon",
          style = DiajakDesignSystem.Typography.Headline,
          color = MaterialTheme.colorScheme.onSurface,
          textAlign = TextAlign.Center,
          modifier = Modifier
            .align(Alignment.Center)
            .fillMaxWidth()
            .padding(horizontal = 48.dp),
          maxLines = 1,
          overflow = TextOverflow.Ellipsis
        )
      }
    }
  }
}

@OptIn(androidx.compose.material3.ExperimentalMaterial3Api::class)
@Composable
fun KreatorCreateVoucherScreen(
  onBack: () -> Unit,
  onSubmit: (String, Int, Int) -> Unit
) {
  var code by remember { mutableStateOf("") }
  var discount by remember { mutableStateOf("") }
  var minPurchase by remember { mutableStateOf("") }
  val hazeState = remember { HazeState() }
  val scrollState = rememberScrollState()

  val displayDiscount = if (discount.isNotEmpty()) {
    String.format("%,d", discount.toLongOrNull() ?: 0L).replace(',', '.')
  } else ""
  val displayMinPurchase = if (minPurchase.isNotEmpty()) {
    String.format("%,d", minPurchase.toLongOrNull() ?: 0L).replace(',', '.')
  } else ""

  Box(
    modifier = Modifier
      .fillMaxSize()
      .background(Color(0xFFE5E7EB))
  ) {
    // 1. Content Layer
    Column(
      modifier = Modifier
        .fillMaxSize()
        .hazeSource(state = hazeState)
        .verticalScroll(scrollState)
        .padding(horizontal = 20.dp),
      verticalArrangement = Arrangement.spacedBy(AppSpacing.Medium)
    ) {
      Spacer(modifier = Modifier.statusBarsPadding().height(80.dp))

      Card(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(16.dp),
        colors = CardDefaults.cardColors(containerColor = Color.White),
        elevation = CardDefaults.cardElevation(0.dp)
      ) {
        Column(
          modifier = Modifier
            .fillMaxWidth()
            .padding(16.dp),
          verticalArrangement = Arrangement.spacedBy(AppSpacing.Medium)
        ) {
          OutlinedTextField(
            value = code,
            onValueChange = { code = it.uppercase() },
            label = { Text("Kode Voucher") },
            modifier = Modifier.fillMaxWidth(),
            shape = RoundedCornerShape(12.dp),
            colors = OutlinedTextFieldDefaults.colors(
              focusedTextColor = MaterialTheme.colorScheme.onSurface,
              unfocusedTextColor = MaterialTheme.colorScheme.onSurface,
              focusedBorderColor = DiajakOrange,
              unfocusedBorderColor = Color(0xFFE2E8F0),
              cursorColor = DiajakOrange
            )
          )
          OutlinedTextField(
            value = displayDiscount,
            onValueChange = { discount = it.filter { ch -> ch.isDigit() } },
            label = { Text("Nominal Diskon") },
            modifier = Modifier.fillMaxWidth(),
            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
            shape = RoundedCornerShape(12.dp),
            colors = OutlinedTextFieldDefaults.colors(
              focusedTextColor = MaterialTheme.colorScheme.onSurface,
              unfocusedTextColor = MaterialTheme.colorScheme.onSurface,
              focusedBorderColor = DiajakOrange,
              unfocusedBorderColor = Color(0xFFE2E8F0),
              cursorColor = DiajakOrange
            )
          )
          OutlinedTextField(
            value = displayMinPurchase,
            onValueChange = { minPurchase = it.filter { ch -> ch.isDigit() } },
            label = { Text("Minimal Belanja") },
            modifier = Modifier.fillMaxWidth(),
            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
            shape = RoundedCornerShape(12.dp),
            colors = OutlinedTextFieldDefaults.colors(
              focusedTextColor = MaterialTheme.colorScheme.onSurface,
              unfocusedTextColor = MaterialTheme.colorScheme.onSurface,
              focusedBorderColor = DiajakOrange,
              unfocusedBorderColor = Color(0xFFE2E8F0),
              cursorColor = DiajakOrange
            )
          )
          Spacer(modifier = Modifier.height(MaterialTheme.spacing.extraSmall))
          Button(
            onClick = { onSubmit(code, discount.toIntOrNull() ?: 0, minPurchase.toIntOrNull() ?: 0) },
            modifier = Modifier.fillMaxWidth().height(50.dp),
            colors = ButtonDefaults.buttonColors(containerColor = DiajakOrange),
            shape = RoundedCornerShape(12.dp)
          ) {
            Text("Simpan Voucher", style = androidx.compose.ui.text.TextStyle(fontSize = 15.sp, fontWeight = FontWeight.Bold, fontFamily = com.example.ui.theme.AppFontFamily))
          }
        }
      }

      Spacer(modifier = Modifier.navigationBarsPadding().height(24.dp))
    }

    // 2. Glass Header Layer
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
          .padding(horizontal = 20.dp)
      ) {
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

        Text(
          text = "Buat Voucher",
          style = DiajakDesignSystem.Typography.Headline,
          color = MaterialTheme.colorScheme.onSurface,
          textAlign = TextAlign.Center,
          modifier = Modifier
            .align(Alignment.Center)
            .fillMaxWidth()
            .padding(horizontal = 48.dp),
          maxLines = 1,
          overflow = TextOverflow.Ellipsis
        )
      }
    }
  }
}
