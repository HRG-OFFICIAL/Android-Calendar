plugins {
    alias(libs.plugins.android.library)
    alias(libs.plugins.kotlin.android)
    alias(libs.plugins.hilt)
    alias(libs.plugins.kotlin.ksp)
    alias(libs.plugins.kotlin.serialization)
}

android {
    namespace = "com.moderncalendar.core.data"
    compileSdk = 36

    defaultConfig {
        minSdk = 26
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
}

dependencies {
    implementation(project(":core:common"))
    
    implementation(libs.androidx.core.ktx)
    implementation(libs.gson)
    implementation(libs.bundles.room)
    implementation(libs.datastore.preferences)
    implementation(libs.bundles.network)
    implementation(libs.kotlinx.serialization.json)
    
    // Hilt
    implementation(libs.hilt.android)
    ksp(libs.hilt.compiler)
    ksp(libs.room.compiler)
    
    // Security
    implementation(libs.security.crypto)
    
    // Testing
    testImplementation(libs.bundles.testing)
    testImplementation(libs.room.testing)
    androidTestImplementation(libs.bundles.android.testing)
}
