package com.example.nayanepal.ui.home

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.compose.ui.platform.ComposeView
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.example.nayanepal.core.designsystem.R as DesignR
import com.google.firebase.auth.FirebaseAuth
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class HomeFragment : Fragment() {

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        return ComposeView(requireContext()).apply {
            setContent {
                HomeScreen(
                    onFeatureClick = { /* Handle Feature Clicks */ },
                    onProfileClick = {
                        findNavController().navigate(DesignR.id.profileFragment)
                    },
                    onNotificationsClick = {
                        findNavController().navigate(DesignR.id.notificationsFragment)
                    }
                )
            }
        }
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        // Optionally trigger data load if not handled by ViewModel init
    }
}
