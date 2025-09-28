package com.moderncalendar

import com.moderncalendar.core.common.Result
import com.moderncalendar.core.data.entity.EventEntity
import com.moderncalendar.core.data.repository.EventRepository
import com.moderncalendar.feature.events.EventViewModel
import io.mockk.*
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.test.*
import org.junit.After
import org.junit.Before
import org.junit.Test
import java.time.LocalDateTime

@ExperimentalCoroutinesApi
class EventViewModelTest {

    private lateinit var viewModel: EventViewModel
    private lateinit var mockEventRepository: EventRepository
    private val testDispatcher = UnconfinedTestDispatcher()

    @Before
    fun setup() {
        Dispatchers.setMain(testDispatcher)
        mockEventRepository = mockk()
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
            EventEntity(
                id = "1",
                title = "Test Event 1",
                startDateTime = LocalDateTime.now(),
                endDateTime = LocalDateTime.now().plusHours(1),
                calendarId = "default"
            ),
            EventEntity(
                id = "2",
                title = "Test Event 2",
                startDateTime = LocalDateTime.now().plusDays(1),
                endDateTime = LocalDateTime.now().plusDays(1).plusHours(1),
                calendarId = "default"
            )
        )
        
        coEvery { 
            mockEventRepository.getAllEvents() 
        } returns flowOf(Result.Success(testEvents))

        // When
        viewModel.loadEvents()

        // Then
        assert(viewModel.events.value is Result.Success)
        assert((viewModel.events.value as Result.Success).data == testEvents)
        assert(!viewModel.isLoading.value)
    }

    @Test
    fun `createEvent should call repository and refresh events`() = runTest {
        // Given
        val testEvent = EventEntity(
            id = "1",
            title = "New Event",
            startDateTime = LocalDateTime.now(),
            endDateTime = LocalDateTime.now().plusHours(1),
            calendarId = "default"
        )
        
        coEvery { 
            mockEventRepository.insertEvent(any()) 
        } returns flowOf(Result.Success(Unit))
        
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
        val testEvent = EventEntity(
            id = "1",
            title = "Updated Event",
            startDateTime = LocalDateTime.now(),
            endDateTime = LocalDateTime.now().plusHours(1),
            calendarId = "default"
        )
        
        coEvery { 
            mockEventRepository.updateEvent(any()) 
        } returns flowOf(Result.Success(Unit))
        
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
        } returns flowOf(Result.Success(Unit))
        
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
        val testEvent = EventEntity(
            id = eventId,
            title = "Specific Event",
            startDateTime = LocalDateTime.now(),
            endDateTime = LocalDateTime.now().plusHours(1),
            calendarId = "default"
        )
        
        coEvery { 
            mockEventRepository.getEventById(eventId) 
        } returns flowOf(Result.Success(testEvent))

        // When
        viewModel.getEventById(eventId)

        // Then
        assert(viewModel.selectedEvent.value == testEvent)
        coVerify { mockEventRepository.getEventById(eventId) }
    }

    @Test
    fun `selectEvent should update selected event`() = runTest {
        // Given
        val testEvent = EventEntity(
            id = "1",
            title = "Selected Event",
            startDateTime = LocalDateTime.now(),
            endDateTime = LocalDateTime.now().plusHours(1),
            calendarId = "default"
        )

        // When
        viewModel.selectEvent(testEvent)

        // Then
        assert(viewModel.selectedEvent.value == testEvent)
    }

    @Test
    fun `clearSelectedEvent should clear selected event`() = runTest {
        // Given
        val testEvent = EventEntity(
            id = "1",
            title = "Selected Event",
            startDateTime = LocalDateTime.now(),
            endDateTime = LocalDateTime.now().plusHours(1),
            calendarId = "default"
        )
        viewModel.selectEvent(testEvent)

        // When
        viewModel.clearSelectedEvent()

        // Then
        assert(viewModel.selectedEvent.value == null)
    }

    @Test
    fun `loadEventsByDateRange should fetch events for specific range`() = runTest {
        // Given
        val startDate = LocalDateTime.of(2024, 1, 1, 0, 0)
        val endDate = LocalDateTime.of(2024, 1, 31, 23, 59)
        val testEvents = listOf(
            EventEntity(
                id = "1",
                title = "January Event",
                startDateTime = startDate,
                endDateTime = startDate.plusHours(1),
                calendarId = "default"
            )
        )
        
        coEvery { 
            mockEventRepository.getEventsByDateRange(startDate, endDate) 
        } returns flowOf(Result.Success(testEvents))

        // When
        viewModel.loadEventsByDateRange(startDate, endDate)

        // Then
        assert(viewModel.events.value is Result.Success)
        assert((viewModel.events.value as Result.Success).data == testEvents)
    }
}
