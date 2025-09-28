package com.moderncalendar.feature.calendar

import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.semantics.*
import androidx.compose.ui.unit.dp
import com.moderncalendar.core.accessibility.AccessibilityManager
import com.moderncalendar.core.accessibility.AccessibilityMatchers
import com.moderncalendar.core.accessibility.AccessibilityActions
import com.moderncalendar.core.data.entity.EventEntity
import java.time.LocalDate
import java.time.format.DateTimeFormatter

@Composable
fun CalendarScreenAccessible(
    modifier: Modifier = Modifier,
    onEventClick: (String) -> Unit = {},
    onCreateEventClick: () -> Unit = {},
    onSearchClick: () -> Unit = {},
    onSettingsClick: () -> Unit = {},
    viewModel: CalendarViewModel = hiltViewModel(),
    accessibilityManager: AccessibilityManager = hiltViewModel<AccessibilityViewModel>().accessibilityManager
) {
    val selectedDate by viewModel.selectedDate.collectAsState()
    val events by viewModel.events.collectAsState()
    val isLoading by viewModel.isLoading.collectAsState()
    
    val isAccessibilityEnabled by remember { 
        derivedStateOf { accessibilityManager.isAccessibilityEnabled() }
    }
    
    val shouldUseHighContrast by remember { 
        derivedStateOf { accessibilityManager.shouldUseHighContrast() }
    }
    
    val shouldUseLargeText by remember { 
        derivedStateOf { accessibilityManager.shouldUseLargeText() }
    }
    
    Box(modifier = modifier.fillMaxSize()) {
        // Main calendar content with accessibility enhancements
        CalendarScreen(
            modifier = Modifier.fillMaxSize(),
            onEventClick = onEventClick,
            onCreateEventClick = onCreateEventClick,
            onSearchClick = onSearchClick,
            onSettingsClick = onSettingsClick,
            viewModel = viewModel
        )
        
        // Accessibility enhancements
        if (isAccessibilityEnabled) {
            AccessibilityEnhancements(
                selectedDate = selectedDate,
                events = events,
                isLoading = isLoading,
                shouldUseHighContrast = shouldUseHighContrast,
                shouldUseLargeText = shouldUseLargeText,
                onCreateEventClick = onCreateEventClick,
                onSearchClick = onSearchClick,
                onSettingsClick = onSettingsClick
            )
        }
    }
}

@Composable
private fun AccessibilityEnhancements(
    selectedDate: LocalDate,
    events: com.moderncalendar.core.common.Result<List<EventEntity>>,
    isLoading: Boolean,
    shouldUseHighContrast: Boolean,
    shouldUseLargeText: Boolean,
    onCreateEventClick: () -> Unit,
    onSearchClick: () -> Unit,
    onSettingsClick: () -> Unit
) {
    // High contrast overlay
    if (shouldUseHighContrast) {
        HighContrastOverlay()
    }
    
    // Large text overlay
    if (shouldUseLargeText) {
        LargeTextOverlay()
    }
    
    // Loading state announcement
    if (isLoading) {
        LoadingAnnouncement()
    }
    
    // Event count announcement
    when (events) {
        is com.moderncalendar.core.common.Result.Success -> {
            EventCountAnnouncement(events.data.size)
        }
        is com.moderncalendar.core.common.Result.Error -> {
            ErrorAnnouncement(events.exception.message ?: "Unknown error")
        }
        is com.moderncalendar.core.common.Result.Loading -> {
            // Loading state
        }
    }
}

@Composable
private fun HighContrastOverlay() {
    // High contrast styling would be applied here
    // This would typically involve using high contrast colors
    // and ensuring sufficient color contrast ratios
}

@Composable
private fun LargeTextOverlay() {
    // Large text styling would be applied here
    // This would typically involve using larger font sizes
    // and ensuring text is readable at larger sizes
}

@Composable
private fun LoadingAnnouncement() {
    // Announce loading state to screen readers
    Box(
        modifier = Modifier.semantics {
            contentDescription = "Loading events"
            stateDescription = "Please wait while events are being loaded"
        }
    )
}

