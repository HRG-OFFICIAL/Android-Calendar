package com.moderncalendar.core.analytics

import android.content.Context
import com.google.firebase.analytics.FirebaseAnalytics
import com.google.firebase.crashlytics.FirebaseCrashlytics
import dagger.hilt.android.qualifiers.ApplicationContext
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class AnalyticsManager @Inject constructor(
    @ApplicationContext private val context: Context,
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
    
    // Screen tracking
    fun trackScreenView(screenName: String, screenClass: String? = null) {
        val parameters = mapOf(
            FirebaseAnalytics.Param.SCREEN_NAME to screenName,
            FirebaseAnalytics.Param.SCREEN_CLASS to (screenClass ?: screenName)
        )
        trackEvent(FirebaseAnalytics.Event.SCREEN_VIEW, parameters)
    }
    
    // User properties
    fun setUserProperty(name: String, value: String) {
        firebaseAnalytics.setUserProperty(name, value)
    }
    
    fun setUserId(userId: String) {
        firebaseAnalytics.setUserId(userId)
        crashlytics.setUserId(userId)
    }
    
    // Crash reporting
    fun logException(throwable: Throwable) {
        crashlytics.recordException(throwable)
    }
    
    fun log(message: String) {
        crashlytics.log(message)
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
    
    // Calendar specific events
    fun trackEventCreated(eventId: String, eventType: String, isRecurring: Boolean) {
        trackEvent("event_created", mapOf(
            "event_id" to eventId,
            "event_type" to eventType,
            "is_recurring" to isRecurring
        ))
    }
    
    fun trackEventEdited(eventId: String, editType: String) {
        trackEvent("event_edited", mapOf(
            "event_id" to eventId,
            "edit_type" to editType
        ))
    }
    
    fun trackEventDeleted(eventId: String) {
        trackEvent("event_deleted", mapOf(
            "event_id" to eventId
        ))
    }
    
    fun trackCalendarViewChanged(viewType: String) {
        trackEvent("calendar_view_changed", mapOf(
            "view_type" to viewType
        ))
    }
    
    fun trackDateSelected(date: String) {
        trackEvent("date_selected", mapOf(
            "selected_date" to date
        ))
    }
    
    fun trackSearchPerformed(query: String, resultCount: Int) {
        trackEvent("search_performed", mapOf(
            "search_query" to query,
            "result_count" to resultCount
        ))
    }
    
    fun trackReminderSet(eventId: String, reminderMinutes: Int) {
        trackEvent("reminder_set", mapOf(
            "event_id" to eventId,
            "reminder_minutes" to reminderMinutes
        ))
    }
    
    fun trackSyncPerformed(syncType: String, success: Boolean, itemCount: Int) {
        trackEvent("sync_performed", mapOf(
            "sync_type" to syncType,
            "success" to success,
            "item_count" to itemCount
        ))
    }
    
    fun trackAppOpened() {
        trackEvent("app_opened", emptyMap())
    }
    
    fun trackAppBackgrounded() {
        trackEvent("app_backgrounded", emptyMap())
    }
    
    fun trackFeatureUsed(featureName: String) {
        trackEvent("feature_used", mapOf(
            "feature_name" to featureName
        ))
    }
    
    fun trackError(errorType: String, errorMessage: String, screen: String) {
        trackEvent("error_occurred", mapOf(
            "error_type" to errorType,
            "error_message" to errorMessage,
            "screen" to screen
        ))
        
        // Also log to crashlytics
        log("Error: $errorType - $errorMessage on screen: $screen")
    }
}
