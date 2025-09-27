package com.moderncalendar.feature.events

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.moderncalendar.core.common.Result
import com.moderncalendar.core.data.entity.EventEntity
import com.moderncalendar.core.data.repository.EventRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import java.time.LocalDateTime
import javax.inject.Inject

@HiltViewModel
class EventViewModel @Inject constructor(
    private val eventRepository: EventRepository
) : ViewModel() {
    
    private val _events = MutableStateFlow<Result<List<EventEntity>>>(Result.Loading)
    val events: StateFlow<Result<List<EventEntity>>> = _events.asStateFlow()
    
    private val _isLoading = MutableStateFlow(false)
    val isLoading: StateFlow<Boolean> = _isLoading.asStateFlow()
    
    private val _searchQuery = MutableStateFlow("")
    val searchQuery: StateFlow<String> = _searchQuery.asStateFlow()
    
    fun loadEvents() {
        viewModelScope.launch {
            _isLoading.value = true
            eventRepository.getAllEvents()
                .collect { result ->
                    _events.value = result
                    _isLoading.value = false
                }
        }
    }
    
    fun loadEventsByDateRange(startDate: LocalDateTime, endDate: LocalDateTime) {
        viewModelScope.launch {
            _isLoading.value = true
            eventRepository.getEventsByDateRange(startDate, endDate)
                .collect { result ->
                    _events.value = result
                    _isLoading.value = false
                }
        }
    }
    
    fun createEvent(event: EventEntity) {
        viewModelScope.launch {
            _isLoading.value = true
            when (val result = eventRepository.insertEvent(event)) {
                is Result.Success -> {
                    loadEvents() // Refresh the list
                }
                is Result.Error -> {
                    _events.value = result
                }
                is Result.Loading -> {
                    // Handle loading state if needed
                }
            }
            _isLoading.value = false
        }
    }
    
    fun updateEvent(event: EventEntity) {
        viewModelScope.launch {
            _isLoading.value = true
            when (val result = eventRepository.updateEvent(event)) {
                is Result.Success -> {
                    loadEvents() // Refresh the list
                }
                is Result.Error -> {
                    _events.value = result
                }
                is Result.Loading -> {
                    // Handle loading state if needed
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
                    loadEvents() // Refresh the list
                }
                is Result.Error -> {
                    _events.value = result
                }
                is Result.Loading -> {
                    // Handle loading state if needed
                }
            }
            _isLoading.value = false
        }
    }
    
    fun searchEvents(query: String) {
        _searchQuery.value = query
        if (query.isNotEmpty()) {
            viewModelScope.launch {
                _isLoading.value = true
                eventRepository.searchEvents(query)
                    .collect { result ->
                        _events.value = result
                        _isLoading.value = false
                    }
            }
        } else {
            loadEvents()
        }
    }
    
    fun getEventById(eventId: String) {
        viewModelScope.launch {
            _isLoading.value = true
            when (val result = eventRepository.getEventById(eventId)) {
                is Result.Success -> {
                    result.data?.let { event ->
                        _events.value = Result.Success(listOf(event))
                    }
                }
                is Result.Error -> {
                    _events.value = result
                }
                is Result.Loading -> {
                    // Handle loading state if needed
                }
            }
            _isLoading.value = false
        }
    }
}
