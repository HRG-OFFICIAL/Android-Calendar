package com.moderncalendar.core.common

import java.time.LocalDate
import java.time.LocalDateTime
import java.time.LocalTime
import java.time.ZoneId
import java.time.format.DateTimeFormatter
import java.time.temporal.ChronoUnit
import java.time.temporal.WeekFields
import java.util.Locale

object DateTimeUtils {
    private val dateFormatter = DateTimeFormatter.ofPattern("MMM dd, yyyy")
    private val timeFormatter = DateTimeFormatter.ofPattern("HH:mm")
    private val dateTimeFormatter = DateTimeFormatter.ofPattern("MMM dd, yyyy HH:mm")

    /**
     * Format a LocalDate to a readable string
     */
    fun formatDate(date: LocalDate): String = date.format(dateFormatter)

    /**
     * Format a LocalTime to a readable string
     */
    fun formatTime(time: LocalTime): String = time.format(timeFormatter)

    /**
     * Format a LocalDateTime to a readable string
     */
    fun formatDateTime(dateTime: LocalDateTime): String = dateTime.format(dateTimeFormatter)

    /**
     * Get the start of day for a given date
     */
    fun startOfDay(date: LocalDate): LocalDateTime = date.atStartOfDay()

    /**
     * Get the end of day for a given date
     */
    fun endOfDay(date: LocalDate): LocalDateTime = date.atTime(23, 59, 59, 999_999_999)

    /**
     * Get the start of week for a given date
     */
    fun startOfWeek(date: LocalDate): LocalDate {
        val weekFields = WeekFields.of(Locale.getDefault())
        return date.with(weekFields.dayOfWeek(), 1)
    }

    /**
     * Get the end of week for a given date
     */
    fun endOfWeek(date: LocalDate): LocalDate {
        val weekFields = WeekFields.of(Locale.getDefault())
        return date.with(weekFields.dayOfWeek(), 7)
    }

    /**
     * Get the start of month for a given date
     */
    fun startOfMonth(date: LocalDate): LocalDate = date.withDayOfMonth(1)

    /**
     * Get the end of month for a given date
     */
    fun endOfMonth(date: LocalDate): LocalDate = date.withDayOfMonth(date.lengthOfMonth())

    /**
     * Get the start of year for a given date
     */
    fun startOfYear(date: LocalDate): LocalDate = date.withDayOfYear(1)

    /**
     * Get the end of year for a given date
     */
    fun endOfYear(date: LocalDate): LocalDate = date.withDayOfYear(date.lengthOfYear())

    /**
     * Check if two dates are on the same day
     */
    fun isSameDay(
        date1: LocalDate,
        date2: LocalDate,
    ): Boolean = date1 == date2

    /**
     * Check if two dates are on the same week
     */
    fun isSameWeek(
        date1: LocalDate,
        date2: LocalDate,
    ): Boolean {
        val weekFields = WeekFields.of(Locale.getDefault())
        return date1.get(weekFields.weekOfYear()) == date2.get(weekFields.weekOfYear()) &&
            date1.get(weekFields.weekBasedYear()) == date2.get(weekFields.weekBasedYear())
    }

    /**
     * Check if two dates are on the same month
     */
    fun isSameMonth(
        date1: LocalDate,
        date2: LocalDate,
    ): Boolean {
        return date1.year == date2.year && date1.month == date2.month
    }

    /**
     * Check if two dates are on the same year
     */
    fun isSameYear(
        date1: LocalDate,
        date2: LocalDate,
    ): Boolean = date1.year == date2.year

    /**
     * Get the number of days between two dates
     */
    fun daysBetween(
        startDate: LocalDate,
        endDate: LocalDate,
    ): Long {
        return ChronoUnit.DAYS.between(startDate, endDate)
    }

    /**
     * Get the number of hours between two date times
     */
    fun hoursBetween(
        startDateTime: LocalDateTime,
        endDateTime: LocalDateTime,
    ): Long {
        return ChronoUnit.HOURS.between(startDateTime, endDateTime)
    }

    /**
     * Get the number of minutes between two date times
     */
    fun minutesBetween(
        startDateTime: LocalDateTime,
        endDateTime: LocalDateTime,
    ): Long {
        return ChronoUnit.MINUTES.between(startDateTime, endDateTime)
    }

    /**
     * Check if a date is today
     */
    fun isToday(date: LocalDate): Boolean = date == LocalDate.now()

    /**
     * Check if a date is yesterday
     */
    fun isYesterday(date: LocalDate): Boolean = date == LocalDate.now().minusDays(1)

    /**
     * Check if a date is tomorrow
     */
    fun isTomorrow(date: LocalDate): Boolean = date == LocalDate.now().plusDays(1)

    /**
     * Check if a date is in the past
     */
    fun isPast(date: LocalDate): Boolean = date.isBefore(LocalDate.now())

    /**
     * Check if a date is in the future
     */
    fun isFuture(date: LocalDate): Boolean = date.isAfter(LocalDate.now())

    /**
     * Get the current time in milliseconds
     */
    fun currentTimeMillis(): Long = System.currentTimeMillis()

    /**
     * Convert LocalDateTime to milliseconds
     */
    fun toMillis(dateTime: LocalDateTime): Long {
        return dateTime.atZone(ZoneId.systemDefault()).toInstant().toEpochMilli()
    }

    /**
     * Convert milliseconds to LocalDateTime
     */
    fun fromMillis(millis: Long): LocalDateTime {
        return LocalDateTime.ofInstant(
            java.time.Instant.ofEpochMilli(millis),
            ZoneId.systemDefault(),
        )
    }
}
