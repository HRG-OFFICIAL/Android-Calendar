package com.moderncalendar.feature.search

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.FilterList
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import androidx.navigation.compose.rememberNavController
import com.moderncalendar.core.common.Result
import com.moderncalendar.core.common.model.EventPriority
import com.moderncalendar.core.ui.components.CalendarScaffold
import com.moderncalendar.core.ui.components.CalendarTextField
import com.moderncalendar.core.ui.components.DateRangeField
import com.moderncalendar.core.ui.components.EmptyState
import com.moderncalendar.core.ui.components.EventCard
import com.moderncalendar.core.ui.components.LoadingIndicator
import java.time.LocalDate
import java.time.format.DateTimeFormatter

@Composable
fun SearchScreen(
    modifier: Modifier = Modifier,
    onBackClick: () -> Unit = {},
    onEventClick: (String) -> Unit = {},
    viewModel: SearchViewModel = hiltViewModel(),
    navController: NavController = rememberNavController(),
) {
    var searchQuery by remember { mutableStateOf("") }
    var showFilters by remember { mutableStateOf(false) }
    var selectedPriority by remember { mutableStateOf<EventPriority?>(null) }
    var startDate by remember { mutableStateOf<LocalDate?>(null) }
    var endDate by remember { mutableStateOf<LocalDate?>(null) }

    val searchResults by viewModel.searchResults.collectAsState()
    val isLoading by viewModel.isSearching.collectAsState()

    LaunchedEffect(searchQuery, selectedPriority, startDate, endDate) {
        if (searchQuery.isNotEmpty() || selectedPriority != null || startDate != null) {
            viewModel.searchEventsWithFilters(
                query = searchQuery,
                priority = selectedPriority,
                startDate = startDate,
                endDate = endDate,
            )
        }
    }

    CalendarScaffold(
        navController = navController,
        title = "Search Events",
        navigationIcon = Icons.AutoMirrored.Filled.ArrowBack,
        onNavigationClick = onBackClick,
        topBarActions = {
            IconButton(onClick = { showFilters = !showFilters }) {
                Icon(Icons.Default.FilterList, contentDescription = "Filters")
            }
        },
    ) { paddingValues ->
        Column(
            modifier =
                modifier
                    .fillMaxSize()
                    .padding(paddingValues),
        ) {
            // Search Bar
            CalendarTextField(
                value = searchQuery,
                onValueChange = { searchQuery = it },
                label = "Search events",
                placeholder = "Search by title, description, or location...",
                leadingIcon = Icons.Default.Search,
                modifier = Modifier.padding(16.dp),
            )

            // Filters Section
            if (showFilters) {
                SearchFilters(
                    selectedPriority = selectedPriority,
                    onPrioritySelected = { selectedPriority = it },
                    startDate = startDate,
                    onStartDateSelected = { startDate = it },
                    endDate = endDate,
                    onEndDateSelected = { endDate = it },
                    onClearFilters = {
                        selectedPriority = null
                        startDate = null
                        endDate = null
                    },
                )
            }

            // Search Results
            when (val resultsLocal = searchResults) {
                is Result.Loading -> {
                    if (searchQuery.isNotEmpty() || selectedPriority != null || startDate != null) {
                        LoadingIndicator()
                    } else {
                        EmptyState(
                            title = "🔍 Search Your Events",
                            description = "Enter event title, description, location, or use filters to find your events quickly",
                        )
                    }
                }
                is Result.Success -> {
                    val data = resultsLocal.data
                    if (data.isEmpty() && (searchQuery.isNotEmpty() || selectedPriority != null || startDate != null)) {
                        EmptyState(
                            title = "No Events Found",
                            description = "Try adjusting your search criteria or filters",
                        )
                    } else if (searchQuery.isEmpty() && selectedPriority == null && startDate == null) {
                        EmptyState(
                            title = "🔍 Search Your Events",
                            description = "Enter event title, description, location, or use filters to find your events quickly",
                        )
                    } else {
                        LazyColumn(
                            modifier = Modifier.fillMaxSize(),
                            contentPadding = PaddingValues(12.dp),
                            verticalArrangement = Arrangement.spacedBy(6.dp),
                        ) {
                            items(data) { event ->
                                EventCard(
                                    title = event.title,
                                    time =
                                        if (event.isAllDay) {
                                            "All day"
                                        } else {
                                            "${event.startDateTime.format(
                                                DateTimeFormatter.ofPattern("MMM dd, HH:mm"),
                                            )} - ${event.endDateTime.format(DateTimeFormatter.ofPattern("HH:mm"))}"
                                        },
                                    description = event.description,
                                    color = com.moderncalendar.core.ui.utils.ColorUtils.parseColorSafely(event.color),
                                    isCompleted = event.isCompleted,
                                    onClick = { onEventClick(event.id) },
                                )
                            }
                        }
                    }
                }
                is Result.Error -> {
                    EmptyState(
                        title = "Search Error",
                        description = "Error: ${resultsLocal.exception.message}",
                    )
                }
            }
        }
    }
}

