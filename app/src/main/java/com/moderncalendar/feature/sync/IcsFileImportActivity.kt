package com.moderncalendar.feature.sync

import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.runtime.Composable

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.FolderOpen
import androidx.compose.material.icons.filled.Upload
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.moderncalendar.ui.theme.ModernCalendarTheme
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class IcsFileImportActivity : ComponentActivity() {
    
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        
        setContent {
            ModernCalendarTheme {
                IcsFileImportScreen()
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun IcsFileImportScreen() {
    var selectedFile by remember { mutableStateOf<String?>(null) }
    var isImporting by remember { mutableStateOf(false) }
    var importProgress by remember { mutableStateOf(0f) }
    var importResult by remember { mutableStateOf<String?>(null) }
    
    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Import ICS File") }
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
                text = "Import ICS Calendar File",
                style = MaterialTheme.typography.headlineMedium
            )
            
            Text(
                text = "Select an .ics file to import calendar events",
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
                        text = "File Selection",
                        style = MaterialTheme.typography.titleMedium
                    )
                    
                    Button(
                        onClick = { 
                            selectedFile = "sample_calendar.ics"
                        },
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        Icon(Icons.Default.FolderOpen, contentDescription = null)
                        Spacer(modifier = Modifier.width(8.dp))
                        Text("Select ICS File")
                    }
                    
                    if (selectedFile != null) {
                        Text(
                            text = "Selected: $selectedFile",
                            style = MaterialTheme.typography.bodyMedium
                        )
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
                            text = "Processing ICS file",
                            style = MaterialTheme.typography.bodyMedium
                        )
                    }
                }
            }
            
            if (importResult != null) {
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
                            text = "Import Complete",
                            style = MaterialTheme.typography.titleMedium
                        )
                        Text(
                            text = importResult!!,
                            style = MaterialTheme.typography.bodyMedium
                        )
                    }
                }
            }
            
            if (selectedFile != null && !isImporting && importResult == null) {
                Button(
                    onClick = { 
                        isImporting = true
                        // Simulate import process
                        importProgress = 1f
                        importResult = "Successfully imported 25 events from $selectedFile"
                        isImporting = false
                    },
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Icon(Icons.Default.Upload, contentDescription = null)
                    Spacer(modifier = Modifier.width(8.dp))
                    Text("Import File")
                }
            }
        }
    }
}
