package com.capstone.basaliproject.ui.login

import android.content.Context
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
import com.capstone.basaliproject.MainActivity
import com.capstone.basaliproject.databinding.ActivityLoginBinding
import com.google.firebase.auth.FirebaseAuth

class LoginActivity : AppCompatActivity() {
    private lateinit var viewModel: LoginViewModel
    private lateinit var binding: ActivityLoginBinding
    private lateinit var edEmail: TextView
    private lateinit var edPassword: TextView
    private lateinit var btnLogin: Button
    private lateinit var progressBar: ProgressBar
    private lateinit var context: Context

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityLoginBinding.inflate(layoutInflater)
        setContentView(binding.root)
        context = this

        viewModel = ViewModelProvider(this).get(LoginViewModel::class.java)

        edEmail = binding.edLoginEmail
        edPassword = binding.edLoginPassword
        btnLogin = binding.loginButton
        progressBar = binding.progressBar

        btnLogin.setOnClickListener {
            val email = edEmail.text.toString()
            val password = edPassword.text.toString()
            if (email.isEmpty()){
                edEmail.error = "Email tidak boleh kosong"
                edEmail.requestFocus()
            } else if (password.isEmpty()){
                edPassword.error = "Password tidak boleh kosong"
                edPassword.requestFocus()
            } else {
                viewModel.login(email, password)
            }
        }

        viewModel.isLoading.observe(this, Observer { isLoading ->
            if (isLoading) {
                progressBar.visibility = View.VISIBLE
            } else {
                progressBar.visibility = View.GONE
            }
        })

        viewModel.isLoginSuccessful.observe(this, Observer { isSuccessful ->
            if (isSuccessful) {
                startActivity(Intent(this@LoginActivity, MainActivity::class.java))
            } else {
                Toast.makeText(context, "Authentication failed.", Toast.LENGTH_SHORT).show()
            }
        })
    }
}
