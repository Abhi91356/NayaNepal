package com.example.nayanepal.ui.home

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.nayanepal.core.auth.domain.model.User
import com.example.nayanepal.data.remote.FirebaseDataSource
import com.google.firebase.auth.FirebaseAuth
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class HomeViewModel @Inject constructor(
    private val firebaseDataSource: FirebaseDataSource,
    private val auth: FirebaseAuth
) : ViewModel() {

    private val _uiState = MutableStateFlow<HomeUiState>(HomeUiState.Loading)
    val uiState: StateFlow<HomeUiState> = _uiState

    init {
        val uid = auth.currentUser?.uid
        if (uid != null) {
            loadData(uid)
        } else {
            _uiState.value = HomeUiState.Error("User not logged in")
        }
    }

    fun loadData(uid: String) {
        viewModelScope.launch {
            _uiState.value = HomeUiState.Loading
            try {
                val balance = firebaseDataSource.getWalletBalance(uid)
                val user = firebaseDataSource.getUserProfile(uid)
                _uiState.value = HomeUiState.Success(user, balance)
            } catch (e: Exception) {
                _uiState.value = HomeUiState.Error(e.message ?: "Unknown error")
            }
        }
    }
}

sealed class HomeUiState {
    object Loading : HomeUiState()
    data class Success(val user: User?, val balance: Double) : HomeUiState()
    data class Error(val message: String) : HomeUiState()
}
