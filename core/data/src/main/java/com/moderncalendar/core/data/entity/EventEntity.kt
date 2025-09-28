package com.moderncalendar.core.data.entity

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import androidx.room.TypeConverters
import com.moderncalendar.core.data.converter.DateTimeConverter
import java.time.LocalDateTime
import java.util.UUID

@Entity(tableName = "events")
@TypeConverters(DateTimeConverter::class)
data class EventEntity(
    @PrimaryKey
    val id: String = UUID.randomUUID().toString(),
    
    @ColumnInfo(name = "title")
    val title: String,
    
    @ColumnInfo(name = "description")
    val description: String? = null,
    
    @ColumnInfo(name = "start_date_time")
    val startDateTime: LocalDateTime,
    
    @ColumnInfo(name = "end_date_time")
    val endDateTime: LocalDateTime,
    
    // Aliases for backward compatibility
    val startTime: LocalDateTime = startDateTime,
    val endTime: LocalDateTime = endDateTime,
    
    @ColumnInfo(name = "is_all_day")
    val isAllDay: Boolean = false,
    
    @ColumnInfo(name = "location")
    val location: String? = null,
    
    @ColumnInfo(name = "color")
    val color: Int = 0xFF6750A4.toInt(),
    
    @ColumnInfo(name = "calendar_id")
    val calendarId: String = "default",
    
    @ColumnInfo(name = "recurrence_rule")
    val recurrenceRule: String? = null, // Store as JSON string for now
    
    @ColumnInfo(name = "reminder_minutes")
    val reminderMinutes: Int? = null, // Store as Int for minutes before event
    
    @ColumnInfo(name = "is_synced")
    val isSynced: Boolean = false,
    
    @ColumnInfo(name = "sync_id")
    val syncId: String? = null,
    
    @ColumnInfo(name = "created_at")
    val createdAt: LocalDateTime = LocalDateTime.now(),
    
    @ColumnInfo(name = "updated_at")
    val updatedAt: LocalDateTime = LocalDateTime.now(),
    
    @ColumnInfo(name = "is_deleted")
    val isDeleted: Boolean = false
)

