package com.example.ui.screens
import com.example.ui.theme.spacing
import androidx.compose.material3.MaterialTheme

import androidx.compose.foundation.Image
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.border
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Dialog
import com.example.model.ActivityModel
import com.example.ui.theme.DiajakOrange
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.Place
import androidx.compose.material.icons.outlined.AccessTime

@Composable
fun BookingConfirmationDialog(
  activity: ActivityModel,
  onDismiss: () -> Unit,
  onConfirm: (pesertaCount: Int) -> Unit
) {
  val screenWidth = androidx.compose.ui.platform.LocalConfiguration.current.screenWidthDp.dp
  val favoriteImageSize = (screenWidth - 60.dp) / 2
  val targetImageSize = favoriteImageSize / 2
  
  var count by remember { mutableStateOf(1) }
  val maxSlots = (activity.maxPeserta - activity.currentPeserta).coerceAtLeast(1)

  Dialog(onDismissRequest = onDismiss) {
    Surface(
      shape = RoundedCornerShape(16.dp),
      color = Color.White
    ) {
      Column(
        modifier = Modifier.padding(MaterialTheme.spacing.large),
        horizontalAlignment = Alignment.CenterHorizontally
      ) {
        Text("Konfirmasi Kehadiran", style = com.example.ui.theme.DiajakDesignSystem.Typography.TitleBold, color = MaterialTheme.colorScheme.onSurface)
        Spacer(modifier = Modifier.height(MaterialTheme.spacing.extraSmall))
        Text("Pilih jumlah undangan yang ingin dipesan", style = MaterialTheme.typography.labelLarge, color = MaterialTheme.colorScheme.onSurface)

        Spacer(modifier = Modifier.height(MaterialTheme.spacing.medium))

        Row(
          modifier = Modifier.fillMaxWidth(),
          verticalAlignment = Alignment.Top
        ) {
          Box(modifier = Modifier.size(targetImageSize).clip(RoundedCornerShape(12.dp))) {
            Image(
              painter = painterResource(id = activity.imageResId),
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
              text = activity.title,
              style = com.example.ui.theme.DiajakDesignSystem.Typography.BodyBold,
              color = MaterialTheme.colorScheme.onSurface,
              maxLines = 1,
              overflow = TextOverflow.Ellipsis
            )
            Row(verticalAlignment = Alignment.Top) {
              Icon(
                androidx.compose.material.icons.Icons.Outlined.Place,
                contentDescription = null,
                tint = MaterialTheme.colorScheme.onSurface,
                modifier = Modifier.padding(top = 2.dp).size(18.dp)
              )
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
              Icon(
                androidx.compose.material.icons.Icons.Outlined.AccessTime,
                contentDescription = null,
                tint = MaterialTheme.colorScheme.onSurface,
                modifier = Modifier.size(18.dp)
              )
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

        Spacer(modifier = Modifier.height(MaterialTheme.spacing.section))

        // Peserta Stepper
        Row(
          verticalAlignment = Alignment.CenterVertically,
          horizontalArrangement = Arrangement.Center
        ) {
          IconButton(
            onClick = { if (count > 1) count-- },
            modifier = Modifier.size(40.dp).background(MaterialTheme.colorScheme.outlineVariant, RoundedCornerShape(16.dp))
          ) {
            Text("–", style = com.example.ui.theme.DiajakDesignSystem.Typography.Body)
          }

          Spacer(modifier = Modifier.width(20.dp))
          Text("$count Peserta", style = com.example.ui.theme.DiajakDesignSystem.Typography.Body)
          Spacer(modifier = Modifier.width(20.dp))

          IconButton(
            onClick = { if (count < maxSlots) count++ },
            modifier = Modifier.size(40.dp).background(MaterialTheme.colorScheme.outlineVariant, RoundedCornerShape(16.dp))
          ) {
            Text("+", style = com.example.ui.theme.DiajakDesignSystem.Typography.Body)
          }
        }

        Spacer(modifier = Modifier.height(20.dp))

        Row(
          modifier = Modifier.fillMaxWidth(),
          horizontalArrangement = Arrangement.End
        ) {
          val totalStr = if (activity.priceValue == 0) "Gratis" else "Rp ${String.format("%,d", activity.priceValue * count).replace(',', '.')}"
          Text(totalStr, style = com.example.ui.theme.DiajakDesignSystem.Typography.Body, color = DiajakOrange)
        }

        Spacer(modifier = Modifier.height(20.dp))

        Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.spacedBy(MaterialTheme.spacing.small)) {
          OutlinedButton(
            onClick = onDismiss,
            modifier = Modifier.weight(1f).height(52.dp),
            shape = RoundedCornerShape(16.dp),
            border = BorderStroke(1.5.dp, DiajakOrange),
            colors = ButtonDefaults.outlinedButtonColors(containerColor = Color.Transparent)
          ) {
            Text("Batal", color = DiajakOrange, style = MaterialTheme.typography.labelLarge)
          }

          Button(
            onClick = { onConfirm(count) },
            modifier = Modifier.weight(1f).height(52.dp),
            shape = RoundedCornerShape(16.dp),
            colors = ButtonDefaults.buttonColors(containerColor = DiajakOrange)
          ) {
            Text("Konfirmasi", style = com.example.ui.theme.DiajakDesignSystem.Typography.Body)
          }
        }
      }
    }
  }
}
