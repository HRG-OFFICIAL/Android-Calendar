package com.moderncalendar.core.analytics

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
    fun trackEvent(eventName: String, parameters: Map<String, Any> = emptyMap()) {
        val bundle = android.os.Bundle().apply {
            parameters.forEach { (key, value) ->
                when (value) {
                    is String -> putString(key, value)
                    is Int -> putInt(key, value)
                    is Long -> putLong(key, value)
                    is Double -> putDouble(key, value)
                    is Boolean -> putBoolean(key, value)
                    else -> putString(key, value.toString())
                }
            }
        }
        firebaseAnalytics.logEvent(eventName, bundle)
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
        val parameters = mutableMapOf<String, Any>(
            FirebaseAnalytics.Param.SCREEN_NAME to screenName
        )
        screenClass?.let { parameters[FirebaseAnalytics.Param.SCREEN_CLASS] = it }
        trackEvent(FirebaseAnalytics.Event.SCREEN_VIEW, parameters)
    }
    
    // Calendar specific events
    fun trackEventCreated(eventType: String, isRecurring: Boolean) {
        trackEvent("event_created", mapOf(
            "event_type" to eventType,
            "is_recurring" to isRecurring
        ))
    }
    
    fun trackEventEdited(eventType: String) {
        trackEvent("event_edited", mapOf(
            "event_type" to eventType
        ))
    }
    
    fun trackEventDeleted(eventType: String) {
        trackEvent("event_deleted", mapOf(
            "event_type" to eventType
        ))
    }
    
    fun trackCalendarViewChanged(viewType: String) {
        trackEvent("calendar_view_changed", mapOf(
            "view_type" to viewType
        ))
    }
    
    fun trackSearchPerformed(query: String, resultCount: Int) {
        trackEvent("search_performed", mapOf(
            "search_query" to query,
            "result_count" to resultCount
        ))
    }
    
    fun trackReminderSet(reminderMinutes: Int) {
        trackEvent("reminder_set", mapOf(
            "reminder_minutes" to reminderMinutes
        ))
    }
    
    fun trackThemeChanged(isDarkMode: Boolean) {
        trackEvent("theme_changed", mapOf(
            "is_dark_mode" to isDarkMode
        ))
    }
    
    fun trackUserSignedIn(method: String) {
        trackEvent("user_signed_in", mapOf(
            "sign_in_method" to method
        ))
    }
    
    fun trackUserSignedUp(method: String) {
        trackEvent("user_signed_up", mapOf(
            "sign_up_method" to method
        ))
    }
    
    fun trackSyncPerformed(syncType: String, itemCount: Int) {
        trackEvent("sync_performed", mapOf(
            "sync_type" to syncType,
            "item_count" to itemCount
        ))
    }
    
    // Error tracking
    fun trackError(error: Throwable, context: String) {
        crashlytics.recordException(error)
        crashlytics.setCustomKey("error_context", context)
    }
    
    fun setCustomKey(key: String, value: String) {
        crashlytics.setCustomKey(key, value)
    }
    
    fun setCustomKey(key: String, value: Int) {
        crashlytics.setCustomKey(key, value)
    }
    
    fun setCustomKey(key: String, value: Boolean) {
        crashlytics.setCustomKey(key, value)
    }
}
