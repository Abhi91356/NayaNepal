package com.example.nayanepal.feature.accounting.ui

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.example.nayanepal.feature.accounting.databinding.FragmentAccountingDashboardBinding
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class AccountingDashboardFragment : Fragment() {

    private var _binding: FragmentAccountingDashboardBinding? = null
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentAccountingDashboardBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        
        setupClickListeners()
    }

    private fun setupClickListeners() {
        binding.btnAddTransaction.setOnClickListener {
            // Logic to open Add Transaction dialog
        }
        
        binding.btnGenerateReport.setOnClickListener {
            // Logic to trigger PDF/Excel export
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
