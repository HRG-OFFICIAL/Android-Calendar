package com.moderncalendar.core.common

import android.util.Log

/**
 * Central error handling utility for the calendar app
 */
object ErrorHandler {
    private const val DEFAULT_TAG = "CalendarApp"

    /**
     * Convert a generic throwable to a CalendarError
     */
    fun handleException(throwable: Throwable): CalendarError {
        return when (throwable) {
            is CalendarError -> throwable
            is java.net.UnknownHostException -> CalendarError.NetworkError.noConnection()
            is java.net.SocketTimeoutException -> CalendarError.NetworkError.timeout()
            is java.net.ConnectException -> CalendarError.NetworkError.noConnection()
            is java.io.IOException ->
                CalendarError.NetworkError(
                    errorMessage = "Network communication failed",
                    cause = throwable,
                )
            is IllegalArgumentException ->
                CalendarError.ValidationError(
                    errorMessage = throwable.message ?: "Invalid input provided",
                    cause = throwable,
                )
            is SecurityException ->
                CalendarError.SecurityError(
                    errorMessage = throwable.message ?: "Security error occurred",
                    cause = throwable,
                )
            else -> CalendarError.UnknownError.fromThrowable(throwable)
        }
    }

    /**
     * Get appropriate recovery actions for a given error
     */
    fun getRecoveryActions(error: CalendarError): List<RecoveryAction> {
        return when (error) {
            is CalendarError.NetworkError -> getNetworkErrorActions(error)
            is CalendarError.DatabaseError -> getDatabaseErrorActions(error)
            is CalendarError.ValidationError -> getValidationErrorActions(error)
            is CalendarError.SecurityError -> getSecurityErrorActions(error)
            is CalendarError.SyncError -> getSyncErrorActions(error)
            is CalendarError.UnknownError -> getUnknownErrorActions(error)
        }
    }

    /**
     * Log error with appropriate level based on severity
     */
    fun logError(
        tag: String = DEFAULT_TAG,
        error: CalendarError,
        additionalContext: Map<String, Any> = emptyMap(),
    ) {
        val logMessage =
            buildString {
                append("Error: ${error.errorMessage}")
                if (error.context.isNotEmpty()) {
                    append(" | Context: ${error.context}")
                }
                if (additionalContext.isNotEmpty()) {
                    append(" | Additional: $additionalContext")
                }
            }

        when (error.getSeverity()) {
            ErrorSeverity.LOW -> Log.i(tag, logMessage, error.cause)
            ErrorSeverity.MEDIUM -> Log.w(tag, logMessage, error.cause)
            ErrorSeverity.HIGH -> Log.e(tag, logMessage, error.cause)
        }
    }

    /**
     * Log a generic throwable
     */
    fun logError(
        tag: String = DEFAULT_TAG,
        message: String,
        throwable: Throwable? = null,
        context: Map<String, Any> = emptyMap(),
    ) {
        val calendarError = throwable?.let { handleException(it) }
        val logMessage =
            buildString {
                append(message)
                if (context.isNotEmpty()) {
                    append(" | Context: $context")
                }
            }

        if (calendarError != null) {
            logError(tag, calendarError)
        } else {
            Log.e(tag, logMessage, throwable)
        }
    }

    private fun getNetworkErrorActions(error: CalendarError.NetworkError): List<RecoveryAction> {
        return buildList {
            if (error.canRetry()) {
                add(RecoveryAction.Retry())
            }
            if (error.isConnectivityIssue) {
                add(RecoveryAction.CheckConnection())
            }
            add(RecoveryAction.Refresh())
            add(RecoveryAction.Dismiss())
        }
    }

    private fun getDatabaseErrorActions(error: CalendarError.DatabaseError): List<RecoveryAction> {
        return buildList {
            if (error.canRetry()) {
                add(RecoveryAction.Retry())
            }
            add(RecoveryAction.Refresh())
            add(RecoveryAction.ClearCache())
            add(RecoveryAction.ContactSupport())
            add(RecoveryAction.Dismiss())
        }
    }

    private fun getValidationErrorActions(error: CalendarError.ValidationError): List<RecoveryAction> {
        return listOf(
            RecoveryAction.Dismiss(),
        )
    }

    private fun getSecurityErrorActions(error: CalendarError.SecurityError): List<RecoveryAction> {
        return buildList {
            error.permission?.let { permission ->
                add(RecoveryAction.RequestPermission(permission = permission))
            }
            add(RecoveryAction.GoToSettings(settingsType = SettingsType.PERMISSIONS))
            add(RecoveryAction.Dismiss())
        }
    }

    private fun getSyncErrorActions(error: CalendarError.SyncError): List<RecoveryAction> {
        return buildList {
            when (error.syncType) {
                "CONFLICT" -> {
                    add(
                        RecoveryAction.ResolveSyncConflict(
                            conflictResolution = ConflictResolution.MANUAL,
                        ),
                    )
                }
                else -> {
                    if (error.canRetry()) {
                        add(RecoveryAction.Retry())
                    }
                    add(RecoveryAction.ForceSync())
                }
            }
            add(RecoveryAction.GoToSettings(settingsType = SettingsType.SYNC))
            add(RecoveryAction.Dismiss())
        }
    }

    private fun getUnknownErrorActions(error: CalendarError.UnknownError): List<RecoveryAction> {
        return buildList {
            add(RecoveryAction.Retry())
            add(RecoveryAction.Refresh())
            add(RecoveryAction.ContactSupport())
            add(RecoveryAction.Dismiss())
        }
    }

    /**
     * Check if an error is critical and requires immediate attention
     */
    fun isCriticalError(error: CalendarError): Boolean {
        return error.getSeverity() == ErrorSeverity.HIGH
    }

    /**
     * Get user-friendly error message with context
     */
    fun getDisplayMessage(error: CalendarError): String {
        return when (error) {
            is CalendarError.NetworkError -> {
                if (error.isConnectivityIssue) {
                    "Please check your internet connection and try again."
                } else {
                    "Network error occurred. ${error.getUserMessage()}"
                }
            }
            is CalendarError.DatabaseError -> {
                "Data error occurred. Please try refreshing or contact support if the problem persists."
            }
            is CalendarError.ValidationError -> {
                error.getUserMessage()
            }
            is CalendarError.SecurityError -> {
                "Permission required. ${error.getUserMessage()}"
            }
            is CalendarError.SyncError -> {
                "Sync error occurred. ${error.getUserMessage()}"
            }
            is CalendarError.UnknownError -> {
                "An unexpected error occurred. Please try again or contact support."
            }
        }
    }
}
