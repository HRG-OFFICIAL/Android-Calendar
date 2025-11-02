package com.moderncalendar

import android.app.Application
import com.moderncalendar.data.MockDataService
import dagger.hilt.android.HiltAndroidApp
import javax.inject.Inject

@HiltAndroidApp
class ModernCalendarApplication : Application() {
    
    @Inject
    lateinit var mockDataService: MockDataService
    
    override fun onCreate() {
        super.onCreate()
        
        // Initialize any global configurations here
        // Firebase, Analytics, etc.
        
        // Initialize mock data for demonstration
        if (mockDataService.shouldInitializeMockData()) {
            mockDataService.initializeMockData()
        }
    }
}
