package com.capstone.basaliproject.ui.settings

import android.content.Intent
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import android.provider.Settings
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.TextView
import androidx.appcompat.app.AlertDialog
import androidx.fragment.app.viewModels
import androidx.lifecycle.ViewModelProvider
import com.capstone.basaliproject.R
import com.capstone.basaliproject.databinding.FragmentSettingsBinding
import com.capstone.basaliproject.ui.logout.LogOutViewModel
import com.capstone.basaliproject.ui.ViewModelFactory
import com.capstone.basaliproject.ui.welcome.WelcomeActivity

class SettingsFragment : Fragment() {
    private val viewModel by viewModels<LogOutViewModel> {
        ViewModelFactory.getInstance(requireContext())
    }
    private var _binding: FragmentSettingsBinding? = null

    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        // Check if the fragment is attached to an activity
        if (!isAdded) {
            return super.onCreateView(inflater, container, savedInstanceState)
        }

        viewModel.getSession().observe(viewLifecycleOwner) { user ->
            if (!user.isLogin) {
                startActivity(Intent(requireActivity(), WelcomeActivity::class.java))
            }
        }

        val settingsViewModel = ViewModelProvider(this)[SettingsViewModel::class.java]

        _binding = FragmentSettingsBinding.inflate(inflater, container, false)
        val root: View = binding.root
        setupAction()

        return root

    }

    private fun setupAction() {
        binding.itemLanguage.setOnClickListener {
            startActivity(Intent(Settings.ACTION_LOCALE_SETTINGS))
        }
        binding.itemLogout.setOnClickListener {
            showCustomDialog(getString(R.string.warning), getString(R.string.yakin_ingin_logout))
        }
    }

    private fun showCustomDialog(titleFill: String, descFill: String) {
        val builder = AlertDialog.Builder(requireContext())

        val customView = LayoutInflater.from(requireContext()).inflate(R.layout.custom_layout_dialog_2_option, null)
        builder.setView(customView)

        val title = customView.findViewById<TextView>(R.id.tv_title)
        val desc = customView.findViewById<TextView>(R.id.tv_desc)
        val btnYes = customView.findViewById<Button>(R.id.yes_btn_id)
        val btnNo = customView.findViewById<Button>(R.id.no_btn_id)

        title.text = titleFill
        desc.text = descFill

        btnYes.setOnClickListener {
            viewModel.logout()
            val intent = Intent(requireActivity(), WelcomeActivity::class.java)
            ViewModelFactory.refreshObject()
            startActivity(intent)
        }



        val dialog = builder.create()
        btnNo.setOnClickListener {
            dialog.cancel()
        }
        dialog.window?.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
        dialog.show()
    }
}