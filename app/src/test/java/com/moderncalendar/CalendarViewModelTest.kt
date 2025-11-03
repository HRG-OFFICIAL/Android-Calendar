package com.moderncalendar

import com.moderncalendar.core.common.Result
import com.moderncalendar.core.common.model.Event
import com.moderncalendar.core.common.model.EventPriority
import com.moderncalendar.core.common.repository.EventRepository
import com.moderncalendar.feature.calendar.CalendarViewModel
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
class CalendarViewModelTest {
    private lateinit var viewModel: CalendarViewModel
    private lateinit var mockEventRepository: EventRepository
    private val testDispatcher = UnconfinedTestDispatcher()

    @Before
    fun setup() {
        Dispatchers.setMain(testDispatcher)
        mockEventRepository = mockk(relaxed = true)

        // Setup default mock responses
        coEvery { mockEventRepository.getEventsByDateRange(any(), any()) } returns flowOf(Result.Success(emptyList()))
        coEvery { mockEventRepository.insertEvent(any()) } returns Result.Success(Unit)
        coEvery { mockEventRepository.updateEvent(any()) } returns Result.Success(Unit)
        coEvery { mockEventRepository.deleteEvent(any()) } returns Result.Success(Unit)

        viewModel = CalendarViewModel(mockEventRepository)
    }

    @After
    fun tearDown() {
        Dispatchers.resetMain()
    }

    @Test
    fun `selectDate should update selected date and load events`() =
        runTest {
            // Given
            val testDate = LocalDate.of(2024, 1, 15)
            val testEvents =
                listOf(
                    Event(
                        id = "1",
                        title = "Test Event",
                        startDateTime = testDate.atStartOfDay(),
                        endDateTime = testDate.atTime(23, 59),
                        priority = EventPriority.MEDIUM,
                    ),
                )

            coEvery {
                mockEventRepository.getEventsByDateRange(any(), any())
            } returns flowOf(Result.Success(testEvents))

            // When
            viewModel.selectDate(testDate)

            // Then
            assertEquals(testDate, viewModel.selectedDate.value)
            assertTrue(viewModel.events.value is Result.Success)
        }

    @Test
    fun `createEvent should call repository and refresh events`() =
        runTest {
            // Given
            val testEvent =
                Event(
                    id = "1",
                    title = "New Event",
                    startDateTime = LocalDateTime.now(),
                    endDateTime = LocalDateTime.now().plusHours(1),
                    priority = EventPriority.MEDIUM,
                )

            coEvery {
                mockEventRepository.insertEvent(any())
            } returns Result.Success(Unit)

            coEvery {
                mockEventRepository.getEventsByDateRange(any(), any())
            } returns flowOf(Result.Success(emptyList()))

            // When
            viewModel.createEvent(testEvent)

            // Then
            coVerify { mockEventRepository.insertEvent(testEvent) }
            coVerify { mockEventRepository.getEventsByDateRange(any(), any()) }
        }

    @Test
    fun `deleteEvent should call repository and refresh events`() =
        runTest {
            // Given
            val eventId = "test-event-id"

            coEvery {
                mockEventRepository.deleteEvent(eventId)
            } returns Result.Success(Unit)

            coEvery {
                mockEventRepository.getEventsByDateRange(any(), any())
            } returns flowOf(Result.Success(emptyList()))

            // When
            viewModel.deleteEvent(eventId)

            // Then
            coVerify { mockEventRepository.deleteEvent(eventId) }
            coVerify { mockEventRepository.getEventsByDateRange(any(), any()) }
        }

    @Test
    fun `navigateToToday should set today as selected date`() =
        runTest {
            // Given
            val today = LocalDate.now()
            coEvery {
                mockEventRepository.getEventsByDateRange(any(), any())
            } returns flowOf(Result.Success(emptyList()))

            // When
            viewModel.navigateToToday()

            // Then
            assertEquals(today, viewModel.selectedDate.value)
        }

    @Test
    fun `loadEventsForDateRange should update events for given range`() =
        runTest {
            // Given
            val startDate = LocalDate.of(2024, 1, 1)
            val endDate = LocalDate.of(2024, 1, 31)
            val testEvents =
                listOf(
                    Event(
                        id = "1",
                        title = "January Event",
                        startDateTime = startDate.atStartOfDay(),
                        endDateTime = startDate.atTime(23, 59),
                        priority = EventPriority.MEDIUM,
                    ),
                )

            coEvery {
                mockEventRepository.getEventsByDateRange(any(), any())
            } returns flowOf(Result.Success(testEvents))

            // When
            viewModel.loadEventsForDateRange(startDate, endDate)

            // Then
            assertTrue(viewModel.events.value is Result.Success)
        }
}
