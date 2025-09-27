package com.moderncalendar.services

import android.app.Service
import android.content.Intent
import android.os.IBinder
import com.moderncalendar.core.reminders.ReminderManager
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import javax.inject.Inject

@AndroidEntryPoint
class DismissAlarmsService : Service() {
    
    @Inject
    lateinit var reminderManager: ReminderManager
    
    override fun onBind(intent: Intent?): IBinder? = null
    
    override fun onStartCommand(intent: Intent?, flags: Int, startId: Int): Int {
        when (intent?.action) {
            ACTION_DISMISS_ALARMS -> {
                dismissAlarms()
            }
        }
        return START_NOT_STICKY
    }
    
    private fun dismissAlarms() {
        CoroutineScope(Dispatchers.IO).launch {
            try {
                // Dismiss all active alarms
                reminderManager.cancelAllReminders()
            } catch (e: Exception) {
                // Handle error
            }
        }
    }
    
    companion object {
        const val ACTION_DISMISS_ALARMS = "com.moderncalendar.action.DISMISS_ALARMS"
    }
}
