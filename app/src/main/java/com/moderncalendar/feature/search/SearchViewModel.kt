package com.moderncalendar.feature.search

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.moderncalendar.core.common.Result
import com.moderncalendar.core.data.entity.EventEntity
import com.moderncalendar.core.data.repository.EventRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class SearchViewModel @Inject constructor(
    private val eventRepository: EventRepository
) : ViewModel() {
    
    private val _events = MutableStateFlow<Result<List<EventEntity>>>(Result.Success(emptyList()))
    val events: StateFlow<Result<List<EventEntity>>> = _events.asStateFlow()
    
    private val _isLoading = MutableStateFlow(false)
    val isLoading: StateFlow<Boolean> = _isLoading.asStateFlow()
    
    fun searchEvents(query: String) {
        if (query.isBlank()) {
            _events.value = Result.Success(emptyList())
            return
        }
        
        viewModelScope.launch {
            _isLoading.value = true
            eventRepository.searchEvents(query)
                .collect { result ->
                    _events.value = result
                    _isLoading.value = false
                }
        }
    }
    
    fun clearSearch() {
        _events.value = Result.Success(emptyList())
    }
}
