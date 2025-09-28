package com.moderncalendar.feature.events

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.moderncalendar.core.common.Result
import com.moderncalendar.core.data.entity.EventEntity
import com.moderncalendar.core.data.repository.EventRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import java.time.LocalDateTime
import javax.inject.Inject

@HiltViewModel
class EventViewModel @Inject constructor(
    private val eventRepository: EventRepository
) : ViewModel() {
    
    private val _events = MutableStateFlow<Result<List<EventEntity>>>(Result.Loading)
    val events: StateFlow<Result<List<EventEntity>>> = _events.asStateFlow()
    
    private val _selectedEvent = MutableStateFlow<EventEntity?>(null)
    val selectedEvent: StateFlow<EventEntity?> = _selectedEvent.asStateFlow()
    
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
    
    fun loadEventsByDateRange(startDate: LocalDateTime, endDate: LocalDateTime) {
        viewModelScope.launch {
            _isLoading.value = true
            eventRepository.getEventsByDateRange(startDate, endDate).collect { result ->
                _events.value = result
                _isLoading.value = false
            }
        }
    }
    
    fun createEvent(event: EventEntity) {
        viewModelScope.launch {
            _isLoading.value = true
            eventRepository.insertEvent(event).collect { result ->
                when (result) {
                    is Result.Success -> {
                        loadEvents() // Refresh the list
                    }
                    is Result.Error -> {
                        // Handle error
                    }
                    is Result.Loading -> {
                        // Handle loading
                    }
                }
                _isLoading.value = false
            }
        }
    }
    
    fun updateEvent(event: EventEntity) {
        viewModelScope.launch {
            _isLoading.value = true
            eventRepository.updateEvent(event).collect { result ->
                when (result) {
                    is Result.Success -> {
                        loadEvents() // Refresh the list
                    }
                    is Result.Error -> {
                        // Handle error
                    }
                    is Result.Loading -> {
                        // Handle loading
                    }
                }
                _isLoading.value = false
            }
        }
    }
    
    fun deleteEvent(eventId: String) {
        viewModelScope.launch {
            _isLoading.value = true
            eventRepository.deleteEvent(eventId).collect { result ->
                when (result) {
                    is Result.Success -> {
                        loadEvents() // Refresh the list
                    }
                    is Result.Error -> {
                        // Handle error
                    }
                    is Result.Loading -> {
                        // Handle loading
                    }
                }
                _isLoading.value = false
            }
        }
    }
    
    fun selectEvent(event: EventEntity) {
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
                        // Handle error
                    }
                    is Result.Loading -> {
                        // Handle loading
                    }
                }
                _isLoading.value = false
            }
        }
    }
}