package com.example.ui.screens

import com.example.ui.theme.AppSpacing
import com.example.ui.theme.spacing
import com.example.ui.theme.diajakGlassButton
import androidx.compose.material3.MaterialTheme

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.background
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.foundation.border
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.outlined.*
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.zIndex
import dev.chrisbanes.haze.HazeState
import dev.chrisbanes.haze.hazeSource
import dev.chrisbanes.haze.hazeEffect
import com.example.ui.components.DiajakGlassHeader

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AboutDiajakScreen(
  onBack: () -> Unit
) {
  val scrollState = rememberScrollState()

  val hazeState = remember { HazeState() }

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
    ) {
      Spacer(modifier = Modifier.statusBarsPadding().height(80.dp))
      
      Column(
        modifier = Modifier
          .fillMaxWidth()
          .padding(start = 20.dp, end = 20.dp, bottom = MaterialTheme.spacing.medium),
        verticalArrangement = Arrangement.spacedBy(AppSpacing.Medium)
      ) {
        Text(
          text = "Platform sosial inovatif ini dirancang untuk menghubungkan peserta-peserta melalui hobi, aktivitas seru di sekitar mereka. Kami percaya bahwa setiap peserta berhak menemukan komunitas yang mendukung, teman baru yang asyik, serta pengalaman hidup yang berkesan.",
          style = com.example.ui.theme.DiajakDesignSystem.Typography.Body,
          color = MaterialTheme.colorScheme.onSurface,
          lineHeight = 22.sp
        )
            
        Text(
          text = "Melalui aplikasi ini, Anda dapat menjelajahi berbagai kategori aktivitas seperti olahraga, seni, kuliner, petualangan alam, teknologi, hingga kumpul santai. Anda juga dapat mendaftar sebagai kreator (penyelenggara) untuk membuat aktivitas sendiri, mengelola peserta, dan membangun komunitas Anda sendiri secara aman dan terverifikasi.",
          style = com.example.ui.theme.DiajakDesignSystem.Typography.Body,
          color = MaterialTheme.colorScheme.onSurfaceVariant,
          lineHeight = 22.sp
        )
            
        Text(
          text = "Misi utama kami adalah mempermudah interaksi sosial yang sehat, aktif, dan bermakna di dunia nyata. Temukan hobi barumu, perluas lingkaran pertemananmu, dan mulailah petualangan barumu hari ini bersama kami!",
          style = com.example.ui.theme.DiajakDesignSystem.Typography.Body,
          color = MaterialTheme.colorScheme.onSurfaceVariant,
          lineHeight = 22.sp
        )
      }
    }

    // 2. Glass Header Layer
    DiajakGlassHeader(
      hazeState = hazeState,
      scrollState = scrollState,
      modifier = Modifier.align(Alignment.TopCenter).zIndex(10f)
    ) {
      Box(
        modifier = Modifier.fillMaxWidth().statusBarsPadding().height(56.dp).padding(horizontal = 20.dp)
      ) {
        // Back Button
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

        // Title
        Text(
          text = "Tentang Aplikasi",
          style = com.example.ui.theme.DiajakDesignSystem.Typography.Headline,
          color = MaterialTheme.colorScheme.onSurface,
          textAlign = androidx.compose.ui.text.style.TextAlign.Center,
          modifier = Modifier.align(Alignment.Center).fillMaxWidth()
        )
      }
    }
  }
}
