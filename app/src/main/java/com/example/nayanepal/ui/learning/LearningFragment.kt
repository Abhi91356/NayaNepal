package com.example.nayanepal.ui.learning

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.lifecycleScope
import com.example.nayanepal.databinding.FragmentLearningBinding
import dagger.hilt.android.AndroidEntryPoint
import com.example.nayanepal.ui.base.BaseFragment
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

/**
 * Enterprise Learning Fragment.
 * Implements Pillar C: Smart Learning & Skills Accelerator.
 */
@AndroidEntryPoint
class LearningFragment : BaseFragment() {
    private var _binding: FragmentLearningBinding? = null
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentLearningBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        
        loadCareerPath()
    }

    private fun loadCareerPath() {
        showLoading(true)
        lifecycleScope.launch {
            // Simulated call to Learning Microservice via API Gateway
            // In real implementation: val courses = firebaseDataSource.getRecommendedCourses()
            delay(1200)
            showLoading(false)
            
            // Skill-to-Job Mapping logic would update the UI here
            showSnackbar("Skills and career paths updated")
        }
    }

    override fun showLoading(isLoading: Boolean) {
        // Shimmer logic inherited from base would be applied here
        binding.rvCourses.alpha = if (isLoading) 0.5f else 1.0f
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
