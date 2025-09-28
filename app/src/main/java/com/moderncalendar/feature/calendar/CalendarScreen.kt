package com.moderncalendar.feature.calendar

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.DateRange
import androidx.compose.material.icons.filled.Today
import androidx.compose.material.icons.filled.ViewWeek
import androidx.compose.material.icons.filled.Search
import androidx.compose.material.icons.filled.Settings
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
import com.kizitonwose.calendar.compose.VerticalCalendar
import com.kizitonwose.calendar.compose.rememberCalendarState
import com.kizitonwose.calendar.core.CalendarDay
import com.kizitonwose.calendar.core.DayPosition
import com.kizitonwose.calendar.core.Week
import com.moderncalendar.core.common.Result
import com.moderncalendar.core.data.entity.EventEntity
import java.time.DayOfWeek
import java.time.LocalDate
import java.time.YearMonth

enum class CalendarViewType {
    MONTH, WEEK, DAY, YEAR, HEATMAP
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CalendarScreen(
    modifier: Modifier = Modifier,
    onEventClick: (String) -> Unit = {},
    onCreateEventClick: () -> Unit = {},
    onSearchClick: () -> Unit = {},
    onSettingsClick: () -> Unit = {},
    viewModel: CalendarViewModel = hiltViewModel()
) {
    val currentDate = remember { LocalDate.now() }
    val startDate = remember { currentDate.minusYears(1) }
    val endDate = remember { currentDate.plusYears(1) }
    
    // Calendar view type state
    var calendarViewType by remember { mutableStateOf(CalendarViewType.MONTH) }
    
    // Calendar states for different view types
    val monthCalendarState = rememberCalendarState(
        startMonth = YearMonth.from(startDate),
        endMonth = YearMonth.from(endDate),
        firstVisibleMonth = YearMonth.from(currentDate),
        firstDayOfWeek = DayOfWeek.SUNDAY
    )
    
    val weekCalendarState = rememberCalendarState(
        startMonth = YearMonth.from(startDate),
        endMonth = YearMonth.from(endDate),
        firstVisibleMonth = YearMonth.from(currentDate),
        firstDayOfWeek = DayOfWeek.SUNDAY
    )
    
    val selectedDate by viewModel.selectedDate.collectAsState()
    val events by viewModel.events.collectAsState()
    val isLoading by viewModel.isLoading.collectAsState()
    
    // Disabled dates (example: weekends for certain operations)
    val disabledDates = remember { 
        setOf<LocalDate>() // Add specific dates to disable
    }
    
    Box(
        modifier = modifier.fillMaxSize()
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(16.dp)
        ) {
        // Top App Bar
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(bottom = 16.dp),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(
                text = "Modern Calendar",
                style = MaterialTheme.typography.headlineLarge,
                fontWeight = FontWeight.Bold
            )
            
            Row(
                horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                IconButton(onClick = onSearchClick) {
                    Icon(
                        imageVector = Icons.Default.Search,
                        contentDescription = "Search"
                    )
                }
                IconButton(onClick = onSettingsClick) {
                    Icon(
                        imageVector = Icons.Default.Settings,
                        contentDescription = "Settings"
                    )
                }
            }
        }
        
        // Calendar View Type Selector
        CalendarViewSelector(
            selectedViewType = calendarViewType,
            onViewTypeChange = { calendarViewType = it },
            modifier = Modifier.padding(bottom = 16.dp)
        )
        
        // Selected Date Display
        Card(
            modifier = Modifier
                .fillMaxWidth()
                .padding(bottom = 16.dp),
            colors = CardDefaults.cardColors(
                containerColor = MaterialTheme.colorScheme.primaryContainer
            )
        ) {
            Column(
                modifier = Modifier.padding(16.dp),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Text(
                    text = selectedDate.dayOfWeek.name.lowercase().replaceFirstChar { it.uppercase() },
                    style = MaterialTheme.typography.titleMedium,
                    color = MaterialTheme.colorScheme.onPrimaryContainer
                )
                Text(
                    text = "${selectedDate.dayOfMonth} ${selectedDate.month.name.lowercase().replaceFirstChar { it.uppercase() }} ${selectedDate.year}",
                    style = MaterialTheme.typography.headlineMedium,
                    fontWeight = FontWeight.Bold,
                    color = MaterialTheme.colorScheme.onPrimaryContainer
                )
            }
        }
        
        // Calendar - Dynamic view based on selected type
        when (calendarViewType) {
            CalendarViewType.MONTH -> {
                HorizontalCalendar(
                    state = monthCalendarState,
                    dayContent = { day ->
                        EnhancedDayContent(
                            day = day,
                            isSelected = selectedDate == day.date,
                            isDisabled = day.date in disabledDates,
                            onClick = { 
                                if (day.date !in disabledDates) {
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
            CalendarViewType.WEEK -> {
                VerticalCalendar(
                    state = weekCalendarState,
                    dayContent = { day ->
                        EnhancedDayContent(
                            day = day,
                            isSelected = selectedDate == day.date,
                            isDisabled = day.date in disabledDates,
                            onClick = { 
                                if (day.date !in disabledDates) {
                                    viewModel.selectDate(day.date)
                                }
                            }
                        )
                    },
                    weekHeader = { week ->
                        WeekHeader(week = week)
                    }
                )
            }
            CalendarViewType.DAY -> {
                DayView(
                    selectedDate = selectedDate,
                    onDateChange = { viewModel.selectDate(it) },
                    disabledDates = disabledDates
                )
            }
            CalendarViewType.YEAR -> {
                YearCalendar(
                    selectedDate = selectedDate,
                    onDateClick = { viewModel.selectDate(it) },
                    disabledDates = disabledDates
                )
            }
            CalendarViewType.HEATMAP -> {
                // Sample data for heatmap (replace with real data)
                val heatmapData = remember {
                    generateHeatmapData(selectedDate)
                }
                HeatMapCalendar(
                    data = heatmapData,
                    onDateClick = { viewModel.selectDate(it) }
                )
            }
        }
        
        Spacer(modifier = Modifier.height(16.dp))
        
        // Events for selected date
        Card(
            modifier = Modifier.fillMaxWidth()
        ) {
            Column(
                modifier = Modifier.padding(16.dp)
            ) {
                Text(
                    text = "Events for ${selectedDate.dayOfMonth}/${selectedDate.monthValue}/${selectedDate.year}",
                    style = MaterialTheme.typography.titleMedium,
                    fontWeight = FontWeight.Bold
                )
                Spacer(modifier = Modifier.height(8.dp))
                
                when (events) {
                    is Result.Loading -> {
                        CircularProgressIndicator(
                            modifier = Modifier.align(Alignment.CenterHorizontally)
                        )
                    }
                    is Result.Success -> {
                        if (events.data.isEmpty()) {
                            Text(
                                text = "No events scheduled",
                                style = MaterialTheme.typography.bodyMedium,
                                color = MaterialTheme.colorScheme.onSurfaceVariant
                            )
                        } else {
                            LazyColumn {
                                items(events.data) { event ->
                                    EventItem(
                                        event = event,
                                        onClick = { onEventClick(event.id) }
                                    )
                                }
                            }
                        }
                    }
                    is Result.Error -> {
                        Text(
                            text = "Error loading events: ${events.exception.message}",
                            style = MaterialTheme.typography.bodyMedium,
                            color = MaterialTheme.colorScheme.error
                        )
                    }
                }
            }
        }
        
        // Floating Action Button
        FloatingActionButton(
            onClick = onCreateEventClick,
            modifier = Modifier
                .align(Alignment.BottomEnd)
                .padding(16.dp)
        ) {
            Icon(
                imageVector = Icons.Default.Add,
                contentDescription = "Create Event"
            )
        }
    }
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
fun DayContent(
    day: CalendarDay,
    isSelected: Boolean,
    onClick: () -> Unit
) {
    EnhancedDayContent(
        day = day,
        isSelected = isSelected,
        isDisabled = false,
        onClick = onClick
    )
}

@Composable
fun EnhancedMonthHeader(month: YearMonth) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 4.dp),
        colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.primaryContainer.copy(alpha = 0.1f)
        )
    ) {
        Text(
            text = "${month.month.name.lowercase().replaceFirstChar { it.uppercase() }} ${month.year}",
            style = MaterialTheme.typography.titleLarge,
            fontWeight = FontWeight.Bold,
            color = MaterialTheme.colorScheme.primary,
            modifier = Modifier.padding(vertical = 12.dp, horizontal = 16.dp)
        )
    }
}

@Composable
fun MonthHeader(month: YearMonth) {
    Text(
        text = "${month.month.name.lowercase().replaceFirstChar { it.uppercase() }} ${month.year}",
        style = MaterialTheme.typography.titleLarge,
        fontWeight = FontWeight.Bold,
        modifier = Modifier.padding(vertical = 8.dp)
    )
}

@Composable
fun WeekHeader(week: Week) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 4.dp),
        colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.secondaryContainer.copy(alpha = 0.1f)
        )
    ) {
        Text(
            text = "Week ${week.weekOfYear}",
            style = MaterialTheme.typography.titleMedium,
            fontWeight = FontWeight.Medium,
            color = MaterialTheme.colorScheme.secondary,
            modifier = Modifier.padding(vertical = 8.dp, horizontal = 16.dp)
        )
    }
}

