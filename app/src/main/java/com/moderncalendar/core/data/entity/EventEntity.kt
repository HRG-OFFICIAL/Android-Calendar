package com.moderncalendar.core.data.entity

import androidx.room.Entity
import androidx.room.PrimaryKey
import java.time.LocalDateTime

@Entity(tableName = "events")
data class EventEntity(
    @PrimaryKey
    val id: String,
    val title: String,
    val description: String? = null,
    val startTime: LocalDateTime,
    val endTime: LocalDateTime,
    val startDateTime: LocalDateTime = startTime, // Alias for startTime
    val endDateTime: LocalDateTime = endTime, // Alias for endTime
    val location: String? = null,
    val isAllDay: Boolean = false,
    val reminderMinutes: Int? = null,
    val recurrenceRule: String? = null,
    val calendarId: String = "default",
    val color: Int = 0xFF6750A4.toInt(),
    val isCompleted: Boolean = false,
    val createdAt: LocalDateTime = LocalDateTime.now(),
    val updatedAt: LocalDateTime = LocalDateTime.now()
)
