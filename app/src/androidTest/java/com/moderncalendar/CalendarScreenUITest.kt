package com.moderncalendar

import androidx.compose.ui.test.*
import androidx.test.ext.junit.runners.AndroidJUnit4
import com.moderncalendar.feature.calendar.CalendarScreen
import com.moderncalendar.feature.calendar.CalendarViewModel
import io.mockk.mockk
import org.junit.Test
import org.junit.runner.RunWith

@RunWith(AndroidJUnit4::class)
class CalendarScreenUITest : BaseUITest() {
    
    @Test
    fun calendarScreen_displaysCorrectly() {
        // Given
        val mockViewModel = mockk<CalendarViewModel>(relaxed = true)
        var onEventClickCalled = false
        var onCreateEventClickCalled = false
        var onSearchClickCalled = false
        var onSettingsClickCalled = false
        
        // When
        setContent {
            CalendarScreen(
                onEventClick = { onEventClickCalled = true },
                onCreateEventClick = { onCreateEventClickCalled = true },
                onSearchClick = { onSearchClickCalled = true },
                onSettingsClick = { onSettingsClickCalled = true },
                viewModel = mockViewModel
            )
        }
        
        // Then
        onNodeWithText("Modern Calendar").assertIsDisplayed()
        onNodeWithContentDescription("Create Event").assertIsDisplayed()
        onNodeWithContentDescription("Search").assertIsDisplayed()
        onNodeWithContentDescription("Settings").assertIsDisplayed()
    }
    
    @Test
    fun calendarScreen_clickCreateEventButton_callsOnCreateEventClick() {
        // Given
        val mockViewModel = mockk<CalendarViewModel>(relaxed = true)
        var onCreateEventClickCalled = false
        
        setContent {
            CalendarScreen(
                onCreateEventClick = { onCreateEventClickCalled = true },
                viewModel = mockViewModel
            )
        }
        
        // When
        performClick(onNodeWithContentDescription("Create Event"))
        
        // Then
        assert(onCreateEventClickCalled)
    }
    
    @Test
    fun calendarScreen_clickSearchButton_callsOnSearchClick() {
        // Given
        val mockViewModel = mockk<CalendarViewModel>(relaxed = true)
        var onSearchClickCalled = false
        
        setContent {
            CalendarScreen(
                onSearchClick = { onSearchClickCalled = true },
                viewModel = mockViewModel
            )
        }
        
        // When
        performClick(onNodeWithContentDescription("Search"))
        
        // Then
        assert(onSearchClickCalled)
    }
    
    @Test
    fun calendarScreen_clickSettingsButton_callsOnSettingsClick() {
        // Given
        val mockViewModel = mockk<CalendarViewModel>(relaxed = true)
        var onSettingsClickCalled = false
        
        setContent {
            CalendarScreen(
                onSettingsClick = { onSettingsClickCalled = true },
                viewModel = mockViewModel
            )
        }
        
        // When
        performClick(onNodeWithContentDescription("Settings"))
        
        // Then
        assert(onSettingsClickCalled)
    }
    
    @Test
    fun calendarScreen_displaysCalendarViewSelector() {
        // Given
        val mockViewModel = mockk<CalendarViewModel>(relaxed = true)
        
        setContent {
            CalendarScreen(viewModel = mockViewModel)
        }
        
        // Then
        onNodeWithText("Month").assertIsDisplayed()
        onNodeWithText("Week").assertIsDisplayed()
        onNodeWithText("Day").assertIsDisplayed()
        onNodeWithText("Year").assertIsDisplayed()
        onNodeWithText("Heatmap").assertIsDisplayed()
    }
    
    @Test
    fun calendarScreen_displaysSelectedDate() {
        // Given
        val mockViewModel = mockk<CalendarViewModel>(relaxed = true)
        
        setContent {
            CalendarScreen(viewModel = mockViewModel)
        }
        
        // Then
        onNodeWithText("Events for").assertIsDisplayed()
    }
}
