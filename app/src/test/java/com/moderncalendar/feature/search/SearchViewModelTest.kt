package com.moderncalendar.feature.search

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import com.moderncalendar.core.common.Result
import com.moderncalendar.core.common.model.Event
import com.moderncalendar.core.common.model.EventPriority
import com.moderncalendar.core.common.repository.EventRepository
import io.mockk.coEvery
import io.mockk.mockk
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.test.StandardTestDispatcher
import kotlinx.coroutines.test.advanceUntilIdle
import kotlinx.coroutines.test.runTest
import kotlinx.coroutines.test.setMain
import org.junit.Assert.*
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import java.time.LocalDate
import java.time.LocalDateTime

@OptIn(ExperimentalCoroutinesApi::class)
class SearchViewModelTest {
    @get:Rule
    val instantTaskExecutorRule = InstantTaskExecutorRule()

    private val testDispatcher = StandardTestDispatcher()
    private val eventRepository = mockk<EventRepository>()
    private lateinit var viewModel: SearchViewModel

    private val sampleEvents =
        listOf(
            Event(
                id = "1",
                title = "Meeting",
                description = "Team meeting",
                location = "Office",
                startDateTime = LocalDateTime.of(2024, 1, 15, 9, 0),
                endDateTime = LocalDateTime.of(2024, 1, 15, 10, 0),
                isAllDay = false,
                color = "#FF0000",
                priority = EventPriority.HIGH,
            ),
            Event(
                id = "2",
                title = "Lunch",
                description = "Lunch with client",
                location = "Restaurant",
                startDateTime = LocalDateTime.of(2024, 1, 16, 12, 0),
                endDateTime = LocalDateTime.of(2024, 1, 16, 13, 0),
                isAllDay = false,
                color = "#00FF00",
                priority = EventPriority.MEDIUM,
            ),
            Event(
                id = "3",
                title = "Conference",
                description = "Tech conference",
                location = "Convention Center",
                startDateTime = LocalDateTime.of(2024, 1, 20, 9, 0),
                endDateTime = LocalDateTime.of(2024, 1, 20, 17, 0),
                isAllDay = false,
                color = "#0000FF",
                priority = EventPriority.LOW,
            ),
        )

    @Before
    fun setup() {
        Dispatchers.setMain(testDispatcher)
        viewModel = SearchViewModel(eventRepository)
    }

    @Test
    fun `searchEventsWithFilters filters by query correctly`() =
        runTest {
            coEvery { eventRepository.getAllEvents() } returns flowOf(Result.Success(sampleEvents))

            viewModel.searchEventsWithFilters(query = "meeting")
            advanceUntilIdle()

            val result = viewModel.searchResults.value
            assertTrue(result is Result.Success)
            val events = (result as Result.Success).data
            assertEquals(1, events.size)
            assertEquals("Meeting", events[0].title)
        }

    @Test
    fun `searchEventsWithFilters filters by priority correctly`() =
        runTest {
            coEvery { eventRepository.getAllEvents() } returns flowOf(Result.Success(sampleEvents))

            viewModel.searchEventsWithFilters(query = "", priority = EventPriority.HIGH)
            advanceUntilIdle()

            val result = viewModel.searchResults.value
            assertTrue(result is Result.Success)
            val events = (result as Result.Success).data
            assertEquals(1, events.size)
            assertEquals(EventPriority.HIGH, events[0].priority)
        }

    @Test
    fun `searchEventsWithFilters filters by date range correctly`() =
        runTest {
            coEvery { eventRepository.getAllEvents() } returns flowOf(Result.Success(sampleEvents))

            val startDate = LocalDate.of(2024, 1, 15)
            val endDate = LocalDate.of(2024, 1, 16)

            viewModel.searchEventsWithFilters(
                query = "",
                startDate = startDate,
                endDate = endDate,
            )
            advanceUntilIdle()

            val result = viewModel.searchResults.value
            assertTrue(result is Result.Success)
            val events = (result as Result.Success).data
            assertEquals(2, events.size)
            assertTrue(events.any { it.title == "Meeting" })
            assertTrue(events.any { it.title == "Lunch" })
        }

    @Test
    fun `searchEventsWithFilters filters by start date only`() =
        runTest {
            coEvery { eventRepository.getAllEvents() } returns flowOf(Result.Success(sampleEvents))

            val startDate = LocalDate.of(2024, 1, 16)

            viewModel.searchEventsWithFilters(
                query = "",
                startDate = startDate,
            )
            advanceUntilIdle()

            val result = viewModel.searchResults.value
            assertTrue(result is Result.Success)
            val events = (result as Result.Success).data
            assertEquals(2, events.size)
            assertTrue(events.any { it.title == "Lunch" })
            assertTrue(events.any { it.title == "Conference" })
        }

    @Test
    fun `searchEventsWithFilters filters by end date only`() =
        runTest {
            coEvery { eventRepository.getAllEvents() } returns flowOf(Result.Success(sampleEvents))

            val endDate = LocalDate.of(2024, 1, 16)

            viewModel.searchEventsWithFilters(
                query = "",
                endDate = endDate,
            )
            advanceUntilIdle()

            val result = viewModel.searchResults.value
            assertTrue(result is Result.Success)
            val events = (result as Result.Success).data
            assertEquals(2, events.size)
            assertTrue(events.any { it.title == "Meeting" })
            assertTrue(events.any { it.title == "Lunch" })
        }

    @Test
    fun `searchEventsWithFilters combines multiple filters`() =
        runTest {
            coEvery { eventRepository.getAllEvents() } returns flowOf(Result.Success(sampleEvents))

            val startDate = LocalDate.of(2024, 1, 15)
            val endDate = LocalDate.of(2024, 1, 16)

            viewModel.searchEventsWithFilters(
                query = "meeting",
                priority = EventPriority.HIGH,
                startDate = startDate,
                endDate = endDate,
            )
            advanceUntilIdle()

            val result = viewModel.searchResults.value
            assertTrue(result is Result.Success)
            val events = (result as Result.Success).data
            assertEquals(1, events.size)
            assertEquals("Meeting", events[0].title)
            assertEquals(EventPriority.HIGH, events[0].priority)
        }

    @Test
    fun `searchEventsWithFilters returns empty list when no matches`() =
        runTest {
            coEvery { eventRepository.getAllEvents() } returns flowOf(Result.Success(sampleEvents))

            viewModel.searchEventsWithFilters(query = "nonexistent")
            advanceUntilIdle()

            val result = viewModel.searchResults.value
            assertTrue(result is Result.Success)
            val events = (result as Result.Success).data
            assertTrue(events.isEmpty())
        }

    @Test
    fun `searchEventsWithFilters handles repository error`() =
        runTest {
            val error = Exception("Repository error")
            coEvery { eventRepository.getAllEvents() } returns flowOf(Result.Error(error))

            viewModel.searchEventsWithFilters(query = "test")
            advanceUntilIdle()

            val result = viewModel.searchResults.value
            assertTrue(result is Result.Error)
            assertEquals(error, (result as Result.Error).exception)
        }

    @Test
    fun `clearSearch resets search state`() =
        runTest {
            coEvery { eventRepository.getAllEvents() } returns flowOf(Result.Success(sampleEvents))

            viewModel.searchEventsWithFilters(query = "meeting")
            advanceUntilIdle()

            viewModel.clearSearch()

            assertEquals("", viewModel.searchQuery.value)
            assertTrue(viewModel.searchResults.value is Result.Loading)
        }
}
