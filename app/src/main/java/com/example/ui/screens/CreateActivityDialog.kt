package com.example.ui.screens
import com.example.ui.theme.spacing
import com.example.ui.components.DiajakFlowRow
import com.example.data.DiajakRepository
import com.example.ui.utils.getCategoryIconVector
import androidx.compose.material3.MaterialTheme

import androidx.compose.material.icons.outlined.*
import androidx.compose.material.icons.automirrored.outlined.*

import android.widget.Toast
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.core.*
import androidx.compose.animation.slideInVertically
import androidx.compose.animation.slideOutVertically
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.gestures.detectTapGestures
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.outlined.*
import androidx.compose.material.icons.automirrored.outlined.*
import androidx.compose.material.icons.outlined.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Path
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Dialog
import androidx.compose.ui.window.DialogProperties
import androidx.compose.foundation.gestures.detectTransformGestures
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.graphics.TransformOrigin
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import com.example.R
import com.example.ui.theme.DiajakOrange
import com.example.ui.theme.DiajakOrangeLight
import androidx.compose.ui.layout.layout
import com.example.ui.theme.DiajakOrangeDark
import dev.chrisbanes.haze.HazeState
import dev.chrisbanes.haze.hazeSource
import dev.chrisbanes.haze.hazeEffect
import com.example.ui.components.DiajakGlassHeader
import com.example.ui.theme.diajakGlassButton
import androidx.compose.ui.zIndex
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

