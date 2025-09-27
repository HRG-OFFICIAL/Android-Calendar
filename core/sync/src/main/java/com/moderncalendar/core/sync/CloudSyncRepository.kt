package com.moderncalendar.core.sync

import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.Query
import com.moderncalendar.core.auth.AuthRepository
import com.moderncalendar.core.common.Result
import com.moderncalendar.core.data.entity.EventEntity
import com.moderncalendar.core.data.entity.CalendarEntity
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.tasks.await
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class CloudSyncRepository @Inject constructor(
    private val firestore: FirebaseFirestore,
    private val authRepository: AuthRepository
) {
    
    private val eventsCollection = "events"
    private val calendarsCollection = "calendars"
    
    fun syncEventsToCloud(): Flow<Result<List<EventEntity>>> = flow {
        emit(Result.Loading)
        try {
            val user = authRepository.currentUser
            if (user == null) {
                emit(Result.Error(Exception("User not authenticated")))
                return@flow
            }
            
            val events = firestore.collection(eventsCollection)
                .whereEqualTo("userId", user.uid)
                .orderBy("startDateTime", Query.Direction.ASCENDING)
                .get()
                .await()
                .toObjects(EventEntity::class.java)
            
            emit(Result.Success(events))
        } catch (exception: Exception) {
            emit(Result.Error(exception))
        }
    }
    
    suspend fun uploadEvent(event: EventEntity): Result<EventEntity> {
        return try {
            val user = authRepository.currentUser
            if (user == null) {
                return Result.Error(Exception("User not authenticated"))
            }
            
            val eventWithUserId = event.copy(
                syncId = event.id,
                isSynced = true
            )
            
            firestore.collection(eventsCollection)
                .document(event.id)
                .set(eventWithUserId)
                .await()
            
            Result.Success(eventWithUserId)
        } catch (exception: Exception) {
            Result.Error(exception)
        }
    }
    
    suspend fun updateEventInCloud(event: EventEntity): Result<EventEntity> {
        return try {
            val user = authRepository.currentUser
            if (user == null) {
                return Result.Error(Exception("User not authenticated"))
            }
            
            val updatedEvent = event.copy(
                isSynced = true,
                updatedAt = java.time.LocalDateTime.now()
            )
            
            firestore.collection(eventsCollection)
                .document(event.id)
                .set(updatedEvent)
                .await()
            
            Result.Success(updatedEvent)
        } catch (exception: Exception) {
            Result.Error(exception)
        }
    }
    
    suspend fun deleteEventFromCloud(eventId: String): Result<Unit> {
        return try {
            val user = authRepository.currentUser
            if (user == null) {
                return Result.Error(Exception("User not authenticated"))
            }
            
            firestore.collection(eventsCollection)
                .document(eventId)
                .delete()
                .await()
            
            Result.Success(Unit)
        } catch (exception: Exception) {
            Result.Error(exception)
        }
    }
    
    suspend fun syncCalendarsToCloud(): Flow<Result<List<CalendarEntity>>> = flow {
        emit(Result.Loading)
        try {
            val user = authRepository.currentUser
            if (user == null) {
                emit(Result.Error(Exception("User not authenticated")))
                return@flow
            }
            
            val calendars = firestore.collection(calendarsCollection)
                .whereEqualTo("userId", user.uid)
                .get()
                .await()
                .toObjects(CalendarEntity::class.java)
            
            emit(Result.Success(calendars))
        } catch (exception: Exception) {
            emit(Result.Error(exception))
        }
    }
    
    suspend fun uploadCalendar(calendar: CalendarEntity): Result<CalendarEntity> {
        return try {
            val user = authRepository.currentUser
            if (user == null) {
                return Result.Error(Exception("User not authenticated"))
            }
            
            val calendarWithUserId = calendar.copy(
                syncId = calendar.id,
                isSynced = true
            )
            
            firestore.collection(calendarsCollection)
                .document(calendar.id)
                .set(calendarWithUserId)
                .await()
            
            Result.Success(calendarWithUserId)
        } catch (exception: Exception) {
            Result.Error(exception)
        }
    }
    
    suspend fun getCloudEventsByDateRange(
        startDate: java.time.LocalDateTime,
        endDate: java.time.LocalDateTime
    ): Result<List<EventEntity>> {
        return try {
            val user = authRepository.currentUser
            if (user == null) {
                return Result.Error(Exception("User not authenticated"))
            }
            
            val events = firestore.collection(eventsCollection)
                .whereEqualTo("userId", user.uid)
                .whereGreaterThanOrEqualTo("startDateTime", startDate)
                .whereLessThanOrEqualTo("startDateTime", endDate)
                .orderBy("startDateTime", Query.Direction.ASCENDING)
                .get()
                .await()
                .toObjects(EventEntity::class.java)
            
            Result.Success(events)
        } catch (exception: Exception) {
            Result.Error(exception)
        }
    }
    
    suspend fun searchCloudEvents(query: String): Result<List<EventEntity>> {
        return try {
            val user = authRepository.currentUser
            if (user == null) {
                return Result.Error(Exception("User not authenticated"))
            }
            
            val events = firestore.collection(eventsCollection)
                .whereEqualTo("userId", user.uid)
                .whereArrayContains("searchKeywords", query.lowercase())
                .get()
                .await()
                .toObjects(EventEntity::class.java)
            
            Result.Success(events)
        } catch (exception: Exception) {
            Result.Error(exception)
        }
    }
}
