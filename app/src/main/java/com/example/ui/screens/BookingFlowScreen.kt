package com.example.ui.screens

import com.example.ui.theme.AppSpacing
import com.example.ui.theme.spacing
import com.example.ui.theme.diajakGlassButton
import androidx.compose.material3.MaterialTheme

import androidx.compose.material.icons.automirrored.outlined.*
import androidx.compose.material.icons.outlined.*

import android.content.ClipData
import android.content.ClipboardManager
import android.content.Context
import android.widget.Toast
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.slideInHorizontally
import androidx.compose.animation.slideOutHorizontally
import androidx.compose.foundation.gestures.detectTapGestures
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.*
import androidx.compose.material.icons.outlined.*
import androidx.compose.material.icons.outlined.*
import androidx.compose.material.icons.outlined.*
import androidx.compose.material.icons.outlined.*
import androidx.compose.material.icons.outlined.*
import androidx.compose.material.icons.outlined.*
import androidx.compose.material.icons.outlined.*
import androidx.compose.material.icons.outlined.*
import androidx.compose.material.icons.outlined.*
import androidx.compose.material.icons.outlined.*
import androidx.compose.material.icons.outlined.*
import androidx.compose.material.icons.outlined.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.drawWithContent
import androidx.compose.ui.geometry.CornerRadius
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.PathEffect
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.zIndex
import androidx.compose.ui.text.AnnotatedString
import dev.chrisbanes.haze.HazeState
import dev.chrisbanes.haze.hazeSource
import dev.chrisbanes.haze.hazeEffect
import com.example.ui.components.DiajakGlassHeader

import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.withStyle
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.buildAnnotatedString

