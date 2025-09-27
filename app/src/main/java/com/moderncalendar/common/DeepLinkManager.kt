package com.moderncalendar.common

import android.content.Context
import android.content.Intent
import android.net.Uri
import com.moderncalendar.MainActivity
import com.moderncalendar.feature.events.EventInfoActivity
import com.moderncalendar.feature.events.EventEditActivity
import com.moderncalendar.feature.search.EventSearchActivity
import com.moderncalendar.feature.settings.CalendarSettingsActivity
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class DeepLinkManager @Inject constructor() {
    
    fun handleDeepLink(context: Context, uri: Uri): Intent? {
        return when {
            // Custom scheme: moderncalendar://
            uri.scheme == "moderncalendar" -> handleCustomScheme(context, uri)
            
            // HTTP/HTTPS schemes: https://calendar.moderncalendar.com/
            uri.scheme == "http" || uri.scheme == "https" -> handleWebScheme(context, uri)
            
            // Content scheme: content://com.moderncalendar/event/123
            uri.scheme == "content" -> handleContentScheme(context, uri)
            
            // Webcal schemes: webcal:// or webcals://
            uri.scheme == "webcal" || uri.scheme == "webcals" -> handleWebcalScheme(context, uri)
            
            else -> null
        }
    }
    
    private fun handleCustomScheme(context: Context, uri: Uri): Intent? {
        return when (uri.host) {
            "event" -> {
                val eventId = uri.pathSegments.lastOrNull()
                if (eventId != null) {
                    Intent(context, EventInfoActivity::class.java).apply {
                        putExtra("event_id", eventId)
                    }
                } else {
                    Intent(context, MainActivity::class.java)
                }
            }
            "create" -> {
                Intent(context, EventEditActivity::class.java).apply {
                    action = Intent.ACTION_INSERT
                }
            }
            "search" -> {
                val query = uri.getQueryParameter("q")
                Intent(context, EventSearchActivity::class.java).apply {
                    putExtra("search_query", query)
                }
            }
            "settings" -> {
                Intent(context, CalendarSettingsActivity::class.java)
            }
            else -> {
                Intent(context, MainActivity::class.java)
            }
        }
    }
    
    private fun handleWebScheme(context: Context, uri: Uri): Intent? {
        return when {
            uri.host == "calendar.moderncalendar.com" -> {
                when {
                    uri.path?.startsWith("/event/") == true -> {
                        val eventId = uri.path?.substringAfter("/event/")
                        if (eventId != null) {
                            Intent(context, EventInfoActivity::class.java).apply {
                                putExtra("event_id", eventId)
                            }
                        } else {
                            Intent(context, MainActivity::class.java)
                        }
                    }
                    uri.path?.startsWith("/create") == true -> {
                        Intent(context, EventEditActivity::class.java).apply {
                            action = Intent.ACTION_INSERT
                        }
                    }
                    uri.path?.startsWith("/search") == true -> {
                        val query = uri.getQueryParameter("q")
                        Intent(context, EventSearchActivity::class.java).apply {
                            putExtra("search_query", query)
                        }
                    }
                    else -> {
                        Intent(context, MainActivity::class.java)
                    }
                }
            }
            else -> null
        }
    }
    
    private fun handleContentScheme(context: Context, uri: Uri): Intent? {
        return when {
            uri.authority == "com.moderncalendar" -> {
                when {
                    uri.path?.startsWith("/event/") == true -> {
                        val eventId = uri.path?.substringAfter("/event/")
                        if (eventId != null) {
                            Intent(context, EventInfoActivity::class.java).apply {
                                putExtra("event_id", eventId)
                            }
                        } else {
                            Intent(context, MainActivity::class.java)
                        }
                    }
                    else -> {
                        Intent(context, MainActivity::class.java)
                    }
                }
            }
            else -> null
        }
    }
    
    private fun handleWebcalScheme(context: Context, uri: Uri): Intent? {
        // Handle webcal:// and webcals:// URLs for calendar subscriptions
        return Intent(context, MainActivity::class.java).apply {
            putExtra("webcal_url", uri.toString())
            putExtra("action", "import_calendar")
        }
    }
    
    companion object {
        // Deep link examples:
        // moderncalendar://event/123
        // moderncalendar://create
        // moderncalendar://search?q=meeting
        // moderncalendar://settings
        // https://calendar.moderncalendar.com/event/123
        // https://calendar.moderncalendar.com/create
        // https://calendar.moderncalendar.com/search?q=meeting
        // content://com.moderncalendar/event/123
        // webcal://example.com/calendar.ics
        // webcals://example.com/calendar.ics
    }
}