@Composable
private fun SearchFilters(
    selectedPriority: EventPriority?,
    onPrioritySelected: (EventPriority?) -> Unit,
    startDate: LocalDate?,
    onStartDateSelected: (LocalDate?) -> Unit,
    endDate: LocalDate?,
    onEndDateSelected: (LocalDate?) -> Unit,
    onClearFilters: () -> Unit,
    modifier: Modifier = Modifier,
) {
    Card(
        modifier =
            modifier
                .fillMaxWidth()
                .padding(horizontal = 16.dp),
        colors =
            CardDefaults.cardColors(
                containerColor = MaterialTheme.colorScheme.surface,
            ),
        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp),
    ) {
        Column(
            modifier = Modifier.padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(16.dp),
        ) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically,
            ) {
                Text(
                    text = "Filters",
                    style = MaterialTheme.typography.titleMedium,
                )

                TextButton(onClick = onClearFilters) {
                    Text("Clear All")
                }
            }

            // Priority Filter
            Text(
                text = "Priority",
                style = MaterialTheme.typography.bodyMedium,
                color = MaterialTheme.colorScheme.onSurface,
            )

            // First row: All, Low, Medium
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(8.dp),
            ) {
                FilterChip(
                    onClick = { onPrioritySelected(null) },
                    label = { Text("All") },
                    selected = selectedPriority == null,
                    modifier = Modifier.weight(1f),
                    colors =
                        FilterChipDefaults.filterChipColors(
                            containerColor = MaterialTheme.colorScheme.surfaceVariant,
                            selectedContainerColor = MaterialTheme.colorScheme.primary,
                            labelColor = MaterialTheme.colorScheme.onSurfaceVariant,
                            selectedLabelColor = MaterialTheme.colorScheme.onPrimary,
                        ),
                )

                FilterChip(
                    onClick = {
                        onPrioritySelected(if (selectedPriority == EventPriority.LOW) null else EventPriority.LOW)
                    },
                    label = { Text("Low") },
                    selected = selectedPriority == EventPriority.LOW,
                    modifier = Modifier.weight(1f),
                    colors =
                        FilterChipDefaults.filterChipColors(
                            containerColor = MaterialTheme.colorScheme.surfaceVariant,
                            selectedContainerColor = MaterialTheme.colorScheme.primary,
                            labelColor = MaterialTheme.colorScheme.onSurfaceVariant,
                            selectedLabelColor = MaterialTheme.colorScheme.onPrimary,
                        ),
                )

                FilterChip(
                    onClick = {
                        onPrioritySelected(if (selectedPriority == EventPriority.MEDIUM) null else EventPriority.MEDIUM)
                    },
                    label = { Text("Medium") },
                    selected = selectedPriority == EventPriority.MEDIUM,
                    modifier = Modifier.weight(1f),
                    colors =
                        FilterChipDefaults.filterChipColors(
                            containerColor = MaterialTheme.colorScheme.surfaceVariant,
                            selectedContainerColor = MaterialTheme.colorScheme.primary,
                            labelColor = MaterialTheme.colorScheme.onSurfaceVariant,
                            selectedLabelColor = MaterialTheme.colorScheme.onPrimary,
                        ),
                )
            }

            Spacer(modifier = Modifier.height(4.dp))

            // Second row: High, Urgent
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(8.dp),
            ) {
                FilterChip(
                    onClick = {
                        onPrioritySelected(if (selectedPriority == EventPriority.HIGH) null else EventPriority.HIGH)
                    },
                    label = { Text("High") },
                    selected = selectedPriority == EventPriority.HIGH,
                    modifier = Modifier.weight(1f),
                    colors =
                        FilterChipDefaults.filterChipColors(
                            containerColor = MaterialTheme.colorScheme.surfaceVariant,
                            selectedContainerColor = MaterialTheme.colorScheme.primary,
                            labelColor = MaterialTheme.colorScheme.onSurfaceVariant,
                            selectedLabelColor = MaterialTheme.colorScheme.onPrimary,
                        ),
                )

                FilterChip(
                    onClick = {
                        onPrioritySelected(if (selectedPriority == EventPriority.URGENT) null else EventPriority.URGENT)
                    },
                    label = { Text("Urgent") },
                    selected = selectedPriority == EventPriority.URGENT,
                    modifier = Modifier.weight(1f),
                    colors =
                        FilterChipDefaults.filterChipColors(
                            containerColor = MaterialTheme.colorScheme.surfaceVariant,
                            selectedContainerColor = MaterialTheme.colorScheme.primary,
                            labelColor = MaterialTheme.colorScheme.onSurfaceVariant,
                            selectedLabelColor = MaterialTheme.colorScheme.onPrimary,
                        ),
                )

                // Empty space to balance the row
                Spacer(modifier = Modifier.weight(1f))
            }

            // Date Range Filter
            Text(
                text = "Date Range",
                style = MaterialTheme.typography.bodyMedium,
            )

            DateRangeField(
                startDate = startDate,
                endDate = endDate,
                onStartDateSelected = { onStartDateSelected(it) },
                onEndDateSelected = { onEndDateSelected(it) },
                modifier = Modifier.fillMaxWidth(),
            )
        }
    }
}
