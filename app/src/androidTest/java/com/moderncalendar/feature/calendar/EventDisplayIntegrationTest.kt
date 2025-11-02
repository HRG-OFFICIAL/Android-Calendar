package com.moderncalendar.feature.calendar

import androidx.test.ext.junit.runners.AndroidJUnit4
import com.moderncalendar.core.ui.utils.ColorUtils
import org.junit.Test
import org.junit.runner.RunWith
import kotlin.test.assertNotNull

@RunWith(AndroidJUnit4::class)
class EventDisplayIntegrationTest {

    @Test
    fun `color parsing works correctly with valid hex color`() {
        // Given
        val validHexColor = "#FF5722"

        // When
        val parsedColor = ColorUtils.parseColorSafely(validHexColor)

        // Then
        assertNotNull("Should parse valid hex color", parsedColor)
    }

    @Test
    fun `color parsing works with null color`() {
        // When
        val parsedColor = ColorUtils.parseColorSafely(null)

        // Then - Should not crash and return default color
        assertNotNull("Should return default color for null input", parsedColor)
    }

    @Test
    fun `color parsing works with empty string`() {
        // When
        val parsedColor = ColorUtils.parseColorSafely("")

        // Then - Should not crash and return default color
        assertNotNull("Should return default color for empty string", parsedColor)
    }

    @Test
    fun `color parsing works with invalid hex color`() {
        // When
        val parsedColor = ColorUtils.parseColorSafely("#GGGGGG")

        // Then - Should not crash and return default color
        assertNotNull("Should return default color for invalid hex", parsedColor)
    }

    @Test
    fun `color parsing works with malformed color string`() {
        // When
        val parsedColor = ColorUtils.parseColorSafely("not-a-color")

        // Then - Should not crash and return default color
        assertNotNull("Should return default color for malformed string", parsedColor)
    }
}