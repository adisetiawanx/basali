package com.capstone.basaliproject.ui.settings

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageButton
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.capstone.basaliproject.R

class ThemeFragment : Fragment() {

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_theme, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        if (savedInstanceState == null) {
            val preferenceFragment = ThemePreference()
            childFragmentManager.beginTransaction()
                .replace(R.id.fl_theme, preferenceFragment)
                .commit()
        }

        val toolbarBackButton: ImageButton = view.findViewById(R.id.toolbar_back)
        toolbarBackButton.setOnClickListener {
            findNavController().navigateUp()
        }
    }
}
