package com.moderncalendar

import android.app.Application
import android.util.Log
import com.moderncalendar.core.common.repository.EventRepository
import com.moderncalendar.data.MockDataService
import dagger.hilt.android.HiltAndroidApp
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltAndroidApp
class ModernCalendarApplication : Application() {
    @Inject
    lateinit var mockDataService: MockDataService

    @Inject
    lateinit var eventRepository: EventRepository

    private val applicationScope = CoroutineScope(SupervisorJob() + Dispatchers.IO)

    override fun onCreate() {
        super.onCreate()

        // Initialize any global configurations here
        // Firebase, Analytics, etc.

        // Cleanup invalid colors in background
        applicationScope.launch {
            try {
                val result = eventRepository.cleanupInvalidColors()
                when (result) {
                    is com.moderncalendar.core.common.Result.Success -> {
                        if (result.data > 0) {
                            Log.i("ModernCalendarApp", "Fixed ${result.data} events with invalid colors")
                        }
                    }
                    is com.moderncalendar.core.common.Result.Error -> {
                        Log.w("ModernCalendarApp", "Failed to cleanup invalid colors: ${result.exception.message}")
                    }
                    else -> { /* Loading state not expected here */ }
                }
            } catch (e: Exception) {
                Log.e("ModernCalendarApp", "Error during color cleanup", e)
            }
        }

        // Initialize mock data for demonstration
        if (mockDataService.shouldInitializeMockData()) {
            mockDataService.initializeMockData()
        }
    }
}
