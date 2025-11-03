package com.moderncalendar.core.common.repository

import com.moderncalendar.core.common.model.DayOfWeek
import kotlinx.coroutines.flow.Flow

/**
 * Repository interface for user settings
 */
interface SettingsRepository {
    fun getDarkMode(): Flow<Boolean>

    suspend fun setDarkMode(enabled: Boolean)

    fun getDynamicColors(): Flow<Boolean>

    suspend fun setDynamicColors(enabled: Boolean)

    fun getEventReminders(): Flow<Boolean>

    suspend fun setEventReminders(enabled: Boolean)

    fun getWeekStartsOn(): Flow<DayOfWeek>

    suspend fun setWeekStartsOn(dayOfWeek: DayOfWeek)

    fun getLanguage(): Flow<String>

    suspend fun setLanguage(language: String)

    fun getTimezone(): Flow<String>

    suspend fun setTimezone(timezone: String)
}
