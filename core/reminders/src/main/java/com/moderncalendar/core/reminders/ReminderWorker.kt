package com.moderncalendar.core.reminders

import android.app.NotificationChannel
import android.app.NotificationManager
import android.content.Context
import androidx.core.app.NotificationCompat
import androidx.hilt.work.HiltWorker
import androidx.work.CoroutineWorker
import androidx.work.WorkerParameters
// import com.moderncalendar.R
import dagger.assisted.Assisted
import dagger.assisted.AssistedInject

@HiltWorker
class ReminderWorker @AssistedInject constructor(
    @Assisted context: Context,
    @Assisted workerParams: WorkerParameters
) : CoroutineWorker(context, workerParams) {

    companion object {
        const val CHANNEL_ID = "event_reminders"
        const val NOTIFICATION_ID = 1000
        const val KEY_EVENT_TITLE = "event_title"
        const val KEY_EVENT_DESCRIPTION = "event_description"
        const val KEY_EVENT_TIME = "event_time"
        const val KEY_EVENT_LOCATION = "event_location"
    }

    override suspend fun doWork(): Result {
        return try {
            val eventTitle = inputData.getString(KEY_EVENT_TITLE) ?: "Event Reminder"
            val eventDescription = inputData.getString(KEY_EVENT_DESCRIPTION)
            val eventTime = inputData.getString(KEY_EVENT_TIME) ?: ""
            val eventLocation = inputData.getString(KEY_EVENT_LOCATION)

            createNotificationChannel()
            sendNotification(eventTitle, eventDescription, eventTime, eventLocation)
            
            Result.success()
        } catch (exception: Exception) {
            Result.failure()
        }
    }

    private fun createNotificationChannel() {
        val channel = NotificationChannel(
            CHANNEL_ID,
            "Event Reminders",
            NotificationManager.IMPORTANCE_HIGH
        ).apply {
            description = "Notifications for upcoming events"
        }

        val notificationManager = applicationContext
            .getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
        notificationManager.createNotificationChannel(channel)
    }

    private fun sendNotification(
        title: String,
        description: String?,
        time: String,
        location: String?
    ) {
        val notificationManager = applicationContext
            .getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager

        val notificationBuilder = NotificationCompat.Builder(applicationContext, CHANNEL_ID)
            .setSmallIcon(android.R.drawable.ic_dialog_info)
            .setContentTitle(title)
            .setContentText(description ?: "Event starting at $time")
            .setPriority(NotificationCompat.PRIORITY_HIGH)
            .setAutoCancel(true)

        if (location != null) {
            notificationBuilder.setSubText("Location: $location")
        }

        notificationManager.notify(NOTIFICATION_ID, notificationBuilder.build())
    }
}
