package com.moderncalendar.feature.settings

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.DarkMode
import androidx.compose.material.icons.filled.LightMode
import androidx.compose.material.icons.filled.Notifications
import androidx.compose.material.icons.filled.Palette
import androidx.compose.material.icons.filled.Logout
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SettingsScreen(
    modifier: Modifier = Modifier,
    onBackClick: () -> Unit = {},
    onSignOut: () -> Unit = {},
    viewModel: SettingsViewModel = hiltViewModel()
) {
    val uiState by viewModel.uiState.collectAsState()
    
    Column(
        modifier = modifier.fillMaxSize()
    ) {
        // Top App Bar
        TopAppBar(
            title = { Text("Settings") },
            navigationIcon = {
                IconButton(onClick = onBackClick) {
                    Icon(Icons.Default.ArrowBack, contentDescription = "Back")
                }
            }
        )
        
        Column(
            modifier = Modifier
                .fillMaxSize()
                .verticalScroll(rememberScrollState())
                .padding(16.dp)
        ) {
            // Theme Section
            SettingsSection(
                title = "Appearance",
                icon = Icons.Default.Palette
            ) {
                // Dark Mode Toggle
                SettingsItem(
                    title = "Dark Mode",
                    subtitle = "Use dark theme",
                    icon = if (uiState.isDarkMode) Icons.Default.DarkMode else Icons.Default.LightMode,
                    onClick = { viewModel.toggleDarkMode() }
                )
                
                // Dynamic Color Toggle
                SettingsItem(
                    title = "Dynamic Colors",
                    subtitle = "Use system color scheme",
                    icon = Icons.Default.Palette,
                    onClick = { viewModel.toggleDynamicColors() }
                )
            }
            
            Spacer(modifier = Modifier.height(24.dp))
            
            // Notifications Section
            SettingsSection(
                title = "Notifications",
                icon = Icons.Default.Notifications
            ) {
                // Event Reminders Toggle
                SettingsItem(
                    title = "Event Reminders",
                    subtitle = "Get notified about upcoming events",
                    icon = Icons.Default.Notifications,
                    onClick = { viewModel.toggleEventReminders() }
                )
            }
            
            Spacer(modifier = Modifier.height(24.dp))
            
            // Account Section
            SettingsSection(
                title = "Account",
                icon = Icons.Default.Palette
            ) {
                SettingsItem(
                    title = "Sign Out",
                    subtitle = "Sign out of your account",
                    icon = Icons.Default.Logout,
                    onClick = onSignOut
                )
            }
            
            Spacer(modifier = Modifier.height(24.dp))
            
            // About Section
            SettingsSection(
                title = "About",
                icon = Icons.Default.Palette
            ) {
                SettingsItem(
                    title = "Version",
                    subtitle = "1.0.0",
                    icon = null,
                    onClick = { }
                )
                
                SettingsItem(
                    title = "Privacy Policy",
                    subtitle = "View our privacy policy",
                    icon = null,
                    onClick = { }
                )
                
                SettingsItem(
                    title = "Terms of Service",
                    subtitle = "View terms of service",
                    icon = null,
                    onClick = { }
                )
            }
        }
    }
}

@Composable
private fun SettingsSection(
    title: String,
    icon: androidx.compose.ui.graphics.vector.ImageVector,
    content: @Composable () -> Unit
) {
    Column {
        Row(
            verticalAlignment = Alignment.CenterVertically,
            modifier = Modifier.padding(bottom = 8.dp)
        ) {
            Icon(
                imageVector = icon,
                contentDescription = null,
                tint = MaterialTheme.colorScheme.primary
            )
            Spacer(modifier = Modifier.width(8.dp))
            Text(
                text = title,
                style = MaterialTheme.typography.titleMedium,
                fontWeight = FontWeight.Medium,
                color = MaterialTheme.colorScheme.primary
            )
        }
        
        Card(
            modifier = Modifier.fillMaxWidth(),
            colors = CardDefaults.cardColors(
                containerColor = MaterialTheme.colorScheme.surfaceVariant
            )
        ) {
            Column {
                content()
            }
        }
    }
}

@Composable
private fun SettingsItem(
    title: String,
    subtitle: String,
    icon: androidx.compose.ui.graphics.vector.ImageVector?,
    onClick: () -> Unit
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .clickable { onClick() }
            .padding(16.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        icon?.let {
            Icon(
                imageVector = it,
                contentDescription = null,
                tint = MaterialTheme.colorScheme.onSurfaceVariant
            )
            Spacer(modifier = Modifier.width(16.dp))
        }
        
        Column(
            modifier = Modifier.weight(1f)
        ) {
            Text(
                text = title,
                style = MaterialTheme.typography.bodyLarge,
                fontWeight = FontWeight.Medium
            )
            Text(
                text = subtitle,
                style = MaterialTheme.typography.bodyMedium,
                color = MaterialTheme.colorScheme.onSurfaceVariant
            )
        }
    }
}
