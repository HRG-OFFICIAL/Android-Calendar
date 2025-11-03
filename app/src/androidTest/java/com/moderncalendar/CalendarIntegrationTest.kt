package com.moderncalendar

import androidx.room.Room
import androidx.test.core.app.ApplicationProvider
import androidx.test.ext.junit.runners.AndroidJUnit4
import com.moderncalendar.core.data.database.CalendarDatabase
import com.moderncalendar.core.data.dao.EventDao
import com.moderncalendar.core.data.entity.EventEntity
import com.moderncalendar.core.common.repository.EventRepository
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.test.runTest
import org.junit.After
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import java.time.LocalDateTime
import kotlin.test.assertEquals
import kotlin.test.assertNotNull
import kotlin.test.assertTrue

@RunWith(AndroidJUnit4::class)
class CalendarIntegrationTest {

    private lateinit var database: CalendarDatabase
    private lateinit var eventDao: EventDao
    private lateinit var eventRepository: EventRepository

    @Before
    fun setup() {
        database = Room.inMemoryDatabaseBuilder(
            ApplicationProvider.getApplicationContext(),
            CalendarDatabase::class.java
        ).allowMainThreadQueries().build()
        
        eventDao = database.eventDao()
        eventRepository = EventRepository(eventDao)
    }

    @After
    fun tearDown() {
        database.close()
    }

    @Test
    fun `create and retrieve event should work correctly`() = runTest {
        // Given
        val testEvent = EventEntity(
            id = "test-event-1",
            title = "Integration Test Event",
            description = "Test event for integration testing",
            startDateTime = LocalDateTime.of(2024, 1, 15, 10, 0),
            endDateTime = LocalDateTime.of(2024, 1, 15, 11, 0),
            location = "Test Location",
            calendarId = "default"
        )

        // When
        val insertResult = eventRepository.insertEvent(testEvent).first()
        val retrievedEvent = eventRepository.getEventById(testEvent.id).first()

        // Then
        assertTrue(insertResult is com.moderncalendar.core.common.Result.Success)
        assertTrue(retrievedEvent is com.moderncalendar.core.common.Result.Success)
        assertEquals(testEvent, (retrievedEvent as com.moderncalendar.core.common.Result.Success).data)
    }

    @Test
    fun `update event should modify existing event`() = runTest {
        // Given
        val originalEvent = EventEntity(
            id = "test-event-2",
            title = "Original Title",
            startDateTime = LocalDateTime.of(2024, 1, 15, 10, 0),
            endDateTime = LocalDateTime.of(2024, 1, 15, 11, 0),
            calendarId = "default"
        )
        
        val updatedEvent = originalEvent.copy(
            title = "Updated Title",
            description = "Updated description"
        )

        // When
        eventRepository.insertEvent(originalEvent).first()
        val updateResult = eventRepository.updateEvent(updatedEvent).first()
        val retrievedEvent = eventRepository.getEventById(originalEvent.id).first()

        // Then
        assertTrue(updateResult is com.moderncalendar.core.common.Result.Success)
        assertTrue(retrievedEvent is com.moderncalendar.core.common.Result.Success)
        assertEquals(updatedEvent, (retrievedEvent as com.moderncalendar.core.common.Result.Success).data)
    }

    @Test
    fun `delete event should remove event from database`() = runTest {
        // Given
        val testEvent = EventEntity(
            id = "test-event-3",
            title = "Event to Delete",
            startDateTime = LocalDateTime.of(2024, 1, 15, 10, 0),
            endDateTime = LocalDateTime.of(2024, 1, 15, 11, 0),
            calendarId = "default"
        )

        // When
        eventRepository.insertEvent(testEvent).first()
        val deleteResult = eventRepository.deleteEvent(testEvent.id).first()
        val retrievedEvent = eventRepository.getEventById(testEvent.id).first()

        // Then
        assertTrue(deleteResult is com.moderncalendar.core.common.Result.Success)
        assertTrue(retrievedEvent is com.moderncalendar.core.common.Result.Success)
        assertEquals(null, (retrievedEvent as com.moderncalendar.core.common.Result.Success).data)
    }

    @Test
    fun `search events should return matching events`() = runTest {
        // Given
        val events = listOf(
            EventEntity(
                id = "search-1",
                title = "Meeting with Team",
                description = "Discuss project progress",
                startDateTime = LocalDateTime.of(2024, 1, 15, 10, 0),
                endDateTime = LocalDateTime.of(2024, 1, 15, 11, 0),
                calendarId = "default"
            ),
            EventEntity(
                id = "search-2",
                title = "Lunch Break",
                description = "Team lunch at restaurant",
                startDateTime = LocalDateTime.of(2024, 1, 15, 12, 0),
                endDateTime = LocalDateTime.of(2024, 1, 15, 13, 0),
                calendarId = "default"
            ),
            EventEntity(
                id = "search-3",
                title = "Client Call",
                description = "Call with important client",
                startDateTime = LocalDateTime.of(2024, 1, 15, 14, 0),
                endDateTime = LocalDateTime.of(2024, 1, 15, 15, 0),
                calendarId = "default"
            )
        )

        // When
        events.forEach { event ->
            eventRepository.insertEvent(event).first()
        }
        
        val searchResult = eventRepository.searchEvents("team").first()

        // Then
        assertTrue(searchResult is com.moderncalendar.core.common.Result.Success)
        val foundEvents = (searchResult as com.moderncalendar.core.common.Result.Success).data
        assertEquals(2, foundEvents.size)
        assertTrue(foundEvents.any { it.title.contains("Team", ignoreCase = true) })
        assertTrue(foundEvents.any { it.description?.contains("team", ignoreCase = true) == true })
    }

    @Test
    fun `get events by date range should return events within range`() = runTest {
        // Given
        val events = listOf(
            EventEntity(
                id = "range-1",
                title = "Event in Range",
                startDateTime = LocalDateTime.of(2024, 1, 15, 10, 0),
                endDateTime = LocalDateTime.of(2024, 1, 15, 11, 0),
                calendarId = "default"
            ),
            EventEntity(
                id = "range-2",
                title = "Event Before Range",
                startDateTime = LocalDateTime.of(2024, 1, 10, 10, 0),
                endDateTime = LocalDateTime.of(2024, 1, 10, 11, 0),
                calendarId = "default"
            ),
            EventEntity(
                id = "range-3",
                title = "Event After Range",
                startDateTime = LocalDateTime.of(2024, 1, 20, 10, 0),
                endDateTime = LocalDateTime.of(2024, 1, 20, 11, 0),
                calendarId = "default"
            )
        )

        // When
        events.forEach { event ->
            eventRepository.insertEvent(event).first()
        }
        
        val startDate = LocalDateTime.of(2024, 1, 15, 0, 0)
        val endDate = LocalDateTime.of(2024, 1, 15, 23, 59)
        val rangeResult = eventRepository.getEventsByDateRange(startDate, endDate).first()

        // Then
        assertTrue(rangeResult is com.moderncalendar.core.common.Result.Success)
        val foundEvents = (rangeResult as com.moderncalendar.core.common.Result.Success).data
        assertEquals(1, foundEvents.size)
        assertEquals("Event in Range", foundEvents.first().title)
    }
}