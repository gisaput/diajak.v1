package com.example

import androidx.compose.material3.MaterialTheme

import android.content.Intent
import android.content.pm.PackageManager
import android.os.Build
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.activity.result.contract.ActivityResultContracts
import androidx.activity.viewModels
import androidx.core.content.ContextCompat
import androidx.compose.foundation.layout.fillMaxSize
import com.example.ui.screens.MainScreen
import com.example.ui.theme.MyApplicationTheme
import com.example.ui.viewmodel.DiajakViewModel

class MainActivity : ComponentActivity() {
  private val viewModel: DiajakViewModel by viewModels()

  override fun onCreate(savedInstanceState: Bundle?) {
    super.onCreate(savedInstanceState)
    enableEdgeToEdge(
      statusBarStyle = androidx.activity.SystemBarStyle.auto(
        android.graphics.Color.TRANSPARENT,
        android.graphics.Color.TRANSPARENT
      ),
      navigationBarStyle = androidx.activity.SystemBarStyle.auto(
        android.graphics.Color.TRANSPARENT,
        android.graphics.Color.TRANSPARENT
      )
    )

    // Handle initial intent for notification routing
    handleIntent(intent)

    setContent {
      MyApplicationTheme {
        androidx.compose.material3.Surface(
          modifier = androidx.compose.ui.Modifier.fillMaxSize(),
          color = androidx.compose.ui.graphics.Color(0xFFE5E7EB)
        ) {
          MainScreen(viewModel = viewModel)
        }
      }
    }
  }

  override fun onNewIntent(intent: Intent) {
    super.onNewIntent(intent)
    handleIntent(intent)
  }

  private fun handleIntent(intent: Intent?) {
    intent?.let {
      if (it.hasExtra("tab_to_open")) {
        val tabIndex = it.getIntExtra("tab_to_open", 0)
        viewModel.setTab(tabIndex)
      }
    }
  }
}
