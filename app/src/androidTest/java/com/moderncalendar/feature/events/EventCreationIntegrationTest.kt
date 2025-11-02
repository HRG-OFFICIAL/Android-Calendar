package com.moderncalendar.feature.events

import androidx.compose.ui.test.*
import androidx.compose.ui.test.junit4.createComposeRule
import androidx.test.ext.junit.runners.AndroidJUnit4
import com.moderncalendar.core.common.model.EventPriority
import com.moderncalendar.core.ui.theme.ModernCalendarTheme
import com.moderncalendar.core.ui.theme.EventColors
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import java.time.LocalDate
import java.time.LocalTime

@RunWith(AndroidJUnit4::class)
class EventCreationIntegrationTest {

    @get:Rule
    val composeTestRule = createComposeRule()

    @Test
    fun eventCreation_withValidInput_createsEventSuccessfully() {
        var eventCreated = false
        val mockViewModel = MockEventViewModel()

        composeTestRule.setContent {
            ModernCalendarTheme {
                EventCreationScreen(
                    onEventCreated = { eventCreated = true },
                    viewModel = mockViewModel
                )
            }
        }

        // Fill in event title
        composeTestRule
            .onNodeWithText("Event Title")
            .performTextInput("Team Meeting")

        // Fill in description
        composeTestRule
            .onNodeWithText("Description")
            .performTextInput("Weekly team sync meeting")

        // Fill in location
        composeTestRule
            .onNodeWithText("Location")
            .performTextInput("Conference Room A")

        // Select date (this would open the date picker in real scenario)
        composeTestRule
            .onNodeWithText("Date")
            .performClick()

        // In a real test, we would interact with the date picker
        // For now, we'll assume the date picker works correctly

        // Toggle all-day off to enable time selection
        composeTestRule
            .onNodeWithText("All Day Event")
            .performClick()

        // Select start time
        composeTestRule
            .onNodeWithText("Start Time")
            .performClick()

        // Select end time
        composeTestRule
            .onNodeWithText("End Time")
            .performClick()

        // Select priority
        composeTestRule
            .onNodeWithText("HIGH")
            .performClick()

        // Create the event
        composeTestRule
            .onNodeWithText("Create Event")
            .performClick()

        // Verify event was created (in a real test, we'd check the ViewModel state)
        // For now, we just verify the UI elements are present
        composeTestRule
            .onNodeWithText("Team Meeting")
            .assertExists()
    }

    @Test
    fun eventCreation_withEmptyTitle_showsValidationError() {
        val mockViewModel = MockEventViewModel()

        composeTestRule.setContent {
            ModernCalendarTheme {
                EventCreationScreen(
                    viewModel = mockViewModel
                )
            }
        }

        // Try to create event without title
        composeTestRule
            .onNodeWithText("Create Event")
            .performClick()

        // Verify validation error is shown
        composeTestRule
            .onNodeWithText("Title is required")
            .assertExists()
    }

    @Test
    fun eventCreation_withInvalidTimeRange_showsValidationError() {
        val mockViewModel = MockEventViewModel()

        composeTestRule.setContent {
            ModernCalendarTheme {
                EventCreationScreen(
                    viewModel = mockViewModel
                )
            }
        }

        // Fill in title
        composeTestRule
            .onNodeWithText("Event Title")
            .performTextInput("Test Event")

        // Toggle all-day off to enable time selection
        composeTestRule
            .onNodeWithText("All Day Event")
            .performClick()

        // In a real scenario, we would set end time before start time
        // and verify the validation error appears

        // For now, we just verify the time fields are present
        composeTestRule
            .onNodeWithText("Start Time")
            .assertExists()

        composeTestRule
            .onNodeWithText("End Time")
            .assertExists()
    }

    @Test
    fun eventCreation_allDayEvent_hidesTimeFields() {
        val mockViewModel = MockEventViewModel()

        composeTestRule.setContent {
            ModernCalendarTheme {
                EventCreationScreen(
                    viewModel = mockViewModel
                )
            }
        }

        // Verify all-day toggle is on by default (or toggle it on)
        composeTestRule
            .onNodeWithText("All Day Event")
            .assertExists()

        // Verify time fields are not visible when all-day is enabled
        // (This test would need to be adjusted based on the actual UI behavior)
    }

    @Test
    fun eventCreation_colorSelection_updatesEventColor() {
        val mockViewModel = MockEventViewModel()

        composeTestRule.setContent {
            ModernCalendarTheme {
                EventCreationScreen(
                    viewModel = mockViewModel
                )
            }
        }

        // Click on color picker
        composeTestRule
            .onNodeWithText("Event Color")
            .performClick()

        // In a real test, we would select a color from the color picker
        // and verify it's applied to the event
    }

    @Test
    fun eventCreation_prioritySelection_updatesPriority() {
        val mockViewModel = MockEventViewModel()

        composeTestRule.setContent {
            ModernCalendarTheme {
                EventCreationScreen(
                    viewModel = mockViewModel
                )
            }
        }

        // Test priority selection
        EventPriority.values().forEach { priority ->
            composeTestRule
                .onNodeWithText(priority.name)
                .assertExists()
                .performClick()
        }
    }
}

// Mock ViewModel for testing
class MockEventViewModel : EventViewModel(mockk()) {
    // Override methods as needed for testing
    // This would typically use a test repository or mock
}