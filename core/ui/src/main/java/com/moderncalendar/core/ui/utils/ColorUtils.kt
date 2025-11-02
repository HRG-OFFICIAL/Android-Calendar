package com.moderncalendar.core.ui.utils

import android.util.Log
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.luminance
import com.moderncalendar.core.ui.theme.CalendarPrimary
import com.moderncalendar.core.ui.theme.CalendarSecondary
import com.moderncalendar.core.ui.theme.CalendarTertiary
import com.moderncalendar.core.ui.theme.EventColors
import com.moderncalendar.core.ui.theme.EventHighPriority
import com.moderncalendar.core.ui.theme.EventMediumPriority
import com.moderncalendar.core.ui.theme.EventLowPriority

/**
 * Utility object for safe color parsing with comprehensive error handling and theme-aware fallbacks.
 * Prevents crashes from invalid color strings by providing appropriate fallback colors based on context.
 * 
 * Features:
 * - Safe color parsing that never throws exceptions
 * - Theme-aware default colors using Material Design principles
 * - Accessibility-compliant color validation and contrast checking
 * - Consistent color assignment across different calendar views
 * - Context-aware fallback selection based on event priority and type
 * 
 * Usage Examples:
 * ```kotlin
 * // Basic safe parsing with primary fallback
 * val color = ColorUtils.parseColorSafely("#invalid")
 * 
 * // Context-aware parsing with priority-based fallback
 * val eventColor = ColorUtils.parseColorSafelyWithContext(
 *     colorString = event.color,
 *     eventId = event.id,
 *     priority = ColorUtils.EventPriority.HIGH
 * )
 * 
 * // Theme-aware default colors
 * val primaryColor = ColorUtils.getThemeAwareDefaultColor(ColorUtils.DefaultColorType.PRIMARY)
 * val highPriorityColor = ColorUtils.getThemeAwareDefaultColor(ColorUtils.DefaultColorType.HIGH_PRIORITY)
 * ```
 */
object ColorUtils {
    
    private const val TAG = "ColorUtils"
    
    // Maximum allowed length for color strings to prevent DoS attacks
    private const val MAX_COLOR_STRING_LENGTH = 50
    
    // Regex patterns for comprehensive hex color validation
    private val hexColorPatterns = mapOf(
        3 to Regex("^#[0-9a-fA-F]{3}$"),           // #RGB
        4 to Regex("^#[0-9a-fA-F]{4}$"),           // #ARGB
        6 to Regex("^#[0-9a-fA-F]{6}$"),           // #RRGGBB
        8 to Regex("^#[0-9a-fA-F]{8}$")            // #AARRGGBB
    )
    
    // Regex pattern for named color validation (letters, numbers, underscores only)
    private val namedColorPattern = Regex("^[a-zA-Z][a-zA-Z0-9_]*$")
    
    // Named colors mapping for basic color support
    private val namedColors = mapOf(
        "red" to Color.Red,
        "green" to Color.Green,
        "blue" to Color.Blue,
        "yellow" to Color.Yellow,
        "cyan" to Color.Cyan,
        "magenta" to Color.Magenta,
        "black" to Color.Black,
        "white" to Color.White,
        "gray" to Color.Gray,
        "grey" to Color.Gray,
        "transparent" to Color.Transparent,
        "darkred" to Color(0xFF8B0000),
        "darkgreen" to Color(0xFF006400),
        "darkblue" to Color(0xFF00008B),
        "lightgray" to Color.LightGray,
        "lightgrey" to Color.LightGray,
        "darkgray" to Color.DarkGray,
        "darkgrey" to Color.DarkGray
    )
    
