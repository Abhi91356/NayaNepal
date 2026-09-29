package com.example.nayanepal.feature.wallet.presentation.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.nayanepal.feature.wallet.domain.model.Wallet
import com.example.nayanepal.feature.wallet.domain.usecase.GetWalletUseCase
import com.example.nayanepal.feature.wallet.presentation.state.UiState
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject
@HiltViewModel
class WalletViewModel @Inject constructor(
    private val getWalletUseCase: GetWalletUseCase
) : ViewModel() {
    private val _walletState = MutableLiveData<UiState<Wallet>>()
    val walletState: LiveData<UiState<Wallet>> = _walletState
    fun loadWallet() {
        viewModelScope.launch {
            _walletState.value = UiState.Loading
            try {
                val wallet = getWalletUseCase()
                _walletState.value = UiState.Success(wallet)
            } catch (e: Exception) {
                _walletState.value = UiState.Error(e.message ?: "Unknown error")
            }
        }
    }
}
