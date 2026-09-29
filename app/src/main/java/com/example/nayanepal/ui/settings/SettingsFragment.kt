package com.example.nayanepal.ui.settings

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.example.nayanepal.databinding.FragmentSettingsBinding
import dagger.hilt.android.AndroidEntryPoint
import com.example.nayanepal.ui.base.BaseFragment

@AndroidEntryPoint
class SettingsFragment : BaseFragment() {
    private var _binding: FragmentSettingsBinding? = null
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentSettingsBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setupClickListeners()
    }

    private fun setupClickListeners() {
        binding.optLanguage.setOnClickListener {
            showSnackbar("Language selection coming soon")
        }
    }

    override fun showLoading(isLoading: Boolean) {
        // Not needed for static settings page
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
