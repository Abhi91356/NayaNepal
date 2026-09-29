package com.example.nayanepal.feature.wallet.di

import android.content.Context
import androidx.room.Room
import com.example.nayanepal.feature.wallet.data.WalletRepositoryImpl
import com.example.nayanepal.feature.wallet.data.local.TransactionDao
import com.example.nayanepal.feature.wallet.data.local.WalletDatabase
import com.example.nayanepal.feature.wallet.domain.repository.IWalletRepository
import dagger.Binds
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
abstract class WalletModule {

    @Binds
    @Singleton
    abstract fun bindWalletRepository(
        walletRepositoryImpl: WalletRepositoryImpl
    ): IWalletRepository

    companion object {
        @Provides
        @Singleton
        fun provideWalletDatabase(@ApplicationContext context: Context): WalletDatabase {
            return Room.databaseBuilder(
                context,
                WalletDatabase::class.java,
                "wallet_db"
            ).build()
        }

        @Provides
        @Singleton
        fun provideTransactionDao(database: WalletDatabase): TransactionDao {
            return database.transactionDao()
        }
    }
}
