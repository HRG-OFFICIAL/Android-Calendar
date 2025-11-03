package com.moderncalendar.core.common

/**
 * A generic class that holds a value or an error.
 */
sealed class Result<out T> {
    data class Success<out T>(val data: T) : Result<T>()

    data class Error(val exception: Throwable) : Result<Nothing>()

    object Loading : Result<Nothing>()
}

/**
 * Returns true if this is a Success result.
 */
val Result<*>.isSuccess: Boolean
    get() = this is Result.Success

/**
 * Returns true if this is an Error result.
 */
val Result<*>.isError: Boolean
    get() = this is Result.Error

/**
 * Returns true if this is a Loading result.
 */
val Result<*>.isLoading: Boolean
    get() = this is Result.Loading

/**
 * Returns the data if this is a Success result, null otherwise.
 */
fun <T> Result<T>.getDataOrNull(): T? {
    return if (this is Result.Success) data else null
}

/**
 * Returns the exception if this is an Error result, null otherwise.
 */
fun <T> Result<T>.getExceptionOrNull(): Throwable? {
    return if (this is Result.Error) exception else null
}

/**
 * Maps the data if this is a Success result, otherwise returns the same result type.
 */
inline fun <T, R> Result<T>.map(transform: (T) -> R): Result<R> {
    return when (this) {
        is Result.Success -> Result.Success(transform(data))
        is Result.Error -> this
        is Result.Loading -> this
    }
}

/**
 * Maps the data if this is a Success result, otherwise returns the same result type.
 */
inline fun <T, R> Result<T>.flatMap(transform: (T) -> Result<R>): Result<R> {
    return when (this) {
        is Result.Success -> transform(data)
        is Result.Error -> this
        is Result.Loading -> this
    }
}

/**
 * Enhanced Result extensions for better error handling
 */

/**
 * Execute action on success and return the same result
 */
inline fun <T> Result<T>.onSuccess(action: (T) -> Unit): Result<T> {
    if (this is Result.Success) {
        action(data)
    }
    return this
}

/**
 * Execute action on error and return the same result
 */
inline fun <T> Result<T>.onError(action: (Throwable) -> Unit): Result<T> {
    if (this is Result.Error) {
        action(exception)
    }
    return this
}

/**
 * Execute action on loading and return the same result
 */
inline fun <T> Result<T>.onLoading(action: () -> Unit): Result<T> {
    if (this is Result.Loading) {
        action()
    }
    return this
}

/**
 * Returns the CalendarError if this is an Error result, null otherwise
 */
fun <T> Result<T>.getErrorOrNull(): CalendarError? {
    return if (this is Result.Error) {
        exception as? CalendarError ?: ErrorHandler.handleException(exception)
    } else {
        null
    }
}

/**
 * Returns the error message if this is an Error result, null otherwise
 */
fun <T> Result<T>.getErrorMessage(): String? {
    return getErrorOrNull()?.getUserMessage()
}

/**
 * Returns true if this error is recoverable
 */
fun <T> Result<T>.isRecoverable(): Boolean {
    return getErrorOrNull()?.canRetry() ?: false
}

/**
 * Get available recovery actions for this error result
 */
fun <T> Result<T>.getRecoveryActions(): List<RecoveryAction> {
    return getErrorOrNull()?.let { ErrorHandler.getRecoveryActions(it) } ?: emptyList()
}
