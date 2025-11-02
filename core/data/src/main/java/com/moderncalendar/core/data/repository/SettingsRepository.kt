package com.moderncalendar.core.data.repository

import android.content.Context
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.booleanPreferencesKey
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.intPreferencesKey
import androidx.datastore.preferences.core.stringPreferencesKey
import androidx.datastore.preferences.preferencesDataStore
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import javax.inject.Inject
import javax.inject.Singleton
import com.moderncalendar.core.common.settings.SettingsRepository

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
    
    override fun setDarkMode(enabled: Boolean): Flow<Unit> = dataStore.data.map { preferences ->
        dataStore.edit { prefs ->
            prefs[isDarkModeKey] = enabled
        }
    }
    
    override fun getDynamicColors(): Flow<Boolean> = dataStore.data.map { preferences ->
        preferences[isDynamicColorsKey] ?: true
    }
    
    override fun setDynamicColors(enabled: Boolean): Flow<Unit> = dataStore.data.map { preferences ->
        dataStore.edit { prefs ->
            prefs[isDynamicColorsKey] = enabled
        }
    }
    
    override fun getEventReminders(): Flow<Boolean> = dataStore.data.map { preferences ->
        preferences[isEventRemindersEnabledKey] ?: true
    }
    
    override fun setEventReminders(enabled: Boolean): Flow<Unit> = dataStore.data.map { preferences ->
        dataStore.edit { prefs ->
            prefs[isEventRemindersEnabledKey] = enabled
        }
    }
    
    override fun getWeekStartsOn(): Flow<Int> = dataStore.data.map { preferences ->
        preferences[weekStartsOnKey] ?: 1 // Default to Monday
    }
    
    override fun setWeekStartsOn(day: Int): Flow<Unit> = dataStore.data.map { preferences ->
        dataStore.edit { prefs ->
            prefs[weekStartsOnKey] = day
        }
    }
    
    override fun getLanguage(): Flow<String> = dataStore.data.map { preferences ->
        preferences[languageKey] ?: "en"
    }
    
    override fun setLanguage(language: String): Flow<Unit> = dataStore.data.map { preferences ->
        dataStore.edit { prefs ->
            prefs[languageKey] = language
        }
    }
    
    override fun getTimezone(): Flow<String> = dataStore.data.map { preferences ->
        preferences[timezoneKey] ?: "UTC"
    }
    
    override fun setTimezone(timezone: String): Flow<Unit> = dataStore.data.map { preferences ->
        dataStore.edit { prefs ->
            prefs[timezoneKey] = timezone
        }
    }
}
