package com.moderncalendar.feature.auth

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.moderncalendar.core.auth.AuthRepository
import com.moderncalendar.core.common.Result
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class AuthViewModel @Inject constructor(
    private val authRepository: AuthRepository
) : ViewModel() {
    
    private val _uiState = MutableStateFlow(AuthUiState())
    val uiState: StateFlow<AuthUiState> = _uiState.asStateFlow()
    
    private val _authResult = MutableStateFlow<Result<Unit>>(Result.Loading)
    val authResult: StateFlow<Result<Unit>> = _authResult.asStateFlow()
    
    init {
        checkAuthStatus()
    }
    
    private fun checkAuthStatus() {
        _uiState.value = _uiState.value.copy(isAuthenticated = authRepository.isUserSignedIn)
    }
    
    fun signInWithGoogle() {
        viewModelScope.launch {
            _uiState.value = _uiState.value.copy(isLoading = true)
            _authResult.value = Result.Loading

            authRepository.signInWithGoogle().collect { result ->
                _authResult.value = result
                _uiState.value = _uiState.value.copy(isLoading = false)

                if (result is Result.Success) {
                    _uiState.value = _uiState.value.copy(isAuthenticated = true)
                }
            }
        }
    }
    
    fun signInWithEmail(email: String, password: String) {
        viewModelScope.launch {
            _uiState.value = _uiState.value.copy(isLoading = true)
            _authResult.value = Result.Loading

            authRepository.signInWithEmail(email, password).collect { result ->
                _authResult.value = result
                _uiState.value = _uiState.value.copy(isLoading = false)

                if (result is Result.Success) {
                    _uiState.value = _uiState.value.copy(isAuthenticated = true)
                }
            }
        }
    }
    
    fun signUpWithEmail(email: String, password: String) {
        viewModelScope.launch {
            _uiState.value = _uiState.value.copy(isLoading = true)
            _authResult.value = Result.Loading

            authRepository.signUpWithEmail(email, password).collect { result ->
                _authResult.value = result
                _uiState.value = _uiState.value.copy(isLoading = false)

                if (result is Result.Success) {
                    _uiState.value = _uiState.value.copy(isAuthenticated = true)
                }
            }
        }
    }
    
    fun signOut() {
        viewModelScope.launch {
            val result = authRepository.signOut()
            if (result is Result.Success) {
                _uiState.value = _uiState.value.copy(isAuthenticated = false)
            }
        }
    }
    
    fun resetPassword(email: String) {
        viewModelScope.launch {
            _uiState.value = _uiState.value.copy(isLoading = true)
            val result = authRepository.resetPassword(email)
            _authResult.value = result
            _uiState.value = _uiState.value.copy(isLoading = false)
        }
    }
    
    fun signInAsGuest() {
        viewModelScope.launch {
            _uiState.value = _uiState.value.copy(isLoading = true)
            // Simply set authenticated to true to bypass authentication
            _uiState.value = _uiState.value.copy(
                isAuthenticated = true,
                isLoading = false
            )
        }
    }
    
    fun validateEmail(email: String): String? {
        return if (email.isBlank() || !android.util.Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
            "Please enter a valid email address"
        } else null
    }
    
    fun validatePassword(password: String): String? {
        return if (password.length < 6) {
            "Password must be at least 6 characters"
        } else null
    }
    
    fun validateConfirmPassword(password: String, confirmPassword: String): String? {
        return if (password != confirmPassword) {
            "Passwords do not match"
        } else null
    }
    
    fun signUp(email: String, password: String, confirmPassword: String) {
        val emailError = validateEmail(email)
        val passwordError = validatePassword(password)
        val confirmPasswordError = validateConfirmPassword(password, confirmPassword)
        
        _uiState.value = _uiState.value.copy(
            emailError = emailError,
            passwordError = passwordError,
            confirmPasswordError = confirmPasswordError
        )
        
        if (emailError == null && passwordError == null && confirmPasswordError == null) {
            signUpWithEmail(email, password)
        }
    }
    
    fun signIn(email: String, password: String) {
        val emailError = validateEmail(email)
        val passwordError = validatePassword(password)
        
        _uiState.value = _uiState.value.copy(
            emailError = emailError,
            passwordError = passwordError
        )
        
        if (emailError == null && passwordError == null) {
            signInWithEmail(email, password)
        }
    }
}