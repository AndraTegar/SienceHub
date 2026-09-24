package com.kelompoksix.siencehub // Sesuaikan dengan nama package lo

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.material3.MaterialTheme
import com.kelompoksix.siencehub.ui.screens.MateriScreen

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            MaterialTheme {
                MateriScreen()
            }
        }
    }
}