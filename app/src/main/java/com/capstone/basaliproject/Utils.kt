package com.capstone.basaliproject

import android.content.Context
import androidx.appcompat.app.AppCompatDelegate
import java.io.File
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

private const val FILENAME_FORMAT = "yyyyMMdd_HHmmss"
private val timeStamp: String = SimpleDateFormat(FILENAME_FORMAT, Locale.US).format(Date())

fun createCustomTempFile(context: Context): File {
    val filesDir = context.externalCacheDir
    return File.createTempFile(timeStamp, ".jpg", filesDir)
}

enum class NightMode(val value: Int) {

    AUTO(AppCompatDelegate.MODE_NIGHT_FOLLOW_SYSTEM),

    ON(AppCompatDelegate.MODE_NIGHT_YES),

    OFF(AppCompatDelegate.MODE_NIGHT_NO)
}