@Composable
fun DayView(
    selectedDate: LocalDate,
    onDateChange: (LocalDate) -> Unit,
    disabledDates: Set<LocalDate>
) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 8.dp),
        colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.tertiaryContainer.copy(alpha = 0.1f)
        )
    ) {
        Column(
            modifier = Modifier.padding(16.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text(
                text = selectedDate.dayOfWeek.name.lowercase().replaceFirstChar { it.uppercase() },
                style = MaterialTheme.typography.titleLarge,
                fontWeight = FontWeight.Bold,
                color = MaterialTheme.colorScheme.tertiary
            )
            Text(
                text = "${selectedDate.dayOfMonth} ${selectedDate.month.name.lowercase().replaceFirstChar { it.uppercase() }} ${selectedDate.year}",
                style = MaterialTheme.typography.headlineMedium,
                fontWeight = FontWeight.Bold,
                color = MaterialTheme.colorScheme.tertiary
            )
            
            // Navigation buttons for day view
            Row(
                modifier = Modifier.padding(top = 8.dp),
                horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                Button(
                    onClick = { onDateChange(selectedDate.minusDays(1)) },
                    enabled = selectedDate.minusDays(1) !in disabledDates
                ) {
                    Text("Previous")
                }
                Button(
                    onClick = { onDateChange(selectedDate.plusDays(1)) },
                    enabled = selectedDate.plusDays(1) !in disabledDates
                ) {
                    Text("Next")
                }
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
            // Event color indicator
            Box(
                modifier = Modifier
                    .size(12.dp)
                    .clip(CircleShape)
                    .background(Color(android.graphics.Color.parseColor(event.color)))
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
                
                event.location?.let { location ->
                    Text(
                        text = location,
                        style = MaterialTheme.typography.bodySmall,
                        color = MaterialTheme.colorScheme.onSurfaceVariant
                    )
                }
            }
        }
    }
}

// Helper function to generate sample heatmap data
fun generateHeatmapData(selectedDate: LocalDate): Map<LocalDate, Int> {
    val data = mutableMapOf<LocalDate, Int>()
    val startDate = selectedDate.minusYears(1)
    val endDate = selectedDate.plusYears(1)
    
    var currentDate = startDate
    while (currentDate.isBefore(endDate) || currentDate.isEqual(endDate)) {
        // Generate random activity data (replace with real data)
        val activity = (0..10).random()
        if (activity > 0) {
            data[currentDate] = activity
        }
        currentDate = currentDate.plusDays(1)
    }
    
    return data
}