@OptIn(ExperimentalMaterial3Api::class, ExperimentalLayoutApi::class)
@Composable
fun CreateActivityDialog(
  onDismiss: () -> Unit,
  onSubmit: (
    title: String,
    category: String,
    location: String,
    address: String,
    schedule: String,
    priceFormatted: String,
    priceValue: Int,
    overview: String,
    quota: Int,
    imageRes: Int,
    benefits: List<String>,
    mapX: Float,
    mapY: Float,
    isOngoing: Boolean,
    isRecurring: Boolean,
    recurringDays: String
  ) -> Unit
) {
  val context = LocalContext.current
  val coroutineScope = rememberCoroutineScope()
  val focusManager = androidx.compose.ui.platform.LocalFocusManager.current

  // Wizard state: 1, 2, or 3
  var currentStep by remember { mutableStateOf(1) }

  // --- Step 1 Fields: Dasar & Benefit ---
  var title by remember { mutableStateOf("") }
  var selectedCategory by remember { mutableStateOf("Alam") }
  var quotaString by remember { mutableStateOf("") }
  var overview by remember { mutableStateOf("") }

  // Benefit checklist
  val standardBenefits = listOf(
    "Free Welcome Drink / Snack",
    "E-Certificate Resmi",
    "Peralatan Lengkap Disediakan",
    "Dokumentasi Foto & Video HD",
    "Pemandu Berlisensi",
    "Transportasi PP",
    "Merchandise Eksklusif",
    "Koneksi Wi-Fi Cepat"
  )
  val selectedBenefits = remember { mutableStateListOf<String>() }
  var customBenefitInput by remember { mutableStateOf("") }

  // --- Step 2 Fields: Jadwal & Biaya ---
  var isFree by remember { mutableStateOf(false) }
  var priceString by remember { mutableStateOf("") }

  // Recurring / Routine fields
  var isRecurringAct by remember { mutableStateOf(false) }
  var isOngoingAct by remember { mutableStateOf(false) }
  val selectedDays = remember { mutableStateListOf<String>() }

  // Multi-date Calendar Selector (July 2026)
  // July 2026 starts on Wednesday (1st)
  val totalDaysInJuly = 31
  val startingDayOffset = 3 // Wed (Sun=0, Mon=1, Tue=2, Wed=3)
  val selectedDates = remember { mutableStateListOf<Int>() }

  // --- Step 3 Fields: Lokasi & Pin Peta ---
  var locationName by remember { mutableStateOf("") }
  var address by remember { mutableStateOf("") }
  var pinX by remember { mutableStateOf(0.5f) }
  var pinY by remember { mutableStateOf(0.5f) }
  var showMapSheet by remember { mutableStateOf(false) }
  var isFullScreenMapMode by remember { mutableStateOf(false) }
  var isSimulatingNearMe by remember { mutableStateOf(false) }
  var mapScale by remember { mutableStateOf(1.2f) }
  var mapOffset by remember { mutableStateOf(Offset.Zero) }

  val categories = DiajakRepository.categoryNames

  Dialog(
    onDismissRequest = onDismiss,
    properties = DialogProperties(
      usePlatformDefaultWidth = false,
      decorFitsSystemWindows = false
    )
  ) {
    val dialogHazeState = remember { HazeState() }

    Surface(
      modifier = Modifier
        .fillMaxWidth(0.96f)
        .fillMaxHeight(0.94f),
      shape = RoundedCornerShape(16.dp),
      color = Color(0xFFE5E7EB)
    ) {
      if (isFullScreenMapMode) {
        Box(
          modifier = Modifier
            .fillMaxSize()
            .pointerInput(Unit) {
              detectTransformGestures { _, pan, zoom, _ ->
                mapScale = (mapScale * zoom).coerceIn(0.6f, 4.0f)
                mapOffset += pan
              }
            }
        ) {
          val animatedMapScale by animateFloatAsState(targetValue = mapScale, label = "mapScale", animationSpec = spring())
          val animatedMapOffsetX by animateFloatAsState(targetValue = mapOffset.x, label = "mapOffsetX", animationSpec = spring())
          val animatedMapOffsetY by animateFloatAsState(targetValue = mapOffset.y, label = "mapOffsetY", animationSpec = spring())

          BoxWithConstraints(
            modifier = Modifier
              .fillMaxSize()
              .hazeSource(state = dialogHazeState)
          ) {
            val mapWidth = maxWidth
            val mapHeight = maxHeight
            val density = LocalDensity.current
            val widthPx = with(density) { mapWidth.toPx() }
            val heightPx = with(density) { mapHeight.toPx() }

            // Smooth auto-centering on the active selected pin coordinate initially
            LaunchedEffect(pinX, pinY) {
              val targetX = -(widthPx * pinX - widthPx / 2f) * mapScale
              val targetY = -(heightPx * pinY - heightPx / 2f) * mapScale
              mapOffset = Offset(targetX, targetY)
            }

            // Inner map container that translates/scales
            Box(
              modifier = Modifier
                .fillMaxSize()
                .graphicsLayer(
                  scaleX = animatedMapScale,
                  scaleY = animatedMapScale,
                  translationX = animatedMapOffsetX,
                  translationY = animatedMapOffsetY,
                  transformOrigin = TransformOrigin(0.5f, 0.5f)
                )
            ) {
              // Stylized canvas map background representation matching explore screen
              Canvas(
                modifier = Modifier
                  .fillMaxSize()
                  .pointerInput(Unit) {
                    detectTapGestures { offset ->
                      val actualX = ((offset.x - mapOffset.x - (widthPx / 2f) * (1f - mapScale)) / (widthPx * mapScale)).coerceIn(0.05f, 0.95f)
                      val actualY = ((offset.y - mapOffset.y - (heightPx / 2f) * (1f - mapScale)) / (heightPx * mapScale)).coerceIn(0.05f, 0.95f)
                      pinX = actualX
                      pinY = actualY
                    }
                  }
              ) {
                val w = size.width
                val h = size.height

                // Water blue background
                drawRect(color = Color(0xFFE3F4F4))

                // Land area path
                val landPath = Path().apply {
                  moveTo(0f, h * 0.15f)
                  quadraticTo(w * 0.4f, h * 0.2f, w * 0.7f, 0f)
                  lineTo(w, 0f)
                  lineTo(w, h)
                  lineTo(0f, h)
                  close()
                }
                drawPath(landPath, color = com.example.ui.theme.BackgroundLight)

                // Parks
                drawCircle(Color(0xFFDCFCE7), radius = w * 0.25f, center = Offset(w * 0.15f, h * 0.2f))
                drawCircle(Color(0xFFDCFCE7), radius = w * 0.18f, center = Offset(w * 0.8f, h * 0.75f))
                
                // Lakes
                val lakePath = Path().apply {
                  moveTo(w * 0.65f, h)
                  quadraticTo(w * 0.7f, h * 0.7f, w, h * 0.6f)
                  lineTo(w, h)
                  close()
                }
                drawPath(lakePath, Color(0xFFE0F2FE))

                // Grid roads
                val roadColor = Color.White
                drawLine(roadColor, Offset(0f, h * 0.45f), Offset(w, h * 0.45f), strokeWidth = 16f)
                drawLine(roadColor, Offset(w * 0.45f, 0f), Offset(w * 0.45f, h), strokeWidth = 16f)
                drawLine(roadColor, Offset(0f, h * 0.75f), Offset(w, h * 0.7f), strokeWidth = 12f)
                drawLine(roadColor, Offset(w * 0.8f, 0f), Offset(w * 0.75f, h), strokeWidth = 12f)
              }

              // Render preset locations inside map coordinate space using new CustomMapPinMarker
              presetLocations.forEach { loc ->
                val lx = widthPx * loc.x
                val ly = heightPx * loc.y
                val isSelected = (pinX == loc.x && pinY == loc.y)

                Box(
                  modifier = Modifier
                    .layout { measurable, constraints ->
                      val placeable = measurable.measure(constraints)
                      layout(placeable.width, placeable.height) {
                        placeable.placeRelative(
                          x = (lx - placeable.width / 2f).toInt(),
                          y = (ly - placeable.height).toInt()
                        )
                      }
                    }
                ) {
                  CustomMapPinMarker(
                    categoryIcon = loc.categoryIcon,
                    isSelected = isSelected,
                    title = loc.name,
                    onClick = {
                      pinX = loc.x
                      pinY = loc.y
                      locationName = loc.cityName
                      address = "${loc.name}, ${loc.address}"
                    }
                  )
                }
              }

              // Active pinned coordinate marker on map if not matching any preset
              if (presetLocations.none { it.x == pinX && it.y == pinY }) {
                val lx = widthPx * pinX
                val ly = heightPx * pinY
                Box(
                  modifier = Modifier
                    .layout { measurable, constraints ->
                      val placeable = measurable.measure(constraints)
                      layout(placeable.width, placeable.height) {
                        placeable.placeRelative(
                          x = (lx - placeable.width / 2f).toInt(),
                          y = (ly - placeable.height).toInt()
                        )
                      }
                    }
                ) {
                  CustomMapPinMarker(
                    categoryIcon = getCategoryIcon(selectedCategory),
                    isSelected = true,
                    title = locationName.ifEmpty { "Titik Terpilih" }
                  )
                }
              }
            }

            // Zoom controls & MyLocation GPS Button on the right
            Column(
              modifier = Modifier
                .align(Alignment.CenterEnd)
                .padding(end = 20.dp),
              verticalArrangement = Arrangement.spacedBy(MaterialTheme.spacing.small)
            ) {
              FloatingActionButton(
                onClick = { mapScale = (mapScale + 0.3f).coerceAtMost(4.0f) },
                containerColor = androidx.compose.ui.graphics.Color(0xFFE5E7EB),
                contentColor = MaterialTheme.colorScheme.onSurface,
                modifier = Modifier.size(44.dp),
                 
                elevation = FloatingActionButtonDefaults.elevation(0.dp),
                shape = RoundedCornerShape(16.dp)
              ) {
                Icon(Icons.Outlined.Add, contentDescription = "Zoom In", modifier = Modifier.size(20.dp))
              }
              FloatingActionButton(
                onClick = { mapScale = (mapScale - 0.3f).coerceIn(0.6f, 4.0f) },
                containerColor = androidx.compose.ui.graphics.Color(0xFFE5E7EB),
                contentColor = MaterialTheme.colorScheme.onSurface,
                modifier = Modifier.size(44.dp),
                 
                elevation = FloatingActionButtonDefaults.elevation(0.dp),
                shape = RoundedCornerShape(16.dp)
              ) {
                Icon(Icons.Outlined.Remove, contentDescription = "Zoom Out", modifier = Modifier.size(20.dp))
              }
              FloatingActionButton(
                onClick = {
                  coroutineScope.launch {
                    isSimulatingNearMe = true
                    Toast.makeText(context, "Mendeteksi sinyal GPS terdekat...", Toast.LENGTH_SHORT).show()
                    delay(1000)
                    val preset = presetLocations[5] // Nox Coffee Boutique
                    pinX = preset.x
                    pinY = preset.y
                    locationName = preset.cityName
                    address = preset.address
                    isSimulatingNearMe = false
                    Toast.makeText(context, "GPS Terpusat: Sleman, Nox Coffee Boutique 📍", Toast.LENGTH_LONG).show()
                  }
                },
                containerColor = androidx.compose.ui.graphics.Color(0xFFE5E7EB),
                contentColor = DiajakOrange,
                modifier = Modifier.size(44.dp),
                 
                elevation = FloatingActionButtonDefaults.elevation(0.dp),
                shape = RoundedCornerShape(16.dp)
              ) {
                if (isSimulatingNearMe) {
                  CircularProgressIndicator(color = DiajakOrange, modifier = Modifier.size(18.dp), strokeWidth = 2.dp)
                } else {
                  Icon(Icons.Outlined.MyLocation, contentDescription = "Deteksi GPS", modifier = Modifier.size(20.dp))
                }
              }
            }

            // Floating header search / input card at the top (kolom melayang)
            Surface(
              modifier = Modifier
                .align(Alignment.TopCenter)
                .fillMaxWidth()
                .padding(20.dp),
              shadowElevation = 0.dp,
              color = Color.White,
              shape = RoundedCornerShape(20.dp),
              border = BorderStroke(0.dp, Color.Transparent)
            ) {
              Column(
                modifier = Modifier.padding(20.dp),
                verticalArrangement = Arrangement.spacedBy(MaterialTheme.spacing.small)
              ) {
                Row(
                  modifier = Modifier.fillMaxWidth(),
                  verticalAlignment = Alignment.CenterVertically,
                  horizontalArrangement = Arrangement.SpaceBetween
                ) {
                  Row(verticalAlignment = Alignment.CenterVertically, horizontalArrangement = Arrangement.spacedBy(MaterialTheme.spacing.small)) {
                    Icon(Icons.Outlined.Explore, contentDescription = null, tint = DiajakOrange, modifier = Modifier.size(18.dp))
                    Text("Tentukan Koordinat Titik Temu", style = com.example.ui.theme.DiajakDesignSystem.Typography.TitleBold)
                  }
                  Text(
                    "Seret & Ketuk Peta",
                    color = Color(0xFF262626),
                    style = com.example.ui.theme.DiajakDesignSystem.Typography.TitleBold,
                    
                  )
                }

                // City / Area
                OutlinedTextField(
                  value = locationName,
                  onValueChange = { locationName = it },
                  label = { Text("Nama Kota / Wilayah") },
                  placeholder = { Text("Misal: Sleman, Yogyakarta") },
                  modifier = Modifier.fillMaxWidth(),
                  singleLine = true,
                  leadingIcon = { Icon(Icons.Outlined.Place, contentDescription = null, tint = DiajakOrange) },
                  colors = OutlinedTextFieldDefaults.colors(
                    focusedTextColor = MaterialTheme.colorScheme.onSurface,
                    unfocusedTextColor = MaterialTheme.colorScheme.onSurface,
                    focusedLabelColor = DiajakOrange,
                    unfocusedLabelColor = MaterialTheme.colorScheme.onSurface,
                    focusedBorderColor = DiajakOrange,
                    unfocusedBorderColor = Color.Transparent
                  )
                )

                // Detailed address
                OutlinedTextField(
                  value = address,
                  onValueChange = { address = it },
                  label = { Text("Alamat Titik Kumpul Lengkap") },
                  placeholder = { Text("Misal: Starbucks Ground Floor Mall Malioboro, DIY") },
                  modifier = Modifier.fillMaxWidth(),
                  singleLine = false,
                  maxLines = 2,
                  leadingIcon = { Icon(Icons.Outlined.PinDrop, contentDescription = null, tint = DiajakOrange) },
                  colors = OutlinedTextFieldDefaults.colors(
                    focusedTextColor = MaterialTheme.colorScheme.onSurface,
                    unfocusedTextColor = MaterialTheme.colorScheme.onSurface,
                    focusedLabelColor = DiajakOrange,
                    unfocusedLabelColor = MaterialTheme.colorScheme.onSurface,
                    focusedBorderColor = DiajakOrange,
                    unfocusedBorderColor = Color.Transparent
                  )
                )

                // Recommendation chips
                Text("Rekomendasi Titik Kumpul:", style = com.example.ui.theme.DiajakDesignSystem.Typography.TitleBold, color = Color(0xFF262626))
                LazyRow(
                  horizontalArrangement = Arrangement.spacedBy(MaterialTheme.spacing.small),
                  modifier = Modifier.fillMaxWidth()
                ) {
                  items(presetLocations) { loc ->
                    val isSelected = (pinX == loc.x && pinY == loc.y)
                    Card(
                      modifier = Modifier.clickable {
                        pinX = loc.x
                        pinY = loc.y
                        locationName = loc.cityName
                        address = "${loc.name}, ${loc.address}"
                      },
                      colors = CardDefaults.cardColors(
                        containerColor = if (isSelected) DiajakOrange.copy(alpha = 0.1f) else Color(0xFFF8FAFC)
                      ),
                      border = BorderStroke(1.dp, if (isSelected) DiajakOrange else Color.Transparent),
                       
                shape = RoundedCornerShape(16.dp)
                    ) {
                      Row(
                        modifier = Modifier.padding(horizontal = 20.dp, vertical = 6.dp),
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.spacedBy(MaterialTheme.spacing.extraSmall)
                      ) {
                        Text(loc.icon, style = com.example.ui.theme.DiajakDesignSystem.Typography.TitleBold)
                        Text(loc.name.take(16) + if (loc.name.length > 16) ".." else "", style = com.example.ui.theme.DiajakDesignSystem.Typography.TitleBold)
                      }
                    }
                  }
                }
              }
            }

            // Bottom Confirm / Cancel Buttons floating row
            Row(
              modifier = Modifier
                .align(Alignment.BottomCenter)
                .fillMaxWidth()
                .padding(20.dp),
              horizontalArrangement = Arrangement.spacedBy(20.dp)
            ) {
              Button(
                onClick = { isFullScreenMapMode = false },
                modifier = Modifier
                  .weight(1f)
                  .height(52.dp),
                colors = ButtonDefaults.buttonColors(containerColor = Color.Transparent),
                border = BorderStroke(1.5.dp, DiajakOrange),
                 
                shape = RoundedCornerShape(16.dp)
              ) {
                Text("Batal", color = DiajakOrange, style = com.example.ui.theme.DiajakDesignSystem.Typography.TitleBold)
              }

              Button(
                onClick = {
                  if (locationName.isBlank() || address.isBlank()) {
                    Toast.makeText(context, "Mohon isi Nama Kota dan Alamat terlebih dahulu", Toast.LENGTH_SHORT).show()
                  } else {
                    isFullScreenMapMode = false
                    Toast.makeText(context, "Titik kumpul berhasil disimpan! 📍", Toast.LENGTH_SHORT).show()
                  }
                },
                modifier = Modifier
                  .weight(1.5f)
                  .height(52.dp),
                colors = ButtonDefaults.buttonColors(containerColor = DiajakOrange),
                 
                shape = RoundedCornerShape(16.dp)
              ) {
                Icon(Icons.Outlined.Check, contentDescription = null, tint = Color.White, modifier = Modifier.size(20.dp))
                Spacer(modifier = Modifier.width(MaterialTheme.spacing.small))
                Text("Pilih Tujuan Ini", color = Color.White, style = com.example.ui.theme.DiajakDesignSystem.Typography.TitleBold)
              }
            }
          }

          // Top iOS Glass Header for Fullscreen Map
          DiajakGlassHeader(
            hazeState = dialogHazeState,
            modifier = Modifier
              .align(Alignment.TopCenter)
              .zIndex(10f)
          ) {
            Box(
              modifier = Modifier
                .fillMaxWidth()
                .height(56.dp)
                .padding(horizontal = 16.dp)
            ) {
              IconButton(
                onClick = { isFullScreenMapMode = false },
                modifier = Modifier
                  .size(40.dp)
                  .align(Alignment.CenterStart)
                  .diajakGlassButton(dialogHazeState)
              ) {
                Icon(
                  imageVector = Icons.AutoMirrored.Outlined.ArrowBack,
                  contentDescription = "Kembali",
                  tint = MaterialTheme.colorScheme.onSurface
                )
              }

              Text(
                text = "Tentukan Lokasi Peta",
                style = com.example.ui.theme.DiajakDesignSystem.Typography.Headline,
                color = MaterialTheme.colorScheme.onSurface,
                textAlign = TextAlign.Center,
                modifier = Modifier
                  .align(Alignment.Center)
                  .fillMaxWidth()
              )
            }
          }
        }
      } else {
        Box(
          modifier = Modifier
            .fillMaxSize()
            .imePadding()
            .pointerInput(Unit) {
              detectTapGestures(
                onTap = {
                  focusManager.clearFocus()
                }
              )
            }
        ) {
        Column(
          modifier = Modifier
            .fillMaxSize()
            .hazeSource(state = dialogHazeState)
            .padding(top = 64.dp)
            .padding(horizontal = 20.dp)
            .padding(bottom = 16.dp)
        ) {
          // Progress Step Indicator
          Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.spacedBy(MaterialTheme.spacing.small)
          ) {
            WizardProgressStep(stepNumber = 1, title = "Detail & Benefit", isActive = currentStep >= 1, isCurrent = currentStep == 1, modifier = Modifier.weight(1f))
            WizardProgressStep(stepNumber = 2, title = "Jadwal & Biaya", isActive = currentStep >= 2, isCurrent = currentStep == 2, modifier = Modifier.weight(1f))
            WizardProgressStep(stepNumber = 3, title = "Lokasi Peta", isActive = currentStep >= 3, isCurrent = currentStep == 3, modifier = Modifier.weight(1f))
          }

          Spacer(modifier = Modifier.height(16.dp))

          // --- STEP CONTENT (SCROLLABLE CONTAINER) ---
          Box(modifier = Modifier.weight(1f)) {
            when (currentStep) {
              1 -> {
                // --- STEP 1: Detail & Benefit ---
                Column(
                  modifier = Modifier
                    .fillMaxSize()
                    .verticalScroll(rememberScrollState()),
                  verticalArrangement = Arrangement.spacedBy(MaterialTheme.spacing.medium)
          ) {
                  OutlinedTextField(
                    value = title,
                    onValueChange = { title = it },
                    label = { Text("Judul Aktivitas (Misal: Trail Run Sentul)") },
                    placeholder = { Text("Masukkan nama aktivitas yang menarik...") },
                    modifier = Modifier.fillMaxWidth(),
                    singleLine = true,
                    colors = OutlinedTextFieldDefaults.colors(
                      focusedTextColor = MaterialTheme.colorScheme.onSurface,
                      unfocusedTextColor = MaterialTheme.colorScheme.onSurface,
                      focusedLabelColor = DiajakOrange,
                      unfocusedLabelColor = MaterialTheme.colorScheme.onSurface,
                      focusedBorderColor = DiajakOrange,
                      unfocusedBorderColor = Color.Transparent,
                      cursorColor = DiajakOrange
                    )
                  )

                  Text("Tentukan Kategori:", style = com.example.ui.theme.DiajakDesignSystem.Typography.TitleBold, color = MaterialTheme.colorScheme.onSurface)
                  var catExpanded by remember { mutableStateOf(false) }
                  val categoryIcon = getCategoryIconVector(selectedCategory)
                  ExposedDropdownMenuBox(
                    expanded = catExpanded,
                    onExpandedChange = { catExpanded = !catExpanded }
                  ) {
                    OutlinedTextField(
                      value = selectedCategory,
                      onValueChange = {},
                      readOnly = true,
                      leadingIcon = {
                        Icon(
                          imageVector = categoryIcon,
                          contentDescription = null,
                          tint = MaterialTheme.colorScheme.onSurface,
                          modifier = Modifier.size(20.dp)
                        )
                      },
                      trailingIcon = { ExposedDropdownMenuDefaults.TrailingIcon(expanded = catExpanded) },
                      modifier = Modifier
                        .menuAnchor(MenuAnchorType.PrimaryNotEditable)
                        .fillMaxWidth(),
                      colors = OutlinedTextFieldDefaults.colors(
                        focusedTextColor = MaterialTheme.colorScheme.onSurface,
                        unfocusedTextColor = MaterialTheme.colorScheme.onSurface,
                        focusedLabelColor = DiajakOrange,
                        unfocusedLabelColor = MaterialTheme.colorScheme.onSurface,
                        focusedBorderColor = DiajakOrange,
                        unfocusedBorderColor = Color.Transparent
                      )
                    )
                    ExposedDropdownMenu(
                      expanded = catExpanded,
                      onDismissRequest = { catExpanded = false }
                    ) {
                      categories.forEach { cat ->
                        val itemIcon = getCategoryIconVector(cat)
                        DropdownMenuItem(
                          text = { Text(cat, style = com.example.ui.theme.DiajakDesignSystem.Typography.Body) },
                          leadingIcon = {
                            Icon(
                              imageVector = itemIcon,
                              contentDescription = null,
                              tint = MaterialTheme.colorScheme.onSurface,
                              modifier = Modifier.size(20.dp)
                            )
                          },
                          onClick = {
                            selectedCategory = cat
                            catExpanded = false
                          }
                        )
                      }
                    }
                  }

                  Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(20.dp),
                    verticalAlignment = Alignment.CenterVertically
                  ) {
                    OutlinedTextField(
                      value = quotaString,
                      onValueChange = { quotaString = it.filter { ch -> ch.isDigit() } },
                      label = { Text("Kuota Peserta (Slot)") },
                      placeholder = { Text("Misal: 15") },
                      keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                      modifier = Modifier.weight(1f),
                      colors = OutlinedTextFieldDefaults.colors(
                        focusedTextColor = MaterialTheme.colorScheme.onSurface,
                        unfocusedTextColor = MaterialTheme.colorScheme.onSurface,
                        focusedLabelColor = DiajakOrange,
                        unfocusedLabelColor = MaterialTheme.colorScheme.onSurface,
                        focusedBorderColor = DiajakOrange,
                        unfocusedBorderColor = Color.Transparent
                      )
                    )
                    Card(
                      modifier = Modifier.weight(1f),
                       
                shape = RoundedCornerShape(16.dp),
                      border = BorderStroke(0.dp, Color.Transparent),
                      colors = CardDefaults.cardColors(containerColor = Color(0xFFFFF2EC)) ){
                      Row(
                        modifier = Modifier.padding(20.dp),
                        verticalAlignment = Alignment.CenterVertically
                      ) {
                        Text(
                          "💡",
                          style = com.example.ui.theme.DiajakDesignSystem.Typography.Body,
                          modifier = Modifier.padding(end = MaterialTheme.spacing.small)
                        )
                        Text(
                          "Kuota ideal untuk komunitas berkisar 10-20 peserta.",
                          style = com.example.ui.theme.DiajakDesignSystem.Typography.TitleBold,
                          color = Color(0xFF7C2D12),
                          lineHeight = 22.sp
                        )
                      }
                    }
                  }

                  OutlinedTextField(
                    value = overview,
                    onValueChange = { overview = it },
                    label = { Text("Deskripsi / Ringkasan Aktivitas") },
                    placeholder = { Text("Tuliskan jadwal aktivitas, titik temu, barang bawaan wajib, dan detail keseruan lainnya...") },
                    modifier = Modifier
                      .fillMaxWidth()
                      .height(110.dp),
                    colors = OutlinedTextFieldDefaults.colors(
                      focusedTextColor = MaterialTheme.colorScheme.onSurface,
                      unfocusedTextColor = MaterialTheme.colorScheme.onSurface,
                      focusedLabelColor = DiajakOrange,
                      unfocusedLabelColor = MaterialTheme.colorScheme.onSurface,
                      focusedBorderColor = DiajakOrange,
                      unfocusedBorderColor = Color.Transparent,
                      cursorColor = DiajakOrange
                    )
                  )

                  Text("Tentukan Benefit yang Disediakan:", style = com.example.ui.theme.DiajakDesignSystem.Typography.TitleBold,
                    color = MaterialTheme.colorScheme.onSurface
                  )

                  // Grid of standard benefits
                  DiajakFlowRow(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(MaterialTheme.spacing.small),
                    verticalArrangement = Arrangement.spacedBy(MaterialTheme.spacing.small)
                  ) {
                    standardBenefits.forEach { benefit ->
                      val isChecked = selectedBenefits.contains(benefit)

                      Card(
                        onClick = {
                          focusManager.clearFocus()
                          if (isChecked) selectedBenefits.remove(benefit)
                          else selectedBenefits.add(benefit)
                        },
                         
                shape = RoundedCornerShape(16.dp),
                        colors = CardDefaults.cardColors(containerColor = Color.Transparent),
                        border = BorderStroke(if (isChecked) 2.dp else 0.dp, if (isChecked) DiajakOrange else Color.Transparent),
                        elevation = CardDefaults.cardElevation(defaultElevation = 0.dp)
                      ) {
                        Row(
                          modifier = Modifier.padding(horizontal = MaterialTheme.spacing.screenMargin, vertical = MaterialTheme.spacing.small),
                          verticalAlignment = Alignment.CenterVertically
                        ) {
                          if (isChecked) {
                            Icon(
                              imageVector = Icons.Outlined.Check,
                              contentDescription = null,
                              tint = DiajakOrange,
                              modifier = Modifier.size(16.dp)
                            )
                            Spacer(modifier = Modifier.width(6.dp))
                          }

                          Text(
                            text = benefit,
                            fontWeight = if (isChecked) FontWeight.Bold else FontWeight.Medium,
                            style = com.example.ui.theme.DiajakDesignSystem.Typography.Body,
                            color = if (isChecked) DiajakOrange else MaterialTheme.colorScheme.onSurface
                          )
                        }
                      }
                    }

                    // Render custom benefits at the same top flow list
                    selectedBenefits.filter { !standardBenefits.contains(it) }.forEach { customBen ->
                      Card(
                        onClick = {
                          focusManager.clearFocus()
                          selectedBenefits.remove(customBen)
                        },
                         
                shape = RoundedCornerShape(16.dp),
                        colors = CardDefaults.cardColors(containerColor = Color.Transparent),
                        border = BorderStroke(2.dp, DiajakOrange),
                        elevation = CardDefaults.cardElevation(defaultElevation = 0.dp)
                      ) {
                        Row(
                          modifier = Modifier.padding(horizontal = MaterialTheme.spacing.screenMargin, vertical = MaterialTheme.spacing.small),
                          verticalAlignment = Alignment.CenterVertically
                        ) {
                          Icon(
                            imageVector = Icons.Outlined.Check,
                            contentDescription = null,
                            tint = DiajakOrange,
                            modifier = Modifier.size(16.dp)
                          )
                          Spacer(modifier = Modifier.width(6.dp))

                          Text(
                            text = customBen,
                            fontWeight = FontWeight.Bold,
                            style = com.example.ui.theme.DiajakDesignSystem.Typography.Body,
                            color = DiajakOrange
                          )
                          Spacer(modifier = Modifier.width(MaterialTheme.spacing.small))
                          Icon(
                            imageVector = Icons.Outlined.Close,
                            contentDescription = "Hapus",
                            tint = DiajakOrange,
                            modifier = Modifier.size(16.dp)
                          )
                        }
                      }
                    }
                  }

                  // Custom benefit input
                  Row(
                    modifier = Modifier.fillMaxWidth(),
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.spacedBy(MaterialTheme.spacing.small) ){
                    OutlinedTextField(
                      value = customBenefitInput,
                      onValueChange = { customBenefitInput = it },
                      label = { Text("Tambah Benefit Lainnya") },
                      placeholder = { Text("Misal: Voucher Belanja Rp 50rb") },
                      modifier = Modifier.weight(1f),
                      singleLine = true,
                      colors = OutlinedTextFieldDefaults.colors(
                        focusedTextColor = MaterialTheme.colorScheme.onSurface,
                        unfocusedTextColor = MaterialTheme.colorScheme.onSurface,
                        focusedLabelColor = DiajakOrange,
                        unfocusedLabelColor = MaterialTheme.colorScheme.onSurface,
                        focusedBorderColor = DiajakOrange,
                        unfocusedBorderColor = Color.Transparent
                      )
                    )
                    Button(
                      onClick = {
                        if (customBenefitInput.isNotBlank()) {
                          selectedBenefits.add(customBenefitInput.trim())
                          customBenefitInput = ""
                        }
                      },
                      colors = ButtonDefaults.buttonColors(containerColor = DiajakOrange),
                      modifier = Modifier.height(52.dp),
                       
                shape = RoundedCornerShape(16.dp) ){
                      Text("Tambah", color = Color.White, style = androidx.compose.ui.text.TextStyle(fontSize = 16.sp, fontWeight = androidx.compose.ui.text.font.FontWeight.Bold, fontFamily = com.example.ui.theme.AppFontFamily))
                    }
                  }
                  
                  Spacer(modifier = Modifier.height(MaterialTheme.spacing.small))
                }
              }

              2 -> {
                // --- STEP 2: Jadwal & Biaya ---
                Column(
                  modifier = Modifier
                    .fillMaxSize()
                    .verticalScroll(rememberScrollState()),
                  verticalArrangement = Arrangement.spacedBy(MaterialTheme.spacing.medium)
          ) {
                  // Price setup with locked toggle
                  Surface(
                    modifier = Modifier.fillMaxWidth(),
                    border = BorderStroke(0.dp, Color.Transparent),
                    color = Color.White,
                    shadowElevation = 0.dp,
                    shape = RoundedCornerShape(20.dp)
                  ) {
                    Column(modifier = Modifier.padding(20.dp), verticalArrangement = Arrangement.spacedBy(MaterialTheme.spacing.medium)
) {
                      Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                      ) {
                        Column {
                          Text("Aktivitas Komunitas Gratis / Free", style = com.example.ui.theme.DiajakDesignSystem.Typography.Body, color = MaterialTheme.colorScheme.onSurface)
                          Text("Aktivitas tidak dikenakan biaya undangan masuk", style = com.example.ui.theme.DiajakDesignSystem.Typography.Body, color = MaterialTheme.colorScheme.onSurface)
                        }
                        Switch(
                          checked = isFree,
                          onCheckedChange = { isFree = it },
                          colors = SwitchDefaults.colors(
                            checkedThumbColor = Color.White,
                            checkedTrackColor = Color(0xFF00B368)
                          )
                        )
                      }

                      Spacer(modifier = Modifier.height(MaterialTheme.spacing.extraSmall))

                      val displayPrice = if (priceString.isNotEmpty()) {
                        String.format("%,d", priceString.toLongOrNull() ?: 0L).replace(',', '.')
                      } else ""
                      
                      OutlinedTextField(
                        value = if (isFree) "0" else displayPrice,
                        onValueChange = { if (!isFree) priceString = it.filter { ch -> ch.isDigit() } },
                        label = { Text("Biaya Undangan Masuk (Rp)") },
                        placeholder = { Text("Misal: 150.000") },
                        enabled = !isFree,
                        keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                        modifier = Modifier.fillMaxWidth(),
                        colors = OutlinedTextFieldDefaults.colors(
                          focusedTextColor = if (isFree) MaterialTheme.colorScheme.onSurface else MaterialTheme.colorScheme.onSurface,
                          unfocusedTextColor = if (isFree) MaterialTheme.colorScheme.onSurface else MaterialTheme.colorScheme.onSurface,
                          disabledTextColor = MaterialTheme.colorScheme.onSurface,
                          focusedLabelColor = DiajakOrange,
                          unfocusedLabelColor = MaterialTheme.colorScheme.onSurface,
                          focusedBorderColor = DiajakOrange,
                          unfocusedBorderColor = Color.Transparent,
                          disabledContainerColor = MaterialTheme.colorScheme.outlineVariant
                        )
                      )
                    }
                  }

                  // --- Recurring & Ongoing Activity Settings ---
                  Surface(
                    modifier = Modifier.fillMaxWidth(),
                    border = BorderStroke(0.dp, Color.Transparent),
                    color = Color.White,
                    shadowElevation = 0.dp,
                    shape = RoundedCornerShape(20.dp)
                  ) {
                    Column(
                      modifier = Modifier.padding(20.dp),
                      verticalArrangement = Arrangement.spacedBy(MaterialTheme.spacing.medium)
) {
                      Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                      ) {
                        Column(modifier = Modifier.weight(1f)) {
                          Text(
                            text = "Aktivitas Rutin / Berulang",
                            style = com.example.ui.theme.DiajakDesignSystem.Typography.Body,
                            color = MaterialTheme.colorScheme.onSurface
                          )
                          Text(
                            text = "Aktivitas akan konsisten terjadwal terulang (seperti Shopee)",
                            style = com.example.ui.theme.DiajakDesignSystem.Typography.Body, color = MaterialTheme.colorScheme.onSurface
                          )
                        }
                        Switch(
                          checked = isRecurringAct,
                          onCheckedChange = { isRecurringAct = it },
                          colors = SwitchDefaults.colors(
                            checkedThumbColor = Color.White,
                            checkedTrackColor = DiajakOrange
                          )
                        )
                      }

                      if (isRecurringAct) {
                        Text(
                          text = "Pilih Hari Rutin Terlaksana:",
                          style = com.example.ui.theme.DiajakDesignSystem.Typography.Body,
                          color = MaterialTheme.colorScheme.onSurface
                        )

                        Row(
                          modifier = Modifier.fillMaxWidth(),
                          horizontalArrangement = Arrangement.spacedBy(MaterialTheme.spacing.small) ){
                          val weekdays = listOf("Sen", "Sel", "Rab", "Kam", "Jum", "Sab", "Min")
                          weekdays.forEach { day ->
                            val isSelected = selectedDays.contains(day)
                            Box(
                              modifier = Modifier
                                .size(width = 44.dp, height = 36.dp)
                                .clip(RoundedCornerShape(8.dp))
                                .background(if (isSelected) DiajakOrange else MaterialTheme.colorScheme.outlineVariant)
                                .clickable {
                                  focusManager.clearFocus()
                                  if (isSelected) selectedDays.remove(day)
                                  else selectedDays.add(day)
                                },
                              contentAlignment = Alignment.Center
                            ) {
                              Text(
                                text = day,
                                style = com.example.ui.theme.DiajakDesignSystem.Typography.Body,
                                color = if (isSelected) Color.White else MaterialTheme.colorScheme.onSurface
                              )
                            }
                          }
                        }
                      }

                      Spacer(modifier = Modifier.height(20.dp))

                      Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                      ) {
                        Column(modifier = Modifier.weight(1f)) {
                          Text(
                            text = "Tandai Sedang Berlangsung (Ongoing)",
                            style = com.example.ui.theme.DiajakDesignSystem.Typography.Body,
                            color = MaterialTheme.colorScheme.onSurface
                          )
                          Text(
                            text = "Matikan pendaftaran selama aktivitas berlangsung",
                            style = com.example.ui.theme.DiajakDesignSystem.Typography.Body, color = MaterialTheme.colorScheme.onSurface
                          )
                        }
                        Switch(
                          checked = isOngoingAct,
                          onCheckedChange = { isOngoingAct = it },
                          colors = SwitchDefaults.colors(
                            checkedThumbColor = Color.White,
                            checkedTrackColor = Color.Red
                          )
                        )
                      }
                    }
                  }

                  // Multi-date checklist calendar instructions
                  Text("Jadwal Terbit Masa Depan:", style = com.example.ui.theme.DiajakDesignSystem.Typography.TitleBold,
                    color = MaterialTheme.colorScheme.onSurface
                  )
                  Text("Pilih tanggal-tanggal terbit di kalender Juli 2026 agar aktivitas ini terbit berulang sesuai jadwal ke depan (tidak hanya satu waktu).", style = com.example.ui.theme.DiajakDesignSystem.Typography.TitleBold, color = MaterialTheme.colorScheme.onSurface,
                    lineHeight = 22.sp
                  )

                  // Calendar Grid Checklist UI (July 2026)
                  Card(
                    modifier = Modifier.fillMaxWidth(),
                     
                shape = RoundedCornerShape(16.dp),
                    border = BorderStroke(0.dp, Color.Transparent),
                    colors = CardDefaults.cardColors(containerColor = Color.White) ){
                    Column(
                      modifier = Modifier.padding(20.dp) ){
                      // Month Title
                      Text("Juli 2026", style = com.example.ui.theme.DiajakDesignSystem.Typography.TitleBold,
                        color = MaterialTheme.colorScheme.onSurface,
                        modifier = Modifier.fillMaxWidth().padding(bottom = MaterialTheme.spacing.medium),
                        textAlign = TextAlign.Center
                      )

                      // Weekday Headers
                      Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween
                      ) {
                        val weekdays = listOf("Min", "Sen", "Sel", "Rab", "Kam", "Jum", "Sab")
                        weekdays.forEach { day ->
                          Text(
                            text = day,
                            modifier = Modifier.width(36.dp),
                            textAlign = TextAlign.Center,
                            style = com.example.ui.theme.DiajakDesignSystem.Typography.Body,
                            color = if (day == "Min") Color.Red else MaterialTheme.colorScheme.onSurface
                          )
                        }
                      }

                      Spacer(modifier = Modifier.height(MaterialTheme.spacing.small))

                      // Days grid
                      var currentDayCount = 1
                      val rows = 6
                      Column(
                        verticalArrangement = Arrangement.spacedBy(MaterialTheme.spacing.small)
                ) {
                        for (r in 0 until rows) {
                          Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.SpaceBetween
                          ) {
                            for (c in 0..6) {
                              val index = r * 7 + c
                              if (index < startingDayOffset || currentDayCount > totalDaysInJuly) {
                                Spacer(modifier = Modifier.width(36.dp))
                              } else {
                                val dayVal = currentDayCount
                                val isSelected = selectedDates.contains(dayVal)
                                Box(
                                  modifier = Modifier
                                    .size(36.dp)
                                    .clip(CircleShape)
                                    .background(
                                      when {
                                        isSelected -> DiajakOrange
                                        else -> Color.Transparent
                                      }
                                    )
                                    .border(
                                      1.dp,
                                      if (isSelected) DiajakOrange else MaterialTheme.colorScheme.outlineVariant,
                                      CircleShape
                                    )
                                    .clickable {
                                      focusManager.clearFocus()
                                      if (isSelected) selectedDates.remove(dayVal)
                                      else selectedDates.add(dayVal)
                                    },
                                  contentAlignment = Alignment.Center
                                ) {
                                  Text(
                                    text = dayVal.toString(),
                                    style = com.example.ui.theme.DiajakDesignSystem.Typography.Body,
                                    fontWeight = if (isSelected) FontWeight.Bold else FontWeight.Medium,
                                    color = when {
                                      isSelected -> Color.White
                                      c == 0 -> Color.Red
                                      else -> MaterialTheme.colorScheme.onSurface
                                    }
                                  )
                                }
                                currentDayCount++
                              }
                            }
                          }
                          if (currentDayCount > totalDaysInJuly) break
                        }
                      }
                    }
                  }

                  // Selected Dates list summary
                  if (selectedDates.isNotEmpty()) {
                    val sortedDates = selectedDates.sorted()
                    Text(
                      text = "Jadwal Terpilih (${selectedDates.size} Sesi):",
                      style = com.example.ui.theme.DiajakDesignSystem.Typography.TitleBold,
                      color = MaterialTheme.colorScheme.onSurface
                    )
                    Card(
                      modifier = Modifier.fillMaxWidth(),
                       
                shape = RoundedCornerShape(16.dp),
                      border = BorderStroke(0.dp, Color.Transparent),
                      colors = CardDefaults.cardColors(containerColor = Color(0xFFF0FDF4)) ){
                      Text(
                        text = sortedDates.joinToString(", ") { "$it Jul 2026" } + " • Mulai pukul 16:00",
                        style = com.example.ui.theme.DiajakDesignSystem.Typography.Body,
                        color = Color(0xFF15803D),
                        modifier = Modifier.padding(20.dp),
                        fontWeight = FontWeight.Medium
                      )
                    }
                  } else {
                    Card(
                      modifier = Modifier.fillMaxWidth(),
                       
                shape = RoundedCornerShape(16.dp),
                      border = BorderStroke(0.dp, Color.Transparent),
                      colors = CardDefaults.cardColors(containerColor = Color(0xFFFFFBEB)) ){
                      Text(
                        "Peringatan: Belum ada tanggal yang dipilih. Sistem akan menggunakan default tanggal 12 Sep 2026.",
                        style = com.example.ui.theme.DiajakDesignSystem.Typography.TitleBold,
                        color = Color(0xFFB45309),
                        modifier = Modifier.padding(20.dp)
                      )
                    }
                  }
                  
                  Spacer(modifier = Modifier.height(MaterialTheme.spacing.small))
                }
              }

              3 -> {
                // --- STEP 3: Lokasi & Pin Peta (Optimized design matching video & screenshots) ---
                Column(
                  modifier = Modifier
                    .fillMaxSize()
                    .verticalScroll(rememberScrollState()),
                  verticalArrangement = Arrangement.spacedBy(MaterialTheme.spacing.medium)
                ) {
                  // Deteksi Lokasi GPS Section
                  Column(
                    modifier = Modifier
                      .fillMaxWidth()
                      .background(Color.White, RoundedCornerShape(16.dp))
                      .border(0.dp, Color.Transparent, RoundedCornerShape(16.dp))
                      .padding(20.dp)
                  ) {
                    Row(
                      modifier = Modifier.fillMaxWidth(),
                      horizontalArrangement = Arrangement.SpaceBetween,
                      verticalAlignment = Alignment.CenterVertically
                    ) {
                      Column(modifier = Modifier.weight(1f)) {
                        Text(
                          text = "Deteksi Lokasi GPS",
                          style = com.example.ui.theme.DiajakDesignSystem.Typography.Body,
                          color = MaterialTheme.colorScheme.onSurface
                        )
                        Spacer(modifier = Modifier.height(2.dp))
                        Text(
                          text = "Temukan lokasi titik temu terdekat Anda saat ini",
                          style = com.example.ui.theme.DiajakDesignSystem.Typography.TitleBold,
                          color = MaterialTheme.colorScheme.onSurface
                        )
                      }
                      
                      TextButton(
                        onClick = {
                          coroutineScope.launch {
                            isSimulatingNearMe = true
                            Toast.makeText(context, "Mendeteksi sinyal GPS terdekat...", Toast.LENGTH_SHORT).show()
                            delay(1000)
                            // Simulate setting closest preset: Nox Coffee Boutique Sleman
                            val preset = presetLocations[5]
                            pinX = preset.x
                            pinY = preset.y
                            locationName = preset.cityName
                            address = "${preset.name}, ${preset.address}"
                            isSimulatingNearMe = false
                            Toast.makeText(context, "GPS Terpusat: Sleman, Nox Coffee Boutique 📍", Toast.LENGTH_LONG).show()
                          }
                        },
                        colors = ButtonDefaults.textButtonColors(contentColor = DiajakOrange)
                      ) {
                        if (isSimulatingNearMe) {
                          CircularProgressIndicator(color = DiajakOrange, modifier = Modifier.size(20.dp), strokeWidth = 2.dp)
                        } else {
                          Icon(Icons.Outlined.MyLocation, contentDescription = null, modifier = Modifier.size(20.dp))
                          Spacer(modifier = Modifier.width(MaterialTheme.spacing.extraSmall))
                          Text("Gunakan GPS", style = com.example.ui.theme.DiajakDesignSystem.Typography.TitleBold)
                        }
                      }
                    }
                  }

                  // Pin Titik Temu di Peta Section
                  Column(verticalArrangement = Arrangement.spacedBy(MaterialTheme.spacing.small)) {
                    Text(
                      text = "Pin Titik Temu di Peta",
                      style = com.example.ui.theme.DiajakDesignSystem.Typography.Body,
                      color = MaterialTheme.colorScheme.onSurface
                    )
                    Text(
                      text = "Ketuk peta untuk mengepin koordinat titik kumpul secara akurat.",
                      style = com.example.ui.theme.DiajakDesignSystem.Typography.TitleBold,
                      color = MaterialTheme.colorScheme.onSurface
                    )
                  }

                  // Large Styled Map preview with coordinate pin & overlay action bar
                  Box(
                    modifier = Modifier
                      .fillMaxWidth()
                      .height(150.dp)
                      .clip(RoundedCornerShape(16.dp))
                      .border(1.dp, Color.Transparent, RoundedCornerShape(16.dp))
                      .background(Color(0xFFE2F0D9))
                      .clickable {
                        focusManager.clearFocus()
                        isFullScreenMapMode = true
                      }
                  ) {
                    // Stylized canvas map background inside preview
                    Canvas(modifier = Modifier.fillMaxSize()) {
                      val w = size.width
                      val h = size.height
                      
                      // Land area color
                      drawRect(color = com.example.ui.theme.BackgroundLight)
                      
                      // Parks
                      drawCircle(Color(0xFFDCFCE7), radius = w * 0.3f, center = Offset(w * 0.1f, h * 0.2f))
                      drawCircle(Color(0xFFDCFCE7), radius = w * 0.2f, center = Offset(w * 0.85f, h * 0.8f))
                      
                      // Water Reservoir
                      val lakePath = Path().apply {
                        moveTo(w * 0.7f, h)
                        quadraticTo(w * 0.75f, h * 0.6f, w, h * 0.5f)
                        lineTo(w, h)
                        close()
                      }
                      drawPath(lakePath, Color(0xFFE0F2FE))

                      // Grid roads lines
                      val roadColor = Color.White
                      drawLine(roadColor, Offset(0f, h * 0.4f), Offset(w, h * 0.45f), strokeWidth = 14f)
                      drawLine(roadColor, Offset(0f, h * 0.75f), Offset(w, h * 0.7f), strokeWidth = 12f)
                      drawLine(roadColor, Offset(w * 0.45f, 0f), Offset(w * 0.52f, h), strokeWidth = 16f)
                    }

                    // Render central pinned coordinate marker using CustomMapPinMarker
                    Box(
                      modifier = Modifier
                        .layout { measurable, constraints ->
                          val placeable = measurable.measure(constraints)
                          val px = constraints.maxWidth * pinX
                          val py = constraints.maxHeight * pinY
                          layout(placeable.width, placeable.height) {
                            placeable.placeRelative(
                              x = (px - placeable.width / 2f).toInt(),
                              y = (py - placeable.height).toInt()
                            )
                          }
                        }
                    ) {
                      CustomMapPinMarker(
                        categoryIcon = getCategoryIcon(selectedCategory),
                        isSelected = true,
                        title = locationName.ifEmpty { "Lokasi Aktivitas" }
                      )
                    }

                    // Black semi-transparent bar overlay at the bottom matching the requested layout perfectly
                    Row(
                      modifier = Modifier
                        .fillMaxWidth()
                        .align(Alignment.BottomCenter)
                        .background(Color.Black.copy(alpha = 0.65f))
                        .padding(horizontal = 20.dp, vertical = MaterialTheme.spacing.small),
                      horizontalArrangement = Arrangement.SpaceBetween,
                      verticalAlignment = Alignment.CenterVertically
                    ) {
                      Row(verticalAlignment = Alignment.CenterVertically, horizontalArrangement = Arrangement.spacedBy(MaterialTheme.spacing.extraSmall)) {
                        Icon(
                          imageVector = Icons.Outlined.PinDrop,
                          contentDescription = null,
                          tint = Color.White,
                          modifier = Modifier.size(20.dp)
                        )
                        Text(
                          text = "📌 Ketuk untuk Atur Pin Peta",
                          color = Color.White,
                          style = com.example.ui.theme.DiajakDesignSystem.Typography.TitleBold,
                          
                        )
                      }
                      Text(
                        text = "Ubah Pin Peta >",
                        color = DiajakOrangeLight,
                        style = com.example.ui.theme.DiajakDesignSystem.Typography.TitleBold,
                        
                      )
                    }
                  }

                  // Kota / Wilayah Field
                  OutlinedTextField(
                    value = locationName,
                    onValueChange = { locationName = it },
                    label = { Text("Nama Kota / Wilayah") },
                    placeholder = { Text("Misal: Yogyakarta, Sleman, Bantul") },
                    modifier = Modifier.fillMaxWidth(),
                    singleLine = true,
                    colors = OutlinedTextFieldDefaults.colors(
                      focusedTextColor = MaterialTheme.colorScheme.onSurface,
                      unfocusedTextColor = MaterialTheme.colorScheme.onSurface,
                      focusedLabelColor = DiajakOrange,
                      unfocusedLabelColor = MaterialTheme.colorScheme.onSurface,
                      focusedBorderColor = DiajakOrange,
                      unfocusedBorderColor = Color.Transparent
                    )
                  )

                  // Alamat Titik Kumpul Lengkap Field
                  OutlinedTextField(
                    value = address,
                    onValueChange = { address = it },
                    label = { Text("Alamat Titik Kumpul Lengkap") },
                    placeholder = { Text("Misal: Starbucks Ground Floor Mall Malioboro, DIY") },
                    modifier = Modifier
                      .fillMaxWidth()
                      .height(96.dp),
                    colors = OutlinedTextFieldDefaults.colors(
                      focusedTextColor = MaterialTheme.colorScheme.onSurface,
                      unfocusedTextColor = MaterialTheme.colorScheme.onSurface,
                      focusedLabelColor = DiajakOrange,
                      unfocusedLabelColor = MaterialTheme.colorScheme.onSurface,
                      focusedBorderColor = DiajakOrange,
                      unfocusedBorderColor = Color.Transparent
                    )
                  )
                  
                  Spacer(modifier = Modifier.height(MaterialTheme.spacing.extraSmall))
                }
              }
            }
          }

          Spacer(modifier = Modifier.height(MaterialTheme.spacing.medium))

          // --- STEP NAVIGATION FOOTER BUTTONS ---
          Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
          ) {
            if (currentStep > 1) {
              OutlinedButton(
                onClick = { currentStep-- },
                 
                shape = RoundedCornerShape(16.dp),
                modifier = Modifier.height(52.dp),
                border = BorderStroke(1.5.dp, DiajakOrange),
                colors = ButtonDefaults.outlinedButtonColors(containerColor = Color.Transparent, contentColor = DiajakOrange) ){
                Icon(Icons.AutoMirrored.Outlined.ArrowBack, contentDescription = "Back", modifier = Modifier.size(20.dp), tint = DiajakOrange)
                Spacer(modifier = Modifier.width(MaterialTheme.spacing.small))
                Text("Sebelumnya", fontWeight = FontWeight.Medium, color = DiajakOrange)
              }
            } else {
              Spacer(modifier = Modifier.width(1.dp))
            }

            if (currentStep < 3) {
              Button(
                onClick = {
                  // Validations per step
                  if (currentStep == 1 && title.isBlank()) {
                    Toast.makeText(context, "Mohon tentukan Judul Aktivitas terlebih dahulu", Toast.LENGTH_SHORT).show()
                    return@Button
                  }
                  currentStep++
                },
                 
                shape = RoundedCornerShape(16.dp),
                modifier = Modifier
                  .weight(1f, fill = false)
                  .height(52.dp),
                colors = ButtonDefaults.buttonColors(containerColor = DiajakOrange) ){
                Text("Lanjut", fontWeight = FontWeight.Medium, color = Color.White)
                Spacer(modifier = Modifier.width(MaterialTheme.spacing.small))
                Icon(Icons.AutoMirrored.Outlined.ArrowForward, contentDescription = "Next", modifier = Modifier.size(20.dp))
              }
            } else {
              // Submit on Step 3
              Button(
                onClick = {
                  if (locationName.isBlank()) {
                    Toast.makeText(context, "Mohon lengkapi Nama Kota / Wilayah", Toast.LENGTH_SHORT).show()
                    return@Button
                  }
                  if (address.isBlank()) {
                    Toast.makeText(context, "Mohon lengkapi Alamat Titik Kumpul", Toast.LENGTH_SHORT).show()
                    return@Button
                  }

                  val finalTitle = title.trim()
                  val finalAddress = address.trim()
                  val finalOverview = if (overview.isBlank()) "Bergabunglah bersama kami dalam aktivitas seru di kota Anda!" else overview.trim()
                  val valPrice = if (isFree) 0 else priceString.toIntOrNull() ?: 150000
                  val formPrice = if (valPrice == 0) "Gratis" else "Rp ${String.format("%,d", valPrice).replace(',', '.')}"
                  val valQuota = quotaString.toIntOrNull() ?: 15

                  // Format schedule from multi-date calendar or default or recurring
                  val finalSchedule = if (isRecurringAct) {
                    if (selectedDays.isEmpty()) {
                      "Rutin Setiap Hari • 16:00"
                    } else {
                      "Rutin Hari ${selectedDays.joinToString(", ")} • 16:00"
                    }
                  } else if (selectedDates.isNotEmpty()) {
                    selectedDates.sorted().take(3).joinToString(", ") { "$it Jul 2026" } + " • 16:00"
                  } else {
                    "12 Sep 2026 • 16:00"
                  }

                  val imgRes = when {
                    selectedCategory.contains("Kopi") -> R.drawable.diajak_activity_coffee_1783253596378
                    selectedCategory.contains("Outdoor") -> R.drawable.diajak_activity_glamping_1783253582807
                    selectedCategory.contains("Seni") -> R.drawable.diajak_banner_workshop_1783253562818
                    selectedCategory.contains("Olahraga") -> R.drawable.img_sport_running_1783584004743
                    selectedCategory.contains("Kuliner") -> R.drawable.img_culinary_baking_1783584043430
                    selectedCategory.contains("Wellness") || selectedCategory.contains("Yoga") -> R.drawable.img_sunset_yoga_1783584057064
                    selectedCategory.contains("Musik") -> R.drawable.diajak_activity_coffee_1783253596378
                    selectedCategory.contains("Gaming") -> R.drawable.diajak_banner_workshop_1783253562818
                    selectedCategory.contains("Hobi") -> R.drawable.diajak_banner_outdoor_1783253550783
                    selectedCategory.contains("Seminar") -> R.drawable.diajak_activity_coffee_1783253596378
                    else -> R.drawable.diajak_banner_outdoor_1783253550783
                  }

                  onSubmit(
                    finalTitle,
                    selectedCategory,
                    locationName,
                    finalAddress,
                    finalSchedule,
                    formPrice,
                    valPrice,
                    finalOverview,
                    valQuota,
                    imgRes,
                    selectedBenefits.toList(),
                    pinX,
                    pinY,
                    isOngoingAct,
                    isRecurringAct,
                    selectedDays.joinToString(", ")
                  )
                  Toast.makeText(context, "Aktivitas berhasil diterbitkan berulang! 🚀", Toast.LENGTH_SHORT).show()
                },
                 
                shape = RoundedCornerShape(16.dp),
                modifier = Modifier
                  .weight(1.5f, fill = false)
                  .height(52.dp),
                colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF00B368)) ){
                Icon(Icons.Outlined.Check, contentDescription = "Publish", modifier = Modifier.size(20.dp), tint = Color.White)
                Spacer(modifier = Modifier.width(MaterialTheme.spacing.small))
                Text("Terbitkan Sesi", fontWeight = FontWeight.Medium, color = Color.White)
              }
            }
          }
        }

        // --- MAP PIN BOTTOM SHEET OVERLAY (SLIDES UP FROM BOTTOM INSIDE THE DIALOG) ---
        AnimatedVisibility(
          visible = showMapSheet,
          enter = slideInVertically(initialOffsetY = { it }),
          exit = slideOutVertically(targetOffsetY = { it }),
          modifier = Modifier.align(Alignment.BottomCenter) ){
          Surface(
            modifier = Modifier
              .fillMaxWidth()
              .fillMaxHeight(0.88f),
            color = Color.White,
            shape = RoundedCornerShape(topStart = 28.dp, topEnd = 28.dp),
            shadowElevation = 0.dp
          ) {
            val nearestPreset = remember(pinX, pinY) {
              presetLocations.minByOrNull { preset ->
                val dx = preset.x - pinX
                val dy = preset.y - pinY
                dx * dx + dy * dy
              } ?: presetLocations.first()
            }

            Column(
              modifier = Modifier
                .fillMaxSize()
                .padding(20.dp),
              horizontalAlignment = Alignment.CenterHorizontally
            ) {
              // Drag handle bar
              Box(
                modifier = Modifier
                  .size(40.dp, 4.dp)
                  .clip(CircleShape)
                  .background(Color.Transparent)
              )

              Spacer(modifier = Modifier.height(MaterialTheme.spacing.small))

              Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
              ) {
                Row(verticalAlignment = Alignment.CenterVertically, horizontalArrangement = Arrangement.spacedBy(MaterialTheme.spacing.small)) {
                  Icon(Icons.Outlined.Place, contentDescription = null, tint = DiajakOrange)
                  Text(
                    text = "Tentukan Lokasi Aktivitas",
                    style = com.example.ui.theme.DiajakDesignSystem.Typography.Body,
                    color = MaterialTheme.colorScheme.onSurface
                  )
                }
                IconButton(onClick = { showMapSheet = false }) {
                  Text("Batal", color = DiajakOrange, style = com.example.ui.theme.DiajakDesignSystem.Typography.TitleBold)
                }
              }

              Text(
                text = "Ketuk peta untuk mengepin koordinat titik kumpul secara akurat, atau pilih pin rekomendasi di bawah.",
                style = com.example.ui.theme.DiajakDesignSystem.Typography.TitleBold,
                 color = MaterialTheme.colorScheme.onSurface,
                modifier = Modifier.fillMaxWidth().padding(bottom = MaterialTheme.spacing.small)
              )

              // Large Styled Canvas Map with interactive pins
              BoxWithConstraints(
                modifier = Modifier
                  .fillMaxWidth()
                  .weight(1f)
                  .clip(RoundedCornerShape(16.dp))
                  .border(0.dp, Color.Transparent, RoundedCornerShape(16.dp))
              ) {
                val mapWidth = maxWidth
                val mapHeight = maxHeight
                val density = LocalDensity.current
                val widthPx = with(density) { mapWidth.toPx() }
                val heightPx = with(density) { mapHeight.toPx() }

                Canvas(
                  modifier = Modifier
                    .fillMaxSize()
                    .pointerInput(Unit) {
                      detectTapGestures { offset ->
                        if (widthPx > 0 && heightPx > 0) {
                          pinX = (offset.x / widthPx).coerceIn(0.05f, 0.95f)
                          pinY = (offset.y / heightPx).coerceIn(0.05f, 0.95f)
                        }
                      }
                    }
                ) {
                  // Base color: Land
                  drawRect(color = com.example.ui.theme.BackgroundLight)

                  // Forest/Green Park Canvas Representation
                  val parkPath = Path().apply {
                    moveTo(0f, 0f)
                    lineTo(size.width * 0.35f, 0f)
                    quadraticTo(size.width * 0.25f, size.height * 0.25f, size.width * 0.15f, size.height * 0.35f)
                    quadraticTo(size.width * 0.05f, size.height * 0.45f, 0f, size.height * 0.5f)
                    close()
                  }
                  drawPath(parkPath, Color(0xFFDDC3A5)) // Sand beach
                  drawCircle(Color(0xFFDCFCE7), radius = size.width * 0.18f, center = Offset(size.width * 0.15f, size.height * 0.15f)) // Park

                  // Water Reservoir
                  val lakePath = Path().apply {
                    moveTo(size.width * 0.75f, size.height)
                    quadraticTo(size.width * 0.7f, size.height * 0.75f, size.width * 0.85f, size.height * 0.65f)
                    quadraticTo(size.width * 0.95f, size.height * 0.55f, size.width, size.height * 0.5f)
                    lineTo(size.width, size.height)
                    close()
                  }
                  drawPath(lakePath, Color(0xFFE0F2FE)) // Lake blue

                  // Grid Roads / Highways
                  val gridColor = Color.White
                  drawLine(gridColor, Offset(0f, size.height * 0.4f), Offset(size.width, size.height * 0.45f), strokeWidth = 16f)
                  drawLine(gridColor, Offset(0f, size.height * 0.75f), Offset(size.width, size.height * 0.7f), strokeWidth = 14f)
                  drawLine(gridColor, Offset(size.width * 0.45f, 0f), Offset(size.width * 0.52f, size.height), strokeWidth = 18f)
                  drawLine(gridColor, Offset(size.width * 0.8f, 0f), Offset(size.width * 0.75f, size.height), strokeWidth = 12f)
                }

                // Render all Preset Locations on Map using CustomMapPinMarker
                presetLocations.forEach { loc ->
                  val isSelected = nearestPreset == loc

                  Box(
                    modifier = Modifier
                      .layout { measurable, constraints ->
                        val placeable = measurable.measure(constraints)
                        val px = with(density) { (mapWidth * loc.x).toPx() }
                        val py = with(density) { (mapHeight * loc.y).toPx() }
                        layout(placeable.width, placeable.height) {
                          placeable.placeRelative(
                            x = (px - placeable.width / 2f).toInt(),
                            y = (py - placeable.height).toInt()
                          )
                        }
                      }
                  ) {
                    CustomMapPinMarker(
                      categoryIcon = loc.categoryIcon,
                      isSelected = isSelected,
                      title = loc.name,
                      onClick = {
                        pinX = loc.x
                        pinY = loc.y
                        locationName = loc.cityName
                      }
                    )
                  }
                }

                // GPS Near Me simulated pulsing background circle if locating
                if (isSimulatingNearMe) {
                  val infiniteTransition = rememberInfiniteTransition(label = "pulse")
                  val scale by infiniteTransition.animateFloat(
                    initialValue = 10f,
                    targetValue = 90f,
                    animationSpec = infiniteRepeatable(
                      animation = tween(1200, easing = LinearEasing),
                      repeatMode = RepeatMode.Restart
                    ),
                    label = "scale"
                  )
                  val alpha by infiniteTransition.animateFloat(
                    initialValue = 0.6f,
                    targetValue = 0.0f,
                    animationSpec = infiniteRepeatable(
                      animation = tween(1200, easing = LinearEasing),
                      repeatMode = RepeatMode.Restart
                    ),
                    label = "alpha"
                  )
                  Canvas(modifier = Modifier.fillMaxSize()) {
                    drawCircle(
                      color = DiajakOrange.copy(alpha = alpha),
                      radius = scale,
                      center = Offset(size.width * pinX, size.height * pinY)
                    )
                  }
                }

                // Render active pin marker on top if not matching any preset
                if (presetLocations.none { it == nearestPreset }) {
                  Box(
                    modifier = Modifier
                      .layout { measurable, constraints ->
                        val placeable = measurable.measure(constraints)
                        val px = with(density) { (mapWidth * pinX).toPx() }
                        val py = with(density) { (mapHeight * pinY).toPx() }
                        layout(placeable.width, placeable.height) {
                          placeable.placeRelative(
                            x = (px - placeable.width / 2f).toInt(),
                            y = (py - placeable.height).toInt()
                          )
                        }
                      }
                  ) {
                    CustomMapPinMarker(
                      categoryIcon = getCategoryIcon(selectedCategory),
                      isSelected = true,
                      title = locationName.ifEmpty { "Titik Terpilih" }
                    )
                  }
                }
              }

              Spacer(modifier = Modifier.height(MaterialTheme.spacing.small))

              // Beautiful Location Detail card showing nearest preset details (exactly like the video reference)
              Card(
                modifier = Modifier.fillMaxWidth(),
                colors = CardDefaults.cardColors(containerColor = Color.White),
                border = BorderStroke(0.dp, Color.Transparent),
                 
                shape = RoundedCornerShape(16.dp)
              ) {
                Row(
                  modifier = Modifier.padding(20.dp),
                  verticalAlignment = Alignment.CenterVertically
                ) {
                  Box(
                    modifier = Modifier
                      .size(40.dp)
                      .clip(CircleShape)
                      .background(DiajakOrangeLight),
                    contentAlignment = Alignment.Center
                  ) {
                    Text(nearestPreset.icon, style = com.example.ui.theme.DiajakDesignSystem.Typography.Body)
                  }
                  Spacer(modifier = Modifier.width(20.dp))
                  Column(modifier = Modifier.weight(1f)) {
                    Text(
                      text = nearestPreset.name,
                      style = com.example.ui.theme.DiajakDesignSystem.Typography.Body,
                      color = MaterialTheme.colorScheme.onSurface
                    )
                    Spacer(modifier = Modifier.height(2.dp))
                    Text(
                      text = nearestPreset.address,
                      style = com.example.ui.theme.DiajakDesignSystem.Typography.TitleBold,
                      color = Color(0xFF262626),
                      maxLines = 1,
                      overflow = TextOverflow.Ellipsis
                    )
                  }
                }
              }

              Spacer(modifier = Modifier.height(MaterialTheme.spacing.medium))

              // Bottom control buttons: Choose Location & Near Me
              Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(MaterialTheme.spacing.small),
                verticalAlignment = Alignment.CenterVertically
              ) {
                // "Near Me" (Deteksi Lokasi Saya) Button
                IconButton(
                  onClick = {
                    coroutineScope.launch {
                      isSimulatingNearMe = true
                      Toast.makeText(context, "Mendeteksi sinyal GPS terdekat...", Toast.LENGTH_SHORT).show()
                      delay(1000)
                      // Move coordinate to Java Chicken
                      pinX = 0.52f
                      pinY = 0.42f
                      isSimulatingNearMe = false
                      Toast.makeText(context, "GPS Terpusat: Sleman, Nox Coffee Boutique 📍", Toast.LENGTH_LONG).show()
                    }
                  },
                  modifier = Modifier
                    .size(52.dp)
                    .background(MaterialTheme.colorScheme.background, RoundedCornerShape(16.dp))
                ) {
                  if (isSimulatingNearMe) {
                    CircularProgressIndicator(color = DiajakOrange, modifier = Modifier.size(20.dp), strokeWidth = 2.dp)
                  } else {
                    Icon(Icons.Outlined.MyLocation, contentDescription = "GPS", tint = Color(0xFF475569))
                  }
                }

                // Choose This Location Button
                Button(
                  onClick = {
                    locationName = nearestPreset.cityName
                    address = "${nearestPreset.name}, ${nearestPreset.address}"
                    pinX = nearestPreset.x
                    pinY = nearestPreset.y
                    showMapSheet = false
                    Toast.makeText(context, "Lokasi terpilih: ${nearestPreset.name} 📍", Toast.LENGTH_LONG).show()
                  },
                  modifier = Modifier
                    .weight(1f)
                    .height(52.dp),
                   
                shape = RoundedCornerShape(16.dp),
                  colors = ButtonDefaults.buttonColors(containerColor = DiajakOrange)
                ) {
                  Icon(Icons.Outlined.Check, contentDescription = null, tint = Color.White)
                  Spacer(modifier = Modifier.width(MaterialTheme.spacing.small))
                  Text("Pilih Tujuan Ini", color = Color.White, style = com.example.ui.theme.DiajakDesignSystem.Typography.TitleBold)
                }
              }
            }
          }
        }

        // Top iOS Glass Header for Wizard
        DiajakGlassHeader(
          hazeState = dialogHazeState,
          modifier = Modifier
            .align(Alignment.TopCenter)
            .zIndex(10f)
        ) {
          Box(
            modifier = Modifier
              .fillMaxWidth()
              .height(56.dp)
              .padding(horizontal = 16.dp)
          ) {
            IconButton(
              onClick = {
                if (currentStep > 1) {
                  currentStep--
                } else {
                  onDismiss()
                }
              },
              modifier = Modifier
                .size(40.dp)
                .align(Alignment.CenterStart)
                .diajakGlassButton(dialogHazeState)
            ) {
              Icon(
                imageVector = if (currentStep > 1) Icons.AutoMirrored.Outlined.ArrowBack else Icons.Outlined.Close,
                contentDescription = if (currentStep > 1) "Kembali" else "Tutup",
                tint = MaterialTheme.colorScheme.onSurface
              )
            }

            Text(
              text = when (currentStep) {
                1 -> "Tambah Aktivitas"
                2 -> "Tentukan Jadwal & Kategori"
                else -> "Tentukan Lokasi & Kuota"
              },
              style = com.example.ui.theme.DiajakDesignSystem.Typography.Headline,
              color = MaterialTheme.colorScheme.onSurface,
              textAlign = TextAlign.Center,
              modifier = Modifier
                .align(Alignment.Center)
                .fillMaxWidth()
            )
          }
        }
      }
      }
    }
  }
}

