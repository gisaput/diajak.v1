package com.example.ui.screens

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
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.drawWithContent
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.PathEffect
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import com.example.ui.theme.diajakGlassButton
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.zIndex
import com.example.model.BookingModel
import com.example.ui.theme.DiajakOrange
import com.example.ui.theme.spacing
import com.example.ui.theme.DiajakDesignSystem
import com.example.ui.components.DiajakGlassHeader
import com.example.ui.components.DiajakGlassConfig
import dev.chrisbanes.haze.HazeState
import dev.chrisbanes.haze.hazeSource
import dev.chrisbanes.haze.hazeEffect
import dev.chrisbanes.haze.HazeStyle
import dev.chrisbanes.haze.HazeTint

@Composable
fun InvitationDetailOverlay(
  booking: BookingModel,
  onDismiss: () -> Unit,
  onChatWithKreator: (kreatorName: String) -> Unit = {}
) {
  val screenWidth = androidx.compose.ui.platform.LocalConfiguration.current.screenWidthDp.dp
  val favoriteImageSize = (screenWidth - 60.dp) / 2
  val targetImageSize = favoriteImageSize / 2
  
  val context = LocalContext.current
  val scrollState = rememberScrollState()
  val hazeState = remember { HazeState() }

  androidx.activity.compose.BackHandler {
    onDismiss()
  }

  Box(
    modifier = Modifier
      .fillMaxSize()
      .background(Color(0xFFE5E7EB))
  ) {
    // 1. Content Layer (Always hazeSource)
    Column(
      modifier = Modifier
        .fillMaxSize()
        .hazeSource(state = hazeState)
        .verticalScroll(scrollState)
    ) {
      // ALWAYS use 80.dp spacer for the content (56.dp header + 24.dp gap)
      Spacer(modifier = Modifier.statusBarsPadding().height(80.dp))
      
      Column(
        modifier = Modifier
          .fillMaxWidth()
          .padding(horizontal = 20.dp),
        horizontalAlignment = Alignment.CenterHorizontally
      ) {
        // Undangan Header
        Row(
          modifier = Modifier.fillMaxWidth(),
          horizontalArrangement = Arrangement.Start,
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
        }

        Spacer(modifier = Modifier.height(MaterialTheme.spacing.medium))

        // Activity Image & Title Row
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
          Column(
            modifier = Modifier.weight(1f).height(targetImageSize),
            verticalArrangement = Arrangement.SpaceBetween
          ) {
            Text(
              text = booking.activityTitle,
              style = DiajakDesignSystem.Typography.BodyBold,
              color = MaterialTheme.colorScheme.onSurface,
              maxLines = 1,
              overflow = TextOverflow.Ellipsis
            )
            Row(verticalAlignment = Alignment.Top) {
              Icon(
                Icons.Outlined.Place,
                contentDescription = null,
                tint = MaterialTheme.colorScheme.onSurface,
                modifier = Modifier.padding(top = 2.dp).size(18.dp)
              )
              Spacer(modifier = Modifier.width(MaterialTheme.spacing.extraSmall))
              Text(
                text = booking.locationName,
                style = DiajakDesignSystem.Typography.Body,
                color = MaterialTheme.colorScheme.onSurface,
                maxLines = 2,
                overflow = TextOverflow.Ellipsis
              )
            }
            Row(verticalAlignment = Alignment.CenterVertically) {
              Icon(
                Icons.Outlined.AccessTime,
                contentDescription = null,
                tint = MaterialTheme.colorScheme.onSurface,
                modifier = Modifier.size(18.dp)
              )
              Spacer(modifier = Modifier.width(MaterialTheme.spacing.extraSmall))
              Text(
                text = booking.schedule,
                style = DiajakDesignSystem.Typography.Body,
                color = MaterialTheme.colorScheme.onSurface,
                maxLines = 1,
                overflow = TextOverflow.Ellipsis
              )
            }
          }
        }

        Spacer(modifier = Modifier.height(36.dp))

        // Details summary box
        Row(
          modifier = Modifier
            .fillMaxWidth(),
          horizontalArrangement = Arrangement.SpaceBetween
        ) {
          Column {
            Text("Kode Booking", style = DiajakDesignSystem.Typography.Body, color = MaterialTheme.colorScheme.onSurfaceVariant)
            Spacer(modifier = Modifier.height(2.dp))
            Text(booking.id, style = DiajakDesignSystem.Typography.BodyBold, color = MaterialTheme.colorScheme.onSurface)
          }
          Column(horizontalAlignment = Alignment.End) {
            Text("Jumlah Undangan", style = DiajakDesignSystem.Typography.Body, color = MaterialTheme.colorScheme.onSurfaceVariant)
            Spacer(modifier = Modifier.height(2.dp))
            Text("${booking.undanganCount} Peserta", style = DiajakDesignSystem.Typography.BodyBold, color = DiajakOrange)
          }
        }

        Spacer(modifier = Modifier.height(20.dp))

        // Visitor Contact Information Section
        Row(modifier = Modifier.fillMaxWidth()) {
          Text(
            text = "Informasi Peserta",
            style = DiajakDesignSystem.Typography.BodyBold,
            color = MaterialTheme.colorScheme.onSurface,
            textAlign = TextAlign.Start
          )
        }
        Spacer(modifier = Modifier.height(10.dp))

        Column(
          modifier = Modifier.fillMaxWidth(),
          verticalArrangement = Arrangement.spacedBy(MaterialTheme.spacing.small)
        ) {
          Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
          ) {
            Text("Nama Lengkap", style = DiajakDesignSystem.Typography.Body, color = MaterialTheme.colorScheme.onSurface)
            Text(
              text = if (booking.userName.isNotBlank()) booking.userName else "Anggi Saputro",
              style = DiajakDesignSystem.Typography.Body,
              color = MaterialTheme.colorScheme.onSurface
            )
          }
          Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
          ) {
            Text("Alamat Email", style = DiajakDesignSystem.Typography.Body, color = MaterialTheme.colorScheme.onSurface)
            Text(
              text = if (booking.userEmail.isNotBlank()) booking.userEmail else "gisaput@gmail.com",
              style = DiajakDesignSystem.Typography.Body,
              color = MaterialTheme.colorScheme.onSurface
            )
          }
          Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
          ) {
            Text("Nomor Handphone", style = DiajakDesignSystem.Typography.Body, color = MaterialTheme.colorScheme.onSurface)
            Text(
              text = if (booking.userPhone.isNotBlank()) booking.userPhone else "081234567890",
              style = DiajakDesignSystem.Typography.Body,
              color = MaterialTheme.colorScheme.onSurface
            )
          }
        }

        // Spacing to push down past the notch line
        Spacer(modifier = Modifier.height(40.dp))

        // Custom QR Code
        Column(
          modifier = Modifier.fillMaxWidth(),
          horizontalAlignment = Alignment.CenterHorizontally
        ) {
          Box(
            modifier = Modifier
              .size(160.dp)
              .clip(RoundedCornerShape(12.dp))
              .border(0.dp, Color.Transparent, RoundedCornerShape(12.dp))
              .background(Color.White)
              .padding(20.dp),
            contentAlignment = Alignment.Center
          ) {
            Canvas(modifier = Modifier.fillMaxSize()) {
              val sizePx = size.width
              val cols = 15
              val cellSize = sizePx / cols

              // Corner Finder 1
              drawRect(androidx.compose.ui.graphics.Color(0xFF202124), Offset(0f, 0f), Size(cellSize * 4, cellSize * 4))
              drawRect(Color.White, Offset(cellSize, cellSize), Size(cellSize * 2, cellSize * 2))
              drawRect(androidx.compose.ui.graphics.Color(0xFF202124), Offset(cellSize * 1.5f, cellSize * 1.5f), Size(cellSize, cellSize))
              
              // Corner Finder 2
              drawRect(androidx.compose.ui.graphics.Color(0xFF202124), Offset(cellSize * (cols - 4), 0f), Size(cellSize * 4, cellSize * 4))
              drawRect(Color.White, Offset(cellSize * (cols - 3), cellSize), Size(cellSize * 2, cellSize * 2))
              drawRect(androidx.compose.ui.graphics.Color(0xFF202124), Offset(cellSize * (cols - 2.5f), cellSize * 1.5f), Size(cellSize, cellSize))
              
              // Corner Finder 3
              drawRect(androidx.compose.ui.graphics.Color(0xFF202124), Offset(0f, cellSize * (cols - 4)), Size(cellSize * 4, cellSize * 4))
              drawRect(Color.White, Offset(cellSize, cellSize * (cols - 3)), Size(cellSize * 2, cellSize * 2))
              drawRect(androidx.compose.ui.graphics.Color(0xFF202124), Offset(cellSize * 1.5f, cellSize * (cols - 2.5f)), Size(cellSize, cellSize))

              for (r in 0 until cols) {
                for (c in 0 until cols) {
                  if ((r < 4 && c < 4) || (r < 4 && c >= cols - 4) || (r >= cols - 4 && c < 4)) {
                    continue
                  }
                  if ((r * c + r * 9 + c * 17) % 3 == 0 || (r + c) % 5 == 0) {
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

          Spacer(modifier = Modifier.height(MaterialTheme.spacing.medium))
          Text(
            text = "Tunjukkan QR untuk Check-in",
            style = DiajakDesignSystem.Typography.Body,
            color = MaterialTheme.colorScheme.onSurface,
            textAlign = TextAlign.Center
          )
        }
        
        Spacer(modifier = Modifier.height(20.dp))
        Spacer(modifier = Modifier.height(160.dp)) // Extra space to scroll past floating bottom bar
      }
    }

    // 2. Glass Header Layer
    DiajakGlassHeader(
      hazeState = hazeState,
      modifier = Modifier.align(Alignment.TopCenter).zIndex(10f)
    ) {
      // Content Layer (Buttons and Title) MUST be 56.dp height
      Box(
        modifier = Modifier.fillMaxWidth().statusBarsPadding().height(56.dp).padding(horizontal = 20.dp)
      ) {
        // Circular Back Button (Left)
        IconButton(
          onClick = onDismiss,
          modifier = Modifier
            .size(40.dp)
            .align(Alignment.CenterStart)
            .diajakGlassButton(hazeState)
        ) {
          Icon(
            imageVector = Icons.AutoMirrored.Outlined.ArrowBack, 
            contentDescription = "Kembali",
            tint = MaterialTheme.colorScheme.onSurface
          )
        }

        // Title (STRICTLY CENTERED WITH HEADLINE TYPOGRAPHY)
        Text(
          text = "Detail Undangan",
          style = DiajakDesignSystem.Typography.Headline,
          color = MaterialTheme.colorScheme.onSurface,
          textAlign = TextAlign.Center,
          modifier = Modifier.align(Alignment.Center).fillMaxWidth()
        )
      }
    }

    // 3. Floating Transparent Bottom Bar
    Box(
      modifier = Modifier
        .align(Alignment.BottomCenter)
        .fillMaxWidth()
        .background(Color.Transparent)
        .navigationBarsPadding()
        .padding(horizontal = 20.dp, vertical = 20.dp)
    ) {
      Column(
        modifier = Modifier.fillMaxWidth(),
        verticalArrangement = Arrangement.spacedBy(MaterialTheme.spacing.medium)
      ) {
        // Save to Gallery Button
        OutlinedButton(
          onClick = {
            Toast.makeText(context, "Undangan berhasil disimpan ke galeri ponsel! 📸", Toast.LENGTH_LONG).show()
          },
          modifier = Modifier
            .fillMaxWidth()
            .height(52.dp),
          shape = RoundedCornerShape(16.dp),
          colors = ButtonDefaults.outlinedButtonColors(containerColor = Color.White, contentColor = DiajakOrange),
          elevation = ButtonDefaults.buttonElevation(defaultElevation = 0.dp),
          border = BorderStroke(1.5.dp, DiajakOrange)
        ) {
          Text("Simpan", color = DiajakOrange, style = androidx.compose.ui.text.TextStyle(fontSize = 16.sp, fontWeight = androidx.compose.ui.text.font.FontWeight.Bold, fontFamily = com.example.ui.theme.AppFontFamily))
        }

        // Contact Host Button
        Button(
          onClick = {
            onChatWithKreator(booking.activityTitle)
          },
          modifier = Modifier
            .fillMaxWidth()
            .height(52.dp),
          shape = RoundedCornerShape(16.dp),
          colors = ButtonDefaults.buttonColors(containerColor = DiajakOrange),
          elevation = ButtonDefaults.buttonElevation(defaultElevation = 0.dp)
        ) {
          Text("Hubungi Kreator", color = Color.White, style = androidx.compose.ui.text.TextStyle(fontSize = 16.sp, fontWeight = androidx.compose.ui.text.font.FontWeight.Bold, fontFamily = com.example.ui.theme.AppFontFamily))
        }
      }
    }
  }
}
