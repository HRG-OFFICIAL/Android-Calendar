package com.moderncalendar.core.data.repository

import com.moderncalendar.core.common.CalendarError
import com.moderncalendar.core.common.ErrorHandler
import com.moderncalendar.core.common.Result
import com.moderncalendar.core.common.model.Event
import com.moderncalendar.core.common.repository.EventRepository
import com.moderncalendar.core.common.utils.retryWithBackoff
import com.moderncalendar.core.data.dao.EventDao
import com.moderncalendar.core.data.mapper.toDomainModel
import com.moderncalendar.core.data.mapper.toDataEntity
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.map
import java.time.LocalDate
import java.time.LocalDateTime
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class RoomEventRepository @Inject constructor(
    private val eventDao: EventDao
) : EventRepository {
    
    override fun getAllEvents(): Flow<Result<List<Event>>> {
        return flow {
            emit(Result.Loading)
            eventDao.getAllEvents()
                .catch { exception -> 
                    val error = ErrorHandler.handleException(exception)
                    ErrorHandler.logError(
                        tag = "EventRepository",
                        message = "Failed to get all events",
                        throwable = exception,
                        context = mapOf("operation" to "getAllEvents")
                    )
                    emit(Result.Error(error))
                }
                .collect { events -> 
                    try {
                        val domainEvents = events.map { it.toDomainModel() }
                        emit(Result.Success(domainEvents))
                    } catch (e: Exception) {
                        val error = CalendarError.DatabaseError.queryFailed("events")
                        ErrorHandler.logError(
                            tag = "EventRepository",
                            message = "Failed to map events to domain model",
                            throwable = e
                        )
                        emit(Result.Error(error))
                    }
                }
        }
    }
    
    override fun getEventsByDateRange(
        startDate: LocalDate,
        endDate: LocalDate
    ): Flow<Result<List<Event>>> {
        return flow {
            // Validate date range
            if (startDate.isAfter(endDate)) {
                val error = CalendarError.ValidationError.invalidRange(
                    "Date range", 
                    startDate.toString(), 
                    endDate.toString()
                )
                emit(Result.Error(error))
                return@flow
            }
            
            emit(Result.Loading)
            eventDao.getEventsInDateRange(startDate.atStartOfDay(), endDate.atStartOfDay().plusDays(1))
                .catch { exception -> 
                    val error = ErrorHandler.handleException(exception)
                    ErrorHandler.logError(
                        tag = "EventRepository",
                        message = "Failed to get events by date range",
                        throwable = exception,
                        context = mapOf(
                            "operation" to "getEventsByDateRange",
                            "startDate" to startDate.toString(),
                            "endDate" to endDate.toString()
                        )
                    )
                    emit(Result.Error(error))
                }
                .collect { events -> 
                    try {
                        val domainEvents = events.map { it.toDomainModel() }
                        emit(Result.Success(domainEvents))
                    } catch (e: Exception) {
                        val error = CalendarError.DatabaseError.queryFailed("events")
                        ErrorHandler.logError(
                            tag = "EventRepository",
                            message = "Failed to map events to domain model",
                            throwable = e
                        )
                        emit(Result.Error(error))
                    }
                }
        }
    }
    
    override fun getEventsByDate(date: LocalDate): Flow<Result<List<Event>>> {
        val startOfDay = date.atStartOfDay()
        val endOfDay = startOfDay.plusDays(1)
        return flow {
            emit(Result.Loading)
            eventDao.getEventsInDateRange(startOfDay, endOfDay)
                .catch { exception -> 
                    val error = ErrorHandler.handleException(exception)
                    ErrorHandler.logError(
                        tag = "EventRepository",
                        message = "Failed to get events by date",
                        throwable = exception,
                        context = mapOf(
                            "operation" to "getEventsByDate",
                            "date" to date.toString()
                        )
                    )
                    emit(Result.Error(error))
                }
                .collect { events -> 
                    try {
                        val domainEvents = events.map { it.toDomainModel() }
                        emit(Result.Success(domainEvents))
                    } catch (e: Exception) {
                        val error = CalendarError.DatabaseError.queryFailed("events")
                        ErrorHandler.logError(
                            tag = "EventRepository",
                            message = "Failed to map events to domain model",
                            throwable = e
                        )
                        emit(Result.Error(error))
                    }
                }
        }
    }
    
    override fun getEventsForDate(
        startDateTime: LocalDateTime,
        endDateTime: LocalDateTime
    ): Flow<Result<List<Event>>> {
        return flow {
            emit(Result.Loading)
            eventDao.getEventsInDateRange(startDateTime, endDateTime)
                .catch { exception -> 
                    val error = ErrorHandler.handleException(exception)
                    ErrorHandler.logError(
                        tag = "EventRepository",
                        message = "Failed to get events for date",
                        throwable = exception,
                        context = mapOf(
                            "operation" to "getEventsForDate",
                            "startDateTime" to startDateTime.toString(),
                            "endDateTime" to endDateTime.toString()
                        )
                    )
                    emit(Result.Error(error))
                }
                .collect { events -> 
                    try {
                        val domainEvents = events.map { it.toDomainModel() }
                        emit(Result.Success(domainEvents))
                    } catch (e: Exception) {
                        val error = CalendarError.DatabaseError.queryFailed("events")
                        ErrorHandler.logError(
                            tag = "EventRepository",
                            message = "Failed to map events to domain model",
                            throwable = e
                        )
                        emit(Result.Error(error))
                    }
                }
        }
    }
    
    override fun getEventById(eventId: String): Flow<Result<Event?>> {
        return flow {
            // Validate event ID
            if (eventId.isBlank()) {
                val error = CalendarError.ValidationError.requiredField("Event ID")
                emit(Result.Error(error))
                return@flow
            }
            
            emit(Result.Loading)
            eventDao.getEventById(eventId)
                .catch { exception -> 
                    val error = ErrorHandler.handleException(exception)
                    ErrorHandler.logError(
                        tag = "EventRepository",
                        message = "Failed to get event by ID",
                        throwable = exception,
                        context = mapOf(
                            "operation" to "getEventById",
                            "eventId" to eventId
                        )
                    )
                    emit(Result.Error(error))
                }
                .collect { event -> 
                    try {
                        val domainEvent = event?.toDomainModel()
                        emit(Result.Success(domainEvent))
                    } catch (e: Exception) {
                        val error = CalendarError.DatabaseError.queryFailed("events")
                        ErrorHandler.logError(
                            tag = "EventRepository",
                            message = "Failed to map event to domain model",
                            throwable = e
                        )
                        emit(Result.Error(error))
                    }
                }
        }
    }
    
    override suspend fun insertEvent(event: Event): Result<Unit> {
        return try {
            retryWithBackoff(
                maxRetries = 2,
                initialDelayMs = 500
            ) {
                // Validate event before insertion
                validateEvent(event)
                
                try {
                    eventDao.insertEvent(event.toDataEntity())
                } catch (e: Exception) {
                    ErrorHandler.logError(
                        tag = "EventRepository",
                        message = "Failed to insert event",
                        throwable = e,
                        context = mapOf(
                            "operation" to "insertEvent",
                            "eventId" to event.id,
                            "eventTitle" to event.title
                        )
                    )
                    throw CalendarError.DatabaseError.insertFailed("events")
                }
            }
            Result.Success(Unit)
        } catch (e: Exception) {
            Result.Error(ErrorHandler.handleException(e))
        }
    }
    
    override suspend fun updateEvent(event: Event): Result<Unit> {
        return try {
            retryWithBackoff(
                maxRetries = 2,
                initialDelayMs = 500
            ) {
                // Validate event before update
                validateEvent(event)
                
                try {
                    eventDao.updateEvent(event.toDataEntity())
                } catch (e: Exception) {
                    ErrorHandler.logError(
                        tag = "EventRepository",
                        message = "Failed to update event",
                        throwable = e,
                        context = mapOf(
                            "operation" to "updateEvent",
                            "eventId" to event.id,
                            "eventTitle" to event.title
                        )
                    )
                    throw CalendarError.DatabaseError.updateFailed("events")
                }
            }
            Result.Success(Unit)
        } catch (e: Exception) {
            Result.Error(ErrorHandler.handleException(e))
        }
    }
    
    override suspend fun deleteEvent(eventId: String): Result<Unit> {
        return try {
            retryWithBackoff(
                maxRetries = 2,
                initialDelayMs = 500
            ) {
                // Validate event ID
                if (eventId.isBlank()) {
                    throw CalendarError.ValidationError.requiredField("Event ID")
                }
                
                try {
                    eventDao.deleteEventById(eventId)
                } catch (e: Exception) {
                    ErrorHandler.logError(
                        tag = "EventRepository",
                        message = "Failed to delete event",
                        throwable = e,
                        context = mapOf(
                            "operation" to "deleteEvent",
                            "eventId" to eventId
                        )
                    )
                    throw CalendarError.DatabaseError.deleteFailed("events")
                }
            }
            Result.Success(Unit)
        } catch (e: Exception) {
            Result.Error(ErrorHandler.handleException(e))
        }
    }
    
    override suspend fun deleteAllEvents(): Result<Unit> {
        return try {
            retryWithBackoff(
                maxRetries = 2,
                initialDelayMs = 500
            ) {
                try {
                    eventDao.deleteAllEvents()
                } catch (e: Exception) {
                    ErrorHandler.logError(
                        tag = "EventRepository",
                        message = "Failed to delete all events",
                        throwable = e,
                        context = mapOf(
                            "operation" to "deleteAllEvents"
                        )
                    )
                    throw CalendarError.DatabaseError.deleteFailed("events")
                }
            }
            Result.Success(Unit)
        } catch (e: Exception) {
            Result.Error(ErrorHandler.handleException(e))
        }
    }
    
    override fun searchEvents(query: String): Flow<Result<List<Event>>> {
        return flow {
            // Validate search query
            if (query.isBlank()) {
                val error = CalendarError.ValidationError.requiredField("Search query")
                emit(Result.Error(error))
                return@flow
            }
            
            if (query.length < 2) {
                val error = CalendarError.ValidationError(
                    errorMessage = "Search query must be at least 2 characters long.",
                    field = "Search query"
                )
                emit(Result.Error(error))
                return@flow
            }
            
            emit(Result.Loading)
            eventDao.searchEvents(query)
                .catch { exception -> 
                    val error = ErrorHandler.handleException(exception)
                    ErrorHandler.logError(
                        tag = "EventRepository",
                        message = "Failed to search events",
                        throwable = exception,
                        context = mapOf(
                            "operation" to "searchEvents",
                            "query" to query
                        )
                    )
                    emit(Result.Error(error))
                }
                .collect { events -> 
                    try {
                        val domainEvents = events.map { it.toDomainModel() }
                        emit(Result.Success(domainEvents))
                    } catch (e: Exception) {
                        val error = CalendarError.DatabaseError.queryFailed("events")
                        ErrorHandler.logError(
                            tag = "EventRepository",
                            message = "Failed to map search results to domain model",
                            throwable = e
                        )
                        emit(Result.Error(error))
                    }
                }
        }
    }
    
    /**
     * Cleans up any events with invalid color values by replacing them with default colors.
     * This is a maintenance function to fix data integrity issues.
     */
    override suspend fun cleanupInvalidColors(): Result<Int> {
        return try {
            var fixedCount = 0
            
            // Get all events using first() to get a single emission
            val allEvents = eventDao.getAllEvents().first()
            
            allEvents.forEach { eventEntity ->
                val colorString = eventEntity.color
                
                // Check if the color is invalid using simple validation
                if (!isValidColorString(colorString)) {
                    // Update with default color
                    val updatedEntity = eventEntity.copy(color = "#009688") // Default teal
                    eventDao.updateEvent(updatedEntity)
                    fixedCount++
                    
                    ErrorHandler.logError(
                        tag = "EventRepository",
                        message = "Fixed invalid color for event ${eventEntity.id}: '$colorString' -> '#009688'"
                    )
                }
            }
            
            if (fixedCount > 0) {
                ErrorHandler.logError(
                    tag = "EventRepository",
                    message = "Color cleanup completed: fixed $fixedCount events with invalid colors"
                )
            }
            
            Result.Success(fixedCount)
        } catch (e: Exception) {
            ErrorHandler.logError(
                tag = "EventRepository", 
                message = "Failed to cleanup invalid colors",
                throwable = e,
                context = mapOf("operation" to "cleanupInvalidColors")
            )
            Result.Error(ErrorHandler.handleException(e))
        }
    }

    /**
     * Simple color string validation for cleanup purposes
     */
    private fun isValidColorString(colorString: String?): Boolean {
        if (colorString.isNullOrBlank()) return false
        
        val trimmed = colorString.trim()
        
        // Check for basic hex color format
        if (trimmed.startsWith("#")) {
            val hex = trimmed.substring(1)
            if (hex.length !in listOf(3, 6, 8)) return false
            return hex.all { it.isDigit() || it.lowercaseChar() in 'a'..'f' }
        }
        
        // Check for basic named colors
        val namedColors = setOf("red", "green", "blue", "yellow", "cyan", "magenta", "black", "white", "gray", "grey")
        return namedColors.contains(trimmed.lowercase())
    }

    /**
     * Validate event data before database operations
     */
    private fun validateEvent(event: Event) {
        if (event.id.isBlank()) {
            throw CalendarError.ValidationError.requiredField("Event ID")
        }
        
        if (event.title.isBlank()) {
            throw CalendarError.ValidationError.requiredField("Event title")
        }
        
        if (event.title.length > 200) {
            throw CalendarError.ValidationError(
                errorMessage = "Event title cannot exceed 200 characters.",
                field = "Event title"
            )
        }
        
        if (event.startDateTime.isAfter(event.endDateTime)) {
            throw CalendarError.ValidationError.invalidRange(
                "Event time",
                event.startDateTime.toString(),
                event.endDateTime.toString()
            )
        }
        
        event.description?.let { description ->
            if (description.length > 1000) {
                throw CalendarError.ValidationError(
                    errorMessage = "Event description cannot exceed 1000 characters.",
                    field = "Event description"
                )
            }
        }
    }
}
