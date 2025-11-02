package com.moderncalendar.di

import com.moderncalendar.core.auth.AuthRepository
import com.moderncalendar.core.common.settings.SettingsRepository
import com.moderncalendar.core.data.repository.CalendarRepository
import com.moderncalendar.core.data.repository.CalendarRepositoryImpl
import com.moderncalendar.core.data.repository.DataStoreSettingsRepository
import com.moderncalendar.core.data.repository.EventRepository
import com.moderncalendar.core.data.repository.RoomEventRepository
import com.moderncalendar.core.data.repository.UserPreferencesRepository
import com.moderncalendar.core.data.repository.UserPreferencesRepositoryImpl
import com.moderncalendar.data.repository.AuthRepositoryImpl
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent

@Module
@InstallIn(SingletonComponent::class)
abstract class RepositoryModule {

    @Binds
    abstract fun bindAuthRepository(
        authRepositoryImpl: AuthRepositoryImpl
    ): AuthRepository
    
    @Binds
    abstract fun bindSettingsRepository(
        settingsRepositoryImpl: DataStoreSettingsRepository
    ): SettingsRepository
    
    @Binds
    abstract fun bindEventRepository(
        eventRepositoryImpl: RoomEventRepository
    ): EventRepository
    
    @Binds
    abstract fun bindCalendarRepository(
        calendarRepository: CalendarRepositoryImpl
    ): CalendarRepository
    
    @Binds
    abstract fun bindUserPreferencesRepository(
        userPreferencesRepository: UserPreferencesRepositoryImpl
    ): UserPreferencesRepository
}
