package com.capstone.basaliproject.ui.login

import android.app.Activity
import android.content.ContentProviderClient
import android.content.Context
import android.content.Intent
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.widget.Button
import android.widget.ProgressBar
import android.widget.TextView
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import com.capstone.basaliproject.MainActivity
import com.capstone.basaliproject.R
import com.capstone.basaliproject.databinding.ActivityLoginBinding
import com.capstone.basaliproject.ui.confirm.ConfirmationActivity
import com.capstone.basaliproject.ui.home.HomeFragment
import com.capstone.basaliproject.ui.welcome.WelcomeActivity
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

        viewModel = ViewModelProvider(this).get(LoginViewModel::class.java)

        edEmail = binding.edLoginEmail
        edPassword = binding.edLoginPassword
        btnLogin = binding.loginButton
        progressBar = binding.progressBar

        btnLogin.setOnClickListener {
            val email = edEmail.text.toString()
            val password = edPassword.text.toString()
            if (email.isEmpty()) {
                edEmail.error = "Email tidak boleh kosong"
                edEmail.requestFocus()
            } else if (password.isEmpty()) {
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
                viewModel.isEmailVerified.observe(this, Observer { isEmailVerified ->
                    if (isEmailVerified == false) {
                        // Navigate to ConfirmationActivity
                        showCustomDialogVerify("Verify your email!", "Click to verify the email")
                    } else {
                        val email = edEmail.text.toString()
                        showCustomDialog("Welcome!", email)
                    }
                })
            } else {
                Toast.makeText(context, "Authentication failed.", Toast.LENGTH_SHORT).show()
                showCustomDialogFailed("Login Failed!", "Authentication failed or account didnt exist")
            }
        })



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
                showCustomDialog("Welcome!", "${account.displayName}")
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
        dialog.getWindow()?.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT));
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
        dialog.getWindow()?.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT));
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
            edEmail.text = ""
            edPassword.text = ""
            dialog.dismiss()
        }


        dialog.getWindow()?.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT));
        dialog.show()
    }
}