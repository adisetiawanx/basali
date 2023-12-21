package com.capstone.basaliproject.ui.login

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.widget.Button
import android.widget.ProgressBar
import android.widget.TextView
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.app.AppCompatDelegate
import androidx.lifecycle.ViewModelProvider
import com.capstone.basaliproject.MainActivity
import com.capstone.basaliproject.R
import com.capstone.basaliproject.databinding.ActivityLoginBinding
import com.capstone.basaliproject.ui.confirm.ConfirmationActivity
import com.capstone.basaliproject.ui.signup.SignupActivity
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.auth.api.signin.GoogleSignInAccount
import com.google.firebase.auth.FirebaseAuth
import com.google.android.gms.auth.api.signin.GoogleSignInClient
import com.google.android.gms.auth.api.signin.GoogleSignInOptions
import com.google.android.gms.tasks.Task
import com.google.firebase.auth.GoogleAuthProvider

class LoginActivity : AppCompatActivity() {
    private lateinit var viewModel: LoginViewModel
    private lateinit var binding: ActivityLoginBinding
    private lateinit var edEmail: TextView
    private lateinit var edPassword: TextView
    private lateinit var btnLogin: Button
    private lateinit var progressBar: ProgressBar
    private lateinit var context: Context
    private lateinit var auth : FirebaseAuth
    private lateinit var googleSignInClient: GoogleSignInClient


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityLoginBinding.inflate(layoutInflater)
        setContentView(binding.root)
        context = this

        setupLoginTextColor()

        viewModel = ViewModelProvider(this)[LoginViewModel::class.java]

        edEmail = binding.edLoginEmail
        edPassword = binding.edLoginPassword
        btnLogin = binding.loginButton
        progressBar = binding.progressBar

        btnLogin.setOnClickListener {
            val email = edEmail.text.toString()
            val password = edPassword.text.toString()
            if (email.isEmpty()) {
                edEmail.error = getString(R.string.email_cannot_be_empty)
                edEmail.requestFocus()
            } else if (password.isEmpty()) {
                edPassword.error = getString(R.string.password_cannot_be_empty)
                edPassword.requestFocus()
            } else {
                viewModel.login(email, password)
            }
        }

        viewModel.isLoading.observe(this) { isLoading ->
            if (isLoading) {
                progressBar.visibility = View.VISIBLE
            } else {
                progressBar.visibility = View.GONE
            }
        }

        viewModel.isLoginSuccessful.observe(this) { isSuccessful ->
            if (isSuccessful) {
                val email = edEmail.text.toString()
                showCustomDialog(getString(R.string.welcome), email)
                viewModel.isEmailVerified.observe(this) { isEmailVerified ->
                    if (isEmailVerified == false) {
                        // Navigate to ConfirmationActivity
                        showCustomDialogVerify(getString(R.string.verify_your_email),
                            getString(R.string.click_to_verify_the_email))
                    } else {
                        val email = edEmail.text.toString()
                        showCustomDialog(getString(R.string.welcome), email)

                        val user = auth.currentUser
                        user?.getIdToken(true)
                            ?.addOnCompleteListener { task ->
                                if (task.isSuccessful) {
                                    val token = task.result?.token
                                    Log.d("FirebaseToken", "Firebase Token: $token")
                                } else {
                                    Log.e("FirebaseToken", "Error getting Firebase Token: ${task.exception}")
                                }
                            }
                    }
                }
            } else {
                Toast.makeText(context,
                    getString(R.string.authentication_failed), Toast.LENGTH_SHORT).show()
                showCustomDialogFailed(
                    getString(R.string.login_failed),
                    getString(R.string.authentication_failed_or_account_didn_t_exist)
                )
            }
        }

        //google sign in
        auth = FirebaseAuth.getInstance()

        val gso = GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
            .requestIdToken(getString(R.string.default_web_client_id))
            .requestEmail()
            .build()

        googleSignInClient = GoogleSignIn.getClient(this, gso)

        binding.google?.setOnClickListener {
            signInGoogle()
        }

