package com.moderncalendar.core.ui.components

import org.junit.Assert.*
import org.junit.Test
import java.time.LocalDate
import java.time.LocalTime

class DateTimeValidationTest {

    @Test
    fun `validateTimeRange returns null for valid time range`() {
        val startTime = LocalTime.of(9, 0)
        val endTime = LocalTime.of(10, 0)
        
        val result = DateTimeValidation.validateTimeRange(startTime, endTime)
        
        assertNull(result)
    }

    @Test
    fun `validateTimeRange returns error when end time is before start time`() {
        val startTime = LocalTime.of(10, 0)
        val endTime = LocalTime.of(9, 0)
        
        val result = DateTimeValidation.validateTimeRange(startTime, endTime)
        
        assertEquals("End time must be after start time", result)
    }

    @Test
    fun `validateTimeRange returns error when times are equal and not allowed`() {
        val startTime = LocalTime.of(9, 0)
        val endTime = LocalTime.of(9, 0)
        
        val result = DateTimeValidation.validateTimeRange(startTime, endTime, allowSameTime = false)
        
        assertEquals("End time must be different from start time", result)
    }

    @Test
    fun `validateTimeRange returns null when times are equal and allowed`() {
        val startTime = LocalTime.of(9, 0)
        val endTime = LocalTime.of(9, 0)
        
        val result = DateTimeValidation.validateTimeRange(startTime, endTime, allowSameTime = true)
        
        assertNull(result)
    }

    @Test
    fun `validateTimeRange returns null when either time is null`() {
        val startTime = LocalTime.of(9, 0)
        
        val result1 = DateTimeValidation.validateTimeRange(startTime, null)
        val result2 = DateTimeValidation.validateTimeRange(null, startTime)
        val result3 = DateTimeValidation.validateTimeRange(null, null)
        
        assertNull(result1)
        assertNull(result2)
        assertNull(result3)
    }

    @Test
    fun `validateDateRange returns null for valid date range`() {
        val startDate = LocalDate.of(2024, 1, 1)
        val endDate = LocalDate.of(2024, 1, 2)
        
        val result = DateTimeValidation.validateDateRange(startDate, endDate)
        
        assertNull(result)
    }

    @Test
    fun `validateDateRange returns null for same dates`() {
        val date = LocalDate.of(2024, 1, 1)
        
        val result = DateTimeValidation.validateDateRange(date, date)
        
        assertNull(result)
    }

    @Test
    fun `validateDateRange returns error when end date is before start date`() {
        val startDate = LocalDate.of(2024, 1, 2)
        val endDate = LocalDate.of(2024, 1, 1)
        
        val result = DateTimeValidation.validateDateRange(startDate, endDate)
        
        assertEquals("End date cannot be before start date", result)
    }

    @Test
    fun `validateDateRange returns null when either date is null`() {
        val date = LocalDate.of(2024, 1, 1)
        
        val result1 = DateTimeValidation.validateDateRange(date, null)
        val result2 = DateTimeValidation.validateDateRange(null, date)
        val result3 = DateTimeValidation.validateDateRange(null, null)
        
        assertNull(result1)
        assertNull(result2)
        assertNull(result3)
    }

    @Test
    fun `validateFutureDate returns null for future date`() {
        val futureDate = LocalDate.now().plusDays(1)
        
        val result = DateTimeValidation.validateFutureDate(futureDate)
        
        assertNull(result)
    }

    @Test
    fun `validateFutureDate returns null for today when allowed`() {
        val today = LocalDate.now()
        
        val result = DateTimeValidation.validateFutureDate(today, allowToday = true)
        
        assertNull(result)
    }

    @Test
    fun `validateFutureDate returns error for today when not allowed`() {
        val today = LocalDate.now()
        
        val result = DateTimeValidation.validateFutureDate(today, allowToday = false)
        
        assertEquals("Date must be in the future", result)
    }

    @Test
    fun `validateFutureDate returns error for past date`() {
        val pastDate = LocalDate.now().minusDays(1)
        
        val result = DateTimeValidation.validateFutureDate(pastDate)
        
        assertEquals("Date cannot be in the past", result)
    }

    @Test
    fun `validateFutureDate returns null when date is null`() {
        val result = DateTimeValidation.validateFutureDate(null)
        
        assertNull(result)
    }
}