package com.moderncalendar.feature.calendar

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.moderncalendar.core.common.CalendarError
import com.moderncalendar.core.common.DefaultErrorStateManager
import com.moderncalendar.core.common.RecoveryAction
import com.moderncalendar.core.common.Result
import com.moderncalendar.core.common.RetryConfig
import com.moderncalendar.core.common.utils.retryWithBackoff
import com.moderncalendar.core.common.model.Event
import com.moderncalendar.core.common.repository.EventRepository
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
    
    private val _selectedDate = MutableStateFlow(LocalDate.now())
    val selectedDate: StateFlow<LocalDate> = _selectedDate.asStateFlow()
    
    private val _selectedDates = MutableStateFlow(setOf<LocalDate>())
    val selectedDates: StateFlow<Set<LocalDate>> = _selectedDates.asStateFlow()
    
    private val _selectedDateRange = MutableStateFlow<ClosedRange<LocalDate>?>(null)
    val selectedDateRange: StateFlow<ClosedRange<LocalDate>?> = _selectedDateRange.asStateFlow()
    
    private val _events = MutableStateFlow<Result<List<Event>>>(Result.Loading)
    val events: StateFlow<Result<List<Event>>> = _events.asStateFlow()
    
    private val _monthEvents = MutableStateFlow<Result<List<Event>>>(Result.Loading)
    val monthEvents: StateFlow<Result<List<Event>>> = _monthEvents.asStateFlow()
    
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
            clearError() // Clear any previous errors
            
            val startDate = _selectedDate.value
            val endDate = _selectedDate.value
            
            eventRepository.getEventsByDateRange(startDate, endDate)
                .collect { result ->
                    _events.value = result
                    _isLoading.value = false
                    
                    // Handle errors from repository
                    if (result is Result.Error) {
                        handleError(result.exception)
                    }
                }
        }
    }
    
    fun loadEventsForDateRange(startDate: LocalDate, endDate: LocalDate) {
        viewModelScope.launch {
            _isLoading.value = true
            clearError()
            
            eventRepository.getEventsByDateRange(startDate, endDate)
                .collect { result ->
                    _events.value = result
                    _isLoading.value = false
                    
                    if (result is Result.Error) {
                        handleError(result.exception)
                    }
                }
        }
    }
    
    fun loadEventsForMonth(yearMonth: java.time.YearMonth) {
        viewModelScope.launch {
            val startDate = yearMonth.atDay(1)
            val endDate = yearMonth.atEndOfMonth()
            
            eventRepository.getEventsByDateRange(startDate, endDate)
                .collect { result ->
                    _monthEvents.value = result
                    
                    if (result is Result.Error) {
                        handleError(result.exception)
                    }
                }
        }
    }
    
    fun createEvent(event: Event) {
        viewModelScope.launch {
            try {
                val result = retryWithBackoff(
                    maxRetries = RetryConfig.DATABASE.maxRetries,
                    initialDelayMs = RetryConfig.DATABASE.initialDelayMs,
                    backoffMultiplier = RetryConfig.DATABASE.backoffFactor
                ) {
                    eventRepository.insertEvent(event)
                }
                
                when (result) {
                    is Result.Success -> {
                        clearError()
                        loadEventsForSelectedDate()
                        // Also refresh month events to update calendar view
                        loadEventsForMonth(java.time.YearMonth.from(_selectedDate.value))
                    }
                    is Result.Error -> {
                        handleError(result.exception)
                    }
                    is Result.Loading -> {
                        // Loading state handled by _isLoading
                    }
                }
            } catch (e: Exception) {
                handleError(e)
            }
        }
    }
    
    fun updateEvent(event: Event) {
        viewModelScope.launch {
            try {
                val result = retryWithBackoff(
                    maxRetries = RetryConfig.DATABASE.maxRetries,
                    initialDelayMs = RetryConfig.DATABASE.initialDelayMs,
                    backoffMultiplier = RetryConfig.DATABASE.backoffFactor
                ) {
                    eventRepository.updateEvent(event)
                }
                
                when (result) {
                    is Result.Success -> {
                        clearError()
                        loadEventsForSelectedDate()
                        // Also refresh month events to update calendar view
                        loadEventsForMonth(java.time.YearMonth.from(_selectedDate.value))
                    }
                    is Result.Error -> {
                        handleError(result.exception)
                    }
                    is Result.Loading -> {
                        // Loading state handled by _isLoading
                    }
                }
            } catch (e: Exception) {
                handleError(e)
            }
        }
    }
    
    fun deleteEvent(eventId: String) {
        viewModelScope.launch {
            try {
                val result = retryWithBackoff(
                    maxRetries = RetryConfig.DATABASE.maxRetries,
                    initialDelayMs = RetryConfig.DATABASE.initialDelayMs,
                    backoffMultiplier = RetryConfig.DATABASE.backoffFactor
                ) {
                    eventRepository.deleteEvent(eventId)
                }
                
                when (result) {
                    is Result.Success -> {
                        clearError()
                        loadEventsForSelectedDate()
                        // Also refresh month events to update calendar view
                        loadEventsForMonth(java.time.YearMonth.from(_selectedDate.value))
                    }
                    is Result.Error -> {
                        handleError(result.exception)
                    }
                    is Result.Loading -> {
                        // Loading state handled by _isLoading
                    }
                }
            } catch (e: Exception) {
                handleError(e)
            }
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
                    refreshData()
                }
                is RecoveryAction.Dismiss -> {
                    clearError()
                }
                else -> {
                    handleRecoveryAction(action)
                }
            }
        }
    }
    
    /**
     * Refresh data to reload current view
     */
    private suspend fun refreshData(): Boolean {
        return try {
            loadEventsForSelectedDate()
            true
        } catch (e: Exception) {
            handleError(e)
            false
        }
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