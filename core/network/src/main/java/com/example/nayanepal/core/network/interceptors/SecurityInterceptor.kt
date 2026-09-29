package com.example.nayanepal.core.network.interceptors

import com.example.nayanepal.core.network.security.SecurityUtils
import okhttp3.Interceptor
import okhttp3.Response
import okio.Buffer
/**
 * Enterprise Security Interceptor.
 * Implements HMAC request signing for every outgoing API call.
 * Security: Prevents request tampering and ensures data integrity.
 */
class SecurityInterceptor(private val apiKey: String, private val apiSecret: String) : Interceptor {
    override fun intercept(chain: Interceptor.Chain): Response {
        val originalRequest = chain.request()
        val timestamp = System.currentTimeMillis()
        
        val bodyString = if (originalRequest.body != null) {
            val buffer = Buffer()
            originalRequest.body?.writeTo(buffer)
            buffer.readUtf8()
        } else ""
        val signature = SecurityUtils.signRequest(
            method = originalRequest.method,
            path = originalRequest.url.encodedPath,
            body = bodyString,
            secret = apiSecret,
            timestamp = timestamp
        )
        val secureRequest = originalRequest.newBuilder()
            .header("X-API-KEY", apiKey)
            .header("X-SIGNATURE", signature)
            .header("X-TIMESTAMP", timestamp.toString())
            .build()
        return chain.proceed(secureRequest)
    }
}
