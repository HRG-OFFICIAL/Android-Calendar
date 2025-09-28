package com.moderncalendar.feature.calendar

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.CalendarToday
import androidx.compose.material.icons.filled.Menu
import androidx.compose.material.icons.filled.Search
import androidx.compose.material.icons.filled.Settings
import androidx.compose.material.icons.filled.Visibility
import androidx.compose.material.icons.filled.VisibilityOff
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.kizitonwose.calendar.compose.HorizontalCalendar
import com.kizitonwose.calendar.compose.rememberCalendarState
import com.kizitonwose.calendar.core.CalendarDay
import com.kizitonwose.calendar.core.CalendarMonth
import com.kizitonwose.calendar.core.DayPosition
import java.time.YearMonth
import com.moderncalendar.core.common.Result
import com.moderncalendar.core.data.entity.EventEntity
import java.time.DayOfWeek
import java.time.LocalDate
import java.time.format.DateTimeFormatter
import java.time.temporal.WeekFields
import java.util.Locale

enum class CalendarViewType {
    MONTH, WEEK, DAY, YEAR, HEATMAP
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CalendarScreen(
    modifier: Modifier = Modifier,
    onNavigateToEventCreation: () -> Unit = {},
    onNavigateToEventDetails: (String) -> Unit = {},
    onNavigateToSearch: () -> Unit = {},
    onNavigateToSettings: () -> Unit = {},
    viewModel: CalendarViewModel = hiltViewModel()
) {
    val selectedDate by viewModel.selectedDate.collectAsState()
    val events by viewModel.events.collectAsState()
    var calendarViewType by remember { mutableStateOf(CalendarViewType.MONTH) }
    
    val currentMonth = remember { YearMonth.now() }
    val startMonth = remember { currentMonth }
    val endMonth = remember { currentMonth }
    val firstDayOfWeek = remember { WeekFields.of(Locale.getDefault()).firstDayOfWeek }

    val monthCalendarState = rememberCalendarState(
        startMonth = startMonth,
        endMonth = endMonth,
        firstVisibleMonth = startMonth,
        firstDayOfWeek = firstDayOfWeek,
    )

    val disabledDates = remember { mutableStateOf(emptySet<LocalDate>()) }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Modern Calendar") },
                navigationIcon = {
                    IconButton(onClick = onNavigateToSettings) {
                        Icon(Icons.Default.Menu, contentDescription = "Settings")
                    }
                },
                actions = {
                    IconButton(onClick = onNavigateToSearch) {
                        Icon(Icons.Default.Search, contentDescription = "Search")
                    }
                    IconButton(onClick = { viewModel.selectDate(LocalDate.now()) }) {
                        Icon(Icons.Default.CalendarToday, contentDescription = "Today")
                    }
                    IconButton(onClick = onNavigateToEventCreation) {
                        Icon(Icons.Default.Add, contentDescription = "Add Event")
                    }
                }
            )
        },
        content = { paddingValues ->
            Column(
                modifier = modifier
                    .fillMaxSize()
                    .padding(paddingValues)
            ) {
                // Calendar View
        when (calendarViewType) {
            CalendarViewType.MONTH -> {
                HorizontalCalendar(
                    state = monthCalendarState,
                    dayContent = { day ->
                        EnhancedDayContent(
                            day = day,
                            isSelected = selectedDate == day.date,
                                    isDisabled = day.date in disabledDates.value,
                            onClick = { 
                                        if (day.date !in disabledDates.value) {
                                    viewModel.selectDate(day.date)
                                }
                            }
                        )
                    },
                    monthHeader = { month ->
                        EnhancedMonthHeader(month = month)
                    }
                )
            }
            CalendarViewType.DAY -> {
                DayView(
                    selectedDate = selectedDate,
                    onDateChange = { viewModel.selectDate(it) },
                            disabledDates = disabledDates.value
                )
            }
            CalendarViewType.YEAR -> {
                YearCalendar(
                    selectedDate = selectedDate,
                    onDateClick = { viewModel.selectDate(it) },
                            disabledDates = disabledDates.value
                )
            }
            CalendarViewType.HEATMAP -> {
                        val heatmapData = remember(selectedDate) {
                    generateHeatmapData(selectedDate)
                }
                HeatMapCalendar(
                    data = heatmapData,
                    onDateClick = { viewModel.selectDate(it) }
                )
            }
                    else -> {
                        Text("View type not implemented")
            }
        }
        
        Spacer(modifier = Modifier.height(16.dp))
        
        // Events for selected date
                Text(
                    text = "Events for ${selectedDate.format(DateTimeFormatter.ofPattern("MMM dd, yyyy"))}",
                    style = MaterialTheme.typography.titleMedium,
                    modifier = Modifier.padding(horizontal = 16.dp)
                )
                
                when (val eventsResult = events) {
                    is Result.Loading -> {
                        Box(
                            modifier = Modifier.fillMaxSize(),
                            contentAlignment = Alignment.Center
                        ) {
                            CircularProgressIndicator()
                        }
                    }
                    is Result.Success -> {
                        if (eventsResult.data.isEmpty()) {
                            Box(
                                modifier = Modifier.fillMaxSize(),
                                contentAlignment = Alignment.Center
                            ) {
                            Text(
                                    text = "No events for this date.",
                                    style = MaterialTheme.typography.bodyLarge,
                                color = MaterialTheme.colorScheme.onSurfaceVariant
                            )
                            }
                        } else {
                            LazyColumn(
                                modifier = Modifier.fillMaxSize(),
                                contentPadding = PaddingValues(16.dp),
                                verticalArrangement = Arrangement.spacedBy(8.dp)
                            ) {
                                items(eventsResult.data) { event ->
                                    EventItem(
                                        event = event,
                                        onClick = { onNavigateToEventDetails(event.id.toString()) }
                                    )
                                }
                            }
                        }
                    }
                    is Result.Error -> {
                        Box(
                            modifier = Modifier.fillMaxSize(),
                            contentAlignment = Alignment.Center
                        ) {
                        Text(
                                text = "Error loading events: ${eventsResult.exception.localizedMessage}",
                                color = MaterialTheme.colorScheme.error,
                                style = MaterialTheme.typography.bodyLarge
                            )
                        }
                    }
                }
            }
        }
    )
}

