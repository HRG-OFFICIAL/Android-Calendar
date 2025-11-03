package com.moderncalendar.widget

import android.content.Intent
import android.widget.RemoteViews
import android.widget.RemoteViewsService
import com.moderncalendar.R
import com.moderncalendar.core.data.entity.EventEntity
import com.moderncalendar.core.common.repository.EventRepository
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.runBlocking
import java.time.LocalDate
import java.time.format.DateTimeFormatter
import javax.inject.Inject

@AndroidEntryPoint
class WeekWidgetService : RemoteViewsService() {
    
    @Inject
    lateinit var eventRepository: EventRepository
    
    override fun onGetViewFactory(intent: Intent): RemoteViewsFactory {
        return WeekRemoteViewsFactory(applicationContext, eventRepository)
    }
}

class WeekRemoteViewsFactory(
    private val context: android.content.Context,
    private val eventRepository: EventRepository
) : RemoteViewsService.RemoteViewsFactory {
    
    private var weekDays: List<LocalDate> = emptyList()
    
    override fun onCreate() {
        // Initialize
    }
    
    override fun onDataSetChanged() {
        runBlocking {
            try {
                val today = LocalDate.now()
                val weekStart = today.minusDays(today.dayOfWeek.value - 1L)
                weekDays = (0..6).map { weekStart.plusDays(it.toLong()) }
            } catch (e: Exception) {
                weekDays = emptyList()
            }
        }
    }
    
    override fun onDestroy() {
        // Cleanup
    }
    
    override fun getCount(): Int = weekDays.size
    
    override fun getViewAt(position: Int): RemoteViews {
        val day = weekDays[position]
        val views = RemoteViews(context.packageName, R.layout.widget_week_item)
        
        views.setTextViewText(R.id.widget_day_name, day.format(DateTimeFormatter.ofPattern("E")))
        views.setTextViewText(R.id.widget_day_number, day.format(DateTimeFormatter.ofPattern("d")))
        
        // Check if today
        val isToday = day == LocalDate.now()
        views.setTextColor(R.id.widget_day_number, if (isToday) 0xFF4CAF50.toInt() else 0xFFFFFFFF.toInt())
        
        return views
    }
    
    override fun getLoadingView(): RemoteViews? = null
    
    override fun getViewTypeCount(): Int = 1
    
    override fun getItemId(position: Int): Long = position.toLong()
    
    override fun hasStableIds(): Boolean = true
}
