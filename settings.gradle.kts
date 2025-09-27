pluginManagement {
    repositories {
        google {
            content {
                includeGroupByRegex("com\\.android.*")
                includeGroupByRegex("com\\.google.*")
                includeGroupByRegex("androidx.*")
            }
        }
        mavenCentral()
        gradlePluginPortal()
    }
}
dependencyResolutionManagement {
    repositoriesMode.set(RepositoriesMode.FAIL_ON_PROJECT_REPOS)
    repositories {
        google()
        mavenCentral()
    }
}

rootProject.name = "ModernCalendar"
include(":app")

// Core modules
include(":core")
include(":core:ui")
include(":core:data")
include(":core:domain")
include(":core:network")
include(":core:common")
include(":core:reminders")
include(":core:auth")
include(":core:sync")
include(":core:analytics")
include(":core:performance")
include(":core:accessibility")

// Feature modules
include(":feature:calendar")
include(":feature:events")
include(":feature:settings")
include(":feature:auth")
include(":feature:sync")
include(":feature:reminders")
include(":feature:search")

// Data modules
include(":data:local")
include(":data:remote")
include(":data:repository")
