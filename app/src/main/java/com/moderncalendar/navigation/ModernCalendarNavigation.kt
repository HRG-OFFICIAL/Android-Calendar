package com.moderncalendar.navigation

import androidx.compose.runtime.Composable
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navDeepLink
import com.moderncalendar.feature.calendar.CalendarScreen
import com.moderncalendar.feature.events.EventCreationScreen
import com.moderncalendar.feature.events.EventDetailsScreen
import com.moderncalendar.feature.events.EventEditScreen
import com.moderncalendar.feature.search.SearchScreen
import com.moderncalendar.feature.settings.SettingsScreen

@Composable
fun ModernCalendarNavigation(navController: NavHostController = rememberNavController()) {
    NavHost(
        navController = navController,
        startDestination = "calendar",
    ) {
        composable(
            route = "calendar",
            deepLinks =
                listOf(
                    navDeepLink { uriPattern = "moderncalendar://calendar" },
                    navDeepLink { uriPattern = "moderncalendar://calendar/{date}" },
                ),
        ) {
            CalendarScreen(
                onNavigateToEventDetails = { eventId: String ->
                    navController.navigate("event_details/$eventId")
                },
                onNavigateToEventCreation = { selectedDate ->
                    navController.navigate("create_event/$selectedDate")
                },
                onNavigateToSearch = {
                    navController.navigate("search")
                },
                onNavigateToSettings = {
                    navController.navigate("settings")
                },
            )
        }

        composable(
            route = "create_event/{selectedDate}",
            deepLinks =
                listOf(
                    navDeepLink { uriPattern = "moderncalendar://create_event" },
                ),
        ) { backStackEntry ->
            val selectedDateString = backStackEntry.arguments?.getString("selectedDate")
            val selectedDate =
                selectedDateString?.let {
                    try {
                        java.time.LocalDate.parse(it)
                    } catch (e: Exception) {
                        null
                    }
                }
            EventCreationScreen(
                initialDate = selectedDate,
                onEventCreated = {
                    navController.popBackStack()
                },
            )
        }

        composable("search") {
            SearchScreen(
                onBackClick = {
                    navController.popBackStack()
                },
                onEventClick = { eventId ->
                    navController.navigate("event_details/$eventId")
                },
            )
        }

        composable(
            route = "event_details/{eventId}",
            deepLinks =
                listOf(
                    navDeepLink { uriPattern = "moderncalendar://event/{eventId}" },
                ),
        ) { backStackEntry ->
            val rawEventId = backStackEntry.arguments?.getString("eventId") ?: ""
            val eventId =
                try {
                    java.net.URLDecoder.decode(rawEventId, "UTF-8")
                } catch (e: Exception) {
                    rawEventId
                }
            EventDetailsScreen(
                eventId = eventId,
                onBackClick = {
                    navController.popBackStack()
                },
                onEditClick = { eventId ->
                    navController.navigate("edit_event/$eventId")
                },
                onDeleteClick = {
                    navController.popBackStack()
                },
            )
        }

        composable("edit_event/{eventId}") { backStackEntry ->
            val eventId = backStackEntry.arguments?.getString("eventId") ?: ""
            EventEditScreen(
                eventId = eventId,
                onEventUpdated = {
                    navController.popBackStack()
                },
            )
        }

        composable("settings") {
            SettingsScreen(
                onBackClick = {
                    navController.popBackStack()
                },
                onSignOut = {
                    // Sign out will be handled by the auth state change
                },
            )
        }
    }
}
