package com.moderncalendar.core.accessibility.di

import android.content.Context
import com.moderncalendar.core.accessibility.AccessibilityManager
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object AccessibilityModule {
    
    @Provides
    @Singleton
    fun provideAccessibilityManager(
        @ApplicationContext context: Context
    ): AccessibilityManager {
        return AccessibilityManager(context)
    }
}
