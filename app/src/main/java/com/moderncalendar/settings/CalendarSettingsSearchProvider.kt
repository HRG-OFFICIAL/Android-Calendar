package com.moderncalendar.settings

import android.content.SearchRecentSuggestionsProvider

class CalendarSettingsSearchProvider : SearchRecentSuggestionsProvider() {
    
    init {
        setupSuggestions(AUTHORITY, MODE)
    }
    
    companion object {
        const val AUTHORITY = "com.moderncalendar.settings.CalendarSettingsSearchProvider"
        const val MODE = DATABASE_MODE_QUERIES
    }
}
