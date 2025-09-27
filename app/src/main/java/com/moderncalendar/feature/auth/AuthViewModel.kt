package com.moderncalendar.feature.auth

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.moderncalendar.core.auth.AuthRepository
import com.moderncalendar.core.common.Result
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class AuthViewModel @Inject constructor(
    private val authRepository: AuthRepository
) : ViewModel() {
    
    private val _uiState = MutableStateFlow(AuthUiState())
    val uiState: StateFlow<AuthUiState> = _uiState.asStateFlow()
    
    init {
        checkAuthState()
    }
    
    private fun checkAuthState() {
        viewModelScope.launch {
            authRepository.getAuthState().collect { result ->
                when (result) {
                    is Result.Success -> {
                        _uiState.value = _uiState.value.copy(
                            isAuthenticated = result.data != null,
                            isLoading = false
                        )
                    }
                    is Result.Error -> {
                        _uiState.value = _uiState.value.copy(
                            isLoading = false,
                            errorMessage = result.exception.message
                        )
                    }
                    is Result.Loading -> {
                        _uiState.value = _uiState.value.copy(isLoading = true)
                    }
                }
            }
        }
    }
    
    fun signIn(email: String, password: String) {
        if (!validateSignInInput(email, password)) return
        
        viewModelScope.launch {
            _uiState.value = _uiState.value.copy(isLoading = true, errorMessage = null)
            
            when (val result = authRepository.signInWithEmailAndPassword(email, password)) {
                is Result.Success -> {
                    _uiState.value = _uiState.value.copy(
                        isAuthenticated = true,
                        isLoading = false
                    )
                }
                is Result.Error -> {
                    _uiState.value = _uiState.value.copy(
                        isLoading = false,
                        errorMessage = result.exception.message ?: "Sign in failed"
                    )
                }
                is Result.Loading -> {
                    // Handle loading state if needed
                }
            }
        }
    }
    
    fun signUp(email: String, password: String, confirmPassword: String, displayName: String) {
        if (!validateSignUpInput(email, password, confirmPassword, displayName)) return
        
        viewModelScope.launch {
            _uiState.value = _uiState.value.copy(isLoading = true, errorMessage = null)
            
            when (val result = authRepository.createUserWithEmailAndPassword(email, password)) {
                is Result.Success -> {
                    // Update display name if provided
                    if (displayName.isNotBlank()) {
                        authRepository.updateUserProfile(displayName)
                    }
                    
                    _uiState.value = _uiState.value.copy(
                        isAuthenticated = true,
                        isLoading = false
                    )
                }
                is Result.Error -> {
                    _uiState.value = _uiState.value.copy(
                        isLoading = false,
                        errorMessage = result.exception.message ?: "Sign up failed"
                    )
                }
                is Result.Loading -> {
                    // Handle loading state if needed
                }
            }
        }
    }
    
    fun signOut() {
        viewModelScope.launch {
            _uiState.value = _uiState.value.copy(isLoading = true)
            
            when (val result = authRepository.signOut()) {
                is Result.Success -> {
                    _uiState.value = _uiState.value.copy(
                        isAuthenticated = false,
                        isLoading = false
                    )
                }
                is Result.Error -> {
                    _uiState.value = _uiState.value.copy(
                        isLoading = false,
                        errorMessage = result.exception.message ?: "Sign out failed"
                    )
                }
                is Result.Loading -> {
                    // Handle loading state if needed
                }
            }
        }
    }
    
    fun resetPassword(email: String) {
        if (email.isBlank()) {
            _uiState.value = _uiState.value.copy(
                emailError = "Email is required"
            )
            return
        }
        
        viewModelScope.launch {
            _uiState.value = _uiState.value.copy(isLoading = true, errorMessage = null)
            
            when (val result = authRepository.resetPassword(email)) {
                is Result.Success -> {
                    _uiState.value = _uiState.value.copy(
                        isLoading = false,
                        errorMessage = "Password reset email sent"
                    )
                }
                is Result.Error -> {
                    _uiState.value = _uiState.value.copy(
                        isLoading = false,
                        errorMessage = result.exception.message ?: "Password reset failed"
                    )
                }
                is Result.Loading -> {
                    // Handle loading state if needed
                }
            }
        }
    }
    
    fun clearErrors() {
        _uiState.value = _uiState.value.copy(
            emailError = null,
            passwordError = null,
            confirmPasswordError = null,
            errorMessage = null
        )
    }
    
    private fun validateSignInInput(email: String, password: String): Boolean {
        var isValid = true
        
        if (email.isBlank()) {
            _uiState.value = _uiState.value.copy(emailError = "Email is required")
            isValid = false
        } else if (!isValidEmail(email)) {
            _uiState.value = _uiState.value.copy(emailError = "Invalid email format")
            isValid = false
        } else {
            _uiState.value = _uiState.value.copy(emailError = null)
        }
        
        if (password.isBlank()) {
            _uiState.value = _uiState.value.copy(passwordError = "Password is required")
            isValid = false
        } else {
            _uiState.value = _uiState.value.copy(passwordError = null)
        }
        
        return isValid
    }
    
    private fun validateSignUpInput(email: String, password: String, confirmPassword: String, displayName: String): Boolean {
        var isValid = true
        
        if (email.isBlank()) {
            _uiState.value = _uiState.value.copy(emailError = "Email is required")
            isValid = false
        } else if (!isValidEmail(email)) {
            _uiState.value = _uiState.value.copy(emailError = "Invalid email format")
            isValid = false
        } else {
            _uiState.value = _uiState.value.copy(emailError = null)
        }
        
        if (password.isBlank()) {
            _uiState.value = _uiState.value.copy(passwordError = "Password is required")
            isValid = false
        } else if (password.length < 6) {
            _uiState.value = _uiState.value.copy(passwordError = "Password must be at least 6 characters")
            isValid = false
        } else {
            _uiState.value = _uiState.value.copy(passwordError = null)
        }
        
        if (confirmPassword.isBlank()) {
            _uiState.value = _uiState.value.copy(confirmPasswordError = "Please confirm your password")
            isValid = false
        } else if (password != confirmPassword) {
            _uiState.value = _uiState.value.copy(confirmPasswordError = "Passwords do not match")
            isValid = false
        } else {
            _uiState.value = _uiState.value.copy(confirmPasswordError = null)
        }
        
        return isValid
    }
    
    private fun isValidEmail(email: String): Boolean {
        return android.util.Patterns.EMAIL_ADDRESS.matcher(email).matches()
    }
}

data class AuthUiState(
    val isAuthenticated: Boolean = false,
    val isLoading: Boolean = false,
    val emailError: String? = null,
    val passwordError: String? = null,
    val confirmPasswordError: String? = null,
    val errorMessage: String? = null
)
