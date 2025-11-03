package com.moderncalendar.feature.calendar

import android.util.Log
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Search
import androidx.compose.material.icons.filled.Settings
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import androidx.navigation.compose.rememberNavController
import com.moderncalendar.core.common.Result
import com.moderncalendar.core.common.model.Event
import com.moderncalendar.core.ui.components.*
import com.moderncalendar.core.ui.utils.ColorUtils
import java.time.LocalDate
import java.time.YearMonth
import java.time.format.DateTimeFormatter

enum class CalendarViewMode {
    MONTH,
    WEEK,
    DAY,
}

@Composable
fun CalendarScreen(
    modifier: Modifier = Modifier,
    onNavigateToEventCreation: (LocalDate) -> Unit = {},
    onNavigateToEventDetails: (String) -> Unit = {},
    onNavigateToSearch: () -> Unit = {},
    onNavigateToSettings: () -> Unit = {},
    viewModel: CalendarViewModel = hiltViewModel(),
    navController: NavController = rememberNavController(),
) {
    val selectedDate by viewModel.selectedDate.collectAsState()
    val events by viewModel.events.collectAsState()
    val monthEvents by viewModel.monthEvents.collectAsState()
    var currentMonth by remember { mutableStateOf(java.time.YearMonth.now()) }
    var viewMode by remember { mutableStateOf(CalendarViewMode.MONTH) }

    // Load events for the current month when it changes
    LaunchedEffect(currentMonth) {
        viewModel.loadEventsForMonth(currentMonth)
    }

    CalendarScaffold(
        navController = navController,
        title = "Calendar",
        showFab = true,
        onFabClick = { onNavigateToEventCreation(selectedDate) },
        topBarActions = {
            // View mode toggle
            TextButton(onClick = {
                viewMode =
                    when (viewMode) {
                        CalendarViewMode.MONTH -> CalendarViewMode.WEEK
                        CalendarViewMode.WEEK -> CalendarViewMode.DAY
                        CalendarViewMode.DAY -> CalendarViewMode.MONTH
                    }
            }) {
                Text(
                    text = viewMode.name,
                    color = MaterialTheme.colorScheme.onSurface,
                )
            }

            IconButton(onClick = onNavigateToSearch) {
                Icon(
                    imageVector = Icons.Default.Search,
                    contentDescription = "Search",
                    tint = MaterialTheme.colorScheme.onSurface,
                )
            }

            IconButton(onClick = onNavigateToSettings) {
                Icon(
                    imageVector = Icons.Default.Settings,
                    contentDescription = "Settings",
                    tint = MaterialTheme.colorScheme.onSurface,
                )
            }
        },
    ) { paddingValues ->
        Column(
            modifier =
                modifier
                    .fillMaxSize()
                    .padding(paddingValues),
        ) {
            when (viewMode) {
                CalendarViewMode.MONTH -> {
                    // Calendar Header
                    CalendarHeader(
                        currentMonth = currentMonth.format(DateTimeFormatter.ofPattern("MMMM yyyy")),
                        onPreviousMonth = {
                            currentMonth = currentMonth.minusMonths(1)
                        },
                        onNextMonth = {
                            currentMonth = currentMonth.plusMonths(1)
                        },
                    )

                    // Simple Month Grid
                    SimpleMonthView(
                        yearMonth = currentMonth,
                        selectedDate = selectedDate,
                        eventsForMonth =
                            when (val monthEventsResult = monthEvents) {
                                is Result.Success -> monthEventsResult.data
                                else -> emptyList()
                            },
                        onDateSelected = { date ->
                            viewModel.selectDate(date)
                        },
                    )
                }

                CalendarViewMode.WEEK -> {
                    WeekView(
                        selectedDate = selectedDate,
                        onDateSelected = { date ->
                            viewModel.selectDate(date)
                        },
                        eventsForWeek =
                            when (val monthEventsResult = monthEvents) {
                                is Result.Success -> monthEventsResult.data
                                else -> emptyList()
                            },
                    )
                }

                CalendarViewMode.DAY -> {
                    DayView(
                        selectedDate = selectedDate,
                        onDateSelected = { date ->
                            viewModel.selectDate(date)
                        },
                        eventsForDay =
                            when (val eventsResult = events) {
                                is Result.Success -> eventsResult.data
                                else -> emptyList()
                            },
                    )
                }
            }

            // Only show events list for month view (week and day views have their own event display)
            if (viewMode == CalendarViewMode.MONTH) {
                Spacer(modifier = Modifier.height(16.dp))

                // Events for selected date
                Text(
                    text = "Events for ${selectedDate.format(DateTimeFormatter.ofPattern("MMM dd, yyyy"))}",
                    style = MaterialTheme.typography.titleMedium,
                    modifier = Modifier.padding(horizontal = 16.dp),
                )

                Spacer(modifier = Modifier.height(8.dp))

                when (val eventsResult = events) {
                    is Result.Loading -> {
                        LoadingIndicator()
                    }
                    is Result.Success -> {
                        if (eventsResult.data.isEmpty()) {
                            EmptyState(
                                title = "No Events",
                                description = "No events scheduled for today.",
                                actionText = "Add Event",
                                onAction = { onNavigateToEventCreation(selectedDate) },
                            )
                        } else {
                            LazyColumn(
                                modifier = Modifier.fillMaxSize(),
                                contentPadding = PaddingValues(12.dp),
                                verticalArrangement = Arrangement.spacedBy(6.dp),
                            ) {
                                items(eventsResult.data) { event ->
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
                                        color =
                                            try {
                                                ColorUtils.parseColorSafely(event.color)
                                            } catch (e: Exception) {
                                                android.util.Log.e(
                                                    "CalendarScreen",
                                                    "Failed to parse color for event ${event.id}: '${event.color}'",
                                                    e,
                                                )
                                                ColorUtils.getDefaultEventColor()
                                            },
                                        isCompleted = event.isCompleted,
                                        onClick = {
                                            val encodedId =
                                                try {
                                                    java.net.URLEncoder.encode(event.id, "UTF-8")
                                                } catch (e: Exception) {
                                                    event.id
                                                }
                                            onNavigateToEventDetails(encodedId)
                                        },
                                    )
                                }
                            }
                        }
                    }
                    is Result.Error -> {
                        ErrorMessage(
                            message = "Error loading events: ${eventsResult.exception.localizedMessage}",
                            onRetry = { viewModel.selectDate(selectedDate) },
                        )
                    }
                }
            }
        }
    }
}

