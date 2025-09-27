package com.moderncalendar.widget

import android.app.PendingIntent
import android.appwidget.AppWidgetManager
import android.appwidget.AppWidgetProvider
import android.content.Context
import android.content.Intent
import android.widget.RemoteViews
import com.moderncalendar.MainActivity
import com.moderncalendar.R
import com.moderncalendar.core.data.repository.EventRepository
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import java.time.LocalDate
import java.time.format.DateTimeFormatter
import javax.inject.Inject

@AndroidEntryPoint
class WeekWidgetProvider : AppWidgetProvider() {
    
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
        val views = RemoteViews(context.packageName, R.layout.widget_week)
        
        // Set up click intent
        val intent = Intent(context, MainActivity::class.java)
        val pendingIntent = PendingIntent.getActivity(
            context, 0, intent,
            PendingIntent.FLAG_UPDATE_CURRENT or PendingIntent.FLAG_IMMUTABLE
        )
        views.setOnClickPendingIntent(R.id.widget_container, pendingIntent)
        
        // Load week's events
        CoroutineScope(Dispatchers.IO).launch {
            try {
                val today = LocalDate.now()
                val weekStart = today.minusDays(today.dayOfWeek.value - 1L)
                val weekEnd = weekStart.plusDays(6)
                
                views.setTextViewText(
                    R.id.widget_week_range,
                    "${weekStart.format(DateTimeFormatter.ofPattern("MMM dd"))} - ${weekEnd.format(DateTimeFormatter.ofPattern("MMM dd"))}"
                )
                
                // Set up remote views service for week grid
                val serviceIntent = Intent(context, WeekWidgetService::class.java)
                serviceIntent.putExtra(AppWidgetManager.EXTRA_APPWIDGET_ID, appWidgetId)
                views.setRemoteAdapter(R.id.widget_week_grid, serviceIntent)
                
                appWidgetManager.updateAppWidget(appWidgetId, views)
            } catch (e: Exception) {
                views.setTextViewText(R.id.widget_week_range, "Error loading week")
                appWidgetManager.updateAppWidget(appWidgetId, views)
            }
        }
    }
}
