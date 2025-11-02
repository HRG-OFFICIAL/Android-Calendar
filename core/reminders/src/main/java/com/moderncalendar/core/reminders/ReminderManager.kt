package com.moderncalendar.core.reminders

import android.app.AlarmManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.os.Build
import androidx.core.app.NotificationCompat
import androidx.core.app.NotificationManagerCompat
import com.moderncalendar.core.data.entity.EventEntity
import com.moderncalendar.core.data.entity.EventPriority
import java.time.LocalDateTime
import java.time.ZoneId
import java.util.concurrent.TimeUnit
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class ReminderManager @Inject constructor(
    private val context: Context,
    private val alarmManager: AlarmManager
) {
    
    companion object {
        private const val NOTIFICATION_CHANNEL_ID = "event_reminders"
        private const val NOTIFICATION_CHANNEL_NAME = "Event Reminders"
        private const val NOTIFICATION_ID_BASE = 1000
        
        // Intent actions
        const val ACTION_EVENT_REMINDER = "com.moderncalendar.ACTION_EVENT_REMINDER"
        const val ACTION_SNOOZE_REMINDER = "com.moderncalendar.ACTION_SNOOZE_REMINDER"
        const val ACTION_DISMISS_REMINDER = "com.moderncalendar.ACTION_DISMISS_REMINDER"
        
        // Intent extras
        const val EXTRA_EVENT_ID = "event_id"
        const val EXTRA_EVENT_TITLE = "event_title"
        const val EXTRA_EVENT_DESCRIPTION = "event_description"
        const val EXTRA_EVENT_LOCATION = "event_location"
        const val EXTRA_EVENT_START_TIME = "event_start_time"
        const val EXTRA_SNOOZE_MINUTES = "snooze_minutes"
    }
    
    fun scheduleEventReminder(event: EventEntity) {
        if (event.reminderMinutes.isEmpty()) return
        
        val eventStartTime = event.startDateTime.atZone(ZoneId.systemDefault()).toInstant().toEpochMilli()
        
        event.reminderMinutes.forEach { minutesBefore ->
            val reminderTime = eventStartTime - TimeUnit.MINUTES.toMillis(minutesBefore.toLong())
            
            // Don't schedule reminders for past events
            if (reminderTime <= System.currentTimeMillis()) return@forEach
            
            val intent = createReminderIntent(event, minutesBefore)
            val pendingIntent = PendingIntent.getBroadcast(
                context,
                getReminderRequestCode(event.id, minutesBefore),
                intent,
                PendingIntent.FLAG_UPDATE_CURRENT or PendingIntent.FLAG_IMMUTABLE
            )
            
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                alarmManager.setExactAndAllowWhileIdle(
                    AlarmManager.RTC_WAKEUP,
                    reminderTime,
                    pendingIntent
                )
            } else {
                alarmManager.setExact(
                    AlarmManager.RTC_WAKEUP,
                    reminderTime,
                    pendingIntent
                )
            }
        }
    }
    
    fun cancelEventReminder(event: EventEntity) {
        event.reminderMinutes.forEach { minutesBefore ->
            val intent = createReminderIntent(event, minutesBefore)
            val pendingIntent = PendingIntent.getBroadcast(
                context,
                getReminderRequestCode(event.id, minutesBefore),
                intent,
                PendingIntent.FLAG_UPDATE_CURRENT or PendingIntent.FLAG_IMMUTABLE
            )
            alarmManager.cancel(pendingIntent)
        }
    }
    
    fun rescheduleEventReminder(event: EventEntity) {
        cancelEventReminder(event)
        scheduleEventReminder(event)
    }
    
    fun scheduleSnoozeReminder(
        eventId: String,
        eventTitle: String,
        snoozeMinutes: Int
    ) {
        val snoozeTime = System.currentTimeMillis() + TimeUnit.MINUTES.toMillis(snoozeMinutes.toLong())
        
        val intent = Intent().apply {
            action = ACTION_SNOOZE_REMINDER
            putExtra(EXTRA_EVENT_ID, eventId)
            putExtra(EXTRA_EVENT_TITLE, eventTitle)
            putExtra(EXTRA_SNOOZE_MINUTES, snoozeMinutes)
        }
        
        val pendingIntent = PendingIntent.getBroadcast(
            context,
            getSnoozeRequestCode(eventId, snoozeMinutes),
            intent,
            PendingIntent.FLAG_UPDATE_CURRENT or PendingIntent.FLAG_IMMUTABLE
        )
        
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            alarmManager.setExactAndAllowWhileIdle(
                AlarmManager.RTC_WAKEUP,
                snoozeTime,
                pendingIntent
            )
        } else {
            alarmManager.setExact(
                AlarmManager.RTC_WAKEUP,
                snoozeTime,
                pendingIntent
            )
        }
    }
    
    fun showReminderNotification(event: EventEntity, minutesBefore: Int) {
        val notificationManager = NotificationManagerCompat.from(context)
        
        val notification = NotificationCompat.Builder(context, NOTIFICATION_CHANNEL_ID)
            .setSmallIcon(android.R.drawable.ic_dialog_info)
            .setContentTitle("Event Reminder")
            .setContentText("${event.title} in $minutesBefore minutes")
            .setStyle(
                NotificationCompat.BigTextStyle()
                    .bigText("${event.title}\n${event.description ?: ""}\n${event.location ?: ""}")
            )
            .setPriority(getNotificationPriority(event.priority))
            .setCategory(NotificationCompat.CATEGORY_REMINDER)
            .setAutoCancel(true)
            .setContentIntent(createEventDetailsIntent(event))
            .addAction(
                android.R.drawable.ic_menu_recent_history,
                "Snooze 15min",
                createSnoozeIntent(event, 15)
            )
            .addAction(
                android.R.drawable.ic_menu_close_clear_cancel,
                "Dismiss",
                createDismissIntent(event)
            )
            .build()
        
        notificationManager.notify(
            getNotificationId(event.id, minutesBefore),
            notification
        )
    }
    
    fun cancelAllReminders() {
        // This would typically cancel all pending reminders
        // Implementation depends on how you want to track active reminders
    }
    
    private fun createReminderIntent(event: EventEntity, minutesBefore: Int): Intent {
        return Intent().apply {
            action = ACTION_EVENT_REMINDER
            putExtra(EXTRA_EVENT_ID, event.id)
            putExtra(EXTRA_EVENT_TITLE, event.title)
            putExtra(EXTRA_EVENT_DESCRIPTION, event.description)
            putExtra(EXTRA_EVENT_LOCATION, event.location)
            putExtra(EXTRA_EVENT_START_TIME, event.startDateTime.toString())
            putExtra(EXTRA_SNOOZE_MINUTES, minutesBefore)
        }
    }
    
    private fun createEventDetailsIntent(event: EventEntity): PendingIntent {
        val intent = Intent().apply {
            putExtra("event_id", event.id)
            flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TOP
        }
        
        return PendingIntent.getActivity(
            context,
            event.id.hashCode(),
            intent,
            PendingIntent.FLAG_UPDATE_CURRENT or PendingIntent.FLAG_IMMUTABLE
        )
    }
    
    private fun createSnoozeIntent(event: EventEntity, snoozeMinutes: Int): PendingIntent {
        val intent = Intent().apply {
            action = ACTION_SNOOZE_REMINDER
            putExtra(EXTRA_EVENT_ID, event.id)
            putExtra(EXTRA_EVENT_TITLE, event.title)
            putExtra(EXTRA_SNOOZE_MINUTES, snoozeMinutes)
        }
        
        return PendingIntent.getBroadcast(
            context,
            getSnoozeRequestCode(event.id, snoozeMinutes),
            intent,
            PendingIntent.FLAG_UPDATE_CURRENT or PendingIntent.FLAG_IMMUTABLE
        )
    }
    
    private fun createDismissIntent(event: EventEntity): PendingIntent {
        val intent = Intent().apply {
            action = ACTION_DISMISS_REMINDER
            putExtra(EXTRA_EVENT_ID, event.id)
        }
        
        return PendingIntent.getBroadcast(
            context,
            event.id.hashCode(),
            intent,
            PendingIntent.FLAG_UPDATE_CURRENT or PendingIntent.FLAG_IMMUTABLE
        )
    }
    
    private fun getReminderRequestCode(eventId: String, minutesBefore: Int): Int {
        return eventId.hashCode() + minutesBefore
    }
    
    private fun getSnoozeRequestCode(eventId: String, snoozeMinutes: Int): Int {
        return eventId.hashCode() + snoozeMinutes + 10000
    }
    
    private fun getNotificationId(eventId: String, minutesBefore: Int): Int {
        return NOTIFICATION_ID_BASE + eventId.hashCode() + minutesBefore
    }
    
    private fun getNotificationPriority(priority: EventPriority): Int {
        return when (priority) {
            EventPriority.LOW -> NotificationCompat.PRIORITY_LOW
            EventPriority.MEDIUM -> NotificationCompat.PRIORITY_DEFAULT
            EventPriority.HIGH -> NotificationCompat.PRIORITY_HIGH
            EventPriority.URGENT -> NotificationCompat.PRIORITY_MAX
        }
    }
}