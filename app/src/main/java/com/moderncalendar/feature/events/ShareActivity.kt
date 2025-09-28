package com.moderncalendar.feature.events

import android.content.Intent
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.moderncalendar.ui.theme.ModernCalendarTheme
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class ShareActivity : ComponentActivity() {
    
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        
        val eventId = intent.getStringExtra("event_id") ?: ""
        
        setContent {
            ModernCalendarTheme {
                ShareScreen(
                    eventId = eventId,
                    onShare = { shareText ->
                        val shareIntent = Intent().apply {
                            action = Intent.ACTION_SEND
                            type = "text/plain"
                            putExtra(Intent.EXTRA_TEXT, shareText)
                        }
                        startActivity(Intent.createChooser(shareIntent, "Share Event"))
                    },
                    onCancel = { finish() }
                )
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ShareScreen(
    eventId: String,
    onShare: (String) -> Unit,
    onCancel: () -> Unit
) {
    var shareText by remember { 
        mutableStateOf("Check out this event: Sample Event on January 15, 2024 at 10:00 AM")
    }
    
    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Share Event") },
                navigationIcon = {
                    TextButton(onClick = onCancel) {
                        Text("Cancel")
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
            Text(
                text = "Share this event with others:",
                style = MaterialTheme.typography.titleMedium
            )
            
            OutlinedTextField(
                value = shareText,
                onValueChange = { shareText = it },
                label = { Text("Share Message") },
                modifier = Modifier
                    .fillMaxWidth()
                    .height(120.dp),
                minLines = 4
            )
            
            Spacer(modifier = Modifier.weight(1f))
            
            Button(
                onClick = { onShare(shareText) },
                modifier = Modifier
                    .fillMaxWidth()
                    .height(48.dp)
            ) {
                Text("Share Event")
            }
        }
    }
}
