package com.moderncalendar.core.ui.components

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import com.moderncalendar.core.ui.theme.EventColors
import java.time.LocalDate
import java.time.LocalTime
import java.time.format.DateTimeFormatter

/**
 * Form components for event creation and editing
 */

/**
 * Validation utilities for date and time fields
 */
object DateTimeValidation {
    
    /**
     * Validates that end time is after start time on the same date
     */
    fun validateTimeRange(
        startTime: LocalTime?,
        endTime: LocalTime?,
        allowSameTime: Boolean = false
    ): String? {
        if (startTime == null || endTime == null) return null
        
        return when {
            endTime.isBefore(startTime) -> "End time must be after start time"
            !allowSameTime && endTime == startTime -> "End time must be different from start time"
            else -> null
        }
    }
    
    /**
     * Validates that end date is not before start date
     */
    fun validateDateRange(
        startDate: LocalDate?,
        endDate: LocalDate?
    ): String? {
        if (startDate == null || endDate == null) return null
        
        return when {
            endDate.isBefore(startDate) -> "End date cannot be before start date"
            else -> null
        }
    }
    
    /**
     * Validates that a date is not in the past (for new events)
     */
    fun validateFutureDate(
        date: LocalDate?,
        allowToday: Boolean = true
    ): String? {
        if (date == null) return null
        
        val today = LocalDate.now()
        return when {
            date.isBefore(today) -> "Date cannot be in the past"
            !allowToday && date == today -> "Date must be in the future"
            else -> null
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CalendarTextField(
    value: String,
    onValueChange: (String) -> Unit,
    label: String,
    modifier: Modifier = Modifier,
    leadingIcon: ImageVector? = null,
    trailingIcon: @Composable (() -> Unit)? = null,
    isError: Boolean = false,
    errorMessage: String? = null,
    keyboardOptions: KeyboardOptions = KeyboardOptions.Default,
    maxLines: Int = 1,
    placeholder: String? = null
) {
    Column(modifier = modifier) {
        OutlinedTextField(
            value = value,
            onValueChange = onValueChange,
            label = { Text(label) },
            leadingIcon = leadingIcon?.let { icon ->
                {
                    Icon(
                        imageVector = icon,
                        contentDescription = null
                    )
                }
            },
            trailingIcon = trailingIcon,
            isError = isError,
            keyboardOptions = keyboardOptions,
            maxLines = maxLines,
            placeholder = placeholder?.let { { Text(it) } },
            colors = OutlinedTextFieldDefaults.colors(
                focusedTextColor = MaterialTheme.colorScheme.onSurface,
                unfocusedTextColor = MaterialTheme.colorScheme.onSurface,
                cursorColor = MaterialTheme.colorScheme.primary
            ),
            modifier = Modifier.fillMaxWidth()
        )
        
        if (isError && errorMessage != null) {
            Text(
                text = errorMessage,
                color = MaterialTheme.colorScheme.error,
                style = MaterialTheme.typography.bodySmall,
                modifier = Modifier.padding(start = 16.dp, top = 4.dp)
            )
        }
    }
}

@Composable
fun DatePickerField(
    selectedDate: LocalDate?,
    onDateSelected: (LocalDate) -> Unit,
    label: String,
    modifier: Modifier = Modifier,
    isError: Boolean = false,
    errorMessage: String? = null,
    minDate: LocalDate? = null,
    maxDate: LocalDate? = null
) {
    var showDatePicker by remember { mutableStateOf(false) }
    
    CalendarTextField(
        value = selectedDate?.format(DateTimeFormatter.ofPattern("MMM dd, yyyy")) ?: "",
        onValueChange = { },
        label = label,
        leadingIcon = Icons.Default.DateRange,
        trailingIcon = {
            IconButton(onClick = { showDatePicker = true }) {
                Icon(
                    imageVector = Icons.Default.ArrowDropDown,
                    contentDescription = "Select date"
                )
            }
        },
        isError = isError,
        errorMessage = errorMessage,
        modifier = modifier.clickable { showDatePicker = true }
    )
    
    if (showDatePicker) {
        DatePickerDialog(
            onDateSelected = { date ->
                onDateSelected(date)
                showDatePicker = false
            },
            onDismiss = { showDatePicker = false },
            initialDate = selectedDate,
            minDate = minDate,
            maxDate = maxDate
        )
    }
}

@Composable
fun TimePickerField(
    selectedTime: LocalTime?,
    onTimeSelected: (LocalTime) -> Unit,
    label: String,
    modifier: Modifier = Modifier,
    isError: Boolean = false,
    errorMessage: String? = null,
    is24Hour: Boolean = true
) {
    var showTimePicker by remember { mutableStateOf(false) }
    
    // Format time based on 24-hour preference
    val timeFormatter = if (is24Hour) {
        DateTimeFormatter.ofPattern("HH:mm")
    } else {
        DateTimeFormatter.ofPattern("h:mm a")
    }
    
    CalendarTextField(
        value = selectedTime?.format(timeFormatter) ?: "",
        onValueChange = { },
        label = label,
        leadingIcon = Icons.Default.Schedule,
        trailingIcon = {
            IconButton(onClick = { showTimePicker = true }) {
                Icon(
                    imageVector = Icons.Default.ArrowDropDown,
                    contentDescription = "Select time"
                )
            }
        },
        isError = isError,
        errorMessage = errorMessage,
        modifier = modifier.clickable { showTimePicker = true }
    )
    
    if (showTimePicker) {
        TimePickerDialog(
            onTimeSelected = { time ->
                onTimeSelected(time)
                showTimePicker = false
            },
            onDismiss = { showTimePicker = false },
            initialTime = selectedTime,
            is24Hour = is24Hour
        )
    }
}

@Composable
fun ColorPickerField(
    selectedColor: Color,
    onColorSelected: (Color) -> Unit,
    label: String,
    modifier: Modifier = Modifier
) {
    var showColorPicker by remember { mutableStateOf(false) }
    
    Column(modifier = modifier) {
        Text(
            text = label,
            style = MaterialTheme.typography.bodyMedium,
            color = MaterialTheme.colorScheme.onSurfaceVariant,
            modifier = Modifier.padding(bottom = 8.dp)
        )
        
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .clickable { showColorPicker = true },
            verticalAlignment = Alignment.CenterVertically
        ) {
            Box(
                modifier = Modifier
                    .size(32.dp)
                    .clip(CircleShape)
                    .background(selectedColor)
            )
            
            Spacer(modifier = Modifier.width(12.dp))
            
            Text(
                text = "Tap to change color",
                style = MaterialTheme.typography.bodyMedium,
                color = MaterialTheme.colorScheme.onSurfaceVariant
            )
        }
        
        if (showColorPicker) {
            ColorPickerDialog(
                selectedColor = selectedColor,
                onColorSelected = { color ->
                    onColorSelected(color)
                    showColorPicker = false
                },
                onDismiss = { showColorPicker = false }
            )
        }
    }
}

@Composable
fun TimeRangeField(
    startTime: LocalTime?,
    endTime: LocalTime?,
    onStartTimeSelected: (LocalTime) -> Unit,
    onEndTimeSelected: (LocalTime) -> Unit,
    modifier: Modifier = Modifier,
    is24Hour: Boolean = true,
    allowSameTime: Boolean = false
) {
    val timeRangeError = DateTimeValidation.validateTimeRange(startTime, endTime, allowSameTime)
    
    Row(
        modifier = modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        TimePickerField(
            selectedTime = startTime,
            onTimeSelected = onStartTimeSelected,
            label = "Start Time",
            modifier = Modifier.weight(1f),
            is24Hour = is24Hour
        )
        
        TimePickerField(
            selectedTime = endTime,
            onTimeSelected = onEndTimeSelected,
            label = "End Time",
            modifier = Modifier.weight(1f),
            isError = timeRangeError != null,
            errorMessage = timeRangeError,
            is24Hour = is24Hour
        )
    }
}

@Composable
fun DateRangeField(
    startDate: LocalDate?,
    endDate: LocalDate?,
    onStartDateSelected: (LocalDate) -> Unit,
    onEndDateSelected: (LocalDate) -> Unit,
    modifier: Modifier = Modifier,
    minDate: LocalDate? = null,
    maxDate: LocalDate? = null
) {
    val dateRangeError = DateTimeValidation.validateDateRange(startDate, endDate)
    
    Column(modifier = modifier.fillMaxWidth()) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            DatePickerField(
                selectedDate = startDate,
                onDateSelected = onStartDateSelected,
                label = "From",
                modifier = Modifier.weight(1f),
                minDate = minDate,
                maxDate = maxDate
            )
            
            DatePickerField(
                selectedDate = endDate,
                onDateSelected = onEndDateSelected,
                label = "To",
                modifier = Modifier.weight(1f),
                isError = dateRangeError != null,
                errorMessage = null, // Don't show error on individual field
                minDate = startDate ?: minDate, // End date can't be before start date
                maxDate = maxDate
            )
        }
        
        // Reserved space for error message to prevent height changes
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .height(20.dp), // Fixed height for error message area
            contentAlignment = Alignment.CenterStart
        ) {
            if (dateRangeError != null) {
                Text(
                    text = dateRangeError,
                    color = MaterialTheme.colorScheme.error,
                    style = MaterialTheme.typography.bodySmall,
                    modifier = Modifier.padding(start = 16.dp, top = 4.dp)
                )
            }
        }
    }
}

