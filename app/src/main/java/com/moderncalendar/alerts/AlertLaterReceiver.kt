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
class AlertLaterReceiver : BroadcastReceiver() {
    
    @Inject
    lateinit var reminderManager: ReminderManager
    
    override fun onReceive(context: Context, intent: Intent) {
        when (intent.action) {
            "com.moderncalendar.action.ALERT_LATER" -> {
                val eventId = intent.getStringExtra("event_id")
                val delayMinutes = intent.getIntExtra("delay_minutes", 15)
                
                if (eventId != null) {
                    CoroutineScope(Dispatchers.IO).launch {
                        // TODO: Implement scheduleReminderLater method
                        // reminderManager.scheduleReminderLater(eventId, delayMinutes)
                    }
                }
            }
        }
    }
}
