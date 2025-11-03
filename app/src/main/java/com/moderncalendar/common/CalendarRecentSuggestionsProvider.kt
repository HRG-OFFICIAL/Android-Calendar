package com.moderncalendar.common

import android.content.SearchRecentSuggestionsProvider

class CalendarRecentSuggestionsProvider : SearchRecentSuggestionsProvider() {
    companion object {
        const val AUTHORITY = "com.moderncalendar.common.CalendarRecentSuggestionsProvider"
        const val MODE = DATABASE_MODE_QUERIES
    }

    init {
        setupSuggestions(AUTHORITY, MODE)
    }
}
