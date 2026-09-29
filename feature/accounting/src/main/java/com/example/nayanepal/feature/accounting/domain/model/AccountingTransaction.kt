package com.example.nayanepal.feature.accounting.domain.model

import java.time.Instant
data class AccountingTransaction(
    val id: String,
    val date: Instant,
    val description: String,
    val amount: Double,
    val type: TransactionType,
    val category: String,
    val taxAmount: Double = 0.0,
    val metadata: Map<String, String> = emptyMap()
)
enum class TransactionType {
    SALE, PURCHASE, EXPENSE
}
