package com.example.nayanepal.ui.onboarding

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.Button
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.viewpager2.widget.ViewPager2
import com.example.nayanepal.MainActivity
import com.example.nayanepal.R
import com.tbuonomo.viewpagerdotsindicator.WormDotsIndicator

class OnboardingActivity : AppCompatActivity() {
    data class OnboardingPage(
        val lottieRes: Int,
        val title: String,
        val description: String
    )

    private val pages = listOf(
        OnboardingPage(
            R.raw.lottie_security,
            "Verified & Secure",
            "Your identity is protected with bank-grade encryption and document verification."
        ),
        OnboardingPage(
            R.raw.lottie_services,
            "All Services, One App",
            "Access government services, payments, health, education and more in one place."
        ),
        OnboardingPage(
            R.raw.lottie_offline,
            "Works Offline Too",
            "Continue using the app even without internet. We sync everything when you're back online."
        )
    )

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_onboarding)
        
        val viewPager = findViewById<ViewPager2>(R.id.viewPager)
        val indicator = findViewById<WormDotsIndicator>(R.id.indicatorView)
        val btnNext = findViewById<Button>(R.id.btnNext)
        val tvSkip = findViewById<TextView>(R.id.tvSkip)

        viewPager.adapter = OnboardingAdapter(pages)
        indicator.attachTo(viewPager)

        viewPager.registerOnPageChangeCallback(object : ViewPager2.OnPageChangeCallback() {
            override fun onPageSelected(position: Int) {
                val isLast = position == pages.lastIndex
                btnNext.text = if (isLast) "Get Started" else "Next"
                tvSkip.visibility = if (isLast) View.INVISIBLE else View.VISIBLE
            }
        })

        btnNext.setOnClickListener {
            if (viewPager.currentItem == pages.lastIndex) {
                finishOnboarding()
            } else {
                viewPager.currentItem = viewPager.currentItem + 1
            }
        }
        tvSkip.setOnClickListener { finishOnboarding() }
    }

    private fun finishOnboarding() {
        val prefs = getSharedPreferences("app_prefs", Context.MODE_PRIVATE)
        prefs.edit().putBoolean("onboarding_completed", true).apply()
        startActivity(Intent(this, MainActivity::class.java))
        finish()
    }
}
