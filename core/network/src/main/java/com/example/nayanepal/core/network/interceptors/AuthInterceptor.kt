package com.example.nayanepal.core.network.interceptors

import com.example.nayanepal.core.network.domain.TokenProvider
import okhttp3.Interceptor
import okhttp3.Response
import java.util.UUID
class AuthInterceptor(private val tokenProvider: TokenProvider) : Interceptor {
    override fun intercept(chain: Interceptor.Chain): Response {
        val originalRequest = chain.request()
        val token = tokenProvider.getToken()
        val authenticatedRequest = originalRequest.newBuilder()
            .apply {
                token?.let { header("Authorization", "Bearer $it") }
            }
            .header("X-Request-ID", UUID.randomUUID().toString())
            .build()
        return chain.proceed(authenticatedRequest)
    }
}
