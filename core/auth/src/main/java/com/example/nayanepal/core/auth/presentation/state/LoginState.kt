package com.example.nayanepal.core.auth.presentation.state

import com.example.nayanepal.core.auth.domain.model.User

sealed class LoginState {
    object Idle : LoginState()
    object Loading : LoginState()
    data class Success(val user: User) : LoginState()
    data class Error(val message: String) : LoginState()
}
