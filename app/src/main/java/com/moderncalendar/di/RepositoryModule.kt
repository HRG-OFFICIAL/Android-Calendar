package com.moderncalendar.di

import com.moderncalendar.core.auth.AuthRepository
import com.moderncalendar.core.auth.FirebaseAuthRepository
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent

@Module
@InstallIn(SingletonComponent::class)
abstract class RepositoryModule {
    @Binds
    abstract fun bindAuthRepository(firebaseAuthRepository: FirebaseAuthRepository): AuthRepository
}
