package com.example.nayanepal.ui.auth

import androidx.test.espresso.Espresso.onView
import androidx.test.espresso.action.ViewActions.*
import androidx.test.espresso.assertion.ViewAssertions.matches
import androidx.test.espresso.matcher.ViewMatchers.*
import androidx.test.ext.junit.rules.ActivityScenarioRule
import androidx.test.ext.junit.runners.AndroidJUnit4
import com.example.nayanepal.MainActivity
import com.example.nayanepal.core.auth.R as AuthR
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
@RunWith(AndroidJUnit4::class)
class LoginFragmentTest {
    @get:Rule
    val activityRule = ActivityScenarioRule(MainActivity::class.java)
    @Test
    fun testLoginValidation_InvalidEmail() {
        // Find the email input using the core:auth resource ID
        onView(withId(AuthR.id.etEmail)).perform(typeText("invalid-email"), closeSoftKeyboard())
        
        onView(withId(AuthR.id.btnLogin)).perform(click())
        // Match the error message
        onView(withText("Enter a valid email address"))
            .check(matches(isDisplayed()))
    }
}
