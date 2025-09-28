package com.moderncalendar.core.data.repository

import kotlinx.coroutines.flow.Flow

interface SettingsRepository {
    fun exportSettings(): Flow<Unit>
    fun importSettings(settingsData: String): Flow<Unit>
}
