package com.moderncalendar

import androidx.test.ext.junit.runners.AndroidJUnit4
import androidx.test.platform.app.InstrumentationRegistry
import androidx.test.rule.ActivityTestRule
import com.moderncalendar.core.data.database.CalendarDatabase
import com.moderncalendar.core.data.entity.EventEntity
import com.moderncalendar.core.data.entity.CalendarEntity
import org.junit.After
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import java.time.LocalDateTime

@RunWith(AndroidJUnit4::class)
class CalendarIntegrationTest {
    
    @get:Rule
    val activityRule = ActivityTestRule(MainActivity::class.java)
    
    private lateinit var database: CalendarDatabase
    
    @Before
    fun setup() {
        val context = InstrumentationRegistry.getInstrumentation().targetContext
        database = CalendarDatabase.getDatabase(context)
    }
    
    @After
    fun tearDown() {
        database.close()
    }
    
    @Test
    fun testEventCreationAndRetrieval() {
        // Given
        val testEvent = EventEntity(
            id = "test-event-1",
            title = "Test Event",
            description = "Test Description",
            startDateTime = LocalDateTime.now(),
            endDateTime = LocalDateTime.now().plusHours(1),
            calendarId = "test-calendar-1"
        )
        
        val testCalendar = CalendarEntity(
            id = "test-calendar-1",
            name = "Test Calendar",
            color = "#FF5722"
        )
        
        // When
        runBlocking {
            database.calendarDao().insertCalendar(testCalendar)
            database.eventDao().insertEvent(testEvent)
        }
        
        // Then
        runBlocking {
            val retrievedEvent = database.eventDao().getEventById("test-event-1")
            assert(retrievedEvent != null)
            assert(retrievedEvent.title == "Test Event")
        }
    }
    
    @Test
    fun testEventUpdate() {
        // Given
        val testEvent = EventEntity(
            id = "test-event-2",
            title = "Original Title",
            startDateTime = LocalDateTime.now(),
            endDateTime = LocalDateTime.now().plusHours(1),
            calendarId = "test-calendar-2"
        )
        
        // When
        runBlocking {
            database.eventDao().insertEvent(testEvent)
            val updatedEvent = testEvent.copy(title = "Updated Title")
            database.eventDao().updateEvent(updatedEvent)
        }
        
        // Then
        runBlocking {
            val retrievedEvent = database.eventDao().getEventById("test-event-2")
            assert(retrievedEvent != null)
            assert(retrievedEvent.title == "Updated Title")
        }
    }
    
    @Test
    fun testEventDeletion() {
        // Given
        val testEvent = EventEntity(
            id = "test-event-3",
            title = "Event to Delete",
            startDateTime = LocalDateTime.now(),
            endDateTime = LocalDateTime.now().plusHours(1),
            calendarId = "test-calendar-3"
        )
        
        // When
        runBlocking {
            database.eventDao().insertEvent(testEvent)
            database.eventDao().deleteEvent(testEvent)
        }
        
        // Then
        runBlocking {
            val retrievedEvent = database.eventDao().getEventById("test-event-3")
            assert(retrievedEvent == null)
        }
    }
}
