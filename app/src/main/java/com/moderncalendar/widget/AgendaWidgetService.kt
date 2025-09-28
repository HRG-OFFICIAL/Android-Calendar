package com.moderncalendar.widget

import android.content.Intent
import android.widget.RemoteViews
import android.widget.RemoteViewsService
import com.moderncalendar.R
import com.moderncalendar.core.data.entity.EventEntity
import com.moderncalendar.core.data.repository.EventRepository
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.firstOrNull
import kotlinx.coroutines.runBlocking
import java.time.format.DateTimeFormatter
import com.moderncalendar.core.common.Result
import javax.inject.Inject

@AndroidEntryPoint
class AgendaWidgetService : RemoteViewsService() {
    
    @Inject
    lateinit var eventRepository: EventRepository
    
    override fun onGetViewFactory(intent: Intent): RemoteViewsFactory {
        return AgendaRemoteViewsFactory(applicationContext, eventRepository)
    }
}

class AgendaRemoteViewsFactory(
    private val context: android.content.Context,
    private val eventRepository: EventRepository
) : RemoteViewsService.RemoteViewsFactory {
    
    private var events: List<EventEntity> = emptyList()
    private var currentDate = java.time.LocalDate.now()
    
    override fun onCreate() {
        // Initialize
    }
    
    override fun onDataSetChanged() {
        runBlocking {
            try {
                currentDate = java.time.LocalDate.now()
                val start = currentDate.atStartOfDay()
                val end = start.plusDays(1)
                val result: Result<List<EventEntity>>? = eventRepository.getEventsByDateRange(start, end).firstOrNull()
                events = if (result is Result.Success<List<EventEntity>>) result.data else emptyList()
            } catch (e: Exception) {
                events = emptyList()
            }
        }
    }
    
    override fun onDestroy() {
        // Cleanup
    }
    
    override fun getCount(): Int = events.size
    
    override fun getViewAt(position: Int): RemoteViews {
        val event = events[position]
        val views = RemoteViews(context.packageName, R.layout.widget_agenda_item)
        
        val time = if (event.isAllDay) {
            "All day"
        } else {
            event.startDateTime.format(DateTimeFormatter.ofPattern("HH:mm"))
        }
        
        views.setTextViewText(R.id.widget_event_time, time)
        views.setTextViewText(R.id.widget_event_title, event.title)
        views.setTextViewText(R.id.widget_event_location, event.location ?: "")
        
        return views
    }
    
    override fun getLoadingView(): RemoteViews? = null
    
    override fun getViewTypeCount(): Int = 1
    
    override fun getItemId(position: Int): Long = position.toLong()
    
    override fun hasStableIds(): Boolean = true
}
