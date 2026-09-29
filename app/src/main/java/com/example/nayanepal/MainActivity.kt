package com.example.nayanepal

import android.Manifest
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.os.Build
import android.os.Bundle
import android.view.View
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import androidx.core.view.WindowCompat
import androidx.navigation.NavController
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.ui.setupWithNavController
import com.example.nayanepal.databinding.ActivityMainBinding
import com.example.nayanepal.ui.onboarding.OnboardingActivity
import com.example.nayanepal.core.designsystem.R as DesignR
import com.example.nayanepal.ui.navigation.SpatialNavigationBar
import com.google.firebase.auth.FirebaseAuth
import dagger.hilt.android.AndroidEntryPoint
import androidx.compose.runtime.*
import androidx.navigation.NavOptions
import androidx.navigation.navOptions

@AndroidEntryPoint
class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding
    private lateinit var navController: NavController

    private val requestNotificationPermissionLauncher = registerForActivityResult(
        ActivityResultContracts.RequestPermission()
    ) { _ -> }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        
        WindowCompat.setDecorFitsSystemWindows(window, false)
        
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val navHostFragment = supportFragmentManager
            .findFragmentById(R.id.nav_host_fragment) as NavHostFragment
        navController = navHostFragment.navController
        
        setupNavigation()

        binding.root.post {
            if (!checkInitialRouting()) {
                checkNotificationPermission()
            }
        }
    }

    private fun setupNavigation() {
        binding.bottomNavigationCompose.setContent {
            var currentDestinationId by remember { mutableStateOf(navController.currentDestination?.id ?: DesignR.id.homeFragment) }
            
            LaunchedEffect(navController) {
                navController.addOnDestinationChangedListener { _, destination, _ ->
                    currentDestinationId = destination.id
                }
            }

            val showNav = when (currentDestinationId) {
                DesignR.id.homeFragment, DesignR.id.profileFragment, DesignR.id.notificationsFragment -> true
                else -> false
            }

            if (showNav) {
                SpatialNavigationBar(
                    currentDestinationId = currentDestinationId,
                    onNavigate = { id ->
                        if (navController.currentDestination?.id != id) {
                            try {
                                navController.navigate(id)
                            } catch (e: Exception) {
                                // Fallback or log
                            }
                        }
                    }
                )
            }
        }
    }

    private fun checkInitialRouting(): Boolean {
        val user = FirebaseAuth.getInstance().currentUser
        val prefs = getSharedPreferences("app_prefs", Context.MODE_PRIVATE)
        val onboardingCompleted = prefs.getBoolean("onboarding_completed", false)

        if (!onboardingCompleted) {
            startActivity(Intent(this, OnboardingActivity::class.java))
            finish()
            return true
        }

        if (user == null) {
            navController.navigate(DesignR.id.loginFragment)
            return false
        }
        
        return false
    }

    private fun checkNotificationPermission() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            if (ContextCompat.checkSelfPermission(this, Manifest.permission.POST_NOTIFICATIONS) !=
                PackageManager.PERMISSION_GRANTED
            ) {
                requestNotificationPermissionLauncher.launch(Manifest.permission.POST_NOTIFICATIONS)
            }
        }
    }

    override fun onSupportNavigateUp(): Boolean {
        return navController.navigateUp() || super.onSupportNavigateUp()
    }
}
