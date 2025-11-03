package com.moderncalendar.feature.events

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
import java.time.LocalDateTime
import javax.inject.Inject

@HiltViewModel
class EventViewModel @Inject constructor(
    private val eventRepository: EventRepository
) : ViewModel() {
    
    private val _events = MutableStateFlow<Result<List<Event>>>(Result.Loading)
    val events: StateFlow<Result<List<Event>>> = _events.asStateFlow()
    
    private val _selectedEvent = MutableStateFlow<Event?>(null)
    val selectedEvent: StateFlow<Event?> = _selectedEvent.asStateFlow()
    
    private val _isLoading = MutableStateFlow(false)
    val isLoading: StateFlow<Boolean> = _isLoading.asStateFlow()
    
    fun loadEvents() {
        viewModelScope.launch {
            _isLoading.value = true
            eventRepository.getAllEvents().collect { result ->
                _events.value = result
                _isLoading.value = false
            }
        }
    }
    
    fun loadEventsByDateRange(startDate: LocalDate, endDate: LocalDate) {
        viewModelScope.launch {
            _isLoading.value = true
            eventRepository.getEventsByDateRange(startDate, endDate).collect { result ->
                _events.value = result
                _isLoading.value = false
            }
        }
    }
    
    fun createEvent(event: Event) {
        viewModelScope.launch {
            _isLoading.value = true
            when (val result = eventRepository.insertEvent(event)) {
                is Result.Success -> loadEvents()
                is Result.Error -> {}
                is Result.Loading -> {}
            }
            _isLoading.value = false
        }
    }
    
    fun updateEvent(event: Event) {
        viewModelScope.launch {
            _isLoading.value = true
            when (val result = eventRepository.updateEvent(event)) {
                is Result.Success -> loadEvents()
                is Result.Error -> {}
                is Result.Loading -> {}
            }
            _isLoading.value = false
        }
    }
    
    fun deleteEvent(eventId: String) {
        viewModelScope.launch {
            _isLoading.value = true
            when (val result = eventRepository.deleteEvent(eventId)) {
                is Result.Success -> loadEvents()
                is Result.Error -> {}
                is Result.Loading -> {}
            }
            _isLoading.value = false
        }
    }
    
    fun selectEvent(event: Event) {
        _selectedEvent.value = event
    }
    
    fun clearSelectedEvent() {
        _selectedEvent.value = null
    }
    
    fun getEventById(eventId: String) {
        viewModelScope.launch {
            _isLoading.value = true
            eventRepository.getEventById(eventId).collect { result ->
                when (result) {
                    is Result.Success -> {
                        _selectedEvent.value = result.data
                    }
                    is Result.Error -> {
                        _selectedEvent.value = null
                    }
                    is Result.Loading -> {}
                }
                _isLoading.value = false
            }
        }
    }
}