@Composable
fun SimpleMonthView(
    yearMonth: java.time.YearMonth,
    selectedDate: LocalDate,
    onDateSelected: (LocalDate) -> Unit,
    eventsForMonth: List<Event> = emptyList(),
    modifier: Modifier = Modifier,
) {
    val firstDayOfMonth = yearMonth.atDay(1)
    val firstDayOfWeek = firstDayOfMonth.dayOfWeek.value % 7 // Convert to 0-6 where Sunday = 0
    val daysInMonth = yearMonth.lengthOfMonth()

    // Group events by date
    val eventsByDate = eventsForMonth.groupBy { it.startDateTime.toLocalDate() }

    Column(modifier = modifier.padding(horizontal = 16.dp)) {
        // Week days header
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceEvenly,
        ) {
            listOf("Sun", "Mon", "Tue", "Wed", "Thu", "Fri", "Sat").forEach { day ->
                Text(
                    text = day,
                    style = MaterialTheme.typography.labelMedium,
                    color = MaterialTheme.colorScheme.onSurfaceVariant,
                    modifier = Modifier.weight(1f),
                    textAlign = androidx.compose.ui.text.style.TextAlign.Center,
                )
            }
        }

        Spacer(modifier = Modifier.height(8.dp))

        // Calendar grid
        val weeks = (daysInMonth + firstDayOfWeek + 6) / 7

        for (week in 0 until weeks) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceEvenly,
            ) {
                for (dayOfWeek in 0..6) {
                    val dayOfMonth = week * 7 + dayOfWeek - firstDayOfWeek + 1

                    if (dayOfMonth in 1..daysInMonth) {
                        val date = yearMonth.atDay(dayOfMonth)
                        val eventsForDate = eventsByDate[date] ?: emptyList()

                        CalendarDateCell(
                            date = date,
                            isSelected = date == selectedDate,
                            isToday = date == LocalDate.now(),
                            isCurrentMonth = true,
                            isWeekend = dayOfWeek == 0 || dayOfWeek == 6,
                            hasEvents = eventsForDate.isNotEmpty(),
                            eventCount = eventsForDate.size,
                            onClick = onDateSelected,
                            modifier = Modifier.weight(1f),
                        )
                    } else {
                        Spacer(modifier = Modifier.weight(1f))
                    }
                }
            }
        }
    }
}
