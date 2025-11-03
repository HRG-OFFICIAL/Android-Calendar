package com.moderncalendar.core.common.utils

import kotlinx.coroutines.delay

/**
 * Utility functions for retry operations with backoff
 */

/**
 * Retry a suspend function with exponential backoff
 */
suspend fun <T> retryWithBackoff(
    maxRetries: Int = 3,
    initialDelayMs: Long = 1000,
    backoffMultiplier: Double = 2.0,
    operation: suspend () -> T,
): T {
    var currentDelay = initialDelayMs
    var lastException: Exception? = null

    repeat(maxRetries) { attempt ->
        try {
            return operation()
        } catch (e: Exception) {
            lastException = e
            if (attempt < maxRetries - 1) {
                delay(currentDelay)
                currentDelay = (currentDelay * backoffMultiplier).toLong()
            }
        }
    }

    throw lastException ?: Exception("Retry failed after $maxRetries attempts")
}
