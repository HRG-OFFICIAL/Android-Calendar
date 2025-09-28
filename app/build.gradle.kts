plugins {
    alias(libs.plugins.android.application)
    alias(libs.plugins.kotlin.android)
    alias(libs.plugins.kotlin.compose)
    alias(libs.plugins.kotlin.ksp)
    alias(libs.plugins.hilt)
    alias(libs.plugins.google.services)
    alias(libs.plugins.firebase.crashlytics)
}

android {
    namespace = "com.moderncalendar"
    compileSdk = 36

    defaultConfig {
        applicationId = "com.moderncalendar"
        minSdk = 26
        targetSdk = 36
        versionCode = 1
        versionName = "1.0"

        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
    }

    buildTypes {
        release {
            isMinifyEnabled = false
            proguardFiles(
                getDefaultProguardFile("proguard-android-optimize.txt"),
                "proguard-rules.pro"
            )
        }
    }
    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_11
        targetCompatibility = JavaVersion.VERSION_11
    }
    kotlin {
        compilerOptions {
            jvmTarget.set(org.jetbrains.kotlin.gradle.dsl.JvmTarget.JVM_11)
        }
    }
    buildFeatures {
        compose = true
    }
}

dependencies {
    // Core modules
    implementation(project(":core:common"))
    implementation(project(":core:ui"))
    implementation(project(":core:data"))
    implementation(project(":core:reminders"))
    implementation(project(":core:auth"))
    implementation(project(":core:sync"))
    implementation(project(":core:analytics"))
    implementation(project(":core:performance"))
    implementation(project(":core:accessibility"))
    
    // Core Android
    implementation(libs.androidx.core.ktx)
    implementation(libs.androidx.lifecycle.runtime.ktx)
    implementation(libs.androidx.activity.compose)
    
    // Compose
    implementation(platform(libs.androidx.compose.bom))
    implementation(libs.bundles.compose)
    implementation(libs.bundles.lifecycle)
    
    // Calendar
    implementation(libs.calendar.compose)
    
    // Hilt
    implementation(libs.hilt.android)
    implementation(libs.hilt.navigation.compose)
    ksp(libs.hilt.compiler)
    
    // Room
    implementation(libs.room.runtime)
    implementation(libs.room.ktx)
    ksp(libs.room.compiler)
    
    // WorkManager
    implementation(libs.work.runtime.ktx)
    implementation(libs.work.hilt)
    ksp(libs.hilt.compiler)
    
    // Firebase
    implementation(platform(libs.firebase.bom))
    implementation(libs.bundles.firebase)
    
    // UI & Animation
    implementation(libs.lottie.compose)
    implementation(libs.coil.compose)
    implementation(libs.accompanist.permissions)
    implementation(libs.accompanist.systemuicontroller)
    
    // Testing
    testImplementation(libs.bundles.testing)
    androidTestImplementation(libs.bundles.android.testing)
    debugImplementation(libs.androidx.ui.tooling)
    debugImplementation(libs.androidx.ui.test.manifest)
}