        binding.tvToRegister.setOnClickListener {
            startActivity(Intent(this, SignupActivity::class.java))
        }
    }

    private fun signInGoogle() {
        val signInIntent = googleSignInClient.signInIntent
        launcher.launch(signInIntent)
        progressBar = binding.progressBar
        progressBar.visibility = View.VISIBLE
    }

    private val launcher = registerForActivityResult(ActivityResultContracts.StartActivityForResult()){ result ->
        if (result.resultCode == Activity.RESULT_OK){
            val task = GoogleSignIn.getSignedInAccountFromIntent(result.data)
            handleResult(task)
        }
    }

    private fun handleResult(task: Task<GoogleSignInAccount>){

        if (task.isSuccessful){
            val account : GoogleSignInAccount? = task.result
            if (account != null){
                progressBar.visibility = View.GONE
                updateUI(account)
            }
        }else{
            Toast.makeText(this, task.exception.toString(), Toast.LENGTH_SHORT).show()
        }
    }

    private fun updateUI(account: GoogleSignInAccount){
        val credential = GoogleAuthProvider.getCredential(account.idToken, null)
        auth.signInWithCredential(credential).addOnCompleteListener{
            if (it.isSuccessful){
                showCustomDialog(getString(R.string.welcome), "${account.displayName}")
            }else{
                Toast.makeText(this, it.exception.toString(), Toast.LENGTH_SHORT).show()
            }
        }
    }

    private fun showCustomDialog(titleFill: String, descFill: String) {
        val builder = AlertDialog.Builder(this)
        val customView =
            LayoutInflater.from(this).inflate(R.layout.custom_layout_dialog_1_option, null)
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
        dialog.window?.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
        dialog.show()
    }

    private fun showCustomDialogVerify(titleFill: String, descFill: String) {
        val builder = AlertDialog.Builder(this)
        val customView =
            LayoutInflater.from(this).inflate(R.layout.custom_layout_dialog_1_option, null)
        builder.setView(customView)

        val title = customView.findViewById<TextView>(R.id.tv_title)
        val desc = customView.findViewById<TextView>(R.id.tv_desc)
        val btnNext = customView.findViewById<Button>(R.id.ok_btn_id)

        title.text = titleFill
        desc.text = descFill
        btnNext.setOnClickListener {
            val intent = Intent(this@LoginActivity, ConfirmationActivity::class.java)
            startActivity(intent)
            finish()
        }

        val dialog = builder.create()
        dialog.window?.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
        dialog.show()
    }

    private fun showCustomDialogFailed(titleFill: String, descFill: String) {
        val builder = AlertDialog.Builder(this)
        val customView =
            LayoutInflater.from(this).inflate(R.layout.custom_layout_dialog_1_option, null)
        builder.setView(customView)
        val dialog = builder.create()

        val title = customView.findViewById<TextView>(R.id.tv_title)
        val desc = customView.findViewById<TextView>(R.id.tv_desc)
        val btnNext = customView.findViewById<Button>(R.id.ok_btn_id)

        title.text = titleFill
        desc.text = descFill
        btnNext.setOnClickListener {
            dialog.dismiss()
        }


        dialog.window?.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
        dialog.show()
    }

    private fun setupLoginTextColor(){
        AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_FOLLOW_SYSTEM)

        when (resources.configuration.uiMode and android.content.res.Configuration.UI_MODE_NIGHT_MASK) {
            android.content.res.Configuration.UI_MODE_NIGHT_NO -> {
                findViewById<TextView>(R.id.titleTextView).setTextColor(getColor(R.color.black))
                findViewById<TextView>(R.id.emailTextView).setTextColor(getColor(R.color.NeutralDarkDark))
                findViewById<TextView>(R.id.passwordTextView).setTextColor(getColor(R.color.NeutralDarkDark))
                findViewById<TextView>(R.id.tv_askToRegister).setTextColor(getColor(R.color.NeutralDarkLight))
            }
            android.content.res.Configuration.UI_MODE_NIGHT_YES -> {
                findViewById<TextView>(R.id.titleTextView).setTextColor(getColor(R.color.NeutralLightDark))
                findViewById<TextView>(R.id.emailTextView).setTextColor(getColor(R.color.NeutralLightLight))
                findViewById<TextView>(R.id.passwordTextView).setTextColor(getColor(R.color.NeutralLightLight))
                findViewById<TextView>(R.id.tv_askToRegister).setTextColor(getColor(R.color.NeutralDarkLightest))
            }
        }
    }
}