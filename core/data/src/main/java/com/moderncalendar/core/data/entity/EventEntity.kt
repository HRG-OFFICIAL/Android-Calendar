package com.moderncalendar.core.data.entity

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import androidx.room.TypeConverters
import com.moderncalendar.core.data.converter.DateTimeConverter
import com.moderncalendar.core.data.converter.RecurrenceConverter
import java.time.LocalDateTime
import java.util.UUID

@Entity(tableName = "events")
@TypeConverters(DateTimeConverter::class, RecurrenceConverter::class)
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
    
    @ColumnInfo(name = "is_all_day")
    val isAllDay: Boolean = false,
    
    @ColumnInfo(name = "location")
    val location: String? = null,
    
    @ColumnInfo(name = "color")
    val color: String = "#6750A4",
    
    @ColumnInfo(name = "calendar_id")
    val calendarId: String,
    
    @ColumnInfo(name = "recurrence_rule")
    val recurrenceRule: RecurrenceRule? = null,
    
    @ColumnInfo(name = "reminder_minutes")
    val reminderMinutes: List<Int> = emptyList(),
    
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

data class RecurrenceRule(
    val frequency: RecurrenceFrequency,
    val interval: Int = 1,
    val count: Int? = null,
    val until: LocalDateTime? = null,
    val byDay: List<DayOfWeek> = emptyList(),
    val byMonthDay: List<Int> = emptyList(),
    val byMonth: List<Int> = emptyList()
)

enum class RecurrenceFrequency {
    DAILY, WEEKLY, MONTHLY, YEARLY
}

enum class DayOfWeek {
    MONDAY, TUESDAY, WEDNESDAY, THURSDAY, FRIDAY, SATURDAY, SUNDAY
}
