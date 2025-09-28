package com.moderncalendar.feature.calendar

import androidx.compose.material3.ExperimentalMaterial3Api

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import com.moderncalendar.ui.theme.ModernCalendarTheme
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class SelectVisibleCalendarsActivity : ComponentActivity() {
    
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        
        setContent {
            ModernCalendarTheme {
                SelectVisibleCalendarsScreen()
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SelectVisibleCalendarsScreen() {
    var calendars by remember { 
        mutableStateOf(listOf(
            CalendarItem("Personal", Color(0xFF6750A4), true),
            CalendarItem("Work", Color(0xFF1976D2), true),
            CalendarItem("Holidays", Color(0xFF388E3C), false),
            CalendarItem("Birthdays", Color(0xFFE91E63), true),
            CalendarItem("Sports", Color(0xFFFF9800), false)
        ))
    }
    
    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Select Visible Calendars") }
            )
        }
    ) { paddingValues ->
        LazyColumn(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
                .padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            item {
                Text(
                    text = "Choose which calendars to display",
                    style = MaterialTheme.typography.titleMedium,
                    modifier = Modifier.padding(vertical = 8.dp)
                )
            }
            
            items(calendars) { calendar ->
                Card(
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(16.dp),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Row(
                            verticalAlignment = Alignment.CenterVertically,
                            horizontalArrangement = Arrangement.spacedBy(12.dp)
                        ) {
                            Box(
                                modifier = Modifier
                                    .size(16.dp)
                                    .background(calendar.color, shape = androidx.compose.foundation.shape.CircleShape)
                            )
                            Text(
                                text = calendar.name,
                                style = MaterialTheme.typography.bodyLarge
                            )
                        }
                        
                        Switch(
                            checked = calendar.isVisible,
                            onCheckedChange = { isChecked ->
                                calendars = calendars.map { 
                                    if (it.name == calendar.name) it.copy(isVisible = isChecked) else it 
                                }
                            }
                        )
                    }
                }
            }
        }
    }
}

data class CalendarItem(
    val name: String,
    val color: Color,
    val isVisible: Boolean
)
