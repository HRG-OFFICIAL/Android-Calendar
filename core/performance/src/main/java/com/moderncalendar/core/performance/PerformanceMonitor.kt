package com.moderncalendar.core.performance

import android.os.SystemClock
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import java.util.concurrent.ConcurrentHashMap
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class PerformanceMonitor @Inject constructor() {
    
    private val performanceMetrics = ConcurrentHashMap<String, PerformanceMetric>()
    private val _performanceData = MutableStateFlow<Map<String, PerformanceMetric>>(emptyMap())
    val performanceData: StateFlow<Map<String, PerformanceMetric>> = _performanceData.asStateFlow()
    
    private val _isMonitoring = MutableStateFlow(false)
    val isMonitoring: StateFlow<Boolean> = _isMonitoring.asStateFlow()
    
    // Performance thresholds
    private val slowOperationThreshold = 1000L // 1 second
    private val memoryWarningThreshold = 0.8 // 80% of max memory
    
    fun startMonitoring() {
        _isMonitoring.value = true
    }
    
    fun stopMonitoring() {
        _isMonitoring.value = false
    }
    
    suspend fun measureOperation(
        operationName: String,
        operation: suspend () -> Unit
    ) {
        if (!_isMonitoring.value) return
        
        val startTime = SystemClock.elapsedRealtime()
        val startMemory = getCurrentMemoryUsage()
        
        try {
            operation()
        } finally {
            val endTime = SystemClock.elapsedRealtime()
            val endMemory = getCurrentMemoryUsage()
            
            val duration = endTime - startTime
            val memoryDelta = endMemory - startMemory
            
            recordMetric(
                operationName,
                duration,
                memoryDelta,
                startMemory,
                endMemory
            )
        }
    }
    
    fun measureOperationSync(
        operationName: String,
        operation: () -> Unit
    ) {
        if (!_isMonitoring.value) return
        
        val startTime = SystemClock.elapsedRealtime()
        val startMemory = getCurrentMemoryUsage()
        
        try {
            operation()
        } finally {
            val endTime = SystemClock.elapsedRealtime()
            val endMemory = getCurrentMemoryUsage()
            
            val duration = endTime - startTime
            val memoryDelta = endMemory - startMemory
            
            recordMetric(
                operationName,
                duration,
                memoryDelta,
                startMemory,
                endMemory
            )
        }
    }
    
    fun recordMetric(
        operationName: String,
        duration: Long,
        memoryDelta: Long,
        startMemory: Long,
        endMemory: Long
    ) {
        val existingMetric = performanceMetrics[operationName]
        val newMetric = if (existingMetric != null) {
            existingMetric.copy(
                totalCalls = existingMetric.totalCalls + 1,
                totalDuration = existingMetric.totalDuration + duration,
                averageDuration = (existingMetric.totalDuration + duration) / (existingMetric.totalCalls + 1),
                minDuration = minOf(existingMetric.minDuration, duration),
                maxDuration = maxOf(existingMetric.maxDuration, duration),
                totalMemoryDelta = existingMetric.totalMemoryDelta + memoryDelta,
                averageMemoryDelta = (existingMetric.totalMemoryDelta + memoryDelta) / (existingMetric.totalCalls + 1),
                lastStartMemory = startMemory,
                lastEndMemory = endMemory,
                lastDuration = duration,
                isSlowOperation = duration > slowOperationThreshold,
                lastUpdated = System.currentTimeMillis()
            )
        } else {
            PerformanceMetric(
                operationName = operationName,
                totalCalls = 1,
                totalDuration = duration,
                averageDuration = duration,
                minDuration = duration,
                maxDuration = duration,
                totalMemoryDelta = memoryDelta,
                averageMemoryDelta = memoryDelta,
                lastStartMemory = startMemory,
                lastEndMemory = endMemory,
                lastDuration = duration,
                isSlowOperation = duration > slowOperationThreshold,
                lastUpdated = System.currentTimeMillis()
            )
        }
        
        performanceMetrics[operationName] = newMetric
        _performanceData.value = performanceMetrics.toMap()
        
        // Log slow operations
        if (newMetric.isSlowOperation) {
            logSlowOperation(newMetric)
        }
    }
    
    fun getMetric(operationName: String): PerformanceMetric? {
        return performanceMetrics[operationName]
    }
    
    fun getAllMetrics(): Map<String, PerformanceMetric> {
        return performanceMetrics.toMap()
    }
    
    fun getSlowOperations(): List<PerformanceMetric> {
        return performanceMetrics.values.filter { it.isSlowOperation }
    }
    
    fun getMemoryIntensiveOperations(): List<PerformanceMetric> {
        return performanceMetrics.values.filter { 
            it.averageMemoryDelta > 1024 * 1024 // 1MB
        }
    }
    
    fun clearMetrics() {
        performanceMetrics.clear()
        _performanceData.value = emptyMap()
    }
    
    fun clearMetric(operationName: String) {
        performanceMetrics.remove(operationName)
        _performanceData.value = performanceMetrics.toMap()
    }
    
    private fun getCurrentMemoryUsage(): Long {
        val runtime = Runtime.getRuntime()
        return runtime.totalMemory() - runtime.freeMemory()
    }
    
    private fun logSlowOperation(metric: PerformanceMetric) {
        println("🐌 Slow Operation: ${metric.operationName} took ${metric.lastDuration}ms")
    }
    
    fun getPerformanceReport(): PerformanceReport {
        val allMetrics = performanceMetrics.values.toList()
        
        return PerformanceReport(
            totalOperations = allMetrics.sumOf { it.totalCalls },
            averageOperationTime = allMetrics.map { it.averageDuration }.average(),
            slowOperations = allMetrics.count { it.isSlowOperation },
            memoryIntensiveOperations = allMetrics.count { it.averageMemoryDelta > 1024 * 1024 },
            topSlowOperations = allMetrics.sortedByDescending { it.averageDuration }.take(5),
            topMemoryIntensiveOperations = allMetrics.sortedByDescending { it.averageMemoryDelta }.take(5),
            generatedAt = System.currentTimeMillis()
        )
    }
}

