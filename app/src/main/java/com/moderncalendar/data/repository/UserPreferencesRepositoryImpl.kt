package com.moderncalendar.data.repository

import com.moderncalendar.core.data.repository.UserPreferencesRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class UserPreferencesRepositoryImpl @Inject constructor() : UserPreferencesRepository {
    
    private var theme = "light"
    private var notificationsEnabled = true
    private var reminderTime = 15
    private var weekStartsOn = 1
    private var timeFormat = "12"
    private var dateFormat = "MM/dd/yyyy"
    
    override fun getTheme(): Flow<String> = flow { emit(theme) }
    override fun setTheme(theme: String): Flow<Unit> = flow { 
        this@UserPreferencesRepositoryImpl.theme = theme
        emit(Unit) 
    }
    
    override fun getNotificationsEnabled(): Flow<Boolean> = flow { emit(notificationsEnabled) }
    override fun setNotificationsEnabled(enabled: Boolean): Flow<Unit> = flow { 
        notificationsEnabled = enabled
        emit(Unit) 
    }
    
    override fun getReminderTime(): Flow<Int> = flow { emit(reminderTime) }
    override fun setReminderTime(minutes: Int): Flow<Unit> = flow { 
        reminderTime = minutes
        emit(Unit) 
    }
    
    override fun getWeekStartsOn(): Flow<Int> = flow { emit(weekStartsOn) }
    override fun setWeekStartsOn(day: Int): Flow<Unit> = flow { 
        weekStartsOn = day
        emit(Unit) 
    }
    
    override fun getTimeFormat(): Flow<String> = flow { emit(timeFormat) }
    override fun setTimeFormat(format: String): Flow<Unit> = flow { 
        timeFormat = format
        emit(Unit) 
    }
    
    override fun getDateFormat(): Flow<String> = flow { emit(dateFormat) }
    override fun setDateFormat(format: String): Flow<Unit> = flow { 
        dateFormat = format
        emit(Unit) 
    }
    
    override fun resetToDefaults(): Flow<Unit> = flow {
        theme = "light"
        notificationsEnabled = true
        reminderTime = 15
        weekStartsOn = 1
        timeFormat = "12"
        dateFormat = "MM/dd/yyyy"
        emit(Unit)
    }
}
