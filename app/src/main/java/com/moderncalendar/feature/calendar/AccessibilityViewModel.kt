package com.moderncalendar.feature.calendar

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.moderncalendar.core.accessibility.AccessibilityManager
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class AccessibilityViewModel @Inject constructor(
    private val accessibilityManager: AccessibilityManager
) : ViewModel() {
    
    private val _isAccessibilityEnabled = MutableStateFlow(false)
    val isAccessibilityEnabled: StateFlow<Boolean> = _isAccessibilityEnabled.asStateFlow()
    
    private val _isTalkBackEnabled = MutableStateFlow(false)
    val isTalkBackEnabled: StateFlow<Boolean> = _isTalkBackEnabled.asStateFlow()
    
    private val _isSwitchAccessEnabled = MutableStateFlow(false)
    val isSwitchAccessEnabled: StateFlow<Boolean> = _isSwitchAccessEnabled.asStateFlow()
    
    private val _shouldUseHighContrast = MutableStateFlow(false)
    val shouldUseHighContrast: StateFlow<Boolean> = _shouldUseHighContrast.asStateFlow()
    
    private val _shouldUseLargeText = MutableStateFlow(false)
    val shouldUseLargeText: StateFlow<Boolean> = _shouldUseLargeText.asStateFlow()
    
    private val _accessibilityCapabilities = MutableStateFlow(setOf<String>())
    val accessibilityCapabilities: StateFlow<Set<String>> = _accessibilityCapabilities.asStateFlow()
    
    init {
        loadAccessibilityState()
    }
    
    fun getAccessibilityManager(): AccessibilityManager = accessibilityManager
    
    private fun loadAccessibilityState() {
        viewModelScope.launch {
            _isAccessibilityEnabled.value = accessibilityManager.isAccessibilityEnabled
            _isTalkBackEnabled.value = accessibilityManager.isScreenReaderEnabled
            _isSwitchAccessEnabled.value = accessibilityManager.isSwitchAccessEnabled
            _accessibilityCapabilities.value = buildSet {
                if (accessibilityManager.isHighContrastEnabled) add("High Contrast")
                if (accessibilityManager.isLargeTextEnabled) add("Large Text")
            }
            
            // Check for high contrast and large text preferences
            _shouldUseHighContrast.value = _accessibilityCapabilities.value.contains("High Contrast")
            _shouldUseLargeText.value = _accessibilityCapabilities.value.contains("Large Text")
        }
    }
    
    fun refreshAccessibilityState() {
        loadAccessibilityState()
    }
    
    fun announceForAccessibility(text: String) {
        accessibilityManager.announceForAccessibility(text)
    }
    
    fun isAccessibilityServiceEnabled(serviceName: String): Boolean {
        // Not supported by current manager API; return general state for now
        return accessibilityManager.isAccessibilityEnabled
    }
}
