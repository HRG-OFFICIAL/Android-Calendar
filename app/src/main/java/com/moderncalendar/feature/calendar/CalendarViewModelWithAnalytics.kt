package com.moderncalendar.feature.calendar

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.moderncalendar.core.analytics.AnalyticsManager
import com.moderncalendar.core.common.Result
import com.moderncalendar.core.common.model.Event
import com.moderncalendar.core.data.repository.EventRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import java.time.LocalDate
import java.time.LocalDateTime
import javax.inject.Inject

@HiltViewModel
class CalendarViewModelWithAnalytics @Inject constructor(
    private val eventRepository: EventRepository,
    private val analyticsManager: AnalyticsManager
) : ViewModel() {
    
    private val _selectedDate = MutableStateFlow(LocalDate.now())
    val selectedDate: StateFlow<LocalDate> = _selectedDate.asStateFlow()
    
    private val _events = MutableStateFlow<Result<List<Event>>>(Result.Loading)
    val events: StateFlow<Result<List<Event>>> = _events.asStateFlow()
    
    private val _isLoading = MutableStateFlow(false)
    val isLoading: StateFlow<Boolean> = _isLoading.asStateFlow()
    
    init {
        analyticsManager.trackEvent("app_opened")
        loadEventsForSelectedDate()
    }
    
    fun selectDate(date: LocalDate) {
        _selectedDate.value = date
        analyticsManager.trackEvent("date_selected", "date" to date.toString())
        loadEventsForSelectedDate()
    }
    
    private fun loadEventsForSelectedDate() {
        viewModelScope.launch {
            _isLoading.value = true
            
            val startOfDay = _selectedDate.value.atStartOfDay()
            val endOfDay = _selectedDate.value.atTime(23, 59, 59)
            
            eventRepository.getEventsByDateRange(startOfDay, endOfDay)
                .collect { result ->
                    _events.value = result
                    _isLoading.value = false
                    
                    when (result) {
                        is Result.Success -> {
                            analyticsManager.trackEvent(
                                "events_loaded",
                                "date" to _selectedDate.value.toString(),
                                "event_count" to result.data.size
                            )
                        }
                        is Result.Error -> {
                            analyticsManager.trackException(result.exception, "calendar_screen")
                        }
                        is Result.Loading -> {
                            // Loading state
                        }
                    }
                }
        }
    }
    
    fun createEvent(event: Event) {
        viewModelScope.launch {
            _isLoading.value = true
            
            when (val result = eventRepository.insertEvent(event)) {
                is Result.Success -> {
                    analyticsManager.trackEventCreated(
                        event.id,
                        if (event.isAllDay) "all_day" else "timed",
                        event.recurrenceRule != null
                    )
                    loadEventsForSelectedDate()
                }
                is Result.Error -> {
                    analyticsManager.trackException(result.exception, "calendar_screen")
                }
                is Result.Loading -> {
                    // Loading state
                }
            }
            _isLoading.value = false
        }
    }
    
    fun updateEvent(event: Event) {
        viewModelScope.launch {
            _isLoading.value = true
            
            when (val result = eventRepository.updateEvent(event)) {
                is Result.Success -> {
                    analyticsManager.trackEventUpdated(event.id, "update")
                    loadEventsForSelectedDate()
                }
                is Result.Error -> {
                    analyticsManager.trackException(result.exception, "calendar_screen")
                }
                is Result.Loading -> {
                    // Loading state
                }
            }
            _isLoading.value = false
        }
    }
    
    fun deleteEvent(eventId: String) {
        viewModelScope.launch {
            _isLoading.value = true
            
            when (val result = eventRepository.deleteEvent(eventId)) {
                is Result.Success -> {
                    analyticsManager.trackEventDeleted(eventId, "user_action")
                    loadEventsForSelectedDate()
                }
                is Result.Error -> {
                    analyticsManager.trackException(result.exception, "calendar_screen")
                }
                is Result.Loading -> {
                    // Loading state
                }
            }
            _isLoading.value = false
        }
    }
    
    fun changeCalendarView(viewType: String) {
        analyticsManager.trackEvent("calendar_view_changed", "view_type" to viewType)
    }
    
    fun navigateToToday() {
        val today = LocalDate.now()
        selectDate(today)
        analyticsManager.trackEvent("navigate_to_today")
    }
    
    fun navigateToPreviousDay() {
        val previousDay = _selectedDate.value.minusDays(1)
        selectDate(previousDay)
        analyticsManager.trackEvent("navigate_previous_day")
    }
    
    fun navigateToNextDay() {
        val nextDay = _selectedDate.value.plusDays(1)
        selectDate(nextDay)
        analyticsManager.trackEvent("navigate_next_day")
    }
    
    override fun onCleared() {
        super.onCleared()
        analyticsManager.trackAppBackgrounded()
    }
}
