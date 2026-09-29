package com.example.nayanepal.ui.profile

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.platform.ComposeView
import androidx.fragment.app.Fragment
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import com.example.nayanepal.R
import com.example.nayanepal.core.auth.domain.model.User
import com.example.nayanepal.data.remote.FirebaseDataSource
import com.google.android.material.snackbar.Snackbar
import com.google.firebase.analytics.FirebaseAnalytics
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.functions.FirebaseFunctions
import com.google.firebase.storage.FirebaseStorage
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.launch

@AndroidEntryPoint
class ProfileFragment : Fragment() {
    private lateinit var auth: FirebaseAuth
    private val _userState = MutableStateFlow<User?>(null)

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        return ComposeView(requireContext()).apply {
            setContent {
                val user by _userState.collectAsState()
                ProfileScreen(
                    user = user,
                    onEditProfile = { showEditProfileDialog() },
                    onSecurityClick = { resetPassword() },
                    onLogoutClick = {
                        auth.signOut()
                        findNavController().navigate(R.id.action_global_loginFragment)
                    }
                )
            }
        }
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        auth = FirebaseAuth.getInstance()
        loadProfile()
    }

    private fun showEditProfileDialog() {
        val dialogBinding = com.example.nayanepal.databinding.DialogEditProfileBinding.inflate(layoutInflater)
        _userState.value?.let {
            dialogBinding.etEditName.setText(it.name)
            dialogBinding.etEditPhone.setText(it.phone)
        }
        androidx.appcompat.app.AlertDialog.Builder(requireContext())
            .setView(dialogBinding.root)
            .setPositiveButton("Save") { _, _ ->
                val newName = dialogBinding.etEditName.text.toString().trim()
                val newPhone = dialogBinding.etEditPhone.text.toString().trim()
                if (newName.isNotEmpty() && newPhone.isNotEmpty()) {
                    updateProfile(newName, newPhone)
                }
            }
            .setNegativeButton("Cancel", null)
            .show()
    }

    private fun updateProfile(name: String, phone: String) {
        val updatedUser = _userState.value?.copy(name = name, phone = phone) ?: return
        val dataSource = getDataSource()
        lifecycleScope.launch {
            val success = dataSource.saveUserProfile(updatedUser)
            if (success) {
                _userState.value = updatedUser
                Snackbar.make(requireView(), "Profile updated", Snackbar.LENGTH_SHORT).show()
            }
        }
    }

    private fun resetPassword() {
        auth.currentUser?.email?.let { email ->
            auth.sendPasswordResetEmail(email)
                .addOnSuccessListener {
                    Snackbar.make(requireView(), "Reset link sent to $email", Snackbar.LENGTH_LONG).show()
                }
        }
    }

    private fun loadProfile() {
        val uid = auth.currentUser?.uid ?: return
        val dataSource = getDataSource()
        lifecycleScope.launch {
            _userState.value = dataSource.getUserProfile(uid)
        }
    }

    private fun getDataSource(): FirebaseDataSource {
        return FirebaseDataSource(
            auth,
            FirebaseFirestore.getInstance(),
            FirebaseStorage.getInstance(),
            FirebaseAnalytics.getInstance(requireContext()),
            FirebaseFunctions.getInstance()
        )
    }
}
