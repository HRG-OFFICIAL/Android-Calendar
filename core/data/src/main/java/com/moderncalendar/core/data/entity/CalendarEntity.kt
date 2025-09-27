package com.moderncalendar.core.data.entity

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import androidx.room.TypeConverters
import com.moderncalendar.core.data.converter.DateTimeConverter
import java.time.LocalDateTime
import java.util.UUID

@Entity(tableName = "calendars")
@TypeConverters(DateTimeConverter::class)
data class CalendarEntity(
    @PrimaryKey
    val id: String = UUID.randomUUID().toString(),
    
    @ColumnInfo(name = "name")
    val name: String,
    
    @ColumnInfo(name = "description")
    val description: String? = null,
    
    @ColumnInfo(name = "color")
    val color: String = "#6750A4",
    
    @ColumnInfo(name = "is_visible")
    val isVisible: Boolean = true,
    
    @ColumnInfo(name = "is_primary")
    val isPrimary: Boolean = false,
    
    @ColumnInfo(name = "calendar_type")
    val calendarType: CalendarType = CalendarType.LOCAL,
    
    @ColumnInfo(name = "account_email")
    val accountEmail: String? = null,
    
    @ColumnInfo(name = "sync_id")
    val syncId: String? = null,
    
    @ColumnInfo(name = "is_synced")
    val isSynced: Boolean = false,
    
    @ColumnInfo(name = "created_at")
    val createdAt: LocalDateTime = LocalDateTime.now(),
    
    @ColumnInfo(name = "updated_at")
    val updatedAt: LocalDateTime = LocalDateTime.now(),
    
    @ColumnInfo(name = "is_deleted")
    val isDeleted: Boolean = false
)

enum class CalendarType {
    LOCAL, GOOGLE, MICROSOFT, CALDAV
}
