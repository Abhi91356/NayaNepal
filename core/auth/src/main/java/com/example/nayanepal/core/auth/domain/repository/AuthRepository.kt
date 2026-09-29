package com.example.nayanepal.core.auth.domain.repository

import com.example.nayanepal.core.auth.domain.model.User

interface AuthRepository {
    suspend fun loginWithEmail(email: String, pass: String): Result<User>
    suspend fun registerWithEmail(name: String, email: String, phone: String, pass: String): Result<User>
}
