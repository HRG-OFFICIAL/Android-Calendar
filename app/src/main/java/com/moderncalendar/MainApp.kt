package com.moderncalendar

import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.hilt.navigation.compose.hiltViewModel
import com.moderncalendar.core.auth.AuthRepository
import com.moderncalendar.core.ui.theme.ModernCalendarTheme
import com.moderncalendar.feature.auth.AuthScreen
import com.moderncalendar.navigation.ModernCalendarNavigation
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@Composable
fun MainApp(
    modifier: Modifier = Modifier,
    authRepository: AuthRepository
) {
    val isUserSignedIn by remember { 
        derivedStateOf { authRepository.isUserSignedIn } 
    }
    
    ModernCalendarTheme {
        if (isUserSignedIn) {
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
