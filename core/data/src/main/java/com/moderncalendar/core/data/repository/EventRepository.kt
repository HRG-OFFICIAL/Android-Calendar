package com.moderncalendar.core.data.repository

import com.moderncalendar.core.common.CalendarError
import com.moderncalendar.core.common.ErrorHandler
import com.moderncalendar.core.common.Result
import com.moderncalendar.core.common.model.Event
import com.moderncalendar.core.data.dao.EventDao
import com.moderncalendar.core.data.mapper.toDomainModel
import com.moderncalendar.core.data.mapper.toDataEntity
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.map
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
        startDate: LocalDateTime,
        endDate: LocalDateTime
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
            eventDao.getEventsInDateRange(startDate, endDate)
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
    
    override fun getEventsByDate(date: LocalDateTime): Flow<Result<List<Event>>> {
        val startOfDay = date.toLocalDate().atStartOfDay()
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
    
    override fun getEventsForDate(date: java.time.LocalDate): Flow<Result<List<Event>>> {
        val startOfDay = date.atStartOfDay()
        val endOfDay = startOfDay.plusDays(1)
        return flow {
            emit(Result.Loading)
            eventDao.getEventsInDateRange(startOfDay, endOfDay)
                .catch { exception -> 
                    val error = ErrorHandler.handleException(exception)
                    ErrorHandler.logError(
                        tag = "EventRepository",
                        message = "Failed to get events for date",
                        throwable = exception,
                        context = mapOf(
                            "operation" to "getEventsForDate",
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
        return ErrorHandler.retryWithBackoff(
            maxRetries = 2,
            initialDelayMs = 500,
            operation = {
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
        )
    }
    
    override suspend fun updateEvent(event: Event): Result<Unit> {
        return ErrorHandler.retryWithBackoff(
            maxRetries = 2,
            initialDelayMs = 500,
            operation = {
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
        )
    }
    
    override suspend fun deleteEvent(eventId: String): Result<Unit> {
        return ErrorHandler.retryWithBackoff(
            maxRetries = 2,
            initialDelayMs = 500,
            operation = {
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
        )
    }
    
    override suspend fun deleteAllEvents(): Result<Unit> {
        return ErrorHandler.retryWithBackoff(
            maxRetries = 2,
            initialDelayMs = 500,
            operation = {
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
        )
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
                    field = "Search query",
                    validationRule = "MIN_LENGTH"
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
                field = "Event title",
                validationRule = "MAX_LENGTH"
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
                    field = "Event description",
                    validationRule = "MAX_LENGTH"
                )
            }
        }
    }
}
