package com.example.nayanepal.ui.marketplace

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.lifecycleScope
import com.example.nayanepal.databinding.FragmentMarketplaceBinding
import dagger.hilt.android.AndroidEntryPoint
import com.example.nayanepal.ui.base.BaseFragment
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

@AndroidEntryPoint
class MarketplaceFragment : BaseFragment() {
    private var _binding: FragmentMarketplaceBinding? = null
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentMarketplaceBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        loadFeaturedProducts()
    }

    private fun loadFeaturedProducts() {
        showLoading(true)
        lifecycleScope.launch {
            // Simulate API call to Marketplace Microservice
            delay(1500)
            showLoading(false)
            showSnackbar("Marketplace data synced")
        }
    }

    override fun showLoading(isLoading: Boolean) {
        binding.rvProducts.visibility = if (isLoading) View.GONE else View.VISIBLE
        // Note: Real implementation would use ShimmerFrameLayout here
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
