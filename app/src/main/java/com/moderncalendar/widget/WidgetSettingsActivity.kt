package com.moderncalendar.widget

import android.appwidget.AppWidgetManager
import android.content.Intent
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.*
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.moderncalendar.ui.theme.ModernCalendarTheme
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class WidgetSettingsActivity : ComponentActivity() {
    
    private var appWidgetId = AppWidgetManager.INVALID_APPWIDGET_ID
    
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        
        // Get the widget ID from the intent
        appWidgetId = intent.extras?.getInt(
            AppWidgetManager.EXTRA_APPWIDGET_ID,
            AppWidgetManager.INVALID_APPWIDGET_ID
        ) ?: AppWidgetManager.INVALID_APPWIDGET_ID
        
        setContent {
            ModernCalendarTheme {
                WidgetSettingsScreen(
                    appWidgetId = appWidgetId,
                    onSave = { saveWidgetSettings() },
                    onCancel = { cancelWidgetCreation() }
                )
            }
        }
    }
    
    private fun saveWidgetSettings() {
        val resultValue = Intent().apply {
            putExtra(AppWidgetManager.EXTRA_APPWIDGET_ID, appWidgetId)
        }
        setResult(RESULT_OK, resultValue)
        finish()
    }
    
    private fun cancelWidgetCreation() {
        setResult(RESULT_CANCELED)
        finish()
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun WidgetSettingsScreen(
    appWidgetId: Int,
    onSave: () -> Unit,
    onCancel: () -> Unit
) {
    var selectedWidgetType by remember { mutableStateOf("Calendar") }
    var showWeekends by remember { mutableStateOf(true) }
    var showEvents by remember { mutableStateOf(true) }
    var widgetSize by remember { mutableStateOf("Medium") }
    
    val widgetTypes = listOf("Calendar", "Agenda", "Week View")
    val widgetSizes = listOf("Small", "Medium", "Large")
    
    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Widget Settings") },
                navigationIcon = {
                    TextButton(onClick = onCancel) {
                        Text("Cancel")
                    }
                },
                actions = {
                    TextButton(onClick = onSave) {
                        Text("Save")
                    }
                }
            )
        }
    ) { paddingValues ->
        LazyColumn(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
                .padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            item {
                Text(
                    text = "Configure Widget",
                    style = MaterialTheme.typography.titleLarge
                )
            }
            
            item {
                Card {
                    Column(
                        modifier = Modifier.padding(16.dp),
                        verticalArrangement = Arrangement.spacedBy(16.dp)
                    ) {
                        Text(
                            text = "Widget Type",
                            style = MaterialTheme.typography.titleMedium
                        )
                        
                        widgetTypes.forEach { type ->
                            Row(
                                modifier = Modifier.fillMaxWidth(),
                                horizontalArrangement = Arrangement.SpaceBetween,
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                Text(type)
                                RadioButton(
                                    selected = selectedWidgetType == type,
                                    onClick = { selectedWidgetType = type }
                                )
                            }
                        }
                    }
                }
            }
            
            item {
                Card {
                    Column(
                        modifier = Modifier.padding(16.dp),
                        verticalArrangement = Arrangement.spacedBy(16.dp)
                    ) {
                        Text(
                            text = "Display Options",
                            style = MaterialTheme.typography.titleMedium
                        )
                        
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.SpaceBetween,
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Text("Show Weekends")
                            Switch(
                                checked = showWeekends,
                                onCheckedChange = { showWeekends = it }
                            )
                        }
                        
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.SpaceBetween,
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Text("Show Events")
                            Switch(
                                checked = showEvents,
                                onCheckedChange = { showEvents = it }
                            )
                        }
                    }
                }
            }
            
            item {
                Card {
                    Column(
                        modifier = Modifier.padding(16.dp),
                        verticalArrangement = Arrangement.spacedBy(16.dp)
                    ) {
                        Text(
                            text = "Widget Size",
                            style = MaterialTheme.typography.titleMedium
                        )
                        
                        widgetSizes.forEach { size ->
                            Row(
                                modifier = Modifier.fillMaxWidth(),
                                horizontalArrangement = Arrangement.SpaceBetween,
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                Text(size)
                                RadioButton(
                                    selected = widgetSize == size,
                                    onClick = { widgetSize = size }
                                )
                            }
                        }
                    }
                }
            }
        }
    }
}
