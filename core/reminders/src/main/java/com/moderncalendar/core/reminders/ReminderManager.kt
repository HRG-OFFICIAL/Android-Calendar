package com.moderncalendar.core.reminders

import android.app.AlarmManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import com.moderncalendar.core.data.entity.EventEntity
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import java.time.LocalDateTime
import java.time.ZoneId
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class ReminderManager @Inject constructor(
    @ApplicationContext private val context: Context,
    private val alarmManager: AlarmManager
) {
    
    fun scheduleReminder(event: EventEntity) {
        val reminderMinutes = event.reminderMinutes ?: return
        
        val reminderTime = event.startDateTime.minusMinutes(reminderMinutes.toLong())
        if (reminderTime.isAfter(LocalDateTime.now())) {
            scheduleAlarm(event.id, reminderTime, event.title)
        }
    }
    
    fun scheduleReminderLater(eventId: String, delayMinutes: Int) {
        val reminderTime = LocalDateTime.now().plusMinutes(delayMinutes.toLong())
        scheduleAlarm(eventId, reminderTime, "Event Reminder")
    }
    
    fun cancelReminder(eventId: String) {
        val intent = Intent("com.moderncalendar.REMINDER_ACTION").apply {
            putExtra("event_id", eventId)
        }
        val pendingIntent = PendingIntent.getBroadcast(
            context,
            eventId.hashCode(),
            intent,
            PendingIntent.FLAG_UPDATE_CURRENT or PendingIntent.FLAG_IMMUTABLE
        )
        alarmManager.cancel(pendingIntent)
    }
    
    fun cancelAllReminders() {
        // This is a placeholder implementation
        // In a real implementation, you would need to track all scheduled reminders
        // and cancel them individually. For now, we'll just log it.
        android.util.Log.d("ReminderManager", "Cancelling all reminders")
        // TODO: Implement proper tracking and cancellation of all reminders
    }

    // Public forwarder for downstream modules to avoid any visibility/tooling ambiguity
    fun cancelAllPublic() = cancelAllReminders()
    
    fun rescheduleAllReminders() {
        CoroutineScope(Dispatchers.IO).launch {
            try {
                // Cancel all existing reminders first
                cancelAllReminders()
                
                // Load all events from database and reschedule their reminders
                // This would typically use EventRepository to get all events
                // For now, we'll implement a placeholder that can be extended
                // TODO: Inject EventRepository and load all events
                // val events = eventRepository.getAllEvents()
                // events.forEach { event ->
                //     if (event.reminderMinutes.isNotEmpty()) {
                //         scheduleReminder(event)
                //     }
                // }
            } catch (e: Exception) {
                // Log error - in production, use proper logging
                android.util.Log.e("ReminderManager", "Error rescheduling reminders", e)
            }
        }
    }
    
    fun updateReminderLocales() {
        CoroutineScope(Dispatchers.IO).launch {
            try {
                // Update notification channels and existing notifications
                // with new locale settings
                // This would typically involve:
                // 1. Updating notification channels with new locale
                // 2. Recreating existing notifications with new text
                // 3. Updating any cached reminder text
                
                // For now, we'll implement a placeholder
                // TODO: Implement locale-specific reminder updates
            } catch (e: Exception) {
                // Log error - in production, use proper logging
                android.util.Log.e("ReminderManager", "Error updating reminder locales", e)
            }
        }
    }
    
    
    private fun scheduleAlarm(eventId: String, reminderTime: LocalDateTime, eventTitle: String) {
        val intent = Intent("com.moderncalendar.REMINDER_ACTION").apply {
            putExtra("event_id", eventId)
            putExtra("event_title", eventTitle)
        }

        val pendingIntent = PendingIntent.getBroadcast(
            context,
            eventId.hashCode(),
            intent,
            PendingIntent.FLAG_UPDATE_CURRENT or PendingIntent.FLAG_IMMUTABLE
        )

        val triggerTime = reminderTime.atZone(ZoneId.systemDefault()).toInstant().toEpochMilli()

        alarmManager.setExactAndAllowWhileIdle(
            AlarmManager.RTC_WAKEUP,
            triggerTime,
            pendingIntent
        )
    }
}