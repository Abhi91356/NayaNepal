package com.example.nayanepal.core.network.security

import android.util.Base64
import javax.crypto.Mac
import javax.crypto.spec.SecretKeySpec

/**
 * Enterprise Security Utilities.
 * Implements HMAC-SHA256 request signing and PII masking.
 * Follows the architecture rule: "Achieve enterprise-grade security and regulatory compliance."
 */
object SecurityUtils {

    // --- PII Masking (Step 2) ---

    fun maskEmail(email: String): String {
        val parts = email.split("@")
        if (parts.size < 2) return "***"
        val name = parts[0]
        val visibleName = if (name.length > 2) name.substring(0, 2) else "*"
        return "$visibleName***@${parts[1]}"
    }

    fun maskPhone(phone: String): String {
        return if (phone.length >= 7) {
            phone.substring(0, 5) + "***" + phone.substring(phone.length - 2)
        } else {
            "***"
        }
    }

    // --- Request Signing (Step 3) ---

    fun signRequest(method: String, path: String, body: String, secret: String, timestamp: Long): String {
        val payload = "$method|$path|$body|$timestamp"
        return hmacSha256(payload, secret)
    }

    private fun hmacSha256(data: String, secret: String): String {
        val hmac = Mac.getInstance("HmacSHA256")
        val secretKey = SecretKeySpec(secret.toByteArray(), "HmacSHA256")
        hmac.init(secretKey)
        val hash = hmac.doFinal(data.toByteArray())
        return Base64.encodeToString(hash, Base64.NO_WRAP)
    }
}
