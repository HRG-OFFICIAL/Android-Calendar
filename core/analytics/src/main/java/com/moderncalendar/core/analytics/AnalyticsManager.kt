package com.moderncalendar.core.analytics

import android.os.Bundle
import com.google.firebase.analytics.FirebaseAnalytics
import com.google.firebase.crashlytics.FirebaseCrashlytics
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class AnalyticsManager @Inject constructor(
    private val firebaseAnalytics: FirebaseAnalytics,
    private val crashlytics: FirebaseCrashlytics
) {
    
    // Event tracking
    fun trackEvent(eventName: String, parameters: Bundle? = null) {
        firebaseAnalytics.logEvent(eventName, parameters)
    }
    
    fun trackEvent(eventName: String, vararg params: Pair<String, Any>) {
        val bundle = Bundle().apply {
            params.forEach { (key, value) ->
                when (value) {
                    is String -> putString(key, value)
                    is Int -> putInt(key, value)
                    is Long -> putLong(key, value)
                    is Double -> putDouble(key, value)
                    is Float -> putFloat(key, value)
                    is Boolean -> putBoolean(key, value)
                    else -> putString(key, value.toString())
                }
            }
        }
        trackEvent(eventName, bundle)
    }
    
    // User properties
    fun setUserProperty(name: String, value: String) {
        firebaseAnalytics.setUserProperty(name, value)
    }
    
    fun setUserId(userId: String) {
        firebaseAnalytics.setUserId(userId)
        crashlytics.setUserId(userId)
    }
    
    // Screen tracking
    fun trackScreenView(screenName: String, screenClass: String? = null) {
        val bundle = Bundle().apply {
            putString(FirebaseAnalytics.Param.SCREEN_NAME, screenName)
            screenClass?.let { putString(FirebaseAnalytics.Param.SCREEN_CLASS, it) }
        }
        trackEvent(FirebaseAnalytics.Event.SCREEN_VIEW, bundle)
    }
    
    // Calendar specific events
    fun trackEventCreated(eventId: String, eventType: String, isAllDay: Boolean) {
        trackEvent("event_created", Bundle().apply {
            putString("event_id", eventId)
            putString("event_type", eventType)
            putBoolean("is_all_day", isAllDay)
        })
    }
    
    fun trackEventUpdated(eventId: String, updateType: String) {
        trackEvent("event_updated", Bundle().apply {
            putString("event_id", eventId)
            putString("update_type", updateType)
        })
    }
    
    fun trackEventDeleted(eventId: String, deletionReason: String) {
        trackEvent("event_deleted", Bundle().apply {
            putString("event_id", eventId)
            putString("deletion_reason", deletionReason)
        })
    }
    
    fun trackEventSearched(searchQuery: String, resultCount: Int) {
        trackEvent("event_searched", Bundle().apply {
            putString("search_query", searchQuery)
            putInt("result_count", resultCount)
        })
    }
    
    fun trackReminderSet(eventId: String, reminderMinutes: Int) {
        trackEvent("reminder_set", Bundle().apply {
            putString("event_id", eventId)
            putInt("reminder_minutes", reminderMinutes)
        })
    }
    
    fun trackReminderTriggered(eventId: String, reminderMinutes: Int) {
        trackEvent("reminder_triggered", Bundle().apply {
            putString("event_id", eventId)
            putInt("reminder_minutes", reminderMinutes)
        })
    }
    
    fun trackReminderSnoozed(eventId: String, snoozeMinutes: Int) {
        trackEvent("reminder_snoozed", Bundle().apply {
            putString("event_id", eventId)
            putInt("snooze_minutes", snoozeMinutes)
        })
    }
    
    fun trackReminderDismissed(eventId: String) {
        trackEvent("reminder_dismissed", Bundle().apply {
            putString("event_id", eventId)
        })
    }
    
    // Authentication events
    fun trackSignIn(method: String, success: Boolean) {
        trackEvent("sign_in", Bundle().apply {
            putString("method", method)
            putBoolean("success", success)
        })
    }
    
    fun trackSignUp(method: String, success: Boolean) {
        trackEvent("sign_up", Bundle().apply {
            putString("method", method)
            putBoolean("success", success)
        })
    }
    
    fun trackSignOut() {
        trackEvent("sign_out")
    }
    
    // Settings events
    fun trackSettingsChanged(settingName: String, oldValue: String, newValue: String) {
        trackEvent("settings_changed", Bundle().apply {
            putString("setting_name", settingName)
            putString("old_value", oldValue)
            putString("new_value", newValue)
        })
    }
    
    fun trackThemeChanged(themeName: String) {
        trackEvent("theme_changed", Bundle().apply {
            putString("theme_name", themeName)
        })
    }
    
    fun trackNotificationSettingsChanged(enabled: Boolean) {
        trackEvent("notification_settings_changed", Bundle().apply {
            putBoolean("notifications_enabled", enabled)
        })
    }
    
    // Performance events
    fun trackAppStartup(duration: Long) {
        trackEvent("app_startup", Bundle().apply {
            putLong("duration_ms", duration)
        })
    }
    
    fun trackScreenLoad(screenName: String, duration: Long) {
        trackEvent("screen_load", Bundle().apply {
            putString("screen_name", screenName)
            putLong("duration_ms", duration)
        })
    }
    
    fun trackDatabaseOperation(operation: String, duration: Long, success: Boolean) {
        trackEvent("database_operation", Bundle().apply {
            putString("operation", operation)
            putLong("duration_ms", duration)
            putBoolean("success", success)
        })
    }
    
    // Error tracking
    fun trackError(error: String, errorCode: String? = null, userId: String? = null) {
        crashlytics.log("Error: $error")
        errorCode?.let { crashlytics.setCustomKey("error_code", it) }
        userId?.let { crashlytics.setUserId(it) }
        
        trackEvent("error_occurred", Bundle().apply {
            putString("error_message", error)
            errorCode?.let { putString("error_code", it) }
        })
    }
    
    fun trackException(exception: Throwable, context: String? = null) {
        context?.let { crashlytics.setCustomKey("context", it) }
        crashlytics.recordException(exception)
    }
    
    // User engagement
    fun trackUserEngagement(action: String, screenName: String) {
        trackEvent("user_engagement", Bundle().apply {
            putString("action", action)
            putString("screen_name", screenName)
        })
    }
    
    fun trackFeatureUsed(featureName: String, usageCount: Int = 1) {
        trackEvent("feature_used", Bundle().apply {
            putString("feature_name", featureName)
            putInt("usage_count", usageCount)
        })
    }
    
    // App lifecycle
    fun trackAppBackgrounded() {
        trackEvent("app_backgrounded")
    }
    
    fun trackAppForegrounded() {
        trackEvent("app_foregrounded")
    }
    
    fun trackAppCrashed() {
        trackEvent("app_crashed")
    }
    
    // Custom events
    fun trackCustomEvent(eventName: String, vararg params: Pair<String, Any>) {
        trackEvent(eventName, *params)
    }
    
    // Set custom keys for crashlytics
    fun setCustomKey(key: String, value: String) {
        crashlytics.setCustomKey(key, value)
    }
    
    fun setCustomKey(key: String, value: Int) {
        crashlytics.setCustomKey(key, value)
    }
    
    fun setCustomKey(key: String, value: Boolean) {
        crashlytics.setCustomKey(key, value)
    }
    
    fun setCustomKey(key: String, value: Float) {
        crashlytics.setCustomKey(key, value)
    }
}