@Composable
fun SwitchField(
    checked: Boolean,
    onCheckedChange: (Boolean) -> Unit,
    label: String,
    description: String? = null,
    modifier: Modifier = Modifier
) {
    Row(
        modifier = modifier
            .fillMaxWidth()
            .clickable { onCheckedChange(!checked) }
            .padding(vertical = 8.dp),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
    ) {
        Column(modifier = Modifier.weight(1f)) {
            Text(
                text = label,
                style = MaterialTheme.typography.bodyLarge,
                color = MaterialTheme.colorScheme.onSurface
            )
            
            description?.let { desc ->
                Text(
                    text = desc,
                    style = MaterialTheme.typography.bodySmall,
                    color = MaterialTheme.colorScheme.onSurfaceVariant,
                    modifier = Modifier.padding(top = 2.dp)
                )
            }
        }
        
        Switch(
            checked = checked,
            onCheckedChange = onCheckedChange
        )
    }
}

// Material 3 DatePickerDialog implementation
@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun DatePickerDialog(
    onDateSelected: (LocalDate) -> Unit,
    onDismiss: () -> Unit,
    initialDate: LocalDate? = null,
    minDate: LocalDate? = null,
    maxDate: LocalDate? = null
) {
    val datePickerState = rememberDatePickerState(
        initialSelectedDateMillis = initialDate?.toEpochDay()?.times(24 * 60 * 60 * 1000),
        yearRange = (minDate?.year ?: 1900)..(maxDate?.year ?: 2100)
    )
    
    DatePickerDialog(
        onDismissRequest = onDismiss,
        confirmButton = {
            TextButton(
                onClick = {
                    datePickerState.selectedDateMillis?.let { millis ->
                        val selectedDate = LocalDate.ofEpochDay(millis / (24 * 60 * 60 * 1000))
                        // Validate date range if constraints are provided
                        val isValidDate = (minDate == null || !selectedDate.isBefore(minDate)) &&
                                        (maxDate == null || !selectedDate.isAfter(maxDate))
                        
                        if (isValidDate) {
                            onDateSelected(selectedDate)
                        }
                    }
                }
            ) {
                Text("OK")
            }
        },
        dismissButton = {
            TextButton(onClick = onDismiss) {
                Text("Cancel")
            }
        }
    ) {
        DatePicker(
            state = datePickerState,
            modifier = Modifier.padding(16.dp)
        )
    }
}

