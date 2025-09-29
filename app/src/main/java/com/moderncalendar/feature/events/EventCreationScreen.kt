package com.moderncalendar.feature.events

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.moderncalendar.core.data.entity.EventEntity
import com.moderncalendar.core.data.entity.RecurrenceFrequency
import java.time.LocalDate
import java.time.LocalDateTime
import java.time.LocalTime

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun EventCreationScreen(
    modifier: Modifier = Modifier,
    onEventCreated: () -> Unit = {},
    viewModel: EventViewModel = hiltViewModel()
) {
    var title by remember { mutableStateOf("") }
    var description by remember { mutableStateOf("") }
    var location by remember { mutableStateOf("") }
    var isAllDay by remember { mutableStateOf(false) }
    var startDate by remember { mutableStateOf(LocalDate.now()) }
    var startTime by remember { mutableStateOf(LocalTime.of(9, 0)) }
    var endTime by remember { mutableStateOf(LocalTime.of(10, 0)) }
    var selectedColor by remember { mutableStateOf("#009688") }
    var isRecurring by remember { mutableStateOf(false) }
    var recurrenceFrequency by remember { mutableStateOf(RecurrenceFrequency.WEEKLY) }
    var reminderMinutes by remember { mutableStateOf(listOf<Int>()) }
    
    val eventColors = listOf(
        "#009688", "#2196F3", "#4CAF50", "#FF9800", "#E91E63",
        "#9C27B0", "#00BCD4", "#FF5722", "#795548", "#607D8B"
    )
    
    Column(
        modifier = modifier
            .fillMaxSize()
            .padding(horizontal = 16.dp)
            .padding(top = 24.dp)
            .verticalScroll(rememberScrollState())
    ) {
        Text(
            text = "Create Event",
            style = MaterialTheme.typography.headlineMedium,
            fontWeight = FontWeight.Bold,
            modifier = Modifier.padding(bottom = 24.dp)
        )
        
        // Title
        OutlinedTextField(
            value = title,
            onValueChange = { title = it },
            label = { Text("Event Title") },
            modifier = Modifier.fillMaxWidth(),
            singleLine = true
        )
        
        Spacer(modifier = Modifier.height(16.dp))
        
        // Description
        OutlinedTextField(
            value = description,
            onValueChange = { description = it },
            label = { Text("Description") },
            modifier = Modifier.fillMaxWidth(),
            minLines = 3
        )
        
        Spacer(modifier = Modifier.height(16.dp))
        
        // Location
        OutlinedTextField(
            value = location,
            onValueChange = { location = it },
            label = { Text("Location") },
            modifier = Modifier.fillMaxWidth(),
            singleLine = true
        )
        
        Spacer(modifier = Modifier.height(16.dp))
        
        // All Day Toggle
        Row(
            modifier = Modifier.fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Checkbox(
                checked = isAllDay,
                onCheckedChange = { isAllDay = it }
            )
            Spacer(modifier = Modifier.width(8.dp))
            Text("All Day Event")
        }
        
        Spacer(modifier = Modifier.height(16.dp))
        
        // Date and Time Selection
        if (!isAllDay) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                OutlinedTextField(
                    value = startTime.toString(),
                    onValueChange = { },
                    label = { Text("Start Time") },
                    modifier = Modifier.weight(1f),
                    readOnly = true
                )
                
                OutlinedTextField(
                    value = endTime.toString(),
                    onValueChange = { },
                    label = { Text("End Time") },
                    modifier = Modifier.weight(1f),
                    readOnly = true
                )
            }
        }
        
        Spacer(modifier = Modifier.height(16.dp))
        
        // Color Selection
        Text(
            text = "Event Color",
            style = MaterialTheme.typography.titleMedium,
            fontWeight = FontWeight.Medium
        )
        
        Spacer(modifier = Modifier.height(8.dp))
        
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            eventColors.forEach { color ->
                Box(
                    modifier = Modifier
                        .size(40.dp)
                        .background(
                            color = androidx.compose.ui.graphics.Color(android.graphics.Color.parseColor(color)),
                            shape = androidx.compose.foundation.shape.CircleShape
                        )
                        .clickable { selectedColor = color }
                ) {
                    if (selectedColor == color) {
                        Box(
                            modifier = Modifier
                                .fillMaxSize()
                                .background(
                                    color = androidx.compose.ui.graphics.Color.White.copy(alpha = 0.3f),
                                    shape = androidx.compose.foundation.shape.CircleShape
                                )
                        )
                    }
                }
            }
        }
        
        Spacer(modifier = Modifier.height(16.dp))
        
        // Recurring Event
        Row(
            modifier = Modifier.fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Checkbox(
                checked = isRecurring,
                onCheckedChange = { isRecurring = it }
            )
            Spacer(modifier = Modifier.width(8.dp))
            Text("Recurring Event")
        }
        
        if (isRecurring) {
            Spacer(modifier = Modifier.height(8.dp))
            
            var expanded by remember { mutableStateOf(false) }
            
            ExposedDropdownMenuBox(
                expanded = expanded,
                onExpandedChange = { expanded = !expanded }
            ) {
                OutlinedTextField(
                    value = recurrenceFrequency.name.lowercase().replaceFirstChar { it.uppercase() },
                    onValueChange = { },
                    readOnly = true,
                    label = { Text("Repeat") },
                    trailingIcon = { ExposedDropdownMenuDefaults.TrailingIcon(expanded = expanded) },
                    modifier = Modifier
                        .fillMaxWidth()
                        .menuAnchor()
                )
                
                ExposedDropdownMenu(
                    expanded = expanded,
                    onDismissRequest = { expanded = false }
                ) {
                    RecurrenceFrequency.values().forEach { frequency ->
                        DropdownMenuItem(
                            text = { Text(frequency.name.lowercase().replaceFirstChar { it.uppercase() }) },
                            onClick = {
                                recurrenceFrequency = frequency
                                expanded = false
                            }
                        )
                    }
                }
            }
        }
        
        Spacer(modifier = Modifier.height(32.dp))
        
        // Create Button
        Button(
            onClick = {
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
                
                val event = EventEntity(
                    id = java.util.UUID.randomUUID().toString(),
                    title = title,
                    description = description.ifEmpty { null },
                    startDateTime = startDateTime,
                    endDateTime = endDateTime,
                    isAllDay = isAllDay,
                    location = location.ifEmpty { null },
                    color = selectedColor.toIntOrNull() ?: 0xFF6750A4.toInt(),
                    calendarId = "default", // TODO: Get from user selection
                    recurrenceRule = if (isRecurring) {
                        recurrenceFrequency.name
                    } else null,
                    reminderMinutes = reminderMinutes.firstOrNull()
                )
                
                viewModel.createEvent(event)
                onEventCreated()
            },
            modifier = Modifier.fillMaxWidth(),
            enabled = title.isNotEmpty()
        ) {
            Text("Create Event")
        }
    }
}

