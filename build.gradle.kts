// Top-level build file where you can add configuration options common to all sub-projects/modules.
plugins {
    alias(libs.plugins.android.application) apply false
    alias(libs.plugins.android.library) apply false
    alias(libs.plugins.kotlin.android) apply false
    alias(libs.plugins.kotlin.ksp) apply false
    alias(libs.plugins.hilt) apply false
    alias(libs.plugins.google.services) apply false
    alias(libs.plugins.firebase.crashlytics) apply false
}

allprojects {
    configurations.all {
        resolutionStrategy {
            force("org.jetbrains.kotlin:kotlin-stdlib:2.0.20")
            force("org.jetbrains.kotlin:kotlin-stdlib-jdk8:2.0.20")
            force("org.jetbrains.kotlin:kotlin-stdlib-jdk7:2.0.20")
            force("org.jetbrains.kotlin:kotlin-stdlib-common:2.0.20")
            force("org.jetbrains.kotlin:kotlin-reflect:2.0.20")
        }
    }
}