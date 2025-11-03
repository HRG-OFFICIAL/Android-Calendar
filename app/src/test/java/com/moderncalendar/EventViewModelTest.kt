package com.moderncalendar

import com.moderncalendar.core.common.Result
import com.moderncalendar.core.common.model.Event
import com.moderncalendar.core.common.model.EventPriority
import com.moderncalendar.core.common.repository.EventRepository
import com.moderncalendar.feature.events.EventViewModel
import io.mockk.*
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

@ExperimentalCoroutinesApi
class EventViewModelTest {

    private lateinit var viewModel: EventViewModel
    private lateinit var mockEventRepository: EventRepository
    private val testDispatcher = UnconfinedTestDispatcher()

    @Before
    fun setup() {
        Dispatchers.setMain(testDispatcher)
        mockEventRepository = mockk(relaxed = true)
        
        // Setup default mock responses
        coEvery { mockEventRepository.getAllEvents() } returns flowOf(Result.Success(emptyList()))
        coEvery { mockEventRepository.getEventsByDateRange(any(), any()) } returns flowOf(Result.Success(emptyList()))
        coEvery { mockEventRepository.getEventById(any()) } returns flowOf(Result.Success(null))
        coEvery { mockEventRepository.insertEvent(any()) } returns Result.Success(Unit)
        coEvery { mockEventRepository.updateEvent(any()) } returns Result.Success(Unit)
        coEvery { mockEventRepository.deleteEvent(any()) } returns Result.Success(Unit)
        
        viewModel = EventViewModel(mockEventRepository)
    }

    @After
    fun tearDown() {
        Dispatchers.resetMain()
    }

    @Test
    fun `loadEvents should fetch all events from repository`() = runTest {
        // Given
        val testEvents = listOf(
            Event(
                id = "1",
                title = "Test Event 1",
                startDateTime = LocalDateTime.now(),
                endDateTime = LocalDateTime.now().plusHours(1),
                priority = EventPriority.MEDIUM
            ),
            Event(
                id = "2",
                title = "Test Event 2",
                startDateTime = LocalDateTime.now().plusDays(1),
                endDateTime = LocalDateTime.now().plusDays(1).plusHours(1),
                priority = EventPriority.HIGH
            )
        )
        
        coEvery { 
            mockEventRepository.getAllEvents() 
        } returns flowOf(Result.Success(testEvents))

        // When
        viewModel.loadEvents()

        // Then
        assertTrue(viewModel.events.value is Result.Success)
        assertFalse(viewModel.isLoading.value)
    }

    @Test
    fun `createEvent should call repository and refresh events`() = runTest {
        // Given
        val testEvent = Event(
            id = "1",
            title = "New Event",
            startDateTime = LocalDateTime.now(),
            endDateTime = LocalDateTime.now().plusHours(1),
            priority = EventPriority.MEDIUM
        )
        
        coEvery { 
            mockEventRepository.insertEvent(any()) 
        } returns Result.Success(Unit)
        
        coEvery { 
            mockEventRepository.getAllEvents() 
        } returns flowOf(Result.Success(emptyList()))

        // When
        viewModel.createEvent(testEvent)

        // Then
        coVerify { mockEventRepository.insertEvent(testEvent) }
        coVerify { mockEventRepository.getAllEvents() }
    }

    @Test
    fun `updateEvent should call repository and refresh events`() = runTest {
        // Given
        val testEvent = Event(
            id = "1",
            title = "Updated Event",
            startDateTime = LocalDateTime.now(),
            endDateTime = LocalDateTime.now().plusHours(1),
            priority = EventPriority.MEDIUM
        )
        
        coEvery { 
            mockEventRepository.updateEvent(any()) 
        } returns Result.Success(Unit)
        
        coEvery { 
            mockEventRepository.getAllEvents() 
        } returns flowOf(Result.Success(emptyList()))

        // When
        viewModel.updateEvent(testEvent)

        // Then
        coVerify { mockEventRepository.updateEvent(testEvent) }
        coVerify { mockEventRepository.getAllEvents() }
    }

    @Test
    fun `deleteEvent should call repository and refresh events`() = runTest {
        // Given
        val eventId = "test-event-id"
        
        coEvery { 
            mockEventRepository.deleteEvent(eventId) 
        } returns Result.Success(Unit)
        
        coEvery { 
            mockEventRepository.getAllEvents() 
        } returns flowOf(Result.Success(emptyList()))

        // When
        viewModel.deleteEvent(eventId)

        // Then
        coVerify { mockEventRepository.deleteEvent(eventId) }
        coVerify { mockEventRepository.getAllEvents() }
    }

    @Test
    fun `getEventById should load specific event`() = runTest {
        // Given
        val eventId = "test-event-id"
        val testEvent = Event(
            id = eventId,
            title = "Specific Event",
            startDateTime = LocalDateTime.now(),
            endDateTime = LocalDateTime.now().plusHours(1),
            priority = EventPriority.MEDIUM
        )
        
        coEvery { 
            mockEventRepository.getEventById(eventId) 
        } returns flowOf(Result.Success(testEvent))

        // When
        viewModel.getEventById(eventId)

        // Then
        coVerify { mockEventRepository.getEventById(eventId) }
    }

    @Test
    fun `selectEvent should update selected event`() = runTest {
        // Given
        val testEvent = Event(
            id = "1",
            title = "Selected Event",
            startDateTime = LocalDateTime.now(),
            endDateTime = LocalDateTime.now().plusHours(1),
            priority = EventPriority.MEDIUM
        )

        // When
        viewModel.selectEvent(testEvent)

        // Then
        assertEquals(testEvent, viewModel.selectedEvent.value)
    }

    @Test
    fun `clearSelectedEvent should clear selected event`() = runTest {
        // Given
        val testEvent = Event(
            id = "1",
            title = "Selected Event",
            startDateTime = LocalDateTime.now(),
            endDateTime = LocalDateTime.now().plusHours(1),
            priority = EventPriority.MEDIUM
        )
        viewModel.selectEvent(testEvent)

        // When
        viewModel.clearSelectedEvent()

        // Then
        assertNull(viewModel.selectedEvent.value)
    }

    @Test
    fun `loadEventsByDateRange should fetch events for specific range`() = runTest {
        // Given
        val startDate = LocalDate.of(2024, 1, 1)
        val endDate = LocalDate.of(2024, 1, 31)
        val testEvents = listOf(
            Event(
                id = "1",
                title = "January Event",
                startDateTime = startDate.atStartOfDay(),
                endDateTime = startDate.atStartOfDay().plusHours(1),
                priority = EventPriority.MEDIUM
            )
        )
        
        coEvery { 
            mockEventRepository.getEventsByDateRange(startDate, endDate) 
        } returns flowOf(Result.Success(testEvents))

        // When
        viewModel.loadEventsByDateRange(startDate, endDate)

        // Then
        assertTrue(viewModel.events.value is Result.Success)
    }
}