    /**
     * Safely parses a color string and returns a valid Color object.
     * Never throws exceptions - always returns a valid color.
     * 
     * @param colorString The color string to parse (hex or named color)
     * @param fallbackType The type of fallback color to use if parsing fails
     * @return A valid Color object, or appropriate fallback color if parsing fails
     */
    fun parseColorSafely(
        colorString: String?, 
        fallbackType: DefaultColorType = DefaultColorType.PRIMARY
    ): Color {
        return try {
            when {
                colorString.isNullOrBlank() -> {
                    Log.w(TAG, "Color string is null or empty, using ${fallbackType.name.lowercase()} fallback color")
                    getThemeAwareDefaultColor(fallbackType)
                }
                
                isValidColorString(colorString) -> {
                    parseValidatedColor(colorString) ?: run {
                        Log.w(TAG, "Failed to parse validated color: $colorString, using ${fallbackType.name.lowercase()} fallback")
                        getThemeAwareDefaultColor(fallbackType)
                    }
                }
                
                else -> {
                    Log.w(TAG, "Invalid color format: $colorString, using ${fallbackType.name.lowercase()} fallback color")
                    getThemeAwareDefaultColor(fallbackType)
                }
            }
        } catch (e: Exception) {
            Log.e(TAG, "Unexpected error parsing color: $colorString", e)
            getThemeAwareDefaultColor(fallbackType)
        }
    }
    
    /**
     * Safely parses a color string with event context for better fallback selection.
     * 
     * @param colorString The color string to parse
     * @param eventId Optional event ID for consistent color assignment
     * @param priority Optional event priority for appropriate fallback color
     * @return A valid Color object with context-aware fallback
     */
    fun parseColorSafelyWithContext(
        colorString: String?,
        eventId: String? = null,
        priority: EventPriority = EventPriority.NORMAL
    ): Color {
        val fallbackType = when (priority) {
            EventPriority.HIGH -> DefaultColorType.HIGH_PRIORITY
            EventPriority.MEDIUM -> DefaultColorType.MEDIUM_PRIORITY
            EventPriority.LOW -> DefaultColorType.LOW_PRIORITY
            EventPriority.NORMAL -> DefaultColorType.PRIMARY
        }
        
        return try {
            when {
                colorString.isNullOrBlank() -> {
                    // Use deterministic color based on event ID if available
                    eventId?.let { id ->
                        Log.w(TAG, "Color string is null/empty for event $id, using deterministic color")
                        getEventColorByIndex(id.hashCode())
                    } ?: run {
                        Log.w(TAG, "Color string is null/empty, using priority-based fallback: ${priority.name.lowercase()}")
                        getThemeAwareDefaultColor(fallbackType)
                    }
                }
                
                isValidColorString(colorString) -> {
                    parseValidatedColor(colorString) ?: run {
                        Log.w(TAG, "Failed to parse validated color: $colorString for event $eventId")
                        eventId?.let { getEventColorByIndex(it.hashCode()) } 
                            ?: getThemeAwareDefaultColor(fallbackType)
                    }
                }
                
                else -> {
                    Log.w(TAG, "Invalid color format: $colorString for event $eventId")
                    eventId?.let { getEventColorByIndex(it.hashCode()) } 
                        ?: getThemeAwareDefaultColor(fallbackType)
                }
            }
        } catch (e: Exception) {
            Log.e(TAG, "Unexpected error parsing color: $colorString for event $eventId", e)
            eventId?.let { getEventColorByIndex(it.hashCode()) } 
                ?: getThemeAwareDefaultColor(fallbackType)
        }
    }
    
    /**
     * Enum for event priority levels to determine appropriate fallback colors.
     */
    enum class EventPriority {
        HIGH,
        MEDIUM,
        NORMAL,
        LOW
    }
    
