package com.moderncalendar.core.data.repository

import kotlinx.coroutines.flow.Flow

interface UserPreferencesRepository {
    fun getTheme(): Flow<String>
    fun setTheme(theme: String): Flow<Unit>
    fun getNotificationsEnabled(): Flow<Boolean>
    fun setNotificationsEnabled(enabled: Boolean): Flow<Unit>
    fun getReminderTime(): Flow<Int>
    fun setReminderTime(minutes: Int): Flow<Unit>
    fun getWeekStartsOn(): Flow<Int>
    fun setWeekStartsOn(day: Int): Flow<Unit>
    fun getTimeFormat(): Flow<String>
    fun setTimeFormat(format: String): Flow<Unit>
    fun getDateFormat(): Flow<String>
    fun setDateFormat(format: String): Flow<Unit>
    fun resetToDefaults(): Flow<Unit>
}
