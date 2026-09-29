package com.example.nayanepal.feature.wallet.presentation.ui

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import com.example.nayanepal.feature.wallet.databinding.FragmentWalletBinding
import com.example.nayanepal.feature.wallet.presentation.viewmodel.WalletViewModel
import com.example.nayanepal.feature.wallet.presentation.state.UiState
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class WalletScreen : Fragment() {

    private var _binding: FragmentWalletBinding? = null
    private val binding get() = _binding!!

    private val viewModel: WalletViewModel by viewModels()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentWalletBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        observeViewModel()
        viewModel.loadWallet()
    }

    private fun observeViewModel() {
        viewModel.walletState.observe(viewLifecycleOwner) { state ->
            when (state) {
                is UiState.Loading -> {
                    // Show loading
                }
                is UiState.Success -> {
                    // Update UI
                }
                is UiState.Error -> {
                    // Show error
                }
            }
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
