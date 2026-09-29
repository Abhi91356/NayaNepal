package com.example.nayanepal.core.auth.presentation.viewmodel

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.nayanepal.core.auth.domain.repository.AuthRepository
import com.example.nayanepal.core.auth.presentation.state.LoginState
import com.example.nayanepal.core.auth.presentation.state.RegisterState
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class AuthViewModel @Inject constructor(
    private val authRepository: AuthRepository
) : ViewModel() {

    private val _loginState = MutableStateFlow<LoginState>(LoginState.Idle)
    val loginState: StateFlow<LoginState> = _loginState

    private val _registerState = MutableStateFlow<RegisterState>(RegisterState.Idle)
    val registerState: StateFlow<RegisterState> = _registerState

    var mfaEnabled by mutableStateOf(false)
        private set

    fun toggleMfa(enabled: Boolean) {
        mfaEnabled = enabled
    }

    fun loginWithEmail(email: String, pass: String) {
        viewModelScope.launch {
            _loginState.value = LoginState.Loading
            authRepository.loginWithEmail(email, pass)
                .onSuccess { user ->
                    _loginState.value = LoginState.Success(user)
                }
                .onFailure { error ->
                    _loginState.value = LoginState.Error(error.message ?: "Login failed")
                }
        }
    }

    fun registerWithEmail(name: String, email: String, phone: String, pass: String) {
        viewModelScope.launch {
            _registerState.value = RegisterState.Loading
            authRepository.registerWithEmail(name, email, phone, pass)
                .onSuccess { user ->
                    _registerState.value = RegisterState.Success(user)
                }
                .onFailure { error ->
                    _registerState.value = RegisterState.Error(error.message ?: "Registration failed")
                }
        }
    }

    fun initBiometricAuth() {
        // Handled by UI dialog
    }

    fun completeBiometricLogin(data: String) {
        // _loginState.value = LoginState.Success("biometric_token")
    }

    fun loginWithGoogle() {
        // Logic for Google OAuth
    }

    fun loginWithFacebook() {
        // Logic for Facebook OAuth
    }

    fun showForgotPasswordDialog() {
        // Handled by UI
    }

    fun navigateToRegister() {
        // Handled by UI/Navigation
    }
}
