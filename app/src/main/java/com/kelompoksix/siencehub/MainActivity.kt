package com.kelompoksix.siencehub

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import com.kelompoksix.siencehub.ui.navigation.AppNavigation
import com.kelompoksix.siencehub.ui.theme.SienceHubTheme
import com.kelompoksix.siencehub.utils.ThemePreferences

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()

        val themePreferences = ThemePreferences(this)

        setContent {
            val isDarkMode = themePreferences.isDarkMode
            SienceHubTheme(darkTheme = isDarkMode) {
                AppNavigation(
                    isDarkMode = isDarkMode,
                    onDarkModeChanged = { enabled ->
                        themePreferences.updateDarkMode(enabled)
                    }
                )
            }
        }
    }
}
