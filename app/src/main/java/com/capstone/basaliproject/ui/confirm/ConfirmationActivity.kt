package com.capstone.basaliproject.ui.confirm

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.text.Editable
import android.text.InputFilter
import android.text.InputType
import android.text.TextWatcher
import com.capstone.basaliproject.R
import com.google.android.material.textfield.TextInputEditText
import com.google.android.material.textfield.TextInputLayout

class ConfirmationActivity : AppCompatActivity() {
    private lateinit var edConfirm1: TextInputEditText
    private lateinit var edConfirm2: TextInputEditText
    private lateinit var edConfirm3: TextInputEditText
    private lateinit var edConfirm4: TextInputEditText
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_confirmation)

        setupEditText()
    }

    private fun setupEditText() {
        val layout1 = findViewById<TextInputLayout>(R.id.confirmEditTextLayout_1)
        val layout2 = findViewById<TextInputLayout>(R.id.confirmEditTextLayout_2)
        val layout3 = findViewById<TextInputLayout>(R.id.confirmEditTextLayout_3)
        val layout4 = findViewById<TextInputLayout>(R.id.confirmEditTextLayout_4)

        edConfirm1 = layout1.findViewById(R.id.ed_confirmEditTextLayout_1)
        edConfirm2 = layout2.findViewById(R.id.ed_confirmEditTextLayout_2)
        edConfirm3 = layout3.findViewById(R.id.ed_confirmEditTextLayout_3)
        edConfirm4 = layout4.findViewById(R.id.ed_confirmEditTextLayout_4)

        setupEditTextListener(edConfirm1, edConfirm2)
        setupEditTextListener(edConfirm2, edConfirm3)
        setupEditTextListener(edConfirm3, edConfirm4)
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

}