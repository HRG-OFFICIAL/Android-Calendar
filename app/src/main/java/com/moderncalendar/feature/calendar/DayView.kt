package com.moderncalendar.feature.calendar

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.KeyboardArrowLeft
import androidx.compose.material.icons.filled.KeyboardArrowRight
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.moderncalendar.core.common.model.Event
import com.moderncalendar.core.ui.components.EventCard
import java.time.LocalDate
import java.time.LocalTime
import java.time.format.DateTimeFormatter

@Composable
fun DayView(
    selectedDate: LocalDate,
    onDateSelected: (LocalDate) -> Unit,
    eventsForDay: List<Event> = emptyList(),
    modifier: Modifier = Modifier
) {
    Column(modifier = modifier.padding(16.dp)) {
        // Day navigation header
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            IconButton(onClick = { onDateSelected(selectedDate.minusDays(1)) }) {
                Text("<")
            }
            
            Text(
                text = selectedDate.format(DateTimeFormatter.ofPattern("EEE, MMM dd")),
                style = MaterialTheme.typography.headlineSmall,
                fontWeight = FontWeight.Bold
            )
            
            IconButton(onClick = { onDateSelected(selectedDate.plusDays(1)) }) {
                Text(">")
            }
        }
        
        Spacer(modifier = Modifier.height(16.dp))
        
        if (eventsForDay.isEmpty()) {
            Box(
                modifier = Modifier.fillMaxSize(),
                contentAlignment = Alignment.Center
            ) {
                Column(
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    Text(
                        text = "No events scheduled",
                        style = MaterialTheme.typography.titleMedium,
                        color = MaterialTheme.colorScheme.onSurfaceVariant
                    )
                    Text(
                        text = "Tap + to add an event",
                        style = MaterialTheme.typography.bodyMedium,
                        color = MaterialTheme.colorScheme.onSurfaceVariant
                    )
                }
            }
        } else {
            // Time-based schedule view
            LazyColumn(
                verticalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                // Group events by time and show in chronological order
                val sortedEvents = eventsForDay.sortedBy { it.startDateTime }
                
                items(sortedEvents) { event ->
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        verticalAlignment = Alignment.Top
                    ) {
                        // Time column
                        Column(
                            modifier = Modifier.width(80.dp),
                            horizontalAlignment = Alignment.End
                        ) {
                            if (event.isAllDay) {
                                Text(
                                    text = "All day",
                                    style = MaterialTheme.typography.labelSmall,
                                    color = MaterialTheme.colorScheme.onSurfaceVariant
                                )
                            } else {
                                Text(
                                    text = event.startDateTime.format(DateTimeFormatter.ofPattern("HH:mm")),
                                    style = MaterialTheme.typography.labelMedium,
                                    fontWeight = FontWeight.Bold
                                )
                                if (event.startDateTime.toLocalTime() != event.endDateTime.toLocalTime()) {
                                    Text(
                                        text = event.endDateTime.format(DateTimeFormatter.ofPattern("HH:mm")),
                                        style = MaterialTheme.typography.labelSmall,
                                        color = MaterialTheme.colorScheme.onSurfaceVariant
                                    )
                                }
                            }
                        }
                        
                        Spacer(modifier = Modifier.width(16.dp))
                        
                        // Event card
                        EventCard(
                            title = event.title,
                            time = if (event.isAllDay) "All day" else 
                                "${event.startDateTime.format(DateTimeFormatter.ofPattern("HH:mm"))} - ${event.endDateTime.format(DateTimeFormatter.ofPattern("HH:mm"))}",
                            description = event.description,
                            color = com.moderncalendar.core.ui.utils.ColorUtils.parseColorSafely(event.color),
                            isCompleted = event.isCompleted,
                            modifier = Modifier.weight(1f)
                        )
                    }
                }
            }
        }
    }
}