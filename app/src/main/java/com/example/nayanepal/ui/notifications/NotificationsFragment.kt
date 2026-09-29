package com.example.nayanepal.ui.notifications

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.lifecycleScope
import com.example.nayanepal.databinding.FragmentNotificationsBinding
import dagger.hilt.android.AndroidEntryPoint
import com.example.nayanepal.ui.base.BaseFragment
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

@AndroidEntryPoint
class NotificationsFragment : BaseFragment() {
    private var _binding: FragmentNotificationsBinding? = null
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentNotificationsBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        loadNotifications()
    }

    private fun loadNotifications() {
        showLoading(true)
        lifecycleScope.launch {
            // Simulate cloud notification fetch
            delay(1500)
            showLoading(false)
            binding.emptyState.visibility = View.VISIBLE
            binding.rvNotifications.visibility = View.GONE
        }
    }

    override fun showLoading(isLoading: Boolean) {
        if (isLoading) {
            binding.shimmerNotifications.visibility = View.VISIBLE
            binding.shimmerNotifications.startShimmer()
            binding.emptyState.visibility = View.GONE
        } else {
            binding.shimmerNotifications.stopShimmer()
            binding.shimmerNotifications.visibility = View.GONE
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
