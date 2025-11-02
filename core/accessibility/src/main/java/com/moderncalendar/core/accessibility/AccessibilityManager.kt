package com.moderncalendar.core.accessibility

import android.accessibilityservice.AccessibilityServiceInfo
import android.content.Context
import android.content.res.Configuration
import android.view.accessibility.AccessibilityManager
// Compose semantics will be added when needed
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class AccessibilityManager @Inject constructor(
    private val context: Context
) {
    
    private val accessibilityManager = context.getSystemService(Context.ACCESSIBILITY_SERVICE) as AccessibilityManager
    
    // Accessibility state
    val isAccessibilityEnabled: Boolean
        get() = accessibilityManager.isEnabled
    
    val isTouchExplorationEnabled: Boolean
        get() = accessibilityManager.isTouchExplorationEnabled
    
    val isScreenReaderEnabled: Boolean
        get() = isAccessibilityServiceEnabled(AccessibilityServiceInfo.FEEDBACK_SPOKEN)
    
    val isSwitchAccessEnabled: Boolean
        get() = isAccessibilityServiceEnabled(AccessibilityServiceInfo.FEEDBACK_GENERIC)
    
    val isVoiceControlEnabled: Boolean
        get() = isAccessibilityServiceEnabled(AccessibilityServiceInfo.FEEDBACK_VISUAL)
    
    // Screen reader support
    fun announceForAccessibility(text: String) {
        if (isScreenReaderEnabled) {
            // This would typically be called from a View or Composable
            // For now, we'll store the announcement to be used by the UI
            lastAnnouncement = text
        }
    }
    
    private var lastAnnouncement: String? = null
    fun getLastAnnouncement(): String? = lastAnnouncement.takeIf { isScreenReaderEnabled }
    
    // High contrast support
    val isHighContrastEnabled: Boolean
        get() = context.resources.configuration.uiMode and Configuration.UI_MODE_NIGHT_MASK == Configuration.UI_MODE_NIGHT_YES
    
    // Font scaling
    val fontScale: Float
        get() = context.resources.configuration.fontScale
    
    val isLargeTextEnabled: Boolean
        get() = fontScale > 1.0f
    
    // Motor accessibility
    val isReduceMotionEnabled: Boolean
        get() = context.resources.configuration.uiMode and Configuration.UI_MODE_NIGHT_MASK == Configuration.UI_MODE_NIGHT_YES
    
    // Color accessibility
    val isColorBlindFriendly: Boolean
        get() = isHighContrastEnabled || isScreenReaderEnabled
    
    // Navigation accessibility
    val isKeyboardNavigationEnabled: Boolean
        get() = isAccessibilityEnabled && !isTouchExplorationEnabled
    
    // Custom accessibility properties for Compose (will be implemented when Compose is added)
    object AccessibilityProperties {
        // These will be implemented when Compose dependencies are added
    }
    
    data class EventCardAccessibilityInfo(
        val eventTitle: String,
        val eventTime: String,
        val eventLocation: String? = null,
        val eventDescription: String? = null,
        val isAllDay: Boolean = false,
        val priority: String = "Medium",
        val category: String? = null
    )
    
    data class CalendarDayAccessibilityInfo(
        val dayNumber: String,
        val dayName: String,
        val eventCount: Int,
        val hasEvents: Boolean,
        val isToday: Boolean = false,
        val isSelected: Boolean = false
    )
    
    data class ReminderItemAccessibilityInfo(
        val reminderTime: String,
        val eventTitle: String,
        val reminderType: String,
        val isActive: Boolean
    )
    
    // Accessibility helpers
    fun getAccessibilityDescription(
        eventTitle: String,
        eventTime: String,
        eventLocation: String? = null,
        eventDescription: String? = null
    ): String {
        val parts = mutableListOf<String>()
        
        parts.add("Event: $eventTitle")
        parts.add("Time: $eventTime")
        
        eventLocation?.let { parts.add("Location: $it") }
        eventDescription?.let { parts.add("Description: $it") }
        
        return parts.joinToString(". ")
    }
    
    fun getCalendarDayDescription(
        dayNumber: String,
        eventCount: Int,
        isToday: Boolean = false
    ): String {
        val parts = mutableListOf<String>()
        
        if (isToday) {
            parts.add("Today")
        }
        
        parts.add("Day $dayNumber")
        
        when (eventCount) {
            0 -> parts.add("No events")
            1 -> parts.add("1 event")
            else -> parts.add("$eventCount events")
        }
        
        return parts.joinToString(", ")
    }
    
    fun getReminderDescription(
        reminderTime: String,
        eventTitle: String,
        reminderType: String
    ): String {
        return "Reminder at $reminderTime for $eventTitle. Type: $reminderType"
    }
    
    // Accessibility service detection
    private fun isAccessibilityServiceEnabled(feedbackType: Int): Boolean {
        val enabledServices = accessibilityManager.getEnabledAccessibilityServiceList(feedbackType)
        return enabledServices.isNotEmpty()
    }
    
    // Accessibility preferences
    fun shouldUseHighContrast(): Boolean = isHighContrastEnabled
    
    fun shouldUseLargeText(): Boolean = isLargeTextEnabled
    
    fun shouldReduceMotion(): Boolean = isReduceMotionEnabled
    
    fun shouldUseColorBlindFriendlyPalette(): Boolean = isColorBlindFriendly
    
    // Screen reader specific
    fun getScreenReaderFriendlyText(text: String): String {
        return if (isScreenReaderEnabled) {
            // Add pauses and emphasis for screen readers
            text.replace(":", " colon ")
                .replace("-", " dash ")
                .replace("_", " underscore ")
        } else {
            text
        }
    }
    
    // Keyboard navigation
    fun getKeyboardNavigationHint(): String? {
        return if (isKeyboardNavigationEnabled) {
            "Use Tab to navigate, Enter to select, Escape to cancel"
        } else null
    }
    
    // Accessibility testing
    fun isAccessibilityTestingEnabled(): Boolean {
        return context.packageManager.hasSystemFeature("android.hardware.type.watch") ||
                context.resources.configuration.screenWidthDp < 600
    }
}

// Extension functions for Compose (will be implemented when Compose is added)
// These functions will be available when Compose dependencies are added to the module