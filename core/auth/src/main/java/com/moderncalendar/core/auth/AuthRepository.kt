package com.moderncalendar.core.auth

import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.firestore.FirebaseFirestore
import com.moderncalendar.core.common.Result
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.tasks.await
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class FirebaseAuthRepository @Inject constructor(
    private val firebaseAuth: FirebaseAuth,
    private val firestore: FirebaseFirestore
) {
    
    val currentUser: FirebaseUser?
        get() = firebaseAuth.currentUser
    
    val isUserSignedIn: Boolean
        get() = currentUser != null
    
    fun getAuthState(): Flow<Result<FirebaseUser?>> = flow {
        emit(Result.Loading)
        try {
            val user = firebaseAuth.currentUser
            emit(Result.Success(user))
        } catch (exception: Exception) {
            emit(Result.Error(exception))
        }
    }
    
    suspend fun signInWithEmailAndPassword(email: String, password: String): Result<FirebaseUser> {
        return try {
            val result = firebaseAuth.signInWithEmailAndPassword(email, password).await()
            val user = result.user
            if (user != null) {
                Result.Success(user)
            } else {
                Result.Error(Exception("Sign in failed"))
            }
        } catch (exception: Exception) {
            Result.Error(exception)
        }
    }
    
    suspend fun createUserWithEmailAndPassword(email: String, password: String): Result<FirebaseUser> {
        return try {
            val result = firebaseAuth.createUserWithEmailAndPassword(email, password).await()
            val user = result.user
            if (user != null) {
                // Create user profile in Firestore
                createUserProfile(user.uid, email)
                Result.Success(user)
            } else {
                Result.Error(Exception("Account creation failed"))
            }
        } catch (exception: Exception) {
            Result.Error(exception)
        }
    }
    
    suspend fun signOut(): Result<Unit> {
        return try {
            firebaseAuth.signOut()
            Result.Success(Unit)
        } catch (exception: Exception) {
            Result.Error(exception)
        }
    }
    
    suspend fun resetPassword(email: String): Result<Unit> {
        return try {
            firebaseAuth.sendPasswordResetEmail(email).await()
            Result.Success(Unit)
        } catch (exception: Exception) {
            Result.Error(exception)
        }
    }
    
    suspend fun deleteAccount(): Result<Unit> {
        return try {
            val user = currentUser ?: return Result.Error(Exception("No user signed in"))
            user.delete().await()
            Result.Success(Unit)
        } catch (exception: Exception) {
            Result.Error(exception)
        }
    }
    
    private suspend fun createUserProfile(uid: String, email: String) {
        try {
            val userProfile = mapOf(
                "uid" to uid,
                "email" to email,
                "displayName" to null,
                "createdAt" to System.currentTimeMillis(),
                "lastLoginAt" to System.currentTimeMillis()
            )
            firestore.collection("users").document(uid).set(userProfile).await()
        } catch (exception: Exception) {
            // Log error but don't fail the sign up
            println("Error creating user profile: ${exception.message}")
        }
    }
    
    suspend fun updateUserProfile(displayName: String? = null): Result<Unit> {
        return try {
            val user = currentUser ?: return Result.Error(Exception("No user signed in"))
            
            val profileUpdates = mutableMapOf<String, Any>()
            displayName?.let { profileUpdates["displayName"] = it }
            profileUpdates["lastUpdatedAt"] = System.currentTimeMillis()
            
            firestore.collection("users").document(user.uid).update(profileUpdates).await()
            Result.Success(Unit)
        } catch (exception: Exception) {
            Result.Error(exception)
        }
    }
}
