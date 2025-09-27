package com.moderncalendar.core.reminders

import android.content.Context
import androidx.work.*
import com.moderncalendar.core.data.entity.EventEntity
import com.moderncalendar.core.common.DateTimeUtils
import java.time.Duration
import java.util.concurrent.TimeUnit
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class ReminderManager @Inject constructor(
    private val context: Context
) {
    
    private val workManager = WorkManager.getInstance(context)
    
    fun scheduleEventReminder(event: EventEntity) {
        // Cancel any existing reminders for this event
        cancelEventReminders(event.id)
        
        // Schedule reminders for each reminder time
        event.reminderMinutes.forEach { minutesBefore ->
            val reminderTime = event.startDateTime.minusMinutes(minutesBefore.toLong())
            val delay = Duration.between(
                java.time.LocalDateTime.now(),
                reminderTime
            ).toMinutes()
            
            if (delay > 0) {
                val workRequest = OneTimeWorkRequestBuilder<ReminderWorker>()
                    .setInputData(createInputData(event))
                    .setInitialDelay(delay, TimeUnit.MINUTES)
                    .addTag("event_${event.id}")
                    .addTag("reminder_${minutesBefore}min")
                    .build()
                
                workManager.enqueue(workRequest)
            }
        }
    }
    
    fun cancelEventReminders(eventId: String) {
        workManager.cancelAllWorkByTag("event_$eventId")
    }
    
    fun cancelAllReminders() {
        workManager.cancelAllWork()
    }
    
    fun rescheduleEventReminder(event: EventEntity) {
        cancelEventReminders(event.id)
        scheduleEventReminder(event)
    }
    
    private fun createInputData(event: EventEntity): Data {
        val timeString = if (event.isAllDay) {
            "All day"
        } else {
            "${event.startDateTime.hour}:${event.startDateTime.minute.toString().padStart(2, '0')}"
        }
        
        return Data.Builder()
            .putString(ReminderWorker.KEY_EVENT_TITLE, event.title)
            .putString(ReminderWorker.KEY_EVENT_DESCRIPTION, event.description)
            .putString(ReminderWorker.KEY_EVENT_TIME, timeString)
            .putString(ReminderWorker.KEY_EVENT_LOCATION, event.location)
            .build()
    }
    
    fun getScheduledReminders(): List<WorkInfo> {
        return workManager.getWorkInfosByTag("event_reminder").get()
    }
}
