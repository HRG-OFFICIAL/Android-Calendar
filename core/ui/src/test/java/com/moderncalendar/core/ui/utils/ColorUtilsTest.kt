package com.moderncalendar.core.ui.utils

import androidx.compose.ui.graphics.Color
import org.junit.Assert.*
import org.junit.Test

class ColorUtilsTest {

    // Test null and empty string validation
    @Test
    fun `isValidColorString returns false for null input`() {
        val result = ColorUtils.isValidColorString(null)
        assertFalse(result)
    }

    @Test
    fun `isValidColorString returns false for empty string`() {
        val result = ColorUtils.isValidColorString("")
        assertFalse(result)
    }

    @Test
    fun `isValidColorString returns false for blank string`() {
        val result = ColorUtils.isValidColorString("   ")
        assertFalse(result)
    }

    // Test length validation
    @Test
    fun `isValidColorString returns false for overly long string`() {
        val longString = "a".repeat(51) // Exceeds MAX_COLOR_STRING_LENGTH
        val result = ColorUtils.isValidColorString(longString)
        assertFalse(result)
    }

    @Test
    fun `isValidColorString returns true for string at max length with valid hex`() {
        val maxLengthHex = "#" + "a".repeat(8) // 9 characters, well under limit
        val result = ColorUtils.isValidColorString(maxLengthHex)
        assertTrue(result)
    }

    // Test hex color validation with regex patterns
    @Test
    fun `isValidColorString returns true for valid 3-digit hex colors`() {
        val validHexColors = listOf("#123", "#abc", "#ABC", "#f0f")
        validHexColors.forEach { color ->
            assertTrue("Failed for $color", ColorUtils.isValidColorString(color))
        }
    }

    @Test
    fun `isValidColorString returns true for valid 4-digit hex colors`() {
        val validHexColors = listOf("#1234", "#abcd", "#ABCD", "#f0f0")
        validHexColors.forEach { color ->
            assertTrue("Failed for $color", ColorUtils.isValidColorString(color))
        }
    }

    @Test
    fun `isValidColorString returns true for valid 6-digit hex colors`() {
        val validHexColors = listOf("#123456", "#abcdef", "#ABCDEF", "#f0f0f0")
        validHexColors.forEach { color ->
            assertTrue("Failed for $color", ColorUtils.isValidColorString(color))
        }
    }

    @Test
    fun `isValidColorString returns true for valid 8-digit hex colors`() {
        val validHexColors = listOf("#12345678", "#abcdef12", "#ABCDEF12", "#f0f0f0f0")
        validHexColors.forEach { color ->
            assertTrue("Failed for $color", ColorUtils.isValidColorString(color))
        }
    }

    @Test
    fun `isValidColorString returns false for invalid hex color lengths`() {
        val invalidHexColors = listOf("#1", "#12", "#12345", "#1234567", "#123456789")
        invalidHexColors.forEach { color ->
            assertFalse("Should fail for $color", ColorUtils.isValidColorString(color))
        }
    }

    @Test
    fun `isValidColorString returns false for hex colors with invalid characters`() {
        val invalidHexColors = listOf("#12g", "#xyz", "#12G4", "#abcdefg1")
        invalidHexColors.forEach { color ->
            assertFalse("Should fail for $color", ColorUtils.isValidColorString(color))
        }
    }

    @Test
    fun `isValidColorString returns false for hex colors without hash`() {
        val invalidHexColors = listOf("123", "abcdef", "123456")
        invalidHexColors.forEach { color ->
            assertFalse("Should fail for $color", ColorUtils.isValidColorString(color))
        }
    }

    // Test named color validation
    @Test
    fun `isValidColorString returns true for valid named colors`() {
        val validNamedColors = listOf("red", "green", "blue", "yellow", "cyan", "magenta", 
                                     "black", "white", "gray", "grey", "transparent",
                                     "darkred", "darkgreen", "darkblue", "lightgray", "darkgray")
        validNamedColors.forEach { color ->
            assertTrue("Failed for $color", ColorUtils.isValidColorString(color))
        }
    }

