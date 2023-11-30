package com.capstone.basaliproject.ui.login

import android.app.ProgressDialog.show
import android.content.Intent
import android.os.Bundle
import android.view.View
import androidx.activity.viewModels
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.lifecycleScope
import com.capstone.basaliproject.MainActivity
import com.capstone.basaliproject.data.api.response.LoginResponse
import com.capstone.basaliproject.data.pref.UserModel
import com.capstone.basaliproject.databinding.ActivityLoginBinding
import com.capstone.basaliproject.ui.ViewModelFactory
import kotlinx.coroutines.launch
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.RequestBody.Companion.toRequestBody

class LoginActivity : AppCompatActivity() {
    private val viewModel by viewModels<LoginViewModel> {
        ViewModelFactory.getInstance(this)
    }

    private lateinit var binding: ActivityLoginBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityLoginBinding.inflate(layoutInflater)
        setContentView(binding.root)

        setupAction()

        viewModel.isLoading.observe(this) {
            binding.progressBar.visibility = if (it) View.VISIBLE else View.GONE
        }

        viewModel.result.observe(this) {
            if (it.token != null) {
                val email = binding.edLoginEmail.text.toString()
                viewModel.saveSession(UserModel(email, it.token))

                AlertDialog.Builder(this).apply {
                    setTitle("Berhasil Login!")
                    setMessage("Selamat datang $email")
                    setPositiveButton("Lanjut") { _, _ ->
                        val intent = Intent(this@LoginActivity, MainActivity::class.java)
                        ViewModelFactory.refreshObject()
                        startActivity(intent)
                        finish()
                    }
                    create()
                    show()
                }

            } else {
                AlertDialog.Builder(this).apply {
                    setTitle("Gagal!")
                    setMessage("tidak bisa login")
                    setPositiveButton("Lanjut") { _, _ ->
                        finish()
                    }
                    create()
                    show()
                }
            }
        }
    }

    override fun onResume() {
        super.onResume()
        if (viewModel.getSession().isLogin) {
            finish()
        }
    }

    private fun setupAction() {
        binding.loginButton.setOnClickListener {
            val email = binding.edLoginEmail.text.toString()
            val password = binding.edLoginPassword.text.toString()

            val json = """
                            {
                                "email": "$email",
                                "password": "$password"
                            }
                        """.trimIndent()

            val requestBody = json.toRequestBody("application/json".toMediaTypeOrNull())

            lifecycleScope.launch {
                viewModel.login(requestBody)
            }
        }
    }
}