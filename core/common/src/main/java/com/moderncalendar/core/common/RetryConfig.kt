package com.moderncalendar.core.common

/**
 * Configuration for retry operations
 */
data class RetryConfiguration(
    val maxRetries: Int,
    val initialDelayMs: Long,
    val maxDelayMs: Long,
    val backoffFactor: Double
)

/**
 * Predefined retry configurations for different operations
 */
object RetryConfig {
    val DATABASE = RetryConfiguration(
        maxRetries = 3,
        initialDelayMs = 500L,
        maxDelayMs = 5000L,
        backoffFactor = 2.0
    )
    
    val NETWORK = RetryConfiguration(
        maxRetries = 5,
        initialDelayMs = 1000L,
        maxDelayMs = 10000L,
        backoffFactor = 2.0
    )
    
    val FILE_IO = RetryConfiguration(
        maxRetries = 2,
        initialDelayMs = 250L,
        maxDelayMs = 2000L,
        backoffFactor = 2.0
    )
}