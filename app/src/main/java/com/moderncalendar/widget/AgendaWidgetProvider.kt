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
                val dateFmt = DateTimeFormatter.ofPattern("MMM dd")
                val timeFmt = DateTimeFormatter.ofPattern("HH:mm")
                val start = today.atStartOfDay()
                val end = start.plusDays(1)
                val result: Result<List<Event>>? = eventRepository.getEventsByDateRange(start, end).firstOrNull()
                val events: List<Event> = if (result is Result.Success<List<Event>>) result.data else emptyList()
                
                // Update widget with events
                val eventText = if (events.isNotEmpty()) {
                    events.take(5).joinToString("\n") { event ->
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
                
                views.setTextViewText(R.id.widget_date, today.format(dateFmt))
                
                // Set up remote views service for list
                val serviceIntent = Intent(context, AgendaWidgetService::class.java)
                serviceIntent.putExtra(AppWidgetManager.EXTRA_APPWIDGET_ID, appWidgetId)
                views.setRemoteAdapter(R.id.widget_agenda_list, serviceIntent)
                
                appWidgetManager.notifyAppWidgetViewDataChanged(appWidgetId, R.id.widget_agenda_list)
                appWidgetManager.updateAppWidget(appWidgetId, views)
            } catch (e: Exception) {
                views.setTextViewText(R.id.widget_date, "Error")
                appWidgetManager.updateAppWidget(appWidgetId, views)
            }
        }
    }
}
