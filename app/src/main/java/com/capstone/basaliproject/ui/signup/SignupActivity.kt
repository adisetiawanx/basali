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
import androidx.appcompat.app.AppCompatDelegate
import androidx.lifecycle.lifecycleScope
import com.capstone.basaliproject.R
import com.capstone.basaliproject.databinding.ActivitySignupBinding
import com.capstone.basaliproject.ui.ViewModelFactory
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

        setupSignupTextColor()
        setupAction()

        viewModel.isLoading.observe(this) {
            binding.progressBar.visibility = if (it) View.VISIBLE else View.GONE
        }

        // sementara intent ke login bukan ke verif code
        viewModel.result.observe(this) {
            if (it.userId != null) {
                binding.edRegisterEmail.text
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

        title.text = getString(R.string.yeah)
        desc.text = getString(R.string.register_success_go_to_login_page)
        btnNext.setOnClickListener {
            val intent = Intent(this@SignupActivity, LoginActivity::class.java)
            startActivity(intent)
            finish()
        }

        val dialog = builder.create()
        dialog.window?.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
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
            val emailError = binding.edRegisterEmail.isError
            val passwordError = binding.edRegisterPassword.isError

            if (name.isEmpty() || email.isEmpty() || password.isEmpty() || confirmPassword.isEmpty()){
                val builder = AlertDialog.Builder(this)
                val customView = LayoutInflater.from(this).inflate(R.layout.custom_layout_dialog_1_option, null)
                builder.setView(customView)
                val dialog = builder.create()

                val title = customView.findViewById<TextView>(R.id.tv_title)
                val desc = customView.findViewById<TextView>(R.id.tv_desc)
                val btnNext = customView.findViewById<Button>(R.id.ok_btn_id)

                title.text = getString(R.string.failed)
                desc.text = getString(R.string.please_fill_all_the_field)
                btnNext.setOnClickListener {
                    dialog.dismiss()
                }
                dialog.window?.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
                dialog.show()
            } else if (emailError) {
                val builder = AlertDialog.Builder(this)
                val customView = LayoutInflater.from(this).inflate(R.layout.custom_layout_dialog_1_option, null)
                builder.setView(customView)
                val dialog = builder.create()

                val title = customView.findViewById<TextView>(R.id.tv_title)
                val desc = customView.findViewById<TextView>(R.id.tv_desc)
                val btnNext = customView.findViewById<Button>(R.id.ok_btn_id)

                title.text = getString(R.string.failed)
                desc.text = getString(R.string.please_use_the_correct_email)
                btnNext.setOnClickListener {
                    dialog.dismiss()
                }
                dialog.window?.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
                dialog.show()
            } else if (passwordError) {
                val builder = AlertDialog.Builder(this)
                val customView = LayoutInflater.from(this).inflate(R.layout.custom_layout_dialog_1_option, null)
                builder.setView(customView)
                val dialog = builder.create()

                val title = customView.findViewById<TextView>(R.id.tv_title)
                val desc = customView.findViewById<TextView>(R.id.tv_desc)
                val btnNext = customView.findViewById<Button>(R.id.ok_btn_id)

                title.text = getString(R.string.failed)
                desc.text = getString(R.string.password_is_less_than_8_character)
                btnNext.setOnClickListener {
                    dialog.dismiss()
                }
                dialog.window?.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
                dialog.show()
            } else if (confirmPassword != password) {
                val builder = AlertDialog.Builder(this)
                val customView = LayoutInflater.from(this).inflate(R.layout.custom_layout_dialog_1_option, null)
                builder.setView(customView)
                val dialog = builder.create()

                val title = customView.findViewById<TextView>(R.id.tv_title)
                val desc = customView.findViewById<TextView>(R.id.tv_desc)
                val btnNext = customView.findViewById<Button>(R.id.ok_btn_id)

                title.text = getString(R.string.failed)
                desc.text = getString(R.string.confirmation_password_is_not_match)
                btnNext.setOnClickListener {
                    dialog.dismiss()
                }
                dialog.window?.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
                dialog.show()
            } else {
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

    private fun setupSignupTextColor(){
        AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_FOLLOW_SYSTEM)

        when (resources.configuration.uiMode and android.content.res.Configuration.UI_MODE_NIGHT_MASK) {
            android.content.res.Configuration.UI_MODE_NIGHT_NO -> {
                findViewById<TextView>(R.id.titleTextView).setTextColor(getColor(R.color.NeutralDarkDarkest))
                findViewById<TextView>(R.id.messageTextView).setTextColor(getColor(R.color.NeutralDarkLight))
                findViewById<TextView>(R.id.nameTextView).setTextColor(getColor(R.color.NeutralDarkDark))
                findViewById<TextView>(R.id.emailTextView).setTextColor(getColor(R.color.NeutralDarkDark))
                findViewById<TextView>(R.id.passwordTextView).setTextColor(getColor(R.color.NeutralDarkDark))
                findViewById<TextView>(R.id.confirmPasswordTextView).setTextColor(getColor(R.color.NeutralDarkDark))
                findViewById<TextView>(R.id.tv_askToLogin).setTextColor(getColor(R.color.NeutralDarkLight))
            }
            android.content.res.Configuration.UI_MODE_NIGHT_YES -> {
                findViewById<TextView>(R.id.titleTextView).setTextColor(getColor(R.color.NeutralLightDark))
                findViewById<TextView>(R.id.messageTextView).setTextColor(getColor(R.color.NeutralLightLight))
                findViewById<TextView>(R.id.nameTextView).setTextColor(getColor(R.color.NeutralLightLight))
                findViewById<TextView>(R.id.emailTextView).setTextColor(getColor(R.color.NeutralLightLight))
                findViewById<TextView>(R.id.passwordTextView).setTextColor(getColor(R.color.NeutralLightLight))
                findViewById<TextView>(R.id.confirmPasswordTextView).setTextColor(getColor(R.color.NeutralLightLight))
                findViewById<TextView>(R.id.tv_askToLogin).setTextColor(getColor(R.color.NeutralDarkLightest))
            }
        }
    }
}