package com.example.nayanepal.feature.wallet.domain.model

data class Transaction(
    val id: String,
    val amount: Double,
    val type: String,
    val timestamp: Long
)
