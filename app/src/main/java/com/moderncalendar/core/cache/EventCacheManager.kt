package com.moderncalendar.core.cache

import com.moderncalendar.core.data.entity.EventEntity
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.sync.Mutex
import kotlinx.coroutines.sync.withLock
import java.time.LocalDateTime
import java.util.concurrent.ConcurrentHashMap
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class EventCacheManager @Inject constructor() {
    
    private val cache = ConcurrentHashMap<String, CachedEvent>()
    private val mutex = Mutex()
    
    data class CachedEvent(
        val event: EventEntity,
        val timestamp: LocalDateTime,
        val ttlMinutes: Long = 30 // Time to live in minutes
    ) {
        fun isExpired(): Boolean {
            return LocalDateTime.now().isAfter(timestamp.plusMinutes(ttlMinutes))
        }
    }
    
    suspend fun getEvent(eventId: String): EventEntity? {
        return mutex.withLock {
            val cached = cache[eventId]
            if (cached != null && !cached.isExpired()) {
                cached.event
            } else {
                cache.remove(eventId)
                null
            }
        }
    }
    
    suspend fun putEvent(event: EventEntity, ttlMinutes: Long = 30) {
        mutex.withLock {
            cache[event.id] = CachedEvent(
                event = event,
                timestamp = LocalDateTime.now(),
                ttlMinutes = ttlMinutes
            )
        }
    }
    
    suspend fun getEventsByDateRange(
        startDate: LocalDateTime,
        endDate: LocalDateTime
    ): Flow<List<EventEntity>> = flow {
        val cachedEvents = mutex.withLock {
            cache.values
                .filter { !it.isExpired() }
                .map { it.event }
                .filter { event ->
                    event.startDateTime.isAfter(startDate.minusNanos(1)) &&
                    event.startDateTime.isBefore(endDate.plusNanos(1))
                }
        }
        emit(cachedEvents)
    }
    
    suspend fun invalidateEvent(eventId: String) {
        mutex.withLock {
            cache.remove(eventId)
        }
    }
    
    suspend fun invalidateAll() {
        mutex.withLock {
            cache.clear()
        }
    }
    
    suspend fun cleanupExpired() {
        mutex.withLock {
            val expiredKeys = cache.entries
                .filter { it.value.isExpired() }
                .map { it.key }
            expiredKeys.forEach { cache.remove(it) }
        }
    }
    
    suspend fun getCacheSize(): Int {
        return mutex.withLock {
            cache.size
        }
    }
    
    suspend fun getCacheStats(): CacheStats {
        return mutex.withLock {
            val total = cache.size
            val expired = cache.values.count { it.isExpired() }
            val valid = total - expired
            
            CacheStats(
                totalItems = total,
                validItems = valid,
                expiredItems = expired
            )
        }
    }
    
    data class CacheStats(
        val totalItems: Int,
        val validItems: Int,
        val expiredItems: Int
    )
}
