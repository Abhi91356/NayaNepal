package com.example.nayanepal.ui.agriculture

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.lifecycleScope
import com.example.nayanepal.databinding.FragmentAgricultureBinding
import dagger.hilt.android.AndroidEntryPoint
import com.example.nayanepal.ui.base.BaseFragment
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

@AndroidEntryPoint
class AgricultureFragment : BaseFragment() {
    private var _binding: FragmentAgricultureBinding? = null
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentAgricultureBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        loadAgriAdvisory()
    }

    private fun loadAgriAdvisory() {
        showLoading(true)
        lifecycleScope.launch {
            // Simulate API call to Agri Microservice
            delay(1000)
            showLoading(false)
            showSnackbar("Weather alerts and crop advice updated")
        }
    }

    override fun showLoading(isLoading: Boolean) {
        binding.rvAdvice.visibility = if (isLoading) View.GONE else View.VISIBLE
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
