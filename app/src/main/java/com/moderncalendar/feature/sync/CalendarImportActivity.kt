package com.moderncalendar.feature.sync

import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.runtime.Composable

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.CloudDownload
import androidx.compose.material.icons.filled.Upload
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.moderncalendar.ui.theme.ModernCalendarTheme
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class CalendarImportActivity : ComponentActivity() {
    
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        
        setContent {
            ModernCalendarTheme {
                CalendarImportScreen()
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CalendarImportScreen() {
    var importSource by remember { mutableStateOf("") }
    var isImporting by remember { mutableStateOf(false) }
    var importProgress by remember { mutableStateOf(0f) }
    
    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Import Calendar") }
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
                text = "Import Calendar Data",
                style = MaterialTheme.typography.headlineMedium
            )
            
            Text(
                text = "Choose how you want to import your calendar data",
                style = MaterialTheme.typography.bodyLarge
            )
            
            Card(
                modifier = Modifier.fillMaxWidth()
            ) {
                Column(
                    modifier = Modifier.padding(16.dp),
                    verticalArrangement = Arrangement.spacedBy(16.dp)
                ) {
                    Text(
                        text = "Import Options",
                        style = MaterialTheme.typography.titleMedium
                    )
                    
                    Button(
                        onClick = { importSource = "Google" },
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        Icon(Icons.Default.CloudDownload, contentDescription = null)
                        Spacer(modifier = Modifier.width(8.dp))
                        Text("Import from Google Calendar")
                    }
                    
                    Button(
                        onClick = { importSource = "Outlook" },
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        Icon(Icons.Default.CloudDownload, contentDescription = null)
                        Spacer(modifier = Modifier.width(8.dp))
                        Text("Import from Outlook")
                    }
                    
                    Button(
                        onClick = { importSource = "File" },
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        Icon(Icons.Default.Upload, contentDescription = null)
                        Spacer(modifier = Modifier.width(8.dp))
                        Text("Import from File")
                    }
                }
            }
            
            if (isImporting) {
                Card(
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Column(
                        modifier = Modifier.padding(16.dp),
                        verticalArrangement = Arrangement.spacedBy(8.dp)
                    ) {
                        Text(
                            text = "Importing...",
                            style = MaterialTheme.typography.titleMedium
                        )
                        
                        LinearProgressIndicator(
                            progress = { importProgress },
                            modifier = Modifier.fillMaxWidth()
                        )
                        
                        Text(
                            text = "Importing from $importSource",
                            style = MaterialTheme.typography.bodyMedium
                        )
                    }
                }
            }
            
            if (importSource.isNotEmpty() && !isImporting) {
                Card(
                    modifier = Modifier.fillMaxWidth(),
                    colors = CardDefaults.cardColors(
                        containerColor = MaterialTheme.colorScheme.primaryContainer
                    )
                ) {
                    Column(
                        modifier = Modifier.padding(16.dp),
                        verticalArrangement = Arrangement.spacedBy(8.dp)
                    ) {
                        Text(
                            text = "Ready to Import",
                            style = MaterialTheme.typography.titleMedium
                        )
                        Text(
                            text = "Selected source: $importSource",
                            style = MaterialTheme.typography.bodyMedium
                        )
                        
                        Button(
                            onClick = { 
                                isImporting = true
                                // Simulate import progress
                                importProgress = 1f
                            },
                            modifier = Modifier.fillMaxWidth()
                        ) {
                            Text("Start Import")
                        }
                    }
                }
            }
        }
    }
}
