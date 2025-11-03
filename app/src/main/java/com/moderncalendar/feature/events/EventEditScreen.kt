package com.moderncalendar.feature.events

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import androidx.navigation.compose.rememberNavController
import com.moderncalendar.core.common.model.Event
import com.moderncalendar.core.common.model.EventPriority
import com.moderncalendar.core.ui.components.CalendarScaffold
import com.moderncalendar.core.ui.components.CalendarTextField
import com.moderncalendar.core.ui.components.DatePickerField
import com.moderncalendar.core.ui.components.TimePickerField
import com.moderncalendar.core.ui.components.TimeRangeField
import com.moderncalendar.core.ui.components.ColorPickerField
import com.moderncalendar.core.ui.components.SwitchField
import com.moderncalendar.core.ui.components.DateTimeValidation
import com.moderncalendar.core.ui.utils.ColorUtils
import com.moderncalendar.core.ui.theme.EventColors
import java.time.LocalDate
import java.time.LocalDateTime
import java.time.LocalTime

@Composable
fun EventEditScreen(
    eventId: String,
    modifier: Modifier = Modifier,
    onEventUpdated: () -> Unit = {},
    viewModel: EventViewModel = hiltViewModel(),
    navController: NavController = rememberNavController()
) {
    val selectedEvent by viewModel.selectedEvent.collectAsState()
    
    LaunchedEffect(eventId) {
        viewModel.getEventById(eventId)
    }
    
    selectedEvent?.let { event ->
        EventEditContent(
            event = event,
            onEventUpdated = onEventUpdated,
            viewModel = viewModel,
            navController = navController,
            modifier = modifier
        )
    }
}

@Composable
private fun EventEditContent(
    event: Event,
    onEventUpdated: () -> Unit,
    viewModel: EventViewModel,
    navController: NavController,
    modifier: Modifier = Modifier
) {
    var title by remember { mutableStateOf(event.title) }
    var description by remember { mutableStateOf(event.description ?: "") }
    var location by remember { mutableStateOf(event.location ?: "") }
    var isAllDay by remember { mutableStateOf(event.isAllDay) }
    var startDate by remember { mutableStateOf(event.startDateTime.toLocalDate()) }
    var startTime by remember { mutableStateOf(event.startDateTime.toLocalTime()) }
    var endTime by remember { mutableStateOf(event.endDateTime.toLocalTime()) }
    var selectedColor by remember { mutableStateOf(com.moderncalendar.core.ui.utils.ColorUtils.parseColorSafely(event.color)) }
    var priority by remember { mutableStateOf(event.priority) }
    
    // Validation states
    var titleError by remember { mutableStateOf<String?>(null) }
    
    // Validate time range
    val timeRangeError = if (!isAllDay) {
        DateTimeValidation.validateTimeRange(startTime, endTime)
    } else null
    
    CalendarScaffold(
        navController = navController,
        title = "Edit Event",
        navigationIcon = Icons.AutoMirrored.Filled.ArrowBack,
        onNavigationClick = { navController.popBackStack() }
    ) { paddingValues ->
        Column(
            modifier = modifier
                .fillMaxSize()
                .padding(paddingValues)
                .padding(horizontal = 16.dp)
                .verticalScroll(rememberScrollState()),
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            Spacer(modifier = Modifier.height(8.dp))
            
            // Title
            CalendarTextField(
                value = title,
                onValueChange = { 
                    title = it
                    titleError = if (it.isBlank()) "Title is required" else null
                },
                label = "Event Title",
                isError = titleError != null,
                errorMessage = titleError,
                placeholder = "Enter event title"
            )
            
            // Description
            CalendarTextField(
                value = description,
                onValueChange = { description = it },
                label = "Description",
                placeholder = "Add description (optional)",
                maxLines = 3
            )
            
            // Location
            CalendarTextField(
                value = location,
                onValueChange = { location = it },
                label = "Location",
                placeholder = "Add location (optional)"
            )
            
            // Date Selection
            DatePickerField(
                selectedDate = startDate,
                onDateSelected = { startDate = it },
                label = "Date"
            )
            
            // All Day Toggle
            SwitchField(
                checked = isAllDay,
                onCheckedChange = { isAllDay = it },
                label = "All Day Event",
                description = "Event lasts the entire day"
            )
            
            // Time Selection (only if not all day)
            if (!isAllDay) {
                TimeRangeField(
                    startTime = startTime,
                    endTime = endTime,
                    onStartTimeSelected = { startTime = it },
                    onEndTimeSelected = { endTime = it },
                    modifier = Modifier.fillMaxWidth()
                )
            }
            
            // Color Selection
            ColorPickerField(
                selectedColor = selectedColor,
                onColorSelected = { selectedColor = it },
                label = "Event Color"
            )
            
            // Priority Selection
            PrioritySelector(
                selectedPriority = priority,
                onPrioritySelected = { priority = it }
            )
            
            Spacer(modifier = Modifier.height(16.dp))
            
            // Update Button
            Button(
                onClick = {
                    if (title.isBlank()) {
                        titleError = "Title is required"
                        return@Button
                    }
                    
                    // Validate all fields before updating event
                    if (timeRangeError != null) {
                        return@Button
                    }
                    
                    val startDateTime = if (isAllDay) {
                        startDate.atStartOfDay()
                    } else {
                        LocalDateTime.of(startDate, startTime)
                    }
                    
                    val endDateTime = if (isAllDay) {
                        startDate.atTime(23, 59, 59)
                    } else {
                        LocalDateTime.of(startDate, endTime)
                    }
                    
                    val updatedEvent = event.copy(
                        title = title.trim(),
                        description = description.trim().ifEmpty { null },
                        location = location.trim().ifEmpty { null },
                        startDateTime = startDateTime,
                        endDateTime = endDateTime,
                        isAllDay = isAllDay,
                        color = ColorUtils.colorToHexString(selectedColor),
                        priority = priority,
                        updatedAt = LocalDateTime.now()
                    )
                    
                    viewModel.updateEvent(updatedEvent)
                    onEventUpdated()
                },
                modifier = Modifier.fillMaxWidth(),
                enabled = title.isNotBlank() && timeRangeError == null
            ) {
                Text("Update Event")
            }
            
            Spacer(modifier = Modifier.height(16.dp))
        }
    }
}

@Composable
private fun PrioritySelector(
    selectedPriority: EventPriority,
    onPrioritySelected: (EventPriority) -> Unit,
    modifier: Modifier = Modifier
) {
    Column(modifier = modifier) {
        Text(
            text = "Priority",
            style = MaterialTheme.typography.bodyMedium,
            color = MaterialTheme.colorScheme.onSurfaceVariant,
            modifier = Modifier.padding(bottom = 8.dp)
        )
        
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            EventPriority.values().forEach { priority ->
                FilterChip(
                    onClick = { onPrioritySelected(priority) },
                    label = { Text(priority.name) },
                    selected = selectedPriority == priority,
                    modifier = Modifier.weight(1f)
                )
            }
        }
    }
}