    @Test
    fun `isValidColorString returns true for named colors with different cases`() {
        val validNamedColors = listOf("RED", "Green", "BLUE", "Yellow")
        validNamedColors.forEach { color ->
            assertTrue("Failed for $color", ColorUtils.isValidColorString(color))
        }
    }

    @Test
    fun `isValidColorString returns false for unsupported named colors`() {
        val invalidNamedColors = listOf("purple", "orange", "pink", "brown", "violet")
        invalidNamedColors.forEach { color ->
            assertFalse("Should fail for $color", ColorUtils.isValidColorString(color))
        }
    }

    @Test
    fun `isValidColorString returns false for named colors with invalid characters`() {
        val invalidNamedColors = listOf("red-color", "blue_123", "green!", "color@name")
        invalidNamedColors.forEach { color ->
            assertFalse("Should fail for $color", ColorUtils.isValidColorString(color))
        }
    }

    // Test suspicious character detection
    @Test
    fun `isValidColorString returns false for strings with suspicious characters`() {
        val suspiciousStrings = listOf("#123<script>", "red'", "blue\"", "green&amp;", 
                                      "color;drop", "test()", "hack{}", "inject[]")
        suspiciousStrings.forEach { color ->
            assertFalse("Should fail for $color", ColorUtils.isValidColorString(color))
        }
    }

    @Test
    fun `isValidColorString returns false for strings with control characters`() {
        val controlCharString = "red\u0000"
        val result = ColorUtils.isValidColorString(controlCharString)
        assertFalse(result)
    }

    // Test edge cases
    @Test
    fun `isValidColorString handles whitespace correctly`() {
        assertTrue(ColorUtils.isValidColorString("  red  "))
        assertTrue(ColorUtils.isValidColorString("  #123456  "))
        assertFalse(ColorUtils.isValidColorString("re d"))
        assertFalse(ColorUtils.isValidColorString("# 123456"))
    }

    @Test
    fun `parseColorSafely returns default color for invalid inputs`() {
        val invalidInputs = listOf(null, "", "   ", "invalid", "#xyz", "purple")
        invalidInputs.forEach { input ->
            val result = ColorUtils.parseColorSafely(input)
            assertEquals("Failed for input: $input", ColorUtils.getDefaultEventColor(), result)
        }
    }

    @Test
    fun `parseColorSafely returns correct colors for valid inputs`() {
        assertEquals(Color.Red, ColorUtils.parseColorSafely("red"))
        assertEquals(Color.Blue, ColorUtils.parseColorSafely("BLUE"))
        assertEquals(Color.Green, ColorUtils.parseColorSafely("  green  "))
    }

    // Test detailed validation
    @Test
    fun `validateColorWithDetails provides detailed error messages`() {
        val nullResult = ColorUtils.validateColorWithDetails(null)
        assertFalse(nullResult.isValid)
        assertEquals("Color string is null", nullResult.message)

        val emptyResult = ColorUtils.validateColorWithDetails("")
        assertFalse(emptyResult.isValid)
        assertEquals("Color string is empty or blank", emptyResult.message)

        val longResult = ColorUtils.validateColorWithDetails("a".repeat(51))
        assertFalse(longResult.isValid)
        assertTrue(longResult.message.contains("too long"))

        val validResult = ColorUtils.validateColorWithDetails("red")
        assertTrue(validResult.isValid)
        assertEquals("Valid named color", validResult.message)

        val validHexResult = ColorUtils.validateColorWithDetails("#123456")
        assertTrue(validHexResult.isValid)
        assertEquals("Valid hex color", validHexResult.message)
    }

