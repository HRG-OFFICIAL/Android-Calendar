package com.moderncalendar.core.performance

import android.os.Debug
import android.os.SystemClock
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class PerformanceMonitor @Inject constructor() {
    
    private val performanceData = mutableMapOf<String, PerformanceMetric>()
    
    fun startTiming(operation: String) {
        val startTime = SystemClock.elapsedRealtimeNanos()
        val startMemory = getUsedMemory()
        
        performanceData[operation] = PerformanceMetric(
            operation = operation,
            startTime = startTime,
            startMemory = startMemory,
            isRunning = true
        )
    }
    
    fun endTiming(operation: String) {
        val metric = performanceData[operation] ?: return
        val endTime = SystemClock.elapsedRealtimeNanos()
        val endMemory = getUsedMemory()
        
        val duration = (endTime - metric.startTime) / 1_000_000 // Convert to milliseconds
        val memoryUsed = endMemory - metric.startMemory
        
        performanceData[operation] = metric.copy(
            endTime = endTime,
            endMemory = endMemory,
            duration = duration,
            memoryUsed = memoryUsed,
            isRunning = false
        )
    }
    
    fun getPerformanceData(): Map<String, PerformanceMetric> = performanceData.toMap()
    
    fun getAverageDuration(operation: String): Long {
        val metrics = performanceData.values.filter { it.operation == operation && !it.isRunning }
        return if (metrics.isNotEmpty()) {
            metrics.map { it.duration ?: 0 }.average().toLong()
        } else 0
    }
    
    fun getMemoryUsage(): MemoryUsage {
        val runtime = Runtime.getRuntime()
        val maxMemory = runtime.maxMemory()
        val totalMemory = runtime.totalMemory()
        val freeMemory = runtime.freeMemory()
        val usedMemory = totalMemory - freeMemory
        
        return MemoryUsage(
            maxMemory = maxMemory,
            totalMemory = totalMemory,
            freeMemory = freeMemory,
            usedMemory = usedMemory,
            usedPercentage = (usedMemory.toFloat() / maxMemory.toFloat() * 100).toInt()
        )
    }
    
    fun logPerformance(operation: String) {
        val metric = performanceData[operation]
        if (metric != null && !metric.isRunning) {
            println("Performance: $operation took ${metric.duration}ms, used ${metric.memoryUsed} bytes")
        }
    }
    
    fun clearPerformanceData() {
        performanceData.clear()
    }
    
    private fun getUsedMemory(): Long {
        val runtime = Runtime.getRuntime()
        return runtime.totalMemory() - runtime.freeMemory()
    }
    
    fun monitorOperation(operation: String, block: suspend () -> Unit) {
        CoroutineScope(Dispatchers.IO).launch {
            startTiming(operation)
            try {
                withContext(Dispatchers.Main) {
                    block()
                }
            } finally {
                endTiming(operation)
                logPerformance(operation)
            }
        }
    }
}

data class PerformanceMetric(
    val operation: String,
    val startTime: Long,
    val startMemory: Long,
    val endTime: Long? = null,
    val endMemory: Long? = null,
    val duration: Long? = null,
    val memoryUsed: Long? = null,
    val isRunning: Boolean = false
)

data class MemoryUsage(
    val maxMemory: Long,
    val totalMemory: Long,
    val freeMemory: Long,
    val usedMemory: Long,
    val usedPercentage: Int
)
