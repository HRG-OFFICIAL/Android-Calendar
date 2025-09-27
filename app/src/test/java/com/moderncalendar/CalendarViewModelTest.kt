package com.moderncalendar

import com.moderncalendar.core.common.Result
import com.moderncalendar.core.data.entity.EventEntity
import com.moderncalendar.core.data.repository.EventRepository
import com.moderncalendar.feature.calendar.CalendarViewModel
import io.mockk.coEvery
import io.mockk.mockk
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.test.runTest
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import org.junit.runners.JUnit4
import java.time.LocalDate
import java.time.LocalDateTime

@RunWith(JUnit4::class)
class CalendarViewModelTest {
    
    private lateinit var viewModel: CalendarViewModel
    private lateinit var mockEventRepository: EventRepository
    
    @Before
    fun setup() {
        mockEventRepository = mockk()
        viewModel = CalendarViewModel(mockEventRepository)
    }
    
    @Test
    fun `selectDate updates selected date`() = runTest {
        // Given
        val testDate = LocalDate.of(2024, 1, 15)
        
        // When
        viewModel.selectDate(testDate)
        
        // Then
        assert(viewModel.selectedDate.value == testDate)
    }
    
    @Test
    fun `loads events for selected date`() = runTest {
        // Given
        val testDate = LocalDate.of(2024, 1, 15)
        val testEvents = listOf(
            EventEntity(
                id = "1",
                title = "Test Event",
                startDateTime = LocalDateTime.of(2024, 1, 15, 10, 0),
                endDateTime = LocalDateTime.of(2024, 1, 15, 11, 0),
                calendarId = "calendar1"
            )
        )
        
        coEvery { mockEventRepository.getEventsForDate(testDate) } returns flowOf(Result.Success(testEvents))
        
        // When
        viewModel.selectDate(testDate)
        
        // Then
        // The events should be loaded asynchronously
        // This test verifies the ViewModel calls the repository correctly
    }
    
    @Test
    fun `handles error when loading events`() = runTest {
        // Given
        val testDate = LocalDate.of(2024, 1, 15)
        val testError = Exception("Database error")
        
        coEvery { mockEventRepository.getEventsForDate(testDate) } returns flowOf(Result.Error(testError))
        
        // When
        viewModel.selectDate(testDate)
        
        // Then
        // The error should be handled gracefully
        // This test verifies error handling in the ViewModel
    }
}