    /**
     * Validates if a color string is in a supported format with comprehensive checks.
     * 
     * @param colorString The color string to validate
     * @return true if the color string is valid, false otherwise
     */
    fun isValidColorString(colorString: String?): Boolean {
        // Null and empty validation
        if (colorString == null) {
            Log.d(TAG, "Color validation failed: null input")
            return false
        }
        
        if (colorString.isBlank()) {
            Log.d(TAG, "Color validation failed: blank input")
            return false
        }
        
        // Length validation to prevent DoS attacks
        if (colorString.length > MAX_COLOR_STRING_LENGTH) {
            Log.w(TAG, "Color validation failed: string too long (${colorString.length} > $MAX_COLOR_STRING_LENGTH)")
            return false
        }
        
        val trimmed = colorString.trim()
        
        // Check for suspicious characters that could indicate injection attempts
        if (containsSuspiciousCharacters(trimmed)) {
            Log.w(TAG, "Color validation failed: suspicious characters detected")
            return false
        }
        
        val normalized = trimmed.lowercase()
        
        return when {
            // Validate named colors with pattern matching
            isValidNamedColor(normalized) -> {
                Log.d(TAG, "Valid named color: $normalized")
                true
            }
            
            // Validate hex colors with regex patterns
            isValidHexColor(normalized) -> {
                Log.d(TAG, "Valid hex color: $normalized")
                true
            }
            
            else -> {
                Log.d(TAG, "Color validation failed: unsupported format '$colorString'")
                false
            }
        }
    }
    
    /**
     * Validates named color format and checks if it exists in our supported colors.
     * 
     * @param colorName The normalized (lowercase, trimmed) color name
     * @return true if valid named color, false otherwise
     */
    private fun isValidNamedColor(colorName: String): Boolean {
        // Check if it matches named color pattern (letters, numbers, underscores)
        if (!namedColorPattern.matches(colorName)) {
            return false
        }
        
        // Check if it's in our supported named colors
        return namedColors.containsKey(colorName)
    }
    
    /**
     * Checks for suspicious characters that could indicate security issues.
     * 
     * @param input The input string to check
     * @return true if suspicious characters found, false otherwise
     */
    private fun containsSuspiciousCharacters(input: String): Boolean {
        // Check for control characters, scripts, or unusual Unicode
        return input.any { char ->
            char.isISOControl() || 
            char.code > 127 && !char.isLetterOrDigit() ||
            char in listOf('<', '>', '"', '\'', '&', ';', '(', ')', '{', '}', '[', ']')
        }
    }
    
    /**
     * Returns the default color for events using Material Design primary color.
     * This is the primary fallback color when color parsing fails.
     * 
     * @return The default event color with good accessibility contrast
     */
    fun getDefaultEventColor(): Color {
        return CalendarPrimary
    }
    
    /**
     * Returns a theme-aware default color based on the specified context.
     * Provides different default colors for different use cases while maintaining consistency.
     * 
     * @param colorType The type of color needed (PRIMARY, SECONDARY, etc.)
     * @return A theme-appropriate default color
     */
    fun getThemeAwareDefaultColor(colorType: DefaultColorType = DefaultColorType.PRIMARY): Color {
        return when (colorType) {
            DefaultColorType.PRIMARY -> CalendarPrimary
            DefaultColorType.SECONDARY -> CalendarSecondary
            DefaultColorType.TERTIARY -> CalendarTertiary
            DefaultColorType.HIGH_PRIORITY -> EventHighPriority
            DefaultColorType.MEDIUM_PRIORITY -> EventMediumPriority
            DefaultColorType.LOW_PRIORITY -> EventLowPriority
        }
    }
    
    /**
     * Returns a random color from the predefined event colors palette.
     * Useful for assigning colors to new events while maintaining visual consistency.
     * 
     * @return A color from the EventColors palette
     */
    fun getRandomEventColor(): Color {
        if (EventColors.isEmpty()) {
            Log.w(TAG, "EventColors is empty, using default color")
            return getDefaultEventColor()
        }
        return EventColors.random()
    }
    
    /**
     * Returns a color from the event colors palette by index.
     * Useful for consistent color assignment based on event properties.
     * 
     * @param index The index in the EventColors palette (will be wrapped if out of bounds)
     * @return A color from the EventColors palette
     */
    fun getEventColorByIndex(index: Int): Color {
        if (EventColors.isEmpty()) {
            Log.w(TAG, "EventColors is empty, using default color")
            return getDefaultEventColor()
        }
        val safeIndex = index % EventColors.size
        return EventColors[safeIndex]
    }
    
