package com.example.nayanepal.feature.wallet.data.local

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
@Dao
interface TransactionDao {
    @Query("SELECT * FROM wallet_transactions WHERE status = 'pending_sync'")
    suspend fun getPendingSyncTransactions(): List<WalletTransactionEntity>
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(transaction: WalletTransactionEntity)
}
