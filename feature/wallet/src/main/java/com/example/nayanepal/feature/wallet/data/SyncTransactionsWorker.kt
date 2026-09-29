package com.example.nayanepal.feature.wallet.data

import android.content.Context
import androidx.hilt.work.HiltWorker
import androidx.work.CoroutineWorker
import androidx.work.WorkerParameters
import com.example.nayanepal.feature.wallet.domain.repository.IWalletRepository
import dagger.assisted.Assisted
import dagger.assisted.AssistedInject
@HiltWorker
class SyncTransactionsWorker @AssistedInject constructor(
    @Assisted context: Context,
    @Assisted params: WorkerParameters,
    private val walletRepository: IWalletRepository
) : CoroutineWorker(context, params) {
    override suspend fun doWork(): Result = try {
        // In real app: val pending = walletRepository.getPendingSyncTransactions()
        // and sync them.
        Result.success()
    } catch (e: Exception) {
        Result.retry()
    }
}
