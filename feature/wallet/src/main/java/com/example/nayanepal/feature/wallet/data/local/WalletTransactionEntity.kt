package com.example.nayanepal.feature.wallet.data.local

import androidx.room.Entity
import androidx.room.PrimaryKey
@Entity(tableName = "wallet_transactions")
data class WalletTransactionEntity(
    @PrimaryKey val id: String,
    val userId: String,
    val amount: Double,
    val type: String,
    val status: String, // 'pending_sync', 'completed', 'failed'
    val createdAt: Long
)
