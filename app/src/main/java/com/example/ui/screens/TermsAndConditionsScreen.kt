package com.example.ui.screens

import com.example.ui.theme.AppSpacing
import com.example.ui.theme.spacing
import com.example.ui.theme.diajakGlassButton
import androidx.compose.material3.MaterialTheme

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.background
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.outlined.*
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

import androidx.compose.ui.zIndex
import dev.chrisbanes.haze.HazeState
import dev.chrisbanes.haze.hazeSource
import dev.chrisbanes.haze.hazeEffect
import com.example.ui.components.DiajakGlassHeader
import androidx.compose.ui.draw.clip
import androidx.compose.foundation.border
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.runtime.remember

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun TermsAndConditionsScreen(
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
        .verticalScroll(scrollState),
      horizontalAlignment = Alignment.CenterHorizontally
    ) {
      Spacer(modifier = Modifier.statusBarsPadding().height(80.dp))
      
      Column(
        modifier = Modifier
          .fillMaxWidth()
          .padding(start = 20.dp, end = 20.dp, bottom = MaterialTheme.spacing.medium),
        verticalArrangement = Arrangement.spacedBy(AppSpacing.Medium)
      ) {
          Text(
            text = "Selamat datang di platform kami. Sebelum menggunakan layanan kami, harap luangkan waktu untuk membaca Syarat & Ketentuan Layanan ini. Syarat & Ketentuan ini mengatur akses dan penggunaan Anda atas aplikasi, situs web, serta layanan yang disediakan oleh kami.",
            style = com.example.ui.theme.DiajakDesignSystem.Typography.Body,
            color = MaterialTheme.colorScheme.onSurface,
            lineHeight = 22.sp
          )
          
          Spacer(modifier = Modifier.height(MaterialTheme.spacing.extraSmall))

          Text(
            text = "1. Ketentuan Akun Pengguna",
            style = com.example.ui.theme.DiajakDesignSystem.Typography.BodyBold,
            color = MaterialTheme.colorScheme.onSurface
          )
          Text(
            text = "• Anda harus memberikan informasi yang akurat, lengkap, dan terbaru saat membuat akun di aplikasi ini.\n" +
                   "• Anda bertanggung jawab penuh untuk menjaga kerahasiaan informasi akun dan kata sandi Anda.\n" +
                   "• Kami berhak untuk menangguhkan atau mengakhiri akun Anda jika ditemukan adanya pelanggaran hukum atau manipulasi informasi.",
            style = com.example.ui.theme.DiajakDesignSystem.Typography.Body,
            color = MaterialTheme.colorScheme.onSurfaceVariant,
            lineHeight = 22.sp
          )
          
          Spacer(modifier = Modifier.height(MaterialTheme.spacing.extraSmall))

          Text(
            text = "2. Ketentuan Pendaftaran & Pemesanan",
            style = com.example.ui.theme.DiajakDesignSystem.Typography.BodyBold,
            color = MaterialTheme.colorScheme.onSurface
          )
          Text(
            text = "• Pengguna dapat mendaftar dan memesan undangan atau slot untuk berbagai aktivitas yang diselenggarakan oleh Kreator.\n" +
                   "• Setiap undangan yang dipesan bersifat pribadi dan tunduk pada kebijakan pembatalan masing-masing aktivitas.\n" +
                   "• Pembayaran atau deposit (jika ada) harus diselesaikan sesuai instruksi untuk memastikan slot pemesanan Anda aman.",
            style = com.example.ui.theme.DiajakDesignSystem.Typography.Body,
            color = MaterialTheme.colorScheme.onSurfaceVariant,
            lineHeight = 22.sp
          )
          
          Spacer(modifier = Modifier.height(MaterialTheme.spacing.extraSmall))

          Text(
            text = "3. Hak & Tanggung Jawab Kreator",
            style = com.example.ui.theme.DiajakDesignSystem.Typography.BodyBold,
            color = MaterialTheme.colorScheme.onSurface
          )
          Text(
            text = "• Kreator bertanggung jawab penuh atas keakuratan deskripsi aktivitas, pelaksanaan aktivitas, dan keselamatan peserta selama aktivitas berlangsung.\n" +
                   "• Kreator wajib mematuhi aturan komunitas kami dan dilarang mempublikasikan aktivitas yang melanggar hukum, SARA, atau norma kesusilaan.",
            style = com.example.ui.theme.DiajakDesignSystem.Typography.Body,
            color = MaterialTheme.colorScheme.onSurfaceVariant,
            lineHeight = 22.sp
          )
          
          Spacer(modifier = Modifier.height(MaterialTheme.spacing.extraSmall))

          Text(
            text = "4. Batasan Tanggung Jawab",
            style = com.example.ui.theme.DiajakDesignSystem.Typography.BodyBold,
            color = MaterialTheme.colorScheme.onSurface
          )
          Text(
            text = "Kami bertindak sebagai platform penghubung antara Kreator dan Peserta. Kami tidak bertanggung jawab atas kerugian fisik, materiil, atau moral yang timbul dari interaksi langsung atau pelaksanaan aktivitas di lapangan.",
            style = com.example.ui.theme.DiajakDesignSystem.Typography.Body,
            color = MaterialTheme.colorScheme.onSurfaceVariant,
            lineHeight = 22.sp
          )
          
          Spacer(modifier = Modifier.height(MaterialTheme.spacing.extraSmall))

          Text(
            text = "Dengan terus menggunakan aplikasi ini, Anda secara sadar setuju untuk terikat oleh seluruh Syarat & Ketentuan yang berlaku. Syarat & Ketentuan ini dapat kami perbarui dari waktu ke waktu demi kenyamanan dan keamanan bersama.",
            style = com.example.ui.theme.DiajakDesignSystem.Typography.Body,
            color = MaterialTheme.colorScheme.onSurface,
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
          text = "Syarat & Ketentuan",
          style = com.example.ui.theme.DiajakDesignSystem.Typography.Headline,
          color = MaterialTheme.colorScheme.onSurface,
          textAlign = androidx.compose.ui.text.style.TextAlign.Center,
          modifier = Modifier.align(Alignment.Center).fillMaxWidth()
        )
      }
    }
  }
}
