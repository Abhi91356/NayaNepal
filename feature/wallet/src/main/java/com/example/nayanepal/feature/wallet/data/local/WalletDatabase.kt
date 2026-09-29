package com.example.nayanepal.feature.wallet.data.local

import androidx.room.Database
import androidx.room.RoomDatabase
@Database(entities = [WalletTransactionEntity::class], version = 1, exportSchema = false)
abstract class WalletDatabase : RoomDatabase() {
    abstract fun transactionDao(): TransactionDao
}
