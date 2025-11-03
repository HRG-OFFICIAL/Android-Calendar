package com.moderncalendar.core.common.model

import java.time.LocalDateTime

/**
 * Domain model for calendar events
 */
data class Event(
    val id: String,
    val title: String,
    val description: String? = null,
    val location: String? = null,
    val startDateTime: LocalDateTime,
    val endDateTime: LocalDateTime,
    val isAllDay: Boolean = false,
    val color: String = "#009688",
    val recurrenceRule: String? = null,
    val reminderMinutes: List<Int> = emptyList(),
    val isCompleted: Boolean = false,
    val priority: EventPriority = EventPriority.MEDIUM,
    val category: String? = null,
    val attendees: List<String> = emptyList(),
    val attachments: List<String> = emptyList(),
    val createdAt: LocalDateTime = LocalDateTime.now(),
    val updatedAt: LocalDateTime = LocalDateTime.now(),
    val userId: String? = null,
    val isSynced: Boolean = false,
    val serverId: String? = null,
)

enum class EventPriority {
    LOW,
    MEDIUM,
    HIGH,
    URGENT,
}
