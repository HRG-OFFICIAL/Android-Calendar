package com.moderncalendar.feature.calendar

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.DateRange
import androidx.compose.material.icons.filled.CalendarToday
import androidx.compose.material.icons.filled.ViewWeek
import androidx.compose.material.icons.filled.Timeline
import androidx.compose.material.icons.filled.Thermostat
import androidx.compose.material.icons.filled.Today
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.kizitonwose.calendar.compose.HorizontalCalendar
import com.kizitonwose.calendar.compose.VerticalCalendar
import com.kizitonwose.calendar.compose.rememberCalendarState
import com.kizitonwose.calendar.core.CalendarDay
import com.kizitonwose.calendar.core.CalendarMonth
import com.kizitonwose.calendar.core.DayPosition
import com.kizitonwose.calendar.core.Week
import java.time.DayOfWeek
import java.time.LocalDate
import java.time.YearMonth

/**
 * HeatMap Calendar for data visualization (like GitHub contributions)
 */
@Composable
fun HeatMapCalendar(
    modifier: Modifier = Modifier,
    data: Map<LocalDate, Int> = emptyMap(),
    onDateClick: (LocalDate) -> Unit = {},
    maxValue: Int = data.values.maxOrNull() ?: 1
) {
    val currentDate = remember { LocalDate.now() }
    val startDate = remember { currentDate.minusYears(1) }
    val endDate = remember { currentDate.plusYears(1) }
    
    val calendarState = rememberCalendarState(
        startMonth = YearMonth.from(startDate),
        endMonth = YearMonth.from(endDate),
        firstVisibleMonth = YearMonth.from(currentDate),
        firstDayOfWeek = DayOfWeek.SUNDAY
    )
    
    Card(
        modifier = modifier.fillMaxWidth(),
        colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.surfaceVariant
        )
    ) {
        Column(
            modifier = Modifier.padding(16.dp)
        ) {
            Text(
                text = "Activity Heatmap",
                style = MaterialTheme.typography.titleLarge,
                fontWeight = FontWeight.Bold
            )
            
            Spacer(modifier = Modifier.height(8.dp))
            
            // Legend
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = "Less",
                    style = MaterialTheme.typography.bodySmall,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )
                
                Row(
                    horizontalArrangement = Arrangement.spacedBy(2.dp)
                ) {
                    repeat(5) { level ->
                        Box(
                            modifier = Modifier
                                .size(12.dp)
                                .clip(RoundedCornerShape(2.dp))
                                .background(
                                    when (level) {
                                        0 -> Color.Gray.copy(alpha = 0.1f)
                                        1 -> Color.Green.copy(alpha = 0.3f)
                                        2 -> Color.Green.copy(alpha = 0.5f)
                                        3 -> Color.Green.copy(alpha = 0.7f)
                                        4 -> Color.Green.copy(alpha = 0.9f)
                                        else -> Color.Green
                                    }
                                )
                        )
                    }
                }
                
                Text(
                    text = "More",
                    style = MaterialTheme.typography.bodySmall,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )
            }
            
            Spacer(modifier = Modifier.height(16.dp))
            
            HorizontalCalendar(
                state = calendarState,
                dayContent = { day ->
                    HeatMapDayContent(
                        day = day,
                        value = data[day.date] ?: 0,
                        maxValue = maxValue,
                        onClick = { onDateClick(day.date) }
                    )
                },
                monthHeader = { month ->
                    HeatMapMonthHeader(month = month)
                }
            )
        }
    }
}

@Composable
private fun HeatMapDayContent(
    day: CalendarDay,
    value: Int,
    maxValue: Int,
    onClick: () -> Unit
) {
    val intensity = if (maxValue > 0) (value.toFloat() / maxValue) else 0f
    val isToday = day.date == LocalDate.now()
    
    Box(
        modifier = Modifier
            .aspectRatio(1f)
            .padding(1.dp)
            .clip(RoundedCornerShape(2.dp))
            .background(
                when {
                    value == 0 -> Color.Gray.copy(alpha = 0.1f)
                    intensity <= 0.25f -> Color.Green.copy(alpha = 0.3f)
                    intensity <= 0.5f -> Color.Green.copy(alpha = 0.5f)
                    intensity <= 0.75f -> Color.Green.copy(alpha = 0.7f)
                    else -> Color.Green.copy(alpha = 0.9f)
                }
            )
            .clickable { onClick() },
        contentAlignment = Alignment.Center
    ) {
        if (isToday) {
            Box(
                modifier = Modifier
                    .size(4.dp)
                    .clip(RoundedCornerShape(2.dp))
                    .background(Color.Red)
            )
        }
    }
}

@Composable
private fun HeatMapMonthHeader(month: CalendarMonth) {
    Text(
        text = month.yearMonth.month.name.take(3),
        style = MaterialTheme.typography.bodySmall,
        color = MaterialTheme.colorScheme.onSurfaceVariant,
        modifier = Modifier.padding(vertical = 4.dp)
    )
}

/**
 * Year View Calendar
 */
