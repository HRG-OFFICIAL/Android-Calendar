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
    
    private val _events = MutableStateFlow<Result<List<EventEntity>>>(Result.Loading)
    val events: StateFlow<Result<List<EventEntity>>> = _events.asStateFlow()
    
    private val _isLoading = MutableStateFlow(false)
    val isLoading: StateFlow<Boolean> = _isLoading.asStateFlow()
    
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
}
