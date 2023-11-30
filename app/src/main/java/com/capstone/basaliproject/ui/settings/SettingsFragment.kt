package com.capstone.basaliproject.ui.settings

import android.content.Intent
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.activity.viewModels
import androidx.appcompat.app.AlertDialog
import androidx.fragment.app.viewModels
import androidx.lifecycle.ViewModelProvider
import com.capstone.basaliproject.databinding.FragmentSettingsBinding
import com.capstone.basaliproject.ui.Logout.LogOutViewModel
import com.capstone.basaliproject.ui.ViewModelFactory
import com.capstone.basaliproject.ui.login.LoginViewModel
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
    ): View {

        viewModel.getSession().observe(requireActivity()) { user ->
            if (!user.isLogin) {
                startActivity(Intent(activity, WelcomeActivity::class.java))
            }
        }

        val settingsViewModel = ViewModelProvider(this).get(SettingsViewModel::class.java)

        _binding = FragmentSettingsBinding.inflate(inflater, container, false)
        val root: View = binding.root
        setupAction()

        val textView: TextView = binding.textSettings
        settingsViewModel.text.observe(viewLifecycleOwner) {
            textView.text = it
        }
        return root

    }

    private fun setupAction() {
        binding.btnLogout.setOnClickListener {
            viewModel.logout()
            AlertDialog.Builder(requireContext()).apply {
                setTitle("Berhasil Logout!")
                setMessage("Anda telah logout")
                setPositiveButton("Lanjut") { _, _ ->
                    val intent = Intent(requireActivity(), WelcomeActivity::class.java)
                    ViewModelFactory.refreshObject()
                    startActivity(intent)
                }
                create()
                show()
            }
        }
    }
}