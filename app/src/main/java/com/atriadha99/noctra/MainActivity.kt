package com.atriadha99.noctra

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import com.atriadha99.noctra.presentation.MainApp
import com.atriadha99.noctra.presentation.theme.NoctraTheme
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        
        requestPermissions()

        setContent {
            NoctraTheme {
                MainApp()
            }
        }
    }

    private fun requestPermissions() {
        val requestPermissionLauncher = registerForActivityResult(
            androidx.activity.result.contract.ActivityResultContracts.RequestMultiplePermissions()
        ) { permissions ->
            // In a real app we'd handle denied permissions here
        }

        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.TIRAMISU) {
            requestPermissionLauncher.launch(
                arrayOf(
                    android.Manifest.permission.READ_MEDIA_AUDIO,
                    android.Manifest.permission.POST_NOTIFICATIONS
                )
            )
        } else {
            requestPermissionLauncher.launch(
                arrayOf(
                    android.Manifest.permission.READ_EXTERNAL_STORAGE
                )
            )
        }
    }
}
