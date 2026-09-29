package com.example.nayanepal.ui.tourism

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.example.nayanepal.databinding.FragmentTourismBinding
import dagger.hilt.android.AndroidEntryPoint
import com.example.nayanepal.ui.base.BaseFragment

@AndroidEntryPoint
class TourismFragment : BaseFragment() {
    private var _binding: FragmentTourismBinding? = null
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentTourismBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun showLoading(isLoading: Boolean) {
        // Placeholder for loading logic
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