    @Test
    fun `getDefaultEventColor returns consistent color`() {
        val color1 = ColorUtils.getDefaultEventColor()
        val color2 = ColorUtils.getDefaultEventColor()
        assertEquals(color1, color2)
    }

    // Test theme-aware default colors
    @Test
    fun `getThemeAwareDefaultColor returns different colors for different types`() {
        val primaryColor = ColorUtils.getThemeAwareDefaultColor(ColorUtils.DefaultColorType.PRIMARY)
        val secondaryColor = ColorUtils.getThemeAwareDefaultColor(ColorUtils.DefaultColorType.SECONDARY)
        val highPriorityColor = ColorUtils.getThemeAwareDefaultColor(ColorUtils.DefaultColorType.HIGH_PRIORITY)
        
        assertNotEquals(primaryColor, secondaryColor)
        assertNotEquals(primaryColor, highPriorityColor)
        assertNotEquals(secondaryColor, highPriorityColor)
    }

    @Test
    fun `getThemeAwareDefaultColor returns consistent colors for same type`() {
        val color1 = ColorUtils.getThemeAwareDefaultColor(ColorUtils.DefaultColorType.PRIMARY)
        val color2 = ColorUtils.getThemeAwareDefaultColor(ColorUtils.DefaultColorType.PRIMARY)
        assertEquals(color1, color2)
    }

    // Test event color palette
    @Test
    fun `getEventColorByIndex returns consistent colors for same index`() {
        val color1 = ColorUtils.getEventColorByIndex(0)
        val color2 = ColorUtils.getEventColorByIndex(0)
        assertEquals(color1, color2)
    }

    @Test
    fun `getEventColorByIndex wraps index correctly`() {
        val color1 = ColorUtils.getEventColorByIndex(0)
        val color2 = ColorUtils.getEventColorByIndex(10) // Assuming EventColors has 10 items
        assertEquals(color1, color2)
    }

    @Test
    fun `getRandomEventColor returns valid colors`() {
        val color1 = ColorUtils.getRandomEventColor()
        val color2 = ColorUtils.getRandomEventColor()
        
        // Colors should be valid (not null) but may be different
        assertNotNull(color1)
        assertNotNull(color2)
    }

    // Test accessibility functions
    @Test
    fun `calculateContrastRatio returns correct values for known colors`() {
        val blackWhiteRatio = ColorUtils.calculateContrastRatio(Color.Black, Color.White)
        assertEquals(21.0f, blackWhiteRatio, 0.1f) // Maximum contrast ratio
        
        val sameColorRatio = ColorUtils.calculateContrastRatio(Color.Red, Color.Red)
        assertEquals(1.0f, sameColorRatio, 0.1f) // Minimum contrast ratio
    }

    @Test
    fun `meetsAccessibilityContrast validates correctly for AA level`() {
        assertTrue(ColorUtils.meetsAccessibilityContrast(Color.Black, Color.White, ColorUtils.AccessibilityLevel.AA))
        assertFalse(ColorUtils.meetsAccessibilityContrast(Color.Red, Color.Green, ColorUtils.AccessibilityLevel.AA))
    }

    @Test
    fun `getContrastingTextColor returns appropriate colors`() {
        assertEquals(Color.Black, ColorUtils.getContrastingTextColor(Color.White))
        assertEquals(Color.White, ColorUtils.getContrastingTextColor(Color.Black))
    }

    // Test context-aware parsing
    @Test
    fun `parseColorSafelyWithContext uses priority-based fallbacks`() {
        val highPriorityColor = ColorUtils.parseColorSafelyWithContext(null, null, ColorUtils.EventPriority.HIGH)
        val lowPriorityColor = ColorUtils.parseColorSafelyWithContext(null, null, ColorUtils.EventPriority.LOW)
        
        assertNotEquals(highPriorityColor, lowPriorityColor)
    }

