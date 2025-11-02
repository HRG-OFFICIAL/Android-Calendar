package com.moderncalendar.core.ui.components

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import com.moderncalendar.core.common.CalendarError
import com.moderncalendar.core.common.ErrorSeverity
import com.moderncalendar.core.common.RecoveryAction

/**
 * Full-screen error display with recovery options
 */
@Composable
fun ErrorScreen(
    error: CalendarError,
    onRecoveryAction: (RecoveryAction) -> Unit,
    modifier: Modifier = Modifier,
    showIcon: Boolean = true
) {
    Column(
        modifier = modifier
            .fillMaxSize()
            .padding(24.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        if (showIcon) {
            Icon(
                imageVector = getErrorIcon(error),
                contentDescription = null,
                modifier = Modifier.size(64.dp),
                tint = getErrorColor(error.getSeverity())
            )
            
            Spacer(modifier = Modifier.height(16.dp))
        }
        
        Text(
            text = getErrorTitle(error),
            style = MaterialTheme.typography.headlineSmall,
            fontWeight = FontWeight.Bold,
            textAlign = TextAlign.Center,
            color = MaterialTheme.colorScheme.onSurface
        )
        
        Spacer(modifier = Modifier.height(8.dp))
        
        Text(
            text = error.getUserMessage(),
            style = MaterialTheme.typography.bodyLarge,
            textAlign = TextAlign.Center,
            color = MaterialTheme.colorScheme.onSurfaceVariant
        )
        
        Spacer(modifier = Modifier.height(24.dp))
        
        ErrorRecoveryActions(
            error = error,
            onRecoveryAction = onRecoveryAction,
            arrangement = Arrangement.spacedBy(8.dp)
        )
    }
}

/**
 * Inline error display for specific sections
 */
@Composable
fun ErrorCard(
    error: CalendarError,
    onRecoveryAction: (RecoveryAction) -> Unit,
    modifier: Modifier = Modifier,
    showIcon: Boolean = true,
    compact: Boolean = false
) {
    Card(
        modifier = modifier.fillMaxWidth(),
        colors = CardDefaults.cardColors(
            containerColor = getErrorBackgroundColor(error.getSeverity())
        ),
        shape = RoundedCornerShape(8.dp)
    ) {
        Column(
            modifier = Modifier.padding(16.dp)
        ) {
            Row(
                verticalAlignment = Alignment.CenterVertically
            ) {
                if (showIcon) {
                    Icon(
                        imageVector = getErrorIcon(error),
                        contentDescription = null,
                        modifier = Modifier.size(if (compact) 20.dp else 24.dp),
                        tint = getErrorColor(error.getSeverity())
                    )
                    
                    Spacer(modifier = Modifier.width(12.dp))
                }
                
                Column(
                    modifier = Modifier.weight(1f)
                ) {
                    if (!compact) {
                        Text(
                            text = getErrorTitle(error),
                            style = MaterialTheme.typography.titleSmall,
                            fontWeight = FontWeight.Medium,
                            color = MaterialTheme.colorScheme.onSurface
                        )
                        
                        Spacer(modifier = Modifier.height(4.dp))
                    }
                    
                    Text(
                        text = error.getUserMessage(),
                        style = if (compact) MaterialTheme.typography.bodyMedium else MaterialTheme.typography.bodySmall,
                        color = MaterialTheme.colorScheme.onSurfaceVariant
                    )
                }
            }
            
            if (!compact) {
                Spacer(modifier = Modifier.height(12.dp))
                
                ErrorRecoveryActions(
                    error = error,
                    onRecoveryAction = onRecoveryAction,
                    arrangement = Arrangement.spacedBy(8.dp, Alignment.End),
                    compact = true
                )
            }
        }
    }
}

/**
 * Non-intrusive error notifications
 */
@Composable
fun ErrorSnackbar(
    error: CalendarError,
    onRecoveryAction: (RecoveryAction) -> Unit,
    onDismiss: () -> Unit,
    modifier: Modifier = Modifier
) {
    Snackbar(
        modifier = modifier,
        action = {
            val primaryAction = getPrimaryRecoveryAction(error)
            if (primaryAction != null) {
                TextButton(
                    onClick = { onRecoveryAction(primaryAction) }
                ) {
                    Text(primaryAction.getDisplayText())
                }
            }
        },
        dismissAction = {
            IconButton(onClick = onDismiss) {
                Icon(
                    imageVector = Icons.Default.Close,
                    contentDescription = "Dismiss"
                )
            }
        },
        containerColor = getErrorBackgroundColor(error.getSeverity()),
        contentColor = MaterialTheme.colorScheme.onSurface
    ) {
        Text(
            text = error.getUserMessage(),
            style = MaterialTheme.typography.bodyMedium
        )
    }
}

/**
 * Combined loading and error state management
 */
@Composable
fun LoadingErrorState(
    isLoading: Boolean,
    error: CalendarError?,
    onRetry: () -> Unit,
    onRecoveryAction: (RecoveryAction) -> Unit,
    modifier: Modifier = Modifier,
    loadingContent: @Composable () -> Unit = {
        Box(
            modifier = Modifier.fillMaxSize(),
            contentAlignment = Alignment.Center
        ) {
            CircularProgressIndicator()
        }
    },
    content: @Composable () -> Unit
) {
    when {
        isLoading -> loadingContent()
        error != null -> {
            ErrorScreen(
                error = error,
                onRecoveryAction = onRecoveryAction,
                modifier = modifier
            )
        }
        else -> content()
    }
}

/**
 * Recovery actions row/column
 */
@Composable
private fun ErrorRecoveryActions(
    error: CalendarError,
    onRecoveryAction: (RecoveryAction) -> Unit,
    modifier: Modifier = Modifier,
    arrangement: Arrangement.Horizontal = Arrangement.spacedBy(8.dp),
    compact: Boolean = false
) {
    val recoveryActions = getRecoveryActionsForError(error)
    
    if (recoveryActions.isNotEmpty()) {
        Row(
            modifier = modifier,
            horizontalArrangement = arrangement
        ) {
            recoveryActions.take(if (compact) 2 else 3).forEach { action ->
                when (action) {
                    is RecoveryAction.Retry -> {
                        if (compact) {
                            TextButton(onClick = { onRecoveryAction(action) }) {
                                Text(action.getDisplayText())
                            }
                        } else {
                            Button(onClick = { onRecoveryAction(action) }) {
                                Text(action.getDisplayText())
                            }
                        }
                    }
                    is RecoveryAction.Dismiss -> {
                        OutlinedButton(onClick = { onRecoveryAction(action) }) {
                            Text(action.getDisplayText())
                        }
                    }
                    else -> {
                        OutlinedButton(onClick = { onRecoveryAction(action) }) {
                            Text(action.getDisplayText())
                        }
                    }
                }
            }
        }
    }
}

/**
 * Get appropriate icon for error type
 */
private fun getErrorIcon(error: CalendarError): ImageVector {
    return when (error) {
        is CalendarError.NetworkError -> Icons.Default.CloudOff
        is CalendarError.DatabaseError -> Icons.Default.Storage
        is CalendarError.ValidationError -> Icons.Default.Warning
        is CalendarError.SecurityError -> Icons.Default.Security
        is CalendarError.SyncError -> Icons.Default.Sync
        is CalendarError.UnknownError -> Icons.Default.Error
    }
}

/**
 * Get appropriate color for error severity
 */
@Composable
private fun getErrorColor(severity: ErrorSeverity): Color {
    return when (severity) {
        ErrorSeverity.LOW -> MaterialTheme.colorScheme.primary
        ErrorSeverity.MEDIUM -> MaterialTheme.colorScheme.tertiary
        ErrorSeverity.HIGH -> MaterialTheme.colorScheme.error
    }
}

/**
 * Get appropriate background color for error severity
 */
@Composable
private fun getErrorBackgroundColor(severity: ErrorSeverity): Color {
    return when (severity) {
        ErrorSeverity.LOW -> MaterialTheme.colorScheme.primaryContainer
        ErrorSeverity.MEDIUM -> MaterialTheme.colorScheme.tertiaryContainer
        ErrorSeverity.HIGH -> MaterialTheme.colorScheme.errorContainer
    }
}

/**
 * Get user-friendly title for error type
 */
private fun getErrorTitle(error: CalendarError): String {
    return when (error) {
        is CalendarError.NetworkError -> "Connection Problem"
        is CalendarError.DatabaseError -> "Data Error"
        is CalendarError.ValidationError -> "Invalid Input"
        is CalendarError.SecurityError -> "Permission Required"
        is CalendarError.SyncError -> "Sync Problem"
        is CalendarError.UnknownError -> "Unexpected Error"
    }
}

/**
 * Get recovery actions for error (using ErrorHandler)
 */
private fun getRecoveryActionsForError(error: CalendarError): List<RecoveryAction> {
    return com.moderncalendar.core.common.ErrorHandler.getRecoveryActions(error)
}

/**
 * Get primary recovery action for quick access
 */
private fun getPrimaryRecoveryAction(error: CalendarError): RecoveryAction? {
    return getRecoveryActionsForError(error).firstOrNull { it !is RecoveryAction.Dismiss }
}