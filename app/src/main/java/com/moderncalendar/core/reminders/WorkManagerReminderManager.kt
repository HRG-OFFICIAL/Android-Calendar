package com.moderncalendar.core.reminders

import android.content.Context
import androidx.work.*
import com.moderncalendar.core.data.entity.EventEntity
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import java.time.Duration
import java.time.LocalDateTime
import java.time.ZoneId
import java.util.concurrent.TimeUnit
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class WorkManagerReminderManager @Inject constructor(
    @ApplicationContext private val context: Context,
    private val workManager: WorkManager
) {
    
    private val coroutineScope = CoroutineScope(Dispatchers.IO)
    
    fun scheduleReminder(event: EventEntity) {
        val reminderMinutes = event.reminderMinutes
        if (reminderMinutes == null) return
        
        val reminderTime = event.startDateTime.minusMinutes(reminderMinutes.toLong())
        if (reminderTime.isAfter(LocalDateTime.now())) {
            scheduleReminderWork(
                eventId = event.id,
                eventTitle = event.title,
                eventDescription = event.description,
                eventLocation = event.location,
                reminderMinutes = reminderMinutes,
                reminderTime = reminderTime
            )
        }
    }
    
    fun cancelReminder(eventId: String) {
        workManager.cancelAllWorkByTag(eventId)
    }
    
    fun rescheduleAllReminders() {
        coroutineScope.launch {
            try {
                // Cancel all existing reminders
                workManager.cancelAllWork()
                
                // Load all events from database and reschedule their reminders
                // This would typically use EventRepository to get all events
                // TODO: Inject EventRepository and load all events
                // val events = eventRepository.getAllEvents()
                // events.forEach { event ->
                //     if (event.reminderMinutes.isNotEmpty()) {
                //         scheduleReminder(event)
                //     }
                // }
            } catch (e: Exception) {
                android.util.Log.e("WorkManagerReminderManager", "Error rescheduling reminders", e)
            }
        }
    }
    
    fun updateReminderLocales() {
        coroutineScope.launch {
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
                android.util.Log.e("WorkManagerReminderManager", "Error updating reminder locales", e)
            }
        }
    }
    
    private fun scheduleReminderWork(
        eventId: String,
        eventTitle: String,
        eventDescription: String?,
        eventLocation: String?,
        reminderMinutes: Int,
        reminderTime: LocalDateTime
    ) {
        val delay = Duration.between(LocalDateTime.now(), reminderTime)
        
        val workRequest = OneTimeWorkRequestBuilder<ReminderWorker>()
            .setInputData(
                Data.Builder()
                    .putString(ReminderWorker.KEY_EVENT_ID, eventId)
                    .putString(ReminderWorker.KEY_EVENT_TITLE, eventTitle)
                    .putString(ReminderWorker.KEY_EVENT_DESCRIPTION, eventDescription)
                    .putString(ReminderWorker.KEY_EVENT_LOCATION, eventLocation)
                    .putInt(ReminderWorker.KEY_REMINDER_MINUTES, reminderMinutes)
                    .build()
            )
            .setInitialDelay(delay.toMinutes(), TimeUnit.MINUTES)
            .addTag(eventId)
            .setConstraints(
                Constraints.Builder()
                    .setRequiredNetworkType(NetworkType.NOT_REQUIRED)
                    .setRequiresBatteryNotLow(true)
                    .build()
            )
            .build()
        
        workManager.enqueue(workRequest)
    }
    
    fun schedulePeriodicSync() {
        val periodicWorkRequest = PeriodicWorkRequestBuilder<ReminderWorker>(
            1, TimeUnit.HOURS
        )
            .setConstraints(
                Constraints.Builder()
                    .setRequiredNetworkType(NetworkType.CONNECTED)
                    .setRequiresBatteryNotLow(true)
                    .build()
            )
            .build()
        
        workManager.enqueueUniquePeriodicWork(
            "sync_work",
            ExistingPeriodicWorkPolicy.KEEP,
            periodicWorkRequest
        )
    }
}
