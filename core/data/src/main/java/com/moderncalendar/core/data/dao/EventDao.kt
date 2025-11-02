package com.moderncalendar.core.data.dao

import androidx.room.*
import com.moderncalendar.core.data.entity.EventEntity
import com.moderncalendar.core.data.entity.EventPriority
import kotlinx.coroutines.flow.Flow
import java.time.LocalDateTime

@Dao
interface EventDao {
    
    @Query("SELECT * FROM events ORDER BY startDateTime ASC")
    fun getAllEvents(): Flow<List<EventEntity>>
    
    @Query("SELECT * FROM events WHERE userId = :userId OR userId IS NULL ORDER BY startDateTime ASC")
    fun getAllEventsForUser(userId: String? = null): Flow<List<EventEntity>>
    
    @Query("SELECT * FROM events WHERE id = :id")
    fun getEventById(id: String): Flow<EventEntity?>
    
    @Query("SELECT * FROM events WHERE startDateTime >= :startDate AND startDateTime <= :endDate ORDER BY startDateTime ASC")
    fun getEventsInDateRange(
        startDate: LocalDateTime,
        endDate: LocalDateTime
    ): Flow<List<EventEntity>>
    
    @Query("SELECT * FROM events WHERE userId = :userId OR userId IS NULL AND startDateTime >= :date ORDER BY startDateTime ASC LIMIT :limit")
    fun getUpcomingEvents(
        date: LocalDateTime,
        limit: Int = 10,
        userId: String? = null
    ): Flow<List<EventEntity>>
    
    @Query("SELECT * FROM events WHERE title LIKE '%' || :query || '%' OR description LIKE '%' || :query || '%' OR location LIKE '%' || :query || '%' ORDER BY startDateTime ASC")
    fun searchEvents(
        query: String
    ): Flow<List<EventEntity>>
    
    @Query("SELECT * FROM events WHERE userId = :userId OR userId IS NULL AND category = :category ORDER BY startDateTime ASC")
    fun getEventsByCategory(
        category: String,
        userId: String? = null
    ): Flow<List<EventEntity>>
    
    @Query("SELECT * FROM events WHERE userId = :userId OR userId IS NULL AND priority = :priority ORDER BY startDateTime ASC")
    fun getEventsByPriority(
        priority: EventPriority,
        userId: String? = null
    ): Flow<List<EventEntity>>
    
    @Query("SELECT * FROM events WHERE userId = :userId OR userId IS NULL AND isCompleted = :isCompleted ORDER BY startDateTime ASC")
    fun getEventsByCompletionStatus(
        isCompleted: Boolean,
        userId: String? = null
    ): Flow<List<EventEntity>>
    
    @Query("SELECT * FROM events WHERE userId = :userId OR userId IS NULL AND startDateTime >= :startDate AND startDateTime <= :endDate AND isAllDay = :isAllDay ORDER BY startDateTime ASC")
    fun getEventsByDateRangeAndType(
        startDate: LocalDateTime,
        endDate: LocalDateTime,
        isAllDay: Boolean,
        userId: String? = null
    ): Flow<List<EventEntity>>
    
    @Query("SELECT DISTINCT category FROM events WHERE userId = :userId OR userId IS NULL AND category IS NOT NULL ORDER BY category ASC")
    fun getAllCategories(userId: String? = null): Flow<List<String>>
    
    @Query("SELECT COUNT(*) FROM events WHERE userId = :userId OR userId IS NULL AND startDateTime >= :startDate AND startDateTime <= :endDate")
    suspend fun getEventCountInDateRange(
        startDate: LocalDateTime,
        endDate: LocalDateTime,
        userId: String? = null
    ): Int
    
    @Query("SELECT * FROM events WHERE userId = :userId OR userId IS NULL AND isSynced = :isSynced ORDER BY updatedAt ASC")
    fun getUnsyncedEvents(
        isSynced: Boolean = false,
        userId: String? = null
    ): Flow<List<EventEntity>>
    
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertEvent(event: EventEntity)
    
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertEvents(events: List<EventEntity>)
    
    @Update
    suspend fun updateEvent(event: EventEntity)
    
    @Update
    suspend fun updateEvents(events: List<EventEntity>)
    
    @Delete
    suspend fun deleteEvent(event: EventEntity)
    
    @Delete
    suspend fun deleteEvents(events: List<EventEntity>)
    
    @Query("DELETE FROM events WHERE id = :id")
    suspend fun deleteEventById(id: String)
    
    @Query("DELETE FROM events WHERE userId = :userId")
    suspend fun deleteAllEventsForUser(userId: String)
    
    @Query("DELETE FROM events")
    suspend fun deleteAllEvents()
    
    @Query("UPDATE events SET isSynced = :isSynced, serverId = :serverId WHERE id = :id")
    suspend fun updateSyncStatus(id: String, isSynced: Boolean, serverId: String? = null)
    
    @Query("UPDATE events SET isCompleted = :isCompleted WHERE id = :id")
    suspend fun updateCompletionStatus(id: String, isCompleted: Boolean)
    
    @Query("UPDATE events SET priority = :priority WHERE id = :id")
    suspend fun updatePriority(id: String, priority: EventPriority)
    
    @Query("SELECT * FROM events WHERE startDateTime >= :startDate AND startDateTime <= :endDate AND (title LIKE '%' || :query || '%' OR description LIKE '%' || :query || '%' OR location LIKE '%' || :query || '%') ORDER BY startDateTime ASC")
    fun searchEventsInDateRange(
        query: String,
        startDate: LocalDateTime,
        endDate: LocalDateTime
    ): Flow<List<EventEntity>>
    
    @Query("SELECT * FROM events WHERE userId = :userId OR userId IS NULL AND startDateTime >= :date AND startDateTime <= :endDate AND priority = :priority ORDER BY startDateTime ASC")
    fun getEventsByDateRangeAndPriority(
        date: LocalDateTime,
        endDate: LocalDateTime,
        priority: EventPriority,
        userId: String? = null
    ): Flow<List<EventEntity>>
    
    @Query("SELECT * FROM events WHERE userId = :userId OR userId IS NULL AND startDateTime >= :date AND startDateTime <= :endDate AND category = :category ORDER BY startDateTime ASC")
    fun getEventsByDateRangeAndCategory(
        date: LocalDateTime,
        endDate: LocalDateTime,
        category: String,
        userId: String? = null
    ): Flow<List<EventEntity>>
}