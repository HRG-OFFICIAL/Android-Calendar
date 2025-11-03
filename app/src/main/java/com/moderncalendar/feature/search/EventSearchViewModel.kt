package com.moderncalendar.feature.search

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.moderncalendar.core.common.CalendarError
import com.moderncalendar.core.common.DefaultErrorStateManager
import com.moderncalendar.core.common.ErrorHandler
import com.moderncalendar.core.common.RecoveryAction
import com.moderncalendar.core.data.dao.EventDao
import com.moderncalendar.core.data.entity.EventEntity
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.FlowPreview
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.debounce
import kotlinx.coroutines.flow.distinctUntilChanged
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.launch
import java.time.LocalDateTime
import javax.inject.Inject

@OptIn(FlowPreview::class, ExperimentalCoroutinesApi::class)
@HiltViewModel
class EventSearchViewModel @Inject constructor(
    private val eventDao: EventDao
) : ViewModel() {
    
    // Error state management
    private val errorStateManager = DefaultErrorStateManager()
    val errorState = errorStateManager.errorState
    
    fun clearError() = errorStateManager.clearError()
    fun handleError(error: CalendarError) = errorStateManager.handleError(error)
    fun handleError(throwable: Throwable) = errorStateManager.handleError(throwable)
    suspend fun retryLastOperation() = errorStateManager.retryLastOperation()
    fun hasError() = errorStateManager.hasError()
    fun isCurrentErrorRecoverable() = errorStateManager.canRetry()
    fun getCurrentErrorRecoveryActions() = errorStateManager.getRecoveryActions()
    
    private val _searchQuery = MutableStateFlow("")
    val searchQuery: StateFlow<String> = _searchQuery.asStateFlow()
    
    private val _searchFilters = MutableStateFlow(SearchFilters())
    val searchFilters: StateFlow<SearchFilters> = _searchFilters.asStateFlow()
    
    private val _isLoading = MutableStateFlow(false)
    val isLoading: StateFlow<Boolean> = _isLoading.asStateFlow()
    
    private val _searchResults = MutableStateFlow<List<EventSearchResult>>(emptyList())
    val searchResults: StateFlow<List<EventSearchResult>> = _searchResults.asStateFlow()
    
    private val _cachedResults = MutableStateFlow<List<EventSearchResult>>(emptyList())
    val cachedResults: StateFlow<List<EventSearchResult>> = _cachedResults.asStateFlow()
    
    private val _lastSuccessfulQuery = MutableStateFlow<String?>(null)
    val lastSuccessfulQuery: StateFlow<String?> = _lastSuccessfulQuery.asStateFlow()
    
    init {
        // Set up search with debouncing and filtering
        viewModelScope.launch {
            combine(
                _searchQuery.debounce(300), // Debounce search input
                _searchFilters
            ) { query: String, filters: SearchFilters ->
                query to filters
            }
            .distinctUntilChanged()
            .flatMapLatest { (query: String, filters: SearchFilters) ->
                if (query.isBlank()) {
                    flowOf(emptyList())
                } else {
                    performSearch(query, filters)
                }
            }
            .collect { results ->
                _searchResults.value = results
                _isLoading.value = false
                
                // Cache successful results
                if (results.isNotEmpty()) {
                    _cachedResults.value = results
                    _lastSuccessfulQuery.value = _searchQuery.value
                }
            }
        }
    }
    
    fun searchEvents(query: String) {
        _searchQuery.value = query
        _isLoading.value = true
        clearError()
    }
    
    fun updateSearchFilters(filters: SearchFilters) {
        _searchFilters.value = filters
    }
    
    fun clearSearch() {
        _searchQuery.value = ""
        _searchResults.value = emptyList()
        clearError()
    }
    
    private fun performSearch(
        query: String,
        filters: SearchFilters
    ): Flow<List<EventSearchResult>> {
        // Validate search query
        if (query.isBlank()) {
            return flowOf(emptyList())
        }
        
        if (query.length < 2) {
            val error = CalendarError.ValidationError(
                errorMessage = "Search query must be at least 2 characters long.",
                field = "Search query"
            )
            handleError(error)
            return flowOf(getCachedResultsForFailedSearch())
        }
        
        val eventsFlow = if (filters.dateRange != null) {
            eventDao.searchEventsInDateRange(
                query = query,
                startDate = filters.dateRange.start,
                endDate = filters.dateRange.end
            )
        } else {
            eventDao.searchEvents(
                query = query
            )
        }
        
        return eventsFlow
            .map { events: List<EventEntity> ->
                try {
                    events.map { event: EventEntity ->
                        EventSearchResult(
                            id = event.id,
                            title = event.title,
                            description = event.description ?: "",
                            location = event.location ?: "",
                            startTime = formatDateTime(event.startDateTime),
                            endTime = formatDateTime(event.endDateTime),
                            isAllDay = event.isAllDay,
                            priority = event.priority.name,
                            category = event.category ?: "",
                            color = event.color,
                            isCompleted = false
                        )
                    }.filter { result: EventSearchResult ->
                        // Apply category filter
                        filters.category == null || result.category == filters.category
                    }.filter { result: EventSearchResult ->
                        // Apply priority filter
                        filters.priority == null || result.priority == filters.priority.name
                    }.filter { result: EventSearchResult ->
                        // Apply completion status filter
                        filters.showCompleted || !result.isCompleted
                    }
                } catch (e: Exception) {
                    ErrorHandler.logError(
                        tag = "EventSearchViewModel",
                        message = "Failed to map search results",
                        throwable = e,
                        context = mapOf("query" to query)
                    )
                    throw CalendarError.DatabaseError(
                        errorMessage = "Failed to process search results.",
                        operation = "SEARCH_MAPPING"
                    )
                }
            }
            .catch { exception ->
                val error = ErrorHandler.handleException(exception)
                ErrorHandler.logError(
                    tag = "EventSearchViewModel",
                    message = "Search operation failed",
                    throwable = exception,
                    context = mapOf(
                        "query" to query,
                        "hasDateRange" to (filters.dateRange != null)
                    )
                )
                handleError(error)
                emit(getCachedResultsForFailedSearch())
            }
    }
    
    private fun formatDateTime(dateTime: LocalDateTime): String {
        return dateTime.format(java.time.format.DateTimeFormatter.ofPattern("MMM dd, yyyy 'at' h:mm a"))
    }
    
    fun getRecentSearches(): List<String> {
        // TODO: Implement recent searches storage
        return emptyList()
    }
    
    fun getSearchSuggestions(): List<String> {
        // TODO: Implement search suggestions based on existing events
        return emptyList()
    }
    
    /**
     * Get cached results when search fails
     */
    private fun getCachedResultsForFailedSearch(): List<EventSearchResult> {
        return if (_cachedResults.value.isNotEmpty()) {
            _cachedResults.value
        } else {
            emptyList()
        }
    }
    
    /**
     * Handle recovery actions from UI
     */
    fun handleRecoveryAction(action: RecoveryAction) {
        viewModelScope.launch {
            when (action) {
                is RecoveryAction.Retry -> {
                    retryLastOperation()
                }
                is RecoveryAction.Refresh -> {
                    refreshSearchResults()
                }
                is RecoveryAction.Dismiss -> {
                    clearError()
                }
                is RecoveryAction.Navigate -> {
                    // Handle navigation if needed
                    clearError()
                }
                else -> {
                    handleRecoveryAction(action)
                }
            }
        }
    }
    
    /**
     * Refresh search results
     */
    private suspend fun refreshSearchResults() {
        val currentQuery = _searchQuery.value
        if (currentQuery.isNotBlank()) {
            clearError()
            searchEvents(currentQuery)
        }
    }
    
    /**
     * Refresh data to retry current search
     */
    private suspend fun refreshData(): Boolean {
        return try {
            refreshSearchResults()
            true
        } catch (e: Exception) {
            handleError(e)
            false
        }
    }
    
    /**
     * Enable offline mode - show cached results
     */
    private suspend fun enableOfflineMode(): Boolean {
        clearError()
        _searchResults.value = _cachedResults.value
        return true
    }
    
    /**
     * Get search suggestions with error handling
     */
    fun getSearchSuggestionsWithErrorHandling(): List<String> {
        return try {
            getSearchSuggestions()
        } catch (e: Exception) {
            val error = CalendarError.DatabaseError(
                errorMessage = "Failed to load search suggestions.",
                operation = "GET_SUGGESTIONS"
            )
            handleError(error)
            emptyList()
        }
    }
    
    /**
     * Get recent searches with error handling
     */
    fun getRecentSearchesWithErrorHandling(): List<String> {
        return try {
            getRecentSearches()
        } catch (e: Exception) {
            val error = CalendarError.DatabaseError(
                errorMessage = "Failed to load recent searches.",
                operation = "GET_RECENT_SEARCHES"
            )
            handleError(error)
            emptyList()
        }
    }
    
    /**
     * Check if we have cached results available
     */
    fun hasCachedResults(): Boolean = _cachedResults.value.isNotEmpty()
    
    /**
     * Show cached results when search is unavailable
     */
    fun showCachedResults() {
        _searchResults.value = _cachedResults.value
        clearError()
    }
    
    /**
     * Get current error for UI display
     */
    fun getCurrentError(): CalendarError? = errorState.value
    
    /**
     * Check if there's an active error
     */
    fun hasActiveError(): Boolean = hasError()
    
    /**
     * Get available recovery actions for current error
     */
    fun getAvailableRecoveryActions(): List<RecoveryAction> = errorStateManager.getRecoveryActions()
}

data class SearchFilters(
    val dateRange: DateRange? = null,
    val category: String? = null,
    val priority: EventPriority? = null,
    val showCompleted: Boolean = false,
    val isAllDay: Boolean? = null
)

data class DateRange(
    val start: LocalDateTime,
    val end: LocalDateTime
)

data class EventSearchResult(
    val id: String,
    val title: String,
    val description: String,
    val location: String,
    val startTime: String,
    val endTime: String,
    val isAllDay: Boolean = false,
    val priority: String = "MEDIUM",
    val category: String = "",
    val color: String = "#009688",
    val isCompleted: Boolean = false
)

enum class EventPriority {
    LOW, MEDIUM, HIGH, URGENT
}