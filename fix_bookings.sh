cat << 'INNER_EOF' > app/src/main/java/com/example/ui/screens/BookingsScreen.kt
package com.example.ui.screens

import android.widget.Toast
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
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.platform.LocalContext
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
      // ALWAYS use 80.dp spacer for the content (56.dp header + 24.dp gap)
      Spacer(modifier = Modifier.statusBarsPadding().height(80.dp))

      if (isLoading) {
        Column(
          modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 20.dp, bottom = 100.dp),
          verticalArrangement = Arrangement.spacedBy(MaterialTheme.spacing.medium)
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
            Spacer(modifier = Modifier.height(20.dp))
            Text(
              text = "Belum Ada Undangan Aktif",
              style = DiajakDesignSystem.Typography.Body,
              color = MaterialTheme.colorScheme.onSurface
            )
            Spacer(modifier = Modifier.height(MaterialTheme.spacing.small))
            Text(
              text = "Yuk cari acara seru di sekitarmu dan daftar sekarang!",
              style = DiajakDesignSystem.Typography.Body, 
              color = MaterialTheme.colorScheme.onSurface,
              textAlign = TextAlign.Center
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
              Text("Temukan Acara", style = DiajakDesignSystem.Typography.TitleBold)
            }
          }
        }
      } else {
        Column(
          modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 20.dp, bottom = 100.dp),
          verticalArrangement = Arrangement.spacedBy(MaterialTheme.spacing.medium)
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
                      contentDescription = booking.acaraTitle,
                      modifier = Modifier.fillMaxSize(),
                      contentScale = ContentScale.Crop
                    )
                  }
                  Spacer(modifier = Modifier.width(MaterialTheme.spacing.medium))
                  Column {
                    Text(
                      text = booking.acaraTitle,
                      style = DiajakDesignSystem.Typography.TitleMedium,
                      color = MaterialTheme.colorScheme.onSurface,
                      maxLines = 1,
                      overflow = TextOverflow.Ellipsis
                    )
                    Spacer(modifier = Modifier.height(4.dp))
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
                    Spacer(modifier = Modifier.height(4.dp))
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
                  Column(verticalArrangement = Arrangement.spacedBy(MaterialTheme.spacing.extraSmall)) {
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

    // 2. Glass Header Layer
    DiajakGlassHeader(
      hazeState = hazeState,
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
        // Title (STRICTLY CENTERED WITH HEADLINE TYPOGRAPHY)
        Text(
          text = "Undangan Acara",
          style = DiajakDesignSystem.Typography.Headline,
          color = MaterialTheme.colorScheme.onSurface,
          textAlign = TextAlign.Center,
          modifier = Modifier.fillMaxWidth()
        )
      }
    }
  }
}
INNER_EOF

# Append the dialog part back to the file!
cat dialog.txt >> app/src/main/java/com/example/ui/screens/BookingsScreen.kt
