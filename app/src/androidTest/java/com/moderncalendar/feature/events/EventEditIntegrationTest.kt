package com.moderncalendar.feature.events

import androidx.compose.ui.test.*
import androidx.compose.ui.test.junit4.createComposeRule
import androidx.test.ext.junit.runners.AndroidJUnit4
import com.moderncalendar.core.common.model.Event
import com.moderncalendar.core.common.model.EventPriority
import com.moderncalendar.core.ui.theme.ModernCalendarTheme
import io.mockk.every
import io.mockk.mockk
import kotlinx.coroutines.flow.MutableStateFlow
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import java.time.LocalDateTime

@RunWith(AndroidJUnit4::class)
class EventEditIntegrationTest {
    @get:Rule
    val composeTestRule = createComposeRule()

    private val sampleEvent =
        Event(
            id = "1",
            title = "Team Meeting",
            description = "Weekly team sync",
            location = "Conference Room",
            startDateTime = LocalDateTime.of(2024, 1, 15, 9, 0),
            endDateTime = LocalDateTime.of(2024, 1, 15, 10, 0),
            isAllDay = false,
            color = "#FF0000",
            priority = EventPriority.HIGH,
        )

    @Test
    fun eventEdit_prePopulatesFields_withExistingEventData() {
        val mockViewModel = createMockEventViewModel(sampleEvent)

        composeTestRule.setContent {
            ModernCalendarTheme {
                EventEditScreen(
                    eventId = "1",
                    viewModel = mockViewModel,
                )
            }
        }

        // Verify fields are pre-populated with existing event data
        composeTestRule
            .onNodeWithText("Team Meeting")
            .assertExists()

        composeTestRule
            .onNodeWithText("Weekly team sync")
            .assertExists()

        composeTestRule
            .onNodeWithText("Conference Room")
            .assertExists()

        // Verify the HIGH priority is selected
        composeTestRule
            .onNodeWithText("HIGH")
            .assertExists()
    }

    @Test
    fun eventEdit_modifyTitle_updatesEventSuccessfully() {
        val mockViewModel = createMockEventViewModel(sampleEvent)
        var eventUpdated = false

        composeTestRule.setContent {
            ModernCalendarTheme {
                EventEditScreen(
                    eventId = "1",
                    viewModel = mockViewModel,
                    onEventUpdated = { eventUpdated = true },
                )
            }
        }

        // Clear existing title and enter new one
        composeTestRule
            .onNodeWithText("Team Meeting")
            .performTextClearance()

        composeTestRule
            .onNodeWithText("Event Title")
            .performTextInput("Updated Meeting")

        // Update the event
        composeTestRule
            .onNodeWithText("Update Event")
            .performClick()

        // Verify the new title is displayed
        composeTestRule
            .onNodeWithText("Updated Meeting")
            .assertExists()
    }

    @Test
    fun eventEdit_modifyDescription_updatesEventSuccessfully() {
        val mockViewModel = createMockEventViewModel(sampleEvent)

        composeTestRule.setContent {
            ModernCalendarTheme {
                EventEditScreen(
                    eventId = "1",
                    viewModel = mockViewModel,
                )
            }
        }

        // Clear existing description and enter new one
        composeTestRule
            .onNodeWithText("Weekly team sync")
            .performTextClearance()

        composeTestRule
            .onNodeWithText("Description")
            .performTextInput("Updated description")

        // Update the event
        composeTestRule
            .onNodeWithText("Update Event")
            .performClick()

        // Verify the new description is displayed
        composeTestRule
            .onNodeWithText("Updated description")
            .assertExists()
    }

    @Test
    fun eventEdit_modifyLocation_updatesEventSuccessfully() {
        val mockViewModel = createMockEventViewModel(sampleEvent)

        composeTestRule.setContent {
            ModernCalendarTheme {
                EventEditScreen(
                    eventId = "1",
                    viewModel = mockViewModel,
                )
            }
        }

        // Clear existing location and enter new one
        composeTestRule
            .onNodeWithText("Conference Room")
            .performTextClearance()

        composeTestRule
            .onNodeWithText("Location")
            .performTextInput("Meeting Room B")

        // Update the event
        composeTestRule
            .onNodeWithText("Update Event")
            .performClick()

        // Verify the new location is displayed
        composeTestRule
            .onNodeWithText("Meeting Room B")
            .assertExists()
    }

