package com.moderncalendar.feature.search

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.moderncalendar.ui.theme.ModernCalendarTheme
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class EventSearchActivity : ComponentActivity() {
    
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        
        setContent {
            ModernCalendarTheme {
                EventSearchScreen()
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun EventSearchScreen() {
    var searchQuery by remember { mutableStateOf("") }
    var searchResults by remember { mutableStateOf(listOf<String>()) }
    
    // Mock search results
    LaunchedEffect(searchQuery) {
        if (searchQuery.isNotBlank()) {
            searchResults = listOf(
                "Team Meeting - Conference Room A",
                "Project Deadline - Office",
                "Lunch with Client - Restaurant",
                "Weekly Standup - Meeting Room B"
            ).filter { it.contains(searchQuery, ignoreCase = true) }
        } else {
            searchResults = emptyList()
        }
    }
    
    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Search Events") },
                navigationIcon = {
                    IconButton(onClick = { /* Handle back */ }) {
                        Icon(Icons.Default.Search, contentDescription = "Search")
                    }
                }
            )
        }
    ) { paddingValues ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
                .padding(16.dp)
        ) {
            OutlinedTextField(
                value = searchQuery,
                onValueChange = { searchQuery = it },
                label = { Text("Search events...") },
                leadingIcon = {
                    Icon(Icons.Default.Search, contentDescription = "Search")
                },
                modifier = Modifier.fillMaxWidth()
            )
            
            Spacer(modifier = Modifier.height(16.dp))
            
            if (searchQuery.isBlank()) {
                Text(
                    text = "Enter a search term to find events",
                    style = MaterialTheme.typography.bodyLarge
                )
            } else if (searchResults.isEmpty()) {
                Text(
                    text = "No events found for \"$searchQuery\"",
                    style = MaterialTheme.typography.bodyLarge
                )
            } else {
                LazyColumn(
                    verticalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    items(searchResults) { result ->
                        Card(
                            modifier = Modifier.fillMaxWidth()
                        ) {
                            Text(
                                text = result,
                                modifier = Modifier.padding(16.dp),
                                style = MaterialTheme.typography.bodyLarge
                            )
                        }
                    }
                }
            }
        }
    }
}
