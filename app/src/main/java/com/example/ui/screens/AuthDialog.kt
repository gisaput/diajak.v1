package com.example.ui.screens
import com.example.ui.theme.spacing
import androidx.compose.material3.MaterialTheme

import androidx.compose.material.icons.outlined.*

import androidx.compose.foundation.gestures.detectTapGestures
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Dialog
import androidx.compose.ui.window.DialogProperties
import com.example.ui.viewmodel.DiajakViewModel
 
 @OptIn(ExperimentalMaterial3Api::class)
 @Composable
 fun AuthDialog(
   viewModel: DiajakViewModel,
   onDismiss: () -> Unit,
   onLoginSuccess: () -> Unit = {}
 ) {
   val focusManager = androidx.compose.ui.platform.LocalFocusManager.current
 
   Dialog(
     onDismissRequest = {
       focusManager.clearFocus()
       onDismiss()
     },
     properties = DialogProperties(
       usePlatformDefaultWidth = false,
       decorFitsSystemWindows = false
     )
   ) {
     Surface(
       shape = RoundedCornerShape(16.dp),
       color = Color.White,
       modifier = Modifier
         .fillMaxWidth(0.92f)
         .padding(horizontal = 4.dp)
         .imePadding()
    ) {
      Column(
        modifier = Modifier
          .fillMaxWidth()
          .pointerInput(Unit) {
            detectTapGestures(
              onTap = {
                focusManager.clearFocus()
              }
            )
          }
          .padding(MaterialTheme.spacing.large),
        horizontalAlignment = Alignment.CenterHorizontally
      ) {
        // Close Button
        Row(
          modifier = Modifier.fillMaxWidth(),
          horizontalArrangement = Arrangement.End,
          verticalAlignment = Alignment.CenterVertically
        ) {
          IconButton(onClick = {
            focusManager.clearFocus()
            onDismiss()
          }) {
            Icon(Icons.Outlined.Close, contentDescription = "Close", tint = MaterialTheme.colorScheme.onSurface)
          }
        }

        Spacer(modifier = Modifier.height(20.dp))

        // Reusable auth content with Phone, Email, Google, Apple options
        AuthContent(
          viewModel = viewModel,
          onSuccess = {
            focusManager.clearFocus()
            onLoginSuccess()
            onDismiss()
          },
          isInline = false
        )
      }
    }
  }
}