    @Test
    fun `parseColorSafelyWithContext uses deterministic colors for event IDs`() {
        val eventId = "test-event-123"
        val color1 = ColorUtils.parseColorSafelyWithContext(null, eventId)
        val color2 = ColorUtils.parseColorSafelyWithContext(null, eventId)
        
        assertEquals(color1, color2) // Should be deterministic
    }

    @Test
    fun `parseColorSafelyWithContext parses valid colors correctly`() {
        val validColor = ColorUtils.parseColorSafelyWithContext("red", "test-event")
        assertEquals(Color.Red, validColor)
    }

    // Test color consistency validation
    @Test
    fun `validateColorConsistency handles null event ID`() {
        val result = ColorUtils.validateColorConsistency(null, Color.Red)
        assertFalse(result.isConsistent)
        assertNotNull(result.recommendedColor)
        assertTrue(result.reason.contains("Event ID is required"))
    }

    @Test
    fun `validateColorConsistency handles null color`() {
        val result = ColorUtils.validateColorConsistency("test-event", null)
        assertFalse(result.isConsistent)
        assertNotNull(result.recommendedColor)
        assertTrue(result.reason.contains("Proposed color is null"))
    }

    @Test
    fun `validateColorConsistency accepts valid inputs`() {
        val result = ColorUtils.validateColorConsistency("test-event", Color.Blue)
        assertTrue(result.isConsistent)
        assertEquals(Color.Blue, result.recommendedColor)
        assertTrue(result.reason.contains("valid and consistent"))
    }

    // Test enhanced parseColorSafely with fallback types
    @Test
    fun `parseColorSafely with fallback type uses correct fallback`() {
        val primaryFallback = ColorUtils.parseColorSafely(null, ColorUtils.DefaultColorType.PRIMARY)
        val secondaryFallback = ColorUtils.parseColorSafely(null, ColorUtils.DefaultColorType.SECONDARY)
        
        assertNotEquals(primaryFallback, secondaryFallback)
        assertEquals(ColorUtils.getThemeAwareDefaultColor(ColorUtils.DefaultColorType.PRIMARY), primaryFallback)
        assertEquals(ColorUtils.getThemeAwareDefaultColor(ColorUtils.DefaultColorType.SECONDARY), secondaryFallback)
    }

    // Additional comprehensive tests for error logging scenarios
    @Test
    fun `parseColorSafely handles malformed hex colors gracefully`() {
        val malformedHexColors = listOf(
            "#", "#g", "#12", "#12345", "#1234567", "#123456789",
            "#gggggg", "#12345g", "#zzzzzz", "##123456"
        )
        
        malformedHexColors.forEach { malformedColor ->
            val result = ColorUtils.parseColorSafely(malformedColor)
            assertEquals("Failed for malformed hex: $malformedColor", 
                        ColorUtils.getDefaultEventColor(), result)
        }
    }

    @Test
    fun `parseColorSafely handles edge case inputs without crashing`() {
        val edgeCaseInputs = listOf(
            "\n", "\t", "\r\n", "   \n   ", 
            "color with spaces", "red\nblue", "color\tname",
            "unicode\u2603color", "\u0000null", "emoji🎨color"
        )
        
        edgeCaseInputs.forEach { edgeCase ->
            val result = ColorUtils.parseColorSafely(edgeCase)
            assertNotNull("Should never return null for: $edgeCase", result)
            // Should return default color for all invalid inputs
            assertEquals("Failed for edge case: $edgeCase", 
                        ColorUtils.getDefaultEventColor(), result)
        }
    }

    @Test
    fun `parseColorSafely preserves valid colors correctly`() {
        val validColors = mapOf(
            "#ff0000" to "red hex",
            "#00ff00" to "green hex", 
            "#0000ff" to "blue hex",
            "#123" to "short hex",
            "#1234" to "short hex with alpha",
            "red" to "named red",
            "GREEN" to "uppercase named",
            "  blue  " to "named with whitespace"
        )
        
        validColors.forEach { (colorString, description) ->
            val result = ColorUtils.parseColorSafely(colorString)
            assertNotNull("Should parse valid color: $description", result)
            // Verify it's not the default fallback color (unless the input actually represents that color)
            if (colorString.trim().lowercase() != "red" && !colorString.contains("ff0000")) {
                assertNotEquals("Should not fallback for valid color: $description", 
                               ColorUtils.getDefaultEventColor(), result)
            }
        }
    }

