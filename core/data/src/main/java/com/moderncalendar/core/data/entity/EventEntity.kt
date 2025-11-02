package com.moderncalendar.core.data.entity

import androidx.room.Entity
import androidx.room.PrimaryKey
import androidx.room.TypeConverters
import java.time.LocalDateTime

@Entity(tableName = "events")
@TypeConverters(Converters::class)
data class EventEntity(
    @PrimaryKey
    val id: String,
    val title: String,
    val description: String? = null,
    val location: String? = null,
    val startDateTime: LocalDateTime,
    val endDateTime: LocalDateTime,
    val isAllDay: Boolean = false,
    val color: String = "#009688", // Default teal color
    val recurrenceRule: String? = null,
    val reminderMinutes: List<Int> = emptyList(),
    val isCompleted: Boolean = false,
    val priority: EventPriority = EventPriority.MEDIUM,
    val category: String? = null,
    val attendees: List<String> = emptyList(),
    val attachments: List<String> = emptyList(),
    val createdAt: LocalDateTime = LocalDateTime.now(),
    val updatedAt: LocalDateTime = LocalDateTime.now(),
    val userId: String? = null, // For multi-user support
    val isSynced: Boolean = false, // For sync status
    val serverId: String? = null // For cloud sync
)

enum class EventPriority {
    LOW, MEDIUM, HIGH, URGENT
}