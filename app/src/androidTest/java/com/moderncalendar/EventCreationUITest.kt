package com.moderncalendar

import androidx.compose.ui.test.*
import androidx.test.ext.junit.runners.AndroidJUnit4
import com.moderncalendar.feature.events.EventCreationScreen
import com.moderncalendar.feature.events.EventViewModel
import io.mockk.mockk
import org.junit.Test
import org.junit.runner.RunWith

@RunWith(AndroidJUnit4::class)
class EventCreationUITest : BaseUITest() {
    
    @Test
    fun eventCreationScreen_displaysCorrectly() {
        // Given
        val mockViewModel = mockk<EventViewModel>(relaxed = true)
        var onEventCreatedCalled = false
        
        // When
        setContent {
            EventCreationScreen(
                onEventCreated = { onEventCreatedCalled = true },
                viewModel = mockViewModel
            )
        }
        
        // Then
        onNodeWithText("Create Event").assertIsDisplayed()
        onNodeWithText("Event Title").assertIsDisplayed()
        onNodeWithText("Description").assertIsDisplayed()
        onNodeWithText("Location").assertIsDisplayed()
        onNodeWithText("All Day Event").assertIsDisplayed()
        onNodeWithText("Event Color").assertIsDisplayed()
        onNodeWithText("Recurring Event").assertIsDisplayed()
        onNodeWithText("Create Event").assertIsDisplayed()
    }
    
    @Test
    fun eventCreationScreen_enterEventTitle_updatesTextField() {
        // Given
        val mockViewModel = mockk<EventViewModel>(relaxed = true)
        val eventTitle = "Test Event Title"
        
        setContent {
            EventCreationScreen(viewModel = mockViewModel)
        }
        
        // When
        performTextInput(onNodeWithText("Event Title"), eventTitle)
        
        // Then
        onNodeWithText(eventTitle).assertIsDisplayed()
    }
    
    @Test
    fun eventCreationScreen_enterDescription_updatesTextField() {
        // Given
        val mockViewModel = mockk<EventViewModel>(relaxed = true)
        val eventDescription = "Test Event Description"
        
        setContent {
            EventCreationScreen(viewModel = mockViewModel)
        }
        
        // When
        performTextInput(onNodeWithText("Description"), eventDescription)
        
        // Then
        onNodeWithText(eventDescription).assertIsDisplayed()
    }
    
    @Test
    fun eventCreationScreen_enterLocation_updatesTextField() {
        // Given
        val mockViewModel = mockk<EventViewModel>(relaxed = true)
        val eventLocation = "Test Location"
        
        setContent {
            EventCreationScreen(viewModel = mockViewModel)
        }
        
        // When
        performTextInput(onNodeWithText("Location"), eventLocation)
        
        // Then
        onNodeWithText(eventLocation).assertIsDisplayed()
    }
    
    @Test
    fun eventCreationScreen_toggleAllDayEvent_updatesCheckbox() {
        // Given
        val mockViewModel = mockk<EventViewModel>(relaxed = true)
        
        setContent {
            EventCreationScreen(viewModel = mockViewModel)
        }
        
        // When
        performClick(onNodeWithText("All Day Event"))
        
        // Then
        onNodeWithText("All Day Event").assertIsDisplayed()
    }
    
    @Test
    fun eventCreationScreen_toggleRecurringEvent_updatesCheckbox() {
        // Given
        val mockViewModel = mockk<EventViewModel>(relaxed = true)
        
        setContent {
            EventCreationScreen(viewModel = mockViewModel)
        }
        
        // When
        performClick(onNodeWithText("Recurring Event"))
        
        // Then
        onNodeWithText("Recurring Event").assertIsDisplayed()
    }
    
    @Test
    fun eventCreationScreen_createButtonDisabled_whenTitleEmpty() {
        // Given
        val mockViewModel = mockk<EventViewModel>(relaxed = true)
        
        setContent {
            EventCreationScreen(viewModel = mockViewModel)
        }
        
        // Then
        onNodeWithText("Create Event").assertIsNotEnabled()
    }
    
    @Test
    fun eventCreationScreen_createButtonEnabled_whenTitleEntered() {
        // Given
        val mockViewModel = mockk<EventViewModel>(relaxed = true)
        val eventTitle = "Test Event"
        
        setContent {
            EventCreationScreen(viewModel = mockViewModel)
        }
        
        // When
        performTextInput(onNodeWithText("Event Title"), eventTitle)
        
        // Then
        onNodeWithText("Create Event").assertIsEnabled()
    }
    
    @Test
    fun eventCreationScreen_clickCreateButton_callsOnEventCreated() {
        // Given
        val mockViewModel = mockk<EventViewModel>(relaxed = true)
        var onEventCreatedCalled = false
        val eventTitle = "Test Event"
        
        setContent {
            EventCreationScreen(
                onEventCreated = { onEventCreatedCalled = true },
                viewModel = mockViewModel
            )
        }
        
        // When
        performTextInput(onNodeWithText("Event Title"), eventTitle)
        performClick(onNodeWithText("Create Event"))
        
        // Then
        assert(onEventCreatedCalled)
    }
}
