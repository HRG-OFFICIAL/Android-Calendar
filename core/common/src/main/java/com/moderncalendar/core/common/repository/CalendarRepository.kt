package com.moderncalendar.core.common.repository

import com.moderncalendar.core.common.Result
import com.moderncalendar.core.common.model.Calendar
import kotlinx.coroutines.flow.Flow

/**
 * Repository interface for calendar operations
 */
interface CalendarRepository {
    
    fun getAllCalendars(): Flow<Result<List<Calendar>>>
    
    fun getVisibleCalendars(): Flow<Result<List<Calendar>>>
    
    fun getCalendarById(calendarId: String): Flow<Result<Calendar?>>
    
    suspend fun insertCalendar(calendar: Calendar): Result<Unit>
    
    suspend fun updateCalendar(calendar: Calendar): Result<Unit>
    
    suspend fun deleteCalendar(calendarId: String): Result<Unit>
    
    suspend fun setCalendarVisibility(calendarId: String, isVisible: Boolean): Result<Unit>
}