    /**
     * Validates that a color meets accessibility contrast requirements.
     * Uses WCAG 2.1 guidelines for color contrast ratios.
     * 
     * @param backgroundColor The background color
     * @param textColor The text/foreground color
     * @param level The accessibility level (AA or AAA)
     * @return true if the contrast ratio meets the specified level
     */
    fun meetsAccessibilityContrast(
        backgroundColor: Color,
        textColor: Color,
        level: AccessibilityLevel = AccessibilityLevel.AA
    ): Boolean {
        val contrastRatio = calculateContrastRatio(backgroundColor, textColor)
        return when (level) {
            AccessibilityLevel.AA -> contrastRatio >= 4.5f
            AccessibilityLevel.AAA -> contrastRatio >= 7.0f
        }
    }
    
    /**
     * Calculates the contrast ratio between two colors according to WCAG guidelines.
     * 
     * @param color1 First color
     * @param color2 Second color
     * @return The contrast ratio (1.0 to 21.0)
     */
    fun calculateContrastRatio(color1: Color, color2: Color): Float {
        val luminance1 = color1.luminance()
        val luminance2 = color2.luminance()
        
        val lighter = maxOf(luminance1, luminance2)
        val darker = minOf(luminance1, luminance2)
        
        return (lighter + 0.05f) / (darker + 0.05f)
    }
    
    /**
     * Returns an appropriate text color (black or white) for the given background color
     * to ensure good accessibility contrast.
     * 
     * @param backgroundColor The background color
     * @return Color.Black or Color.White for optimal contrast
     */
    fun getContrastingTextColor(backgroundColor: Color): Color {
        return if (backgroundColor.luminance() > 0.5f) {
            Color.Black
        } else {
            Color.White
        }
    }
    
    /**
     * Validates color consistency across different calendar views.
     * Ensures that the same event maintains the same color representation.
     * 
     * @param eventId Unique identifier for the event
     * @param proposedColor The color being proposed for the event
     * @return ValidationResult indicating if the color is consistent
     */
    fun validateColorConsistency(eventId: String?, proposedColor: Color?): ConsistencyResult {
        // For now, we'll implement basic validation
        // In a full implementation, this would check against a color registry
        return when {
            eventId.isNullOrBlank() -> ConsistencyResult(
                isConsistent = false,
                recommendedColor = getDefaultEventColor(),
                reason = "Event ID is required for consistency validation"
            )
            proposedColor == null -> ConsistencyResult(
                isConsistent = false,
                recommendedColor = getEventColorByIndex(eventId.hashCode()),
                reason = "Proposed color is null, using deterministic color based on event ID"
            )
            else -> ConsistencyResult(
                isConsistent = true,
                recommendedColor = proposedColor,
                reason = "Color is valid and consistent"
            )
        }
    }
    
    /**
     * Enum for different types of default colors based on context.
     */
    enum class DefaultColorType {
        PRIMARY,
        SECONDARY,
        TERTIARY,
        HIGH_PRIORITY,
        MEDIUM_PRIORITY,
        LOW_PRIORITY
    }
    
    /**
     * Enum for accessibility contrast levels.
     */
    enum class AccessibilityLevel {
        AA,   // WCAG 2.1 Level AA (4.5:1 contrast ratio)
        AAA   // WCAG 2.1 Level AAA (7.0:1 contrast ratio)
    }
    