@Composable
fun WizardProgressStep(
  stepNumber: Int,
  title: String,
  isActive: Boolean,
  isCurrent: Boolean,
  modifier: Modifier = Modifier
) {
  Column(
    modifier = modifier,
    horizontalAlignment = Alignment.CenterHorizontally
  ) {
    Box(
      modifier = Modifier
        .fillMaxWidth()
        .height(4.dp)
        .clip(CircleShape)
        .background(
          when {
            isCurrent -> DiajakOrange
            isActive -> DiajakOrange.copy(alpha = 0.5f)
            else -> MaterialTheme.colorScheme.outlineVariant
          }
        )
    )
    Spacer(modifier = Modifier.height(MaterialTheme.spacing.extraSmall))
    Text(
      text = title,
      style = com.example.ui.theme.DiajakDesignSystem.Typography.Body,
      fontWeight = if (isCurrent) FontWeight.Bold else FontWeight.Medium,
      color = if (isCurrent) DiajakOrange else if (isActive) MaterialTheme.colorScheme.onSurface else MaterialTheme.colorScheme.onSurface,
      textAlign = TextAlign.Center
    )
  }
}

private data class PresetLocation(
  val name: String,
  val cityName: String,
  val address: String,
  val x: Float,
  val y: Float,
  val icon: String = "📍",
  val categoryIcon: androidx.compose.ui.graphics.vector.ImageVector = Icons.Outlined.Place
)

