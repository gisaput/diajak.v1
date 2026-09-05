package com.example.ui.screens
import com.example.ui.theme.spacing
import androidx.compose.material3.MaterialTheme

import android.widget.Toast
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.*
import androidx.compose.material.icons.outlined.*
import androidx.compose.material.icons.outlined.*
import androidx.compose.material.icons.outlined.*
import androidx.compose.material.icons.outlined.*
import androidx.compose.material.icons.outlined.*
import androidx.compose.material.icons.automirrored.outlined.*
import androidx.compose.material.icons.automirrored.outlined.*
import androidx.compose.material.icons.outlined.*
import androidx.compose.material.icons.outlined.*
import androidx.compose.material.icons.outlined.*
import androidx.compose.material.icons.outlined.*
import androidx.compose.material.icons.outlined.*
import androidx.compose.material.icons.automirrored.outlined.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.zIndex
import dev.chrisbanes.haze.HazeState
import dev.chrisbanes.haze.hazeSource
import dev.chrisbanes.haze.hazeEffect
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import com.example.ui.components.DiajakGlassHeader

import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.foundation.Image
import androidx.compose.ui.layout.ContentScale
import coil.compose.rememberAsyncImagePainter
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.ui.theme.DiajakOrange
import com.example.ui.viewmodel.DiajakViewModel

@Composable
fun ProfileScreen(
  viewModel: DiajakViewModel,
  isLoggedIn: Boolean,
  isKreatorMode: Boolean,
  profileName: String,
  profileEmail: String,
  profileUsername: String,
  profileImageUri: String?,
  profileImageRes: Int,
  onToggleKreatorMode: () -> Unit,
  onOpenCreateActivity: () -> Unit,
  onLoginSuccess: () -> Unit,
  onLogoutClick: () -> Unit,
  onEditProfileClick: () -> Unit,
  onAboutDiajakClick: () -> Unit,
  onPrivacyPolicyClick: () -> Unit,
  onTermsAndConditionsClick: () -> Unit,
  onHelpCenterClick: () -> Unit
) {
  val scrollState = rememberScrollState()
  val context = LocalContext.current

  if (!isLoggedIn) {
    // --- GUEST MODE (CENTERED LAYOUT WITH BALANCED MARGINS) ---
    Box(
      modifier = Modifier
        .fillMaxSize().background(androidx.compose.ui.graphics.Color(0xFFE5E7EB))
        .padding(horizontal = 20.dp),
      contentAlignment = Alignment.Center
    ) {
      Column(
        modifier = Modifier
          .fillMaxWidth()
          .verticalScroll(rememberScrollState()),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
      ) {
        AuthContent(
          viewModel = viewModel,
          onSuccess = onLoginSuccess,
          isInline = true
        )
      }
    }
  } else {
    // --- LOGGED-IN MODE ---
    val hazeState = remember { HazeState() }
    val density = LocalDensity.current
    val isScrolled by remember {
      derivedStateOf { scrollState.value > 0 }
    }
    val showTitle by remember { 
      derivedStateOf { scrollState.value > with(density) { 140.dp.toPx() } } 
    }

    Box(
      modifier = Modifier
        .fillMaxSize()
        .background(Color(0xFFE5E7EB))
    ) {
      Column(
        modifier = Modifier
          .fillMaxSize()
          .hazeSource(state = hazeState)
          .verticalScroll(scrollState)
          .padding(bottom = 124.dp)
      ) {
        // Natural Root Tab Top Padding (Status bar + 20dp)
        Spacer(modifier = Modifier.statusBarsPadding().height(20.dp))
        
        Column(
          modifier = Modifier
            .fillMaxWidth()
            .padding(start = 20.dp, end = 20.dp, bottom = MaterialTheme.spacing.medium),
        horizontalAlignment = Alignment.CenterHorizontally
      ) {
        Box(
          modifier = Modifier
            .size(90.dp)
            .clip(CircleShape)
            .background(MaterialTheme.colorScheme.outlineVariant),
          contentAlignment = Alignment.Center
        ) {
          Image(
            painter = if (profileImageUri != null) {
              rememberAsyncImagePainter(model = profileImageUri)
            } else {
              rememberAsyncImagePainter(model = profileImageRes)
            },
            contentDescription = "Foto Profil",
            modifier = Modifier.fillMaxSize(),
            contentScale = ContentScale.Crop
          )
        }
        Spacer(modifier = Modifier.height(MaterialTheme.spacing.medium))
        Text(
          text = profileName,
          fontSize = 20.sp,
          fontWeight = FontWeight.Bold,
          style = com.example.ui.theme.DiajakDesignSystem.Typography.Display,
          color = MaterialTheme.colorScheme.onSurface
        )
      }

      // Menu list for logged in user
      Column(
        modifier = Modifier
          .fillMaxWidth()
          .padding(start = 20.dp, end = 20.dp, top = MaterialTheme.spacing.medium, bottom = MaterialTheme.spacing.medium)
      ) {
        Card(
          modifier = Modifier.fillMaxWidth(),
          shape = RoundedCornerShape(16.dp),
          colors = CardDefaults.cardColors(containerColor = Color.White),
          border = BorderStroke(0.dp, Color.Transparent),
          elevation = CardDefaults.cardElevation(defaultElevation = 0.dp)
        ) {
          Column(modifier = Modifier.fillMaxWidth()) {
            ProfileMenuItem(
              title = "Pengaturan Profil",
              icon = Icons.Outlined.Person,
              onClick = onEditProfileClick
            )
            HorizontalDivider(color = Color(0xFFE2E8F0), thickness = 1.dp, modifier = Modifier.padding(horizontal = MaterialTheme.spacing.screenMargin))
            ProfileMenuItem(
              title = "Tentang Aplikasi",
              icon = Icons.Outlined.Info,
              onClick = onAboutDiajakClick
            )
            HorizontalDivider(color = Color(0xFFE2E8F0), thickness = 1.dp, modifier = Modifier.padding(horizontal = MaterialTheme.spacing.screenMargin))
            ProfileMenuItem(
              title = "Kebijakan Privasi",
              icon = Icons.Outlined.Security,
              onClick = onPrivacyPolicyClick
            )
            HorizontalDivider(color = Color(0xFFE2E8F0), thickness = 1.dp, modifier = Modifier.padding(horizontal = MaterialTheme.spacing.screenMargin))
            ProfileMenuItem(
              title = "Syarat & Ketentuan",
              icon = Icons.AutoMirrored.Outlined.Assignment,
              onClick = onTermsAndConditionsClick
            )
            HorizontalDivider(color = Color(0xFFE2E8F0), thickness = 1.dp, modifier = Modifier.padding(horizontal = MaterialTheme.spacing.screenMargin))
            ProfileMenuItem(
              title = "Pusat Bantuan / FAQ",
              icon = Icons.AutoMirrored.Outlined.HelpOutline,
              onClick = onHelpCenterClick
            )
          }
        }
        
        Spacer(modifier = Modifier.height(MaterialTheme.spacing.medium))
        
        Card(
          modifier = Modifier.fillMaxWidth(),
          shape = RoundedCornerShape(16.dp),
          colors = CardDefaults.cardColors(containerColor = Color.White),
          border = BorderStroke(0.dp, Color.Transparent),
          elevation = CardDefaults.cardElevation(defaultElevation = 0.dp)
        ) {
          Column(modifier = Modifier.fillMaxWidth()) {
            ProfileMenuItem(
              title = "Reset Database",
              icon = Icons.Outlined.Refresh,
              onClick = { viewModel.resetDatabase(context) }
            )
            HorizontalDivider(color = Color(0xFFE2E8F0), thickness = 1.dp, modifier = Modifier.padding(horizontal = MaterialTheme.spacing.screenMargin))
            KreatorDashboardMenuItem(
              isKreatorMode = isKreatorMode,
              onToggle = onToggleKreatorMode
            )
          }
        }

        Spacer(modifier = Modifier.height(MaterialTheme.spacing.section))
        
        Button(
          onClick = onLogoutClick,
          modifier = Modifier.fillMaxWidth().height(52.dp),
          colors = ButtonDefaults.buttonColors(containerColor = Color(0xFFFEF2F2)),
          shape = RoundedCornerShape(16.dp)
        ) {
          Row(
            horizontalArrangement = Arrangement.Center,
            verticalAlignment = Alignment.CenterVertically
          ) {
            Icon(
              imageVector = Icons.AutoMirrored.Outlined.Logout,
              contentDescription = "Keluar",
              tint = Color(0xFFDC2626),
              modifier = Modifier.size(20.dp)
            )
            Spacer(modifier = Modifier.width(MaterialTheme.spacing.small))
            Text(
              text = "Keluar",
              color = Color(0xFFDC2626),
              fontWeight = FontWeight.Bold,
              fontSize = 16.sp
            )
          }
        }
      }
    } // End of verticalScroll Column
      
    // Floating Glass Header at the very top (Status Bar area) - Appears instantly on scroll, exactly like HomeScreen
    if (isScrolled) {
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
          androidx.compose.animation.AnimatedVisibility(
            visible = showTitle,
            enter = androidx.compose.animation.fadeIn(),
            exit = androidx.compose.animation.fadeOut()
          ) {
            Text(
              text = profileName,
              style = com.example.ui.theme.DiajakDesignSystem.Typography.Headline,
              textAlign = TextAlign.Center,
              maxLines = 1,
              overflow = TextOverflow.Ellipsis,
              color = MaterialTheme.colorScheme.onSurface,
              modifier = Modifier.fillMaxWidth().padding(horizontal = 48.dp)
            )
          }
        }
      }
    }
  }
}
}

