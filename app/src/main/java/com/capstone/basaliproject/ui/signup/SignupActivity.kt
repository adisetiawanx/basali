package com.capstone.basaliproject.ui.signup

import android.content.Intent
import android.os.Bundle
import android.view.View
import androidx.activity.viewModels
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.lifecycleScope
import com.capstone.basaliproject.databinding.ActivitySignupBinding
import com.capstone.basaliproject.ui.ViewModelFactory
import com.capstone.basaliproject.ui.confirm.ConfirmationActivity
import kotlinx.coroutines.launch
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.RequestBody.Companion.toRequestBody

class SignupActivity : AppCompatActivity() {
    private val viewModel by viewModels<SignupViewModel> {
        ViewModelFactory.getInstance(this)
    }

    private lateinit var binding: ActivitySignupBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivitySignupBinding.inflate(layoutInflater)
        setContentView(binding.root)

        setupAction()

        viewModel.isLoading.observe(this) {
            binding.progressBar.visibility = if (it) View.VISIBLE else View.GONE
        }

        viewModel.result.observe(this) {
            if (it.token != null) {
                val email = binding.edRegisterEmail.text
                AlertDialog.Builder(this).apply {
                    setTitle("Yeah!")
                    setMessage("The verification code has been sent to your email $email")
                    setPositiveButton("Next") { _, _ ->
                        val intent = Intent(this@SignupActivity, ConfirmationActivity::class.java)
                        startActivity(intent)
                    }
                    create()
                    show()
                }

            } else {
                AlertDialog.Builder(this).apply {
                    setTitle("Failed!")
                    setMessage("Account cannot be made")
                    setPositiveButton("Next") { _, _ ->
                        finish()
                    }
                    create()
                    show()
                }
            }
        }
    }

    private fun setupAction() {
        binding.registerButton.setOnClickListener {
            val name = binding.edRegisterName.text.toString()
            val email = binding.edRegisterEmail.text.toString()
            val password = binding.edRegisterPassword.text.toString()
            val confirmPassword = binding.edRegisterComfirmPassword.text.toString()

            val json = """
                            {
                                "email": "$email",
                                "password": "$password",
                                "confirmPassword": "$confirmPassword",
                                "name": "$name"
                            }
                        """.trimIndent()

            val requestBody = json.toRequestBody("application/json".toMediaTypeOrNull())

            lifecycleScope.launch {
                viewModel.register(requestBody)
            }
        }
    }
}