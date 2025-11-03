package com.moderncalendar.core.common.repository

import com.moderncalendar.core.common.model.UserSettings
import kotlinx.coroutines.flow.Flow

/**
 * Repository interface for user preferences
 */
interface UserPreferencesRepository {
    
    fun getUserSettings(): Flow<UserSettings>
    
    suspend fun updateUserSettings(settings: UserSettings)
    
    suspend fun clearAllSettings()
    
    // Theme-specific methods
    fun getTheme(): Flow<String>
    
    suspend fun setTheme(theme: String)
}