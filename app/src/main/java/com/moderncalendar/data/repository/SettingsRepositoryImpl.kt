package com.moderncalendar.data.repository

import com.moderncalendar.core.data.repository.SettingsRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class SettingsRepositoryImpl @Inject constructor() : SettingsRepository {
    
    override fun exportSettings(): Flow<Unit> = flow {
        // Simulate settings export
        emit(Unit)
    }
    
    override fun importSettings(settingsData: String): Flow<Unit> = flow {
        // Simulate settings import
        emit(Unit)
    }
}
