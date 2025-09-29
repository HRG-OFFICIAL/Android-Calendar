package com.moderncalendar.core.sync.di

import com.moderncalendar.core.sync.CloudSyncRepository
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object SyncModule {
    
    @Provides
    @Singleton
    fun provideCloudSyncRepository(): CloudSyncRepository = CloudSyncRepository()
}
