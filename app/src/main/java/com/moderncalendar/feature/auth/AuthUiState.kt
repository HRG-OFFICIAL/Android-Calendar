package com.moderncalendar.feature.auth

data class AuthUiState(
    val isAuthenticated: Boolean = false,
    val isLoading: Boolean = false,
    val emailError: String? = null,
    val passwordError: String? = null,
    val confirmPasswordError: String? = null,
    val errorMessage: String? = null
)
