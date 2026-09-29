package com.example.nayanepal.feature.wallet.domain.usecase

import com.example.nayanepal.feature.wallet.domain.repository.IWalletRepository
import javax.inject.Inject
class GetWalletUseCase @Inject constructor(
    private val repository: IWalletRepository
) {
    suspend operator fun invoke() = repository.getWalletBalance()
}
