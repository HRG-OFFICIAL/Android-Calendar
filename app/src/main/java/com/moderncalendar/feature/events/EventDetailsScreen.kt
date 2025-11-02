package com.moderncalendar.feature.events

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import androidx.navigation.compose.rememberNavController
import com.moderncalendar.core.common.Result
import com.moderncalendar.core.common.model.Event
import com.moderncalendar.core.ui.components.CalendarScaffold
import com.moderncalendar.core.ui.components.LoadingIndicator
import com.moderncalendar.core.ui.components.ErrorMessage
import java.time.format.DateTimeFormatter

@Composable
fun EventDetailsScreen(
    eventId: String,
    modifier: Modifier = Modifier,
    onBackClick: () -> Unit = {},
    onEditClick: (String) -> Unit = {},
    onDeleteClick: () -> Unit = {},
    viewModel: EventViewModel = hiltViewModel(),
    navController: NavController = rememberNavController()
) {
    val selectedEvent by viewModel.selectedEvent.collectAsState()
    val isLoading by viewModel.isLoading.collectAsState()
    
    LaunchedEffect(eventId) {
        viewModel.getEventById(eventId)
    }
    
    CalendarScaffold(
        navController = navController,
        title = "Event Details",
        navigationIcon = Icons.AutoMirrored.Filled.ArrowBack,
        onNavigationClick = onBackClick,
        topBarActions = {
            selectedEvent?.let { event ->
                IconButton(onClick = { onEditClick(event.id) }) {
                    Icon(Icons.Default.Edit, contentDescription = "Edit")
                }
                IconButton(onClick = { 
                    viewModel.deleteEvent(event.id)
                    onDeleteClick()
                }) {
                    Icon(Icons.Default.Delete, contentDescription = "Delete")
                }
            }
        }
    ) { paddingValues ->
        when {
            isLoading -> {
                LoadingIndicator()
            }
            selectedEvent == null -> {
                ErrorMessage(
                    message = "Event not found",
                    onRetry = { viewModel.getEventById(eventId) }
                )
            }
            else -> {
                EventDetailsContent(
                    event = selectedEvent!!,
                    modifier = Modifier.padding(paddingValues)
                )
            }
        }
    }
}

@Composable
private fun EventDetailsContent(
    event: Event,
    modifier: Modifier = Modifier
) {
    val dateFormatter = DateTimeFormatter.ofPattern("EEEE, MMMM dd, yyyy")
    val timeFormatter = DateTimeFormatter.ofPattern("HH:mm")
    
    Column(
        modifier = modifier
            .fillMaxSize()
            .verticalScroll(rememberScrollState())
            .padding(16.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        // Event Header with colored background
        Card(
            modifier = Modifier.fillMaxWidth(),
            colors = CardDefaults.cardColors(
                containerColor = com.moderncalendar.core.ui.utils.ColorUtils.parseColorSafely(event.color)
            )
        ) {
            Column(
                modifier = Modifier.padding(16.dp)
            ) {
                // Title without color dot
                Text(
                    text = event.title,
                    style = MaterialTheme.typography.headlineMedium,
                    fontWeight = FontWeight.Bold,
                    color = com.moderncalendar.core.ui.utils.ColorUtils.getContrastingTextColor(
                        com.moderncalendar.core.ui.utils.ColorUtils.parseColorSafely(event.color)
                    )
                )
                
                Spacer(modifier = Modifier.height(8.dp))
                
                Text(
                    text = event.startDateTime.format(dateFormatter),
                    style = MaterialTheme.typography.titleMedium,
                    color = com.moderncalendar.core.ui.utils.ColorUtils.getContrastingTextColor(
                        com.moderncalendar.core.ui.utils.ColorUtils.parseColorSafely(event.color)
                    )
                )
                
                if (!event.isAllDay) {
                    Text(
                        text = "${event.startDateTime.format(timeFormatter)} - ${event.endDateTime.format(timeFormatter)}",
                        style = MaterialTheme.typography.bodyLarge,
                        color = com.moderncalendar.core.ui.utils.ColorUtils.getContrastingTextColor(
                            com.moderncalendar.core.ui.utils.ColorUtils.parseColorSafely(event.color)
                        )
                    )
                } else {
                    Text(
                        text = "All day",
                        style = MaterialTheme.typography.bodyLarge,
                        color = com.moderncalendar.core.ui.utils.ColorUtils.getContrastingTextColor(
                            com.moderncalendar.core.ui.utils.ColorUtils.parseColorSafely(event.color)
                        )
                    )
                }
            }
        }
        
        // Priority Badge
        Card(
            modifier = Modifier.fillMaxWidth()
        ) {
            Row(
                modifier = Modifier.padding(16.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = "Priority:",
                    style = MaterialTheme.typography.titleMedium,
                    fontWeight = FontWeight.Medium
                )
                Spacer(modifier = Modifier.width(8.dp))
                AssistChip(
                    onClick = { },
                    label = { Text(event.priority.name) }
                )
            }
        }
        
        // Event Description
        if (!event.description.isNullOrEmpty()) {
            Card(
                modifier = Modifier.fillMaxWidth()
            ) {
                Column(
                    modifier = Modifier.padding(16.dp)
                ) {
                    Text(
                        text = "Description",
                        style = MaterialTheme.typography.titleMedium,
                        fontWeight = FontWeight.Medium
                    )
                    Spacer(modifier = Modifier.height(8.dp))
                    Text(
                        text = event.description!!,
                        style = MaterialTheme.typography.bodyLarge
                    )
                }
            }
        }
        
        // Event Location
        if (!event.location.isNullOrEmpty()) {
            Card(
                modifier = Modifier.fillMaxWidth()
            ) {
                Column(
                    modifier = Modifier.padding(16.dp)
                ) {
                    Text(
                        text = "Location",
                        style = MaterialTheme.typography.titleMedium,
                        fontWeight = FontWeight.Medium
                    )
                    Spacer(modifier = Modifier.height(8.dp))
                    Text(
                        text = event.location!!,
                        style = MaterialTheme.typography.bodyLarge
                    )
                }
            }
        }
        
        // Recurrence Information
        event.recurrenceRule?.let { recurrenceRule ->
            Card(
                modifier = Modifier.fillMaxWidth()
            ) {
                Column(
                    modifier = Modifier.padding(16.dp)
                ) {
                    Text(
                        text = "Recurrence",
                        style = MaterialTheme.typography.titleMedium,
                        fontWeight = FontWeight.Medium
                    )
                    Spacer(modifier = Modifier.height(8.dp))
                    Text(
                        text = "Repeats ${recurrenceRule.frequency.name.lowercase()}",
                        style = MaterialTheme.typography.bodyLarge
                    )
                }
            }
        }
        
        // Event Status
        if (event.isCompleted) {
            Card(
                modifier = Modifier.fillMaxWidth(),
                colors = CardDefaults.cardColors(
                    containerColor = MaterialTheme.colorScheme.secondaryContainer
                )
            ) {
                Row(
                    modifier = Modifier.padding(16.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(
                        text = "✓ Completed",
                        style = MaterialTheme.typography.titleMedium,
                        fontWeight = FontWeight.Medium,
                        color = MaterialTheme.colorScheme.onSecondaryContainer
                    )
                }
            }
        }
    }
}
