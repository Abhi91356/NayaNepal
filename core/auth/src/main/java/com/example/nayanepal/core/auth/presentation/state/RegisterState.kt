package com.example.nayanepal.core.auth.presentation.state

import com.example.nayanepal.core.auth.domain.model.User

sealed class RegisterState {
    object Idle : RegisterState()
    object Loading : RegisterState()
    data class Success(val user: User) : RegisterState()
    data class Error(val message: String) : RegisterState()
}
