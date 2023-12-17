package com.capstone.basaliproject.ui.signup

import android.content.Intent
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.widget.Button
import android.widget.TextView
import androidx.activity.viewModels
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.lifecycleScope
import com.capstone.basaliproject.R
import com.capstone.basaliproject.databinding.ActivitySignupBinding
import com.capstone.basaliproject.ui.ViewModelFactory
import com.capstone.basaliproject.ui.confirm.ConfirmationActivity
import com.capstone.basaliproject.ui.login.LoginActivity
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


        //sementara intent ke login bukan ke verif code
        viewModel.result.observe(this) {
            if (it.userId != null) {
                val email = binding.edRegisterEmail.text
                showCustomDialog()

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

    private fun showCustomDialog() {
        val builder = AlertDialog.Builder(this)
        val customView = LayoutInflater.from(this).inflate(R.layout.custom_layout_dialog_1_option, null)
        builder.setView(customView)

        val title = customView.findViewById<TextView>(R.id.tv_title)
        val desc = customView.findViewById<TextView>(R.id.tv_desc)
        val btnNext = customView.findViewById<Button>(R.id.ok_btn_id)

        title.text = "Yeah!"
        desc.text = "Register success! Go to Login page"
        btnNext.setOnClickListener {
            val intent = Intent(this@SignupActivity, LoginActivity::class.java)
            startActivity(intent)
            finish()
        }

        val dialog = builder.create()
        dialog.getWindow()?.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT));
        dialog.show()
    }

    private fun setupAction() {

        binding.tvToLogin.setOnClickListener {
            startActivity(Intent(this@SignupActivity, LoginActivity::class.java))
            finish()
        }

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