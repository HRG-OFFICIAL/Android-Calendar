package com.moderncalendar.data

import com.moderncalendar.core.common.model.Event
import com.moderncalendar.core.common.model.EventPriority
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter

object MockDataInitializer {
    
    /**
     * Check if an event ID belongs to a mock event
     */
    fun isMockEvent(eventId: String): Boolean {
        return eventId.startsWith("mock_")
    }
    
    /**
     * Get the list of mock event IDs for a specific date
     */
    fun getMockEventIds(dateStr: String): List<String> {
        return listOf(
            "mock_yoga_$dateStr",
            "mock_meeting_$dateStr", 
            "mock_lunch_$dateStr",
            "mock_study_$dateStr",
            "mock_movie_$dateStr"
        )
    }
    
    fun getMockEvents(): List<Event> {
        val today = LocalDateTime.now().toLocalDate()
        val dateStr = today.format(DateTimeFormatter.ISO_LOCAL_DATE)
        
        return listOf(
            // 🌞 Morning Yoga & Meditation
            Event(
                id = "mock_yoga_$dateStr",
                title = "🌞 Morning Yoga & Meditation",
                description = "Awaken your body and mind with sunrise yoga, calm breathing, and nature vibes. Bring your mat & water bottle!",
                location = "🌳 Central Park – Green Zone",
                startDateTime = today.atTime(6, 30),
                endDateTime = today.atTime(7, 15),
                isAllDay = false,
                color = "#FF9800", // Orange for morning energy
                priority = EventPriority.MEDIUM,
                category = "Health & Fitness",
                reminderMinutes = listOf(15, 5) // 15 and 5 minutes before
            ),
            
            // 💼 Team Sync Meeting
            Event(
                id = "mock_meeting_$dateStr",
                title = "💼 Team Sync Meeting",
                description = "Weekly sprint check-in with project updates, blockers, and goals. Don't forget your coffee!",
                location = "Google Meet",
                startDateTime = today.atTime(10, 0),
                endDateTime = today.atTime(11, 0),
                isAllDay = false,
                color = "#2196F3", // Blue for work
                priority = EventPriority.HIGH,
                category = "Work",
                reminderMinutes = listOf(10, 2) // 10 and 2 minutes before
            ),
            
            // 🍔 Lunch with Friends
            Event(
                id = "mock_lunch_$dateStr",
                title = "🍔 Lunch with Friends",
                description = "Catch up over cheesy slices and laughter. RSVP in group chat! 😋",
                location = "Pizza Hub, Downtown",
                startDateTime = today.atTime(13, 0),
                endDateTime = today.atTime(14, 30),
                isAllDay = false,
                color = "#00ed64", // Green for social
                priority = EventPriority.MEDIUM,
                category = "Social",
                attendees = listOf("Alex", "Sarah", "Mike"),
                reminderMinutes = listOf(30, 10) // 30 and 10 minutes before
            ),
            
            // 📖 Study Session – Data Structures
            Event(
                id = "mock_study_$dateStr",
                title = "📖 Study Session – Data Structures",
                description = "Focused study on AVL Trees & Tries. No distractions!",
                location = "Home Office / Library",
                startDateTime = today.atTime(17, 0),
                endDateTime = today.atTime(19, 0),
                isAllDay = false,
                color = "#9C27B0", // Purple for learning
                priority = EventPriority.HIGH,
                category = "Education",
                reminderMinutes = listOf(15) // 15 minutes before
            ),
            
            // 🌙 Movie Night & Chill
            Event(
                id = "mock_movie_$dateStr",
                title = "🌙 Movie Night & Chill",
                description = "Watching \"Inception\" with popcorn and cozy blankets",
                location = "Living Room",
                startDateTime = today.atTime(20, 30),
                endDateTime = today.atTime(23, 0),
                isAllDay = false,
                color = "#FF5722", // Deep orange for entertainment
                priority = EventPriority.LOW,
                category = "Entertainment",
                reminderMinutes = listOf(20) // 20 minutes before
            )
        )
    }
}