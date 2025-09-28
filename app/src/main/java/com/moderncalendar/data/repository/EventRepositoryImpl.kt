package com.moderncalendar.data.repository

import com.moderncalendar.core.common.Result
import com.moderncalendar.core.data.entity.EventEntity
import com.moderncalendar.core.data.repository.EventRepository
import com.moderncalendar.core.data.dao.EventDao
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.map
import java.time.LocalDateTime
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class EventRepositoryImpl @Inject constructor(
    private val eventDao: EventDao
) : EventRepository {
    
    override fun getAllEvents(): Flow<Result<List<EventEntity>>> = 
        eventDao.getAllEvents().map { events ->
            Result.Success(events)
        }
    
    override fun getEventById(id: String): Flow<Result<EventEntity?>> = flow {
        try {
            val event = eventDao.getEventById(id)
            emit(Result.Success(event))
        } catch (e: Exception) {
            emit(Result.Error(e))
        }
    }
    
    override fun getEventsByDateRange(startDate: LocalDateTime, endDate: LocalDateTime): Flow<Result<List<EventEntity>>> = 
        eventDao.getEventsByDateRange(startDate, endDate).map { events ->
            Result.Success(events)
        }
    
    override fun searchEvents(query: String): Flow<Result<List<EventEntity>>> = 
        eventDao.searchEvents(query).map { events ->
            Result.Success(events)
        }
    
    override fun searchEventsByTitle(title: String): Flow<Result<List<EventEntity>>> = 
        eventDao.searchEvents(title).map { events ->
            Result.Success(events.filter { it.title.contains(title, ignoreCase = true) })
        }
    
    override fun searchEventsByDescription(description: String): Flow<Result<List<EventEntity>>> = 
        eventDao.searchEvents(description).map { events ->
            Result.Success(events.filter { it.description?.contains(description, ignoreCase = true) == true })
        }
    
    override fun searchEventsByLocation(location: String): Flow<Result<List<EventEntity>>> = 
        eventDao.searchEvents(location).map { events ->
            Result.Success(events.filter { it.location?.contains(location, ignoreCase = true) == true })
        }
    
    override fun insertEvent(event: EventEntity): Flow<Result<Unit>> = flow {
        try {
            eventDao.insertEvent(event)
            emit(Result.Success(Unit))
        } catch (e: Exception) {
            emit(Result.Error(e))
        }
    }
    
    override fun updateEvent(event: EventEntity): Flow<Result<Unit>> = flow {
        try {
            eventDao.updateEvent(event)
            emit(Result.Success(Unit))
        } catch (e: Exception) {
            emit(Result.Error(e))
        }
    }
    
    override fun deleteEvent(id: String): Flow<Result<Unit>> = flow {
        try {
            eventDao.deleteEvent(id)
            emit(Result.Success(Unit))
        } catch (e: Exception) {
            emit(Result.Error(e))
        }
    }
}