    /**
     * Ensures color consistency across different calendar views (month, week, day, agenda).
     * This function validates that the same event maintains consistent color representation
     * regardless of which calendar view is being displayed.
     * 
     * @param eventId The unique identifier for the event
     * @param currentColor The color currently being used for the event
     * @param viewType The type of calendar view where the color will be displayed
     * @return A consistent color that works well across all calendar views
     */
    fun ensureViewConsistency(
        eventId: String,
        currentColor: Color?,
        viewType: CalendarViewType = CalendarViewType.MONTH
    ): Color {
        // If we have a valid current color, validate it meets accessibility requirements
        currentColor?.let { color ->
            val backgroundColor = when (viewType) {
                CalendarViewType.MONTH -> Color.White // Typical month view background
                CalendarViewType.WEEK -> Color.White  // Week view background
                CalendarViewType.DAY -> Color.White   // Day view background
                CalendarViewType.AGENDA -> Color.White // Agenda view background
            }
            
            // Check if the color has good contrast for the view type
            if (meetsAccessibilityContrast(backgroundColor, color)) {
                return color
            } else {
                Log.w(TAG, "Color $color doesn't meet accessibility requirements for $viewType view, using fallback")
            }
        }
        
        // Use deterministic color based on event ID for consistency
        return getEventColorByIndex(eventId.hashCode())
    }
    
    /**
     * Gets the appropriate default color for a specific calendar view context.
     * Different views may need different default colors for optimal visibility.
     * 
     * @param viewType The type of calendar view
     * @param isHighlighted Whether this is a highlighted/selected state
     * @return The appropriate default color for the view context
     */
    fun getViewContextDefaultColor(
        viewType: CalendarViewType,
        isHighlighted: Boolean = false
    ): Color {
        return when {
            isHighlighted -> getThemeAwareDefaultColor(DefaultColorType.HIGH_PRIORITY)
            viewType == CalendarViewType.AGENDA -> getThemeAwareDefaultColor(DefaultColorType.SECONDARY)
            else -> getThemeAwareDefaultColor(DefaultColorType.PRIMARY)
        }
    }
    
    /**
     * Validates that all default colors meet accessibility contrast requirements.
     * This ensures that the fallback color system provides accessible colors.
     * 
     * @return A report of accessibility compliance for all default colors
     */
    fun validateDefaultColorAccessibility(): AccessibilityReport {
        val backgroundColor = Color.White // Common background color
        val results = mutableMapOf<DefaultColorType, Boolean>()
        
        DefaultColorType.values().forEach { colorType ->
            val color = getThemeAwareDefaultColor(colorType)
            val meetsAA = meetsAccessibilityContrast(backgroundColor, color, AccessibilityLevel.AA)
            results[colorType] = meetsAA
            
            if (!meetsAA) {
                Log.w(TAG, "Default color for $colorType doesn't meet AA accessibility standards")
            }
        }
        
        return AccessibilityReport(
            allColorsCompliant = results.values.all { it },
            individualResults = results.toMap(),
            recommendedImprovements = results.filterValues { !it }.keys.toList()
        )
    }
    
    /**
     * Enum for different calendar view types to ensure color consistency.
     */
    enum class CalendarViewType {
        MONTH,
        WEEK,
        DAY,
        AGENDA
    }
    
    /**
     * Data class for accessibility compliance reporting.
     */
    data class AccessibilityReport(
        val allColorsCompliant: Boolean,
        val individualResults: Map<DefaultColorType, Boolean>,
        val recommendedImprovements: List<DefaultColorType>
    )
    
    /**
     * Data class for color consistency validation results.
     */
    data class ConsistencyResult(
        val isConsistent: Boolean,
        val recommendedColor: Color,
        val reason: String
    )
    
