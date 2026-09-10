package com.example.ui.screens

import com.example.ui.theme.AppSpacing

import android.widget.Toast
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.outlined.ArrowBack
import androidx.compose.material.icons.outlined.CheckCircle
import androidx.compose.material.icons.outlined.CloudUpload
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.zIndex
import com.example.ui.components.DiajakGlassConfig
import com.example.ui.components.DiajakGlassHeader
import com.example.ui.theme.DiajakDesignSystem
import com.example.ui.theme.diajakGlassButton
import com.example.ui.theme.DiajakOrange
import com.example.ui.theme.spacing
import com.example.ui.viewmodel.DiajakViewModel
import dev.chrisbanes.haze.HazeState
import dev.chrisbanes.haze.hazeEffect
import dev.chrisbanes.haze.hazeSource
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun DocumentUploadScreen(
  viewModel: DiajakViewModel,
  onBack: () -> Unit
) {
  val context = LocalContext.current
  val coroutineScope = rememberCoroutineScope()
  val hazeState = remember { HazeState() }
  val scrollState = rememberScrollState()
  
  val isKtpUploaded by viewModel.isKtpUploaded.collectAsState()
  val isBukuTabunganUploaded by viewModel.isBukuTabunganUploaded.collectAsState()
  var ktpUri by remember { mutableStateOf<android.net.Uri?>(null) }
  var bukuTabunganUri by remember { mutableStateOf<android.net.Uri?>(null) }
  var isUploading by remember { mutableStateOf(false) }

  val ktpLauncher = androidx.activity.compose.rememberLauncherForActivityResult(
    contract = androidx.activity.result.contract.ActivityResultContracts.GetContent()
  ) { uri ->
    if (uri != null) {
      coroutineScope.launch {
        isUploading = true
        delay(800) // Simulate upload delay
        ktpUri = uri
        viewModel.setKtpUploaded(true)
        isUploading = false
        Toast.makeText(context, "KTP berhasil diupload", Toast.LENGTH_SHORT).show()
      }
    }
  }

  val bukuTabunganLauncher = androidx.activity.compose.rememberLauncherForActivityResult(
    contract = androidx.activity.result.contract.ActivityResultContracts.GetContent()
  ) { uri ->
    if (uri != null) {
      coroutineScope.launch {
        isUploading = true
        delay(800) // Simulate upload delay
        bukuTabunganUri = uri
        viewModel.setBukuTabunganUploaded(true)
        isUploading = false
        Toast.makeText(context, "Buku Tabungan berhasil diupload", Toast.LENGTH_SHORT).show()
      }
    }
  }

  Box(
    modifier = Modifier
      .fillMaxSize()
      .background(Color(0xFFE5E7EB))
  ) {
    // 1. Content Layer (hazeSource)
    Column(
      modifier = Modifier
        .fillMaxSize()
        .hazeSource(state = hazeState)
        .verticalScroll(scrollState)
        .padding(horizontal = 20.dp),
      verticalArrangement = Arrangement.spacedBy(AppSpacing.Medium)
    ) {
      // 56.dp header + 24.dp gap = 80.dp
      Spacer(modifier = Modifier.statusBarsPadding().height(80.dp))

      Text(
        text = "Upload dokumen berikut untuk keperluan verifikasi akun dan pencairan dana Anda.",
        style = DiajakDesignSystem.Typography.Body,
        color = MaterialTheme.colorScheme.onSurfaceVariant
      )
      
      // KTP Upload Section
      DocumentUploadCard(
        title = "Kartu Tanda Penduduk (KTP)",
        description = "Pastikan foto KTP terlihat jelas, tidak terpotong, dan tulisan dapat dibaca.",
        isUploaded = isKtpUploaded,
        onUploadClick = {
          ktpLauncher.launch("image/*")
        }
      )
      
      // Buku Tabungan Upload Section
      DocumentUploadCard(
        title = "Buku Tabungan / Rekening",
        description = "Upload foto bagian depan buku tabungan yang menampilkan nama dan nomor rekening.",
        isUploaded = isBukuTabunganUploaded,
        onUploadClick = {
          bukuTabunganLauncher.launch("image/*")
        }
      )
      
      Spacer(modifier = Modifier.height(MaterialTheme.spacing.medium))
      
      Button(
        onClick = {
          Toast.makeText(context, "Dokumen berhasil disimpan!", Toast.LENGTH_SHORT).show()
          onBack()
        },
        modifier = Modifier
          .fillMaxWidth()
          .height(52.dp),
        enabled = isKtpUploaded && isBukuTabunganUploaded,
        colors = ButtonDefaults.buttonColors(
          containerColor = DiajakOrange,
          disabledContainerColor = Color(0xFFE2E8F0)
        ),
        shape = RoundedCornerShape(16.dp)
      ) {
        if (isUploading) {
          CircularProgressIndicator(color = Color.White, modifier = Modifier.size(24.dp), strokeWidth = 2.dp)
        } else {
          Text(
            "Simpan Dokumen",
            color = if (isKtpUploaded && isBukuTabunganUploaded) Color.White else Color(0xFF94A3B8),
            style = androidx.compose.ui.text.TextStyle(fontSize = 16.sp, fontWeight = FontWeight.Bold, fontFamily = com.example.ui.theme.AppFontFamily)
          )
        }
      }

      Spacer(modifier = Modifier.navigationBarsPadding().height(24.dp))
    }

    // 2. Glass Header Layer
    DiajakGlassHeader(
      hazeState = hazeState,
      scrollState = scrollState,
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
          text = "Upload Dokumen",
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

@Composable
fun DocumentUploadCard(
  title: String,
  description: String,
  isUploaded: Boolean,
  onUploadClick: () -> Unit
) {
  Surface(
    modifier = Modifier.fillMaxWidth(),
    shape = RoundedCornerShape(16.dp),
    color = Color.White,
    shadowElevation = 0.dp,
    border = null
  ) {
    Column(
      modifier = Modifier
        .fillMaxWidth()
        .padding(20.dp)
    ) {
      Text(
        text = title,
        style = DiajakDesignSystem.Typography.TitleBold,
        color = MaterialTheme.colorScheme.onSurface
      )
      Spacer(modifier = Modifier.height(MaterialTheme.spacing.extraSmall))
      Text(
        text = description,
        style = DiajakDesignSystem.Typography.Body,
        color = MaterialTheme.colorScheme.onSurfaceVariant
      )
      Spacer(modifier = Modifier.height(MaterialTheme.spacing.medium))
      
      Box(
        modifier = Modifier
          .fillMaxWidth()
          .height(120.dp)
          .clip(RoundedCornerShape(12.dp))
          .background(if (isUploaded) Color(0xFFF0FDF4) else Color(0xFFFFF7ED))
          .border(
            width = 1.5.dp,
            color = if (isUploaded) Color(0xFF22C55E) else DiajakOrange.copy(alpha = 0.5f),
            shape = RoundedCornerShape(12.dp)
          )
          .clickable(enabled = !isUploaded, onClick = onUploadClick),
        contentAlignment = Alignment.Center
      ) {
        if (isUploaded) {
          Column(horizontalAlignment = Alignment.CenterHorizontally) {
            Icon(
              imageVector = Icons.Outlined.CheckCircle,
              contentDescription = "Success",
              tint = Color(0xFF22C55E),
              modifier = Modifier.size(40.dp)
            )
            Spacer(modifier = Modifier.height(MaterialTheme.spacing.small))
            Text("Dokumen berhasil diupload", color = Color(0xFF16A34A), style = DiajakDesignSystem.Typography.TitleBold)
          }
        } else {
          Column(horizontalAlignment = Alignment.CenterHorizontally) {
            Icon(
              imageVector = Icons.Outlined.CloudUpload,
              contentDescription = "Upload",
              tint = DiajakOrange,
              modifier = Modifier.size(40.dp)
            )
            Spacer(modifier = Modifier.height(MaterialTheme.spacing.small))
            Text("Tap untuk upload dokumen", color = DiajakOrange, style = DiajakDesignSystem.Typography.TitleBold)
          }
        }
      }
    }
  }
}
