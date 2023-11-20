package com.capstone.basaliproject

import android.content.res.ColorStateList
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.core.content.ContextCompat
import androidx.navigation.findNavController
import androidx.navigation.ui.setupWithNavController
import com.google.android.material.bottomnavigation.BottomNavigationView

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

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