package com.example.ui.screens
import com.example.ui.theme.spacing
import androidx.compose.material3.MaterialTheme

import androidx.compose.material.icons.automirrored.outlined.*
import androidx.compose.material.icons.outlined.*

import android.widget.Toast
import androidx.compose.animation.*
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.focus.focusRequester
import androidx.compose.ui.focus.onFocusChanged
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Dialog
import com.example.ui.theme.DiajakOrange
import com.example.ui.viewmodel.DiajakViewModel
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

@Composable
fun PersistentOutlinedTextField(
  value: String,
  onValueChange: (String) -> Unit,
  labelText: String,
  placeholderText: String,
  leadingIcon: @Composable (() -> Unit)? = null,
  trailingIcon: @Composable (() -> Unit)? = null,
  singleLine: Boolean = true,
  visualTransformation: VisualTransformation = VisualTransformation.None,
  keyboardOptions: KeyboardOptions = KeyboardOptions.Default,
  containerColor: Color = Color.White,
  modifier: Modifier = Modifier
) {
  var isFocused by remember { mutableStateOf(false) }
  val focusRequester = remember { FocusRequester() }

  Box(
    modifier = modifier
      .fillMaxWidth()
      .padding(top = 10.dp)
  ) {
    OutlinedTextField(
      value = value,
      onValueChange = onValueChange,
      placeholder = {
        Text(
          text = placeholderText,
          color = MaterialTheme.colorScheme.onSurfaceVariant.copy(alpha = 0.5f),
          style = androidx.compose.ui.text.TextStyle(
            fontSize = 14.sp,
            fontFamily = com.example.ui.theme.AppFontFamily
          )
        )
      },
      leadingIcon = leadingIcon,
      trailingIcon = trailingIcon,
      singleLine = singleLine,
      visualTransformation = visualTransformation,
      keyboardOptions = keyboardOptions,
      shape = RoundedCornerShape(16.dp),
      modifier = Modifier
        .fillMaxWidth()
        .focusRequester(focusRequester)
        .onFocusChanged { isFocused = it.isFocused },
      colors = OutlinedTextFieldDefaults.colors(
        focusedTextColor = MaterialTheme.colorScheme.onSurface,
        unfocusedTextColor = MaterialTheme.colorScheme.onSurface,
        focusedBorderColor = DiajakOrange,
        unfocusedBorderColor = Color.Transparent,
        cursorColor = DiajakOrange,
        focusedPlaceholderColor = MaterialTheme.colorScheme.onSurfaceVariant.copy(alpha = 0.5f),
        unfocusedPlaceholderColor = MaterialTheme.colorScheme.onSurfaceVariant.copy(alpha = 0.5f)
      )
    )

    // Persistent floating label cut into top border - sits cleanly on the outline without being cut off by Box top bounds
    Surface(
      color = containerColor,
      modifier = Modifier
        .padding(start = 14.dp)
        .offset(y = (-9).dp)
        .clickable(
          interactionSource = remember { MutableInteractionSource() },
          indication = null
        ) {
          focusRequester.requestFocus()
        }
    ) {
      Text(
        text = labelText,
        style = androidx.compose.ui.text.TextStyle(
          fontSize = 12.sp,
          fontWeight = FontWeight.Medium,
          fontFamily = com.example.ui.theme.AppFontFamily
        ),
        color = if (isFocused) DiajakOrange else MaterialTheme.colorScheme.onSurfaceVariant,
        modifier = Modifier.padding(horizontal = 4.dp)
      )
    }
  }
}

@Composable
fun GoogleLogo(modifier: Modifier = Modifier) {
  Box(contentAlignment = Alignment.Center, modifier = modifier.size(20.dp)) {
    Canvas(modifier = Modifier.fillMaxSize()) {
      val strokePx = 3.dp.toPx()
      // Segmented colored ring for Google branding
      drawArc(color = Color(0xFFEA4335), startAngle = 135f, sweepAngle = 90f, useCenter = false, style = Stroke(width = strokePx))
      drawArc(color = Color(0xFF4285F4), startAngle = 225f, sweepAngle = 90f, useCenter = false, style = Stroke(width = strokePx))
      drawArc(color = Color(0xFF34A853), startAngle = 315f, sweepAngle = 90f, useCenter = false, style = Stroke(width = strokePx))
      drawArc(color = Color(0xFFFBBC05), startAngle = 45f, sweepAngle = 90f, useCenter = false, style = Stroke(width = strokePx))
    }
    Text("G", color = Color(0xFF4285F4), style = com.example.ui.theme.DiajakDesignSystem.Typography.TitleBold)
  }
}

