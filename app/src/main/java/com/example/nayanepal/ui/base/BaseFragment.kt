package com.example.nayanepal.ui.base

import android.view.View
import androidx.fragment.app.Fragment
import dagger.hilt.android.AndroidEntryPoint
import com.google.android.material.snackbar.Snackbar
/**
 * Enterprise Base Fragment.
 * Standardizes common UI behaviors like Snackbars, Loading states, and Navigation.
 */
abstract class BaseFragment : Fragment() {
    fun showSnackbar(message: String, isError: Boolean = false) {
        val view = view ?: return
        val snackbar = Snackbar.make(view, message, Snackbar.LENGTH_LONG)
        if (isError) {
            snackbar.setBackgroundTint(resources.getColor(android.R.color.holo_red_dark, null))
        }
        snackbar.show()
    }
    abstract fun showLoading(isLoading: Boolean)
}