import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.model.ActivityModel
import com.example.ui.theme.DiajakOrange
import kotlinx.coroutines.delay
import java.util.UUID

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun BookingFlowScreen(
  activity: ActivityModel,
  onDismiss: () -> Unit,
  onBookingConfirmed: (undanganCount: Int, name: String, email: String, phone: String, bookingId: String, selectedDate: String) -> Unit,
  onViewAllUndangans: () -> Unit = {},
  initialName: String = "",
  initialEmail: String = "",
  initialPhone: String = ""
) {
  val context = LocalContext.current
  val clipboardManager = remember(context) {
    context.getSystemService(Context.CLIPBOARD_SERVICE) as? ClipboardManager
  }
  val focusManager = androidx.compose.ui.platform.LocalFocusManager.current
  
  val screenWidth = androidx.compose.ui.platform.LocalConfiguration.current.screenWidthDp.dp
  val favoriteImageSize = (screenWidth - 60.dp) / 2
  val targetImageSize = favoriteImageSize / 2

  // States
  var currentStep by remember { mutableStateOf(1) } // 1: Undangan select, 2: Midtrans Pay, 3: Undangan
  var undanganCount by remember { mutableStateOf(1) }
  var selectedPaymentMethod by remember { mutableStateOf("qris") } // qris, bca_va, mandiri_va, card
  var isPaying by remember { mutableStateOf(false) }

  androidx.activity.compose.BackHandler {
    when (currentStep) {
      3 -> onViewAllUndangans()
      2 -> currentStep = 1
      1 -> onDismiss()
    }
  }
  
  var userNameInput by remember { mutableStateOf(initialName) }
  var userEmailInput by remember { mutableStateOf(initialEmail) }
  var userPhoneInput by remember { mutableStateOf(initialPhone) }
  
  val dateOptions = remember(activity.schedule) { generateBookingDateOptions(activity.schedule) }
  var selectedDateOption by remember { mutableStateOf(dateOptions.firstOrNull()?.fullDateText ?: activity.schedule) }
  
  // Generate a random Booking ID
  val bookingId = remember { "DJK-" + UUID.randomUUID().toString().substring(0, 8).uppercase() }
  // Generate a random VA number
  val vaNumber = remember { "88019" + (10000000..99999999).random().toString() }

  val maxSlots = (activity.maxPeserta - activity.currentPeserta).coerceAtLeast(1)
  val undanganPrice = activity.priceValue
  val totalPrice = undanganPrice * undanganCount
  val totalPriceFormatted = if (totalPrice == 0) "Gratis" else "Rp ${String.format("%,d", totalPrice).replace(',', '.')}"

  val hazeState = remember { HazeState() }

  Box(
    modifier = Modifier
      .fillMaxSize()
      .background(Color(0xFFE5E7EB))
      .pointerInput(Unit) {
        detectTapGestures(
          onTap = {
            focusManager.clearFocus()
          }
        )
      }
  ) {
    // 1. Content Layer
    Box(
      modifier = Modifier
        .fillMaxSize()
        .hazeSource(state = hazeState)
    ) {
      when (currentStep) {
        1 -> {
          // STEP 1: Undangan Selection
          Column(
            modifier = Modifier.fillMaxSize()
          ) {
            Column(
              modifier = Modifier
                .weight(1f)
                .verticalScroll(rememberScrollState())
                .padding(start = MaterialTheme.spacing.screenMargin, end = MaterialTheme.spacing.screenMargin, bottom = MaterialTheme.spacing.section)
            ) {
              Spacer(modifier = Modifier.statusBarsPadding().height(80.dp))
              
              // Activity Summary Flat Row
              Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.Top
              ) {
                Box(
                  modifier = Modifier
                    .size(targetImageSize)
                    .clip(RoundedCornerShape(12.dp))
                ) {
                  Image(
                    painter = painterResource(id = activity.imageResId),
                    contentDescription = activity.title,
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
                      Icons.Outlined.Place,
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
                      Icons.Outlined.AccessTime,
                      contentDescription = null,
                      tint = MaterialTheme.colorScheme.onSurface,
                      modifier = Modifier.size(18.dp)
                    )
                    Spacer(modifier = Modifier.width(MaterialTheme.spacing.extraSmall))
                    Text(
                      text = selectedDateOption,
                      style = com.example.ui.theme.DiajakDesignSystem.Typography.Body,
                      color = MaterialTheme.colorScheme.onSurface,
                      maxLines = 1,
                      overflow = TextOverflow.Ellipsis
                    )
                  }
                }
              }

              Spacer(modifier = Modifier.height(MaterialTheme.spacing.section))

              // Pilih Tanggal Ketersediaan Section
              Column(
                modifier = Modifier.fillMaxWidth()
              ) {
                Row(
                  verticalAlignment = Alignment.CenterVertically
                ) {
                  Icon(
                    imageVector = Icons.Outlined.Event,
                    contentDescription = "Pilih Tanggal",
                    tint = DiajakOrange,
                    modifier = Modifier.size(20.dp)
                  )
                  Spacer(modifier = Modifier.width(MaterialTheme.spacing.small))
                  Text(
                    text = "Pilih Tanggal Ketersediaan",
                    style = com.example.ui.theme.DiajakDesignSystem.Typography.BodyBold.copy(fontSize = 15.sp),
                    color = MaterialTheme.colorScheme.onSurface
                  )
                }

                Spacer(modifier = Modifier.height(MaterialTheme.spacing.medium))

                LazyRow(
                  horizontalArrangement = Arrangement.spacedBy(MaterialTheme.spacing.medium),
                  contentPadding = PaddingValues(horizontal = 2.dp)
                ) {
                  items(dateOptions) { option ->
                    val isSelected = (selectedDateOption == option.fullDateText)

                    Surface(
                      modifier = Modifier
                        .width(135.dp)
                        .clickable { selectedDateOption = option.fullDateText },
                      shape = RoundedCornerShape(20.dp),
                      color = Color.White,
                      shadowElevation = 0.dp,
                      border = BorderStroke(
                        width = if (isSelected) 2.dp else 0.dp,
                        color = if (isSelected) DiajakOrange else Color.Transparent
                      )
                    ) {
                      Column(
                        modifier = Modifier
                          .fillMaxWidth()
                          .padding(MaterialTheme.spacing.medium),
                        horizontalAlignment = Alignment.CenterHorizontally
                      ) {
                        Text(
                          text = option.dayName,
                          style = com.example.ui.theme.DiajakDesignSystem.Typography.BodyBold.copy(fontSize = 14.sp),
                          color = if (isSelected) DiajakOrange else MaterialTheme.colorScheme.onSurface,
                          maxLines = 1,
                          overflow = TextOverflow.Ellipsis
                        )
                        Spacer(modifier = Modifier.height(MaterialTheme.spacing.extraSmall))
                        Text(
                          text = option.dateFormatted,
                          style = com.example.ui.theme.DiajakDesignSystem.Typography.Body.copy(fontSize = 12.sp, fontWeight = FontWeight.Medium),
                          color = MaterialTheme.colorScheme.onSurface,
                          textAlign = TextAlign.Center,
                          maxLines = 1,
                          overflow = TextOverflow.Ellipsis
                        )
                        Spacer(modifier = Modifier.height(MaterialTheme.spacing.extraSmall))
                        Text(
                          text = option.timeText,
                          style = com.example.ui.theme.DiajakDesignSystem.Typography.Body.copy(fontSize = 11.sp),
                          color = MaterialTheme.colorScheme.onSurfaceVariant,
                          textAlign = TextAlign.Center,
                          maxLines = 1,
                          overflow = TextOverflow.Ellipsis
                        )
                      }
                    }
                  }
                }
              }

              Spacer(modifier = Modifier.height(MaterialTheme.spacing.medium))

              // Stepper Block
              Column(
                modifier = Modifier.fillMaxWidth()
              ) {
                  Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                  ) {
                    Text(
                      text = "Jumlah Peserta",
                      style = com.example.ui.theme.DiajakDesignSystem.Typography.Body,
                      color = MaterialTheme.colorScheme.onSurface
                    )

                    Row(
                      verticalAlignment = Alignment.CenterVertically,
                      horizontalArrangement = Arrangement.spacedBy(MaterialTheme.spacing.medium),
                      modifier = Modifier.offset(x = 12.dp)
                    ) {
                      Box(
                        modifier = Modifier
                          .size(36.dp)
                          .clip(CircleShape)
                          .background(androidx.compose.ui.graphics.Color(0xFFE5E7EB))
                          .clickable { if (undanganCount > 1) undanganCount-- },
                        contentAlignment = Alignment.Center
                      ) {
                        Text("–", style = com.example.ui.theme.DiajakDesignSystem.Typography.TitleBold, color = MaterialTheme.colorScheme.onSurface)
                      }

                      Text(
                        text = "$undanganCount",
                        style = com.example.ui.theme.DiajakDesignSystem.Typography.BodyBold,
                        color = MaterialTheme.colorScheme.onSurface
                      )

                      Box(
                        modifier = Modifier
                          .size(36.dp)
                          .clip(CircleShape)
                          .background(androidx.compose.ui.graphics.Color(0xFFE5E7EB))
                          .clickable { if (undanganCount < maxSlots) undanganCount++ },
                        contentAlignment = Alignment.Center
                      ) {
                        Text("+", style = com.example.ui.theme.DiajakDesignSystem.Typography.TitleBold, color = MaterialTheme.colorScheme.onSurface)
                      }
                    }
                  }
              }

              Spacer(modifier = Modifier.height(MaterialTheme.spacing.extraSmall)) // Compensated top spacer for visually larger text field

              Column(
                modifier = Modifier.fillMaxWidth()
              ) {
                PersistentOutlinedTextField(
                  value = userNameInput,
                  onValueChange = { userNameInput = it },
                  labelText = "Nama Lengkap",
                  placeholderText = "Masukkan nama lengkap",
                  leadingIcon = { Icon(Icons.Outlined.Person, contentDescription = null, tint = MaterialTheme.colorScheme.onSurface, modifier = Modifier.size(18.dp)) },
                  singleLine = true
                )

                Spacer(modifier = Modifier.height(MaterialTheme.spacing.small))

                PersistentOutlinedTextField(
                  value = userEmailInput,
                  onValueChange = { userEmailInput = it },
                  labelText = "Alamat Email",
                  placeholderText = "contoh@email.com",
                  leadingIcon = { Icon(Icons.Outlined.Email, contentDescription = null, tint = MaterialTheme.colorScheme.onSurface, modifier = Modifier.size(18.dp)) },
                  singleLine = true,
                  keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Email)
                )

                Spacer(modifier = Modifier.height(MaterialTheme.spacing.small))

                PersistentOutlinedTextField(
                  value = userPhoneInput,
                  onValueChange = { userPhoneInput = it },
                  labelText = "Nomor Handphone",
                  placeholderText = "08xxxxxxxxxx",
                  leadingIcon = { Icon(Icons.Outlined.Phone, contentDescription = null, tint = MaterialTheme.colorScheme.onSurface, modifier = Modifier.size(18.dp)) },
                  singleLine = true,
                  keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number)
                )
              }

              Spacer(modifier = Modifier.height(MaterialTheme.spacing.medium))
              Column(
                modifier = Modifier.fillMaxWidth()
              ) {
                Text(
                  text = "Rincian Biaya",
                  style = com.example.ui.theme.DiajakDesignSystem.Typography.BodyBold,
                  color = MaterialTheme.colorScheme.onSurface
                )
                
                Column(
                  modifier = Modifier
                    .fillMaxWidth()
                    .padding(top = 10.dp),
                  verticalArrangement = Arrangement.spacedBy(AppSpacing.Small)
                ) {
                  Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.Top
                  ) {
                    Row(
                      modifier = Modifier.weight(1f),
                      verticalAlignment = Alignment.Top
                    ) {
                      Text(
                        text = activity.title,
                        style = com.example.ui.theme.DiajakDesignSystem.Typography.Body,
                        color = MaterialTheme.colorScheme.onSurface,
                        maxLines = 1,
                        overflow = TextOverflow.Ellipsis,
                        modifier = Modifier.weight(1f, fill = false)
                      )
                      Text(
                        text = " (x$undanganCount)",
                        style = com.example.ui.theme.DiajakDesignSystem.Typography.Body,
                        color = MaterialTheme.colorScheme.onSurface
                      )
                    }
                    Spacer(modifier = Modifier.width(MaterialTheme.spacing.medium))
                    Text(
                      text = totalPriceFormatted,
                      style = com.example.ui.theme.DiajakDesignSystem.Typography.Body,
                      color = MaterialTheme.colorScheme.onSurface,
                      textAlign = TextAlign.End,
                      maxLines = 1,
                      modifier = Modifier.padding(end = 6.dp)
                    )
                  }

                  Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                  ) {
                    Text(
                      text = "Biaya Layanan",
                      style = com.example.ui.theme.DiajakDesignSystem.Typography.Body,
                      color = MaterialTheme.colorScheme.onSurface
                    )
                    Text(
                      text = "Rp 0",
                      style = com.example.ui.theme.DiajakDesignSystem.Typography.Body,
                      color = MaterialTheme.colorScheme.onSurface,
                      textAlign = TextAlign.End,
                      maxLines = 1,
                      modifier = Modifier.padding(end = 6.dp)
                    )
                  }

                  Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                  ) {
                    Text(
                      text = "Biaya Transaksi",
                      style = com.example.ui.theme.DiajakDesignSystem.Typography.Body,
                      color = MaterialTheme.colorScheme.onSurface
                    )
                    Text(
                      text = "Rp 0",
                      style = com.example.ui.theme.DiajakDesignSystem.Typography.Body,
                      color = MaterialTheme.colorScheme.onSurface,
                      textAlign = TextAlign.End,
                      maxLines = 1,
                      modifier = Modifier.padding(end = 6.dp)
                    )
                  }
                }

                Spacer(modifier = Modifier.height(MaterialTheme.spacing.medium))

                Row(
                  modifier = Modifier.fillMaxWidth(),
                  horizontalArrangement = Arrangement.SpaceBetween,
                  verticalAlignment = Alignment.CenterVertically
                ) {
                  Text(
                    text = "Total Pembayaran",
                    style = com.example.ui.theme.DiajakDesignSystem.Typography.BodyBold.copy(fontSize = 16.sp),
                    color = MaterialTheme.colorScheme.onSurface
                  )
                  Text(
                    text = totalPriceFormatted,
                    style = com.example.ui.theme.DiajakDesignSystem.Typography.BodyBold.copy(fontSize = 16.sp),
                    color = MaterialTheme.colorScheme.onSurface,
                    textAlign = TextAlign.End,
                    maxLines = 1,
                    modifier = Modifier.padding(end = 6.dp)
                  )
                }
              }

            }

            // Fixed Bottom action panel - Always visible!
            Box(
              modifier = Modifier
                .fillMaxWidth()
                .navigationBarsPadding()
                .padding(horizontal = MaterialTheme.spacing.screenMargin, vertical = MaterialTheme.spacing.medium)
            ) {
              Surface(
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(16.dp),
                color = Color.Transparent,
                shadowElevation = 0.dp
              ) {
                Box(
                  modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 20.dp, vertical = MaterialTheme.spacing.medium)
                ) {
                Button(
                  onClick = {
                    if (userNameInput.isBlank() || userEmailInput.isBlank() || userPhoneInput.isBlank()) {
                      Toast.makeText(context, "Harap lengkapi semua informasi kontak peserta!", Toast.LENGTH_SHORT).show()
                      return@Button
                    }
                    if (totalPrice == 0) {
                      // Free event doesn't need Midtrans, proceed straight to Undangan!
                      currentStep = 3
                      onBookingConfirmed(undanganCount, userNameInput, userEmailInput, userPhoneInput, bookingId, selectedDateOption)
                    } else {
                      currentStep = 2
                    }
                  },
                  modifier = Modifier
                    .fillMaxWidth()
                    .height(52.dp),
                  shape = RoundedCornerShape(16.dp),
                  colors = ButtonDefaults.buttonColors(containerColor = DiajakOrange)
                ) {
                  Text(
                    text = "Lanjut",
                    style = androidx.compose.ui.text.TextStyle(fontSize = 16.sp, fontWeight = androidx.compose.ui.text.font.FontWeight.Bold, fontFamily = com.example.ui.theme.AppFontFamily),
                    color = Color.White
                  )
                }
              }
            }
            }
          }
        }

        2 -> {
          // STEP 2: Midtrans Payment Gate Simulator
          Column(
            modifier = Modifier.fillMaxSize()
          ) {
            Column(
              modifier = Modifier
                .weight(1f)
                .verticalScroll(rememberScrollState())
                .padding(start = MaterialTheme.spacing.screenMargin, end = MaterialTheme.spacing.screenMargin, bottom = MaterialTheme.spacing.medium)
            ) {
              Spacer(modifier = Modifier.statusBarsPadding().height(80.dp))
              
              Column(
                modifier = Modifier
                  .fillMaxWidth()
              ) {
                Row(
                  modifier = Modifier.fillMaxWidth(),
                  horizontalArrangement = Arrangement.SpaceBetween,
                ) {
                  Text(
                    text = "Tanggal Aktivitas:\nOrder ID:\nTotal Pembayaran:",
                    style = com.example.ui.theme.DiajakDesignSystem.Typography.Body,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                  )
                  Text(
                    text = buildAnnotatedString {
                      withStyle(style = SpanStyle(
                        color = MaterialTheme.colorScheme.onSurface,
                        fontWeight = com.example.ui.theme.DiajakDesignSystem.Typography.Body.fontWeight
                      )) {
                        append(selectedDateOption + "\n" + bookingId + "\n")
                      }
                      withStyle(style = SpanStyle(
                        color = DiajakOrange,
                        fontWeight = androidx.compose.ui.text.font.FontWeight.Bold
                      )) {
                        append(totalPriceFormatted)
                      }
                    },
                    style = com.example.ui.theme.DiajakDesignSystem.Typography.Body,
                    textAlign = androidx.compose.ui.text.style.TextAlign.End,
                    modifier = Modifier.padding(end = 6.dp)
                  )
                }
              }

              Spacer(modifier = Modifier.height(MaterialTheme.spacing.medium))

              // Payment Methods list
              Column(
                verticalArrangement = Arrangement.spacedBy(AppSpacing.Medium)
              ) {
                // QRIS
                PaymentMethodItem(
                  title = "QRIS",
                  subtitle = "",
                  icon = Icons.Outlined.QrCodeScanner,
                  selected = selectedPaymentMethod == "qris",
                  onClick = { selectedPaymentMethod = "qris" }
                ) {
                  Column(
                    modifier = Modifier
                      .fillMaxWidth()
                      .padding(start = MaterialTheme.spacing.screenMargin, end = MaterialTheme.spacing.screenMargin, bottom = MaterialTheme.spacing.section, top = MaterialTheme.spacing.extraSmall),
                    horizontalAlignment = Alignment.CenterHorizontally
                  ) {
                    Text(
                      text = "Gopay, OVO, Dana, LinkAja, M-Banking, dll",
                      style = com.example.ui.theme.DiajakDesignSystem.Typography.Body,
                      color = MaterialTheme.colorScheme.onSurfaceVariant,
                      textAlign = TextAlign.Start,
                      modifier = Modifier.align(Alignment.Start)
                    )

                    Spacer(modifier = Modifier.height(MaterialTheme.spacing.medium))

                    // QRIS Code box simulation
                    Box(
                      modifier = Modifier
                        .size(130.dp)
                        .background(Color.White)
                        .border(1.dp, Color.Transparent, RoundedCornerShape(8.dp))
                        .padding(10.dp),
                      contentAlignment = Alignment.Center
                    ) {
                      Canvas(modifier = Modifier.fillMaxSize()) {
                        val sizePx = size.width
                        val cols = 11
                        val cellSize = sizePx / cols
                        
                        drawRect(Color.White)

                        // Draw Corner Finders
                        drawRect(androidx.compose.ui.graphics.Color(0xFF202124), Offset(0f, 0f), Size(cellSize * 3, cellSize * 3))
                        drawRect(Color.White, Offset(cellSize, cellSize), Size(cellSize, cellSize))
                        drawRect(androidx.compose.ui.graphics.Color(0xFF202124), Offset(cellSize * 8, 0f), Size(cellSize * 3, cellSize * 3))
                        drawRect(Color.White, Offset(cellSize * 9, cellSize), Size(cellSize, cellSize))
                        drawRect(androidx.compose.ui.graphics.Color(0xFF202124), Offset(0f, cellSize * 8), Size(cellSize * 3, cellSize * 3))
                        drawRect(Color.White, Offset(cellSize, cellSize * 9), Size(cellSize, cellSize))

                        for (r in 0 until cols) {
                          for (c in 0 until cols) {
                            if ((r < 3 && c < 3) || (r < 3 && c >= 8) || (r >= 8 && c < 3)) {
                              continue
                            }
                            if ((r * c + r + c) % 3 == 0 || (r + c) % 5 == 0) {
                              drawRect(
                                color = androidx.compose.ui.graphics.Color(0xFF202124),
                                topLeft = Offset(c * cellSize, r * cellSize),
                                size = Size(cellSize, cellSize)
                              )
                            }
                          }
                        }
                      }
                    }
                  }
                }

                // BCA VA
                PaymentMethodItem(
                  title = "BCA Virtual Account",
                  subtitle = "",
                  icon = Icons.Outlined.AccountBalance,
                  selected = selectedPaymentMethod == "bca_va",
                  onClick = { selectedPaymentMethod = "bca_va" }
                ) {
                  Column(
                    modifier = Modifier
                      .fillMaxWidth()
                      .padding(start = MaterialTheme.spacing.screenMargin, end = MaterialTheme.spacing.screenMargin, bottom = MaterialTheme.spacing.section, top = MaterialTheme.spacing.extraSmall)
                  ) {
                    Text("Nomor Virtual Account BCA", style = com.example.ui.theme.DiajakDesignSystem.Typography.Body, color = MaterialTheme.colorScheme.onSurface)
                    Row(
                      modifier = Modifier
                        .fillMaxWidth()
                        .padding(vertical = MaterialTheme.spacing.extraSmall),
                      horizontalArrangement = Arrangement.SpaceBetween,
                      verticalAlignment = Alignment.CenterVertically
                    ) {
                      Text(
                        text = vaNumber,
                        style = com.example.ui.theme.DiajakDesignSystem.Typography.Body,
                        color = MaterialTheme.colorScheme.onSurface,
                        fontFamily = com.example.ui.theme.AppFontFamily
                      )
                      Row(
                        modifier = Modifier
                          .clickable {
                            val clip = ClipData.newPlainText("Virtual Account", vaNumber)
                            clipboardManager?.setPrimaryClip(clip)
                            Toast.makeText(context, "Nomor VA disalin!", Toast.LENGTH_SHORT).show()
                          }
                          .background(DiajakOrange.copy(alpha = 0.1f), RoundedCornerShape(6.dp))
                          .padding(horizontal = MaterialTheme.spacing.small, vertical = MaterialTheme.spacing.extraSmall),
                        verticalAlignment = Alignment.CenterVertically
                      ) {
                        Icon(Icons.Outlined.ContentCopy, contentDescription = "Copy", tint = DiajakOrange, modifier = Modifier.size(12.dp))
                        Spacer(modifier = Modifier.width(MaterialTheme.spacing.extraSmall))
                        Text("Salin", style = com.example.ui.theme.DiajakDesignSystem.Typography.BodyBold, color = DiajakOrange)
                      }
                    }
                    Spacer(modifier = Modifier.height(MaterialTheme.spacing.extraSmall))
                    Text("Langkah Pembayaran:", style = com.example.ui.theme.DiajakDesignSystem.Typography.TitleBold, color = MaterialTheme.colorScheme.onSurface)
                    Text("1. Pilih m-Transfer > BCA Virtual Account.", style = com.example.ui.theme.DiajakDesignSystem.Typography.Body, color = MaterialTheme.colorScheme.onSurface)
                    Text("2. Masukkan nomor VA di atas.", style = com.example.ui.theme.DiajakDesignSystem.Typography.Body, color = MaterialTheme.colorScheme.onSurface)
                    Text("3. Tagihan akan otomatis terdeteksi, tekan Bayar.", style = com.example.ui.theme.DiajakDesignSystem.Typography.Body, color = MaterialTheme.colorScheme.onSurface)
                  }
                }

                // Mandiri VA
                PaymentMethodItem(
                  title = "Mandiri Virtual Account",
                  subtitle = "",
                  icon = Icons.Outlined.AccountBalanceWallet,
                  selected = selectedPaymentMethod == "mandiri_va",
                  onClick = { selectedPaymentMethod = "mandiri_va" }
                ) {
                  Column(
                    modifier = Modifier
                      .fillMaxWidth()
                      .padding(start = MaterialTheme.spacing.screenMargin, end = MaterialTheme.spacing.screenMargin, bottom = MaterialTheme.spacing.section, top = MaterialTheme.spacing.extraSmall)
                  ) {
                    Text("Nomor Virtual Account Mandiri", style = com.example.ui.theme.DiajakDesignSystem.Typography.TitleBold, color = MaterialTheme.colorScheme.onSurface)
                    Row(
                      modifier = Modifier
                        .fillMaxWidth()
                        .padding(vertical = MaterialTheme.spacing.extraSmall),
                      horizontalArrangement = Arrangement.SpaceBetween,
                      verticalAlignment = Alignment.CenterVertically
                    ) {
                      val mandiriVa = "89022$vaNumber"
                      Text(
                        text = mandiriVa,
                        style = com.example.ui.theme.DiajakDesignSystem.Typography.Body,
                        color = MaterialTheme.colorScheme.onSurface,
                        fontFamily = com.example.ui.theme.AppFontFamily
                      )
                      Row(
                        modifier = Modifier
                          .clickable {
                            val clip = ClipData.newPlainText("Virtual Account", mandiriVa)
                            clipboardManager?.setPrimaryClip(clip)
                            Toast.makeText(context, "Nomor VA Mandiri disalin!", Toast.LENGTH_SHORT).show()
                          }
                          .background(DiajakOrange.copy(alpha = 0.1f), RoundedCornerShape(6.dp))
                          .padding(horizontal = MaterialTheme.spacing.small, vertical = MaterialTheme.spacing.extraSmall),
                        verticalAlignment = Alignment.CenterVertically
                      ) {
                        Icon(Icons.Outlined.ContentCopy, contentDescription = "Copy", tint = DiajakOrange, modifier = Modifier.size(12.dp))
                        Spacer(modifier = Modifier.width(MaterialTheme.spacing.extraSmall))
                        Text("Salin", style = com.example.ui.theme.DiajakDesignSystem.Typography.BodyBold, color = DiajakOrange)
                      }
                    }
                    Spacer(modifier = Modifier.height(MaterialTheme.spacing.extraSmall))
                    Text("Langkah Pembayaran:", style = com.example.ui.theme.DiajakDesignSystem.Typography.TitleBold, color = MaterialTheme.colorScheme.onSurface)
                    Text("1. Pilih Bayar > Multi Payment > Masukkan Kode Perusahaan.", style = com.example.ui.theme.DiajakDesignSystem.Typography.Body, color = MaterialTheme.colorScheme.onSurface)
                    Text("2. Masukkan nomor VA di atas.", style = com.example.ui.theme.DiajakDesignSystem.Typography.Body, color = MaterialTheme.colorScheme.onSurface)
                  }
                }
              }
            }

            // Fixed Bottom Action Panel for Step 2
            Box(
              modifier = Modifier
                .fillMaxWidth()
                .navigationBarsPadding()
                .padding(horizontal = MaterialTheme.spacing.screenMargin, vertical = MaterialTheme.spacing.medium)
            ) {
              Surface(
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(16.dp),
                color = Color.Transparent,
                shadowElevation = 0.dp
              ) {
                Box(
                  modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 20.dp, vertical = MaterialTheme.spacing.medium)
                ) {
                // Pay Button with Simulation Delay
                Button(
                  onClick = {
                    isPaying = true
                  },
                  enabled = !isPaying,
                  modifier = Modifier
                    .fillMaxWidth()
                    .height(52.dp),
                  shape = RoundedCornerShape(16.dp),
                  colors = ButtonDefaults.buttonColors(
                    containerColor = DiajakOrange,
                    disabledContainerColor = DiajakOrange.copy(alpha = 0.6f)
                  )
                ) {
                  if (isPaying) {
                    CircularProgressIndicator(color = Color.White, modifier = Modifier.size(20.dp), strokeWidth = 2.5.dp)
                    Spacer(modifier = Modifier.width(20.dp))
                    Text("Memverifikasi Pembayaran...", color = Color.White, style = androidx.compose.ui.text.TextStyle(fontSize = 16.sp, fontWeight = androidx.compose.ui.text.font.FontWeight.Bold, fontFamily = com.example.ui.theme.AppFontFamily))
                  } else {
                    Text("Bayar", color = Color.White, style = androidx.compose.ui.text.TextStyle(fontSize = 16.sp, fontWeight = androidx.compose.ui.text.font.FontWeight.Bold, fontFamily = com.example.ui.theme.AppFontFamily))
                  }
                }
              }
            }
            }

            // Pay Simulate effect
            if (isPaying) {
              LaunchedEffect(Unit) {
                delay(2000)
                isPaying = false
                currentStep = 3
                onBookingConfirmed(undanganCount, userNameInput, userEmailInput, userPhoneInput, bookingId, selectedDateOption)
              }
            }
          }
        }

        3 -> {
          // STEP 3: Undangan Screen
          Column(
            modifier = Modifier.fillMaxSize()
          ) {
            Column(
              modifier = Modifier
                .weight(1f)
                .verticalScroll(rememberScrollState())
                .padding(horizontal = 20.dp)
                .padding(bottom = MaterialTheme.spacing.section),
              horizontalAlignment = Alignment.CenterHorizontally
            ) {
              Spacer(modifier = Modifier.statusBarsPadding().height(80.dp))
              
              Column(
                modifier = Modifier.fillMaxWidth()
              ) {
                // Undangan Header
                Row(
                  modifier = Modifier.fillMaxWidth(),
                  horizontalArrangement = Arrangement.Start,
                  verticalAlignment = Alignment.CenterVertically
                ) {
                  Row(verticalAlignment = Alignment.CenterVertically) {
                    Icon(
                      imageVector = Icons.Outlined.CheckCircle,
                      contentDescription = "OK",
                      tint = Color(0xFF00B368),
                      modifier = Modifier.size(20.dp)
                    )
                    Spacer(modifier = Modifier.width(6.dp))
                    Text(
                      text = "TERKONFIRMASI",
                      color = Color(0xFF00B368),
                      style = com.example.ui.theme.DiajakDesignSystem.Typography.BodyBold
                    )
                  }
                }

                Spacer(modifier = Modifier.height(MaterialTheme.spacing.medium))

                // Activity Image & Title Row
                Row(verticalAlignment = Alignment.Top) {
                  Box(
                    modifier = Modifier
                      .size(targetImageSize)
                      .clip(RoundedCornerShape(12.dp))
                  ) {
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
                        Icons.Outlined.Place,
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
                        Icons.Outlined.AccessTime,
                        contentDescription = null,
                        tint = MaterialTheme.colorScheme.onSurface,
                        modifier = Modifier.size(18.dp)
                      )
                      Spacer(modifier = Modifier.width(MaterialTheme.spacing.extraSmall))
                      Text(
                        text = selectedDateOption,
                        style = com.example.ui.theme.DiajakDesignSystem.Typography.Body,
                        color = MaterialTheme.colorScheme.onSurface,
                        maxLines = 1,
                        overflow = TextOverflow.Ellipsis
                      )
                    }
                  }
                }

                Spacer(modifier = Modifier.height(MaterialTheme.spacing.medium))

                // Details summary box
                Row(
                  modifier = Modifier.fillMaxWidth(),
                  horizontalArrangement = Arrangement.SpaceBetween
                ) {
                  Column {
                    Text("Kode Booking", style = com.example.ui.theme.DiajakDesignSystem.Typography.Body, color = MaterialTheme.colorScheme.onSurfaceVariant)
                    Spacer(modifier = Modifier.height(MaterialTheme.spacing.extraSmall))
                    Text(bookingId, style = com.example.ui.theme.DiajakDesignSystem.Typography.BodyBold, color = MaterialTheme.colorScheme.onSurface)
                  }
                  Column(horizontalAlignment = Alignment.End) {
                    Text("Jumlah Undangan", style = com.example.ui.theme.DiajakDesignSystem.Typography.Body, color = MaterialTheme.colorScheme.onSurfaceVariant)
                    Spacer(modifier = Modifier.height(MaterialTheme.spacing.extraSmall))
                    Text("$undanganCount Peserta", style = com.example.ui.theme.DiajakDesignSystem.Typography.BodyBold, color = DiajakOrange)
                  }
                }

                Spacer(modifier = Modifier.height(MaterialTheme.spacing.medium))

                // Visitor Contact Information Section
                Text(
                  text = "Informasi Peserta",
                  style = com.example.ui.theme.DiajakDesignSystem.Typography.BodyBold,
                  color = MaterialTheme.colorScheme.onSurface
                )
                Spacer(modifier = Modifier.height(MaterialTheme.spacing.medium))

                Column(
                  modifier = Modifier.fillMaxWidth(),
                  verticalArrangement = Arrangement.spacedBy(AppSpacing.Small)
                ) {
                  Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                  ) {
                    Text("Nama Lengkap", style = com.example.ui.theme.DiajakDesignSystem.Typography.Body, color = MaterialTheme.colorScheme.onSurface)
                    Text(
                      text = if (userNameInput.isNotBlank()) userNameInput else "Anggi Saputro",
                      style = com.example.ui.theme.DiajakDesignSystem.Typography.Body,
                      color = MaterialTheme.colorScheme.onSurface
                    )
                  }
                  Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                  ) {
                    Text("Alamat Email", style = com.example.ui.theme.DiajakDesignSystem.Typography.Body, color = MaterialTheme.colorScheme.onSurface)
                    Text(
                      text = if (userEmailInput.isNotBlank()) userEmailInput else "gisaput@gmail.com",
                      style = com.example.ui.theme.DiajakDesignSystem.Typography.Body,
                      color = MaterialTheme.colorScheme.onSurface
                    )
                  }
                  Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                  ) {
                    Text("Nomor Handphone", style = com.example.ui.theme.DiajakDesignSystem.Typography.Body, color = MaterialTheme.colorScheme.onSurface)
                    Text(
                      text = if (userPhoneInput.isNotBlank()) userPhoneInput else "081234567890",
                      style = com.example.ui.theme.DiajakDesignSystem.Typography.Body,
                      color = MaterialTheme.colorScheme.onSurface
                    )
                  }
                }

                Spacer(modifier = Modifier.height(MaterialTheme.spacing.medium))

                // Custom QR Code
                Column(
                  modifier = Modifier.fillMaxWidth(),
                  horizontalAlignment = Alignment.CenterHorizontally
                ) {
                  Box(
                    modifier = Modifier
                      .size(160.dp)
                      .clip(RoundedCornerShape(12.dp))
                      .border(0.dp, Color.Transparent, RoundedCornerShape(12.dp))
                      .background(Color.White)
                      .padding(20.dp),
                    contentAlignment = Alignment.Center
                  ) {
                    Canvas(modifier = Modifier.fillMaxSize()) {
                      val sizePx = size.width
                      val cols = 15
                      val cellSize = sizePx / cols

                      // Corner Finder 1
                      drawRect(androidx.compose.ui.graphics.Color(0xFF202124), Offset(0f, 0f), Size(cellSize * 4, cellSize * 4))
                      drawRect(Color.White, Offset(cellSize, cellSize), Size(cellSize * 2, cellSize * 2))
                      drawRect(androidx.compose.ui.graphics.Color(0xFF202124), Offset(cellSize * 1.5f, cellSize * 1.5f), Size(cellSize, cellSize))
                      
                      // Corner Finder 2
                      drawRect(androidx.compose.ui.graphics.Color(0xFF202124), Offset(cellSize * (cols - 4), 0f), Size(cellSize * 4, cellSize * 4))
                      drawRect(Color.White, Offset(cellSize * (cols - 3), cellSize), Size(cellSize * 2, cellSize * 2))
                      drawRect(androidx.compose.ui.graphics.Color(0xFF202124), Offset(cellSize * (cols - 2.5f), cellSize * 1.5f), Size(cellSize, cellSize))
                      
                      // Corner Finder 3
                      drawRect(androidx.compose.ui.graphics.Color(0xFF202124), Offset(0f, cellSize * (cols - 4)), Size(cellSize * 4, cellSize * 4))
                      drawRect(Color.White, Offset(cellSize, cellSize * (cols - 3)), Size(cellSize * 2, cellSize * 2))
                      drawRect(androidx.compose.ui.graphics.Color(0xFF202124), Offset(cellSize * 1.5f, cellSize * (cols - 2.5f)), Size(cellSize, cellSize))

                      for (r in 0 until cols) {
                        for (c in 0 until cols) {
                          if ((r < 4 && c < 4) || (r < 4 && c >= cols - 4) || (r >= cols - 4 && c < 4)) {
                            continue
                          }
                          if ((r * c + r * 9 + c * 17) % 3 == 0 || (r + c) % 5 == 0) {
                            drawRect(
                              color = androidx.compose.ui.graphics.Color(0xFF202124),
                              topLeft = Offset(c * cellSize, r * cellSize),
                              size = Size(cellSize, cellSize)
                            )
                          }
                        }
                      }
                    }
                  }

                  Spacer(modifier = Modifier.height(MaterialTheme.spacing.medium))
                  Text(
                    text = "Tunjukkan QR untuk Check-in",
                    style = com.example.ui.theme.DiajakDesignSystem.Typography.Body,
                    color = MaterialTheme.colorScheme.onSurface,
                    textAlign = TextAlign.Center
                  )
                }
              }

              Spacer(modifier = Modifier.height(MaterialTheme.spacing.medium))
            }

            // Sticky Bottom Bar
            Surface(
              modifier = Modifier.fillMaxWidth(),
              color = Color.Transparent,
              shadowElevation = 0.dp
            ) {
              Column(
                modifier = Modifier
                  .fillMaxWidth()
                  .padding(start = 20.dp, end = 20.dp, top = MaterialTheme.spacing.medium, bottom = MaterialTheme.spacing.medium)
                  .navigationBarsPadding(),
                verticalArrangement = Arrangement.spacedBy(AppSpacing.Medium)
              ) {
                // Save to Gallery Button
                OutlinedButton(
                  onClick = {
                    Toast.makeText(context, "Undangan berhasil disimpan ke galeri ponsel! 📸", Toast.LENGTH_LONG).show()
                  },
                  modifier = Modifier
                    .fillMaxWidth()
                    .height(52.dp),
                  shape = RoundedCornerShape(16.dp),
                  border = BorderStroke(1.5.dp, DiajakOrange)
                ) {
                  Text("Simpan", color = DiajakOrange, style = androidx.compose.ui.text.TextStyle(fontSize = 16.sp, fontWeight = androidx.compose.ui.text.font.FontWeight.Bold, fontFamily = com.example.ui.theme.AppFontFamily))
                }

                // Return to Undangans Button
                Button(
                  onClick = {
                    onViewAllUndangans()
                  },
                  modifier = Modifier
                    .fillMaxWidth()
                    .height(52.dp),
                  shape = RoundedCornerShape(16.dp),
                  colors = ButtonDefaults.buttonColors(containerColor = DiajakOrange)
                ) {
                  Text("Lihat", color = Color.White, style = androidx.compose.ui.text.TextStyle(fontSize = 16.sp, fontWeight = androidx.compose.ui.text.font.FontWeight.Bold, fontFamily = com.example.ui.theme.AppFontFamily))
                }
              }
            }
          }
        }
      }
    }

    // 2. Glass Header Layer (for all steps)
    DiajakGlassHeader(
      hazeState = hazeState,
      modifier = Modifier.align(Alignment.TopCenter).zIndex(10f)
    ) {
      Box(
        modifier = Modifier.fillMaxWidth().statusBarsPadding().height(56.dp).padding(horizontal = 20.dp)
      ) {
        // Back Button
        IconButton(
          onClick = {
            if (currentStep == 2) {
              currentStep = 1
            } else {
              onDismiss()
            }
          },
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
        val headerTitle = when (currentStep) {
          1 -> "Ikuti Aktivitas"
          2 -> "Pembayaran"
          else -> "E-Tiket"
        }
        Text(
          text = headerTitle,
          style = com.example.ui.theme.DiajakDesignSystem.Typography.Headline,
          color = MaterialTheme.colorScheme.onSurface,
          textAlign = TextAlign.Center,
          modifier = Modifier.align(Alignment.Center).fillMaxWidth()
        )
      }
    }
  }
}

