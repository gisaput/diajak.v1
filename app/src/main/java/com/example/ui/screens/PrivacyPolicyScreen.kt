package com.example.ui.screens

import com.example.ui.theme.diajakGlassButton
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.outlined.ArrowBack
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.zIndex
import com.example.ui.theme.spacing

import androidx.compose.ui.graphics.CompositingStrategy
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.graphics.BlendMode
import androidx.compose.ui.draw.drawWithContent
import androidx.compose.ui.draw.clip
import androidx.compose.foundation.border

import dev.chrisbanes.haze.HazeState
import dev.chrisbanes.haze.HazeStyle
import dev.chrisbanes.haze.HazeTint
import dev.chrisbanes.haze.hazeSource
import dev.chrisbanes.haze.hazeEffect

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun PrivacyPolicyScreen(
    onBack: () -> Unit
) {
    val scrollState = rememberScrollState()
    val hazeState = remember { HazeState() }

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(Color(0xFFE5E7EB)) // Standard Diajak background
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .hazeSource(state = hazeState)
                .verticalScroll(scrollState),
            horizontalAlignment = Alignment.CenterHorizontally,
        ) {
            // Spacer for TopBar
            Spacer(modifier = Modifier.statusBarsPadding().height(com.example.ui.theme.DiajakDesignSystem.Header.HeaderTotalTopPadding))
            
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(start = 20.dp, end = 20.dp, top = 0.dp, bottom = MaterialTheme.spacing.medium),
                verticalArrangement = Arrangement.spacedBy(MaterialTheme.spacing.medium)
            ) {
                Text(
                    text = "Selamat datang di Kebijakan Privasi kami. Kami sangat menghargai kepercayaan Anda dan berkomitmen penuh untuk melindungi privasi serta keamanan data pribadi Anda. Kebijakan ini menjelaskan bagaimana kami mengumpulkan, menggunakan, menyimpan, dan membagikan informasi Anda saat menggunakan aplikasi ini.",
                    style = com.example.ui.theme.DiajakDesignSystem.Typography.Body,
                    color = MaterialTheme.colorScheme.onSurface,
                    lineHeight = 22.sp
                )
                
                Spacer(modifier = Modifier.height(MaterialTheme.spacing.small))
                Text(
                    text = "1. Informasi yang Kami Kumpulkan",
                    style = com.example.ui.theme.DiajakDesignSystem.Typography.BodyBold,
                    color = MaterialTheme.colorScheme.onSurface
                )
                Text(
                    text = "• Data Akun: Nama lengkap, alamat email, foto profil, dan kata sandi saat Anda mendaftar.\n" +
                           "• Data Lokasi: Informasi GPS presisi atau kasar untuk merekomendasikan aktivitas terdekat (hanya jika Anda memberikan izin akses lokasi).\n" +
                           "• Informasi Aktivitas: Riwayat pemesanan aktivitas, daftar favorit, serta percakapan obrolan dengan kreator atau sesama peserta.",
                    style = com.example.ui.theme.DiajakDesignSystem.Typography.Body,
                    color = MaterialTheme.colorScheme.onSurfaceVariant,
                    lineHeight = 22.sp
                )
                
                Spacer(modifier = Modifier.height(MaterialTheme.spacing.small))
                Text(
                    text = "2. Penggunaan Informasi",
                    style = com.example.ui.theme.DiajakDesignSystem.Typography.BodyBold,
                    color = MaterialTheme.colorScheme.onSurface
                )
                Text(
                    text = "Kami menggunakan data Anda untuk mengelola akun, memverifikasi pemesanan undangan, memfasilitasi komunikasi dalam aplikasi, serta merekomendasikan konten aktivitas yang relevan sesuai minat dan lokasi Anda.",
                    style = com.example.ui.theme.DiajakDesignSystem.Typography.Body,
                    color = MaterialTheme.colorScheme.onSurfaceVariant,
                    lineHeight = 22.sp
                )
                
                Spacer(modifier = Modifier.height(MaterialTheme.spacing.small))
                Text(
                    text = "3. Perlindungan & Keamanan Data",
                    style = com.example.ui.theme.DiajakDesignSystem.Typography.BodyBold,
                    color = MaterialTheme.colorScheme.onSurface
                )
                Text(
                    text = "Kami menerapkan langkah-langkah teknis dan organisasional yang ketat untuk mengamankan data Anda dari akses, pengungkapan, perubahan, atau penghancuran tanpa izin.",
                    style = com.example.ui.theme.DiajakDesignSystem.Typography.Body,
                    color = MaterialTheme.colorScheme.onSurfaceVariant,
                    lineHeight = 22.sp
                )
                
                Spacer(modifier = Modifier.height(MaterialTheme.spacing.small))
                Text(
                    text = "4. Berbagi Informasi",
                    style = com.example.ui.theme.DiajakDesignSystem.Typography.BodyBold,
                    color = MaterialTheme.colorScheme.onSurface
                )
                Text(
                    text = "Kami tidak akan pernah menjual atau menyewakan data pribadi Anda kepada pihak ketiga. Informasi Anda hanya dibagikan dengan penyelenggara aktivitas (kreator) untuk keperluan koordinasi aktivitas yang Anda ikuti.",
                    style = com.example.ui.theme.DiajakDesignSystem.Typography.Body,
                    color = MaterialTheme.colorScheme.onSurfaceVariant,
                    lineHeight = 22.sp
                )
                
                Spacer(modifier = Modifier.height(MaterialTheme.spacing.small))
                Text(
                    text = "Dengan menggunakan layanan kami, Anda menyetujui pengumpulan dan penggunaan informasi sebagaimana dijelaskan dalam Kebijakan Privasi ini. Kami dapat memperbarui kebijakan ini secara berkala, dan perubahan akan diumumkan langsung melalui aplikasi.",
                    style = com.example.ui.theme.DiajakDesignSystem.Typography.Body,
                    color = MaterialTheme.colorScheme.onSurface,
                    lineHeight = 22.sp
                )
                
                Spacer(modifier = Modifier.windowInsetsBottomHeight(WindowInsets.navigationBars))
            }
        }

        // 2. Glass Header Layer
        com.example.ui.components.DiajakGlassHeader(
            hazeState = hazeState,
            scrollState = scrollState,
            modifier = Modifier.align(Alignment.TopCenter).zIndex(10f)
        ) {
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .statusBarsPadding()
                    .height(56.dp)
                    .padding(horizontal = 20.dp)
            ) {
                // Circular Back Button
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
                
                // Centered Title
                Text(
                    text = "Kebijakan Privasi",
                    style = com.example.ui.theme.DiajakDesignSystem.Typography.Headline,
                    color = MaterialTheme.colorScheme.onSurface,
                    textAlign = androidx.compose.ui.text.style.TextAlign.Center,
                    modifier = Modifier.align(Alignment.Center).fillMaxWidth()
                )
            }
        }
    }
}