@Composable
fun EnhancedDayContent(
    day: CalendarDay,
    isSelected: Boolean,
    isDisabled: Boolean,
    onClick: () -> Unit
) {
    val isToday = day.date == LocalDate.now()
    val isWeekend = day.date.dayOfWeek == DayOfWeek.SATURDAY || day.date.dayOfWeek == DayOfWeek.SUNDAY
    
    Box(
        modifier = Modifier
            .aspectRatio(1f)
            .padding(2.dp)
            .clip(CircleShape)
            .background(
                when {
                    isDisabled -> MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.3f)
                    isSelected -> MaterialTheme.colorScheme.primary
                    isToday -> MaterialTheme.colorScheme.primaryContainer
                    else -> Color.Transparent
                }
            )
            .clickable(enabled = !isDisabled) { onClick() },
        contentAlignment = Alignment.Center
    ) {
        Text(
            text = day.date.dayOfMonth.toString(),
            color = when {
                isDisabled -> MaterialTheme.colorScheme.onSurfaceVariant.copy(alpha = 0.3f)
                isSelected -> MaterialTheme.colorScheme.onPrimary
                isToday -> MaterialTheme.colorScheme.onPrimaryContainer
                isWeekend -> MaterialTheme.colorScheme.onSurfaceVariant
                day.position == DayPosition.MonthDate -> MaterialTheme.colorScheme.onSurface
                else -> MaterialTheme.colorScheme.onSurfaceVariant
            },
            style = MaterialTheme.typography.bodyMedium,
            fontWeight = if (isToday || isSelected) FontWeight.Bold else FontWeight.Normal
        )
    }
}

@Composable
fun DayView(
    selectedDate: LocalDate,
    onDateChange: (LocalDate) -> Unit,
    disabledDates: Set<LocalDate> = emptySet()
) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(16.dp)
    ) {
        Text(
            text = "Day View for ${selectedDate}",
            style = MaterialTheme.typography.headlineMedium,
            modifier = Modifier.padding(bottom = 16.dp)
        )

        Card(
            modifier = Modifier.fillMaxWidth(),
            elevation = CardDefaults.cardElevation(defaultElevation = 4.dp)
    ) {
        Column(
                modifier = Modifier.padding(16.dp)
        ) {
            Text(
                    text = "Events for ${selectedDate}",
                    style = MaterialTheme.typography.titleMedium,
                    fontWeight = FontWeight.Bold
                )
                Spacer(modifier = Modifier.height(8.dp))
            Text(
                    text = "No events scheduled for this day",
                    style = MaterialTheme.typography.bodyMedium,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )
            }
        }
    }
}

@Composable
fun EventItem(
    event: EventEntity,
    onClick: () -> Unit
) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 4.dp)
            .clickable { onClick() },
        colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.surfaceVariant
        )
    ) {
        Row(
            modifier = Modifier.padding(12.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Box(
                modifier = Modifier
                    .size(12.dp)
                    .clip(CircleShape)
                    .background(Color(event.color))
            )
            
            Spacer(modifier = Modifier.width(12.dp))
            
            Column(
                modifier = Modifier.weight(1f)
            ) {
                Text(
                    text = event.title,
                    style = MaterialTheme.typography.titleSmall,
                    fontWeight = FontWeight.Medium
                )
                
                if (!event.isAllDay) {
                    Text(
                        text = "${event.startDateTime.hour}:${event.startDateTime.minute.toString().padStart(2, '0')} - ${event.endDateTime.hour}:${event.endDateTime.minute.toString().padStart(2, '0')}",
                        style = MaterialTheme.typography.bodySmall,
                        color = MaterialTheme.colorScheme.onSurfaceVariant
                    )
                } else {
                    Text(
                        text = "All day",
                        style = MaterialTheme.typography.bodySmall,
                        color = MaterialTheme.colorScheme.onSurfaceVariant
                    )
                }
                
                if (!event.location.isNullOrEmpty()) {
                    Text(
                        text = event.location ?: "",
                        style = MaterialTheme.typography.bodySmall,
                        color = MaterialTheme.colorScheme.onSurfaceVariant
                    )
                }
            }
        }
    }
}

@Composable
fun EnhancedMonthHeader(month: CalendarMonth) {
    val ym = month.yearMonth
    Text(
        text = "${ym.month.name.lowercase().replaceFirstChar { it.uppercase() }} ${ym.year}",
        style = MaterialTheme.typography.headlineSmall,
        fontWeight = FontWeight.Bold,
        modifier = Modifier.padding(16.dp)
    )
}

fun generateHeatmapData(selectedDate: LocalDate): Map<LocalDate, Int> {
    val data = mutableMapOf<LocalDate, Int>()
    for (i in 0..30) {
        data[selectedDate.minusDays(i.toLong())] = (0..5).random()
    }
    return data
}