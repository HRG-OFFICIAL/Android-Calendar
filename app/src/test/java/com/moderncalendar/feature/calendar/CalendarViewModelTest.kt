package com.moderncalendar.feature.calendar

import com.moderncalendar.core.common.Result
import com.moderncalendar.core.common.model.Event
import com.moderncalendar.core.common.model.EventPriority
import com.moderncalendar.core.common.repository.EventRepository
import io.mockk.coEvery
import io.mockk.mockk
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.test.*
import org.junit.After
import org.junit.Assert.*
import org.junit.Before
import org.junit.Test
import java.time.LocalDate
import java.time.LocalDateTime

@OptIn(ExperimentalCoroutinesApi::class)
class CalendarViewModelTest {
    
    private lateinit var viewModel: CalendarViewModel
    private lateinit var eventRepository: EventRepository
    private val testDispatcher = StandardTestDispatcher()
    
    @Before
    fun setup() {
        Dispatchers.setMain(testDispatcher)
        eventRepository = mockk(relaxed = true)
        
        // Setup default mock responses
        coEvery { eventRepository.getEventsByDateRange(any(), any()) } returns flowOf(Result.Success(emptyList()))
        coEvery { eventRepository.insertEvent(any()) } returns Result.Success(Unit)
        coEvery { eventRepository.updateEvent(any()) } returns Result.Success(Unit)
        coEvery { eventRepository.deleteEvent(any()) } returns Result.Success(Unit)
        
        viewModel = CalendarViewModel(eventRepository)
    }
    
    @After
    fun tearDown() {
        Dispatchers.resetMain()
    }
    
    @Test
    fun `selectDate updates selectedDate state`() = runTest {
        // Given
        val testDate = LocalDate.of(2024, 1, 15)
        
        // When
        viewModel.selectDate(testDate)
        
        // Then
        assertEquals(testDate, viewModel.selectedDate.value)
    }
    
    @Test
    fun `navigateToToday sets selectedDate to current date`() = runTest {
        // Given
        val today = LocalDate.now()
        
        // When
        viewModel.navigateToToday()
        
        // Then
        assertEquals(today, viewModel.selectedDate.value)
    }
    
    @Test
    fun `createEvent calls repository insertEvent`() = runTest {
        // Given
        val testEvent = Event(
            id = "test-id",
            title = "Test Event",
            startDateTime = LocalDateTime.now(),
            endDateTime = LocalDateTime.now().plusHours(1),
            priority = EventPriority.MEDIUM
        )
        
        coEvery { eventRepository.insertEvent(testEvent) } returns Result.Success(Unit)
        coEvery { eventRepository.getEventsByDateRange(any(), any()) } returns flowOf(Result.Success(emptyList()))
        
        // When
        viewModel.createEvent(testEvent)
        testDispatcher.scheduler.advanceUntilIdle()
        
        // Then
        assertFalse(viewModel.isLoading.value)
    }
    
    @Test
    fun `loadEventsForSelectedDate updates events state`() = runTest {
        // Given
        val testEvents = listOf(
            Event(
                id = "1",
                title = "Event 1",
                startDateTime = LocalDateTime.now(),
                endDateTime = LocalDateTime.now().plusHours(1),
                priority = EventPriority.HIGH
            ),
            Event(
                id = "2", 
                title = "Event 2",
                startDateTime = LocalDateTime.now().plusHours(2),
                endDateTime = LocalDateTime.now().plusHours(3),
                priority = EventPriority.LOW
            )
        )
        
        coEvery { eventRepository.getEventsByDateRange(any(), any()) } returns flowOf(Result.Success(testEvents))
        
        // When
        viewModel.selectDate(LocalDate.now())
        testDispatcher.scheduler.advanceUntilIdle()
        
        // Then
        val eventsResult = viewModel.events.value
        assertTrue(eventsResult is Result.Success)
        val successResult = eventsResult as Result.Success
        assertEquals(2, successResult.data.size)
        assertEquals("Event 1", successResult.data[0].title)
        assertEquals("Event 2", successResult.data[1].title)
    }
    
    @Test
    fun `deleteEvent calls repository deleteEvent`() = runTest {
        // Given
        val eventId = "test-event-id"
        
        coEvery { eventRepository.deleteEvent(eventId) } returns Result.Success(Unit)
        coEvery { eventRepository.getEventsByDateRange(any(), any()) } returns flowOf(Result.Success(emptyList()))
        
        // When
        viewModel.deleteEvent(eventId)
        testDispatcher.scheduler.advanceUntilIdle()
        
        // Then
        assertFalse(viewModel.isLoading.value)
    }
}