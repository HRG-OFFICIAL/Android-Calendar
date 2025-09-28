package com.moderncalendar.core.accessibility

import android.content.Context
import android.view.accessibility.AccessibilityManager
import androidx.compose.ui.semantics.*
import dagger.hilt.android.qualifiers.ApplicationContext
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class AccessibilityManager @Inject constructor(
    @ApplicationContext private val context: Context
) {
    
    private val accessibilityManager = context.getSystemService(Context.ACCESSIBILITY_SERVICE) as AccessibilityManager
    
    fun isAccessibilityEnabled(): Boolean {
        return accessibilityManager.isEnabled
    }
    
    fun isTalkBackEnabled(): Boolean {
        return accessibilityManager.isTouchExplorationEnabled
    }
    
    fun isSwitchAccessEnabled(): Boolean {
        return accessibilityManager.isEnabled
    }
    
    fun getAccessibilityServices(): List<String> {
        return accessibilityManager.getEnabledAccessibilityServiceList(
            android.accessibilityservice.AccessibilityServiceInfo.FEEDBACK_ALL_MASK
        ).map { it.resolveInfo.serviceInfo.packageName }
    }
    
    fun isScreenReaderEnabled(): Boolean {
        return accessibilityManager.isTouchExplorationEnabled
    }
    
    fun isVoiceOverEnabled(): Boolean {
        return accessibilityManager.isEnabled
    }
    
    fun getAccessibilityServiceInfo(): List<android.accessibilityservice.AccessibilityServiceInfo> {
        return accessibilityManager.getEnabledAccessibilityServiceList(
            android.accessibilityservice.AccessibilityServiceInfo.FEEDBACK_ALL_MASK
        )
    }
    
    fun isAccessibilityServiceEnabled(serviceName: String): Boolean {
        return getAccessibilityServices().contains(serviceName)
    }
    
    fun getAccessibilityCapabilities(): Set<String> {
        val capabilities = mutableSetOf<String>()
        
        if (isTalkBackEnabled()) {
            capabilities.add("TalkBack")
        }
        
        if (isSwitchAccessEnabled()) {
            capabilities.add("Switch Access")
        }
        
        if (isScreenReaderEnabled()) {
            capabilities.add("Screen Reader")
        }
        
        return capabilities
    }
    
    fun announceForAccessibility(text: String) {
        // This would typically be implemented with a custom view or accessibility service
        // For now, we'll just log it
        android.util.Log.d("AccessibilityManager", "Announcing: $text")
    }
}