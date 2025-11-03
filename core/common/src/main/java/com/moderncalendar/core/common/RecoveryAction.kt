package com.moderncalendar.core.common

/**
 * Sealed class representing different recovery actions that can be taken for errors
 */
sealed class RecoveryAction(
    open val label: String,
    open val description: String? = null
) {

    /**
     * Retry the failed operation
     */
    data class Retry(
        override val label: String = "Retry",
        override val description: String? = "Try the operation again"
    ) : RecoveryAction(label, description)

    /**
     * Refresh or reload data
     */
    data class Refresh(
        override val label: String = "Refresh",
        override val description: String? = "Reload the data"
    ) : RecoveryAction(label, description)

    /**
     * Navigate to settings
     */
    data class GoToSettings(
        override val label: String = "Settings",
        override val description: String? = "Open app settings",
        val settingsType: SettingsType = SettingsType.GENERAL
    ) : RecoveryAction(label, description)

    /**
     * Request permission
     */
    data class RequestPermission(
        override val label: String = "Grant Permission",
        override val description: String? = "Allow required permissions",
        val permission: String
    ) : RecoveryAction(label, description)

    /**
     * Check network connection
     */
    data class CheckConnection(
        override val label: String = "Check Connection",
        override val description: String? = "Verify your internet connection"
    ) : RecoveryAction(label, description)

    /**
     * Contact support
     */
    data class ContactSupport(
        override val label: String = "Contact Support",
        override val description: String? = "Get help from support team"
    ) : RecoveryAction(label, description)

    /**
     * Navigate back or dismiss
     */
    data class Dismiss(
        override val label: String = "Dismiss",
        override val description: String? = "Close this error"
    ) : RecoveryAction(label, description)

    /**
     * Navigate to a specific screen
     */
    data class Navigate(
        override val label: String,
        override val description: String? = null,
        val destination: String
    ) : RecoveryAction(label, description)

    /**
     * Clear cache or data
     */
    data class ClearCache(
        override val label: String = "Clear Cache",
        override val description: String? = "Clear app cache and try again"
    ) : RecoveryAction(label, description)

    /**
     * Force sync
     */
    data class ForceSync(
        override val label: String = "Force Sync",
        override val description: String? = "Force synchronization with server"
    ) : RecoveryAction(label, description)

    /**
     * Resolve sync conflict
     */
    data class ResolveSyncConflict(
        override val label: String = "Resolve Conflict",
        override val description: String? = "Choose how to resolve the sync conflict",
        val conflictResolution: ConflictResolution
    ) : RecoveryAction(label, description)
}

/**
 * Types of settings that can be opened
 */
enum class SettingsType {
    GENERAL,
    PERMISSIONS,
    NETWORK,
    SYNC,
    NOTIFICATIONS
}

/**
 * Types of conflict resolution strategies
 */
enum class ConflictResolution {
    USE_LOCAL,      // Keep local changes
    USE_REMOTE,     // Use server version
    MERGE,          // Attempt to merge both
    MANUAL          // Let user decide manually
}