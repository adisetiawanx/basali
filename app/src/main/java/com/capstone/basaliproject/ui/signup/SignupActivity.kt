package com.capstone.basaliproject.ui.signup

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.TextView
import androidx.appcompat.app.AppCompatDelegate
import com.capstone.basaliproject.R

class SignupActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_signup)

        setupTextColor()
    }

    private fun setupTextColor(){
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