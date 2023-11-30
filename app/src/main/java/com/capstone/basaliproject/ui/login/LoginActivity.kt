package com.capstone.basaliproject.ui.login

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
import com.capstone.basaliproject.MainActivity
import com.capstone.basaliproject.R
import com.capstone.basaliproject.data.pref.UserModel
import com.capstone.basaliproject.databinding.ActivityLoginBinding
import com.capstone.basaliproject.ui.ViewModelFactory
import com.capstone.basaliproject.ui.signup.SignupActivity
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
            if (it.token != null) { val email = binding.edLoginEmail.text.toString()
                viewModel.saveSession(UserModel(it.token))

//                AlertDialog.Builder(this).apply {
//                    setTitle("Berhasil Login!")
//                    setMessage("Selamat datang ")
//                    setPositiveButton("Lanjut") { _, _ ->
////                        val intent = Intent(this@LoginActivity, MainActivity::class.java)
//                        val intent = Intent(this@LoginActivity, MainActivity::class.java)
//                        intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
//                        ViewModelFactory.refreshObject()
//                        startActivity(intent)
//
//                    }
//                    create()
//                    show()
//                }
                showCustomDialog("Berhasil Login!", "Selamat Datang $email!")

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

    private fun showCustomDialog(titleFill: String, descFill: String) {
        val builder = AlertDialog.Builder(this)
        val customView = LayoutInflater.from(this).inflate(R.layout.custom_layout_dialog_1_option, null)
        builder.setView(customView)

        val title = customView.findViewById<TextView>(R.id.tv_title)
        val desc = customView.findViewById<TextView>(R.id.tv_desc)
        val btnNext = customView.findViewById<Button>(R.id.ok_btn_id)

        title.text = titleFill
        desc.text = descFill
        btnNext.setOnClickListener {
            val intent = Intent(this@LoginActivity, MainActivity::class.java)
            startActivity(intent)
            finish()
        }

        val dialog = builder.create()
        dialog.getWindow()?.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT));
        dialog.show()
    }

    //ini bug
    override fun onResume() {
        super.onResume()
        if (viewModel.getSession().isLogin) {
            finish()
        }
    }

    private fun setupAction() {
        binding.tvToRegister.setOnClickListener{
            startActivity(Intent(this@LoginActivity, SignupActivity::class.java))
            finish()
        }
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