@Composable
fun YearCalendar(
    modifier: Modifier = Modifier,
    selectedDate: LocalDate = LocalDate.now(),
    onDateClick: (LocalDate) -> Unit = {},
    disabledDates: Set<LocalDate> = emptySet()
) {
    val currentYear = selectedDate.year
    val months = remember {
        (1..12).map { month -> YearMonth.of(currentYear, month) }
    }
    
    Card(
        modifier = modifier.fillMaxWidth(),
        colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.surfaceVariant
        )
    ) {
        Column(
            modifier = Modifier.padding(16.dp)
        ) {
            Text(
                text = "Year $currentYear",
                style = MaterialTheme.typography.headlineMedium,
                fontWeight = FontWeight.Bold,
                modifier = Modifier.padding(bottom = 16.dp)
            )
            
            LazyColumn(
                verticalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                items(months.chunked(3)) { monthRow ->
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceEvenly
                    ) {
                        monthRow.forEach { month ->
                            YearMonthCard(
                                month = month,
                                selectedDate = selectedDate,
                                onDateClick = onDateClick,
                                disabledDates = disabledDates,
                                modifier = Modifier.weight(1f)
                            )
                        }
                    }
                }
            }
        }
    }
}

@Composable
private fun YearMonthCard(
    month: YearMonth,
    selectedDate: LocalDate,
    onDateClick: (LocalDate) -> Unit,
    disabledDates: Set<LocalDate>,
    modifier: Modifier = Modifier
) {
    Card(
        modifier = modifier
            .padding(4.dp)
            .aspectRatio(1f),
        colors = CardDefaults.cardColors(
            containerColor = if (month.year == selectedDate.year && month.month == selectedDate.month) {
                MaterialTheme.colorScheme.primaryContainer
            } else {
                MaterialTheme.colorScheme.surface
            }
        )
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(8.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text(
                text = month.month.name.take(3),
                style = MaterialTheme.typography.titleMedium,
                fontWeight = FontWeight.Bold,
                color = if (month.year == selectedDate.year && month.month == selectedDate.month) {
                    MaterialTheme.colorScheme.onPrimaryContainer
                } else {
                    MaterialTheme.colorScheme.onSurface
                }
            )
            
            Spacer(modifier = Modifier.height(4.dp))
            
            // Show first few days of the month
            val firstDay = month.atDay(1)
            val daysInMonth = month.lengthOfMonth()
            val startDayOfWeek = firstDay.dayOfWeek.value % 7
            
            // Create a grid of days
            repeat(6) { week ->
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceEvenly
                ) {
                    repeat(7) { dayOfWeek ->
                        val dayNumber = week * 7 + dayOfWeek - startDayOfWeek + 1
                        if (dayNumber in 1..daysInMonth) {
                            val date = month.atDay(dayNumber)
                            val isSelected = date == selectedDate
                            val isDisabled = date in disabledDates
                            
                            Box(
                                modifier = Modifier
                                    .size(16.dp)
                                    .clip(RoundedCornerShape(4.dp))
                                    .background(
                                        when {
                                            isSelected -> MaterialTheme.colorScheme.primary
                                            isDisabled -> MaterialTheme.colorScheme.surfaceVariant
                                            else -> Color.Transparent
                                        }
                                    )
                                    .clickable(enabled = !isDisabled) { onDateClick(date) },
                                contentAlignment = Alignment.Center
                            ) {
                                Text(
                                    text = dayNumber.toString(),
                                    style = MaterialTheme.typography.bodySmall,
                                    color = when {
                                        isSelected -> MaterialTheme.colorScheme.onPrimary
                                        isDisabled -> MaterialTheme.colorScheme.onSurfaceVariant.copy(alpha = 0.5f)
                                        else -> MaterialTheme.colorScheme.onSurface
                                    }
                                )
                            }
                        } else {
                            Spacer(modifier = Modifier.size(16.dp))
                        }
                    }
                }
            }
        }
    }
}

/**
 * Calendar View Type Selector
 */
@Composable
fun CalendarViewSelector(
    selectedViewType: CalendarViewType,
    onViewTypeChange: (CalendarViewType) -> Unit,
    modifier: Modifier = Modifier
) {
    Card(
        modifier = modifier.fillMaxWidth(),
        colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.surfaceVariant
        )
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(8.dp),
            horizontalArrangement = Arrangement.SpaceEvenly
        ) {
            CalendarViewType.values().forEach { viewType ->
                FilterChip(
                    onClick = { onViewTypeChange(viewType) },
                    label = { 
                        Text(
                            text = viewType.name.lowercase().replaceFirstChar { it.uppercase() },
                            style = MaterialTheme.typography.labelMedium
                        )
                    },
                    selected = selectedViewType == viewType,
                    leadingIcon = {
                        Icon(
                            imageVector = when (viewType) {
                                CalendarViewType.MONTH -> Icons.Default.DateRange
                                CalendarViewType.WEEK -> Icons.Default.ViewWeek
                                CalendarViewType.DAY -> Icons.Default.Today
                                CalendarViewType.YEAR -> Icons.Default.DateRange
                                CalendarViewType.HEATMAP -> Icons.Default.Thermostat
                            },
                            contentDescription = null
                        )
                    }
                )
            }
        }
    }
}
