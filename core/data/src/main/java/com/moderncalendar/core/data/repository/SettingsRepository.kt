package com.moderncalendar.core.data.repository

import android.content.Context
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.booleanPreferencesKey
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.intPreferencesKey
import androidx.datastore.preferences.core.stringPreferencesKey
import androidx.datastore.preferences.preferencesDataStore
import com.moderncalendar.core.common.model.DayOfWeek
import com.moderncalendar.core.common.repository.SettingsRepository
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import javax.inject.Inject
import javax.inject.Singleton

private val Context.dataStore: DataStore<Preferences> by preferencesDataStore(name = "settings")

@Singleton
class DataStoreSettingsRepository @Inject constructor(
    @ApplicationContext private val context: Context
): SettingsRepository {
    
    private val dataStore: DataStore<Preferences> = context.dataStore
    
    private val isDarkModeKey = booleanPreferencesKey("is_dark_mode")
    private val isDynamicColorsKey = booleanPreferencesKey("is_dynamic_colors")
    private val isEventRemindersEnabledKey = booleanPreferencesKey("is_event_reminders_enabled")
    private val weekStartsOnKey = intPreferencesKey("week_starts_on")
    private val languageKey = stringPreferencesKey("language")
    private val timezoneKey = stringPreferencesKey("timezone")
    
    override fun getDarkMode(): Flow<Boolean> = dataStore.data.map { preferences ->
        preferences[isDarkModeKey] ?: false
    }
    
    override suspend fun setDarkMode(enabled: Boolean) {
        dataStore.edit { prefs ->
            prefs[isDarkModeKey] = enabled
        }
    }
    
    override fun getDynamicColors(): Flow<Boolean> = dataStore.data.map { preferences ->
        preferences[isDynamicColorsKey] ?: true
    }
    
    override suspend fun setDynamicColors(enabled: Boolean) {
        dataStore.edit { prefs ->
            prefs[isDynamicColorsKey] = enabled
        }
    }
    
    override fun getEventReminders(): Flow<Boolean> = dataStore.data.map { preferences ->
        preferences[isEventRemindersEnabledKey] ?: true
    }
    
    override suspend fun setEventReminders(enabled: Boolean) {
        dataStore.edit { prefs ->
            prefs[isEventRemindersEnabledKey] = enabled
        }
    }
    
    override fun getWeekStartsOn(): Flow<DayOfWeek> = dataStore.data.map { preferences ->
        val dayIndex = preferences[weekStartsOnKey] ?: 1 // Default to Monday
        DayOfWeek.values()[dayIndex]
    }
    
    override suspend fun setWeekStartsOn(dayOfWeek: DayOfWeek) {
        dataStore.edit { prefs ->
            prefs[weekStartsOnKey] = dayOfWeek.ordinal
        }
    }
    
    override fun getLanguage(): Flow<String> = dataStore.data.map { preferences ->
        preferences[languageKey] ?: "en"
    }
    
    override suspend fun setLanguage(language: String) {
        dataStore.edit { prefs ->
            prefs[languageKey] = language
        }
    }
    
    override fun getTimezone(): Flow<String> = dataStore.data.map { preferences ->
        preferences[timezoneKey] ?: "UTC"
    }
    
    override suspend fun setTimezone(timezone: String) {
        dataStore.edit { prefs ->
            prefs[timezoneKey] = timezone
        }
    }
}
