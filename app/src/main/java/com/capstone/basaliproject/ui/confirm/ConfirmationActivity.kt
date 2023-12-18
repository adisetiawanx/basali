package com.capstone.basaliproject.ui.confirm

import android.content.ContentValues.TAG
import android.content.Intent
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import android.text.Editable
import android.text.InputFilter
import android.text.InputType
import android.text.TextWatcher
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.widget.Button
import android.widget.ProgressBar
import android.widget.TextView
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProvider
import com.capstone.basaliproject.R
import com.capstone.basaliproject.data.api.retrofit.ApiConfig
import com.capstone.basaliproject.databinding.ActivityConfirmationBinding
import com.capstone.basaliproject.ui.login.LoginActivity
import com.capstone.basaliproject.ui.welcome.WelcomeActivity
import com.google.android.material.textfield.TextInputEditText
import com.google.android.material.textfield.TextInputLayout
import com.google.firebase.Firebase
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.auth
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class ConfirmationActivity : AppCompatActivity() {
    private lateinit var binding: ActivityConfirmationBinding
    private lateinit var viewModel: ConfirmationViewModel
    private lateinit var email: String
    private lateinit var edConfirm1: TextInputEditText
    private lateinit var edConfirm2: TextInputEditText
    private lateinit var edConfirm3: TextInputEditText
    private lateinit var edConfirm4: TextInputEditText
    private lateinit var edConfirm5: TextInputEditText
    private lateinit var edConfirm6: TextInputEditText
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityConfirmationBinding.inflate(layoutInflater)
        setContentView(binding.root)
        viewModel = ViewModelProvider(this)[ConfirmationViewModel::class.java]
        setupAction()
        setupEditText()

        viewModel.isLoading.observe(this) { isLoading ->
            if (isLoading) {
                binding.progressBar.visibility = View.VISIBLE
            } else {
                binding.progressBar.visibility = View.GONE
            }
        }

        viewModel.isSuccess.observe(this) {isSuccess ->
            if (isSuccess) {
                viewModel.navigateToMain.observe(this) { navigate ->
                    if (navigate) {
                        showCustomDialog(
                            getString(R.string.verify_success),
                            getString(R.string.click_to_continue)
                        )
                        viewModel.doneNavigating()
                    } else {
                        Log.d("Navigation", "Navigation failed: navigate is false")
                    }
                }
            } else {
                showCustomDialogFailed(
                    getString(R.string.verify_failed),
                    getString(R.string.click_to_retry))
            }
        }

        val auth = FirebaseAuth.getInstance()
        val user = auth.currentUser

        if (user != null) {
            email = user.email.toString()
        }

        binding.confirmationMessage.text = getString(R.string.a_6_digit_code_was_sent_to, email)
    }

    private fun setupEditText() {
        val layout1 = findViewById<TextInputLayout>(R.id.confirmEditTextLayout_1)
        val layout2 = findViewById<TextInputLayout>(R.id.confirmEditTextLayout_2)
        val layout3 = findViewById<TextInputLayout>(R.id.confirmEditTextLayout_3)
        val layout4 = findViewById<TextInputLayout>(R.id.confirmEditTextLayout_4)
        val layout5 = findViewById<TextInputLayout>(R.id.confirmEditTextLayout_5)
        val layout6 = findViewById<TextInputLayout>(R.id.confirmEditTextLayout_6)

        edConfirm1 = layout1.findViewById(R.id.ed_confirmEditTextLayout_1)
        edConfirm2 = layout2.findViewById(R.id.ed_confirmEditTextLayout_2)
        edConfirm3 = layout3.findViewById(R.id.ed_confirmEditTextLayout_3)
        edConfirm4 = layout4.findViewById(R.id.ed_confirmEditTextLayout_4)
        edConfirm5 = layout5.findViewById(R.id.ed_confirmEditTextLayout_5)
        edConfirm6 = layout6.findViewById(R.id.ed_confirmEditTextLayout_6)

        setupEditTextListener(edConfirm1, edConfirm2)
        setupEditTextListener(edConfirm2, edConfirm3)
        setupEditTextListener(edConfirm3, edConfirm4)
        setupEditTextListener(edConfirm4, edConfirm5)
        setupEditTextListener(edConfirm5, edConfirm6)
    }

    private fun setupEditTextListener(currentEditText: TextInputEditText, nextEditText: TextInputEditText) {
        currentEditText.inputType = InputType.TYPE_CLASS_NUMBER
        currentEditText.filters = arrayOf(InputFilter.LengthFilter(1))

        currentEditText.addTextChangedListener(object : TextWatcher {
            override fun afterTextChanged(s: Editable?) {
                if (s?.length == 1) {
                    nextEditText.requestFocus()
                }
            }

            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {}
        })
    }

    private fun setupAction() {
        binding.resendCode.setOnClickListener {
            binding.progressBar.visibility = View.VISIBLE
            //resend code logic here
            // Get an instance of your ApiService
            val user = Firebase.auth.currentUser
            user?.getIdToken(true)?.addOnCompleteListener { task ->
                if (task.isSuccessful) {
                    val idToken = task.result?.token
                    val apiService = idToken?.let { it1 -> ApiConfig.getApiService(it1) }

                    // Call the resendCode function
                    CoroutineScope(Dispatchers.IO).launch {
                        val response = apiService?.resendCode()

                        withContext(Dispatchers.Main) {
                            if (response != null) {
                                if (response.userId != null) {
                                    binding.progressBar.visibility = View.GONE
                                    // Handle successful response
                                    val auth = FirebaseAuth.getInstance()
                                    val user = auth.currentUser

                                    if (user != null) {
                                        email = user.email.toString()
                                    }
                                    showCustomDialogResend(getString(R.string.resend_success),
                                        getString(
                                            R.string.the_code_has_been_resent_to, email
                                        ))
                                    val confirmationResponse = response.msg
                                    Log.d(
                                        "ConfirmationActivity",
                                        "Code successfully resent: $confirmationResponse"
                                    )
                                } else {
                                    binding.progressBar.visibility = View.GONE
                                    // Handle error
                                    Log.e("ConfirmationActivity", "Failed to resend code")
                                }
                            }
                        }
                    }
                } else {
                    Log.w(TAG, "Failed to get ID Token", task.exception)
                }
            }
        }
        binding.continueButton.setOnClickListener {
            val progressBar = findViewById<ProgressBar>(R.id.progressBar)
            progressBar.visibility = View.VISIBLE

            val verifCode = "${edConfirm1.text.toString()}${edConfirm2.text.toString()}${edConfirm3.text.toString()}${edConfirm4.text.toString()}${edConfirm5.text.toString()}${edConfirm6.text.toString()}"
            Log.d("ConfirmationActivity", verifCode)

            viewModel.verifyEmail(verifCode)
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
            val intent = Intent(this@ConfirmationActivity, LoginActivity::class.java)
            startActivity(intent)
            finish()
        }

        val dialog = builder.create()
        dialog.window?.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
        dialog.show()
    }

    private fun showCustomDialogResend(titleFill: String, descFill: String) {
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


    override fun onBackPressed() {
        startActivity(Intent(this@ConfirmationActivity, WelcomeActivity::class.java))
        finish()
        super.onBackPressed()
    }

}