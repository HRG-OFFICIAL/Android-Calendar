package com.moderncalendar.alerts

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.moderncalendar.core.ui.theme.ModernCalendarTheme
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class AlertActivity : ComponentActivity() {
    
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        
        val eventId = intent.getStringExtra("event_id") ?: ""
        
        setContent {
            ModernCalendarTheme {
                EventReminderScreen(
                    eventId = eventId,
                    onDismiss = { finish() },
                    onSnooze = { delayMinutes ->
                        // Schedule snooze
                        finish()
                    }
                )
            }
        }
    }
}

@Composable
fun EventReminderScreen(
    eventId: String,
    onDismiss: () -> Unit,
    onSnooze: (Int) -> Unit
) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        Text(
            text = "Event Reminder",
            style = MaterialTheme.typography.headlineMedium
        )
        
        Spacer(modifier = Modifier.height(16.dp))
        
        Text(
            text = "Event ID: $eventId",
            style = MaterialTheme.typography.bodyLarge
        )
        
        Spacer(modifier = Modifier.height(32.dp))
        
        Row(
            horizontalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            Button(
                onClick = onDismiss,
                colors = ButtonDefaults.buttonColors(
                    containerColor = MaterialTheme.colorScheme.error
                )
            ) {
                Text("Dismiss")
            }
            
            Button(
                onClick = { onSnooze(15) }
            ) {
                Text("Snooze 15min")
            }
        }
    }
}
