package com.moderncalendar.core.common.model

/**
 * Domain model for user settings
 */
data class UserSettings(
    val darkMode: Boolean = false,
    val dynamicColors: Boolean = true,
    val eventReminders: Boolean = true,
    val weekStartsOn: DayOfWeek = DayOfWeek.MONDAY,
    val language: String = "en",
    val timezone: String = "UTC"
)

enum class DayOfWeek {
    SUNDAY, MONDAY, TUESDAY, WEDNESDAY, THURSDAY, FRIDAY, SATURDAY
}