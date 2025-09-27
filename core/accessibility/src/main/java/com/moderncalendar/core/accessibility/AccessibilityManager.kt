package com.moderncalendar.core.accessibility

import android.content.Context
import android.view.accessibility.AccessibilityManager
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class AccessibilityManager @Inject constructor(
    private val context: Context
) {
    
    private val accessibilityManager = context.getSystemService(Context.ACCESSIBILITY_SERVICE) as AccessibilityManager
    
    fun isAccessibilityEnabled(): Boolean {
        return accessibilityManager.isEnabled
    }
    
    fun isTalkBackEnabled(): Boolean {
        return accessibilityManager.isTouchExplorationEnabled
    }
    
    fun getAccessibilityServices(): List<String> {
        return accessibilityManager.getEnabledAccessibilityServiceList(
            android.accessibilityservice.AccessibilityServiceInfo.FEEDBACK_ALL_MASK
        ).map { it.id }
    }
    
    fun isHighContrastEnabled(): Boolean {
        // Check if high contrast is enabled (this is a simplified check)
        return context.resources.configuration.uiMode and 
                android.content.res.Configuration.UI_MODE_NIGHT_MASK == 
                android.content.res.Configuration.UI_MODE_NIGHT_YES
    }
    
    fun isLargeTextEnabled(): Boolean {
        val fontScale = context.resources.configuration.fontScale
        return fontScale > 1.0f
    }
    
    fun getAccessibilitySettings(): AccessibilitySettings {
        return AccessibilitySettings(
            isAccessibilityEnabled = isAccessibilityEnabled(),
            isTalkBackEnabled = isTalkBackEnabled(),
            isHighContrastEnabled = isHighContrastEnabled(),
            isLargeTextEnabled = isLargeTextEnabled(),
            fontScale = context.resources.configuration.fontScale,
            enabledServices = getAccessibilityServices()
        )
    }
    
    fun shouldUseHighContrast(): Boolean {
        return isAccessibilityEnabled() && (isHighContrastEnabled() || isTalkBackEnabled())
    }
    
    fun shouldUseLargeText(): Boolean {
        return isAccessibilityEnabled() && isLargeTextEnabled()
    }
    
    fun getRecommendedTextSize(): Float {
        val baseSize = 14f
        val fontScale = context.resources.configuration.fontScale
        return baseSize * fontScale
    }
    
    fun getRecommendedIconSize(): Int {
        val baseSize = 24
        val fontScale = context.resources.configuration.fontScale
        return (baseSize * fontScale).toInt()
    }
    
    fun getRecommendedTouchTargetSize(): Int {
        val baseSize = 48
        val fontScale = context.resources.configuration.fontScale
        return (baseSize * fontScale).toInt()
    }
}

data class AccessibilitySettings(
    val isAccessibilityEnabled: Boolean,
    val isTalkBackEnabled: Boolean,
    val isHighContrastEnabled: Boolean,
    val isLargeTextEnabled: Boolean,
    val fontScale: Float,
    val enabledServices: List<String>
)
