package com.example.nayanepal.ui.splash

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import dagger.hilt.android.AndroidEntryPoint
import androidx.navigation.fragment.findNavController
import com.example.nayanepal.R
import com.example.nayanepal.core.designsystem.R as DesignR
import com.google.firebase.auth.FirebaseAuth

@AndroidEntryPoint
class SplashFragment : Fragment() {
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_splash, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        
        routeUser()
    }

    private fun routeUser() {
        val user = FirebaseAuth.getInstance().currentUser
        if (user != null) {
            findNavController().navigate(DesignR.id.homeFragment)
        } else {
            findNavController().navigate(DesignR.id.loginFragment)
        }
    }
}
