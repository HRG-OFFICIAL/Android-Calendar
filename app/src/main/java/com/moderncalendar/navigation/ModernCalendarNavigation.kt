package com.moderncalendar.navigation

import androidx.compose.runtime.Composable
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.moderncalendar.feature.calendar.CalendarScreen
import com.moderncalendar.feature.events.EventCreationScreen
import com.moderncalendar.feature.events.EventDetailsScreen
import com.moderncalendar.feature.search.SearchScreen
import com.moderncalendar.feature.settings.SettingsScreen

@Composable
fun ModernCalendarNavigation(
    navController: NavHostController = rememberNavController()
) {
    NavHost(
        navController = navController,
        startDestination = "calendar"
    ) {
        composable("calendar") {
            CalendarScreen(
                onEventClick = { eventId ->
                    navController.navigate("event_details/$eventId")
                },
                onCreateEventClick = {
                    navController.navigate("create_event")
                },
                onSearchClick = {
                    navController.navigate("search")
                },
                onSettingsClick = {
                    navController.navigate("settings")
                }
            )
        }
        
        composable("create_event") {
            EventCreationScreen(
                onEventCreated = {
                    navController.popBackStack()
                }
            )
        }
        
        composable("search") {
            SearchScreen(
                onBackClick = {
                    navController.popBackStack()
                },
                onEventClick = { eventId ->
                    navController.navigate("event_details/$eventId")
                }
            )
        }
        
        composable("event_details/{eventId}") { backStackEntry ->
            val eventId = backStackEntry.arguments?.getString("eventId") ?: ""
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
                }
            )
        }
        
        composable("settings") {
            SettingsScreen(
                onBackClick = {
                    navController.popBackStack()
                },
                onSignOut = {
                    // Sign out will be handled by the auth state change
                }
            )
        }
    }
}
