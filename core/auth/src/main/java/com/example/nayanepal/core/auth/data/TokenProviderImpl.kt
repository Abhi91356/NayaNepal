package com.example.nayanepal.core.auth.data

import com.example.nayanepal.core.auth.domain.SessionManager
import com.example.nayanepal.core.network.domain.TokenProvider
import javax.inject.Inject
class TokenProviderImpl @Inject constructor(
    private val sessionManager: SessionManager
) : TokenProvider {
    override fun getToken(): String? = sessionManager.getAuthToken()
}
