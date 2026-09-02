package com.example

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.core.splashscreen.SplashScreen.Companion.installSplashScreen
import com.example.core.navigation.AppNavigation
import com.example.ui.theme.ScanXTheme

import com.tom_roush.pdfbox.android.PDFBoxResourceLoader

class MainActivity : ComponentActivity() {
  override fun onCreate(savedInstanceState: Bundle?) {
    val splashScreen = installSplashScreen()
    super.onCreate(savedInstanceState)
    PDFBoxResourceLoader.init(applicationContext)
    enableEdgeToEdge()
    setContent {
      val systemDark = isSystemInDarkTheme()
      var isDarkTheme by rememberSaveable { mutableStateOf(systemDark) }

      ScanXTheme(darkTheme = isDarkTheme) {
        AppNavigation(
          isDarkTheme = isDarkTheme,
          onThemeToggle = { isDarkTheme = !isDarkTheme }
        )
      }
    }
  }
}
