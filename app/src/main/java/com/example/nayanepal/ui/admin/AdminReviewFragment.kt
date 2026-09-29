package com.example.nayanepal.ui.admin

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import coil.load
import com.example.nayanepal.data.model.VerificationDocument
import com.example.nayanepal.databinding.FragmentAdminReviewBinding
import dagger.hilt.android.AndroidEntryPoint
import com.example.nayanepal.ui.base.BaseFragment
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.storage.FirebaseStorage

@AndroidEntryPoint
class AdminReviewFragment : BaseFragment() {
    private var _binding: FragmentAdminReviewBinding? = null
    private val binding get() = _binding!!
    private lateinit var firestore: FirebaseFirestore
    private var docId: String? = null

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentAdminReviewBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        firestore = FirebaseFirestore.getInstance()
        docId = arguments?.getString("documentId")
        loadDocumentDetails()
        setupClickListeners()
    }

    private fun loadDocumentDetails() {
        val id = docId ?: return
        firestore.collection("verification_documents").document(id).get()
            .addOnSuccessListener { snap ->
                val doc = snap.toObject(VerificationDocument::class.java)
                doc?.let {
                    val storageRef = FirebaseStorage.getInstance().reference.child(it.storagePath)
                    storageRef.downloadUrl.addOnSuccessListener { uri ->
                        binding.ivDocumentLarge.load(uri)
                    }
                }
            }
    }

    private fun setupClickListeners() {
        binding.btnApprove.setOnClickListener { updateStatus("verified") }
        binding.btnReject.setOnClickListener { updateStatus("rejected") }
    }

    private fun updateStatus(status: String) {
        val id = docId ?: return
        showLoading(true)
        firestore.collection("verification_documents").document(id)
            .update("status", status)
            .addOnSuccessListener {
                showLoading(false)
                showSnackbar("Document $status")
                // In a real app, update user profile status here or via Cloud Function
            }
    }

    override fun showLoading(isLoading: Boolean) {
        // Basic loading logic
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