@Composable
fun AuthContent(
  viewModel: DiajakViewModel,
  onSuccess: () -> Unit,
  isInline: Boolean = false
) {
  val context = LocalContext.current
  val coroutineScope = rememberCoroutineScope()

  var isRegisterMode by remember { mutableStateOf(false) }

  // Fields for Email login
  var name by remember { mutableStateOf("") }
  var email by remember { mutableStateOf("gisaput@diajak.com") }
  var password by remember { mutableStateOf("password123") }
  var isPasswordVisible by remember { mutableStateOf(false) }

  // State overlays for interactive mocks
  var showGooglePicker by remember { mutableStateOf(false) }
  var showApplePrompt by remember { mutableStateOf(false) }

  // Main UI Column - perfectly vertically centered and spaced
  Column(
    modifier = Modifier.fillMaxWidth(),
    horizontalAlignment = Alignment.CenterHorizontally,
    verticalArrangement = Arrangement.Center
  ) {
    // Name input (Register Mode only)
    if (isRegisterMode) {
      PersistentOutlinedTextField(
        value = name,
        onValueChange = { name = it },
        labelText = "Nama Lengkap",
        placeholderText = "Masukkan nama lengkap",
        leadingIcon = { Icon(Icons.Outlined.Person, contentDescription = null, tint = DiajakOrange) },
        singleLine = true
      )
      Spacer(modifier = Modifier.height(6.dp))
    }

    // Email field
    PersistentOutlinedTextField(
      value = email,
      onValueChange = { email = it },
      labelText = "Email",
      placeholderText = "nama@email.com",
      leadingIcon = { Icon(Icons.Outlined.Email, contentDescription = null, tint = DiajakOrange) },
      singleLine = true,
      keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Email)
    )

    Spacer(modifier = Modifier.height(6.dp))

    // Password field
    PersistentOutlinedTextField(
      value = password,
      onValueChange = { password = it },
      labelText = "Kata Sandi",
      placeholderText = "Masukkan kata sandi",
      leadingIcon = { Icon(Icons.Outlined.Lock, contentDescription = null, tint = DiajakOrange) },
      trailingIcon = {
        IconButton(onClick = { isPasswordVisible = !isPasswordVisible }) {
          Icon(
            imageVector = if (isPasswordVisible) Icons.Outlined.Visibility else Icons.Outlined.VisibilityOff,
            contentDescription = if (isPasswordVisible) "Sembunyikan Kata Sandi" else "Tampilkan Kata Sandi",
            tint = MaterialTheme.colorScheme.onSurfaceVariant
          )
        }
      },
      singleLine = true,
      visualTransformation = if (isPasswordVisible) VisualTransformation.None else PasswordVisualTransformation(),
      keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Password)
    )

    Spacer(modifier = Modifier.height(20.dp))

    // Submit Email button
    Button(
      onClick = {
        if (email.isBlank() || password.isBlank()) {
          Toast.makeText(context, "Email dan kata sandi harus diisi!", Toast.LENGTH_SHORT).show()
        } else if (isRegisterMode && name.isBlank()) {
          Toast.makeText(context, "Nama lengkap harus diisi!", Toast.LENGTH_SHORT).show()
        } else {
          val loggedName = if (isRegisterMode) name else "Gisa Putra"
          val successMsg = if (isRegisterMode) "Pendaftaran berhasil! Selamat datang, $loggedName." else "Berhasil masuk! Selamat datang kembali."
          Toast.makeText(context, successMsg, Toast.LENGTH_LONG).show()
          
          viewModel.loginWithCustomProfile(
            name = loggedName,
            email = email,
            phone = "***********43",
            imageRes = com.example.R.drawable.img_profile_cat_1783601304885
          )
          onSuccess()
        }
      },
      modifier = Modifier
        .fillMaxWidth()
        .height(52.dp),
      shape = RoundedCornerShape(16.dp),
      colors = ButtonDefaults.buttonColors(containerColor = DiajakOrange)
    ) {
      Text(
        text = if (isRegisterMode) "Daftar" else "Masuk",
        style = androidx.compose.ui.text.TextStyle(fontSize = 16.sp, fontWeight = androidx.compose.ui.text.font.FontWeight.Bold, fontFamily = com.example.ui.theme.AppFontFamily),
        color = Color.White
      )
    }

    Spacer(modifier = Modifier.height(20.dp))

    // Switch mode CTA
    Row(
      verticalAlignment = Alignment.CenterVertically,
      horizontalArrangement = Arrangement.Center,
      modifier = Modifier.fillMaxWidth()
    ) {
      Text(
        text = if (isRegisterMode) "Sudah punya akun? " else "Belum punya akun? ",
        style = com.example.ui.theme.DiajakDesignSystem.Typography.Body, color = MaterialTheme.colorScheme.onSurface
      )
      Text(
        text = if (isRegisterMode) "Masuk" else "Daftar sekarang",
        style = com.example.ui.theme.DiajakDesignSystem.Typography.Body,
        color = DiajakOrange,
        modifier = Modifier.clickable {
          isRegisterMode = !isRegisterMode
          if (isRegisterMode && name.isEmpty()) {
            name = "Gisa Putra"
          }
        }
      )
    }

    Spacer(modifier = Modifier.height(20.dp))

    // Social Divider Text
    Box(
      modifier = Modifier.fillMaxWidth(),
      contentAlignment = Alignment.Center
    ) {
      Text(
        text = "atau login dengan",
        style = com.example.ui.theme.DiajakDesignSystem.Typography.Body,
        color = MaterialTheme.colorScheme.onSurfaceVariant
      )
    }

    Spacer(modifier = Modifier.height(20.dp))

    // Social Login Buttons
    // Google Button
    Row(
      verticalAlignment = Alignment.CenterVertically,
      horizontalArrangement = Arrangement.Center,
      modifier = Modifier
        .fillMaxWidth()
        .height(52.dp)
        .clip(RoundedCornerShape(16.dp))
        .background(Color.White)
        .border(1.dp, Color.Transparent, RoundedCornerShape(16.dp))
        .clickable { showGooglePicker = true }
    ) {
      GoogleLogo()
      Spacer(modifier = Modifier.width(MaterialTheme.spacing.small))
      Text(
        text = "Lanjutkan dengan Google",
        color = MaterialTheme.colorScheme.onSurface,
        style = androidx.compose.ui.text.TextStyle(fontSize = 16.sp, fontWeight = androidx.compose.ui.text.font.FontWeight.Bold, fontFamily = com.example.ui.theme.AppFontFamily)
      )
    }

    Spacer(modifier = Modifier.height(MaterialTheme.spacing.small))

    // Apple Button
    Row(
      verticalAlignment = Alignment.CenterVertically,
      horizontalArrangement = Arrangement.Center,
      modifier = Modifier
        .fillMaxWidth()
        .height(52.dp)
        .clip(RoundedCornerShape(16.dp))
        .background(androidx.compose.material3.MaterialTheme.colorScheme.onSurface)
        .clickable { showApplePrompt = true }
    ) {
      Text(
        text = "",
        color = Color.White,
        style = com.example.ui.theme.DiajakDesignSystem.Typography.Body,
        modifier = Modifier.padding(bottom = 3.dp)
      )
      Spacer(modifier = Modifier.width(MaterialTheme.spacing.small))
      Text(
        text = "Lanjutkan dengan Apple",
        color = Color.White,
        style = androidx.compose.ui.text.TextStyle(fontSize = 16.sp, fontWeight = androidx.compose.ui.text.font.FontWeight.Bold, fontFamily = com.example.ui.theme.AppFontFamily)
      )
    }
  }

  // --- GOOGLE ACCOUNTS PICKER SIMULATION ---
  if (showGooglePicker) {
    Dialog(onDismissRequest = { showGooglePicker = false }) {
      Surface(
        shape = RoundedCornerShape(16.dp),
        color = Color.White,
        modifier = Modifier
          .fillMaxWidth()
          .padding(20.dp)
      ) {
        Column(
          modifier = Modifier
            .fillMaxWidth()
            .padding(MaterialTheme.spacing.large)
        ) {
          // Google Header
          Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
          ) {
            Row(verticalAlignment = Alignment.CenterVertically) {
              GoogleLogo(modifier = Modifier.size(24.dp))
              Spacer(modifier = Modifier.width(MaterialTheme.spacing.small))
              Text(
                text = "Google",
                style = com.example.ui.theme.DiajakDesignSystem.Typography.Body,
                color = MaterialTheme.colorScheme.onSurface
              )
            }
            IconButton(onClick = { showGooglePicker = false }, modifier = Modifier.size(24.dp)) {
              Icon(Icons.Outlined.Close, contentDescription = "Close", tint = MaterialTheme.colorScheme.onSurface)
            }
          }

          Spacer(modifier = Modifier.height(20.dp))

          Text(
            text = "Pilih akun untuk melanjutkan",
            style = com.example.ui.theme.DiajakDesignSystem.Typography.Body,
            color = MaterialTheme.colorScheme.onSurface
          )

          Spacer(modifier = Modifier.height(20.dp))

          // Account Item 1: Gisa Putra (User's actual email in metadata)
          GoogleAccountRow(
            name = "Gisa Putra",
            email = "gisaput@gmail.com",
            avatarRes = com.example.R.drawable.img_profile_cat_1783601304885,
            onClick = {
              showGooglePicker = false
              Toast.makeText(context, "Selamat datang, Gisa Putra!", Toast.LENGTH_SHORT).show()
              viewModel.loginWithCustomProfile(
                name = "Gisa Putra",
                email = "gisaput@gmail.com",
                phone = "+62 812-4839-2041",
                imageRes = com.example.R.drawable.img_profile_cat_1783601304885
              )
              onSuccess()
            }
          )

          Spacer(modifier = Modifier.height(MaterialTheme.spacing.medium))

          // Account Item 2: Anggi Saputro (Alternate profile)
          GoogleAccountRow(
            name = "Anggi Saputro",
            email = "anggi.saputro@gmail.com",
            avatarRes = com.example.R.drawable.img_profile_panda_1783603225548,
            onClick = {
              showGooglePicker = false
              Toast.makeText(context, "Selamat datang, Anggi Saputro!", Toast.LENGTH_SHORT).show()
              viewModel.loginWithCustomProfile(
                name = "Anggi Saputro",
                email = "anggi.saputro@gmail.com",
                phone = "+62 856-7890-1234",
                imageRes = com.example.R.drawable.img_profile_panda_1783603225548
              )
              onSuccess()
            }
          )

          Spacer(modifier = Modifier.height(MaterialTheme.spacing.medium))

          // Option: Gunakan akun lain
          Row(
            modifier = Modifier
              .fillMaxWidth()
              .clickable {
                showGooglePicker = false
                Toast.makeText(context, "Silakan masukkan kredensial Google Anda di browser.", Toast.LENGTH_SHORT).show()
              }
              .padding(vertical = MaterialTheme.spacing.small),
            verticalAlignment = Alignment.CenterVertically
          ) {
            Box(
              modifier = Modifier
                .size(36.dp)
                .clip(CircleShape)
                .background(MaterialTheme.colorScheme.surfaceVariant),
              contentAlignment = Alignment.Center
            ) {
              Icon(Icons.Outlined.Add, contentDescription = null, tint = MaterialTheme.colorScheme.onSurface, modifier = Modifier.size(20.dp))
            }
            Spacer(modifier = Modifier.width(20.dp))
            Text(
              text = "Gunakan akun lain",
              style = com.example.ui.theme.DiajakDesignSystem.Typography.Body,
              color = MaterialTheme.colorScheme.onSurface
            )
          }

          Spacer(modifier = Modifier.height(20.dp))

          Text(
            text = "Untuk melanjutkan, Google akan membagikan nama, alamat email, preferensi bahasa, dan gambar profil Anda dengan aplikasi ini. Lihat Kebijakan Privasi dan Ketentuan Layanan.",
            style = com.example.ui.theme.DiajakDesignSystem.Typography.Body, color = MaterialTheme.colorScheme.onSurface,
            lineHeight = 22.sp
          )
        }
      }
    }
  }

  // --- APPLE SIGN-IN FACEID PROMPT SIMULATION ---
  if (showApplePrompt) {
    Dialog(onDismissRequest = { showApplePrompt = false }) {
      Surface(
        shape = RoundedCornerShape(16.dp),
        color = MaterialTheme.colorScheme.onSurface, // Elegant Dark iOS aesthetic
        modifier = Modifier
          .fillMaxWidth()
          .padding(20.dp)
      ) {
        Column(
          modifier = Modifier
            .fillMaxWidth()
            .padding(MaterialTheme.spacing.large),
          horizontalAlignment = Alignment.CenterHorizontally
        ) {
          // Apple ID Title
          Text(
            text = " Sign In with Apple",
            style = com.example.ui.theme.DiajakDesignSystem.Typography.Body,
            color = Color.White
          )

          Spacer(modifier = Modifier.height(MaterialTheme.spacing.small))

          Text(
            text = "Gunakan Face ID untuk masuk menggunakan Apple ID \"gisaput@icloud.com\".",
            style = com.example.ui.theme.DiajakDesignSystem.Typography.Body, color = MaterialTheme.colorScheme.onSurface,
            textAlign = TextAlign.Center,
            modifier = Modifier.padding(horizontal = MaterialTheme.spacing.small),
            lineHeight = 22.sp
          )

          Spacer(modifier = Modifier.height(20.dp))

          // Pulsing Face ID Scan Simulation
          var pulseScale by remember { mutableStateOf(1f) }
          LaunchedEffect(Unit) {
            while (true) {
              pulseScale = 1.15f
              delay(800L)
              pulseScale = 1f
              delay(800L)
            }
          }

          Box(
            modifier = Modifier
              .size(80.dp)
              .clip(CircleShape)
              .background(Color(0xFF262626)),
            contentAlignment = Alignment.Center
          ) {
            Icon(
              imageVector = Icons.Outlined.FilterCenterFocus, // FaceID scanner vibe icon
              contentDescription = "FaceID",
              tint = Color(0xFF38BDF8),
              modifier = Modifier
                .size(44.dp)
                .animateContentSize()
            )
          }

          Spacer(modifier = Modifier.height(20.dp))

          Text(
            text = "Memindai Face ID...",
            style = com.example.ui.theme.DiajakDesignSystem.Typography.Body,
            color = Color(0xFF38BDF8)
          )

          Spacer(modifier = Modifier.height(20.dp))

          Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.spacedBy(20.dp)
          ) {
            Button(
              onClick = { showApplePrompt = false },
              modifier = Modifier
                .weight(1f)
                .height(48.dp),
              colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF262626)),
              shape = RoundedCornerShape(16.dp)
            ) {
              Text("Batal", color = Color.White, fontWeight = FontWeight.Medium)
            }
            Button(
              onClick = {
                showApplePrompt = false
                Toast.makeText(context, "Selamat datang, Gisa Putra!", Toast.LENGTH_SHORT).show()
                viewModel.loginWithCustomProfile(
                  name = "Gisa Putra",
                  email = "gisaput@icloud.com",
                  phone = "***********",
                  imageRes = com.example.R.drawable.img_profile_dog_1783603245364
                )
                onSuccess()
              },
              modifier = Modifier
                .weight(1f)
                .height(48.dp),
              colors = ButtonDefaults.buttonColors(containerColor = Color.White),
              shape = RoundedCornerShape(16.dp)
            ) {
              Text("Lanjutkan", color = MaterialTheme.colorScheme.onSurface, style = com.example.ui.theme.DiajakDesignSystem.Typography.TitleBold)
            }
          }
        }
      }
    }
  }
}