// Material 3 TimePickerDialog implementation
@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun TimePickerDialog(
    onTimeSelected: (LocalTime) -> Unit,
    onDismiss: () -> Unit,
    initialTime: LocalTime? = null,
    is24Hour: Boolean = true
) {
    val timePickerState = rememberTimePickerState(
        initialHour = initialTime?.hour ?: 9,
        initialMinute = initialTime?.minute ?: 0,
        is24Hour = is24Hour
    )
    
    AlertDialog(
        onDismissRequest = onDismiss,
        title = { 
            Text(
                text = "Select Time",
                style = MaterialTheme.typography.headlineSmall
            ) 
        },
        text = {
            Column(
                horizontalAlignment = Alignment.CenterHorizontally,
                modifier = Modifier.fillMaxWidth()
            ) {
                TimePicker(
                    state = timePickerState,
                    modifier = Modifier.padding(16.dp)
                )
            }
        },
        confirmButton = {
            TextButton(
                onClick = {
                    val selectedTime = LocalTime.of(
                        timePickerState.hour,
                        timePickerState.minute
                    )
                    onTimeSelected(selectedTime)
                }
            ) {
                Text("OK")
            }
        },
        dismissButton = {
            TextButton(onClick = onDismiss) {
                Text("Cancel")
            }
        }
    )
}

@Composable
private fun ColorPickerDialog(
    selectedColor: Color,
    onColorSelected: (Color) -> Unit,
    onDismiss: () -> Unit
) {
    AlertDialog(
        onDismissRequest = onDismiss,
        title = { Text("Select Color") },
        text = {
            LazyVerticalGrid(
                columns = GridCells.Fixed(5),
                contentPadding = PaddingValues(8.dp),
                horizontalArrangement = Arrangement.spacedBy(8.dp),
                verticalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                items(EventColors.size) { index ->
                    val color = EventColors[index]
                    Box(
                        modifier = Modifier
                            .size(40.dp)
                            .clip(CircleShape)
                            .background(color)
                            .clickable { onColorSelected(color) }
                    )
                }
            }
        },
        confirmButton = {
            TextButton(onClick = onDismiss) {
                Text("Done")
            }
        }
    )
}