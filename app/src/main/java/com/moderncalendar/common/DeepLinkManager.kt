package com.moderncalendar.common

import android.content.Context
import android.content.Intent
import android.net.Uri
import com.moderncalendar.MainActivity
import com.moderncalendar.feature.events.EventInfoActivity
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class DeepLinkManager
    @Inject
    constructor() {
        fun handleDeepLink(
            context: Context,
            uri: Uri,
        ): Boolean {
            return when {
                uri.scheme == "moderncalendar" -> handleModernCalendarScheme(context, uri)
                uri.scheme == "http" || uri.scheme == "https" -> handleHttpScheme(context, uri)
                uri.scheme == "content" -> handleContentScheme(context, uri)
                else -> false
            }
        }

        private fun handleModernCalendarScheme(
            context: Context,
            uri: Uri,
        ): Boolean {
            return when (uri.host) {
                "event" -> {
                    val eventId = uri.getQueryParameter("id")
                    if (eventId != null) {
                        openEvent(context, eventId)
                        true
                    } else {
                        openMainActivity(context)
                        true
                    }
                }
                "calendar" -> {
                    openMainActivity(context)
                    true
                }
                else -> {
                    openMainActivity(context)
                    true
                }
            }
        }

        private fun handleHttpScheme(
            context: Context,
            uri: Uri,
        ): Boolean {
            return when {
                uri.host == "calendar.moderncalendar.com" -> {
                    when (uri.pathSegments.firstOrNull()) {
                        "event" -> {
                            val eventId = uri.getQueryParameter("id")
                            if (eventId != null) {
                                openEvent(context, eventId)
                            } else {
                                openMainActivity(context)
                            }
                            true
                        }
                        else -> {
                            openMainActivity(context)
                            true
                        }
                    }
                }
                else -> false
            }
        }

        private fun handleContentScheme(
            context: Context,
            uri: Uri,
        ): Boolean {
            return when (uri.host) {
                "com.moderncalendar" -> {
                    openMainActivity(context)
                    true
                }
                else -> false
            }
        }

        private fun openMainActivity(context: Context) {
            val intent =
                Intent(context, MainActivity::class.java).apply {
                    flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TOP
                }
            context.startActivity(intent)
        }

        private fun openEvent(
            context: Context,
            eventId: String,
        ) {
            val intent =
                Intent(context, EventInfoActivity::class.java).apply {
                    putExtra("event_id", eventId)
                    flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TOP
                }
            context.startActivity(intent)
        }
    }
