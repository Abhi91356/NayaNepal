package com.example.nayanepal.data.model

data class VerificationDocument(
    val id: String = "",
    val userId: String = "",
    val documentType: String = "", // citizenship, passport, nationalId
    val storagePath: String = "",
    val status: String = "pending", // pending, approved, rejected
    val rejectionReason: String? = null,
    val createdAt: Long = System.currentTimeMillis()
)
