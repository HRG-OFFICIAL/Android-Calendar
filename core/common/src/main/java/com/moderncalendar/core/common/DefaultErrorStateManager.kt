package com.moderncalendar.core.common

import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow

/**
 * Default implementation of error state management for ViewModels
 */
class DefaultErrorStateManager {
    private val _errorState = MutableStateFlow<CalendarError?>(null)
    val errorState: StateFlow<CalendarError?> = _errorState.asStateFlow()

    private var lastFailedOperation: (suspend () -> Unit)? = null

    /**
     * Handle a CalendarError
     */
    fun handleError(error: CalendarError) {
        ErrorHandler.logError(error = error)
        _errorState.value = error
    }

    /**
     * Handle a generic throwable by converting it to CalendarError
     */
    fun handleError(throwable: Throwable) {
        val calendarError = ErrorHandler.handleException(throwable)
        handleError(calendarError)
    }

    /**
     * Clear the current error state
     */
    fun clearError() {
        _errorState.value = null
        lastFailedOperation = null
    }

    /**
     * Set the last failed operation for retry functionality
     */
    fun setLastFailedOperation(operation: suspend () -> Unit) {
        lastFailedOperation = operation
    }

    /**
     * Retry the last failed operation if available
     */
    suspend fun retryLastOperation() {
        lastFailedOperation?.let { operation ->
            clearError()
            try {
                operation()
            } catch (e: Exception) {
                handleError(e)
            }
        }
    }

    /**
     * Check if there's a current error
     */
    fun hasError(): Boolean = _errorState.value != null

    /**
     * Get the current error
     */
    fun getCurrentError(): CalendarError? = _errorState.value

    /**
     * Check if the current error can be retried
     */
    fun canRetry(): Boolean {
        return _errorState.value?.canRetry() == true && lastFailedOperation != null
    }

    /**
     * Get recovery actions for the current error
     */
    fun getRecoveryActions(): List<RecoveryAction> {
        return _errorState.value?.let { ErrorHandler.getRecoveryActions(it) } ?: emptyList()
    }

    /**
     * Execute an operation with automatic error handling
     */
    suspend fun executeWithErrorHandling(operation: suspend () -> Unit) {
        try {
            clearError()
            setLastFailedOperation(operation)
            operation()
        } catch (e: Exception) {
            handleError(e)
        }
    }
}
