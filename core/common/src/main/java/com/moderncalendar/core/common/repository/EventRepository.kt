package com.moderncalendar.core.common.repository

import com.moderncalendar.core.common.Result
import com.moderncalendar.core.common.model.Event
import kotlinx.coroutines.flow.Flow
import java.time.LocalDate
import java.time.LocalDateTime

/**
 * Repository interface for event operations
 */
interface EventRepository {
    fun getAllEvents(): Flow<Result<List<Event>>>

    fun getEventsByDateRange(
        startDate: LocalDate,
        endDate: LocalDate,
    ): Flow<Result<List<Event>>>

    fun getEventsByDate(date: LocalDate): Flow<Result<List<Event>>>

    fun getEventsForDate(
        startDateTime: LocalDateTime,
        endDateTime: LocalDateTime,
    ): Flow<Result<List<Event>>>

    fun getEventById(eventId: String): Flow<Result<Event?>>

    suspend fun insertEvent(event: Event): Result<Unit>

    suspend fun updateEvent(event: Event): Result<Unit>

    suspend fun deleteEvent(eventId: String): Result<Unit>

    suspend fun deleteAllEvents(): Result<Unit>

    fun searchEvents(query: String): Flow<Result<List<Event>>>

    suspend fun cleanupInvalidColors(): Result<Int>
}
