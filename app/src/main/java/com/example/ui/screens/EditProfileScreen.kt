package com.example.ui.screens
import com.example.ui.theme.spacing
import com.example.ui.theme.diajakGlassButton
import androidx.compose.material3.MaterialTheme

import androidx.compose.material.icons.outlined.*

import android.widget.Toast
import androidx.compose.ui.zIndex
import dev.chrisbanes.haze.HazeState
import dev.chrisbanes.haze.hazeSource
import dev.chrisbanes.haze.hazeEffect
import com.example.ui.components.DiajakGlassHeader

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.outlined.*
import androidx.compose.material.icons.outlined.*
import androidx.compose.material.icons.outlined.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.foundation.gestures.detectTapGestures
import androidx.compose.ui.input.pointer.pointerInput
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.border
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.rememberLazyListState
import coil.compose.rememberAsyncImagePainter
import com.example.R
import com.example.ui.theme.DiajakOrange
import com.example.ui.theme.DiajakDesignSystem
import kotlinx.coroutines.launch
import kotlinx.coroutines.flow.distinctUntilChanged
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun EditProfileScreen(
  profileName: String,
  profileUsername: String,
  profileBio: String,
  profileGender: String,
  profileBirthDate: String,
  profilePhone: String,
  profileEmail: String,
  profileImageUri: String?,
  profileImageRes: Int,
  onUpdateName: (String) -> Unit,
  onUpdateUsername: (String) -> Unit,
  onUpdateBio: (String) -> Unit,
  onUpdateGender: (String) -> Unit,
  onUpdateBirthDate: (String) -> Unit,
  onUpdatePhone: (String) -> Unit,
  onUpdateEmail: (String) -> Unit,
  onUpdateImageUri: (String?) -> Unit,
  onUpdateImageRes: (Int) -> Unit,
  onBack: () -> Unit
) {
  val context = LocalContext.current
  val scrollState = rememberScrollState()
  val focusManager = androidx.compose.ui.platform.LocalFocusManager.current

  // State for Edit Dialogs
  var showNameDialog by remember { mutableStateOf(false) }
  var showUsernameDialog by remember { mutableStateOf(false) }
  var showBioDialog by remember { mutableStateOf(false) }
  var showGenderDialog by remember { mutableStateOf(false) }
  var showBirthDateDialog by remember { mutableStateOf(false) }
  var showPhoneDialog by remember { mutableStateOf(false) }
  var showEmailDialog by remember { mutableStateOf(false) }
  var showAvatarDialog by remember { mutableStateOf(false) }

  // Temp State values for Dialog Inputs
  var tempName by remember { mutableStateOf(profileName) }
  var tempUsername by remember { mutableStateOf(profileUsername) }
  var tempBio by remember { mutableStateOf(profileBio) }
  var tempGender by remember { mutableStateOf(profileGender) }
  var tempPhone by remember { mutableStateOf(profilePhone) }
  var tempEmail by remember { mutableStateOf(profileEmail) }

  // Sync temp values when screen values change
  LaunchedEffect(profileName) { tempName = profileName }
  LaunchedEffect(profileUsername) { tempUsername = profileUsername }
  LaunchedEffect(profileBio) { tempBio = profileBio }
  LaunchedEffect(profileGender) { tempGender = profileGender }
  LaunchedEffect(profilePhone) { tempPhone = profilePhone }
  LaunchedEffect(profileEmail) { tempEmail = profileEmail }

  // Launcher for choosing profile picture from gallery
  val imagePickerLauncher = rememberLauncherForActivityResult(
    contract = ActivityResultContracts.GetContent()
  ) { uri ->
    if (uri != null) {
      onUpdateImageUri(uri.toString())
      Toast.makeText(context, "Foto profil berhasil diperbarui!", Toast.LENGTH_SHORT).show()
    }
  }

  val hazeState = remember { HazeState() }

  Box(
    modifier = Modifier
      .fillMaxSize()
      .background(androidx.compose.ui.graphics.Color(0xFFE5E7EB))
  ) {
    // 1. Content Layer
    Column(
      modifier = Modifier
        .fillMaxSize()
        .hazeSource(state = hazeState)
        .pointerInput(Unit) {
          detectTapGestures(
            onTap = {
              focusManager.clearFocus()
            }
          )
        }
        .verticalScroll(scrollState)
        .padding(
          start = DiajakDesignSystem.Dimens.ScreenPaddingHorizontal,
          end = DiajakDesignSystem.Dimens.ScreenPaddingHorizontal,
          bottom = DiajakDesignSystem.Dimens.ScreenPaddingVertical
        ),
      horizontalAlignment = Alignment.CenterHorizontally,
      verticalArrangement = Arrangement.spacedBy(20.dp)
    ) {
      Spacer(modifier = Modifier.statusBarsPadding().height(80.dp))
      
      // Card 1: Avatar Profile Picture
      Box(
        modifier = Modifier.fillMaxWidth(),
      ) {
        Column(
          modifier = Modifier
            .fillMaxWidth()
            .padding(top = 20.dp, bottom = MaterialTheme.spacing.small),
          horizontalAlignment = Alignment.CenterHorizontally
        ) {
          // Circular Image Frame
          Box(
            modifier = Modifier
              .size(100.dp)
              .clip(CircleShape)
              .background(MaterialTheme.colorScheme.outlineVariant)
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
          
          // "Ubah" label under avatar with pen icon
          Row(
            modifier = Modifier
              .clip(RoundedCornerShape(8.dp))
              .clickable { showAvatarDialog = true }
              .padding(horizontal = 20.dp, vertical = 6.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.Center
          ) {
            Icon(
              imageVector = Icons.Outlined.Edit,
              contentDescription = null,
              tint = MaterialTheme.colorScheme.onSurface,
              modifier = Modifier.size(20.dp)
            )
            Spacer(modifier = Modifier.width(MaterialTheme.spacing.small))
            Text(
              text = "Ubah",
              style = com.example.ui.theme.DiajakDesignSystem.Typography.Body,
              color = MaterialTheme.colorScheme.onSurface
            )
          }
        }
      }

      Spacer(modifier = Modifier.height(20.dp))

      // Profile Data Fields Container
      Column(
        modifier = Modifier
          .fillMaxWidth()
          .padding(top = MaterialTheme.spacing.small)
      ) {
        EditProfileRow(
          label = "Nama",
          value = profileName,
          hasArrow = true,
          onClick = { showNameDialog = true }
        )

        EditProfileRow(
          label = "Jenis Kelamin",
          value = profileGender,
          hasArrow = true,
          onClick = { showGenderDialog = true }
        )

        EditProfileRow(
          label = "Tanggal Lahir",
          value = profileBirthDate,
          hasArrow = true,
          onClick = { showBirthDateDialog = true }
        )

        EditProfileRow(
          label = "No. Handphone",
          value = profilePhone,
          hasArrow = true,
          onClick = { showPhoneDialog = true }
        )

        EditProfileRow(
          label = "Email",
          value = profileEmail,
          hasArrow = true,
          onClick = { showEmailDialog = true }
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
          text = "Ubah Profil",
          style = com.example.ui.theme.DiajakDesignSystem.Typography.Headline,
          color = MaterialTheme.colorScheme.onSurface,
          textAlign = androidx.compose.ui.text.style.TextAlign.Center,
          modifier = Modifier.align(Alignment.Center).fillMaxWidth()
        )
      }
    }
  }

  // BOTTOM SHEETS
  @OptIn(ExperimentalMaterial3Api::class)
  fun dummy() {} // to allow OptIn if needed

  // 1. Edit Name Bottom Sheet
  if (showNameDialog) {
    ModalBottomSheet(
      onDismissRequest = { showNameDialog = false },
      sheetState = rememberModalBottomSheetState(skipPartiallyExpanded = true),
      containerColor = androidx.compose.ui.graphics.Color(0xFFE5E7EB)
    ) {
      Column(
        modifier = Modifier
          .fillMaxWidth()
          .navigationBarsPadding()
          .padding(horizontal = 20.dp, vertical = 20.dp)
      ) {
        Text("Ubah Nama", style = androidx.compose.ui.text.TextStyle(fontSize = 16.sp, fontWeight = androidx.compose.ui.text.font.FontWeight.Bold, fontFamily = com.example.ui.theme.AppFontFamily), color = MaterialTheme.colorScheme.onSurface)
        Spacer(modifier = Modifier.height(20.dp))
        OutlinedTextField(
          value = tempName,
          onValueChange = { tempName = it },
          label = { Text("Nama Lengkap") },
          singleLine = true,
          modifier = Modifier.fillMaxWidth(),
          colors = OutlinedTextFieldDefaults.colors(
            focusedTextColor = MaterialTheme.colorScheme.onSurface,
            unfocusedTextColor = MaterialTheme.colorScheme.onSurface,
            focusedLabelColor = DiajakOrange,
            unfocusedLabelColor = MaterialTheme.colorScheme.onSurface,
            focusedBorderColor = DiajakOrange,
            unfocusedBorderColor = Color.Transparent,
            cursorColor = DiajakOrange,
            focusedPlaceholderColor = MaterialTheme.colorScheme.onSurface,
            unfocusedPlaceholderColor = MaterialTheme.colorScheme.onSurface
          )
        )
        Spacer(modifier = Modifier.height(20.dp))
        Row(
          modifier = Modifier.fillMaxWidth(),
          horizontalArrangement = Arrangement.spacedBy(20.dp)
        ) {
          OutlinedButton(
            onClick = { showNameDialog = false },
            modifier = Modifier.weight(1f),
            border = BorderStroke(1.5.dp, DiajakOrange),
            colors = ButtonDefaults.outlinedButtonColors(containerColor = Color.Transparent)
          ) {
            Text("Batal", style = androidx.compose.ui.text.TextStyle(fontSize = 16.sp, fontWeight = androidx.compose.ui.text.font.FontWeight.Bold, fontFamily = com.example.ui.theme.AppFontFamily), color = DiajakOrange)
          }
          Button(
            onClick = {
              if (tempName.isNotBlank()) {
                onUpdateName(tempName)
                showNameDialog = false
                Toast.makeText(context, "Nama berhasil diperbarui!", Toast.LENGTH_SHORT).show()
              } else {
                Toast.makeText(context, "Nama tidak boleh kosong!", Toast.LENGTH_SHORT).show()
              }
            },
            modifier = Modifier.weight(1f),
            colors = ButtonDefaults.buttonColors(containerColor = DiajakOrange)
          ) {
            Text("Simpan")
          }
        }
      }
    }
  }

  // 1b. Edit Username Bottom Sheet
  if (showUsernameDialog) {
    ModalBottomSheet(
      onDismissRequest = { showUsernameDialog = false },
      sheetState = rememberModalBottomSheetState(skipPartiallyExpanded = true),
      containerColor = androidx.compose.ui.graphics.Color(0xFFE5E7EB)
    ) {
      Column(
        modifier = Modifier
          .fillMaxWidth()
          .navigationBarsPadding()
          .padding(horizontal = 20.dp, vertical = 20.dp)
      ) {
        Text("Ubah Username", style = androidx.compose.ui.text.TextStyle(fontSize = 16.sp, fontWeight = androidx.compose.ui.text.font.FontWeight.Bold, fontFamily = com.example.ui.theme.AppFontFamily), color = MaterialTheme.colorScheme.onSurface)
        Spacer(modifier = Modifier.height(20.dp))
        OutlinedTextField(
          value = tempUsername,
          onValueChange = { tempUsername = it },
          label = { Text("Username") },
          singleLine = true,
          modifier = Modifier.fillMaxWidth(),
          colors = OutlinedTextFieldDefaults.colors(
            focusedTextColor = MaterialTheme.colorScheme.onSurface,
            unfocusedTextColor = MaterialTheme.colorScheme.onSurface,
            focusedLabelColor = DiajakOrange,
            unfocusedLabelColor = MaterialTheme.colorScheme.onSurface,
            focusedBorderColor = DiajakOrange,
            unfocusedBorderColor = Color.Transparent,
            cursorColor = DiajakOrange,
            focusedPlaceholderColor = MaterialTheme.colorScheme.onSurface,
            unfocusedPlaceholderColor = MaterialTheme.colorScheme.onSurface
          )
        )
        Spacer(modifier = Modifier.height(20.dp))
        Row(
          modifier = Modifier.fillMaxWidth(),
          horizontalArrangement = Arrangement.spacedBy(20.dp)
        ) {
          OutlinedButton(
            onClick = { showUsernameDialog = false },
            modifier = Modifier.weight(1f),
            border = BorderStroke(1.5.dp, DiajakOrange),
            colors = ButtonDefaults.outlinedButtonColors(containerColor = Color.Transparent)
          ) {
            Text("Batal", style = androidx.compose.ui.text.TextStyle(fontSize = 16.sp, fontWeight = androidx.compose.ui.text.font.FontWeight.Bold, fontFamily = com.example.ui.theme.AppFontFamily), color = DiajakOrange)
          }
          Button(
            onClick = {
              if (tempUsername.isNotBlank()) {
                onUpdateUsername(tempUsername)
                showUsernameDialog = false
                Toast.makeText(context, "Username berhasil diperbarui!", Toast.LENGTH_SHORT).show()
              } else {
                Toast.makeText(context, "Username tidak boleh kosong!", Toast.LENGTH_SHORT).show()
              }
            },
            modifier = Modifier.weight(1f),
            colors = ButtonDefaults.buttonColors(containerColor = DiajakOrange)
          ) {
            Text("Simpan")
          }
        }
      }
    }
  }

  // 2. Edit Bio Bottom Sheet
  if (showBioDialog) {
    ModalBottomSheet(
      onDismissRequest = { showBioDialog = false },
      sheetState = rememberModalBottomSheetState(skipPartiallyExpanded = true),
      containerColor = androidx.compose.ui.graphics.Color(0xFFE5E7EB)
    ) {
      Column(
        modifier = Modifier
          .fillMaxWidth()
          .navigationBarsPadding()
          .padding(horizontal = 20.dp, vertical = 20.dp)
      ) {
        Text("Ubah Bio", style = androidx.compose.ui.text.TextStyle(fontSize = 16.sp, fontWeight = androidx.compose.ui.text.font.FontWeight.Bold, fontFamily = com.example.ui.theme.AppFontFamily), color = MaterialTheme.colorScheme.onSurface)
        Spacer(modifier = Modifier.height(20.dp))
        OutlinedTextField(
          value = tempBio,
          onValueChange = { tempBio = it },
          label = { Text("Deskripsi Singkat") },
          maxLines = 3,
          modifier = Modifier.fillMaxWidth(),
          colors = OutlinedTextFieldDefaults.colors(
            focusedTextColor = MaterialTheme.colorScheme.onSurface,
            unfocusedTextColor = MaterialTheme.colorScheme.onSurface,
            focusedLabelColor = DiajakOrange,
            unfocusedLabelColor = MaterialTheme.colorScheme.onSurface,
            focusedBorderColor = DiajakOrange,
            unfocusedBorderColor = Color.Transparent,
            cursorColor = DiajakOrange,
            focusedPlaceholderColor = MaterialTheme.colorScheme.onSurface,
            unfocusedPlaceholderColor = MaterialTheme.colorScheme.onSurface
          )
        )
        Spacer(modifier = Modifier.height(20.dp))
        Row(
          modifier = Modifier.fillMaxWidth(),
          horizontalArrangement = Arrangement.spacedBy(20.dp)
        ) {
          OutlinedButton(
            onClick = { showBioDialog = false },
            modifier = Modifier.weight(1f),
            border = BorderStroke(1.5.dp, DiajakOrange),
            colors = ButtonDefaults.outlinedButtonColors(containerColor = Color.Transparent)
          ) {
            Text("Batal", style = androidx.compose.ui.text.TextStyle(fontSize = 16.sp, fontWeight = androidx.compose.ui.text.font.FontWeight.Bold, fontFamily = com.example.ui.theme.AppFontFamily), color = DiajakOrange)
          }
          Button(
            onClick = {
              onUpdateBio(tempBio)
              showBioDialog = false
              Toast.makeText(context, "Bio berhasil diperbarui!", Toast.LENGTH_SHORT).show()
            },
            modifier = Modifier.weight(1f),
            colors = ButtonDefaults.buttonColors(containerColor = DiajakOrange)
          ) {
            Text("Simpan")
          }
        }
      }
    }
  }

  // 3. Edit Gender Bottom Sheet
  if (showGenderDialog) {
    ModalBottomSheet(
      onDismissRequest = { showGenderDialog = false },
      sheetState = rememberModalBottomSheetState(skipPartiallyExpanded = true),
      containerColor = androidx.compose.ui.graphics.Color(0xFFE5E7EB)
    ) {
      Column(
        modifier = Modifier
          .fillMaxWidth()
          .navigationBarsPadding()
          .padding(horizontal = 20.dp, vertical = 20.dp)
      ) {
        Text("Pilih Jenis Kelamin", style = androidx.compose.ui.text.TextStyle(fontSize = 16.sp, fontWeight = androidx.compose.ui.text.font.FontWeight.Bold, fontFamily = com.example.ui.theme.AppFontFamily), color = MaterialTheme.colorScheme.onSurface)
        Spacer(modifier = Modifier.height(20.dp))
        Column(verticalArrangement = Arrangement.spacedBy(20.dp)) {
          Row(
            modifier = Modifier
              .fillMaxWidth()
              .clickable { tempGender = "Pria" }
              .padding(vertical = MaterialTheme.spacing.small),
            verticalAlignment = Alignment.CenterVertically
          ) {
            RadioButton(
              selected = tempGender == "Pria",
              onClick = { tempGender = "Pria" },
              colors = RadioButtonDefaults.colors(selectedColor = DiajakOrange)
            )
            Spacer(modifier = Modifier.width(MaterialTheme.spacing.small))
            Text("Pria", style = com.example.ui.theme.DiajakDesignSystem.Typography.Body, color = MaterialTheme.colorScheme.onSurface)
          }
          Row(
            modifier = Modifier
              .fillMaxWidth()
              .clickable { tempGender = "Wanita" }
              .padding(vertical = MaterialTheme.spacing.small),
            verticalAlignment = Alignment.CenterVertically
          ) {
            RadioButton(
              selected = tempGender == "Wanita",
              onClick = { tempGender = "Wanita" },
              colors = RadioButtonDefaults.colors(selectedColor = DiajakOrange)
            )
            Spacer(modifier = Modifier.width(MaterialTheme.spacing.small))
            Text("Wanita", style = com.example.ui.theme.DiajakDesignSystem.Typography.Body, color = MaterialTheme.colorScheme.onSurface)
          }
        }
        Spacer(modifier = Modifier.height(20.dp))
        Row(
          modifier = Modifier.fillMaxWidth(),
          horizontalArrangement = Arrangement.spacedBy(20.dp)
        ) {
          OutlinedButton(
            onClick = { showGenderDialog = false },
            modifier = Modifier.weight(1f),
            border = BorderStroke(1.5.dp, DiajakOrange),
            colors = ButtonDefaults.outlinedButtonColors(containerColor = Color.Transparent)
          ) {
            Text("Batal", style = androidx.compose.ui.text.TextStyle(fontSize = 16.sp, fontWeight = androidx.compose.ui.text.font.FontWeight.Bold, fontFamily = com.example.ui.theme.AppFontFamily), color = DiajakOrange)
          }
          Button(
            onClick = {
              onUpdateGender(tempGender)
              showGenderDialog = false
              Toast.makeText(context, "Jenis Kelamin diperbarui!", Toast.LENGTH_SHORT).show()
            },
            modifier = Modifier.weight(1f),
            colors = ButtonDefaults.buttonColors(containerColor = DiajakOrange)
          ) {
            Text("Simpan")
          }
        }
      }
    }
  }

  // 3b. Edit Birth Date Bottom Sheet
  if (showBirthDateDialog) {
    // Parse initial values
    var initialDay = 12
    var initialMonthIndex = 7 // Agustus
    var initialYear = 1987

    try {
      val parts = profileBirthDate.split("/")
      if (parts.size == 3) {
        initialDay = parts[0].toIntOrNull() ?: 12
        initialMonthIndex = (parts[1].toIntOrNull() ?: 8) - 1
        initialYear = parts[2].toIntOrNull() ?: 1987
      }
    } catch (e: Exception) {
      // ignore
    }

    val monthsList = listOf(
      "Jan", "Feb", "Mar", "Apr", "Mei", "Jun", 
      "Jul", "Ags", "Sep", "Okt", "Nov", "Des"
    )
    val yearsList = (1950..2026).map { it.toString() }

    // Selected state holders
    var selectedMonthIndex by remember { mutableStateOf(initialMonthIndex) }
    var selectedYear by remember { mutableStateOf(initialYear) }

    // Days count depends on selected month & year
    val daysInMonth = remember(selectedMonthIndex, selectedYear) {
      val yearVal = selectedYear
      when (selectedMonthIndex) {
        1 -> if ((yearVal % 4 == 0 && yearVal % 100 != 0) || (yearVal % 400 == 0)) 29 else 28
        3, 5, 8, 10 -> 30
        else -> 31
      }
    }

    var selectedDay by remember {
      mutableStateOf(initialDay)
    }

    LaunchedEffect(daysInMonth) {
      if (selectedDay > daysInMonth) {
        selectedDay = daysInMonth
      }
    }

    val daysList = remember(daysInMonth) {
      (1..daysInMonth).map { it.toString().padStart(2, '0') }
    }

    ModalBottomSheet(
      onDismissRequest = { showBirthDateDialog = false },
      sheetState = rememberModalBottomSheetState(skipPartiallyExpanded = true),
      containerColor = androidx.compose.ui.graphics.Color(0xFFE5E7EB)
    ) {
      Column(
        modifier = Modifier
          .fillMaxWidth()
          .navigationBarsPadding()
          .padding(bottom = 20.dp)
      ) {
        Row(
          modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 20.dp, vertical = 20.dp),
          horizontalArrangement = Arrangement.SpaceBetween,
          verticalAlignment = Alignment.CenterVertically
        ) {
          Text(
            text = "Pilih Tanggal Lahir",
            style = com.example.ui.theme.DiajakDesignSystem.Typography.Body,
            color = MaterialTheme.colorScheme.onSurface
          )
        }

        Spacer(modifier = Modifier.height(MaterialTheme.spacing.small))

        // Three Wheel Columns Side-by-Side (iOS Style)
        Row(
          modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 20.dp)
            .height(180.dp),
          horizontalArrangement = Arrangement.spacedBy(20.dp)
        ) {
          // Day Column
          Box(modifier = Modifier.weight(1f)) {
            WheelPicker(
              items = daysList,
              initialIndex = (selectedDay - 1).coerceIn(0, daysList.size - 1),
              onIndexSelected = { index ->
                selectedDay = index + 1
              }
            )
          }

          // Month Column
          Box(modifier = Modifier.weight(1.5f)) {
            WheelPicker(
              items = monthsList,
              initialIndex = selectedMonthIndex.coerceIn(0, monthsList.size - 1),
              onIndexSelected = { index ->
                selectedMonthIndex = index
              }
            )
          }

          // Year Column
          Box(modifier = Modifier.weight(1.2f)) {
            val initialYearIndex = yearsList.indexOf(selectedYear.toString()).coerceIn(0, yearsList.size - 1)
            WheelPicker(
              items = yearsList,
              initialIndex = initialYearIndex,
              onIndexSelected = { index ->
                selectedYear = yearsList[index].toInt()
              }
            )
          }
        }

        Spacer(modifier = Modifier.height(20.dp))

        Row(
          modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 20.dp),
          horizontalArrangement = Arrangement.spacedBy(20.dp)
        ) {
          OutlinedButton(
            onClick = { showBirthDateDialog = false },
            modifier = Modifier.weight(1f),
            border = BorderStroke(1.5.dp, DiajakOrange),
            colors = ButtonDefaults.outlinedButtonColors(containerColor = Color.Transparent)
          ) {
            Text("Batal", style = androidx.compose.ui.text.TextStyle(fontSize = 16.sp, fontWeight = androidx.compose.ui.text.font.FontWeight.Bold, fontFamily = com.example.ui.theme.AppFontFamily), color = DiajakOrange)
          }
          Button(
            onClick = {
              val formattedDay = selectedDay.toString().padStart(2, '0')
              val formattedMonth = (selectedMonthIndex + 1).toString().padStart(2, '0')
              val formattedYear = selectedYear.toString()
              val formattedDate = "$formattedDay/$formattedMonth/$formattedYear"
              onUpdateBirthDate(formattedDate)
              showBirthDateDialog = false
              Toast.makeText(context, "Tanggal lahir berhasil diperbarui!", Toast.LENGTH_SHORT).show()
            },
            modifier = Modifier.weight(1f),
            colors = ButtonDefaults.buttonColors(containerColor = DiajakOrange)
          ) {
            Text("Simpan")
          }
        }
      }
    }
  }

  // 4. Edit Phone Bottom Sheet
  if (showPhoneDialog) {
    ModalBottomSheet(
      onDismissRequest = { showPhoneDialog = false },
      sheetState = rememberModalBottomSheetState(skipPartiallyExpanded = true),
      containerColor = androidx.compose.ui.graphics.Color(0xFFE5E7EB)
    ) {
      Column(
        modifier = Modifier
          .fillMaxWidth()
          .navigationBarsPadding()
          .padding(horizontal = 20.dp, vertical = 20.dp)
      ) {
        Text("Ubah No. Handphone", style = androidx.compose.ui.text.TextStyle(fontSize = 16.sp, fontWeight = androidx.compose.ui.text.font.FontWeight.Bold, fontFamily = com.example.ui.theme.AppFontFamily), color = MaterialTheme.colorScheme.onSurface)
        Spacer(modifier = Modifier.height(20.dp))
        OutlinedTextField(
          value = tempPhone,
          onValueChange = { tempPhone = it },
          label = { Text("No. Handphone") },
          singleLine = true,
          keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Phone),
          modifier = Modifier.fillMaxWidth(),
          colors = OutlinedTextFieldDefaults.colors(
            focusedTextColor = MaterialTheme.colorScheme.onSurface,
            unfocusedTextColor = MaterialTheme.colorScheme.onSurface,
            focusedLabelColor = DiajakOrange,
            unfocusedLabelColor = MaterialTheme.colorScheme.onSurface,
            focusedBorderColor = DiajakOrange,
            unfocusedBorderColor = Color.Transparent,
            cursorColor = DiajakOrange,
            focusedPlaceholderColor = MaterialTheme.colorScheme.onSurface,
            unfocusedPlaceholderColor = MaterialTheme.colorScheme.onSurface
          )
        )
        Spacer(modifier = Modifier.height(20.dp))
        Row(
          modifier = Modifier.fillMaxWidth(),
          horizontalArrangement = Arrangement.spacedBy(20.dp)
        ) {
          OutlinedButton(
            onClick = { showPhoneDialog = false },
            modifier = Modifier.weight(1f),
            border = BorderStroke(1.5.dp, DiajakOrange),
            colors = ButtonDefaults.outlinedButtonColors(containerColor = Color.Transparent)
          ) {
            Text("Batal", style = androidx.compose.ui.text.TextStyle(fontSize = 16.sp, fontWeight = androidx.compose.ui.text.font.FontWeight.Bold, fontFamily = com.example.ui.theme.AppFontFamily), color = DiajakOrange)
          }
          Button(
            onClick = {
              if (tempPhone.isNotBlank()) {
                onUpdatePhone(tempPhone)
                showPhoneDialog = false
                Toast.makeText(context, "No. Handphone berhasil diperbarui!", Toast.LENGTH_SHORT).show()
              } else {
                Toast.makeText(context, "No. Handphone tidak boleh kosong!", Toast.LENGTH_SHORT).show()
              }
            },
            modifier = Modifier.weight(1f),
            colors = ButtonDefaults.buttonColors(containerColor = DiajakOrange)
          ) {
            Text("Simpan")
          }
        }
      }
    }
  }

  // 5. Edit Email Bottom Sheet
  if (showEmailDialog) {
    ModalBottomSheet(
      onDismissRequest = { showEmailDialog = false },
      sheetState = rememberModalBottomSheetState(skipPartiallyExpanded = true),
      containerColor = androidx.compose.ui.graphics.Color(0xFFE5E7EB)
    ) {
      Column(
        modifier = Modifier
          .fillMaxWidth()
          .navigationBarsPadding()
          .padding(horizontal = 20.dp, vertical = 20.dp)
      ) {
        Text("Ubah Email", style = androidx.compose.ui.text.TextStyle(fontSize = 16.sp, fontWeight = androidx.compose.ui.text.font.FontWeight.Bold, fontFamily = com.example.ui.theme.AppFontFamily), color = MaterialTheme.colorScheme.onSurface)
        Spacer(modifier = Modifier.height(20.dp))
        OutlinedTextField(
          value = tempEmail,
          onValueChange = { tempEmail = it },
          label = { Text("Email") },
          singleLine = true,
          keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Email),
          modifier = Modifier.fillMaxWidth(),
          colors = OutlinedTextFieldDefaults.colors(
            focusedTextColor = MaterialTheme.colorScheme.onSurface,
            unfocusedTextColor = MaterialTheme.colorScheme.onSurface,
            focusedLabelColor = DiajakOrange,
            unfocusedLabelColor = MaterialTheme.colorScheme.onSurface,
            focusedBorderColor = DiajakOrange,
            unfocusedBorderColor = Color.Transparent,
            cursorColor = DiajakOrange,
            focusedPlaceholderColor = MaterialTheme.colorScheme.onSurface,
            unfocusedPlaceholderColor = MaterialTheme.colorScheme.onSurface
          )
        )
        Spacer(modifier = Modifier.height(20.dp))
        Row(
          modifier = Modifier.fillMaxWidth(),
          horizontalArrangement = Arrangement.spacedBy(20.dp)
        ) {
          OutlinedButton(
            onClick = { showEmailDialog = false },
            modifier = Modifier.weight(1f),
            border = BorderStroke(1.5.dp, DiajakOrange),
            colors = ButtonDefaults.outlinedButtonColors(containerColor = Color.Transparent)
          ) {
            Text("Batal", style = androidx.compose.ui.text.TextStyle(fontSize = 16.sp, fontWeight = androidx.compose.ui.text.font.FontWeight.Bold, fontFamily = com.example.ui.theme.AppFontFamily), color = DiajakOrange)
          }
          Button(
            onClick = {
              if (tempEmail.isNotBlank()) {
                onUpdateEmail(tempEmail)
                showEmailDialog = false
                Toast.makeText(context, "Email berhasil diperbarui!", Toast.LENGTH_SHORT).show()
              } else {
                Toast.makeText(context, "Email tidak boleh kosong!", Toast.LENGTH_SHORT).show()
              }
            },
            modifier = Modifier.weight(1f),
            colors = ButtonDefaults.buttonColors(containerColor = DiajakOrange)
          ) {
            Text("Simpan")
          }
        }
      }
    }
  }

  // 6. Ubah Avatar Selection Bottom Sheet
  if (showAvatarDialog) {
    ModalBottomSheet(
      onDismissRequest = { showAvatarDialog = false },
      sheetState = rememberModalBottomSheetState(skipPartiallyExpanded = true),
      containerColor = androidx.compose.ui.graphics.Color(0xFFE5E7EB)
    ) {
      Column(
        modifier = Modifier
          .fillMaxWidth()
          .navigationBarsPadding()
          .padding(horizontal = 20.dp, vertical = 20.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.spacedBy(20.dp)
      ) {
        Text("Pilih Foto Profil", style = androidx.compose.ui.text.TextStyle(fontSize = 16.sp, fontWeight = androidx.compose.ui.text.font.FontWeight.Bold, fontFamily = com.example.ui.theme.AppFontFamily), color = MaterialTheme.colorScheme.onSurface)
        
        // Gallery Selector Button
        Button(
          onClick = {
            showAvatarDialog = false
            imagePickerLauncher.launch("image/*")
          },
          colors = ButtonDefaults.buttonColors(containerColor = DiajakOrange),
          modifier = Modifier.fillMaxWidth(),
          shape = RoundedCornerShape(16.dp)
        ) {
          Icon(
            imageVector = Icons.Outlined.Edit,
            contentDescription = null
          )
          Spacer(modifier = Modifier.width(MaterialTheme.spacing.small))
          Text("Pilih dari Galeri HP", fontWeight = FontWeight.Medium)
        }

        Text(
          text = "Atau gunakan Preset Pilihan:",
          style = com.example.ui.theme.DiajakDesignSystem.Typography.Body,
          color = MaterialTheme.colorScheme.onSurface
        )

        // 2x2 Preset Grid
        Column(
          modifier = Modifier.fillMaxWidth(),
          verticalArrangement = Arrangement.spacedBy(20.dp)
        ) {
          Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceEvenly
          ) {
            // White Cat option
            Column(
              horizontalAlignment = Alignment.CenterHorizontally,
              modifier = Modifier.clickable {
                onUpdateImageRes(R.drawable.img_profile_cat_1783601304885)
                showAvatarDialog = false
                Toast.makeText(context, "Foto profil diubah ke Kucing!", Toast.LENGTH_SHORT).show()
              }
            ) {
              Box(
                modifier = Modifier
                  .size(64.dp)
                  .clip(CircleShape)
                  .background(MaterialTheme.colorScheme.outlineVariant)
              ) {
                Image(
                  painter = painterResource(id = R.drawable.img_profile_cat_1783601304885),
                  contentDescription = "Kucing",
                  modifier = Modifier.fillMaxSize(),
                  contentScale = ContentScale.Crop
                )
              }
              Spacer(modifier = Modifier.height(MaterialTheme.spacing.extraSmall))
              Text("Kucing", style = androidx.compose.ui.text.TextStyle(fontSize = 16.sp, fontWeight = androidx.compose.ui.text.font.FontWeight.Bold, fontFamily = com.example.ui.theme.AppFontFamily))
            }

            // Panda option
            Column(
              horizontalAlignment = Alignment.CenterHorizontally,
              modifier = Modifier.clickable {
                onUpdateImageRes(R.drawable.img_profile_panda_1783603225548)
                showAvatarDialog = false
                Toast.makeText(context, "Foto profil diubah ke Panda!", Toast.LENGTH_SHORT).show()
              }
            ) {
              Box(
                modifier = Modifier
                  .size(64.dp)
                  .clip(CircleShape)
                  .background(MaterialTheme.colorScheme.outlineVariant)
              ) {
                Image(
                  painter = painterResource(id = R.drawable.img_profile_panda_1783603225548),
                  contentDescription = "Panda",
                  modifier = Modifier.fillMaxSize(),
                  contentScale = ContentScale.Crop
                )
              }
              Spacer(modifier = Modifier.height(MaterialTheme.spacing.extraSmall))
              Text("Panda", style = androidx.compose.ui.text.TextStyle(fontSize = 16.sp, fontWeight = androidx.compose.ui.text.font.FontWeight.Bold, fontFamily = com.example.ui.theme.AppFontFamily))
            }
          }

          Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceEvenly
          ) {
            // Dog option
            Column(
              horizontalAlignment = Alignment.CenterHorizontally,
              modifier = Modifier.clickable {
                onUpdateImageRes(R.drawable.img_profile_dog_1783603245364)
                showAvatarDialog = false
                Toast.makeText(context, "Foto profil diubah ke Anjing!", Toast.LENGTH_SHORT).show()
              }
            ) {
              Box(
                modifier = Modifier
                  .size(64.dp)
                  .clip(CircleShape)
                  .background(MaterialTheme.colorScheme.outlineVariant)
              ) {
                Image(
                  painter = painterResource(id = R.drawable.img_profile_dog_1783603245364),
                  contentDescription = "Anjing",
                  modifier = Modifier.fillMaxSize(),
                  contentScale = ContentScale.Crop
                )
              }
              Spacer(modifier = Modifier.height(MaterialTheme.spacing.extraSmall))
              Text("Anjing", style = androidx.compose.ui.text.TextStyle(fontSize = 16.sp, fontWeight = androidx.compose.ui.text.font.FontWeight.Bold, fontFamily = com.example.ui.theme.AppFontFamily))
            }

            // Default Logo option
            Column(
              horizontalAlignment = Alignment.CenterHorizontally,
              modifier = Modifier.clickable {
                onUpdateImageRes(R.drawable.ic_diajak_logo)
                showAvatarDialog = false
                Toast.makeText(context, "Foto profil diubah ke Logo Aplikasi!", Toast.LENGTH_SHORT).show()
              }
            ) {
              Box(
                modifier = Modifier
                  .size(64.dp)
                  .clip(CircleShape)
                  .background(DiajakOrange),
                contentAlignment = Alignment.Center
              ) {
                Image(
                  painter = painterResource(id = R.drawable.ic_diajak_logo),
                  contentDescription = "Logo",
                  modifier = Modifier.fillMaxSize(),
                  contentScale = ContentScale.Fit
                )
              }
              Spacer(modifier = Modifier.height(MaterialTheme.spacing.extraSmall))
              Text("Logo Aplikasi", style = androidx.compose.ui.text.TextStyle(fontSize = 16.sp, fontWeight = androidx.compose.ui.text.font.FontWeight.Bold, fontFamily = com.example.ui.theme.AppFontFamily))
            }
          }
        }
        
        Spacer(modifier = Modifier.height(MaterialTheme.spacing.small))
        OutlinedButton(
          onClick = { showAvatarDialog = false },
          modifier = Modifier.fillMaxWidth(),
          border = BorderStroke(1.5.dp, DiajakOrange),
          colors = ButtonDefaults.outlinedButtonColors(containerColor = Color.Transparent)
        ) {
          Text("Batal", style = androidx.compose.ui.text.TextStyle(fontSize = 16.sp, fontWeight = androidx.compose.ui.text.font.FontWeight.Bold, fontFamily = com.example.ui.theme.AppFontFamily), color = DiajakOrange)
        }
      }
    }
  }
}

