package com.moderncalendar.data

import android.content.Context
import android.content.SharedPreferences
import com.moderncalendar.core.common.Result
import com.moderncalendar.core.common.repository.EventRepository
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class MockDataService
    @Inject
    constructor(
        private val eventRepository: EventRepository,
        @ApplicationContext private val context: Context,
    ) {
        private val prefs: SharedPreferences by lazy {
            context.getSharedPreferences("mock_data_prefs", Context.MODE_PRIVATE)
        }

        companion object {
            private const val KEY_MOCK_DATA_INITIALIZED = "mock_data_initialized"
        }

        /**
         * Initialize mock calendar events
         * This should be called once when the app starts for the first time
         */
        fun initializeMockData() {
            CoroutineScope(Dispatchers.IO).launch {
                try {
                    val mockEvents = MockDataInitializer.getMockEvents()

                    // Check if mock events already exist to avoid duplicates
                    val existingEventIds = mutableSetOf<String>()
                    eventRepository.getAllEvents().collect { result ->
                        when (result) {
                            is Result.Success -> {
                                existingEventIds.addAll(result.data.map { it.id })
                                return@collect // Exit after getting the data
                            }
                            is Result.Error -> {
                                println("⚠️ Warning: Failed to get existing events - ${result.exception.message}")
                                return@collect // Exit on error
                            }
                            is Result.Loading -> {
                                // Continue processing
                            }
                        }
                    }

                    // Insert only new events that don't already exist
                    var insertedCount = 0
                    var skippedCount = 0

                    mockEvents.forEach { event ->
                        if (existingEventIds.contains(event.id)) {
                            println("⏭️ Skipping existing event: ${event.title}")
                            skippedCount++
                        } else {
                            when (val result = eventRepository.insertEvent(event)) {
                                is Result.Success -> {
                                    println("✅ Successfully inserted event: ${event.title}")
                                    insertedCount++
                                }
                                is Result.Error -> {
                                    println("❌ Failed to insert event: ${event.title} - ${result.exception.message}")
                                }
                                is Result.Loading -> {
                                    // This shouldn't happen for suspend functions
                                }
                            }
                        }
                    }

                    // Mark as initialized
                    markMockDataAsInitialized()
                    println("🎉 Mock data initialization completed! Inserted: $insertedCount, Skipped: $skippedCount")
                } catch (e: Exception) {
                    println("💥 Error initializing mock data: ${e.message}")
                    e.printStackTrace()
                }
            }
        }

        /**
         * Check if mock data has already been initialized
         */
        fun shouldInitializeMockData(): Boolean {
            return !prefs.getBoolean(KEY_MOCK_DATA_INITIALIZED, false)
        }

        /**
         * Mark mock data as initialized
         */
        private fun markMockDataAsInitialized() {
            prefs.edit().putBoolean(KEY_MOCK_DATA_INITIALIZED, true).apply()
        }

        /**
         * Reset mock data flag (for testing purposes)
         */
        fun resetMockDataFlag() {
            prefs.edit().putBoolean(KEY_MOCK_DATA_INITIALIZED, false).apply()
        }

        /**
         * Add mock events without clearing existing ones
         */
        fun addMockEventsIfNotExist() {
            CoroutineScope(Dispatchers.IO).launch {
                try {
                    val mockEvents = MockDataInitializer.getMockEvents()

                    // Check which events already exist
                    val existingEventIds = mutableSetOf<String>()
                    eventRepository.getAllEvents().collect { result ->
                        when (result) {
                            is Result.Success -> {
                                existingEventIds.addAll(result.data.map { it.id })
                                return@collect // Exit after getting the data
                            }
                            is Result.Error -> {
                                println("⚠️ Warning: Failed to get existing events - ${result.exception.message}")
                                return@collect // Exit on error
                            }
                            is Result.Loading -> {
                                // Continue processing
                            }
                        }
                    }

                    // Insert only new events
                    var insertedCount = 0
                    var skippedCount = 0

                    mockEvents.forEach { event ->
                        if (existingEventIds.contains(event.id)) {
                            skippedCount++
                        } else {
                            when (val result = eventRepository.insertEvent(event)) {
                                is Result.Success -> {
                                    println("✅ Added new mock event: ${event.title}")
                                    insertedCount++
                                }
                                is Result.Error -> {
                                    println("❌ Failed to add event: ${event.title} - ${result.exception.message}")
                                }
                                is Result.Loading -> {
                                    // This shouldn't happen for suspend functions
                                }
                            }
                        }
                    }

                    println("📅 Mock events check completed! Added: $insertedCount, Already existed: $skippedCount")
                } catch (e: Exception) {
                    println("💥 Error adding mock events: ${e.message}")
                    e.printStackTrace()
                }
            }
        }

        /**
         * Clean up duplicate events (removes events with same title and time but different IDs)
         */
        fun cleanupDuplicateEvents() {
            CoroutineScope(Dispatchers.IO).launch {
                try {
                    println("🧹 Cleaning up duplicate events...")

                    eventRepository.getAllEvents().collect { result ->
                        when (result) {
                            is Result.Success -> {
                                val allEvents = result.data
                                val eventsToKeep = mutableSetOf<String>()
                                val eventsToDelete = mutableListOf<String>()

                                // Group events by title and start time to find duplicates
                                val eventGroups = allEvents.groupBy { "${it.title}_${it.startDateTime}" }

                                eventGroups.forEach { (key, events) ->
                                    if (events.size > 1) {
                                        // Keep the first event (preferably one with mock_ prefix)
                                        val mockEvent = events.find { it.id.startsWith("mock_") }
                                        val eventToKeep = mockEvent ?: events.first()
                                        eventsToKeep.add(eventToKeep.id)

                                        // Mark others for deletion
                                        events.filter { it.id != eventToKeep.id }.forEach { duplicate ->
                                            eventsToDelete.add(duplicate.id)
                                            println("🗑️ Marking duplicate for deletion: ${duplicate.title} (ID: ${duplicate.id})")
                                        }
                                    } else {
                                        eventsToKeep.add(events.first().id)
                                    }
                                }

                                // Delete duplicate events
                                eventsToDelete.forEach { eventId ->
                                    eventRepository.deleteEvent(eventId)
                                }

                                println("🧹 Cleanup completed! Removed ${eventsToDelete.size} duplicate events")
                                return@collect
                            }
                            is Result.Error -> {
                                println("⚠️ Warning: Failed to get events for cleanup - ${result.exception.message}")
                                return@collect
                            }
                            is Result.Loading -> {
                                // Continue processing
                            }
                        }
                    }
                } catch (e: Exception) {
                    println("💥 Error cleaning up duplicates: ${e.message}")
                    e.printStackTrace()
                }
            }
        }

        /**
         * Refresh mock events with current date (removes old mock events and adds new ones)
         */
        fun refreshMockEventsForToday() {
            CoroutineScope(Dispatchers.IO).launch {
                try {
                    println("🔄 Refreshing mock events for today...")

                    // First clean up any duplicates
                    cleanupDuplicateEvents()

                    // Delete only existing mock events (those with IDs starting with "mock_")
                    eventRepository.getAllEvents().collect { result ->
                        when (result) {
                            is Result.Success -> {
                                val mockEventsToDelete = result.data.filter { it.id.startsWith("mock_") }
                                mockEventsToDelete.forEach { event ->
                                    eventRepository.deleteEvent(event.id)
                                    println("🗑️ Removed old mock event: ${event.title}")
                                }
                                return@collect // Exit after processing
                            }
                            is Result.Error -> {
                                println("⚠️ Warning: Failed to get existing events - ${result.exception.message}")
                                return@collect // Exit on error
                            }
                            is Result.Loading -> {
                                // Continue processing
                            }
                        }
                    }

                    // Add fresh mock events for today
                    addMockEventsIfNotExist()
                    println("🔄 Mock events refreshed for today!")
                } catch (e: Exception) {
                    println("💥 Error refreshing mock data: ${e.message}")
                    e.printStackTrace()
                }
            }
        }
    }
