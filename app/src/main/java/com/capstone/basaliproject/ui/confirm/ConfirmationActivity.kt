package com.capstone.basaliproject.ui.confirm

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.text.Editable
import android.text.InputFilter
import android.text.InputType
import android.text.TextWatcher
import android.util.Log
import com.capstone.basaliproject.R
import com.capstone.basaliproject.databinding.ActivityConfirmationBinding
import com.google.android.material.textfield.TextInputEditText
import com.google.android.material.textfield.TextInputLayout
import kotlin.math.log

class ConfirmationActivity : AppCompatActivity() {
    private lateinit var binding: ActivityConfirmationBinding
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

        setupEditText()
        setupAction()
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
        binding.continueButton.setOnClickListener {
            val verifCode = "$edConfirm1$edConfirm2$edConfirm3$edConfirm4$edConfirm5$edConfirm6"
            Log.d("ConfirmationActivity", verifCode)
        }
    }

}