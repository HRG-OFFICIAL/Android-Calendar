package com.moderncalendar.core.data.repository

import com.moderncalendar.core.common.CalendarError
import com.moderncalendar.core.common.ErrorHandler
import com.moderncalendar.core.common.Result
import com.moderncalendar.core.common.model.Calendar
import com.moderncalendar.core.common.repository.CalendarRepository
import com.moderncalendar.core.common.utils.retryWithBackoff
import com.moderncalendar.core.data.dao.CalendarDao
import com.moderncalendar.core.data.mapper.toDomainModel
import com.moderncalendar.core.data.mapper.toDataEntity
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.map
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class CalendarRepositoryImpl @Inject constructor(
    private val calendarDao: CalendarDao
) : CalendarRepository {
    
    override fun getAllCalendars(): Flow<Result<List<Calendar>>> {
        return flow {
            emit(Result.Loading)
            calendarDao.getAllCalendars()
                .catch { exception -> 
                    val error = ErrorHandler.handleException(exception)
                    ErrorHandler.logError(
                        tag = "CalendarRepository",
                        message = "Failed to get all calendars",
                        throwable = exception,
                        context = mapOf("operation" to "getAllCalendars")
                    )
                    emit(Result.Error(error))
                }
                .collect { calendars -> 
                    try {
                        val domainCalendars = calendars.map { it.toDomainModel() }
                        emit(Result.Success(domainCalendars))
                    } catch (e: Exception) {
                        val error = CalendarError.DatabaseError.queryFailed("calendars")
                        ErrorHandler.logError(
                            tag = "CalendarRepository",
                            message = "Failed to map calendars to domain model",
                            throwable = e
                        )
                        emit(Result.Error(error))
                    }
                }
        }
    }
    
    override fun getVisibleCalendars(): Flow<Result<List<Calendar>>> {
        return flow {
            emit(Result.Loading)
            calendarDao.getVisibleCalendars()
                .catch { exception -> 
                    val error = ErrorHandler.handleException(exception)
                    ErrorHandler.logError(
                        tag = "CalendarRepository",
                        message = "Failed to get visible calendars",
                        throwable = exception,
                        context = mapOf("operation" to "getVisibleCalendars")
                    )
                    emit(Result.Error(error))
                }
                .collect { calendars -> 
                    try {
                        val domainCalendars = calendars.map { it.toDomainModel() }
                        emit(Result.Success(domainCalendars))
                    } catch (e: Exception) {
                        val error = CalendarError.DatabaseError.queryFailed("calendars")
                        ErrorHandler.logError(
                            tag = "CalendarRepository",
                            message = "Failed to map visible calendars to domain model",
                            throwable = e
                        )
                        emit(Result.Error(error))
                    }
                }
        }
    }
    
    override fun getCalendarById(calendarId: String): Flow<Result<Calendar?>> {
        return flow {
            // Validate calendar ID
            if (calendarId.isBlank()) {
                val error = CalendarError.ValidationError.requiredField("Calendar ID")
                emit(Result.Error(error))
                return@flow
            }
            
            emit(Result.Loading)
            try {
                val calendar = calendarDao.getCalendarById(calendarId)
                val domainCalendar = calendar?.toDomainModel()
                emit(Result.Success(domainCalendar))
            } catch (exception: Exception) {
                val error = ErrorHandler.handleException(exception)
                ErrorHandler.logError(
                    tag = "CalendarRepository",
                    message = "Failed to get calendar by ID",
                    throwable = exception,
                    context = mapOf(
                        "operation" to "getCalendarById",
                        "calendarId" to calendarId
                    )
                )
                emit(Result.Error(error))
            }
        }
    }
    
    override suspend fun insertCalendar(calendar: Calendar): Result<Unit> {
        return try {
            retryWithBackoff(
                maxRetries = 2,
                initialDelayMs = 500
            ) {
                // Validate calendar before insertion
                validateCalendar(calendar)
                
                try {
                    calendarDao.insertCalendar(calendar.toDataEntity())
                } catch (e: Exception) {
                    ErrorHandler.logError(
                        tag = "CalendarRepository",
                        message = "Failed to insert calendar",
                        throwable = e,
                        context = mapOf(
                            "operation" to "insertCalendar",
                            "calendarId" to calendar.id,
                            "calendarName" to calendar.name
                        )
                    )
                    throw CalendarError.DatabaseError.insertFailed("calendars")
                }
            }
            Result.Success(Unit)
        } catch (e: Exception) {
            Result.Error(ErrorHandler.handleException(e))
        }
    }
    
    override suspend fun updateCalendar(calendar: Calendar): Result<Unit> {
        return try {
            retryWithBackoff(
                maxRetries = 2,
                initialDelayMs = 500
            ) {
                // Validate calendar before update
                validateCalendar(calendar)
                
                try {
                    calendarDao.updateCalendar(calendar.toDataEntity())
                } catch (e: Exception) {
                    ErrorHandler.logError(
                        tag = "CalendarRepository",
                        message = "Failed to update calendar",
                        throwable = e,
                        context = mapOf(
                            "operation" to "updateCalendar",
                            "calendarId" to calendar.id,
                            "calendarName" to calendar.name
                        )
                    )
                    throw CalendarError.DatabaseError.updateFailed("calendars")
                }
            }
            Result.Success(Unit)
        } catch (e: Exception) {
            Result.Error(ErrorHandler.handleException(e))
        }
    }
    
    override suspend fun deleteCalendar(calendarId: String): Result<Unit> {
        return try {
            retryWithBackoff(
                maxRetries = 2,
                initialDelayMs = 500
            ) {
                // Validate calendar ID
                if (calendarId.isBlank()) {
                    throw CalendarError.ValidationError.requiredField("Calendar ID")
                }
                
                try {
                    calendarDao.deleteCalendar(calendarId)
                } catch (e: Exception) {
                    ErrorHandler.logError(
                        tag = "CalendarRepository",
                        message = "Failed to delete calendar",
                        throwable = e,
                        context = mapOf(
                            "operation" to "deleteCalendar",
                            "calendarId" to calendarId
                        )
                    )
                    throw CalendarError.DatabaseError.deleteFailed("calendars")
                }
            }
            Result.Success(Unit)
        } catch (e: Exception) {
            Result.Error(ErrorHandler.handleException(e))
        }
    }
    
    override suspend fun setCalendarVisibility(calendarId: String, isVisible: Boolean): Result<Unit> {
        return try {
            retryWithBackoff(
                maxRetries = 2,
                initialDelayMs = 500
            ) {
                // Validate calendar ID
                if (calendarId.isBlank()) {
                    throw CalendarError.ValidationError.requiredField("Calendar ID")
                }
                
                try {
                    calendarDao.updateCalendarVisibility(calendarId, isVisible)
                } catch (e: Exception) {
                    ErrorHandler.logError(
                        tag = "CalendarRepository",
                        message = "Failed to set calendar visibility",
                        throwable = e,
                        context = mapOf(
                            "operation" to "setCalendarVisibility",
                            "calendarId" to calendarId,
                            "isVisible" to isVisible
                        )
                    )
                    throw CalendarError.DatabaseError.updateFailed("calendars")
                }
            }
            Result.Success(Unit)
        } catch (e: Exception) {
            Result.Error(ErrorHandler.handleException(e))
        }
    }
    
    /**
     * Validate calendar data before database operations
     */
    private fun validateCalendar(calendar: Calendar) {
        if (calendar.id.isBlank()) {
            throw CalendarError.ValidationError.requiredField("Calendar ID")
        }
        
        if (calendar.name.isBlank()) {
            throw CalendarError.ValidationError.requiredField("Calendar name")
        }
        
        if (calendar.name.length > 100) {
            throw CalendarError.ValidationError(
                errorMessage = "Calendar name cannot exceed 100 characters.",
                field = "Calendar name"
            )
        }
        
        calendar.description?.let { description ->
            if (description.length > 500) {
                throw CalendarError.ValidationError(
                    errorMessage = "Calendar description cannot exceed 500 characters.",
                    field = "Calendar description"
                )
            }
        }
    }
}