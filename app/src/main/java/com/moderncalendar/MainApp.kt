package com.moderncalendar

import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.hilt.navigation.compose.hiltViewModel
import com.moderncalendar.core.auth.AuthRepository
import com.moderncalendar.core.common.Result
import com.moderncalendar.ui.theme.ModernCalendarTheme
import com.moderncalendar.feature.auth.AuthScreen
import com.moderncalendar.feature.auth.AuthViewModel
import com.moderncalendar.navigation.ModernCalendarNavigation

@Composable
fun MainApp(
    modifier: Modifier = Modifier
) {
    val authViewModel: AuthViewModel = hiltViewModel()
    val uiState by authViewModel.uiState.collectAsState()
    
    ModernCalendarTheme {
        if (uiState.isAuthenticated) {
            ModernCalendarNavigation()
        } else {
            AuthScreen(
                onAuthSuccess = {
                    // Navigation will be handled by the state change
                }
            )
        }
    }
}
