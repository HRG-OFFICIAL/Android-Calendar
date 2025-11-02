package com.moderncalendar.feature.search

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.moderncalendar.core.common.Result
import com.moderncalendar.core.common.model.Event
import com.moderncalendar.core.common.model.EventPriority
import com.moderncalendar.core.data.repository.EventRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import java.time.LocalDate
import javax.inject.Inject

@HiltViewModel
class SearchViewModel @Inject constructor(
    private val eventRepository: EventRepository
) : ViewModel() {
    
    private val _searchQuery = MutableStateFlow("")
    val searchQuery: StateFlow<String> = _searchQuery.asStateFlow()
    
    private val _searchResults = MutableStateFlow<Result<List<Event>>>(Result.Loading)
    val searchResults: StateFlow<Result<List<Event>>> = _searchResults.asStateFlow()
    
    private val _isSearching = MutableStateFlow(false)
    val isSearching: StateFlow<Boolean> = _isSearching.asStateFlow()
    
    private val _recentSearches = MutableStateFlow<List<String>>(emptyList())
    val recentSearches: StateFlow<List<String>> = _recentSearches.asStateFlow()
    
    fun searchEvents(query: String) {
        if (query.isBlank()) return
        
        _searchQuery.value = query
        _isSearching.value = true
        
        viewModelScope.launch {
            eventRepository.searchEvents(query).collect { result ->
                _searchResults.value = result
                _isSearching.value = false
                
                if (result is Result.Success) {
                    addToRecentSearches(query)
                }
            }
        }
    }
    
    fun searchEventsWithFilters(
        query: String,
        priority: EventPriority? = null,
        startDate: LocalDate? = null,
        endDate: LocalDate? = null
    ) {
        _searchQuery.value = query
        _isSearching.value = true
        
        viewModelScope.launch {
            // Get all events first, then filter
            eventRepository.getAllEvents().collect { result ->
                when (result) {
                    is Result.Success -> {
                        val filteredEvents = result.data.filter { event ->
                            val matchesQuery = if (query.isBlank()) true else {
                                event.title.contains(query, ignoreCase = true) ||
                                event.description?.contains(query, ignoreCase = true) == true ||
                                event.location?.contains(query, ignoreCase = true) == true
                            }
                            
                            val matchesPriority = priority == null || event.priority == priority
                            
                            val matchesDateRange = if (startDate == null && endDate == null) {
                                true
                            } else {
                                val eventDate = event.startDateTime.toLocalDate()
                                val afterStart = startDate == null || !eventDate.isBefore(startDate)
                                val beforeEnd = endDate == null || !eventDate.isAfter(endDate)
                                afterStart && beforeEnd
                            }
                            
                            matchesQuery && matchesPriority && matchesDateRange
                        }
                        
                        _searchResults.value = Result.Success(filteredEvents)
                        
                        if (query.isNotBlank()) {
                            addToRecentSearches(query)
                        }
                    }
                    is Result.Error -> {
                        _searchResults.value = result
                    }
                    is Result.Loading -> {
                        _searchResults.value = result
                    }
                }
                _isSearching.value = false
            }
        }
    }
    
    fun clearSearch() {
        _searchQuery.value = ""
        _searchResults.value = Result.Loading
    }
    
    private fun addToRecentSearches(query: String) {
        val currentSearches = _recentSearches.value.toMutableList()
        currentSearches.remove(query)
        currentSearches.add(0, query)
        if (currentSearches.size > 10) {
            currentSearches.removeAt(currentSearches.size - 1)
        }
        _recentSearches.value = currentSearches
    }
    
    fun clearRecentSearches() {
        _recentSearches.value = emptyList()
    }
    
    fun removeRecentSearch(query: String) {
        val currentSearches = _recentSearches.value.toMutableList()
        currentSearches.remove(query)
        _recentSearches.value = currentSearches
    }
}