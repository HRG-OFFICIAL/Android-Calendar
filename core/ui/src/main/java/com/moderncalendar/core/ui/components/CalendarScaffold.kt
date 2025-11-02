package com.moderncalendar.core.ui.components

import androidx.compose.foundation.layout.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.navigation.NavController

/**
 * Main scaffold component that provides consistent layout structure
 */

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CalendarScaffold(
    navController: NavController,
    title: String,
    showFab: Boolean = false,
    fabIcon: ImageVector = Icons.Default.Add,
    fabContentDescription: String = "Add",
    onFabClick: () -> Unit = {},
    navigationIcon: ImageVector? = null,
    onNavigationClick: (() -> Unit)? = null,
    topBarActions: @Composable () -> Unit = {},
    content: @Composable (PaddingValues) -> Unit
) {
    Scaffold(
        modifier = Modifier.fillMaxSize(),
        topBar = {
            CalendarTopAppBar(
                title = title,
                navigationIcon = navigationIcon,
                onNavigationClick = onNavigationClick,
                actions = topBarActions
            )
        },
        floatingActionButton = {
            if (showFab) {
                ModernFloatingActionButton(
                    onClick = onFabClick,
                    icon = fabIcon,
                    contentDescription = fabContentDescription
                )
            }
        },
        containerColor = MaterialTheme.colorScheme.background,
        content = content
    )
}