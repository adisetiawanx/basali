package com.capstone.basaliproject.ui.signup

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.Button
import android.widget.ProgressBar
import android.widget.TextView
import android.widget.Toast
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import com.capstone.basaliproject.R
import com.capstone.basaliproject.databinding.ActivitySignupBinding
import com.capstone.basaliproject.ui.login.LoginActivity

class SignupActivity : AppCompatActivity() {
    private lateinit var viewModel: SignupViewModel
    private lateinit var binding: ActivitySignupBinding
    private lateinit var edUserName: TextView
    private lateinit var edEmail: TextView
    private lateinit var edPassword: TextView
    private lateinit var edConfirmPass: TextView
    private lateinit var btnRegis: Button
    private lateinit var progressBar: ProgressBar

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivitySignupBinding.inflate(layoutInflater)
        setContentView(binding.root)

        viewModel = ViewModelProvider(this).get(SignupViewModel::class.java)

        edUserName = binding.edRegisterName
        edEmail = binding.edRegisterEmail
        edPassword = binding.edRegisterPassword
        edConfirmPass = binding.edRegisterComfirmPassword
        btnRegis = binding.registerButton
        progressBar = binding.progressBar

        btnRegis.setOnClickListener {
            val userName = edUserName.text.toString()
            val email = edEmail.text.toString()
            val password = edPassword.text.toString()
            val confirmPass = edConfirmPass.text.toString()

            if (userName.isEmpty() || email.isEmpty() || password.isEmpty()) {
                Toast.makeText(applicationContext, "data tidak boleh kosong", Toast.LENGTH_SHORT)
                    .show()
            } else if (!confirmPass.equals(password)) {
                Toast.makeText(
                    applicationContext,
                    "konfirmasi password tidak sesuai",
                    Toast.LENGTH_SHORT
                ).show()
            } else {
                viewModel.signup(userName, email, password)
            }
        }

        viewModel.isLoading.observe(this, Observer { isLoading ->
            if (isLoading) {
                progressBar.visibility = View.VISIBLE
            } else {
                progressBar.visibility = View.GONE
            }
        })

        viewModel.isSignupSuccessful.observe(this, Observer { isSuccessful ->
            if (isSuccessful) {
                Toast.makeText(
                    applicationContext,
                    "register sukses",
                    Toast.LENGTH_SHORT
                ).show()
                startActivity(
                    Intent(
                        this@SignupActivity,
                        LoginActivity::class.java
                    )
                )
            } else {
                Toast.makeText(
                    baseContext,
                    "Authentication failed or email already exists.",
                    Toast.LENGTH_SHORT
                ).show()
            }
        })
    }
}
