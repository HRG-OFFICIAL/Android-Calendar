package com.moderncalendar.core.data.repository

import com.moderncalendar.core.common.Result
import com.moderncalendar.core.data.dao.EventDao
import com.moderncalendar.core.data.entity.EventEntity
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.map
import java.time.LocalDateTime
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class EventRepository @Inject constructor(
    private val eventDao: EventDao
) {
    
    fun getAllEvents(): Flow<Result<List<EventEntity>>> {
        return eventDao.getAllEvents()
            .map { events -> Result.Success(events) }
            .catch { exception -> Result.Error(exception) }
    }
    
    fun getEventsByDateRange(
        startDate: LocalDateTime,
        endDate: LocalDateTime
    ): Flow<Result<List<EventEntity>>> {
        return eventDao.getEventsByDateRange(startDate, endDate)
            .map { events -> Result.Success(events) }
            .catch { exception -> Result.Error(exception) }
    }
    
    fun getEventsByDate(date: LocalDateTime): Flow<Result<List<EventEntity>>> {
        val startOfDay = date.toLocalDate().atStartOfDay()
        val endOfDay = startOfDay.plusDays(1)
        return eventDao.getEventsByDate(startOfDay, endOfDay)
            .map { events -> Result.Success(events) }
            .catch { exception -> Result.Error(exception) }
    }
    
    fun getEventsForDate(date: java.time.LocalDate): Flow<Result<List<EventEntity>>> {
        val startOfDay = date.atStartOfDay()
        val endOfDay = startOfDay.plusDays(1)
        return eventDao.getEventsByDate(startOfDay, endOfDay)
            .map { events -> Result.Success(events) }
            .catch { exception -> Result.Error(exception) }
    }
    
    suspend fun getEventById(eventId: String): Result<EventEntity?> {
        return try {
            val event = eventDao.getEventById(eventId)
            Result.Success(event)
        } catch (exception: Exception) {
            Result.Error(exception)
        }
    }
    
    suspend fun insertEvent(event: EventEntity): Result<Unit> {
        return try {
            eventDao.insertEvent(event)
            Result.Success(Unit)
        } catch (exception: Exception) {
            Result.Error(exception)
        }
    }
    
    suspend fun updateEvent(event: EventEntity): Result<Unit> {
        return try {
            eventDao.updateEvent(event)
            Result.Success(Unit)
        } catch (exception: Exception) {
            Result.Error(exception)
        }
    }
    
    suspend fun deleteEvent(eventId: String): Result<Unit> {
        return try {
            eventDao.deleteEvent(eventId)
            Result.Success(Unit)
        } catch (exception: Exception) {
            Result.Error(exception)
        }
    }
    
    fun searchEvents(query: String): Flow<Result<List<EventEntity>>> {
        return eventDao.searchEvents(query)
            .map { events -> Result.Success(events) }
            .catch { exception -> Result.Error(exception) }
    }
}
