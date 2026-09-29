package com.example.nayanepal.core.auth.domain.model

/**
 * Enterprise User Model.
 * Aligned with robust PostgreSQL schema including audit-ready fields.
 */
data class User(
    val id: String = "",
    val name: String = "",
    val email: String = "",
    val phone: String = "",
    val language: String = "en",
    val isAdmin: Boolean = false,
    val kycStatus: String = "pending", // pending, approved, rejected
    val isVerified: Boolean = false,
    val isActive: Boolean = true,
    val createdAt: Long = System.currentTimeMillis(),
    val updatedAt: Long = System.currentTimeMillis(),
    val deletedAt: Long? = null // For soft delete audit trail
) {
    // Helper to check if user is active and not deleted
    fun isValid() = isActive && deletedAt == null
}
