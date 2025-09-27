package com.moderncalendar.widget

import android.app.PendingIntent
import android.appwidget.AppWidgetManager
import android.appwidget.AppWidgetProvider
import android.content.Context
import android.content.Intent
import android.widget.RemoteViews
import com.moderncalendar.MainActivity
import com.moderncalendar.R
import com.moderncalendar.core.data.entity.EventEntity
import com.moderncalendar.core.data.repository.EventRepository
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import java.time.LocalDate
import java.time.format.DateTimeFormatter
import javax.inject.Inject

@AndroidEntryPoint
class AgendaWidgetProvider : AppWidgetProvider() {
    
    @Inject
    lateinit var eventRepository: EventRepository
    
    override fun onUpdate(
        context: Context,
        appWidgetManager: AppWidgetManager,
        appWidgetIds: IntArray
    ) {
        for (appWidgetId in appWidgetIds) {
            updateAppWidget(context, appWidgetManager, appWidgetId)
        }
    }
    
    private fun updateAppWidget(
        context: Context,
        appWidgetManager: AppWidgetManager,
        appWidgetId: Int
    ) {
        val views = RemoteViews(context.packageName, R.layout.widget_agenda)
        
        // Set up click intent
        val intent = Intent(context, MainActivity::class.java)
        val pendingIntent = PendingIntent.getActivity(
            context, 0, intent,
            PendingIntent.FLAG_UPDATE_CURRENT or PendingIntent.FLAG_IMMUTABLE
        )
        views.setOnClickPendingIntent(R.id.widget_container, pendingIntent)
        
        // Load today's events
        CoroutineScope(Dispatchers.IO).launch {
            try {
                val today = LocalDate.now()
                val events = eventRepository.getEventsForDate(today)
                
                // Update widget with events
                val eventText = if (events.isNotEmpty()) {
                    events.take(5).joinToString("\n") { event ->
                        val time = if (event.isAllDay) {
                            "All day"
                        } else {
                            event.startDateTime.format(DateTimeFormatter.ofPattern("HH:mm"))
                        }
                        "$time - ${event.title}"
                    }
                } else {
                    "No events today"
                }
                
                views.setTextViewText(R.id.widget_date, today.format(DateTimeFormatter.ofPattern("MMM dd")))
                
                // Set up remote views service for list
                val serviceIntent = Intent(context, AgendaWidgetService::class.java)
                serviceIntent.putExtra(AppWidgetManager.EXTRA_APPWIDGET_ID, appWidgetId)
                views.setRemoteAdapter(R.id.widget_agenda_list, serviceIntent)
                
                appWidgetManager.updateAppWidget(appWidgetId, views)
            } catch (e: Exception) {
                views.setTextViewText(R.id.widget_date, "Error loading events")
                appWidgetManager.updateAppWidget(appWidgetId, views)
            }
        }
    }
}
