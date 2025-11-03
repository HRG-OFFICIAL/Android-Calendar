package com.moderncalendar.feature.events

import com.moderncalendar.core.ui.components.DateTimeValidation
import org.junit.Assert.*
import org.junit.Test
import java.time.LocalDate
import java.time.LocalTime

class EventValidationTest {
    @Test
    fun `event creation validation passes for valid input`() {
        val title = "Valid Event"
        val startDate = LocalDate.now().plusDays(1)
        val startTime = LocalTime.of(9, 0)
        val endTime = LocalTime.of(10, 0)

        val titleError = if (title.isBlank()) "Title is required" else null
        val dateError = DateTimeValidation.validateFutureDate(startDate, allowToday = true)
        val timeRangeError = DateTimeValidation.validateTimeRange(startTime, endTime)

        assertNull(titleError)
        assertNull(dateError)
        assertNull(timeRangeError)
    }

    @Test
    fun `event creation validation fails for blank title`() {
        val title = ""
        val startDate = LocalDate.now().plusDays(1)
        val startTime = LocalTime.of(9, 0)
        val endTime = LocalTime.of(10, 0)

        val titleError = if (title.isBlank()) "Title is required" else null
        val dateError = DateTimeValidation.validateFutureDate(startDate, allowToday = true)
        val timeRangeError = DateTimeValidation.validateTimeRange(startTime, endTime)

        assertEquals("Title is required", titleError)
        assertNull(dateError)
        assertNull(timeRangeError)
    }

    @Test
    fun `event creation validation fails for past date`() {
        val title = "Valid Event"
        val startDate = LocalDate.now().minusDays(1)
        val startTime = LocalTime.of(9, 0)
        val endTime = LocalTime.of(10, 0)

        val titleError = if (title.isBlank()) "Title is required" else null
        val dateError = DateTimeValidation.validateFutureDate(startDate, allowToday = true)
        val timeRangeError = DateTimeValidation.validateTimeRange(startTime, endTime)

        assertNull(titleError)
        assertEquals("Date cannot be in the past", dateError)
        assertNull(timeRangeError)
    }

    @Test
    fun `event creation validation fails for invalid time range`() {
        val title = "Valid Event"
        val startDate = LocalDate.now().plusDays(1)
        val startTime = LocalTime.of(10, 0)
        val endTime = LocalTime.of(9, 0)

        val titleError = if (title.isBlank()) "Title is required" else null
        val dateError = DateTimeValidation.validateFutureDate(startDate, allowToday = true)
        val timeRangeError = DateTimeValidation.validateTimeRange(startTime, endTime)

        assertNull(titleError)
        assertNull(dateError)
        assertEquals("End time must be after start time", timeRangeError)
    }

    @Test
    fun `event creation validation allows today's date`() {
        val title = "Valid Event"
        val startDate = LocalDate.now()
        val startTime = LocalTime.of(9, 0)
        val endTime = LocalTime.of(10, 0)

        val titleError = if (title.isBlank()) "Title is required" else null
        val dateError = DateTimeValidation.validateFutureDate(startDate, allowToday = true)
        val timeRangeError = DateTimeValidation.validateTimeRange(startTime, endTime)

        assertNull(titleError)
        assertNull(dateError)
        assertNull(timeRangeError)
    }

    @Test
    fun `event editing validation allows past dates for existing events`() {
        val title = "Valid Event"
        val startDate = LocalDate.now().minusDays(1) // Past date for existing event
        val startTime = LocalTime.of(9, 0)
        val endTime = LocalTime.of(10, 0)

        val titleError = if (title.isBlank()) "Title is required" else null
        // For editing, we don't validate future dates as strictly
        val timeRangeError = DateTimeValidation.validateTimeRange(startTime, endTime)

        assertNull(titleError)
        assertNull(timeRangeError)
    }

    @Test
    fun `all day event validation skips time range validation`() {
        val title = "All Day Event"
        val startDate = LocalDate.now().plusDays(1)
        val isAllDay = true

        val titleError = if (title.isBlank()) "Title is required" else null
        val dateError = DateTimeValidation.validateFutureDate(startDate, allowToday = true)
        val timeRangeError =
            if (!isAllDay) {
                DateTimeValidation.validateTimeRange(LocalTime.of(10, 0), LocalTime.of(9, 0))
            } else {
                null
            }

        assertNull(titleError)
        assertNull(dateError)
        assertNull(timeRangeError) // Should be null because it's all day
    }

    @Test
    fun `event validation with multiple errors`() {
        val title = ""
        val startDate = LocalDate.now().minusDays(1)
        val startTime = LocalTime.of(10, 0)
        val endTime = LocalTime.of(9, 0)

        val titleError = if (title.isBlank()) "Title is required" else null
        val dateError = DateTimeValidation.validateFutureDate(startDate, allowToday = true)
        val timeRangeError = DateTimeValidation.validateTimeRange(startTime, endTime)

        assertEquals("Title is required", titleError)
        assertEquals("Date cannot be in the past", dateError)
        assertEquals("End time must be after start time", timeRangeError)
    }
}