data class PerformanceMetric(
    val operationName: String,
    val totalCalls: Int,
    val totalDuration: Long,
    val averageDuration: Long,
    val minDuration: Long,
    val maxDuration: Long,
    val totalMemoryDelta: Long,
    val averageMemoryDelta: Long,
    val lastStartMemory: Long,
    val lastEndMemory: Long,
    val lastDuration: Long,
    val isSlowOperation: Boolean,
    val lastUpdated: Long
)

data class PerformanceReport(
    val totalOperations: Int,
    val averageOperationTime: Double,
    val slowOperations: Int,
    val memoryIntensiveOperations: Int,
    val topSlowOperations: List<PerformanceMetric>,
    val topMemoryIntensiveOperations: List<PerformanceMetric>,
    val generatedAt: Long
)

// Extension functions for easy performance monitoring
suspend fun <T> PerformanceMonitor.measure(
    operationName: String,
    operation: suspend () -> T
): T {
    var result: T? = null
    measureOperation(operationName) {
        result = operation()
    }
    return result!!
}

fun <T> PerformanceMonitor.measureSync(
    operationName: String,
    operation: () -> T
): T {
    var result: T? = null
    measureOperationSync(operationName) {
        result = operation()
    }
    return result!!
}

// Performance monitoring for specific operations
class DatabasePerformanceMonitor(
    private val performanceMonitor: PerformanceMonitor
) {
    suspend fun <T> measureQuery(
        queryName: String,
        operation: suspend () -> T
    ): T {
        return performanceMonitor.measure("database_query_$queryName", operation)
    }
    
    suspend fun <T> measureInsert(
        tableName: String,
        operation: suspend () -> T
    ): T {
        return performanceMonitor.measure("database_insert_$tableName", operation)
    }
    
    suspend fun <T> measureUpdate(
        tableName: String,
        operation: suspend () -> T
    ): T {
        return performanceMonitor.measure("database_update_$tableName", operation)
    }
    
    suspend fun <T> measureDelete(
        tableName: String,
        operation: suspend () -> T
    ): T {
        return performanceMonitor.measure("database_delete_$tableName", operation)
    }
}

class NetworkPerformanceMonitor(
    private val performanceMonitor: PerformanceMonitor
) {
    suspend fun <T> measureApiCall(
        endpoint: String,
        operation: suspend () -> T
    ): T {
        return performanceMonitor.measure("api_call_$endpoint", operation)
    }
    
    suspend fun <T> measureFileDownload(
        fileName: String,
        operation: suspend () -> T
    ): T {
        return performanceMonitor.measure("file_download_$fileName", operation)
    }
    
    suspend fun <T> measureFileUpload(
        fileName: String,
        operation: suspend () -> T
    ): T {
        return performanceMonitor.measure("file_upload_$fileName", operation)
    }
}