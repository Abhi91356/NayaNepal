package com.example.nayanepal.ui.splash

import android.content.Intent
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.view.animation.DecelerateInterpolator
import android.widget.ImageView
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.WindowCompat
import com.example.nayanepal.MainActivity
import com.example.nayanepal.R
import com.example.nayanepal.ui.onboarding.OnboardingActivity
import com.google.firebase.auth.FirebaseAuth

class SplashActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        // Full screen, no status bar for splash
        WindowCompat.setDecorFitsSystemWindows(window, false)
        setContentView(R.layout.activity_splash)

        // Animate logo on entry
        val logo = findViewById<ImageView>(R.id.ivLogo)
        val appName = findViewById<TextView>(R.id.tvAppName)
        val tagline = findViewById<TextView>(R.id.tvTagline)

        // Start invisible then fade + slide in
        logo.alpha = 0f
        appName.alpha = 0f
        tagline.alpha = 0f
        logo.translationY = 60f
        appName.translationY = 60f

        logo.animate().alpha(1f).translationY(0f).setDuration(600)
            .setInterpolator(DecelerateInterpolator()).start()
        
        appName.animate().alpha(1f).translationY(0f).setStartDelay(200)
            .setDuration(500).setInterpolator(DecelerateInterpolator()).start()
            
        tagline.animate().alpha(1f).setStartDelay(400).setDuration(500).start()

        // Route user based on auth state after 2 seconds
        Handler(Looper.getMainLooper()).postDelayed({
            routeUser()
        }, 2000)
    }

    private fun routeUser() {
        val user = FirebaseAuth.getInstance().currentUser
        val intent = if (user != null) {
            Intent(this, MainActivity::class.java)
        } else {
            Intent(this, OnboardingActivity::class.java)
        }
        startActivity(intent)
        overridePendingTransition(R.anim.fade_in, R.anim.fade_out) // smooth transition
        finish()
    }
}