@Composable
fun EditProfileRow(
  label: String,
  value: String,
  hasArrow: Boolean,
  onClick: () -> Unit
) {
  Row(
    modifier = Modifier
      .fillMaxWidth()
      .clickable { onClick() }
      .padding(vertical = MaterialTheme.spacing.medium),
    verticalAlignment = Alignment.CenterVertically,
    horizontalArrangement = Arrangement.SpaceBetween
  ) {
    Text(
      text = label,
      style = com.example.ui.theme.DiajakDesignSystem.Typography.Body,
      color = MaterialTheme.colorScheme.onSurface,
      modifier = Modifier.weight(1.2f)
    )
    
    Row(
      modifier = Modifier.weight(2f),
      verticalAlignment = Alignment.CenterVertically,
      horizontalArrangement = Arrangement.End
    ) {
      Text(
        text = value,
        style = com.example.ui.theme.DiajakDesignSystem.Typography.BodyBold,
        color = MaterialTheme.colorScheme.onSurface
      )
      
      if (hasArrow) {
        Spacer(modifier = Modifier.width(MaterialTheme.spacing.extraSmall))
        Icon(
          imageVector = Icons.Outlined.ChevronRight,
          contentDescription = null,
          tint = MaterialTheme.colorScheme.onSurface,
          modifier = Modifier.size(20.dp)
        )
      }
    }
  }
}

