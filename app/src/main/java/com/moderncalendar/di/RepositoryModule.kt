package com.moderncalendar.di

import com.moderncalendar.core.auth.AuthRepository
import com.moderncalendar.core.data.repository.EventRepository
import com.moderncalendar.core.data.repository.SettingsRepository
import com.moderncalendar.core.data.repository.UserPreferencesRepository
import com.moderncalendar.data.repository.AuthRepositoryImpl
import com.moderncalendar.data.repository.EventRepositoryImpl
import com.moderncalendar.data.repository.SettingsRepositoryImpl
import com.moderncalendar.data.repository.UserPreferencesRepositoryImpl
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
    abstract fun bindEventRepository(
        eventRepositoryImpl: EventRepositoryImpl
    ): EventRepository
    
    @Binds
    abstract fun bindSettingsRepository(
        settingsRepositoryImpl: SettingsRepositoryImpl
    ): SettingsRepository
    
    @Binds
    abstract fun bindUserPreferencesRepository(
        userPreferencesRepositoryImpl: UserPreferencesRepositoryImpl
    ): UserPreferencesRepository
}
