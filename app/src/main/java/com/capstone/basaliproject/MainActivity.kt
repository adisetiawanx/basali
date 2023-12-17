package com.capstone.basaliproject

import android.content.Intent
import android.content.res.ColorStateList
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import androidx.core.content.ContextCompat
import androidx.navigation.findNavController
import androidx.navigation.ui.setupWithNavController
import com.capstone.basaliproject.databinding.ActivityMainBinding
import com.capstone.basaliproject.ui.welcome.WelcomeActivity
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.google.firebase.auth.FirebaseAuth

class MainActivity : AppCompatActivity() {
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

        navController.addOnDestinationChangedListener { _, destination, _ ->
            when (destination.id) {
                R.id.navigation_home -> showBottomNav()
                R.id.navigation_learn -> showBottomNav()
                R.id.navigation_scan -> showBottomNav()
                R.id.navigation_settings -> showBottomNav()
                else -> hideBottomNav()
            }
        }
    }

    private fun hideBottomNav() {
        binding.navView.visibility = View.GONE
    }

    private fun showBottomNav() {
        binding.navView.visibility = View.VISIBLE
    }



}