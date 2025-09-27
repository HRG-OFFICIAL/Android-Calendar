package com.moderncalendar.core.data.di

import android.content.Context
import com.moderncalendar.core.data.database.CalendarDatabase
import com.moderncalendar.core.data.dao.CalendarDao
import com.moderncalendar.core.data.dao.EventDao
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object DatabaseModule {
    
    @Provides
    @Singleton
    fun provideCalendarDatabase(
        @ApplicationContext context: Context
    ): CalendarDatabase {
        return CalendarDatabase.getDatabase(context)
    }
    
    @Provides
    fun provideEventDao(database: CalendarDatabase): EventDao {
        return database.eventDao()
    }
    
    @Provides
    fun provideCalendarDao(database: CalendarDatabase): CalendarDao {
        return database.calendarDao()
    }
}