@Composable
fun PaymentMethodItem(
  title: String,
  subtitle: String,
  icon: androidx.compose.ui.graphics.vector.ImageVector,
  selected: Boolean,
  onClick: () -> Unit,
  content: @Composable (() -> Unit)? = null
) {
  Surface(
    modifier = Modifier
      .fillMaxWidth()
      .clickable { onClick() },
    shape = RoundedCornerShape(20.dp),
    color = Color.White,
    border = BorderStroke(
      width = if (selected) 2.dp else 0.dp,
      color = if (selected) DiajakOrange else Color.Transparent
    ),
    shadowElevation = 0.dp
  ) {
    Column {
      Row(
        modifier = Modifier
          .fillMaxWidth()
          .padding(MaterialTheme.spacing.large),
        verticalAlignment = Alignment.CenterVertically
      ) {
        Box(
          modifier = Modifier
            .size(40.dp)
            .clip(RoundedCornerShape(10.dp))
            .background(if (selected) DiajakOrange.copy(alpha = 0.12f) else Color(0xFFEDF2F7)),
          contentAlignment = Alignment.Center
        ) {
          Icon(
            imageVector = icon,
            contentDescription = null,
            tint = if (selected) DiajakOrange else MaterialTheme.colorScheme.onSurfaceVariant,
            modifier = Modifier.size(20.dp)
          )
        }
        Spacer(modifier = Modifier.width(20.dp))
        Column(modifier = Modifier.weight(1f)) {
          Text(title, style = com.example.ui.theme.DiajakDesignSystem.Typography.BodyBold.copy(fontSize = 16.sp), color = MaterialTheme.colorScheme.onSurface)
          if (subtitle.isNotEmpty()) {
            Spacer(modifier = Modifier.height(MaterialTheme.spacing.extraSmall))
            Text(subtitle, style = com.example.ui.theme.DiajakDesignSystem.Typography.Body, color = MaterialTheme.colorScheme.onSurfaceVariant)
          }
        }
        RadioButton(
          selected = selected,
          onClick = onClick,
          colors = RadioButtonDefaults.colors(selectedColor = DiajakOrange)
        )
      }
      
      if (selected && content != null) {
        content()
      }
    }
  }
}

