package com.example.nayanepal.ui.wallet

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.lifecycleScope
import com.example.nayanepal.databinding.FragmentWalletBinding
import dagger.hilt.android.AndroidEntryPoint
import com.example.nayanepal.ui.base.BaseFragment
import com.google.mlkit.vision.barcode.common.Barcode
import com.google.mlkit.vision.codescanner.GmsBarcodeScannerOptions
import com.google.mlkit.vision.codescanner.GmsBarcodeScanning
import java.util.Locale
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

@AndroidEntryPoint
class WalletFragment : BaseFragment() {
    private var _binding: FragmentWalletBinding? = null
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentWalletBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        loadBalance()
        setupClickListeners()
    }

    private fun setupClickListeners() {
        binding.btnScanQR.setOnClickListener {
            startQRScanner()
        }
        
        binding.btnLoadFund.setOnClickListener {
            showSnackbar("Load Fund feature coming soon")
        }
        binding.btnSendMoney.setOnClickListener {
            showSnackbar("Send Money feature coming soon")
        }
    }

    private fun startQRScanner() {
        val options = GmsBarcodeScannerOptions.Builder()
            .setBarcodeFormats(Barcode.FORMAT_QR_CODE)
            .enableAutoZoom()
            .build()
        val scanner = GmsBarcodeScanning.getClient(requireContext(), options)
        scanner.startScan()
            .addOnSuccessListener { barcode ->
                val rawValue: String? = barcode.rawValue
                if (rawValue != null) {
                    showSnackbar("Scanned: $rawValue")
                }
            }
            .addOnFailureListener { e ->
                showSnackbar("Scanning failed: ${e.message}", true)
            }
    }

    private fun loadBalance() {
        showLoading(true)
        lifecycleScope.launch {
            // Simulate API call
            delay(1200)
            showLoading(false)
        }
    }

    override fun showLoading(isLoading: Boolean) {
        binding.rvTransactions.visibility = if (isLoading) View.GONE else View.VISIBLE
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
