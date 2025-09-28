package com.moderncalendar.core.accessibility

import android.content.Context
import android.view.accessibility.AccessibilityManager
import androidx.compose.ui.semantics.*
import androidx.compose.ui.test.SemanticsMatcher
import dagger.hilt.android.qualifiers.ApplicationContext
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class AccessibilityManager @Inject constructor(
    @ApplicationContext private val context: Context
) {
    
    private val accessibilityManager = context.getSystemService(Context.ACCESSIBILITY_SERVICE) as AccessibilityManager
    
    fun isAccessibilityEnabled(): Boolean {
        return accessibilityManager.isEnabled
    }
    
    fun isTalkBackEnabled(): Boolean {
        return accessibilityManager.isTouchExplorationEnabled
    }
    
    fun isSwitchAccessEnabled(): Boolean {
        return accessibilityManager.isAccessibilityEnabled
    }
    
    fun getAccessibilityServices(): List<String> {
        return accessibilityManager.getEnabledAccessibilityServiceList(
            android.accessibilityservice.AccessibilityServiceInfo.FEEDBACK_ALL_MASK
        ).map { it.resolveInfo.serviceInfo.packageName }
    }
    
    fun shouldUseHighContrast(): Boolean {
        return isAccessibilityEnabled() && 
               (isTalkBackEnabled() || isSwitchAccessEnabled())
    }
    
    fun shouldUseLargeText(): Boolean {
        return isAccessibilityEnabled()
    }
    
    fun shouldUseReducedMotion(): Boolean {
        return isAccessibilityEnabled()
    }
}

// Accessibility Semantics Extensions
fun SemanticsPropertyReceiver.eventSemantics(
    eventTitle: String,
    eventTime: String,
    eventLocation: String? = null,
    isAllDay: Boolean = false
) {
    contentDescription = buildString {
        append("Event: $eventTitle")
        if (isAllDay) {
            append(", All day")
        } else {
            append(", Time: $eventTime")
        }
        eventLocation?.let { append(", Location: $it") }
    }
    
    role = Role.Button
    stateDescription = "Double tap to open event details"
}

fun SemanticsPropertyReceiver.calendarDaySemantics(
    dayNumber: String,
    isSelected: Boolean,
    isToday: Boolean,
    hasEvents: Boolean
) {
    contentDescription = buildString {
        append("Day $dayNumber")
        if (isToday) append(", Today")
        if (isSelected) append(", Selected")
        if (hasEvents) append(", Has events")
    }
    
    role = Role.Button
    stateDescription = "Double tap to select this day"
}

fun SemanticsPropertyReceiver.calendarViewSemantics(
    viewType: String,
    selectedDate: String
) {
    contentDescription = "Calendar view: $viewType, showing $selectedDate"
    role = Role.Tab
    stateDescription = "Swipe left or right to change view"
}

fun SemanticsPropertyReceiver.createEventButtonSemantics() {
    contentDescription = "Create new event"
    role = Role.Button
    stateDescription = "Double tap to create a new event"
}

fun SemanticsPropertyReceiver.searchButtonSemantics() {
    contentDescription = "Search events"
    role = Role.Button
    stateDescription = "Double tap to search for events"
}

fun SemanticsPropertyReceiver.settingsButtonSemantics() {
    contentDescription = "Open settings"
    role = Role.Button
    stateDescription = "Double tap to open app settings"
}

// Accessibility Matchers for Testing
object AccessibilityMatchers {
    
    fun hasEventSemantics(
        eventTitle: String,
        eventTime: String,
        eventLocation: String? = null
    ): SemanticsMatcher {
        return SemanticsMatcher.expectValue(
            SemanticsProperties.ContentDescription,
            buildString {
                append("Event: $eventTitle")
                append(", Time: $eventTime")
                eventLocation?.let { append(", Location: $it") }
            }
        )
    }
    
    fun hasCalendarDaySemantics(
        dayNumber: String,
        isSelected: Boolean = false,
        isToday: Boolean = false,
        hasEvents: Boolean = false
    ): SemanticsMatcher {
        return SemanticsMatcher.expectValue(
            SemanticsProperties.ContentDescription,
            buildString {
                append("Day $dayNumber")
                if (isToday) append(", Today")
                if (isSelected) append(", Selected")
                if (hasEvents) append(", Has events")
            }
        )
    }
    
    fun hasCreateEventButtonSemantics(): SemanticsMatcher {
        return SemanticsMatcher.expectValue(
            SemanticsProperties.ContentDescription,
            "Create new event"
        )
    }
    
    fun hasSearchButtonSemantics(): SemanticsMatcher {
        return SemanticsMatcher.expectValue(
            SemanticsProperties.ContentDescription,
            "Search events"
        )
    }
    
    fun hasSettingsButtonSemantics(): SemanticsMatcher {
        return SemanticsMatcher.expectValue(
            SemanticsProperties.ContentDescription,
            "Open settings"
        )
    }
}

// Accessibility Actions
object AccessibilityActions {
    
    fun createEventAction(): SemanticsAction<() -> Unit> {
        return SemanticsAction("Create Event") { performAction ->
            performAction()
        }
    }
    
    fun searchEventsAction(): SemanticsAction<() -> Unit> {
        return SemanticsAction("Search Events") { performAction ->
            performAction()
        }
    }
    
    fun openSettingsAction(): SemanticsAction<() -> Unit> {
        return SemanticsAction("Open Settings") { performAction ->
            performAction()
        }
    }
    
    fun selectDateAction(): SemanticsAction<(String) -> Unit> {
        return SemanticsAction("Select Date") { performAction ->
            performAction("")
        }
    }
}
