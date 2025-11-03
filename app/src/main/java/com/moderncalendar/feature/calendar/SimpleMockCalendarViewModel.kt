package com.moderncalendar.feature.calendar

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.moderncalendar.core.common.Result
import com.moderncalendar.core.common.model.Event
import com.moderncalendar.core.common.repository.EventRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import java.time.LocalDate
import java.time.YearMonth
import javax.inject.Inject

@HiltViewModel
class SimpleMockCalendarViewModel
    @Inject
    constructor(
        private val eventRepository: EventRepository,
    ) : ViewModel() {
        private val _selectedDate = MutableStateFlow(LocalDate.now())
        val selectedDate: StateFlow<LocalDate> = _selectedDate.asStateFlow()

        private val _events = MutableStateFlow<Result<List<Event>>>(Result.Success(emptyList()))
        val events: StateFlow<Result<List<Event>>> = _events.asStateFlow()

        private val _monthEvents = MutableStateFlow<Result<List<Event>>>(Result.Success(emptyList()))
        val monthEvents: StateFlow<Result<List<Event>>> = _monthEvents.asStateFlow()

        init {
            // Load today's events initially
            selectDate(LocalDate.now())
            loadEventsForMonth(YearMonth.now())
        }

        fun selectDate(date: LocalDate) {
            _selectedDate.value = date

            // Get events for the selected date from repository
            viewModelScope.launch {
                eventRepository.getEventsByDate(date).collect { result ->
                    _events.value = result
                }
            }
        }

        fun loadEventsForMonth(yearMonth: YearMonth) {
            // Load events for the entire month from repository
            viewModelScope.launch {
                val startOfMonth = yearMonth.atDay(1)
                val endOfMonth = yearMonth.atEndOfMonth()

                eventRepository.getEventsByDateRange(startOfMonth, endOfMonth).collect { result ->
                    _monthEvents.value = result
                }
            }
        }

        fun hasEventsForDate(date: LocalDate): Boolean {
            return when (val monthEventsResult = _monthEvents.value) {
                is Result.Success -> {
                    monthEventsResult.data.any { event ->
                        event.startDateTime.toLocalDate() == date
                    }
                }
                else -> false
            }
        }

        fun getEventCountForDate(date: LocalDate): Int {
            return when (val monthEventsResult = _monthEvents.value) {
                is Result.Success -> {
                    monthEventsResult.data.count { event ->
                        event.startDateTime.toLocalDate() == date
                    }
                }
                else -> 0
            }
        }
    }
