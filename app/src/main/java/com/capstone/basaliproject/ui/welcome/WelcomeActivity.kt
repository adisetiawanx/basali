package com.capstone.basaliproject.ui.welcome

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.activity.viewModels
import com.capstone.basaliproject.MainActivity
import com.capstone.basaliproject.R
import com.capstone.basaliproject.databinding.ActivityWelcomeBinding
import com.capstone.basaliproject.ui.ViewModelFactory
import com.capstone.basaliproject.ui.login.LoginActivity
import com.capstone.basaliproject.ui.login.LoginViewModel
import com.capstone.basaliproject.ui.signup.SignupActivity

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

    override fun onResume() {
        super.onResume()
        if (viewModel.getSession().isLogin) {
            startActivity(Intent(this, MainActivity::class.java))

        } else {

        }
    }

    @Deprecated("Deprecated in Java")
    override fun onBackPressed() {
        finishAffinity()
        super.onBackPressed()
    }
}