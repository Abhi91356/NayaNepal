package com.example.nayanepal.ui.verification

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.example.nayanepal.databinding.FragmentSubmissionConfirmationBinding
import com.example.nayanepal.core.designsystem.R as DesignR
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class SubmissionConfirmationFragment : Fragment() {

    private var _binding: FragmentSubmissionConfirmationBinding? = null
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentSubmissionConfirmationBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        
        binding.root.setOnClickListener {
            findNavController().navigate(DesignR.id.action_submissionConfirmationFragment_to_homeFragment)
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
