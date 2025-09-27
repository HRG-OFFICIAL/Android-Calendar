package com.moderncalendar

import android.app.Application
import dagger.hilt.android.HiltAndroidApp

@HiltAndroidApp
class ModernCalendarApplication : Application() {
    
    override fun onCreate() {
        super.onCreate()
        
        // Initialize any global configurations here
        // Firebase, Analytics, etc.
    }
}
