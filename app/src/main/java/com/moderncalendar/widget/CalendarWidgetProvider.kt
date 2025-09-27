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
class CalendarWidgetProvider : AppWidgetProvider() {
    
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
        val views = RemoteViews(context.packageName, R.layout.widget_calendar)
        
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
                    events.take(3).joinToString("\n") { event ->
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
                
                views.setTextViewText(R.id.widget_events, eventText)
                views.setTextViewText(R.id.widget_date, today.format(DateTimeFormatter.ofPattern("MMM dd")))
                
                appWidgetManager.updateAppWidget(appWidgetId, views)
            } catch (e: Exception) {
                views.setTextViewText(R.id.widget_events, "Error loading events")
                appWidgetManager.updateAppWidget(appWidgetId, views)
            }
        }
    }
}
