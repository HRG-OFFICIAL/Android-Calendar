package com.moderncalendar.core.sync.di

import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import com.moderncalendar.core.auth.AuthRepository
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