@Composable
fun KreatorDashboardMenuItem(
  isKreatorMode: Boolean,
  onToggle: () -> Unit
) {
  Row(
    modifier = Modifier
      .fillMaxWidth()
      .clickable { onToggle() }
      .padding(horizontal = 20.dp, vertical = 14.dp),
    verticalAlignment = Alignment.CenterVertically
  ) {
    Icon(
      imageVector = Icons.Outlined.Campaign,
      contentDescription = "Menjadi Kreator",
      tint = DiajakOrange,
      modifier = Modifier.size(22.dp)
    )
    Spacer(modifier = Modifier.width(20.dp))
    Text(
      text = "Menjadi Kreator",
      style = com.example.ui.theme.DiajakDesignSystem.Typography.Body,
      color = MaterialTheme.colorScheme.onSurface
    )
  }
}

@Composable
fun ProfileMenuItem(
  title: String,
  icon: androidx.compose.ui.graphics.vector.ImageVector,
  isDestructive: Boolean = false,
  onClick: () -> Unit = {}
) {
  Row(
    modifier = Modifier
      .fillMaxWidth()
      .clickable { onClick() }
      .padding(horizontal = 20.dp, vertical = 14.dp),
    verticalAlignment = Alignment.CenterVertically
  ) {
    Icon(
      imageVector = icon,
      contentDescription = title,
      tint = if (isDestructive) Color.Red else MaterialTheme.colorScheme.onSurface,
      modifier = Modifier.size(22.dp)
    )
    Spacer(modifier = Modifier.width(20.dp))
    Text(
      text = title,
      style = com.example.ui.theme.DiajakDesignSystem.Typography.Body,
      color = if (isDestructive) Color.Red else MaterialTheme.colorScheme.onSurface
    )
  }
}
