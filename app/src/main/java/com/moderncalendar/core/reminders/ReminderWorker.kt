package com.moderncalendar.core.reminders

import android.content.Context
import androidx.core.app.NotificationCompat
import androidx.core.app.NotificationManagerCompat
import androidx.hilt.work.HiltWorker
import androidx.work.CoroutineWorker
import androidx.work.WorkerParameters
import com.moderncalendar.core.data.entity.EventEntity
import dagger.assisted.Assisted
import dagger.assisted.AssistedInject
import java.time.LocalDateTime

@HiltWorker
class ReminderWorker @AssistedInject constructor(
    @Assisted context: Context,
    @Assisted workerParams: WorkerParameters
) : CoroutineWorker(context, workerParams) {

    override suspend fun doWork(): Result {
        return try {
            val eventId = inputData.getString(KEY_EVENT_ID) ?: return Result.failure()
            val eventTitle = inputData.getString(KEY_EVENT_TITLE) ?: "Event Reminder"
            val eventDescription = inputData.getString(KEY_EVENT_DESCRIPTION)
            val eventLocation = inputData.getString(KEY_EVENT_LOCATION)
            val reminderMinutes = inputData.getInt(KEY_REMINDER_MINUTES, 0)

            showNotification(
                eventId = eventId,
                title = eventTitle,
                description = eventDescription,
                location = eventLocation,
                reminderMinutes = reminderMinutes
            )

            Result.success()
        } catch (e: Exception) {
            Result.failure()
        }
    }

    private fun showNotification(
        eventId: String,
        title: String,
        description: String?,
        location: String?,
        reminderMinutes: Int
    ) {
        val notificationManager = NotificationManagerCompat.from(applicationContext)
        
        val contentText = buildString {
            if (reminderMinutes > 0) {
                append("Reminder: Event starts in $reminderMinutes minutes")
            } else {
                append("Event is starting now!")
            }
            description?.let { 
                append("\n$it")
            }
            location?.let {
                append("\n📍 $it")
            }
        }

        val notification = NotificationCompat.Builder(applicationContext, CHANNEL_ID)
            .setSmallIcon(android.R.drawable.ic_dialog_info)
            .setContentTitle(title)
            .setContentText(contentText)
            .setPriority(NotificationCompat.PRIORITY_HIGH)
            .setAutoCancel(true)
            .setCategory(NotificationCompat.CATEGORY_REMINDER)
            .build()

        notificationManager.notify(eventId.hashCode(), notification)
    }

    companion object {
        const val KEY_EVENT_ID = "event_id"
        const val KEY_EVENT_TITLE = "event_title"
        const val KEY_EVENT_DESCRIPTION = "event_description"
        const val KEY_EVENT_LOCATION = "event_location"
        const val KEY_REMINDER_MINUTES = "reminder_minutes"
        const val CHANNEL_ID = "event_reminders"
    }
}
