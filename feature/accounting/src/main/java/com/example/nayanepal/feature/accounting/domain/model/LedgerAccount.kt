package com.example.nayanepal.feature.accounting.domain.model

data class LedgerAccount(
    val id: String,
    val name: String,
    val type: AccountType,
    val balance: Double = 0.0
)
enum class AccountType {
    ASSET, LIABILITY, EQUITY, REVENUE, EXPENSE
}
