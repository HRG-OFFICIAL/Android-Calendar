package com.moderncalendar.core.data.dao

import androidx.room.*
import com.moderncalendar.core.data.entity.CalendarEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface CalendarDao {
    
    @Query("SELECT * FROM calendars WHERE is_deleted = 0 ORDER BY is_primary DESC, name ASC")
    fun getAllCalendars(): Flow<List<CalendarEntity>>
    
    @Query("SELECT * FROM calendars WHERE id = :calendarId AND is_deleted = 0")
    suspend fun getCalendarById(calendarId: String): CalendarEntity?
    
    @Query("SELECT * FROM calendars WHERE is_visible = 1 AND is_deleted = 0 ORDER BY is_primary DESC, name ASC")
    fun getVisibleCalendars(): Flow<List<CalendarEntity>>
    
    @Query("SELECT * FROM calendars WHERE calendar_type = :type AND is_deleted = 0 ORDER BY name ASC")
    fun getCalendarsByType(type: String): Flow<List<CalendarEntity>>
    
    @Query("SELECT * FROM calendars WHERE is_primary = 1 AND is_deleted = 0 LIMIT 1")
    suspend fun getPrimaryCalendar(): CalendarEntity?
    
    @Query("SELECT * FROM calendars WHERE sync_id = :syncId AND is_deleted = 0")
    suspend fun getCalendarBySyncId(syncId: String): CalendarEntity?
    
    @Query("SELECT * FROM calendars WHERE is_synced = 0 AND is_deleted = 0")
    suspend fun getUnsyncedCalendars(): List<CalendarEntity>
    
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertCalendar(calendar: CalendarEntity)
    
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertCalendars(calendars: List<CalendarEntity>)
    
    @Update
    suspend fun updateCalendar(calendar: CalendarEntity)
    
    @Query("UPDATE calendars SET is_deleted = 1, updated_at = :updatedAt WHERE id = :calendarId")
    suspend fun deleteCalendar(calendarId: String, updatedAt: LocalDateTime = LocalDateTime.now())
    
    @Query("UPDATE calendars SET is_primary = 0 WHERE id != :calendarId")
    suspend fun unsetPrimaryCalendar(calendarId: String)
    
    @Query("UPDATE calendars SET is_primary = 1 WHERE id = :calendarId")
    suspend fun setPrimaryCalendar(calendarId: String)
    
    @Query("UPDATE calendars SET is_visible = :isVisible WHERE id = :calendarId")
    suspend fun updateCalendarVisibility(calendarId: String, isVisible: Boolean)
    
    @Query("UPDATE calendars SET is_synced = 1 WHERE id = :calendarId")
    suspend fun markCalendarAsSynced(calendarId: String)
    
    @Query("UPDATE calendars SET is_synced = 0 WHERE sync_id = :syncId")
    suspend fun markCalendarAsUnsynced(syncId: String)
}
