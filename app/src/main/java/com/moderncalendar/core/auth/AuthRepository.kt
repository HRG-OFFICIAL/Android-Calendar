package com.moderncalendar.core.auth

import kotlinx.coroutines.flow.Flow

interface AuthRepository {
    fun isUserSignedIn(): Flow<Boolean>
    fun signInWithGoogle(): Flow<com.moderncalendar.core.common.Result<Unit>>
    fun signInWithEmail(email: String, password: String): Flow<com.moderncalendar.core.common.Result<Unit>>
    fun signUpWithEmail(email: String, password: String): Flow<com.moderncalendar.core.common.Result<Unit>>
    fun signOut(): Flow<Unit>
    fun resetPassword(email: String): Flow<com.moderncalendar.core.common.Result<Unit>>
}