@Composable
fun GoogleAccountRow(
  name: String,
  email: String,
  avatarRes: Int,
  onClick: () -> Unit
) {
  Row(
    modifier = Modifier
      .fillMaxWidth()
      .clickable { onClick() }
      .padding(vertical = MaterialTheme.spacing.small),
    verticalAlignment = Alignment.CenterVertically
  ) {
    Box(
      modifier = Modifier
        .size(36.dp)
        .clip(CircleShape)
        .border(1.5.dp, Color(0xFF4285F4), CircleShape) // Google-blue border circle!
    ) {
      Box(
        modifier = Modifier
          .fillMaxSize()
          .clip(CircleShape)
          .background(MaterialTheme.colorScheme.outlineVariant),
        contentAlignment = Alignment.Center
      ) {
        // Mock image showing initials or avatar placeholder
        Text(name.take(1), fontWeight = FontWeight.Medium, color = MaterialTheme.colorScheme.onSurface, style = com.example.ui.theme.DiajakDesignSystem.Typography.Body)
      }
    }
    Spacer(modifier = Modifier.width(20.dp))
    Column(modifier = Modifier.weight(1f)) {
      Text(
        text = name,
        style = com.example.ui.theme.DiajakDesignSystem.Typography.Body,
        color = MaterialTheme.colorScheme.onSurface
      )
      Text(
        text = email,
        style = com.example.ui.theme.DiajakDesignSystem.Typography.Body, color = MaterialTheme.colorScheme.onSurface
      )
    }
    Icon(
      Icons.AutoMirrored.Outlined.ArrowForwardIos,
      contentDescription = null,
      tint = MaterialTheme.colorScheme.onSurface,
      modifier = Modifier.size(12.dp)
    )
  }
}
