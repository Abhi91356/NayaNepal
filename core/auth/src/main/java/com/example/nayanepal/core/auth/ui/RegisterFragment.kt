package com.example.nayanepal.core.auth.ui

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.compose.ui.platform.ComposeView
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.example.nayanepal.core.auth.presentation.ui.RegisterScreen
import com.example.nayanepal.core.designsystem.R as DesignR
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class RegisterFragment : Fragment() {

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        return ComposeView(requireContext()).apply {
            setContent {
                RegisterScreen(
                    onRegisterSuccess = {
                        findNavController().navigate(DesignR.id.action_registerFragment_to_homeFragment)
                    },
                    onNavigateToLogin = {
                        findNavController().popBackStack()
                    }
                )
            }
        }
    }
}
