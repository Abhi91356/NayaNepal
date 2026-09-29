package com.example.nayanepal.ui.admin

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.nayanepal.data.model.VerificationDocument
import com.example.nayanepal.databinding.FragmentAdminDashboardBinding
import com.example.nayanepal.ui.base.BaseFragment
import com.example.nayanepal.core.designsystem.R as DesignR
import com.google.firebase.firestore.FirebaseFirestore
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class AdminDashboardFragment : BaseFragment() {

    private var _binding: FragmentAdminDashboardBinding? = null
    private val binding get() = _binding!!

    private lateinit var adapter: PendingDocsAdapter
    private lateinit var firestore: FirebaseFirestore

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentAdminDashboardBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        firestore = FirebaseFirestore.getInstance()
        setupRecyclerView()
        loadStats()
        loadPendingDocuments()
    }

    private fun setupRecyclerView() {
        binding.rvPendingDocs.layoutManager = LinearLayoutManager(requireContext())
        adapter = PendingDocsAdapter { doc -> openReview(doc) }
        binding.rvPendingDocs.adapter = adapter
    }

    private fun loadStats() {
        firestore.collection("users").get().addOnSuccessListener { 
            binding.tvTotalUsers.text = it.size().toString()
        }
    }

    private fun loadPendingDocuments() {
        showLoading(true)
        firestore.collection("verification_documents")
            .whereEqualTo("status", "pending")
            .get()
            .addOnSuccessListener { query ->
                val list = query.documents.mapNotNull { it.toObject(VerificationDocument::class.java)?.copy(id = it.id) }
                adapter.submitList(list)
                binding.tvPendingCount.text = list.size.toString()
                showLoading(false)
            }
            .addOnFailureListener {
                showLoading(false)
                showSnackbar("Failed to load requests", true)
            }
    }

    private fun openReview(doc: VerificationDocument) {
        val bundle = Bundle().apply {
            putString("documentId", doc.id)
        }
        findNavController().navigate(DesignR.id.action_adminDashboardFragment_to_adminReviewFragment, bundle)
    }

    override fun showLoading(isLoading: Boolean) {
        // Shimmer logic could be added here
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
