package com.capstone.basaliproject

import android.content.Intent
import android.content.res.ColorStateList
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.activity.viewModels
import androidx.core.content.ContextCompat
import androidx.fragment.app.viewModels
import androidx.navigation.findNavController
import androidx.navigation.ui.setupWithNavController
import com.capstone.basaliproject.databinding.ActivityMainBinding
import com.capstone.basaliproject.ui.Logout.LogOutViewModel
import com.capstone.basaliproject.ui.ViewModelFactory
import com.capstone.basaliproject.ui.home.HomeFragment
import com.capstone.basaliproject.ui.learn.LearnFragment
import com.capstone.basaliproject.ui.login.LoginActivity
import com.capstone.basaliproject.ui.login.LoginViewModel
import com.capstone.basaliproject.ui.scan.ScanFragment
import com.capstone.basaliproject.ui.settings.SettingsFragment
import com.capstone.basaliproject.ui.welcome.WelcomeActivity
import com.google.android.material.bottomnavigation.BottomNavigationView

class MainActivity : AppCompatActivity() {
    private val viewModel by viewModels<LoginViewModel> {
        ViewModelFactory.getInstance(this)
    }
    private lateinit var binding: ActivityMainBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        setupBottomNav()


    }
    private fun setupBottomNav(){
        val navView: BottomNavigationView = findViewById(R.id.nav_view)
        val colorStateList = ColorStateList(
            arrayOf(
                intArrayOf(android.R.attr.state_checked),
                intArrayOf(-android.R.attr.state_checked)
            ),
            intArrayOf(
                ContextCompat.getColor(this, R.color.HighlightDark),
                ContextCompat.getColor(this, R.color.NeutralLightDark)
            )
        )

        val navController = findNavController(R.id.nav_host_fragment_activity_main)
        navView.setupWithNavController(navController)

        navView.itemIconTintList = colorStateList
        navView.isItemActiveIndicatorEnabled = false
        navView.itemTextColor = colorStateList
    }

}