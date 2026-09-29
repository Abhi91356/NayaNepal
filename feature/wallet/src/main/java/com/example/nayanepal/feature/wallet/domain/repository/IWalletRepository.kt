package com.example.nayanepal.feature.wallet.domain.repository

import com.example.nayanepal.feature.wallet.domain.model.Wallet
import com.example.nayanepal.feature.wallet.domain.model.Transaction
interface IWalletRepository {
    suspend fun getWalletBalance(): Wallet
    suspend fun sendMoney(recipientId: String, amount: Double): Boolean
}
