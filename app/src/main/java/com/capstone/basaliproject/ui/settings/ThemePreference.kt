package com.capstone.basaliproject.ui.settings

import android.os.Bundle
import androidx.appcompat.app.AppCompatDelegate
import androidx.preference.ListPreference
import androidx.preference.PreferenceFragmentCompat
import com.capstone.basaliproject.NightMode
import com.capstone.basaliproject.R
import java.util.Locale

class ThemePreference : PreferenceFragmentCompat() {

    override fun onCreatePreferences(savedInstanceState: Bundle?, rootKey: String?) {
        setPreferencesFromResource(R.xml.preferences, rootKey)

        val theme = findPreference<ListPreference>(getString(R.string.pref_key_dark))
        theme?.setOnPreferenceChangeListener{ _, newValue ->
            val mode = NightMode.valueOf(
                newValue.toString().uppercase(
                    Locale.US
                )
            )
            updateTheme(mode.value)
            true
        }
    }

    private fun updateTheme(nightMode: Int): Boolean {
        AppCompatDelegate.setDefaultNightMode(nightMode)
        requireActivity().recreate()
        return true
    }
}