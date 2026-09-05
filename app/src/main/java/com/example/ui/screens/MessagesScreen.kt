package com.example.ui.screens
import com.example.ui.theme.spacing
import com.example.ui.theme.diajakGlassButton
import androidx.compose.material3.MaterialTheme

import androidx.activity.compose.BackHandler
import androidx.compose.animation.animateColorAsState
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.Spring
import androidx.compose.foundation.gestures.detectHorizontalDragGestures
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.unit.IntOffset
import kotlin.math.roundToInt
import androidx.compose.animation.core.spring
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.outlined.*
import androidx.compose.material.icons.automirrored.outlined.*
import androidx.compose.material.icons.outlined.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.zIndex
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.model.DiajakNotification

import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.ui.layout.onSizeChanged
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.ExperimentalLayoutApi
import androidx.compose.foundation.layout.isImeVisible
import androidx.compose.foundation.layout.imePadding
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.ui.text.TextStyle
import dev.chrisbanes.haze.HazeState
import dev.chrisbanes.haze.hazeSource
import dev.chrisbanes.haze.hazeEffect
import com.example.ui.components.DiajakGlassHeader
import androidx.compose.foundation.gestures.detectTapGestures
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.input.pointer.pointerInput
import kotlinx.coroutines.launch

import com.example.model.MessageThread
import com.example.ui.theme.DiajakOrange
import com.example.ui.theme.DiajakOrangeLight
import com.example.ui.theme.DiajakOrangeDark
import com.example.ui.theme.BackgroundLight
import com.example.ui.theme.SlateDark
import com.example.ui.theme.SurfaceWhite

