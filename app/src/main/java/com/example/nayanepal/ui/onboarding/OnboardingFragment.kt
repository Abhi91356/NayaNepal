package com.example.nayanepal.ui.onboarding

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.example.nayanepal.R
import com.example.nayanepal.core.designsystem.R as DesignR
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class OnboardingFragment : Fragment() {

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_onboarding, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        view.findViewById<Button>(R.id.btnNext).setOnClickListener {
            findNavController().navigate(DesignR.id.action_onboardingFragment_to_loginFragment)
        }

        view.findViewById<TextView>(R.id.btnSkip).setOnClickListener {
            findNavController().navigate(DesignR.id.action_onboardingFragment_to_loginFragment)
        }
    }
}
