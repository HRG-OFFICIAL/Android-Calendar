package com.moderncalendar

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.runtime.*
import androidx.hilt.navigation.compose.hiltViewModel
import com.moderncalendar.core.ui.theme.ModernCalendarTheme
import com.moderncalendar.feature.auth.AuthScreen
import com.moderncalendar.navigation.ModernCalendarNavigation
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            MainApp()
        }
    }
}

@Composable
fun MainApp(
    viewModel: MainActivityViewModel = hiltViewModel()
) {
    val isUserSignedIn by viewModel.isUserSignedIn.collectAsState()
    
    ModernCalendarTheme {
        if (isUserSignedIn) {
            ModernCalendarNavigation()
        } else {
            AuthScreen()
        }
    }
}
