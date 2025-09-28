package com.moderncalendar.feature.events

import android.content.Intent
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material.icons.filled.Share
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.moderncalendar.ui.theme.ModernCalendarTheme
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class EventInfoActivity : ComponentActivity() {
    
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        
        val eventId = intent.getStringExtra("event_id") ?: ""
        
        setContent {
            ModernCalendarTheme {
                EventInfoScreen(
                    eventId = eventId,
                    onEditEvent = { 
                        val editIntent = Intent(this, EventEditActivity::class.java).apply {
                            putExtra("event_id", eventId)
                            action = Intent.ACTION_EDIT
                        }
                        startActivity(editIntent)
                    },
                    onShareEvent = {
                        val shareIntent = Intent(this, ShareActivity::class.java).apply {
                            putExtra("event_id", eventId)
                        }
                        startActivity(shareIntent)
                    }
                )
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun EventInfoScreen(
    eventId: String,
    onEditEvent: () -> Unit,
    onShareEvent: () -> Unit
) {
    // Mock data - replace with actual data loading
    val eventTitle = "Sample Event"
    val eventDescription = "This is a sample event description"
    val eventLocation = "Conference Room A"
    val eventDate = "January 15, 2024"
    val eventTime = "10:00 AM - 11:00 AM"
    
    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Event Details") },
                actions = {
                    IconButton(onClick = onEditEvent) {
                        Icon(Icons.Default.Edit, contentDescription = "Edit")
                    }
                    IconButton(onClick = onShareEvent) {
                        Icon(Icons.Default.Share, contentDescription = "Share")
                    }
                }
            )
        }
    ) { paddingValues ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
                .padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            Card(
                modifier = Modifier.fillMaxWidth()
            ) {
                Column(
                    modifier = Modifier.padding(16.dp),
                    verticalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    Text(
                        text = eventTitle,
                        style = MaterialTheme.typography.headlineSmall
                    )
                    
                    Text(
                        text = eventDate,
                        style = MaterialTheme.typography.titleMedium
                    )
                    
                    Text(
                        text = eventTime,
                        style = MaterialTheme.typography.bodyLarge
                    )
                    
                    if (eventLocation.isNotBlank()) {
                        Text(
                            text = "📍 $eventLocation",
                            style = MaterialTheme.typography.bodyMedium
                        )
                    }
                    
                    if (eventDescription.isNotBlank()) {
                        Spacer(modifier = Modifier.height(8.dp))
                        Text(
                            text = eventDescription,
                            style = MaterialTheme.typography.bodyMedium
                        )
                    }
                }
            }
            
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                Button(
                    onClick = onEditEvent,
                    modifier = Modifier.weight(1f)
                ) {
                    Text("Edit Event")
                }
                
                OutlinedButton(
                    onClick = onShareEvent,
                    modifier = Modifier.weight(1f)
                ) {
                    Text("Share")
                }
            }
        }
    }
}
