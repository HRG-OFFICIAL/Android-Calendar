package com.moderncalendar.feature.settings

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.BugReport
import androidx.compose.material.icons.filled.Info
import androidx.compose.material.icons.filled.Notifications
import androidx.compose.material.icons.filled.Palette
import androidx.compose.material3.*
import androidx.compose.material3.MenuAnchorType
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import androidx.navigation.compose.rememberNavController
import com.moderncalendar.core.ui.components.CalendarScaffold
import com.moderncalendar.core.ui.components.ModernCard
import com.moderncalendar.core.ui.components.SwitchField
import com.moderncalendar.core.ui.theme.ThemeMode
import com.moderncalendar.core.ui.theme.ThemeViewModel

@Composable
fun SettingsScreen(
    modifier: Modifier = Modifier,
    onBackClick: () -> Unit = {},
    onSignOut: () -> Unit = {},
    viewModel: SettingsViewModel = hiltViewModel(),
    themeViewModel: ThemeViewModel = hiltViewModel(),
    navController: NavController = rememberNavController(),
) {
    val notificationsEnabled by viewModel.notificationsEnabled.collectAsState()
    val reminderTime by viewModel.reminderTime.collectAsState()
    val weekStartsOn by viewModel.weekStartsOn.collectAsState()
    val timeFormat by viewModel.timeFormat.collectAsState()
    val mockDataMessage by viewModel.mockDataMessage.collectAsState()

    val themeMode by themeViewModel.themeMode.collectAsState()

    val snackbarHostState = remember { SnackbarHostState() }

    // Show snackbar when mock data message changes
    LaunchedEffect(mockDataMessage) {
        mockDataMessage?.let { message ->
            snackbarHostState.showSnackbar(
                message = message,
                duration = SnackbarDuration.Short,
            )
            viewModel.clearMockDataMessage()
        }
    }

    Box(modifier = Modifier.fillMaxSize()) {
        CalendarScaffold(
            navController = navController,
            title = "Settings",
            navigationIcon = Icons.AutoMirrored.Filled.ArrowBack,
            onNavigationClick = onBackClick,
            topBarActions = {
                // Removed duplicate theme toggle button
            },
        ) { paddingValues ->
            Column(
                modifier =
                    modifier
                        .fillMaxSize()
                        .padding(paddingValues)
                        .verticalScroll(rememberScrollState())
                        .padding(16.dp),
                verticalArrangement = Arrangement.spacedBy(24.dp),
            ) {
                // Appearance Section
                SettingsSection(
                    title = "Appearance",
                    icon = Icons.Default.Palette,
                ) {
                    SwitchField(
                        checked = themeMode == ThemeMode.DARK,
                        onCheckedChange = { isDark ->
                            themeViewModel.setThemeMode(if (isDark) ThemeMode.DARK else ThemeMode.LIGHT)
                        },
                        label = "Dark Mode",
                        description = "Use dark theme",
                    )

                    // Week starts on
                    SettingsDropdown(
                        title = "Week starts on",
                        selectedValue = if (weekStartsOn == 1) "Monday" else "Sunday",
                        options = listOf("Sunday", "Monday"),
                        onValueSelected = { value ->
                            viewModel.updateWeekStartsOn(if (value == "Monday") 1 else 7)
                        },
                    )

                    // Time format
                    SettingsDropdown(
                        title = "Time format",
                        selectedValue = if (timeFormat == "24h") "24 hour" else "12 hour",
                        options = listOf("12 hour", "24 hour"),
                        onValueSelected = { value ->
                            viewModel.updateTimeFormat(if (value == "24 hour") "24h" else "12h")
                        },
                    )
                }

                // Notifications Section
                SettingsSection(
                    title = "Notifications",
                    icon = Icons.Default.Notifications,
                ) {
                    SwitchField(
                        checked = notificationsEnabled,
                        onCheckedChange = { viewModel.updateNotificationsEnabled(it) },
                        label = "Event Reminders",
                        description = "Get notified about upcoming events",
                    )

                    if (notificationsEnabled) {
                        SettingsDropdown(
                            title = "Default reminder time",
                            selectedValue = "$reminderTime minutes before",
                            options =
                                listOf(
                                    "5 minutes before",
                                    "10 minutes before",
                                    "15 minutes before",
                                    "30 minutes before",
                                    "1 hour before",
                                ),
                            onValueSelected = { value ->
                                val minutes =
                                    when (value) {
                                        "5 minutes before" -> 5
                                        "10 minutes before" -> 10
                                        "15 minutes before" -> 15
                                        "30 minutes before" -> 30
                                        "1 hour before" -> 60
                                        else -> 15
                                    }
                                viewModel.updateReminderTime(minutes)
                            },
                        )
                    }
                }

                // Developer Section
                SettingsSection(
                    title = "Developer",
                    icon = Icons.Default.BugReport,
                ) {
                    SettingsItem(
                        title = "Add Mock Events",
                        subtitle = "Add 5 sample calendar events for testing",
                        onClick = {
                            viewModel.addMockEvents()
                        },
                    )

                    SettingsItem(
                        title = "Refresh Mock Events",
                        subtitle = "Update mock events to today's date",
                        onClick = {
                            viewModel.refreshMockEventsForToday()
                        },
                    )

                    SettingsItem(
                        title = "Reset Mock Data Flag",
                        subtitle = "Allow mock data to be added again",
                        onClick = {
                            viewModel.resetMockDataFlag()
                        },
                    )
                }

                // About Section
                SettingsSection(
                    title = "About",
                    icon = Icons.Default.Info,
                ) {
                    SettingsItem(
                        title = "Version",
                        subtitle = "1.0.0",
                    )

                    SettingsItem(
                        title = "Privacy Policy",
                        subtitle = "View our privacy policy",
                    )

                    SettingsItem(
                        title = "Terms of Service",
                        subtitle = "View terms of service",
                    )
                }
            }
        }

        SnackbarHost(
            hostState = snackbarHostState,
            modifier = Modifier.align(Alignment.BottomCenter),
        )
    }
}

