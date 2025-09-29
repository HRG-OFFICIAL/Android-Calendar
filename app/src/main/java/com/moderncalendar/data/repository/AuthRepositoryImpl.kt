package com.moderncalendar.data.repository

import com.moderncalendar.core.auth.AuthRepository
import com.moderncalendar.core.common.Result
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class AuthRepositoryImpl @Inject constructor() : AuthRepository {
    
    private var isSignedIn = false  // Start as not signed in
    
    // Mock credentials for demonstration
    private val mockEmail = "demo@calendar.com"
    private val mockPassword = "password123"
    
    override fun isUserSignedIn(): Flow<Boolean> = flow {
        emit(isSignedIn)
    }
    
    override fun signInWithGoogle(): Flow<Result<Unit>> = flow {
        // Simulate Google sign in with delay
        emit(Result.Loading)
        delay(1500) // Simulate network delay
        isSignedIn = true
        emit(Result.Success(Unit))
    }
    
    override fun signInWithEmail(email: String, password: String): Flow<Result<Unit>> = flow {
        // Simulate realistic email sign in with typing delay
        emit(Result.Loading)
        
        // Simulate typing delay for email
        delay(800)
        
        // Simulate typing delay for password
        delay(600)
        
        // Simulate network authentication delay
        delay(1000)
        
        // Always succeed for demo purposes
        isSignedIn = true
        emit(Result.Success(Unit))
    }
    
    override fun signUpWithEmail(email: String, password: String): Flow<Result<Unit>> = flow {
        // Simulate realistic email sign up with typing delay
        emit(Result.Loading)
        
        // Simulate typing delay for email
        delay(800)
        
        // Simulate typing delay for password
        delay(600)
        
        // Simulate account creation delay
        delay(1200)
        
        // Always succeed for demo purposes
        isSignedIn = true
        emit(Result.Success(Unit))
    }
    
    override fun signOut(): Flow<Unit> = flow {
        isSignedIn = false
        emit(Unit)
    }
    
    override fun resetPassword(email: String): Flow<Result<Unit>> = flow {
        // Simulate password reset with delay
        emit(Result.Loading)
        delay(1000)
        emit(Result.Success(Unit))
    }
    
    // Helper method to get mock credentials for UI
    fun getMockCredentials(): Pair<String, String> = Pair(mockEmail, mockPassword)
}
