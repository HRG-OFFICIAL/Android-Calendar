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
import com.moderncalendar.core.common.model.UserSettings
import com.moderncalendar.core.common.repository.UserPreferencesRepository
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import javax.inject.Inject
import javax.inject.Singleton

private val Context.userPreferencesDataStore: DataStore<Preferences> by preferencesDataStore(name = "user_preferences")

@Singleton
class UserPreferencesRepositoryImpl @Inject constructor(
    @ApplicationContext private val context: Context
) : UserPreferencesRepository {
    
    private val dataStore: DataStore<Preferences> = context.userPreferencesDataStore
    
    private val darkModeKey = booleanPreferencesKey("dark_mode")
    private val dynamicColorsKey = booleanPreferencesKey("dynamic_colors")
    private val eventRemindersKey = booleanPreferencesKey("event_reminders")
    private val weekStartsOnKey = intPreferencesKey("week_starts_on")
    private val languageKey = stringPreferencesKey("language")
    private val timezoneKey = stringPreferencesKey("timezone")
    
    override fun getUserSettings(): Flow<UserSettings> = dataStore.data.map { preferences ->
        UserSettings(
            darkMode = preferences[darkModeKey] ?: false,
            dynamicColors = preferences[dynamicColorsKey] ?: true,
            eventReminders = preferences[eventRemindersKey] ?: true,
            weekStartsOn = DayOfWeek.values().getOrElse(preferences[weekStartsOnKey] ?: 1) { DayOfWeek.MONDAY },
            language = preferences[languageKey] ?: "en",
            timezone = preferences[timezoneKey] ?: "UTC"
        )
    }
    
    override suspend fun updateUserSettings(settings: UserSettings) {
        dataStore.edit { preferences ->
            preferences[darkModeKey] = settings.darkMode
            preferences[dynamicColorsKey] = settings.dynamicColors
            preferences[eventRemindersKey] = settings.eventReminders
            preferences[weekStartsOnKey] = settings.weekStartsOn.ordinal
            preferences[languageKey] = settings.language
            preferences[timezoneKey] = settings.timezone
        }
    }
    
    override suspend fun clearAllSettings() {
        dataStore.edit { preferences ->
            preferences.clear()
        }
    }
    
    override fun getTheme(): Flow<String> = dataStore.data.map { preferences ->
        preferences[stringPreferencesKey("theme")] ?: "system"
    }
    
    override suspend fun setTheme(theme: String) {
        dataStore.edit { preferences ->
            preferences[stringPreferencesKey("theme")] = theme
        }
    }
}