@Composable
private fun SettingsSection(
    title: String,
    icon: androidx.compose.ui.graphics.vector.ImageVector,
    content: @Composable () -> Unit,
) {
    Column {
        Row(
            verticalAlignment = Alignment.CenterVertically,
            modifier = Modifier.padding(bottom = 12.dp),
        ) {
            Box(
                modifier =
                    Modifier
                        .size(32.dp)
                        .clip(CircleShape)
                        .background(MaterialTheme.colorScheme.primary.copy(alpha = 0.1f)),
                contentAlignment = Alignment.Center,
            ) {
                Icon(
                    imageVector = icon,
                    contentDescription = null,
                    tint = MaterialTheme.colorScheme.primary,
                    modifier = Modifier.size(18.dp),
                )
            }
            Spacer(modifier = Modifier.width(12.dp))
            Text(
                text = title,
                style =
                    MaterialTheme.typography.titleLarge.copy(
                        fontWeight = FontWeight.SemiBold,
                    ),
                color = MaterialTheme.colorScheme.onSurface,
            )
        }

        ModernCard(
            elevation = 2,
        ) {
            content()
        }
    }
}

@Composable
private fun SettingsItem(
    title: String,
    subtitle: String,
    onClick: () -> Unit = {},
) {
    Surface(
        modifier =
            Modifier
                .fillMaxWidth()
                .padding(vertical = 2.dp)
                .clip(MaterialTheme.shapes.medium)
                .clickable { onClick() },
        color = Color.Transparent,
    ) {
        Column(
            modifier = Modifier.padding(16.dp),
        ) {
            Text(
                text = title,
                style =
                    MaterialTheme.typography.bodyLarge.copy(
                        fontWeight = FontWeight.SemiBold,
                    ),
                color = MaterialTheme.colorScheme.onSurface,
            )
            Spacer(modifier = Modifier.height(4.dp))
            Text(
                text = subtitle,
                style = MaterialTheme.typography.bodyMedium,
                color = MaterialTheme.colorScheme.onSurfaceVariant,
            )
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun SettingsDropdown(
    title: String,
    selectedValue: String,
    options: List<String>,
    onValueSelected: (String) -> Unit,
    modifier: Modifier = Modifier,
) {
    var expanded by remember { mutableStateOf(false) }

    Column(modifier = modifier.padding(vertical = 4.dp)) {
        Text(
            text = title,
            style = MaterialTheme.typography.bodyMedium,
            color = MaterialTheme.colorScheme.onSurfaceVariant,
            modifier = Modifier.padding(bottom = 8.dp),
        )

        ExposedDropdownMenuBox(
            expanded = expanded,
            onExpandedChange = { expanded = !expanded },
        ) {
            OutlinedTextField(
                value = selectedValue,
                onValueChange = { },
                readOnly = true,
                trailingIcon = { ExposedDropdownMenuDefaults.TrailingIcon(expanded = expanded) },
                modifier =
                    Modifier
                        .fillMaxWidth()
                        .menuAnchor(MenuAnchorType.PrimaryNotEditable, enabled = true),
            )

            ExposedDropdownMenu(
                expanded = expanded,
                onDismissRequest = { expanded = false },
            ) {
                options.forEach { option ->
                    DropdownMenuItem(
                        text = { Text(option) },
                        onClick = {
                            onValueSelected(option)
                            expanded = false
                        },
                    )
                }
            }
        }
    }
}
