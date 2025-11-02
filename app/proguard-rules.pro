# Add project specific ProGuard rules here.

# Keep all model classes
-keep class com.moderncalendar.core.common.model.** { *; }
-keep class com.moderncalendar.core.data.entity.** { *; }

# Keep Room database classes
-keep class * extends androidx.room.RoomDatabase
-keep @androidx.room.Entity class *
-keep @androidx.room.Dao class *

# Keep Hilt generated classes
-keep class dagger.hilt.** { *; }
-keep class * extends dagger.hilt.android.HiltAndroidApp
-keep @dagger.hilt.android.lifecycle.HiltViewModel class * { *; }

# Keep Compose classes
-keep class androidx.compose.** { *; }

# Keep Gson classes for JSON serialization
-keepattributes Signature
-keepattributes *Annotation*
-keep class com.google.gson.** { *; }

# Keep Firebase classes
-keep class com.google.firebase.** { *; }

# Keep line numbers for debugging
-keepattributes SourceFile,LineNumberTable
-renamesourcefileattribute SourceFile