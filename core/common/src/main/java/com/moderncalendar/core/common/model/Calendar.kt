package com.moderncalendar.core.common.model

import java.time.LocalDateTime

/**
 * Domain model for calendars
 */
data class Calendar(
    val id: String,
    val name: String,
    val description: String? = null,
    val color: String = "#6750A4",
    val isVisible: Boolean = true,
    val isPrimary: Boolean = false,
    val calendarType: CalendarType = CalendarType.LOCAL,
    val accountEmail: String? = null,
    val syncId: String? = null,
    val isSynced: Boolean = false,
    val createdAt: LocalDateTime = LocalDateTime.now(),
    val updatedAt: LocalDateTime = LocalDateTime.now(),
    val isDeleted: Boolean = false
)

enum class CalendarType {
    LOCAL, GOOGLE, MICROSOFT, CALDAV
}