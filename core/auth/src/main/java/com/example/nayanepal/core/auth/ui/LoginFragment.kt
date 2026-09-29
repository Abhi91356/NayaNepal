package com.example.nayanepal.core.auth.ui

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.compose.ui.platform.ComposeView
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.example.nayanepal.core.auth.presentation.ui.LoginScreen
import com.example.nayanepal.core.designsystem.R as DesignR
import dagger.hilt.android.AndroidEntryPoint
@AndroidEntryPoint
class LoginFragment : Fragment() {
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        return ComposeView(requireContext()).apply {
            setContent {
                LoginScreen(
                    onLoginSuccess = { token ->
                        // Navigate to home
                        findNavController().navigate(DesignR.id.action_loginFragment_to_homeFragment)
                    },
                    onNavigateToRegister = {
                        findNavController().navigate(DesignR.id.action_loginFragment_to_registerFragment)
                    }
                )
            }
        }
    }
}