data class BookingDateOption(
  val dayName: String,
  val dateFormatted: String,
  val timeText: String,
  val fullDateText: String
)

fun generateBookingDateOptions(schedule: String): List<BookingDateOption> {
  val baseToday = java.time.LocalDate.now().let {
    if (it.isBefore(java.time.LocalDate.of(2026, 7, 12))) {
      java.time.LocalDate.of(2026, 7, 12)
    } else {
      it
    }
  }

  val timePart = if (schedule.contains("•")) {
    schedule.split("•")[1].trim()
  } else {
    "10.00 WIB"
  }

  val dayNamesIndo = listOf("Senin", "Selasa", "Rabu", "Kamis", "Jumat", "Sabtu", "Minggu")
  val monthNamesIndo = listOf("Jan", "Feb", "Mar", "Apr", "Mei", "Jun", "Jul", "Ags", "Sep", "Okt", "Nov", "Des")

  val options = mutableListOf<BookingDateOption>()

  if (schedule.isNotBlank()) {
    val datePartRaw = schedule.split("•").firstOrNull()?.trim() ?: ""
    val dateStrings = datePartRaw.split(";")
    for (ds in dateStrings) {
      val trimmed = ds.trim()
      if (trimmed.isNotEmpty()) {
        val parts = trimmed.split(",")
        val dayName = if (parts.size > 1) parts[0].trim() else ""
        val dateText = if (parts.size > 1) parts[1].trim() else trimmed

        options.add(
          BookingDateOption(
            dayName = if (dayName.isNotEmpty()) dayName else "Hari Aktivitas",
            dateFormatted = dateText,
            timeText = timePart,
            fullDateText = if (dayName.isNotEmpty()) "$dayName, $dateText • $timePart" else "$dateText • $timePart"
          )
        )
      }
    }
  }

  if (options.size < 3) {
    val offsets = listOf(0L, 1L, 2L, 3L, 5L, 7L)
    for (offset in offsets) {
      val targetDate = baseToday.plusDays(offset)
      val dayName = dayNamesIndo[targetDate.dayOfWeek.value - 1]
      val monthName = monthNamesIndo[targetDate.monthValue - 1]
      val dateFormatted = "${targetDate.dayOfMonth} $monthName ${targetDate.year}"
      val fullText = "$dayName, $dateFormatted • $timePart"

      if (options.none { it.dateFormatted == dateFormatted || it.fullDateText == fullText }) {
        options.add(
          BookingDateOption(
            dayName = dayName,
            dateFormatted = dateFormatted,
            timeText = timePart,
            fullDateText = fullText
          )
        )
      }
    }
  }

  return options.take(5)
}