@Composable
fun WheelPicker(
  items: List<String>,
  initialIndex: Int,
  onIndexSelected: (Int) -> Unit,
  modifier: Modifier = Modifier
) {
  val paddedItems = remember(items) {
    listOf("", "") + items + listOf("", "")
  }
  
  val listState = rememberLazyListState(initialFirstVisibleItemIndex = initialIndex)
  val coroutineScope = rememberCoroutineScope()
  
  // Track the current selected index internally to avoid feedback loops
  var selectedIndex by remember { mutableStateOf(initialIndex) }
  
  // Sync initialIndex from parent if it changes externally (only when not scrolling)
  LaunchedEffect(initialIndex) {
    if (!listState.isScrollInProgress && selectedIndex != initialIndex) {
      selectedIndex = initialIndex
      listState.scrollToItem(initialIndex)
    }
  }
  
  // Use snapshotFlow to monitor listState scrolling and find the nearest item
  LaunchedEffect(listState) {
    snapshotFlow { 
      val firstVisible = listState.firstVisibleItemIndex
      val offset = listState.firstVisibleItemScrollOffset
      val layoutInfo = listState.layoutInfo
      val visibleItems = layoutInfo.visibleItemsInfo
      if (visibleItems.isNotEmpty()) {
        val itemHeight = visibleItems.first().size
        if (itemHeight > 0) {
          val added = if (offset > itemHeight / 2) 1 else 0
          (firstVisible + added).coerceIn(0, items.size - 1)
        } else {
          firstVisible.coerceIn(0, items.size - 1)
        }
      } else {
        firstVisible.coerceIn(0, items.size - 1)
      }
    }
    .distinctUntilChanged()
    .collect { index ->
      selectedIndex = index
      onIndexSelected(index)
    }
  }
  
  // Snap to the selected index when scroll finishes
  LaunchedEffect(listState.isScrollInProgress) {
    if (!listState.isScrollInProgress) {
      listState.animateScrollToItem(selectedIndex)
    }
  }
  
  Box(
    modifier = modifier
      .height(180.dp)
      .background(Color.White, RoundedCornerShape(12.dp)),
    contentAlignment = Alignment.Center
  ) {
    // Highlight indicator
    Box(
      modifier = Modifier
        .fillMaxWidth()
        .height(36.dp)
        .background(MaterialTheme.colorScheme.onSurface.copy(alpha = 0.05f), RoundedCornerShape(8.dp))
        .border(0.dp, Color.Transparent, RoundedCornerShape(8.dp))
    )
    
    LazyColumn(
      state = listState,
      modifier = Modifier.fillMaxSize(),
      horizontalAlignment = Alignment.CenterHorizontally
    ) {
      items(paddedItems.size) { index ->
        val isSelected = (selectedIndex + 2) == index
        Box(
          modifier = Modifier
            .fillMaxWidth()
            .height(36.dp)
            .clickable(
              interactionSource = remember { MutableInteractionSource() },
              indication = null
            ) {
              val targetIndex = index - 2
              if (targetIndex in items.indices) {
                coroutineScope.launch {
                  listState.animateScrollToItem(targetIndex)
                  selectedIndex = targetIndex
                  onIndexSelected(targetIndex)
                }
              }
            },
          contentAlignment = Alignment.Center
        ) {
          val text = paddedItems[index]
          if (text.isNotEmpty()) {
            Text(
              text = text,
              style = com.example.ui.theme.DiajakDesignSystem.Typography.Body,
              fontWeight = if (isSelected) FontWeight.Bold else FontWeight.Medium,
              color = if (isSelected) DiajakOrange else MaterialTheme.colorScheme.onSurface
            )
          }
        }
      }
    }
  }
}

