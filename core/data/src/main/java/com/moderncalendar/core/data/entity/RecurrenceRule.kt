package com.moderncalendar.core.data.entity

import java.time.DayOfWeek
import java.time.LocalDateTime

data class RecurrenceRule(
    val frequency: RecurrenceFrequency,
    val interval: Int = 1,
    val count: Int? = null,
    val until: LocalDateTime? = null,
    val byDay: List<DayOfWeek> = emptyList(),
    val byMonth: List<Int> = emptyList(),
    val byMonthDay: List<Int> = emptyList(),
    val byYearDay: List<Int> = emptyList(),
    val byWeekNo: List<Int> = emptyList(),
    val bySetPos: List<Int> = emptyList(),
    val weekStart: DayOfWeek = DayOfWeek.MONDAY
)

enum class RecurrenceFrequency {
    DAILY,
    WEEKLY,
    MONTHLY,
    YEARLY
}
