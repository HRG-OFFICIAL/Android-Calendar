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
