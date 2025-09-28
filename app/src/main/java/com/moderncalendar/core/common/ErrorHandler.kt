package com.moderncalendar.core.common

import android.content.Context
import android.util.Log
import androidx.compose.material3.SnackbarHostState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import kotlinx.coroutines.launch

/**
 * Centralized error handling for the application
 */
object ErrorHandler {
    
    private const val TAG = "ErrorHandler"
    
    /**
     * Handle different types of errors and provide user-friendly messages
     */
    fun getErrorMessage(error: Throwable): String {
        return when (error) {
            is java.net.UnknownHostException -> "No internet connection. Please check your network."
            is java.net.SocketTimeoutException -> "Request timed out. Please try again."
            is java.io.IOException -> "Network error. Please check your connection."
            is SecurityException -> "Permission denied. Please check app permissions."
            is IllegalArgumentException -> "Invalid data provided. Please check your input."
            is UnsupportedOperationException -> "This operation is not supported."
            else -> "An unexpected error occurred: ${error.message ?: "Unknown error"}"
        }
    }
    
    /**
     * Log error with appropriate level
     */
    fun logError(tag: String, message: String, throwable: Throwable? = null) {
        when (throwable) {
            is SecurityException -> Log.w(tag, message, throwable)
            is IllegalArgumentException -> Log.w(tag, message, throwable)
            else -> Log.e(tag, message, throwable)
        }
    }
    
    /**
     * Check if error is retryable
     */
    fun isRetryable(error: Throwable): Boolean {
        return when (error) {
            is java.net.UnknownHostException,
            is java.net.SocketTimeoutException,
            is java.io.IOException -> true
            else -> false
        }
    }
}

/**
 * Composable for showing error messages with retry functionality
 */
@Composable
fun ErrorMessageWithRetry(
    error: String,
    isRetryable: Boolean = false,
    onRetry: (() -> Unit)? = null,
    snackbarHostState: SnackbarHostState = remember { SnackbarHostState() }
) {
    val coroutineScope = rememberCoroutineScope()
    
    if (error.isNotEmpty()) {
        coroutineScope.launch {
            val action = if (isRetryable && onRetry != null) {
                "Retry"
            } else {
                "Dismiss"
            }
            
            snackbarHostState.showSnackbar(
                message = error,
                actionLabel = action,
                duration = androidx.compose.material3.SnackbarDuration.Long
            )
        }
    }
}

/**
 * Result wrapper with retry functionality
 */
sealed class ResultWithRetry<out T> {
    data class Success<T>(val data: T) : ResultWithRetry<T>()
    data class Error(val exception: Throwable, val isRetryable: Boolean = false) : ResultWithRetry<Nothing>()
    object Loading : ResultWithRetry<Nothing>()
}

/**
 * Extension function to convert Result to ResultWithRetry
 */
fun <T> Result<T>.toResultWithRetry(): ResultWithRetry<T> {
    return when (this) {
        is Result.Success -> ResultWithRetry.Success(data)
        is Result.Error -> ResultWithRetry.Error(exception, ErrorHandler.isRetryable(exception))
        is Result.Loading -> ResultWithRetry.Loading
    }
}