@OptIn(ExperimentalMaterial3Api::class, ExperimentalLayoutApi::class)
@Composable
fun MessagesScreen(
  onExploreClick: () -> Unit,
  initialChatPartner: String? = null,
  initialActivityTitle: String? = null,
  initialAcaraTitle: String? = initialActivityTitle,
  onChatOpened: () -> Unit = {},
  onChatClosed: () -> Unit = {},
  isKreatorMode: Boolean = false,
  notifications: List<DiajakNotification> = emptyList(),
  onToggleNotificationRead: (String) -> Unit = {},
  onToggleNotificationArchive: (String) -> Unit = {},
  onDeleteNotification: (String) -> Unit = {},
  onMarkAllNotificationsAsRead: () -> Unit = {},
  onClearAllNotifications: () -> Unit = {},
  threads: List<MessageThread> = emptyList(),
  onDeleteThread: (String) -> Unit = {},
  onToggleThreadRead: (String) -> Unit = {},
  onToggleThreadArchive: (String) -> Unit = {},
  onAddThread: (MessageThread) -> Unit = {}
) {
  var selectedThread by remember { mutableStateOf<MessageThread?>(null) }
  var searchQuery by remember { mutableStateOf("") }
  var chatInput by remember { mutableStateOf("") }

  val tabs = listOf("Pesan", "Notifikasi", "Arsip")
  var selectedTabIdx by remember { mutableStateOf(0) }
  val coroutineScope = rememberCoroutineScope()

  LaunchedEffect(initialChatPartner) {
    if (initialChatPartner != null) {
      val existing = threads.find { it.senderName.equals(initialChatPartner, ignoreCase = true) }
      if (existing != null) {
        selectedThread = existing
      }
      onChatOpened()
    }
  }

  val relevantThreads = threads // Tampilkan semua chat

  // Calculate dynamic tab counts
  val countPesan = relevantThreads.count { !it.isArchived }
  val countNotifikasi = notifications.count { !it.isArchived }
  val countArsip = relevantThreads.count { it.isArchived } + notifications.count { it.isArchived }
  
  val tabCounts = listOf(countPesan, countNotifikasi, countArsip)

  val filteredNotifications = notifications.filter {
    val matchSearch = if (searchQuery.isBlank()) true else it.title.contains(searchQuery, ignoreCase = true) || it.body.contains(searchQuery, ignoreCase = true)
    val matchTab = when (selectedTabIdx) {
      0 -> false // Pesan
      1 -> !it.isArchived // Notifikasi
      2 -> it.isArchived // Arsip
      else -> false
    }
    matchSearch && matchTab
  }

  val filteredThreads = relevantThreads.filter {
    val matchSearch = if (searchQuery.isBlank()) true else it.senderName.contains(searchQuery, ignoreCase = true) || it.lastMessage.contains(searchQuery, ignoreCase = true)
    val matchTab = when (selectedTabIdx) {
      0 -> !it.isArchived // Pesan
      1 -> false // Notifikasi
      2 -> it.isArchived // Arsip
      else -> false
    }
    matchSearch && matchTab
  }

  val hazeState = remember { HazeState() }
  val listState = rememberLazyListState()

  Box(
    modifier = Modifier
      .fillMaxSize()
      .background(androidx.compose.ui.graphics.Color(0xFFE5E7EB))
  ) {
    Column(
      modifier = Modifier
        .fillMaxSize()
        .hazeSource(state = hazeState)
    ) {

    LazyColumn(
      state = listState,
      contentPadding = PaddingValues(bottom = if (isKreatorMode) 20.dp else 124.dp),
      // verticalArrangement removed so the first item starts exactly at HeaderTotalTopPadding
      modifier = Modifier
        .weight(1f)
        .background(Color.Transparent)
    ) {
      item { Spacer(modifier = Modifier.statusBarsPadding().height(108.dp)) }
      
      if (filteredNotifications.isEmpty() && filteredThreads.isEmpty()) {
        item {
          // High-Fidelity empty state
          Column(
            modifier = Modifier
              .fillMaxWidth()
              .padding(top = 48.dp)
              .padding(horizontal = 40.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
          ) {
            Icon(
              imageVector = when (selectedTabIdx) {
                0 -> Icons.Outlined.ChatBubbleOutline
                1 -> Icons.Outlined.Notifications
                else -> Icons.Outlined.Archive
              },
              contentDescription = null,
              modifier = Modifier.size(72.dp),
              tint = MaterialTheme.colorScheme.onSurface
            )
            Spacer(modifier = Modifier.height(20.dp))
            Text(
              text = if (searchQuery.isNotEmpty()) {
                "Pencarian Kosong"
              } else {
                when (selectedTabIdx) {
                  0 -> "Belum Ada Pesan"
                  1 -> "Belum Ada Notifikasi"
                  else -> "Arsip Kosong"
                }
              },
              style = com.example.ui.theme.DiajakDesignSystem.Typography.TitleBold,
              color = MaterialTheme.colorScheme.onSurface
            )
            Spacer(modifier = Modifier.height(MaterialTheme.spacing.small))
            Text(
              text = if (searchQuery.isNotEmpty()) {
                "Tidak ditemukan hasil untuk \"$searchQuery\""
              } else {
                when (selectedTabIdx) {
                  0 -> "Pesan dari penyelenggara atau pengguna lain akan muncul di sini."
                  1 -> "Info terbaru dan pengingat aktivitas akan muncul di sini."
                  else -> "Pesan yang kamu arsipkan akan disimpan di sini dengan aman."
                }
              },
              style = com.example.ui.theme.DiajakDesignSystem.Typography.Body,
              color = MaterialTheme.colorScheme.onSurfaceVariant,
              textAlign = androidx.compose.ui.text.style.TextAlign.Center,
              lineHeight = 22.sp
            )
            Spacer(modifier = Modifier.height(20.dp))
            Button(
              onClick = onExploreClick,
              colors = ButtonDefaults.buttonColors(containerColor = DiajakOrange),
              shape = RoundedCornerShape(16.dp),
              contentPadding = PaddingValues(horizontal = MaterialTheme.spacing.screenMargin, vertical = MaterialTheme.spacing.medium)
            ) {
              Icon(Icons.Outlined.Search, contentDescription = null, modifier = Modifier.size(18.dp))
              Spacer(modifier = Modifier.width(MaterialTheme.spacing.small))
              Text("Temukan Aktivitas", style = com.example.ui.theme.DiajakDesignSystem.Typography.TitleBold)
            }
          }
        }
      } else {
        if (filteredNotifications.isNotEmpty()) {
          // Removed INFO & NOTIFIKASI header          // Removed INFO & NOTIFIKASI header
          
          items(filteredNotifications, key = { it.id }) { notification ->
            // Custom styling depending on notification type
            val iconType = when (notification.type) {
              "booking" -> Icons.Outlined.CardMembership
              "promo" -> Icons.Outlined.Percent
              "activity" -> Icons.Outlined.Info
              "message" -> Icons.Outlined.ChatBubbleOutline
              else -> Icons.Outlined.Notifications
            }
            
            val iconBg = when (notification.type) {
              "booking" -> Color(0xFFE6F4EA)
              "promo" -> Color(0xFFFCE8E6)
              "activity" -> DiajakOrangeLight
              "message" -> DiajakOrangeLight
              else -> MaterialTheme.colorScheme.outlineVariant
            }
            
            val iconTint = when (notification.type) {
              "booking" -> Color(0xFF137333)
              "promo" -> Color(0xFFC5221F)
              "activity" -> DiajakOrange
              "message" -> DiajakOrange
              else -> MaterialTheme.colorScheme.onSurface
            }

            key(notification.id, notification.isArchived, notification.isRead) {
              SwipeToRevealItem(
                key = notification.id,
                onArchive = { onToggleNotificationArchive(notification.id) },
                onDelete = { onDeleteNotification(notification.id) },
                isArchived = notification.isArchived
              ) {
                com.example.ui.theme.DiajakCard(
                  modifier = Modifier.fillMaxWidth(),
                  onClick = { onToggleNotificationRead(notification.id) }
                ) {
                  Column(
                    modifier = Modifier.padding(horizontal = 20.dp, vertical = 20.dp)
                  ) {
                  Row(
                    verticalAlignment = Alignment.Top
                  ) {
                    Box(
                      modifier = Modifier
                        .size(40.dp)
                        .clip(CircleShape)
                        .background(iconBg),
                      contentAlignment = Alignment.Center
                    ) {
                      Icon(
                        imageVector = iconType,
                        contentDescription = null,
                        modifier = Modifier.size(18.dp),
                        tint = iconTint
                      )
                    }
                    
                    Spacer(modifier = Modifier.width(20.dp))
                    
                    Column(modifier = Modifier.weight(1f)) {
                      Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                      ) {
                        Row(
                          verticalAlignment = Alignment.CenterVertically,
                          horizontalArrangement = Arrangement.spacedBy(MaterialTheme.spacing.small)
                        ) {
                          Text(
                            text = notification.title,
                            fontSize = 14.sp,
                            fontWeight = FontWeight.Bold,
                            style = com.example.ui.theme.DiajakDesignSystem.Typography.Body,
                            color = MaterialTheme.colorScheme.onSurface
                          )
                          if (!notification.isRead) {
                            Box(
                              modifier = Modifier
                                .size(6.dp)
                                .background(DiajakOrange, CircleShape)
                            )
                          }
                        }
                        
                        Text(
                          text = notification.timestamp,
                          style = com.example.ui.theme.DiajakDesignSystem.Typography.Body, color = MaterialTheme.colorScheme.onSurface
                        )
                      }
                      
                      Spacer(modifier = Modifier.height(MaterialTheme.spacing.extraSmall))
                      
                      Text(
                        text = notification.body,
                        style = com.example.ui.theme.DiajakDesignSystem.Typography.Body, color = MaterialTheme.colorScheme.onSurface,
                        lineHeight = 22.sp
                      )
                    }
                  }
                }
              }
              }
            }
          }
        }
        
        // Threads list
        if (filteredThreads.isNotEmpty()) {
          if (filteredNotifications.isNotEmpty()) {
            item {
              Spacer(modifier = Modifier.height(MaterialTheme.spacing.small))
            }
          }
          // Removed PERCAKAPAN CHAT header
          
          items(filteredThreads, key = { it.id }) { thread ->
            key(thread.id, thread.isArchived, thread.unreadCount) {
              SwipeToRevealItem(
                key = thread.id,
                onArchive = { onToggleThreadArchive(thread.id) },
                onDelete = { onDeleteThread(thread.id) },
                isArchived = thread.isArchived
              ) {
                com.example.ui.theme.DiajakCard(
                  modifier = Modifier.fillMaxWidth(),
                  onClick = {
                      selectedThread = thread
                      if (thread.unreadCount > 0) onToggleThreadRead(thread.id)
                      onChatOpened()
                    }
                ) {
                  Column(
                    modifier = Modifier.padding(horizontal = 20.dp, vertical = 20.dp)
                  ) {
                  Row(
                    verticalAlignment = Alignment.CenterVertically,
                    modifier = Modifier.fillMaxWidth()
                  ) {
                    // Beautiful initials avatar with circular gradient background
                    val cleanAvatarName = thread.senderName.replace(Regex(" \\(Kreator\\)$| \\(Peserta\\)$", RegexOption.IGNORE_CASE), "")
                    Box(
                      modifier = Modifier
                        .size(48.dp)
                        .clip(CircleShape)
                        .background(
                          Brush.linearGradient(
                            colors = listOf(thread.avatarColor, thread.avatarColor.copy(alpha = 0.6f))
                          )
                        ),
                      contentAlignment = Alignment.Center
                    ) {
                      Text(
                        text = cleanAvatarName.take(1).uppercase(),
                        color = Color.White,
                        style = com.example.ui.theme.DiajakDesignSystem.Typography.Body
                      )
                    }
                    
                    Spacer(modifier = Modifier.width(20.dp))
                    
                    Column(modifier = Modifier.weight(1f)) {
                      Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                      ) {
                        Row(
                          verticalAlignment = Alignment.CenterVertically,
                          horizontalArrangement = Arrangement.spacedBy(MaterialTheme.spacing.small)
                        ) {
                          val cleanName = thread.senderName.replace(Regex(" \\(Kreator\\)$| \\(Peserta\\)$", RegexOption.IGNORE_CASE), "")
                          Text(
                            text = cleanName,
                            style = com.example.ui.theme.DiajakDesignSystem.Typography.BodyBold,
                            color = MaterialTheme.colorScheme.onSurface
                          )
                          
                          val hasKreatorTag = thread.role == "peserta" && !thread.senderRole.contains("Grup", ignoreCase = true) && !thread.senderRole.contains("Customer Service", ignoreCase = true)
                          val hasPesertaTag = thread.role == "kreator"

                          if (hasKreatorTag) {
                            Box(
                              modifier = Modifier
                                .clip(RoundedCornerShape(4.dp))
                                .background(DiajakOrangeLight)
                                .padding(horizontal = 4.dp, vertical = 1.dp)
                            ) {
                              Text(
                                text = "Kreator",
                                color = DiajakOrange,
                                style = com.example.ui.theme.DiajakDesignSystem.Typography.Body
                              )
                            }
                          } else if (hasPesertaTag) {
                            Box(
                              modifier = Modifier
                                .clip(RoundedCornerShape(4.dp))
                                .background(DiajakOrangeLight)
                                .padding(horizontal = 4.dp, vertical = 1.dp)
                            ) {
                              Text(
                                text = "Peserta",
                                color = DiajakOrange,
                                style = com.example.ui.theme.DiajakDesignSystem.Typography.Body
                              )
                            }
                          }
                        }
                        
                        Text(
                          text = thread.time,
                          style = com.example.ui.theme.DiajakDesignSystem.Typography.Body,
                          color = if (thread.unreadCount > 0) DiajakOrange else MaterialTheme.colorScheme.onSurface,
                          fontWeight = if (thread.unreadCount > 0) FontWeight.Bold else FontWeight.Medium
                        )
                      }
                      
                      Spacer(modifier = Modifier.height(MaterialTheme.spacing.small))
                      
                      Row(
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.SpaceBetween,
                        modifier = Modifier.fillMaxWidth()
                      ) {
                        Text(
                          text = thread.lastMessage,
                          style = com.example.ui.theme.DiajakDesignSystem.Typography.Body,
                          color = if (thread.unreadCount > 0) MaterialTheme.colorScheme.onSurface else MaterialTheme.colorScheme.onSurface,
                          fontWeight = if (thread.unreadCount > 0) FontWeight.Bold else FontWeight.Medium,
                          maxLines = 1,
                          overflow = TextOverflow.Ellipsis,
                          modifier = Modifier.weight(1f).padding(end = MaterialTheme.spacing.small)
                        )
                        
                        if (thread.unreadCount > 0) {
                          Box(
                            modifier = Modifier
                              .size(18.dp)
                              .background(DiajakOrange, CircleShape),
                            contentAlignment = Alignment.Center
                          ) {
                            Text(
                              text = thread.unreadCount.toString(),
                              color = Color.White,
                              style = com.example.ui.theme.DiajakDesignSystem.Typography.TitleBold,
                              
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
          }
        }
      }
    }
    }

    // 2. Glass Header Layer
    DiajakGlassHeader(
      hazeState = hazeState,
      lazyListState = listState,
      modifier = Modifier.align(Alignment.TopCenter).zIndex(10f)
    ) {
      // Content Layer (Buttons and Title) MUST be 56.dp height
      Box(
        modifier = Modifier
          .fillMaxWidth()
          .statusBarsPadding()
          .height(56.dp)
          .padding(horizontal = 20.dp)
      ) {
        if (isKreatorMode) {
          IconButton(
            onClick = onExploreClick, // Use onExploreClick to go back to Dashboard
            modifier = Modifier
              .size(40.dp)
              .align(Alignment.CenterStart)
              .diajakGlassButton(hazeState)
          ) {
            Icon(
              imageVector = Icons.AutoMirrored.Outlined.ArrowBack,
              contentDescription = "Back",
              tint = MaterialTheme.colorScheme.onSurface,
              modifier = Modifier.size(24.dp)
            )
          }
        }
        
        // Title (STRICTLY CENTERED WITH HEADLINE TYPOGRAPHY)
        Text(
          text = "Pesan Masuk",
          style = com.example.ui.theme.DiajakDesignSystem.Typography.Headline,
          color = MaterialTheme.colorScheme.onSurface,
          textAlign = androidx.compose.ui.text.style.TextAlign.Center,
          modifier = Modifier.align(Alignment.Center).fillMaxWidth()
        )
      }
    }
    
    // Floating Tabs
    LazyRow(
      modifier = Modifier
        .fillMaxWidth()
        .align(Alignment.TopCenter)
        .statusBarsPadding()
        .padding(top = 56.dp)
        .zIndex(9f),
      contentPadding = PaddingValues(horizontal = 20.dp, vertical = 12.dp),
      horizontalArrangement = Arrangement.spacedBy(MaterialTheme.spacing.small)
    ) {
      items(tabs.size) { index ->
        val isSelected = selectedTabIdx == index
        Box(
          modifier = Modifier
            .height(36.dp)
            .clip(RoundedCornerShape(24.dp))
            .background(if (isSelected) DiajakOrange else Color.Black.copy(alpha = 0.03f))
            .then(
                if (!isSelected) Modifier.hazeEffect(
                    state = hazeState,
                    style = com.example.ui.components.DiajakGlassConfig.ButtonStyle
                ) else Modifier
            )
            .border(
                width = 0.5.dp,
                color = if (isSelected) Color.Transparent else Color.Black.copy(alpha = 0.08f),
                shape = RoundedCornerShape(24.dp)
            )
            .clickable { selectedTabIdx = index }
            .padding(horizontal = 16.dp),
          contentAlignment = Alignment.Center
        ) {
            Text(
              text = "${tabs[index]} ${tabCounts[index]}",
              style = com.example.ui.theme.DiajakDesignSystem.Typography.Label,
              color = if (isSelected) Color.White else com.example.ui.theme.DiajakTextDark
            )
        }
      }
    }
    
  var chatMessages by remember { mutableStateOf(listOf<String>()) }
  LaunchedEffect(selectedThread) {
    if (selectedThread != null) {
      chatMessages = listOf(selectedThread!!.lastMessage)
    }
  }

  if (selectedThread != null) {
    BackHandler {
      selectedThread = null
      onChatClosed()
    }
    
    ModalBottomSheet(
      onDismissRequest = { selectedThread = null; onChatClosed() },
      sheetState = rememberModalBottomSheetState(skipPartiallyExpanded = true),
      containerColor = androidx.compose.ui.graphics.Color(0xFFE5E7EB),
      contentWindowInsets = { WindowInsets(0, 0, 0, 0) }
    ) {
      val coroutineScope = rememberCoroutineScope()
      val listState = rememberLazyListState()
      val chatHazeState = remember { HazeState() }
      var listHeight by remember { mutableStateOf(0) }
      val focusManager = androidx.compose.ui.platform.LocalFocusManager.current

      val reversedMessages = remember(chatMessages) {
        chatMessages.asReversed()
      }

      val isKeyboardVisible = WindowInsets.isImeVisible
      LaunchedEffect(chatMessages.size, isKeyboardVisible, selectedThread) {
        if (chatMessages.isNotEmpty()) {
          kotlinx.coroutines.delay(150)
          listState.animateScrollToItem(0)
        }
      }

      Box(
        modifier = Modifier
          .fillMaxWidth()
          .fillMaxHeight(0.88f)
          .navigationBarsPadding()
          .imePadding()
          .pointerInput(Unit) {
            detectTapGestures(onTap = {
              focusManager.clearFocus()
            })
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
          Row(
            modifier = Modifier
              .fillMaxWidth()
              .clip(RoundedCornerShape(8.dp))
              .background(Color.White)
              .border(0.dp, Color.Transparent, RoundedCornerShape(8.dp))
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
                style = com.example.ui.theme.DiajakDesignSystem.Typography.TitleBold,
                color = MaterialTheme.colorScheme.onSurface,
                
              )
              Text(
                text = selectedThread!!.activityTitle,
                fontSize = 14.sp,
                fontWeight = FontWeight.Bold,
                style = com.example.ui.theme.DiajakDesignSystem.Typography.Body,
                color = MaterialTheme.colorScheme.onSurface,
                maxLines = 1,
                overflow = TextOverflow.Ellipsis
              )
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
          verticalArrangement = Arrangement.spacedBy(MaterialTheme.spacing.small)
        ) {
          itemsIndexed(
            items = reversedMessages,
            key = { index, _ -> chatMessages.size - 1 - index }
          ) { index, msg ->
            val isFromPartner = (index == reversedMessages.lastIndex)
            
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
                  text = if (isFromPartner) selectedThread!!.time else "Baru saja",
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
            placeholder = { Text("Tulis pesan...", style = com.example.ui.theme.DiajakDesignSystem.Typography.TitleBold, color = MaterialTheme.colorScheme.onSurface) },
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
                text = selectedThread!!.senderName.take(1).uppercase(),
                color = Color.White,
                style = com.example.ui.theme.DiajakDesignSystem.Typography.Body
              )
            }
            Spacer(modifier = Modifier.width(12.dp))
            Column(modifier = Modifier.weight(1f)) {
              Text(
                text = selectedThread!!.senderName,
                style = com.example.ui.theme.DiajakDesignSystem.Typography.BodyBold,
                color = MaterialTheme.colorScheme.onSurface
              )
              Text(
                text = selectedThread!!.senderRole,
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
              onClick = { selectedThread = null; onChatClosed() },
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
}
}
}

// Clean helper to decide border outlines
@Composable
private fun borderStroke(isUnread: Boolean): androidx.compose.foundation.BorderStroke? {
  return if (isUnread) {
    androidx.compose.foundation.BorderStroke(0.dp, Color.Transparent)
  } else {
    androidx.compose.foundation.BorderStroke(1.5.dp, DiajakOrange.copy(alpha = 0.5f))
  }
}

@Composable
fun SwipeToRevealItem(
  key: Any,
  modifier: Modifier = Modifier,
  onArchive: () -> Unit,
  onDelete: () -> Unit,
  isArchived: Boolean,
  content: @Composable () -> Unit
) {
  var offsetX by remember(key) { mutableStateOf(0f) }
  val density = LocalDensity.current
  val revealWidthPx = with(density) { 100.dp.toPx() } // two buttons fit nicely in 100.dp (each 36.dp size + margins)
  
  val animatedOffsetX by animateFloatAsState(
    targetValue = offsetX,
    animationSpec = spring(stiffness = Spring.StiffnessMediumLow),
    label = "offsetX"
  )

  Box(
    modifier = modifier
      .fillMaxWidth()
      .padding(horizontal = 20.dp, vertical = 6.dp)
      .clip(androidx.compose.foundation.shape.RoundedCornerShape(16.dp))
      .background(androidx.compose.ui.graphics.Color(0xFFE5E7EB))
  ) {
    // Revealed buttons in the background
    Row(
      modifier = Modifier
        .align(if (animatedOffsetX > 0) Alignment.CenterStart else Alignment.CenterEnd)
        .padding(horizontal = 20.dp),
      verticalAlignment = Alignment.CenterVertically,
      horizontalArrangement = Arrangement.spacedBy(MaterialTheme.spacing.small)
    ) {
      // Archive Button
      IconButton(
        onClick = {
          offsetX = 0f
          onArchive()
        },
        modifier = Modifier
          .size(36.dp)
          .clip(CircleShape)
          .background(Color.White)
      ) {
        Icon(
          imageVector = if (isArchived) Icons.Outlined.Unarchive else Icons.Outlined.Archive,
          contentDescription = if (isArchived) "Buka Arsip" else "Arsipkan",
          tint = MaterialTheme.colorScheme.onSurface,
          modifier = Modifier.size(18.dp)
        )
      }

      // Delete Button
      IconButton(
        onClick = {
          offsetX = 0f
          onDelete()
        },
        modifier = Modifier
          .size(36.dp)
          .clip(CircleShape)
          .background(Color(0xFFFEE2E2))
      ) {
        Icon(
          imageVector = Icons.Outlined.Delete,
          contentDescription = "Hapus",
          tint = Color(0xFFEF4444),
          modifier = Modifier.size(18.dp)
        )
      }
    }

    // Foreground draggable item
    Box(
      modifier = Modifier
        .fillMaxWidth()
        .offset { IntOffset(animatedOffsetX.roundToInt(), 0) }
        .background(Color.Transparent)
        .pointerInput(key) {
          detectHorizontalDragGestures(
            onHorizontalDrag = { change, dragAmount ->
              change.consume()
              val newOffset = offsetX + dragAmount
              offsetX = newOffset.coerceIn(-revealWidthPx, revealWidthPx)
            },
            onDragEnd = {
              offsetX = if (offsetX > revealWidthPx / 2) {
                revealWidthPx
              } else if (offsetX < -revealWidthPx / 2) {
                -revealWidthPx
              } else {
                0f
              }
            },
            onDragCancel = {
              offsetX = 0f
            }
          )
        }
    ) {
      content()
    }
  }
}

