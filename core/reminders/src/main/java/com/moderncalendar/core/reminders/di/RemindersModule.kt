package com.moderncalendar.core.reminders.di

import android.app.AlarmManager
import android.content.Context
import com.moderncalendar.core.reminders.ReminderManager
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object RemindersModule {
    
    @Provides
    @Singleton
    fun provideReminderManager(
        @ApplicationContext context: Context,
        alarmManager: AlarmManager
    ): ReminderManager {
        return ReminderManager(context, alarmManager)
    }
}