@Composable
private fun EventCountAnnouncement(eventCount: Int) {
    // Announce event count to screen readers
    Box(
        modifier = Modifier.semantics {
            contentDescription = when (eventCount) {
                0 -> "No events scheduled"
                1 -> "1 event scheduled"
                else -> "$eventCount events scheduled"
            }
            stateDescription = "Use arrow keys to navigate through events"
        }
    )
}

@Composable
private fun ErrorAnnouncement(errorMessage: String) {
    // Announce error to screen readers
    Box(
        modifier = Modifier.semantics {
            contentDescription = "Error loading events: $errorMessage"
            stateDescription = "Press refresh to try again"
        }
    )
}

// Enhanced Event Item with Accessibility
@Composable
fun AccessibleEventItem(
    event: EventEntity,
    onClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    val eventTime = if (event.isAllDay) {
        "All day"
    } else {
        "${event.startDateTime.hour}:${event.startDateTime.minute.toString().padStart(2, '0')} - ${event.endDateTime.hour}:${event.endDateTime.minute.toString().padStart(2, '0')}"
    }
    
    Card(
        modifier = modifier
            .fillMaxWidth()
            .semantics {
                eventSemantics(
                    eventTitle = event.title,
                    eventTime = eventTime,
                    eventLocation = event.location,
                    isAllDay = event.isAllDay
                )
                onClick { onClick(); true }
            }
            .clickable { onClick() },
        colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.surfaceVariant
        )
    ) {
        Row(
            modifier = Modifier.padding(12.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            // Event color indicator with accessibility
            Box(
                modifier = Modifier
                    .size(12.dp)
                    .semantics {
                        contentDescription = "Event color indicator"
                        stateDescription = "Color: ${event.color}"
                    }
                    .background(
                        color = androidx.compose.ui.graphics.Color(android.graphics.Color.parseColor(event.color)),
                        shape = androidx.compose.foundation.shape.CircleShape
                    )
            )
            
            Spacer(modifier = Modifier.width(12.dp))
            
            Column(
                modifier = Modifier.weight(1f)
            ) {
                Text(
                    text = event.title,
                    style = MaterialTheme.typography.titleSmall,
                    fontWeight = FontWeight.Medium,
                    modifier = Modifier.semantics {
                        contentDescription = "Event title: ${event.title}"
                    }
                )
                
                Text(
                    text = eventTime,
                    style = MaterialTheme.typography.bodySmall,
                    color = MaterialTheme.colorScheme.onSurfaceVariant,
                    modifier = Modifier.semantics {
                        contentDescription = "Event time: $eventTime"
                    }
                )
                
                event.location?.let { location ->
                    Text(
                        text = location,
                        style = MaterialTheme.typography.bodySmall,
                        color = MaterialTheme.colorScheme.onSurfaceVariant,
                        modifier = Modifier.semantics {
                            contentDescription = "Event location: $location"
                        }
                    )
                }
            }
        }
    }
}

// Enhanced Calendar Day with Accessibility
@Composable
fun AccessibleCalendarDay(
    day: com.kizitonwose.calendar.core.CalendarDay,
    isSelected: Boolean,
    isToday: Boolean,
    hasEvents: Boolean,
    onClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    val dayNumber = day.date.dayOfMonth.toString()
    val isWeekend = day.date.dayOfWeek == java.time.DayOfWeek.SATURDAY || 
                   day.date.dayOfWeek == java.time.DayOfWeek.SUNDAY
    
    Box(
        modifier = modifier
            .aspectRatio(1f)
            .semantics {
                calendarDaySemantics(
                    dayNumber = dayNumber,
                    isSelected = isSelected,
                    isToday = isToday,
                    hasEvents = hasEvents
                )
                onClick { onClick(); true }
            }
            .clickable { onClick() },
        contentAlignment = Alignment.Center
    ) {
        Text(
            text = dayNumber,
            style = MaterialTheme.typography.bodyMedium,
            fontWeight = if (isToday || isSelected) FontWeight.Bold else FontWeight.Normal,
            color = when {
                isSelected -> MaterialTheme.colorScheme.onPrimary
                isToday -> MaterialTheme.colorScheme.onPrimaryContainer
                isWeekend -> MaterialTheme.colorScheme.onSurfaceVariant
                else -> MaterialTheme.colorScheme.onSurface
            }
        )
    }
}
