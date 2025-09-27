package com.moderncalendar.core.data.dao

import androidx.room.*
import com.moderncalendar.core.data.entity.EventEntity
import kotlinx.coroutines.flow.Flow
import java.time.LocalDateTime

@Dao
interface EventDao {
    
    @Query("SELECT * FROM events WHERE is_deleted = 0 ORDER BY start_date_time ASC")
    fun getAllEvents(): Flow<List<EventEntity>>
    
    @Query("SELECT * FROM events WHERE id = :eventId AND is_deleted = 0")
    suspend fun getEventById(eventId: String): EventEntity?
    
    @Query("SELECT * FROM events WHERE calendar_id = :calendarId AND is_deleted = 0 ORDER BY start_date_time ASC")
    fun getEventsByCalendarId(calendarId: String): Flow<List<EventEntity>>
    
    @Query("SELECT * FROM events WHERE start_date_time >= :startDate AND start_date_time < :endDate AND is_deleted = 0 ORDER BY start_date_time ASC")
    fun getEventsByDateRange(startDate: LocalDateTime, endDate: LocalDateTime): Flow<List<EventEntity>>
    
    @Query("SELECT * FROM events WHERE DATE(start_date_time) = DATE(:date) AND is_deleted = 0 ORDER BY start_date_time ASC")
    fun getEventsByDate(date: LocalDateTime): Flow<List<EventEntity>>
    
    @Query("SELECT * FROM events WHERE title LIKE '%' || :query || '%' OR description LIKE '%' || :query || '%' AND is_deleted = 0 ORDER BY start_date_time ASC")
    fun searchEvents(query: String): Flow<List<EventEntity>>
    
    @Query("SELECT * FROM events WHERE is_synced = 0 AND is_deleted = 0")
    suspend fun getUnsyncedEvents(): List<EventEntity>
    
    @Query("SELECT * FROM events WHERE sync_id = :syncId AND is_deleted = 0")
    suspend fun getEventBySyncId(syncId: String): EventEntity?
    
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertEvent(event: EventEntity)
    
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertEvents(events: List<EventEntity>)
    
    @Update
    suspend fun updateEvent(event: EventEntity)
    
    @Query("UPDATE events SET is_deleted = 1, updated_at = :updatedAt WHERE id = :eventId")
    suspend fun deleteEvent(eventId: String, updatedAt: LocalDateTime = LocalDateTime.now())
    
    @Query("DELETE FROM events WHERE is_deleted = 1 AND updated_at < :cutoffDate")
    suspend fun deleteOldDeletedEvents(cutoffDate: LocalDateTime)
    
    @Query("UPDATE events SET is_synced = 1 WHERE id = :eventId")
    suspend fun markEventAsSynced(eventId: String)
    
    @Query("UPDATE events SET is_synced = 0 WHERE sync_id = :syncId")
    suspend fun markEventAsUnsynced(syncId: String)
}