    /**
     * Validates color string with detailed error reporting.
     * Useful for debugging and providing specific validation feedback.
     * 
     * @param colorString The color string to validate
     * @return ValidationResult with success status and error details
     */
    fun validateColorWithDetails(colorString: String?): ValidationResult {
        when {
            colorString == null -> return ValidationResult(false, "Color string is null")
            colorString.isBlank() -> return ValidationResult(false, "Color string is empty or blank")
            colorString.length > MAX_COLOR_STRING_LENGTH -> 
                return ValidationResult(false, "Color string too long: ${colorString.length} characters")
        }
        
        val trimmed = colorString.trim()
        
        if (containsSuspiciousCharacters(trimmed)) {
            return ValidationResult(false, "Color string contains invalid characters")
        }
        
        val normalized = trimmed.lowercase()
        
        return when {
            isValidNamedColor(normalized) -> ValidationResult(true, "Valid named color")
            isValidHexColor(normalized) -> ValidationResult(true, "Valid hex color")
            normalized.startsWith("#") -> ValidationResult(false, "Invalid hex color format")
            else -> ValidationResult(false, "Unsupported color format")
        }
    }
    
    /**
     * Data class for detailed validation results.
     */
    data class ValidationResult(
        val isValid: Boolean,
        val message: String
    )
    
    /**
     * Validates hex color format using comprehensive regex patterns.
     * Supports: #RGB, #ARGB, #RRGGBB, #AARRGGBB
     * 
     * @param colorString The color string to validate (should be normalized)
     * @return true if valid hex color format, false otherwise
     */
    private fun isValidHexColor(colorString: String): Boolean {
        // Must start with #
        if (!colorString.startsWith("#")) {
            return false
        }
        
        // Check against each supported hex pattern
        return hexColorPatterns.values.any { pattern ->
            pattern.matches(colorString)
        }
    }
    
    /**
     * Additional validation for edge cases in hex colors.
     * 
     * @param hex The hex part without the # symbol
     * @return true if hex string is valid, false otherwise
     */
    private fun isValidHexString(hex: String): Boolean {
        // Empty hex string
        if (hex.isEmpty()) return false
        
        // Check for valid length
        if (hex.length !in listOf(3, 4, 6, 8)) return false
        
        // Check each character is valid hex digit
        return hex.all { char ->
            char.isDigit() || char.lowercaseChar() in 'a'..'f'
        }
    }
    
    /**
     * Parses a validated color string to Color object.
     * Should only be called after isValidColorString returns true.
     */
    private fun parseValidatedColor(colorString: String): Color? {
        val trimmed = colorString.trim().lowercase()
        
        return try {
            when {
                // Parse named colors
                namedColors.containsKey(trimmed) -> namedColors[trimmed]
                
                // Parse hex colors
                trimmed.startsWith("#") -> parseHexColor(trimmed)
                
                else -> null
            }
        } catch (e: Exception) {
            Log.w(TAG, "Error parsing validated color: $colorString", e)
            null
        }
    }
    
    /**
     * Parses hex color strings to Color objects.
     * Supports #RGB, #ARGB, #RRGGBB, #AARRGGBB formats.
     */
    private fun parseHexColor(hex: String): Color? {
        return try {
            val colorValue = hex.substring(1) // Remove #
            
            when (colorValue.length) {
                3 -> {
                    // #RGB -> #RRGGBB
                    val r = colorValue[0].toString().repeat(2)
                    val g = colorValue[1].toString().repeat(2)
                    val b = colorValue[2].toString().repeat(2)
                    Color(android.graphics.Color.parseColor("#$r$g$b"))
                }
                
                4 -> {
                    // #ARGB -> #AARRGGBB
                    val a = colorValue[0].toString().repeat(2)
                    val r = colorValue[1].toString().repeat(2)
                    val g = colorValue[2].toString().repeat(2)
                    val b = colorValue[3].toString().repeat(2)
                    Color(android.graphics.Color.parseColor("#$a$r$g$b"))
                }
                
                6 -> {
                    // #RRGGBB
                    Color(android.graphics.Color.parseColor(hex))
                }
                
                8 -> {
                    // #AARRGGBB
                    Color(android.graphics.Color.parseColor(hex))
                }
                
                else -> null
            }
        } catch (e: Exception) {
            Log.w(TAG, "Error parsing hex color: $hex", e)
            null
        }
    }
}