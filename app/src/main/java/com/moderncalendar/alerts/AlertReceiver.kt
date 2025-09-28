package com.moderncalendar.alerts

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.util.Log

class AlertReceiver : BroadcastReceiver() {
    
    override fun onReceive(context: Context, intent: Intent) {
        val eventId = intent.getStringExtra("event_id")
        val eventTitle = intent.getStringExtra("event_title")
        
        Log.d("AlertReceiver", "Received reminder for event: $eventTitle (ID: $eventId)")
        
        // TODO: Show notification for the reminder
        // This would typically involve creating a notification using NotificationManager
    }
}