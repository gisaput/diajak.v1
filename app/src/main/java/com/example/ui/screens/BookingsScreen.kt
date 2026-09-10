package com.example.ui.screens

import com.example.ui.theme.AppSpacing

import android.widget.Toast
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.outlined.*
import androidx.compose.material.icons.outlined.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.drawWithContent
import androidx.compose.ui.draw.clip
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Dialog
import androidx.compose.ui.window.DialogProperties
import androidx.compose.ui.zIndex
import dev.chrisbanes.haze.HazeState
import dev.chrisbanes.haze.hazeSource
import com.example.model.BookingModel
import com.example.ui.components.DiajakGlassHeader
import com.example.ui.components.SkeletonBookingCard
import com.example.ui.theme.DiajakDesignSystem
import com.example.ui.theme.DiajakOrange
import com.example.ui.theme.diajakGlassButton
import com.example.ui.theme.spacing

@Composable
fun BookingsScreen(
  bookings: List<BookingModel>,
  onExploreClick: () -> Unit,
  onBookingClick: (BookingModel) -> Unit,
  isLoading: Boolean = false
) {
  val scrollState = rememberScrollState()
  val hazeState = remember { HazeState() }
  val isScrolled by remember {
    derivedStateOf {
      scrollState.value > 0
    }
  }

  Box(
    modifier = Modifier
      .fillMaxSize()
      .background(Color(0xFFE5E7EB))
  ) {
    // 1. Content Layer (Always hazeSource with verticalScroll directly on the root Column as per AGENTS.md)
    Column(
      modifier = Modifier
        .fillMaxSize()
        .hazeSource(state = hazeState)
        .verticalScroll(scrollState)
    ) {
      // Natural Root Tab Header (Padding status bar + 16dp)
      Column(
        modifier = Modifier
          .fillMaxWidth()
          .statusBarsPadding()
          .padding(start = 20.dp, end = 20.dp, top = 16.dp, bottom = 16.dp)
      ) {
        Text(
          text = "Undangan Aktivitas",
          style = DiajakDesignSystem.Typography.Headline,
          color = MaterialTheme.colorScheme.onSurface
        )
      }

      if (isLoading) {
        Column(
          modifier = Modifier
            .fillMaxWidth()
            .padding(start = 20.dp, top = 0.dp, end = 20.dp, bottom = 124.dp),
          verticalArrangement = Arrangement.spacedBy(AppSpacing.Medium)
        ) {
          repeat(4) {
            SkeletonBookingCard()
          }
        }
      } else if (bookings.isEmpty()) {
        Box(
          modifier = Modifier
            .fillMaxWidth()
            .heightIn(min = 400.dp),
          contentAlignment = Alignment.Center
        ) {
          Column(horizontalAlignment = Alignment.CenterHorizontally) {
            Icon(
              imageVector = Icons.Outlined.CardMembership,
              contentDescription = null,
              modifier = Modifier.size(72.dp),
              tint = MaterialTheme.colorScheme.onSurface
            )
            Spacer(modifier = Modifier.height(MaterialTheme.spacing.medium))
            Text(
              text = "Belum Ada Undangan Aktif",
              style = DiajakDesignSystem.Typography.Body,
              color = MaterialTheme.colorScheme.onSurface
            )
            Spacer(modifier = Modifier.height(MaterialTheme.spacing.small))
            Text(
              text = "Yuk cari aktivitas seru di sekitarmu dan daftar sekarang!",
              style = DiajakDesignSystem.Typography.Body, 
              color = MaterialTheme.colorScheme.onSurface,
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
        Column(
          modifier = Modifier
            .fillMaxWidth()
            .padding(start = 20.dp, top = 0.dp, end = 20.dp, bottom = 124.dp),
          verticalArrangement = Arrangement.spacedBy(AppSpacing.Medium)
        ) {
          bookings.forEach { booking ->
            Surface(
              modifier = Modifier
                .fillMaxWidth()
                .clickable { onBookingClick(booking) },
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
                Row(
                  modifier = Modifier.fillMaxWidth(),
                  horizontalArrangement = Arrangement.SpaceBetween,
                  verticalAlignment = Alignment.CenterVertically
                ) {
                  Row(verticalAlignment = Alignment.CenterVertically) {
                    Icon(
                      imageVector = Icons.Outlined.CheckCircle,
                      contentDescription = "OK",
                      tint = Color(0xFF00B368),
                      modifier = Modifier.size(20.dp)
                    )
                    Spacer(modifier = Modifier.width(6.dp))
                    Text(
                      text = booking.status.uppercase(),
                      color = Color(0xFF00B368),
                      style = DiajakDesignSystem.Typography.BodyBold
                    )
                  }
                  Text(
                    text = "ID: ${booking.id}",
                    color = MaterialTheme.colorScheme.onSurfaceVariant,
                    style = DiajakDesignSystem.Typography.Body
                  )
                }
                
                Spacer(modifier = Modifier.height(MaterialTheme.spacing.medium))
                
                Row(verticalAlignment = Alignment.CenterVertically) {
                  Box(
                    modifier = Modifier
                      .size(64.dp)
                      .clip(RoundedCornerShape(12.dp))
                      .background(MaterialTheme.colorScheme.surfaceVariant)
                  ) {
                    Image(
                      painter = painterResource(id = booking.imageResId),
                      contentDescription = booking.activityTitle,
                      modifier = Modifier.fillMaxSize(),
                      contentScale = ContentScale.Crop
                    )
                  }
                  Spacer(modifier = Modifier.width(MaterialTheme.spacing.medium))
                  Column {
                    Text(
                      text = booking.activityTitle,
                      style = DiajakDesignSystem.Typography.TitleMedium,
                      color = MaterialTheme.colorScheme.onSurface,
                      maxLines = 1,
                      overflow = TextOverflow.Ellipsis
                    )
                    Spacer(modifier = Modifier.height(MaterialTheme.spacing.extraSmall))
                    Row(verticalAlignment = Alignment.CenterVertically) {
                      Icon(
                        imageVector = Icons.Outlined.LocationOn,
                        contentDescription = "Lokasi",
                        tint = MaterialTheme.colorScheme.onSurfaceVariant,
                        modifier = Modifier.size(16.dp)
                      )
                      Spacer(modifier = Modifier.width(4.dp))
                      Text(
                        text = booking.locationName,
                        style = DiajakDesignSystem.Typography.Body,
                        color = MaterialTheme.colorScheme.onSurfaceVariant,
                        maxLines = 1,
                        overflow = TextOverflow.Ellipsis
                      )
                    }
                    Spacer(modifier = Modifier.height(MaterialTheme.spacing.extraSmall))
                    Row(verticalAlignment = Alignment.CenterVertically) {
                      Icon(
                        imageVector = Icons.Outlined.AccessTime,
                        contentDescription = "Waktu",
                        tint = MaterialTheme.colorScheme.onSurfaceVariant,
                        modifier = Modifier.size(16.dp)
                      )
                      Spacer(modifier = Modifier.width(4.dp))
                      Text(
                        text = booking.schedule,
                        style = DiajakDesignSystem.Typography.Body,
                        color = MaterialTheme.colorScheme.onSurfaceVariant
                      )
                    }
                  }
                }
                
                Spacer(modifier = Modifier.height(MaterialTheme.spacing.medium))
                HorizontalDivider(color = MaterialTheme.colorScheme.outlineVariant, thickness = 1.dp)
                
                Row(
                  modifier = Modifier.fillMaxWidth().padding(top = MaterialTheme.spacing.medium),
                  horizontalArrangement = Arrangement.SpaceBetween,
                  verticalAlignment = Alignment.CenterVertically
                ) {
                  Column(verticalArrangement = Arrangement.spacedBy(AppSpacing.ExtraSmall)) {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                      Text(
                        text = "Peserta: ",
                        style = DiajakDesignSystem.Typography.Body,
                        color = MaterialTheme.colorScheme.onSurfaceVariant
                      )
                      Text(
                        text = "${booking.undanganCount} Peserta",
                        style = DiajakDesignSystem.Typography.BodyBold,
                        color = MaterialTheme.colorScheme.onSurface
                      )
                    }
                    Row(verticalAlignment = Alignment.CenterVertically) {
                      Text(
                        text = "Total: ",
                        style = DiajakDesignSystem.Typography.Body,
                        color = MaterialTheme.colorScheme.onSurfaceVariant
                      )
                      Text(
                        text = booking.totalPriceFormatted,
                        style = DiajakDesignSystem.Typography.BodyBold,
                        color = DiajakOrange
                      )
                    }
                  }
                  Surface(
                    shape = RoundedCornerShape(12.dp),
                    color = DiajakOrange,
                    modifier = Modifier.clickable { onBookingClick(booking) }
                  ) {
                    Row(
                      modifier = Modifier.padding(horizontal = 14.dp, vertical = MaterialTheme.spacing.small),
                      verticalAlignment = Alignment.CenterVertically
                    ) {
                      Icon(Icons.Outlined.QrCode, contentDescription = "QR", tint = Color.White, modifier = Modifier.size(20.dp))
                      Spacer(modifier = Modifier.width(6.dp))
                      Text("Buka Undangan", color = Color.White, style = DiajakDesignSystem.Typography.TitleBold)
                    }
                  }
                }
              }
            }
          }
        }
      }
    }

    // 2. Glass Header Layer (Appears instantly as soon as screen scrolls, identical to HomeScreen)
    if (isScrolled) {
      val density = LocalDensity.current
      val showHeaderTitle by remember {
        derivedStateOf {
          scrollState.value > with(density) { 48.dp.toPx() }
        }
      }

      DiajakGlassHeader(
        hazeState = hazeState,
        scrollState = scrollState,
        modifier = Modifier.align(Alignment.TopCenter).zIndex(10f)
      ) {
        // Content Layer (Buttons and Title) MUST be 56.dp height
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
              text = "Undangan Aktivitas",
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
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun UndanganDetailDialog(
  booking: BookingModel,
  onDismiss: () -> Unit
) {
  val screenWidth = androidx.compose.ui.platform.LocalConfiguration.current.screenWidthDp.dp
  val favoriteImageSize = (screenWidth - 60.dp) / 2
  val targetImageSize = favoriteImageSize / 2

  val context = LocalContext.current
  val dialogHazeState = remember { HazeState() }
  val dialogScrollState = rememberScrollState()

  Dialog(
    onDismissRequest = onDismiss,
    properties = DialogProperties(usePlatformDefaultWidth = false)
  ) {
    Box(
      modifier = Modifier
        .fillMaxSize()
        .background(Color(0xFFE5E7EB))
    ) {
      Column(
        modifier = Modifier
          .fillMaxSize()
          .hazeSource(state = dialogHazeState)
          .verticalScroll(dialogScrollState)
          .padding(horizontal = 20.dp),
        horizontalAlignment = Alignment.CenterHorizontally
      ) {
        Spacer(modifier = Modifier.statusBarsPadding().height(80.dp))

        // Beautiful Undangan Shape Card
        Box(
          modifier = Modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(24.dp))
            .background(Color.White)
            .border(0.dp, Color.Transparent, RoundedCornerShape(24.dp))
            .drawWithContent {
              drawContent()
              // Draw side notches
              val notchY = size.height * 0.65f
              val radius = 20.dp.toPx()

              // Left Notch
              drawCircle(
                color = Color.White,
                radius = radius,
                center = Offset(0f, notchY)
              )
              // Right Notch
              drawCircle(
                color = Color.White,
                radius = radius,
                center = Offset(size.width, notchY)
              )
            }
        ) {
          Column(
            modifier = Modifier
              .fillMaxWidth()
              .padding(20.dp)
          ) {
            // Undangan Header
            Row(
              modifier = Modifier.fillMaxWidth(),
              horizontalArrangement = Arrangement.SpaceBetween,
              verticalAlignment = Alignment.CenterVertically
            ) {
              Row(verticalAlignment = Alignment.CenterVertically) {
                Box(
                  modifier = Modifier
                    .size(36.dp)
                    .clip(CircleShape)
                    .background(DiajakOrange.copy(alpha = 0.1f)),
                  contentAlignment = Alignment.Center
                ) {
                  Icon(
                    Icons.Outlined.CardMembership,
                    contentDescription = null,
                    tint = DiajakOrange,
                    modifier = Modifier.size(18.dp)
                  )
                }
                Spacer(modifier = Modifier.width(MaterialTheme.spacing.small))
                Text(
                  text = "UNDANGAN",
                  style = com.example.ui.theme.DiajakDesignSystem.Typography.Body,
                  color = DiajakOrange
                )
              }

              Box(
                modifier = Modifier
                  .background(Color(0xFFE6F4EA), RoundedCornerShape(8.dp))
                  .padding(horizontal = MaterialTheme.spacing.small, vertical = MaterialTheme.spacing.extraSmall)
              ) {
                Text(
                  text = booking.status.uppercase(),
                  color = Color(0xFF137333),
                  style = com.example.ui.theme.DiajakDesignSystem.Typography.Body,
                  
                )
              }
            }

            Spacer(modifier = Modifier.height(MaterialTheme.spacing.medium))

            // Activity Image & Details
            Row(verticalAlignment = Alignment.Top) {
              Box(
                modifier = Modifier
                  .size(targetImageSize)
                  .clip(RoundedCornerShape(12.dp))
              ) {
                Image(
                  painter = painterResource(id = booking.imageResId),
                  contentDescription = null,
                  contentScale = ContentScale.Crop,
                  modifier = Modifier.fillMaxSize()
                )
              }
              Spacer(modifier = Modifier.width(36.dp))
              Column(modifier = Modifier.weight(1f)) {
                Text(
                  text = booking.activityTitle,
                  style = com.example.ui.theme.DiajakDesignSystem.Typography.Body,
                  color = MaterialTheme.colorScheme.onSurface,
                  lineHeight = 22.sp
                )
              }
            }

            Spacer(modifier = Modifier.height(MaterialTheme.spacing.medium))

            // Info Rows
            Row(modifier = Modifier.fillMaxWidth(), verticalAlignment = Alignment.CenterVertically) {
              Icon(Icons.Outlined.AccessTime, contentDescription = null, tint = MaterialTheme.colorScheme.onSurface, modifier = Modifier.size(20.dp))
              Spacer(modifier = Modifier.width(MaterialTheme.spacing.small))
              Text(text = booking.schedule, style = com.example.ui.theme.DiajakDesignSystem.Typography.Body, color = MaterialTheme.colorScheme.onSurface)
            }

            Spacer(modifier = Modifier.height(MaterialTheme.spacing.small))

            Row(modifier = Modifier.fillMaxWidth(), verticalAlignment = Alignment.Top) {
              Icon(Icons.Outlined.Place, contentDescription = null, tint = MaterialTheme.colorScheme.onSurface, modifier = Modifier.padding(top = 2.dp).size(20.dp))
              Spacer(modifier = Modifier.width(MaterialTheme.spacing.small))
              Column {
                Text(text = booking.locationName, style = com.example.ui.theme.DiajakDesignSystem.Typography.Body, color = MaterialTheme.colorScheme.onSurface)
              }
            }

            Spacer(modifier = Modifier.height(MaterialTheme.spacing.medium))

            Surface(
              modifier = Modifier.fillMaxWidth(),
              shape = RoundedCornerShape(16.dp),
              color = Color.White,
              shadowElevation = 0.dp,
              border = BorderStroke(0.dp, Color.Transparent)
            ) {
              Row(
                modifier = Modifier
                  .fillMaxWidth()
                  .padding(20.dp),
                horizontalArrangement = Arrangement.SpaceBetween
              ) {
                Column(modifier = Modifier.weight(1f)) {
                  Text("Kode Booking", style = com.example.ui.theme.DiajakDesignSystem.Typography.Body, color = MaterialTheme.colorScheme.onSurface)
                  Text(booking.id, style = com.example.ui.theme.DiajakDesignSystem.Typography.Body, color = MaterialTheme.colorScheme.onSurface)
                }
                Column(modifier = Modifier.weight(1f), horizontalAlignment = Alignment.End) {
                  Text("Undangan", style = com.example.ui.theme.DiajakDesignSystem.Typography.Body, color = MaterialTheme.colorScheme.onSurface)
                  Text("${booking.undanganCount} Peserta", style = com.example.ui.theme.DiajakDesignSystem.Typography.Body, color = DiajakOrange)
                }
              }
            }

            Spacer(modifier = Modifier.height(MaterialTheme.spacing.medium))

            // Visitor Information Section (NAME, EMAIL, PHONE)
            Text(
              text = "Informasi Kontak Peserta",
              style = com.example.ui.theme.DiajakDesignSystem.Typography.Body,
              color = MaterialTheme.colorScheme.onSurface
            )
            Spacer(modifier = Modifier.height(MaterialTheme.spacing.small))

            Surface(
              modifier = Modifier.fillMaxWidth(),
              shape = RoundedCornerShape(16.dp),
              color = Color.White,
              shadowElevation = 0.dp,
              border = BorderStroke(0.dp, Color.Transparent)
            ) {
              Column(
                modifier = Modifier
                  .fillMaxWidth()
                  .padding(20.dp),
                verticalArrangement = Arrangement.spacedBy(AppSpacing.Small)
              ) {
              Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween) {
                Text("Nama Lengkap", style = com.example.ui.theme.DiajakDesignSystem.Typography.Body, color = MaterialTheme.colorScheme.onSurface)
                Text(
                  text = if (booking.userName.isNotBlank()) booking.userName else "Anggi Saputro",
                  style = com.example.ui.theme.DiajakDesignSystem.Typography.Body,
                  
                  color = MaterialTheme.colorScheme.onSurface
                )
              }
              Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween) {
                Text("Alamat Email", style = com.example.ui.theme.DiajakDesignSystem.Typography.Body, color = MaterialTheme.colorScheme.onSurface)
                Text(
                  text = if (booking.userEmail.isNotBlank()) booking.userEmail else "gisaput@gmail.com",
                  style = com.example.ui.theme.DiajakDesignSystem.Typography.Body,
                  
                  color = MaterialTheme.colorScheme.onSurface
                )
              }
              Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween) {
                Text("Nomor Telepon", style = com.example.ui.theme.DiajakDesignSystem.Typography.Body, color = MaterialTheme.colorScheme.onSurface)
                Text(
                  text = if (booking.userPhone.isNotBlank()) booking.userPhone else "081234567890",
                  style = com.example.ui.theme.DiajakDesignSystem.Typography.Body,
                  
                  color = MaterialTheme.colorScheme.onSurface
                )
              }
            }
          }

            // Spacing to push down past the notch line
            Spacer(modifier = Modifier.height(MaterialTheme.spacing.medium))

            Spacer(modifier = Modifier.height(MaterialTheme.spacing.medium))

            // Beautiful custom drawn Barcode or QR code
            Column(
              modifier = Modifier.fillMaxWidth(),
              horizontalAlignment = Alignment.CenterHorizontally
            ) {
              Box(
                modifier = Modifier
                  .size(120.dp)
                  .clip(RoundedCornerShape(8.dp))
                  .border(0.dp, Color.Transparent, RoundedCornerShape(8.dp))
                  .padding(MaterialTheme.spacing.small),
                contentAlignment = Alignment.Center
              ) {
                Canvas(modifier = Modifier.fillMaxSize()) {
                  val sizePx = size.width
                  val cols = 15
                  val cellSize = sizePx / cols
                  drawRect(Color.White)

                  // Corner Finder 1
                  drawRect(androidx.compose.ui.graphics.Color(0xFF202124), Offset(0f, 0f), Size(cellSize * 3, cellSize * 3))
                  drawRect(Color.White, Offset(cellSize, cellSize), Size(cellSize, cellSize))
                  
                  // Corner Finder 2
                  drawRect(androidx.compose.ui.graphics.Color(0xFF202124), Offset(cellSize * (cols - 3), 0f), Size(cellSize * 3, cellSize * 3))
                  drawRect(Color.White, Offset(cellSize * (cols - 2), cellSize), Size(cellSize, cellSize))
                  
                  // Corner Finder 3
                  drawRect(androidx.compose.ui.graphics.Color(0xFF202124), Offset(0f, cellSize * (cols - 3)), Size(cellSize * 3, cellSize * 3))
                  drawRect(Color.White, Offset(cellSize, cellSize * (cols - 2)), Size(cellSize, cellSize))

                  for (r in 0 until cols) {
                    for (c in 0 until cols) {
                      if ((r < 3 && c < 3) || (r < 3 && c >= cols - 3) || (r >= cols - 3 && c < 3)) {
                        continue
                      }
                      if ((r * c + r * 7 + c * 13) % 3 == 0 || (r + c) % 5 == 0) {
                        drawRect(
                          color = androidx.compose.ui.graphics.Color(0xFF202124),
                          topLeft = Offset(c * cellSize, r * cellSize),
                          size = Size(cellSize, cellSize)
                        )
                      }
                    }
                  }
                }
              }

              Spacer(modifier = Modifier.height(MaterialTheme.spacing.small))
              Text(
                text = "Tunjukkan QR untuk Check-in",
                style = com.example.ui.theme.DiajakDesignSystem.Typography.Body,
                color = MaterialTheme.colorScheme.onSurface
              )
            }
          }
        }

        Spacer(modifier = Modifier.height(MaterialTheme.spacing.medium))

        // Save to Gallery Button
        OutlinedButton(
          onClick = {
            Toast.makeText(context, "Undangan berhasil disimpan ke galeri ponsel! 📸", Toast.LENGTH_LONG).show()
          },
          modifier = Modifier
            .fillMaxWidth()
            .height(52.dp),
          shape = RoundedCornerShape(16.dp),
          border = BorderStroke(1.5.dp, DiajakOrange)
        ) {
          Text("Simpan ke Galeri", color = DiajakOrange, style = com.example.ui.theme.DiajakDesignSystem.Typography.TitleBold)
        }

        Spacer(modifier = Modifier.height(MaterialTheme.spacing.medium))

        Button(
          onClick = onDismiss,
          modifier = Modifier
            .fillMaxWidth()
            .height(52.dp),
          shape = RoundedCornerShape(16.dp),
          colors = ButtonDefaults.buttonColors(containerColor = DiajakOrange)
        ) {
          Text("Tutup", color = Color.White, style = com.example.ui.theme.DiajakDesignSystem.Typography.TitleBold)
        }
        
        Spacer(modifier = Modifier.height(MaterialTheme.spacing.medium))
      }

      DiajakGlassHeader(
        hazeState = dialogHazeState,
        modifier = Modifier.align(Alignment.TopCenter).zIndex(10f)
      ) {
        Box(
          modifier = Modifier
            .fillMaxWidth()
            .statusBarsPadding()
            .height(56.dp)
            .padding(horizontal = 20.dp)
        ) {
          IconButton(
            onClick = onDismiss,
            modifier = Modifier
              .size(40.dp)
              .align(Alignment.CenterStart)
              .diajakGlassButton(dialogHazeState)
          ) {
            Icon(
              imageVector = Icons.AutoMirrored.Outlined.ArrowBack,
              contentDescription = "Kembali",
              tint = MaterialTheme.colorScheme.onSurface,
              modifier = Modifier.size(24.dp)
            )
          }

          Text(
            text = "Detail Undangan",
            style = DiajakDesignSystem.Typography.Headline,
            color = MaterialTheme.colorScheme.onSurface,
            textAlign = TextAlign.Center,
            modifier = Modifier.align(Alignment.Center).fillMaxWidth()
          )
        }
      }
    }
  }
}
