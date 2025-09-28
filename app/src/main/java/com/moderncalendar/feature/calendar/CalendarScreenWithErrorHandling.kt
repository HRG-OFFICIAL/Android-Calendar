package com.moderncalendar.feature.calendar

import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.moderncalendar.core.common.ErrorHandler
import com.moderncalendar.core.common.ResultWithRetry
import com.moderncalendar.core.common.toResultWithRetry
import com.moderncalendar.core.data.entity.EventEntity
import kotlinx.coroutines.launch

/**
 * Enhanced Calendar Screen with comprehensive error handling
 */
@Composable
fun CalendarScreenWithErrorHandling(
    modifier: Modifier = Modifier,
    onEventClick: (String) -> Unit = {},
    onCreateEventClick: () -> Unit = {},
    onSearchClick: () -> Unit = {},
    onSettingsClick: () -> Unit = {},
    viewModel: CalendarViewModel = hiltViewModel()
) {
    val selectedDate by viewModel.selectedDate.collectAsState()
    val events by viewModel.events.collectAsState()
    val isLoading by viewModel.isLoading.collectAsState()
    
    val snackbarHostState = remember { SnackbarHostState() }
    val coroutineScope = rememberCoroutineScope()
    
    // Avoid smart casts on delegated state by binding to local val first
    val eventsLocal = events
    val eventsWithRetry = remember(eventsLocal) {
        when (eventsLocal) {
            is com.moderncalendar.core.common.Result.Success -> ResultWithRetry.Success(eventsLocal.data)
            is com.moderncalendar.core.common.Result.Error -> ResultWithRetry.Error(
                eventsLocal.exception, 
                ErrorHandler.isRetryable(eventsLocal.exception)
            )
            is com.moderncalendar.core.common.Result.Loading -> ResultWithRetry.Loading
        }
    }
    
    Box(modifier = modifier.fillMaxSize()) {
        // Main calendar content
        CalendarScreen(
            modifier = Modifier.fillMaxSize(),
            onNavigateToEventDetails = onEventClick,
            onNavigateToEventCreation = onCreateEventClick,
            onNavigateToSearch = onSearchClick,
            onNavigateToSettings = onSettingsClick,
            viewModel = viewModel
        )
        
        // Error handling overlay
        when (eventsWithRetry) {
            is ResultWithRetry.Error -> {
                ErrorOverlay(
                    error = ErrorHandler.getErrorMessage(eventsWithRetry.exception),
                    isRetryable = eventsWithRetry.isRetryable,
                    onRetry = {
                        coroutineScope.launch {
                            viewModel.selectDate(selectedDate)
                        }
                    },
                    onDismiss = {
                        // Handle dismiss if needed
                    }
                )
            }
            is ResultWithRetry.Loading -> {
                if (isLoading) {
                    LoadingOverlay()
                }
            }
            is ResultWithRetry.Success -> {
                // Success state - no overlay needed
            }
        }
        
        // Snackbar host for error messages
        SnackbarHost(
            hostState = snackbarHostState,
            modifier = Modifier.align(Alignment.BottomCenter)
        )
    }
}

@Composable
private fun ErrorOverlay(
    error: String,
    isRetryable: Boolean,
    onRetry: () -> Unit,
    onDismiss: () -> Unit,
    modifier: Modifier = Modifier
) {
    Card(
        modifier = modifier
            .fillMaxWidth()
            .padding(16.dp),
        colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.errorContainer
        )
    ) {
        Column(
            modifier = Modifier.padding(16.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text(
                text = "Error",
                style = MaterialTheme.typography.titleMedium,
                color = MaterialTheme.colorScheme.onErrorContainer
            )
            
            Spacer(modifier = Modifier.height(8.dp))
            
            Text(
                text = error,
                style = MaterialTheme.typography.bodyMedium,
                color = MaterialTheme.colorScheme.onErrorContainer
            )
            
            Spacer(modifier = Modifier.height(16.dp))
            
            Row(
                horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                if (isRetryable) {
                    Button(
                        onClick = onRetry,
                        colors = ButtonDefaults.buttonColors(
                            containerColor = MaterialTheme.colorScheme.error
                        )
                    ) {
                        Text("Retry")
                    }
                }
                
                OutlinedButton(
                    onClick = onDismiss,
                    colors = ButtonDefaults.outlinedButtonColors(
                        contentColor = MaterialTheme.colorScheme.onErrorContainer
                    )
                ) {
                    Text("Dismiss")
                }
            }
        }
    }
}

@Composable
private fun LoadingOverlay(
    modifier: Modifier = Modifier
) {
    Box(
        modifier = modifier.fillMaxSize(),
        contentAlignment = Alignment.Center
    ) {
        Card(
            colors = CardDefaults.cardColors(
                containerColor = MaterialTheme.colorScheme.surface.copy(alpha = 0.9f)
            )
        ) {
            Column(
                modifier = Modifier.padding(24.dp),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                CircularProgressIndicator()
                Spacer(modifier = Modifier.height(16.dp))
                Text(
                    text = "Loading events...",
                    style = MaterialTheme.typography.bodyMedium
                )
            }
        }
    }
}
