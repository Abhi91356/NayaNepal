package com.example.nayanepal.ui.verification

import android.net.Uri
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import androidx.work.Constraints
import androidx.work.NetworkType
import androidx.work.OneTimeWorkRequestBuilder
import androidx.work.WorkManager
import coil.load
import com.example.nayanepal.R
import com.example.nayanepal.data.local.AppDatabase
import com.example.nayanepal.data.local.VerificationDocumentEntity
import com.example.nayanepal.data.model.VerificationDocument
import com.example.nayanepal.data.remote.FirebaseDataSource
import com.example.nayanepal.data.sync.SyncWorker
import com.example.nayanepal.databinding.FragmentVerificationBinding
import com.example.nayanepal.core.designsystem.R as DesignR
import com.google.android.material.snackbar.Snackbar
import com.google.firebase.analytics.FirebaseAnalytics
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.functions.FirebaseFunctions
import com.google.firebase.storage.FirebaseStorage
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import java.io.File
import java.io.FileOutputStream
import java.util.UUID

@AndroidEntryPoint
class VerificationFragment : Fragment() {

    private var _binding: FragmentVerificationBinding? = null
    private val binding get() = _binding!!

    private var docType: String? = null
    private var imageUri: Uri? = null

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentVerificationBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.rgDocType.setOnCheckedChangeListener { _, checkedId ->
            docType = when (checkedId) {
                R.id.rbCitizenship -> "citizenship"
                R.id.rbPassport -> "passport"
                R.id.rbNationalId -> "nationalId"
                else -> null
            }
        }

        binding.btnPickImage.setOnClickListener {
            findNavController().navigate(DesignR.id.action_verificationFragment_to_cameraFragment)
        }

        // Receive captured image from CameraFragment
        findNavController().currentBackStackEntry?.savedStateHandle?.getLiveData<String>("capturedUri")
            ?.observe(viewLifecycleOwner) { uriString ->
                if (!uriString.isNullOrEmpty()) {
                    imageUri = Uri.parse(uriString)
                    binding.ivDocPreview.visibility = View.VISIBLE
                    binding.placeholderGroup.visibility = View.GONE
                    binding.ivDocPreview.load(imageUri)
                }
            }

        binding.btnSubmit.setOnClickListener {
            val selectedDocType = docType
            val selectedUri = imageUri
            if (selectedDocType != null && selectedUri != null) {
                submitVerification(selectedDocType, selectedUri)
            } else {
                Snackbar.make(requireView(), "Please select type and image", Snackbar.LENGTH_SHORT).show()
            }
        }
    }

    private fun submitVerification(docType: String, uri: Uri) {
        val auth = FirebaseAuth.getInstance()
        val uid = auth.currentUser?.uid ?: return
        val firestore = FirebaseFirestore.getInstance()
        val storage = FirebaseStorage.getInstance()
        val analytics = FirebaseAnalytics.getInstance(requireContext())
        val functions = FirebaseFunctions.getInstance()
        
        val dataSource = FirebaseDataSource(auth, firestore, storage, analytics, functions)
        val database = AppDatabase.getInstance(requireContext())

        lifecycleScope.launch {
            setLoading(true)
            
            val storagePath = dataSource.uploadDocumentImage(uid, docType, uri)
            
            if (storagePath != null) {
                val doc = VerificationDocument(
                    id = "",
                    userId = uid,
                    documentType = docType,
                    storagePath = storagePath,
                    status = "pending"
                )

                val ok = dataSource.createVerificationDocument(doc)
                if (ok) {
                    saveToRoom(database, uid, docType, storagePath, true)
                    updateUserStatusToPending(dataSource, uid)
                    Snackbar.make(requireView(), "Submitted successfully", Snackbar.LENGTH_LONG).show()
                    findNavController().navigateUp()
                } else {
                    handleOfflineSubmission(database, uid, docType, uri)
                }
            } else {
                handleOfflineSubmission(database, uid, docType, uri)
            }
            
            setLoading(false)
        }
    }

    private fun setLoading(isLoading: Boolean) {
        binding.btnSubmit.isEnabled = !isLoading
        binding.btnPickImage.isEnabled = !isLoading
        binding.progressBar.visibility = if (isLoading) View.VISIBLE else View.GONE
    }

    private suspend fun handleOfflineSubmission(
        database: AppDatabase,
        uid: String,
        docType: String,
        uri: Uri
    ) {
        val localPath = copyFileToInternalStorage(uri, docType)
        if (localPath != null) {
            saveToRoom(database, uid, docType, localPath, false)
            scheduleSync()
            Snackbar.make(requireView(), "Offline: Saved for sync", Snackbar.LENGTH_LONG).show()
            findNavController().navigateUp()
        } else {
            Snackbar.make(requireView(), "Failed to save image locally", Snackbar.LENGTH_LONG).show()
        }
    }

    private suspend fun copyFileToInternalStorage(uri: Uri, docType: String): String? = withContext(Dispatchers.IO) {
        try {
            val inputStream = requireContext().contentResolver.openInputStream(uri) ?: return@withContext null
            val file = File(requireContext().filesDir, "pending_${docType}_${System.currentTimeMillis()}.jpg")
            val outputStream = FileOutputStream(file)
            inputStream.copyTo(outputStream)
            inputStream.close()
            outputStream.close()
            file.absolutePath
        } catch (e: Exception) {
            null
        }
    }

    private suspend fun saveToRoom(
        database: AppDatabase,
        uid: String,
        docType: String,
        path: String,
        isSynced: Boolean
    ) {
        val entity = VerificationDocumentEntity(
            id = UUID.randomUUID().toString(),
            userId = uid,
            documentType = docType,
            storagePath = path,
            status = "pending",
            rejectionReason = null,
            createdAt = System.currentTimeMillis(),
            isSynced = isSynced
        )
        database.verificationDocumentDao().upsertDocument(entity)
    }

    private suspend fun updateUserStatusToPending(dataSource: FirebaseDataSource, uid: String) {
        val user = dataSource.getUserProfile(uid)
        user?.let {
            val updated = it.copy(kycStatus = "pending")
            dataSource.saveUserProfile(updated)
        }
    }

    private fun scheduleSync() {
        val constraints = Constraints.Builder()
            .setRequiredNetworkType(NetworkType.CONNECTED)
            .build()

        val syncRequest = OneTimeWorkRequestBuilder<SyncWorker>()
            .setConstraints(constraints)
            .build()

        WorkManager.getInstance(requireContext()).enqueue(syncRequest)
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
