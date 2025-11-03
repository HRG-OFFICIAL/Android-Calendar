package com.moderncalendar.core.data.di

import android.content.Context
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.preferencesDataStore
import com.moderncalendar.core.common.repository.CalendarRepository
import com.moderncalendar.core.common.repository.EventRepository
import com.moderncalendar.core.common.repository.SettingsRepository
import com.moderncalendar.core.common.repository.UserPreferencesRepository
import com.moderncalendar.core.data.dao.CalendarDao
import com.moderncalendar.core.data.dao.EventDao
import com.moderncalendar.core.data.database.CalendarDatabase
import com.moderncalendar.core.data.repository.CalendarRepositoryImpl
import com.moderncalendar.core.data.repository.DataStoreSettingsRepository
import com.moderncalendar.core.data.repository.RoomEventRepository
import com.moderncalendar.core.data.repository.UserPreferencesRepositoryImpl
import dagger.Binds
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

private val Context.dataStore: DataStore<Preferences> by preferencesDataStore(name = "user_preferences")

@Module
@InstallIn(SingletonComponent::class)
abstract class DatabaseModule {
    
    @Binds
    abstract fun bindEventRepository(
        roomEventRepository: RoomEventRepository
    ): EventRepository
    
    @Binds
    abstract fun bindCalendarRepository(
        calendarRepositoryImpl: CalendarRepositoryImpl
    ): CalendarRepository
    
    @Binds
    abstract fun bindSettingsRepository(
        dataStoreSettingsRepository: DataStoreSettingsRepository
    ): SettingsRepository
    
    @Binds
    abstract fun bindUserPreferencesRepository(
        userPreferencesRepositoryImpl: UserPreferencesRepositoryImpl
    ): UserPreferencesRepository
    
    companion object {
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
        
        @Provides
        @Singleton
        fun provideDataStore(@ApplicationContext context: Context): DataStore<Preferences> {
            return context.dataStore
        }
    }
}


