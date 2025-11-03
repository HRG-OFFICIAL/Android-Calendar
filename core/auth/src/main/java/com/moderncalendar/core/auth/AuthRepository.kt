package com.moderncalendar.core.auth

import com.google.firebase.auth.FirebaseUser
import com.moderncalendar.core.common.Result
import kotlinx.coroutines.flow.Flow

/**
 * Repository interface for authentication operations
 */
interface AuthRepository {
    val currentUser: FirebaseUser?
    val isUserSignedIn: Boolean
    
    fun getAuthState(): Flow<Result<FirebaseUser?>>
    fun signInWithGoogle(): Flow<Result<Unit>>
    fun signInWithEmail(email: String, password: String): Flow<Result<Unit>>
    fun signUpWithEmail(email: String, password: String): Flow<Result<Unit>>
    suspend fun signOut(): Result<Unit>
    suspend fun resetPassword(email: String): Result<Unit>
}