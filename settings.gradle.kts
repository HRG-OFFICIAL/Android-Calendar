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
include(":core:common")
include(":core:ui")
include(":core:data")
include(":core:reminders")
include(":core:auth")
include(":core:sync")
include(":core:analytics")
include(":core:performance")
include(":core:accessibility")