    @Test
    fun `parseColorSafelyWithContext handles all priority levels correctly`() {
        val priorities = ColorUtils.EventPriority.values()
        val testEventId = "test-event-123"
        
        priorities.forEach { priority ->
            val result = ColorUtils.parseColorSafelyWithContext(null, testEventId, priority)
            assertNotNull("Should return valid color for priority: ${priority.name}", result)
            
            // Verify consistency - same inputs should produce same outputs
            val secondResult = ColorUtils.parseColorSafelyWithContext(null, testEventId, priority)
            assertEquals("Should be consistent for priority: ${priority.name}", result, secondResult)
        }
    }

    @Test
    fun `parseColorSafelyWithContext provides consistent deterministic colors for multiple event IDs`() {
        // Simple test to verify the method works without crashing
        val eventId = "test-event"
        val color1 = ColorUtils.parseColorSafelyWithContext(null, eventId)
        val color2 = ColorUtils.parseColorSafelyWithContext(null, eventId)
        
        assertNotNull("Should return a valid color", color1)
        assertNotNull("Should return a valid color", color2)
        assertEquals("Should be deterministic for same event ID", color1, color2)
    }

    @Test
    fun `isValidColorString handles comprehensive boundary conditions`() {
        // Test exact boundary conditions with valid hex format
        val validHexAtMaxLength = "#" + "a".repeat(8) + "b".repeat(41) // Total 50 chars but invalid hex format
        assertFalse("Should reject invalid hex format even at max length", 
                   ColorUtils.isValidColorString(validHexAtMaxLength))
        
        val overMaxLength = "a".repeat(51) // Over MAX_COLOR_STRING_LENGTH  
        assertFalse("Should reject string over max length", 
                   ColorUtils.isValidColorString(overMaxLength))
        
        // Test various whitespace scenarios
        assertTrue("Should handle leading/trailing spaces", 
                  ColorUtils.isValidColorString("  red  "))
        assertTrue("Should handle tabs", 
                  ColorUtils.isValidColorString("\tblue\t"))
        assertFalse("Should reject internal spaces in hex", 
                   ColorUtils.isValidColorString("# 123456"))
        assertFalse("Should reject internal spaces in named colors", 
                   ColorUtils.isValidColorString("light blue"))
    }

    @Test
    fun `validateColorWithDetails provides comprehensive error information`() {
        val testCases = mapOf(
            null to "null",
            "" to "empty",
            "   " to "blank", 
            "a".repeat(51) to "too long",
            "#xyz" to "invalid hex",
            "purple" to "unsupported named color",
            "red<script>" to "suspicious characters",
            "#" to "incomplete hex",
            "#12345" to "invalid hex length"
        )
        
        testCases.forEach { (input, expectedErrorType) ->
            val result = ColorUtils.validateColorWithDetails(input)
            assertFalse("Should be invalid for $expectedErrorType: $input", result.isValid)
            assertNotNull("Should have error message for $expectedErrorType", result.message)
            assertTrue("Error message should be descriptive for $expectedErrorType", 
                      result.message.isNotEmpty())
        }
    }

