package com.moderncalendar.widget

import android.app.PendingIntent
import android.appwidget.AppWidgetManager
import android.appwidget.AppWidgetProvider
import android.content.Context
import android.content.Intent
import android.widget.RemoteViews
import com.moderncalendar.MainActivity
import com.moderncalendar.R
import com.moderncalendar.core.common.model.Event
import com.moderncalendar.core.data.repository.EventRepository
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.firstOrNull
import kotlinx.coroutines.launch
import java.time.LocalDate
import com.moderncalendar.core.common.Result
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
                val dateFmt = DateTimeFormatter.ofPattern("MMM dd")
                val timeFmt = DateTimeFormatter.ofPattern("HH:mm")
                val start = today.atStartOfDay()
                val end = start.plusDays(1)
                val result: Result<List<Event>>? = eventRepository.getEventsByDateRange(start, end).firstOrNull()
                val events: List<Event> = if (result is Result.Success<List<Event>>) result.data else emptyList()
                
                // Update widget with events
                val eventText = if (events.isNotEmpty()) {
                    events.take(3).joinToString("\n") { event ->
                        val time = if (event.isAllDay) {
                            "All day"
                        } else {
                            event.startDateTime.format(timeFmt)
                        }
                        "$time - ${event.title}"
                    }
                } else {
                    "No events today"
                }
                
                views.setTextViewText(R.id.widget_events, eventText)
                views.setTextViewText(R.id.widget_date, today.format(dateFmt))
                
                appWidgetManager.updateAppWidget(appWidgetId, views)
            } catch (e: Exception) {
                views.setTextViewText(R.id.widget_events, "Error")
                appWidgetManager.updateAppWidget(appWidgetId, views)
            }
        }
    }
}
