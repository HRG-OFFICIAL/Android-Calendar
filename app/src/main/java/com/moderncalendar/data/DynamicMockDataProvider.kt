package com.moderncalendar.data

import com.moderncalendar.core.common.model.Event
import com.moderncalendar.core.common.model.EventPriority
import java.time.LocalDate

/**
 * @deprecated This class is deprecated and should not be used.
 * Use MockDataInitializer and MockDataService instead for consistent mock data management.
 * This class creates events that don't exist in the database, causing "event not found" errors.
 */
@Deprecated(
    message = "Use MockDataInitializer and MockDataService instead",
    replaceWith = ReplaceWith("MockDataInitializer.getMockEvents()"),
)
object DynamicMockDataProvider {
    /**
     * @deprecated Get mock events for any given date
     * This creates events dynamically without database persistence
     */
    @Deprecated("Use EventRepository.getEventsForDate() instead")
    fun getMockEventsForDate(date: LocalDate): List<Event> {
        return listOf(
            // 🌞 Morning Yoga & Meditation
            Event(
                id = "mock_yoga_$date",
                title = "🌞 Morning Yoga & Meditation",
                description = "Awaken your body and mind with sunrise yoga, calm breathing, and nature vibes. Bring your mat & water bottle!",
                location = "🌳 Central Park – Green Zone",
                startDateTime = date.atTime(6, 30),
                endDateTime = date.atTime(7, 15),
                isAllDay = false,
                color = "#FF9800", // Orange for morning energy
                priority = EventPriority.MEDIUM,
                category = "Health & Fitness",
                reminderMinutes = listOf(15, 5),
            ),
            // 💼 Team Sync Meeting
            Event(
                id = "mock_meeting_$date",
                title = "💼 Team Sync Meeting",
                description = "Weekly sprint check-in with project updates, blockers, and goals. Don't forget your coffee!",
                location = "Google Meet",
                startDateTime = date.atTime(10, 0),
                endDateTime = date.atTime(11, 0),
                isAllDay = false,
                color = "#2196F3", // Blue for work
                priority = EventPriority.HIGH,
                category = "Work",
                reminderMinutes = listOf(10, 2),
            ),
            // 🍔 Lunch with Friends
            Event(
                id = "mock_lunch_$date",
                title = "🍔 Lunch with Friends",
                description = "Catch up over cheesy slices and laughter. RSVP in group chat! 😋",
                location = "Pizza Hub, Downtown",
                startDateTime = date.atTime(13, 0),
                endDateTime = date.atTime(14, 30),
                isAllDay = false,
                color = "#00ed64", // Green for social
                priority = EventPriority.MEDIUM,
                category = "Social",
                attendees = listOf("Alex", "Sarah", "Mike"),
                reminderMinutes = listOf(30, 10),
            ),
            // 📖 Study Session – Data Structures
            Event(
                id = "mock_study_$date",
                title = "📖 Study Session – Data Structures",
                description = "Focused study on AVL Trees & Tries. No distractions!",
                location = "Home Office / Library",
                startDateTime = date.atTime(17, 0),
                endDateTime = date.atTime(19, 0),
                isAllDay = false,
                color = "#9C27B0", // Purple for learning
                priority = EventPriority.HIGH,
                category = "Education",
                reminderMinutes = listOf(15),
            ),
            // 🌙 Movie Night & Chill
            Event(
                id = "mock_movie_$date",
                title = "🌙 Movie Night & Chill",
                description = "Watching \"Inception\" with popcorn and cozy blankets",
                location = "Living Room",
                startDateTime = date.atTime(20, 30),
                endDateTime = date.atTime(23, 0),
                isAllDay = false,
                color = "#FF5722", // Deep orange for entertainment
                priority = EventPriority.LOW,
                category = "Entertainment",
                reminderMinutes = listOf(20),
            ),
        )
    }

    /**
     * @deprecated Get mock events for today
     */
    @Deprecated("Use EventRepository.getEventsForDate(LocalDate.now()) instead")
    fun getTodaysMockEvents(): List<Event> {
        return getMockEventsForDate(LocalDate.now())
    }

    /**
     * @deprecated Check if a date has mock events
     */
    @Deprecated("Use EventRepository to check for actual events instead")
    fun hasEventsForDate(date: LocalDate): Boolean {
        // For demo purposes, show events only for today
        return date == LocalDate.now()
    }

    /**
     * @deprecated Get event count for a date
     */
    @Deprecated("Use EventRepository to get actual event count instead")
    fun getEventCountForDate(date: LocalDate): Int {
        return if (hasEventsForDate(date)) 5 else 0
    }
}
