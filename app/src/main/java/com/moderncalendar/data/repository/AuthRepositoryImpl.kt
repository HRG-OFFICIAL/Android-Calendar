package com.moderncalendar.data.repository

import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.auth.UserProfileChangeRequest
import com.moderncalendar.core.auth.AuthRepository
import com.moderncalendar.core.common.Result
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.tasks.await
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class AuthRepositoryImpl @Inject constructor(
    private val firebaseAuth: FirebaseAuth
) : AuthRepository {

    override val currentUser: FirebaseUser?
        get() = firebaseAuth.currentUser

    override val isUserSignedIn: Boolean
        get() = firebaseAuth.currentUser != null

    override fun getAuthState(): Flow<Result<FirebaseUser?>> = flow {
        emit(Result.Success(firebaseAuth.currentUser))
    }

    override fun signInWithGoogle(): Flow<Result<Unit>> = flow {
        try {
            emit(Result.Error(Exception("Google Sign-In requires additional setup")))
        } catch (e: Exception) {
            emit(Result.Error(e))
        }
    }

    override fun signInWithEmail(email: String, password: String): Flow<Result<Unit>> = flow {
        try {
            if (email.isBlank() || password.isBlank()) {
                emit(Result.Error(Exception("Email and password cannot be empty")))
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
            firebaseAuth.signInWithEmailAndPassword(email, password).await()
            emit(Result.Success(Unit))
        } catch (e: Exception) {
            val errorMessage = when (e.message) {
                "The email address is badly formatted." -> "Invalid email format"
                "The password is invalid or the user does not have a password." -> "Invalid password"
                "There is no user record corresponding to this identifier." -> "No account found with this email"
                "The user account has been disabled by an administrator." -> "Account has been disabled"
                "Too many unsuccessful login attempts. Please try again later." -> "Too many failed attempts. Please try again later"
                else -> e.message ?: "Authentication failed"
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

    override suspend fun signOut(): Result<Unit> {
        return try {
            firebaseAuth.signOut()
            Result.Success(Unit)
        } catch (e: Exception) {
            Result.Error(e)
        }
    }

    override suspend fun resetPassword(email: String): Result<Unit> {
        return try {
            if (email.isBlank()) {
                Result.Error(Exception("Email cannot be empty"))
            } else if (!android.util.Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
                Result.Error(Exception("Invalid email format"))
            } else {
                firebaseAuth.sendPasswordResetEmail(email).await()
                Result.Success(Unit)
            }
        } catch (e: Exception) {
            val errorMessage = when (e.message) {
                "There is no user record corresponding to this identifier." -> "No account found with this email"
                "The email address is badly formatted." -> "Invalid email format"
                else -> e.message ?: "Failed to send reset email"
            }
            Result.Error(Exception(errorMessage))
        }
    }

    fun getCurrentUserId(): String? = firebaseAuth.currentUser?.uid
    fun getCurrentUserEmail(): String? = firebaseAuth.currentUser?.email
    fun getCurrentUserDisplayName(): String? = firebaseAuth.currentUser?.displayName
}