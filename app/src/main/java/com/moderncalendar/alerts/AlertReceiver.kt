package com.moderncalendar.alerts

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import com.moderncalendar.core.reminders.ReminderManager
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import javax.inject.Inject

@AndroidEntryPoint
class AlertReceiver : BroadcastReceiver() {
    
    @Inject
    lateinit var reminderManager: ReminderManager
    
    override fun onReceive(context: Context, intent: Intent) {
        when (intent.action) {
            Intent.ACTION_BOOT_COMPLETED -> {
                // Reschedule all reminders after boot
                CoroutineScope(Dispatchers.IO).launch {
                    reminderManager.rescheduleAllReminders()
                }
            }
            Intent.ACTION_TIME_SET,
            Intent.ACTION_TIMEZONE_CHANGED -> {
                // Reschedule reminders when time changes
                CoroutineScope(Dispatchers.IO).launch {
                    reminderManager.rescheduleAllReminders()
                }
            }
            Intent.ACTION_LOCALE_CHANGED -> {
                // Update reminders when locale changes
                CoroutineScope(Dispatchers.IO).launch {
                    reminderManager.updateReminderLocales()
                }
            }
            "android.intent.action.EVENT_REMINDER" -> {
                // Handle event reminder
                val eventId = intent.getStringExtra("event_id")
                if (eventId != null) {
                    showEventReminder(context, eventId)
                }
            }
        }
    }
    
    private fun showEventReminder(context: Context, eventId: String) {
        val intent = Intent(context, AlertActivity::class.java).apply {
            putExtra("event_id", eventId)
            flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TOP
        }
        context.startActivity(intent)
    }
}
