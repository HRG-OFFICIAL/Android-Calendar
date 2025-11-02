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
) : AuthRepository {
    
    override val currentUser: FirebaseUser?
        get() = firebaseAuth.currentUser
    
    override val isUserSignedIn: Boolean
        get() = currentUser != null
    
    override fun getAuthState(): Flow<Result<FirebaseUser?>> = flow {
        emit(Result.Loading)
        try {
            val user = firebaseAuth.currentUser
            emit(Result.Success(user))
        } catch (exception: Exception) {
            emit(Result.Error(exception))
        }
    }
    
    override fun signInWithGoogle(): Flow<Result<Unit>> = flow {
        emit(Result.Error(Exception("Google Sign-In not yet implemented")))
    }
    
    override fun signInWithEmail(email: String, password: String): Flow<Result<Unit>> = flow {
        try {
            if (email.isBlank() || password.isBlank()) {
                emit(Result.Error(Exception("Email and password are required")))
                return@flow
            }

            if (!android.util.Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
                emit(Result.Error(Exception("Invalid email format")))
                return@flow
            }

            emit(Result.Loading)
            firebaseAuth.signInWithEmailAndPassword(email, password).await()
            emit(Result.Success(Unit))
        } catch (e: Exception) {
            val errorMessage = when (e.message) {
                "There is no user record corresponding to this identifier. The user may have been deleted." -> "No account found with this email"
                "The email address is badly formatted." -> "Invalid email format"
                "The password is invalid." -> "Incorrect password"
                else -> e.message ?: "Login failed"
            }
            emit(Result.Error(Exception(errorMessage)))
        }
    }
    
    override fun signUpWithEmail(email: String, password: String): Flow<Result<Unit>> = flow {
        try {
            if (email.isBlank() || password.isBlank()) {
                emit(Result.Error(Exception("Email and password are required")))
                return@flow
            }

            if (!android.util.Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
                emit(Result.Error(Exception("Invalid email format")))
                return@flow
            }

            if (password.length < 6) {
                emit(Result.Error(Exception("Password must be at least 6 characters")))
                return@flow
            }

            emit(Result.Loading)
            firebaseAuth.createUserWithEmailAndPassword(email, password).await()
            emit(Result.Success(Unit))
        } catch (e: Exception) {
            val errorMessage = when (e.message) {
                "The email address is already in use by another account." -> "An account with this email already exists"
                "The email address is badly formatted." -> "Invalid email format"
                "The given password is invalid." -> "Password is too weak"
                "Password should be at least 6 characters" -> "Password must be at least 6 characters"
                else -> e.message ?: "Registration failed"
            }
            emit(Result.Error(Exception(errorMessage)))
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
    
    override suspend fun signOut(): Result<Unit> {
        return try {
            firebaseAuth.signOut()
            Result.Success(Unit)
        } catch (exception: Exception) {
            Result.Error(exception)
        }
    }
    
    override suspend fun resetPassword(email: String): Result<Unit> {
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
