package com.moderncalendar.core.settings

import kotlinx.coroutines.flow.Flow

interface SettingsRepository {
    fun getDarkMode(): Flow<Boolean>
    fun setDarkMode(enabled: Boolean): Flow<Unit>
    fun getDynamicColors(): Flow<Boolean>
    fun setDynamicColors(enabled: Boolean): Flow<Unit>
    fun getEventReminders(): Flow<Boolean>
    fun setEventReminders(enabled: Boolean): Flow<Unit>
    fun getWeekStartsOn(): Flow<Int>
    fun setWeekStartsOn(day: Int): Flow<Unit>
    fun getLanguage(): Flow<String>
    fun setLanguage(language: String): Flow<Unit>
    fun getTimezone(): Flow<String>
    fun setTimezone(timezone: String): Flow<Unit>
}
