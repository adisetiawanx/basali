package com.capstone.basaliproject.utils

import androidx.activity.OnBackPressedCallback
import androidx.fragment.app.Fragment

class SetupUtils {
    companion object {
        fun Fragment.closeOnBackPressed() {
            val callback = object : OnBackPressedCallback(true) {
                override fun handleOnBackPressed() {
                    requireActivity().finishAffinity()
                }
            }
            requireActivity().onBackPressedDispatcher.addCallback(viewLifecycleOwner, callback)
        }
    }
}