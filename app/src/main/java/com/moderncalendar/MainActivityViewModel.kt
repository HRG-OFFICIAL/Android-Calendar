package com.moderncalendar

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.moderncalendar.core.auth.AuthRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class MainActivityViewModel
    @Inject
    constructor(
        private val authRepository: AuthRepository,
    ) : ViewModel() {
        private val _isUserSignedIn = MutableStateFlow(false)
        val isUserSignedIn: StateFlow<Boolean> = _isUserSignedIn.asStateFlow()

        init {
            checkAuthStatus()
        }

        private fun checkAuthStatus() {
            _isUserSignedIn.value = authRepository.isUserSignedIn
        }

        fun signOut() {
            viewModelScope.launch {
                authRepository.signOut()
                _isUserSignedIn.value = false
            }
        }
    }
