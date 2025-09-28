package com.moderncalendar.feature.settings

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.moderncalendar.ui.theme.ModernCalendarTheme
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class AccountSettingsActivity : ComponentActivity() {
    
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        
        setContent {
            ModernCalendarTheme {
                AccountSettingsScreen()
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AccountSettingsScreen() {
    var accounts by remember { 
        mutableStateOf(listOf(
            "Google Account (user@gmail.com)",
            "Microsoft Account (user@outlook.com)",
            "Local Account"
        ))
    }
    
    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Account Settings") }
            )
        },
        floatingActionButton = {
            FloatingActionButton(
                onClick = { /* Add new account */ }
            ) {
                Icon(Icons.Default.Add, contentDescription = "Add Account")
            }
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
                    text = "Connected Accounts",
                    style = MaterialTheme.typography.titleLarge,
                    modifier = Modifier.padding(vertical = 8.dp)
                )
            }
            
            items(accounts) { account ->
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
                        Text(
                            text = account,
                            style = MaterialTheme.typography.bodyLarge
                        )
                        
                        Row {
                            IconButton(onClick = { /* Edit account */ }) {
                                Icon(Icons.Default.Edit, contentDescription = "Edit")
                            }
                            IconButton(onClick = { 
                                accounts = accounts.filter { it != account }
                            }) {
                                Icon(Icons.Default.Delete, contentDescription = "Delete")
                            }
                        }
                    }
                }
            }
            
            item {
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
                            text = "Sync Settings",
                            style = MaterialTheme.typography.titleMedium
                        )
                        Text(
                            text = "Manage how your calendars sync across devices",
                            style = MaterialTheme.typography.bodyMedium
                        )
                    }
                }
            }
        }
    }
}
