package com.moderncalendar.feature.calendar

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.moderncalendar.core.common.model.Event
import com.moderncalendar.core.ui.components.CalendarDateCell
import com.moderncalendar.core.ui.components.EventCard
import java.time.LocalDate
import java.time.format.DateTimeFormatter
import java.time.temporal.WeekFields
import java.util.*

@Composable
fun WeekView(
    selectedDate: LocalDate,
    onDateSelected: (LocalDate) -> Unit,
    eventsForWeek: List<Event> = emptyList(),
    modifier: Modifier = Modifier,
) {
    val weekFields = WeekFields.of(Locale.getDefault())
    val startOfWeek = selectedDate.with(weekFields.dayOfWeek(), 1)
    val weekDates = (0..6).map { startOfWeek.plusDays(it.toLong()) }

    // Group events by date
    val eventsByDate = eventsForWeek.groupBy { it.startDateTime.toLocalDate() }

    Column(modifier = modifier.padding(16.dp)) {
        // Week header with dates
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceEvenly,
        ) {
            weekDates.forEach { date ->
                Column(
                    horizontalAlignment = Alignment.CenterHorizontally,
                    modifier = Modifier.weight(1f),
                ) {
                    Text(
                        text = date.dayOfWeek.name.take(3),
                        style = MaterialTheme.typography.labelSmall,
                        color = MaterialTheme.colorScheme.onSurfaceVariant,
                    )

                    Spacer(modifier = Modifier.height(4.dp))

                    CalendarDateCell(
                        date = date,
                        isSelected = date == selectedDate,
                        isToday = date == LocalDate.now(),
                        isCurrentMonth = true,
                        isWeekend = date.dayOfWeek.value >= 6,
                        hasEvents = eventsByDate[date]?.isNotEmpty() == true,
                        eventCount = eventsByDate[date]?.size ?: 0,
                        onClick = onDateSelected,
                    )
                }
            }
        }

        Spacer(modifier = Modifier.height(16.dp))

        // Events for selected date in week
        Text(
            text = "Events for ${selectedDate.format(DateTimeFormatter.ofPattern("EEEE, MMM dd"))}",
            style = MaterialTheme.typography.titleMedium,
            fontWeight = FontWeight.Bold,
        )

        Spacer(modifier = Modifier.height(8.dp))

        val eventsForSelectedDate = eventsByDate[selectedDate] ?: emptyList()

        if (eventsForSelectedDate.isEmpty()) {
            Text(
                text = "No events for this date",
                style = MaterialTheme.typography.bodyMedium,
                color = MaterialTheme.colorScheme.onSurfaceVariant,
                modifier = Modifier.padding(vertical = 16.dp),
            )
        } else {
            LazyColumn(
                verticalArrangement = Arrangement.spacedBy(8.dp),
            ) {
                items(eventsForSelectedDate) { event ->
                    EventCard(
                        title = event.title,
                        time =
                            if (event.isAllDay) {
                                "All day"
                            } else {
                                "${event.startDateTime.format(
                                    DateTimeFormatter.ofPattern("HH:mm"),
                                )} - ${event.endDateTime.format(DateTimeFormatter.ofPattern("HH:mm"))}"
                            },
                        description = event.description,
                        color = com.moderncalendar.core.ui.utils.ColorUtils.parseColorSafely(event.color),
                        isCompleted = event.isCompleted,
                    )
                }
            }
        }
    }
}
