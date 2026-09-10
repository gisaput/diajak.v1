package com.example.ui.screens

import com.example.ui.theme.AppSpacing
import com.example.ui.theme.spacing
import com.example.ui.theme.diajakGlassButton
import androidx.compose.material3.MaterialTheme

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.background
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.outlined.*
import androidx.compose.material.icons.outlined.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.rotate
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
fun HelpCenterScreen(
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
        // Intro Text
        Column(
          modifier = Modifier.fillMaxWidth(),
          verticalArrangement = Arrangement.spacedBy(AppSpacing.Small)
        ) {
        Text(
          text = "Ada pertanyaan mengenai layanan kami?",
          style = com.example.ui.theme.DiajakDesignSystem.Typography.TitleBold,
          color = MaterialTheme.colorScheme.onSurface
        )
        Text(
          text = "Kami merangkum beberapa pertanyaan yang paling sering diajukan untuk membantu Anda memahami dan memaksimalkan penggunaan aplikasi ini.",
          style = com.example.ui.theme.DiajakDesignSystem.Typography.Body,
          color = MaterialTheme.colorScheme.onSurfaceVariant,
          lineHeight = 22.sp
        )
      }

      Spacer(modifier = Modifier.height(MaterialTheme.spacing.small))

      Text(
        text = "Pertanyaan Populer",
        style = com.example.ui.theme.DiajakDesignSystem.Typography.BodyBold,
        color = MaterialTheme.colorScheme.onSurface,
        modifier = Modifier.fillMaxWidth()
      )

      // FAQ List
      FaqItem(
        question = "Apa fungsi utama aplikasi ini?",
        answer = "Aplikasi ini adalah platform komunitas yang memudahkan pengguna untuk berinteraksi, membuat, dan mengikuti berbagai aktivitas menarik di sekitar mereka. Kami menghubungkan para pencipta aktivitas (Kreator) dengan peserta yang ingin mencari pengalaman baru."
      )

      FaqItem(
        question = "Bagaimana cara mengikuti aktivitas di aplikasi?",
        answer = "Untuk mengikuti aktivitas, cari aktivitas yang Anda minati di halaman beranda. Klik pada aktivitas tersebut untuk melihat detailnya, kemudian klik tombol 'Ikuti' atau 'Pesan Undangan'. Status pemesanan Anda akan otomatis disimpan di tab Pesanan Anda."
      )

      FaqItem(
        question = "Bagaimana cara menjadi Kreator di platform ini?",
        answer = "Anda bisa menjadi Kreator dengan masuk ke tab 'Profil' Anda, kemudian aktifkan tombol toggle 'Menjadi Kreator'. Setelah itu, Anda akan memiliki akses ke Dashboard Kreator untuk membuat, mengelola, dan mempublikasikan aktivitas buatan Anda sendiri."
      )

      FaqItem(
        question = "Apakah ada biaya untuk menggunakan aplikasi ini?",
        answer = "Mengunduh dan mendaftar akun di aplikasi ini 100% gratis. Biaya hanya dikenakan jika Anda memesan aktivitas berbayar yang diselenggarakan oleh Kreator tertentu. Detail pembayaran akan selalu tertera secara transparan di setiap deskripsi aktivitas."
      )

      FaqItem(
        question = "Apa arti status tombol pada halaman detail aktivitas?",
        answer = "Tombol pendaftaran memiliki beberapa status:\n" +
                "• Ikuti: Anda dapat mendaftar karena kuota masih tersedia dan jadwal belum terlewat.\n" +
                "• Tunggu: Pendaftaran ditunda karena Kreator belum melengkapi dokumen (KTP/Buku Tabungan), tidak aktif update jadwal selama 7 hari, belum menentukan jadwal, atau semua jadwal yang ada telah lewat.\n" +
                "• Penuh: Kuota maksimal peserta untuk aktivitas ini sudah terpenuhi.\n" +
                "• Berjalan: Aktivitas sedang berlangsung pada hari ini. Pendaftaran tetap bisa dilakukan jika kuota masih tersedia, hingga jam aktivitas selesai."
      )

      FaqItem(
        question = "Bagaimana cara membatalkan keikutsertaan aktivitas?",
        answer = "Buka menu 'Pesanan Saya' di aplikasi, pilih aktivitas yang ingin Anda batalkan, lalu klik opsi 'Batalkan Pesanan'. Kebijakan pengembalian dana (jika aktivitas tersebut berbayar) akan disesuaikan dengan syarat ketentuan yang ditetapkan oleh masing-masing Kreator."
      )

      FaqItem(
        question = "Bagaimana cara menghubungi Layanan Pelanggan kami?",
        answer = "Jika Anda memiliki kendala teknis atau pertanyaan lebih lanjut yang tidak terjawab di FAQ ini, Anda dapat mengirimkan email dukungan ke customer service melalui kontak bantuan resmi. Tim kami siap membantu Anda kapan saja!"
      )

      Spacer(modifier = Modifier.height(MaterialTheme.spacing.medium))
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
          text = "Pusat Bantuan / FAQ",
          style = com.example.ui.theme.DiajakDesignSystem.Typography.Headline,
          color = MaterialTheme.colorScheme.onSurface,
          textAlign = androidx.compose.ui.text.style.TextAlign.Center,
          modifier = Modifier.align(Alignment.Center).fillMaxWidth()
        )
      }
    }
  }
}

@Composable
fun FaqItem(
  question: String,
  answer: String
) {
  var expanded by remember { mutableStateOf(false) }
  val rotationState by animateFloatAsState(targetValue = if (expanded) 180f else 0f)

  Column(
    modifier = Modifier
      .fillMaxWidth()
      .clickable { expanded = !expanded }
      .padding(vertical = MaterialTheme.spacing.medium)
  ) {
    Row(
      modifier = Modifier.fillMaxWidth(),
      horizontalArrangement = Arrangement.SpaceBetween,
      verticalAlignment = Alignment.CenterVertically
    ) {
      Text(
        text = question,
        style = com.example.ui.theme.DiajakDesignSystem.Typography.BodyBold,
        color = MaterialTheme.colorScheme.onSurface,
        modifier = Modifier.weight(1f)
      )
      Icon(
        imageVector = Icons.Outlined.ExpandMore,
        contentDescription = if (expanded) "Sembunyikan" else "Tampilkan",
        tint = MaterialTheme.colorScheme.onSurface,
        modifier = Modifier.rotate(rotationState)
      )
    }
    
    AnimatedVisibility(visible = expanded) {
      Column {
        Spacer(modifier = Modifier.height(MaterialTheme.spacing.medium))
        Text(
          text = answer,
          style = com.example.ui.theme.DiajakDesignSystem.Typography.Body,
          color = MaterialTheme.colorScheme.onSurfaceVariant,
          lineHeight = 22.sp
        )
      }
    }
  }
}

