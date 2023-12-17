package com.capstone.basaliproject.ui.welcome

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.TextView
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatDelegate
import com.capstone.basaliproject.MainActivity
import com.capstone.basaliproject.R
import com.capstone.basaliproject.databinding.ActivityWelcomeBinding
import com.capstone.basaliproject.ui.ViewModelFactory
import com.capstone.basaliproject.ui.login.LoginActivity
import com.capstone.basaliproject.ui.login.LoginViewModel
import com.capstone.basaliproject.ui.signup.SignupActivity
import com.google.firebase.auth.FirebaseAuth

class WelcomeActivity : AppCompatActivity() {
    private val viewModel by viewModels<LoginViewModel> {
        ViewModelFactory.getInstance(this)
    }

    private lateinit var binding: ActivityWelcomeBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityWelcomeBinding.inflate(layoutInflater)
        setContentView(binding.root)

        supportActionBar?.hide()

        setupWelcomeTextColor()
        setupAction()
    }

    private fun setupAction() {
        binding.signUpButton.setOnClickListener {
            startActivity(Intent(this, SignupActivity::class.java))
        }

        binding.loginButton.setOnClickListener {
            startActivity(Intent(this, LoginActivity::class.java))
        }
    }

    private fun setupWelcomeTextColor(){
        AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_FOLLOW_SYSTEM)

        when (resources.configuration.uiMode and android.content.res.Configuration.UI_MODE_NIGHT_MASK) {
            android.content.res.Configuration.UI_MODE_NIGHT_NO -> {
                findViewById<TextView>(R.id.tv_hello_description).setTextColor(getColor(R.color.NeutralDarkMid))
            }
            android.content.res.Configuration.UI_MODE_NIGHT_YES -> {
                findViewById<TextView>(R.id.tv_hello_description).setTextColor(getColor(R.color.NeutralLightLight))
            }
        }
    }

    @Deprecated("Deprecated in Java")
    override fun onBackPressed() {
        finishAffinity()
        super.onBackPressed()
    }

    override fun onStart() {
        super.onStart()
        // Check if user is signed in (non-null) and update UI accordingly.
        val currentUser = FirebaseAuth.getInstance().currentUser
        if (currentUser != null){
            startActivity(Intent(this@WelcomeActivity, MainActivity::class.java))
            finish()
        }
    }
}