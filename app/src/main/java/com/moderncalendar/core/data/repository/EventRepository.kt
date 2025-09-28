package com.moderncalendar.core.data.repository

import com.moderncalendar.core.common.Result
import com.moderncalendar.core.data.entity.EventEntity
import kotlinx.coroutines.flow.Flow
import java.time.LocalDateTime

interface EventRepository {
    fun getAllEvents(): Flow<Result<List<EventEntity>>>
    fun getEventById(id: String): Flow<Result<EventEntity?>>
    fun getEventsByDateRange(startDate: LocalDateTime, endDate: LocalDateTime): Flow<Result<List<EventEntity>>>
    fun searchEvents(query: String): Flow<Result<List<EventEntity>>>
    fun searchEventsByTitle(title: String): Flow<Result<List<EventEntity>>>
    fun searchEventsByDescription(description: String): Flow<Result<List<EventEntity>>>
    fun searchEventsByLocation(location: String): Flow<Result<List<EventEntity>>>
    fun insertEvent(event: EventEntity): Flow<Result<Unit>>
    fun updateEvent(event: EventEntity): Flow<Result<Unit>>
    fun deleteEvent(id: String): Flow<Result<Unit>>
}