private val presetLocations = listOf(
  PresetLocation(
    name = "Java Chicken Purworejo",
    cityName = "Purworejo, Jawa Tengah",
    address = "Jl. Ir. H Juanda, Baledono, Purworejo",
    x = 0.52f,
    y = 0.42f,
    icon = "🍗",
    categoryIcon = Icons.Outlined.Restaurant
  ),
  PresetLocation(
    name = "Mie Ayam Pak Asep",
    cityName = "Purworejo, Jawa Tengah",
    address = "Jl. Ir. H Juanda, Purworejo, Purworejo",
    x = 0.45f,
    y = 0.55f,
    icon = "🍜",
    categoryIcon = Icons.Outlined.Restaurant
  ),
  PresetLocation(
    name = "Toko SRC Budi Baledono",
    cityName = "Purworejo, Jawa Tengah",
    address = "Jl. Ir. H Juanda No. 25, Baledono, Purworejo",
    x = 0.38f,
    y = 0.60f,
    icon = "🏪",
    categoryIcon = Icons.Outlined.Storefront
  ),
  PresetLocation(
    name = "Pintu Masuk Makam Brengkelan",
    cityName = "Purworejo, Jawa Tengah",
    address = "Jl. Kyai Brengkel, Purworejo, Purworejo",
    x = 0.30f,
    y = 0.40f,
    icon = "⛰️",
    categoryIcon = Icons.Outlined.Explore
  ),
  PresetLocation(
    name = "Agen Shuttle Sumber Alam Brengkelan",
    cityName = "Purworejo, Jawa Tengah",
    address = "Jl. Kyai Brengkel No.12, Purworejo, Jawa Tengah",
    x = 0.70f,
    y = 0.65f,
    icon = "🚌",
    categoryIcon = Icons.Outlined.DirectionsBus
  ),
  PresetLocation(
    name = "Nox Coffee Boutique Sleman",
    cityName = "Sleman, D.I. Yogyakarta",
    address = "Jl. Kaliurang KM 5, Sleman, D.I. Yogyakarta",
    x = 0.60f,
    y = 0.30f,
    icon = "☕",
    categoryIcon = Icons.Outlined.LocalCafe
  ),
  PresetLocation(
    name = "Starbucks Malioboro Mall",
    cityName = "Kota Yogyakarta, DIY",
    address = "Malioboro Mall Ground Floor, Jl. Malioboro, DIY",
    x = 0.50f,
    y = 0.50f,
    icon = "☕",
    categoryIcon = Icons.Outlined.LocalCafe
  )
)

@Composable
fun CreateEventDialog(
  onDismiss: () -> Unit,
  onSubmit: (
    title: String,
    category: String,
    location: String,
    address: String,
    schedule: String,
    priceFormatted: String,
    priceValue: Int,
    overview: String,
    quota: Int,
    imageRes: Int,
    benefits: List<String>,
    mapX: Float,
    mapY: Float,
    isOngoing: Boolean,
    isRecurring: Boolean,
    recurringDays: String
  ) -> Unit
) = CreateActivityDialog(onDismiss, onSubmit)
