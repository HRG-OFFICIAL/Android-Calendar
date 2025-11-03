package com.moderncalendar.core.common

/**
 * Sealed class representing different types of errors that can occur in the calendar app
 */
sealed class CalendarError(
    open val errorMessage: String,
    override val cause: Throwable? = null,
    open val context: Map<String, Any> = emptyMap(),
) : Throwable(errorMessage, cause) {
    /**
     * Network-related errors
     */
    data class NetworkError(
        override val errorMessage: String,
        override val cause: Throwable? = null,
        override val context: Map<String, Any> = emptyMap(),
        val isConnectivityIssue: Boolean = false,
    ) : CalendarError(errorMessage, cause, context) {
        companion object {
            fun noConnection(): NetworkError =
                NetworkError(
                    errorMessage = "No internet connection available",
                    isConnectivityIssue = true,
                )

            fun timeout(): NetworkError =
                NetworkError(
                    errorMessage = "Request timed out",
                )

            fun serverError(code: Int): NetworkError =
                NetworkError(
                    errorMessage = "Server error occurred",
                    context = mapOf("statusCode" to code),
                )
        }
    }

    /**
     * Database-related errors
     */
    data class DatabaseError(
        override val errorMessage: String,
        override val cause: Throwable? = null,
        override val context: Map<String, Any> = emptyMap(),
        val operation: String? = null,
    ) : CalendarError(errorMessage, cause, context) {
        companion object {
            fun queryFailed(table: String): DatabaseError =
                DatabaseError(
                    errorMessage = "Failed to query data from $table",
                    operation = "QUERY",
                    context = mapOf("table" to table),
                )

            fun insertFailed(table: String): DatabaseError =
                DatabaseError(
                    errorMessage = "Failed to insert data into $table",
                    operation = "INSERT",
                    context = mapOf("table" to table),
                )

            fun updateFailed(table: String): DatabaseError =
                DatabaseError(
                    errorMessage = "Failed to update data in $table",
                    operation = "UPDATE",
                    context = mapOf("table" to table),
                )

            fun deleteFailed(table: String): DatabaseError =
                DatabaseError(
                    errorMessage = "Failed to delete data from $table",
                    operation = "DELETE",
                    context = mapOf("table" to table),
                )
        }
    }

    /**
     * Validation-related errors
     */
    data class ValidationError(
        override val errorMessage: String,
        override val cause: Throwable? = null,
        override val context: Map<String, Any> = emptyMap(),
        val field: String? = null,
    ) : CalendarError(errorMessage, cause, context) {
        companion object {
            fun requiredField(fieldName: String): ValidationError =
                ValidationError(
                    errorMessage = "$fieldName is required",
                    field = fieldName,
                )

            fun invalidFormat(
                fieldName: String,
                expectedFormat: String,
            ): ValidationError =
                ValidationError(
                    errorMessage = "$fieldName has invalid format. Expected: $expectedFormat",
                    field = fieldName,
                    context = mapOf("expectedFormat" to expectedFormat),
                )

            fun invalidRange(
                fieldName: String,
                min: String,
                max: String,
            ): ValidationError =
                ValidationError(
                    errorMessage = "$fieldName is out of valid range ($min - $max)",
                    field = fieldName,
                    context = mapOf("min" to min, "max" to max),
                )
        }
    }

    /**
     * Security and permission-related errors
     */
    data class SecurityError(
        override val errorMessage: String,
        override val cause: Throwable? = null,
        override val context: Map<String, Any> = emptyMap(),
        val permission: String? = null,
    ) : CalendarError(errorMessage, cause, context) {
        companion object {
            fun permissionDenied(permission: String): SecurityError =
                SecurityError(
                    errorMessage = "Permission denied: $permission",
                    permission = permission,
                )

            fun authenticationFailed(): SecurityError =
                SecurityError(
                    errorMessage = "Authentication failed",
                )
        }
    }

    /**
     * Synchronization-related errors
     */
    data class SyncError(
        override val errorMessage: String,
        override val cause: Throwable? = null,
        override val context: Map<String, Any> = emptyMap(),
        val syncType: String? = null,
    ) : CalendarError(errorMessage, cause, context) {
        companion object {
            fun conflictDetected(): SyncError =
                SyncError(
                    errorMessage = "Sync conflict detected",
                    syncType = "CONFLICT",
                )

            fun syncFailed(type: String): SyncError =
                SyncError(
                    errorMessage = "Failed to sync $type",
                    syncType = type,
                )
        }
    }

    /**
     * Unknown or unexpected errors
     */
    data class UnknownError(
        override val errorMessage: String,
        override val cause: Throwable? = null,
        override val context: Map<String, Any> = emptyMap(),
    ) : CalendarError(errorMessage, cause, context) {
        companion object {
            fun fromThrowable(throwable: Throwable): UnknownError =
                UnknownError(
                    errorMessage = throwable.message ?: "An unexpected error occurred",
                    cause = throwable,
                )
        }
    }

    /**
     * Get user-friendly error message
     */
    fun getUserMessage(): String = errorMessage

    /**
     * Check if this error can be retried
     */
    fun canRetry(): Boolean =
        when (this) {
            is NetworkError -> isConnectivityIssue || errorMessage.contains("timeout", ignoreCase = true)
            is DatabaseError -> operation in listOf("QUERY", "INSERT", "UPDATE", "DELETE")
            is SyncError -> syncType != "CONFLICT"
            else -> false
        }

    /**
     * Get error severity level
     */
    fun getSeverity(): ErrorSeverity =
        when (this) {
            is SecurityError -> ErrorSeverity.HIGH
            is DatabaseError -> ErrorSeverity.HIGH
            is NetworkError -> if (isConnectivityIssue) ErrorSeverity.MEDIUM else ErrorSeverity.LOW
            is ValidationError -> ErrorSeverity.LOW
            is SyncError -> ErrorSeverity.MEDIUM
            is UnknownError -> ErrorSeverity.HIGH
        }
}

/**
 * Error severity levels
 */
enum class ErrorSeverity {
    LOW, // User can continue with limited functionality
    MEDIUM, // Some features may be unavailable
    HIGH, // Critical error, app functionality severely impacted
}
