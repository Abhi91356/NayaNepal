package com.example.nayanepal.feature.wallet.data

import com.example.nayanepal.feature.wallet.domain.model.Wallet
import com.example.nayanepal.feature.wallet.domain.repository.IWalletRepository
import javax.inject.Inject

class WalletRepositoryImpl @Inject constructor() : IWalletRepository {
    override suspend fun getWalletBalance(): Wallet {
        // In real implementation, call remote and local data sources
        return Wallet("user_123", 12450.00, "NPR")
    }

    override suspend fun sendMoney(recipientId: String, amount: Double): Boolean {
        return true
    }
}
