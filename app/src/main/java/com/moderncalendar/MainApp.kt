package com.moderncalendar

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.lifecycle.viewmodel.compose.viewModel
import com.moderncalendar.core.ui.theme.ModernCalendarTheme
import com.moderncalendar.core.ui.theme.ThemeViewModel
import com.moderncalendar.navigation.ModernCalendarNavigation

@Composable
fun MainApp(
    modifier: Modifier = Modifier
) {
    val themeViewModel: ThemeViewModel = viewModel()
    val themeMode by themeViewModel.themeMode.collectAsState()
    
    ModernCalendarTheme(themeMode = themeMode) {
        // Directly show the main navigation without authentication
        ModernCalendarNavigation()
    }
}
