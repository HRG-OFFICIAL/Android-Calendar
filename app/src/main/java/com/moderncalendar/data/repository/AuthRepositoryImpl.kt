package com.moderncalendar.data.repository

import com.moderncalendar.core.auth.AuthRepository
import com.moderncalendar.core.common.Result
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class AuthRepositoryImpl @Inject constructor() : AuthRepository {
    
    private var isSignedIn = false
    
    override fun isUserSignedIn(): Flow<Boolean> = flow {
        emit(isSignedIn)
    }
    
    override fun signInWithGoogle(): Flow<Result<Unit>> = flow {
        // Simulate Google sign in
        isSignedIn = true
        emit(Result.Success(Unit))
    }
    
    override fun signInWithEmail(email: String, password: String): Flow<Result<Unit>> = flow {
        // Simulate email sign in
        isSignedIn = true
        emit(Result.Success(Unit))
    }
    
    override fun signUpWithEmail(email: String, password: String): Flow<Result<Unit>> = flow {
        // Simulate email sign up
        isSignedIn = true
        emit(Result.Success(Unit))
    }
    
    override fun signOut(): Flow<Unit> = flow {
        isSignedIn = false
        emit(Unit)
    }
    
    override fun resetPassword(email: String): Flow<Result<Unit>> = flow {
        // Simulate password reset
        emit(Result.Success(Unit))
    }
}
