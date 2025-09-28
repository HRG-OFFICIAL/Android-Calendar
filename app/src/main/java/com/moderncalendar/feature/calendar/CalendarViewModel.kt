package com.moderncalendar.feature.calendar

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.moderncalendar.core.common.Result
import com.moderncalendar.core.data.entity.EventEntity
import com.moderncalendar.core.data.repository.EventRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import java.time.LocalDate
import java.time.LocalDateTime
import javax.inject.Inject

@HiltViewModel
class CalendarViewModel @Inject constructor(
    private val eventRepository: EventRepository
) : ViewModel() {
    
    private val _selectedDate = MutableStateFlow(LocalDate.now())
    val selectedDate: StateFlow<LocalDate> = _selectedDate.asStateFlow()
    
    private val _selectedDates = MutableStateFlow(setOf<LocalDate>())
    val selectedDates: StateFlow<Set<LocalDate>> = _selectedDates.asStateFlow()
    
    private val _selectedDateRange = MutableStateFlow<ClosedRange<LocalDate>?>(null)
    val selectedDateRange: StateFlow<ClosedRange<LocalDate>?> = _selectedDateRange.asStateFlow()
    
    private val _events = MutableStateFlow<Result<List<EventEntity>>>(Result.Loading)
    val events: StateFlow<Result<List<EventEntity>>> = _events.asStateFlow()
    
    private val _isLoading = MutableStateFlow(false)
    val isLoading: StateFlow<Boolean> = _isLoading.asStateFlow()
    
    private val _disabledDates = MutableStateFlow(setOf<LocalDate>())
    val disabledDates: StateFlow<Set<LocalDate>> = _disabledDates.asStateFlow()
    
    init {
        loadEventsForSelectedDate()
    }
    
    fun selectDate(date: LocalDate) {
        _selectedDate.value = date
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
                }
        }
    }
    
    fun loadEventsForDateRange(startDate: LocalDate, endDate: LocalDate) {
        viewModelScope.launch {
            _isLoading.value = true
            
            val startDateTime = startDate.atStartOfDay()
            val endDateTime = endDate.atTime(23, 59, 59)
            
            eventRepository.getEventsByDateRange(startDateTime, endDateTime)
                .collect { result ->
                    _events.value = result
                    _isLoading.value = false
                }
        }
    }
    
    fun createEvent(event: EventEntity) {
        viewModelScope.launch {
            eventRepository.insertEvent(event)
            loadEventsForSelectedDate()
        }
    }
    
    fun updateEvent(event: EventEntity) {
        viewModelScope.launch {
            eventRepository.updateEvent(event)
            loadEventsForSelectedDate()
        }
    }
    
    fun deleteEvent(eventId: String) {
        viewModelScope.launch {
            eventRepository.deleteEvent(eventId)
            loadEventsForSelectedDate()
        }
    }
    
    // Multiple date selection
    fun toggleDateSelection(date: LocalDate) {
        val currentSelection = _selectedDates.value.toMutableSet()
        if (date in currentSelection) {
            currentSelection.remove(date)
        } else {
            currentSelection.add(date)
        }
        _selectedDates.value = currentSelection
    }
    
    fun clearDateSelection() {
        _selectedDates.value = emptySet()
    }
    
    // Date range selection
    fun selectDateRange(startDate: LocalDate, endDate: LocalDate) {
        if (startDate <= endDate) {
            _selectedDateRange.value = startDate..endDate
        }
    }
    
    fun clearDateRange() {
        _selectedDateRange.value = null
    }
    
    // Disabled dates management
    fun addDisabledDate(date: LocalDate) {
        val currentDisabled = _disabledDates.value.toMutableSet()
        currentDisabled.add(date)
        _disabledDates.value = currentDisabled
    }
    
    fun removeDisabledDate(date: LocalDate) {
        val currentDisabled = _disabledDates.value.toMutableSet()
        currentDisabled.remove(date)
        _disabledDates.value = currentDisabled
    }
    
    fun setDisabledDates(dates: Set<LocalDate>) {
        _disabledDates.value = dates
    }
    
    // Programmatic navigation
    fun navigateToDate(date: LocalDate) {
        if (date !in _disabledDates.value) {
            _selectedDate.value = date
            loadEventsForSelectedDate()
        }
    }
    
    fun navigateToToday() {
        navigateToDate(LocalDate.now())
    }
    
    fun navigateToPreviousDay() {
        val previousDay = _selectedDate.value.minusDays(1)
        if (previousDay !in _disabledDates.value) {
            navigateToDate(previousDay)
        }
    }
    
    fun navigateToNextDay() {
        val nextDay = _selectedDate.value.plusDays(1)
        if (nextDay !in _disabledDates.value) {
            navigateToDate(nextDay)
        }
    }
}