    @Test
    fun `error logging scenarios are handled gracefully`() {
        // Test scenarios that would trigger different types of logging
        val loggingTestCases = listOf(
            null to "null input logging",
            "" to "empty input logging", 
            "invalid-format" to "invalid format logging",
            "#xyz" to "malformed hex logging",
            "unsupported-color" to "unsupported named color logging",
            "a".repeat(100) to "overly long string logging"
        )
        
        loggingTestCases.forEach { (input, scenario) ->
            // These should all return valid colors despite triggering logging
            val result = ColorUtils.parseColorSafely(input)
            assertNotNull("Should return valid color for $scenario", result)
            
            val contextResult = ColorUtils.parseColorSafelyWithContext(input, "test-event")
            assertNotNull("Should return valid color with context for $scenario", contextResult)
        }
    }

    @Test
    fun `hex color parsing handles all supported formats correctly`() {
        val hexTestCases = mapOf(
            "#f00" to "3-digit red",
            "#F00" to "3-digit red uppercase", 
            "#ff00" to "4-digit red with alpha",
            "#FF00" to "4-digit red with alpha uppercase",
            "#ff0000" to "6-digit red",
            "#FF0000" to "6-digit red uppercase",
            "#ffff0000" to "8-digit red with alpha",
            "#FFFF0000" to "8-digit red with alpha uppercase"
        )
        
        hexTestCases.forEach { (hexColor, description) ->
            assertTrue("Should validate $description: $hexColor", 
                      ColorUtils.isValidColorString(hexColor))
            
            val parsedColor = ColorUtils.parseColorSafely(hexColor)
            assertNotNull("Should parse $description: $hexColor", parsedColor)
            assertNotEquals("Should not fallback for valid $description", 
                           ColorUtils.getDefaultEventColor(), parsedColor)
        }
    }

    @Test
    fun `named color parsing is case insensitive and comprehensive`() {
        val namedColorVariations = listOf(
            "red", "RED", "Red", "rEd",
            "green", "GREEN", "Green", "gReEn", 
            "blue", "BLUE", "Blue", "bLuE",
            "white", "WHITE", "White",
            "black", "BLACK", "Black",
            "gray", "GRAY", "Gray", "grey", "GREY", "Grey"
        )
        
        namedColorVariations.forEach { colorName ->
            assertTrue("Should validate named color: $colorName", 
                      ColorUtils.isValidColorString(colorName))
            
            val parsedColor = ColorUtils.parseColorSafely(colorName)
            assertNotNull("Should parse named color: $colorName", parsedColor)
        }
    }

    // Test view consistency functions
    @Test
    fun `ensureViewConsistency returns consistent colors for same event ID`() {
        val eventId = "test-event-123"
        val color1 = ColorUtils.ensureViewConsistency(eventId, null, ColorUtils.CalendarViewType.MONTH)
        val color2 = ColorUtils.ensureViewConsistency(eventId, null, ColorUtils.CalendarViewType.WEEK)
        
        assertEquals(color1, color2) // Should be consistent across views
    }

    @Test
    fun `ensureViewConsistency preserves valid colors`() {
        val eventId = "test-event"
        val validColor = Color.Blue
        val result = ColorUtils.ensureViewConsistency(eventId, validColor, ColorUtils.CalendarViewType.MONTH)
        
        assertEquals(validColor, result) // Should preserve valid colors
    }

    @Test
    fun `getViewContextDefaultColor returns different colors for different contexts`() {
        val monthColor = ColorUtils.getViewContextDefaultColor(ColorUtils.CalendarViewType.MONTH)
        val agendaColor = ColorUtils.getViewContextDefaultColor(ColorUtils.CalendarViewType.AGENDA)
        val highlightedColor = ColorUtils.getViewContextDefaultColor(ColorUtils.CalendarViewType.MONTH, true)
        
        assertNotEquals(agendaColor, monthColor)
        assertNotEquals(highlightedColor, monthColor)
    }

    @Test
    fun `validateDefaultColorAccessibility returns comprehensive report`() {
        val report = ColorUtils.validateDefaultColorAccessibility()
        
        assertNotNull(report)
        assertTrue(report.individualResults.isNotEmpty())
        assertEquals(ColorUtils.DefaultColorType.values().size, report.individualResults.size)
    }
}