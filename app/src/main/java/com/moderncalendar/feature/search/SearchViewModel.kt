package com.moderncalendar.feature.search

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
import javax.inject.Inject

@HiltViewModel
class SearchViewModel @Inject constructor(
    private val eventRepository: EventRepository
) : ViewModel() {
    
    private val _searchQuery = MutableStateFlow("")
    val searchQuery: StateFlow<String> = _searchQuery.asStateFlow()
    
    private val _searchResults = MutableStateFlow<Result<List<EventEntity>>>(Result.Loading)
    val searchResults: StateFlow<Result<List<EventEntity>>> = _searchResults.asStateFlow()
    
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
    
    fun searchEventsByTitle(title: String) {
        viewModelScope.launch {
            _isSearching.value = true
            eventRepository.searchEventsByTitle(title).collect { result ->
                _searchResults.value = result
                _isSearching.value = false
            }
        }
    }
    
    fun searchEventsByDescription(description: String) {
        viewModelScope.launch {
            _isSearching.value = true
            eventRepository.searchEventsByDescription(description).collect { result ->
                _searchResults.value = result
                _isSearching.value = false
            }
        }
    }
    
    fun searchEventsByLocation(location: String) {
        viewModelScope.launch {
            _isSearching.value = true
            eventRepository.searchEventsByLocation(location).collect { result ->
                _searchResults.value = result
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
        currentSearches.remove(query) // Remove if already exists
        currentSearches.add(0, query) // Add to beginning
        
        // Keep only last 10 searches
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