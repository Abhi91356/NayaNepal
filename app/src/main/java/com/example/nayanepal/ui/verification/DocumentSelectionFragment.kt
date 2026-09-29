package com.example.nayanepal.ui.verification

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.example.nayanepal.databinding.FragmentDocumentSelectionBinding
import com.example.nayanepal.core.designsystem.R as DesignR
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class DocumentSelectionFragment : Fragment() {

    private var _binding: FragmentDocumentSelectionBinding? = null
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentDocumentSelectionBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        
        binding.root.setOnClickListener {
            findNavController().navigate(DesignR.id.action_documentSelectionFragment_to_documentCaptureFragment)
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
