package com.moderncalendar

import androidx.compose.ui.test.*
import androidx.compose.ui.test.junit4.createComposeRule
import androidx.test.ext.junit.runners.AndroidJUnit4
import com.moderncalendar.ui.theme.ModernCalendarTheme
import org.junit.Rule
import org.junit.runner.RunWith

@RunWith(AndroidJUnit4::class)
abstract class BaseUITest {
    
    @get:Rule
    val composeTestRule = createComposeRule()
    
    protected fun setContent(content: @Composable () -> Unit) {
        composeTestRule.setContent {
            ModernCalendarTheme {
                content()
            }
        }
    }
    
    protected fun onNodeWithText(text: String): SemanticsNodeInteraction {
        return composeTestRule.onNodeWithText(text)
    }
    
    protected fun onNodeWithContentDescription(description: String): SemanticsNodeInteraction {
        return composeTestRule.onNodeWithContentDescription(description)
    }
    
    protected fun onNodeWithTag(tag: String): SemanticsNodeInteraction {
        return composeTestRule.onNodeWithTag(tag)
    }
    
    protected fun onAllNodesWithText(text: String): SemanticsNodeInteractionCollection {
        return composeTestRule.onAllNodesWithText(text)
    }
    
    protected fun waitForIdle() {
        composeTestRule.waitForIdle()
    }
    
    protected fun performClick(node: SemanticsNodeInteraction) {
        node.performClick()
    }
    
    protected fun performTextInput(node: SemanticsNodeInteraction, text: String) {
        node.performTextInput(text)
    }
    
    protected fun assertIsDisplayed(node: SemanticsNodeInteraction) {
        node.assertIsDisplayed()
    }
    
    protected fun assertIsEnabled(node: SemanticsNodeInteraction) {
        node.assertIsEnabled()
    }
    
    protected fun assertTextContains(node: SemanticsNodeInteraction, text: String) {
        node.assertTextContains(text)
    }
}