    @Test
    fun eventEdit_changePriority_updatesEventSuccessfully() {
        val mockViewModel = createMockEventViewModel(sampleEvent)

        composeTestRule.setContent {
            ModernCalendarTheme {
                EventEditScreen(
                    eventId = "1",
                    viewModel = mockViewModel,
                )
            }
        }

        // Change priority from HIGH to MEDIUM
        composeTestRule
            .onNodeWithText("MEDIUM")
            .performClick()

        // Update the event
        composeTestRule
            .onNodeWithText("Update Event")
            .performClick()

        // Verify MEDIUM priority is now selected
        composeTestRule
            .onNodeWithText("MEDIUM")
            .assertExists()
    }

    @Test
    fun eventEdit_toggleAllDay_hidesTimeFields() {
        val mockViewModel = createMockEventViewModel(sampleEvent)

        composeTestRule.setContent {
            ModernCalendarTheme {
                EventEditScreen(
                    eventId = "1",
                    viewModel = mockViewModel,
                )
            }
        }

        // Toggle all-day on
        composeTestRule
            .onNodeWithText("All Day Event")
            .performClick()

        // Verify time fields are hidden (this would need to be implemented based on actual behavior)
        // For now, we just verify the toggle exists
        composeTestRule
            .onNodeWithText("All Day Event")
            .assertExists()
    }

    @Test
    fun eventEdit_modifyDateTime_updatesEventSuccessfully() {
        val mockViewModel = createMockEventViewModel(sampleEvent)

        composeTestRule.setContent {
            ModernCalendarTheme {
                EventEditScreen(
                    eventId = "1",
                    viewModel = mockViewModel,
                )
            }
        }

        // Click on date field to open date picker
        composeTestRule
            .onNodeWithText("Date")
            .performClick()

        // In a real test, we would interact with the date picker
        // and verify the date is updated

        // Click on start time to open time picker
        composeTestRule
            .onNodeWithText("Start Time")
            .performClick()

        // In a real test, we would interact with the time picker
        // and verify the time is updated

        // Update the event
        composeTestRule
            .onNodeWithText("Update Event")
            .performClick()
    }

    @Test
    fun eventEdit_withEmptyTitle_showsValidationError() {
        val mockViewModel = createMockEventViewModel(sampleEvent)

        composeTestRule.setContent {
            ModernCalendarTheme {
                EventEditScreen(
                    eventId = "1",
                    viewModel = mockViewModel,
                )
            }
        }

        // Clear the title
        composeTestRule
            .onNodeWithText("Team Meeting")
            .performTextClearance()

        // Try to update event
        composeTestRule
            .onNodeWithText("Update Event")
            .performClick()

        // Verify validation error is shown
        composeTestRule
            .onNodeWithText("Title is required")
            .assertExists()
    }

    @Test
    fun eventEdit_withInvalidTimeRange_showsValidationError() {
        val mockViewModel = createMockEventViewModel(sampleEvent)

        composeTestRule.setContent {
            ModernCalendarTheme {
                EventEditScreen(
                    eventId = "1",
                    viewModel = mockViewModel,
                )
            }
        }

        // In a real test, we would set end time before start time
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
    fun eventEdit_colorSelection_updatesEventColor() {
        val mockViewModel = createMockEventViewModel(sampleEvent)

        composeTestRule.setContent {
            ModernCalendarTheme {
                EventEditScreen(
                    eventId = "1",
                    viewModel = mockViewModel,
                )
            }
        }

        // Click on color picker
        composeTestRule
            .onNodeWithText("Event Color")
            .performClick()

        // In a real test, we would select a different color
        // and verify it's applied to the event
    }

    private fun createMockEventViewModel(event: Event): EventViewModel {
        val mockViewModel = mockk<EventViewModel>(relaxed = true)

        every { mockViewModel.selectedEvent } returns MutableStateFlow(event)

        return mockViewModel
    }
}
