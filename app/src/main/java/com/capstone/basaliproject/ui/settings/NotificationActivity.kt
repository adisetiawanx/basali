package com.capstone.basaliproject.ui.settings

import android.Manifest
import android.content.Context
import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.ImageButton
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import com.capstone.basaliproject.R
import com.capstone.basaliproject.databinding.ActivityNotificationBinding
import com.capstone.basaliproject.notification.AlarmReceiver
import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.Locale

class NotificationActivity : AppCompatActivity(), View.OnClickListener, TimePickerFragment.DialogTimeListener {

    private var binding: ActivityNotificationBinding? = null
    private lateinit var alarmReceiver: AlarmReceiver

    private val sharedPrefsKey = "alarm_data"
    private val repeatTimeKey = "repeat_time"
    private val repeatMessageKey = "repeat_message"

    private val requestPermissionLauncher =
        registerForActivityResult(
            ActivityResultContracts.RequestPermission()
        ) { isGranted: Boolean ->
            if (isGranted) {
                Toast.makeText(this, "Notifications permission granted", Toast.LENGTH_SHORT).show()
            } else {
                Toast.makeText(this, "Notifications permission rejected", Toast.LENGTH_SHORT).show()
            }
        }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityNotificationBinding.inflate(layoutInflater)
        setContentView(binding?.root)

        if (Build.VERSION.SDK_INT >= 33) {
            requestPermissionLauncher.launch(Manifest.permission.POST_NOTIFICATIONS)
        }

        binding?.btnRepeatingTime?.setOnClickListener(this)
        binding?.btnSetRepeatingAlarm?.setOnClickListener(this)

        binding?.btnCancelRepeatingAlarm?.setOnClickListener(this)

        alarmReceiver = AlarmReceiver()

        loadAlarmData()

        val toolbarBackButton: ImageButton = findViewById(R.id.toolbar_back)
        toolbarBackButton.setOnClickListener {
            onBackPressed()
        }
    }

    override fun onClick(v: View?) {
        when (v?.id) {
            R.id.btn_repeating_time -> {
                val timePickerFragmentRepeat = TimePickerFragment()
                timePickerFragmentRepeat.show(supportFragmentManager, TIME_PICKER_REPEAT_TAG)
            }

            R.id.btn_set_repeating_alarm -> {
                val repeatTime = binding?.tvRepeatingTime?.text.toString()
                val repeatMessage = binding?.edtRepeatingMessage?.text.toString()
                alarmReceiver.setRepeatingAlarm(
                    this, AlarmReceiver.TYPE_REPEATING,
                    repeatTime, repeatMessage)

                saveAlarmData(repeatTime, repeatMessage)
            }

            R.id.btn_cancel_repeating_alarm -> {
                alarmReceiver.cancelAlarm(this)
                clearAlarmData()

                binding?.tvRepeatingTime?.text = ""
                binding?.edtRepeatingMessage?.setText("")
            }
        }
    }

    override fun onDialogTimeSet(tag: String?, hourOfDay: Int, minute: Int) {

        val calendar = Calendar.getInstance()
        calendar.set(Calendar.HOUR_OF_DAY, hourOfDay)
        calendar.set(Calendar.MINUTE, minute)

        val dateFormat = SimpleDateFormat("HH:mm", Locale.getDefault())

        when (tag) {
            TIME_PICKER_REPEAT_TAG -> binding?.tvRepeatingTime?.text = dateFormat.format(calendar.time)
            else -> {
            }
        }
    }

    private fun saveAlarmData(repeatTime: String, repeatMessage: String) {
        val sharedPreferences = getSharedPreferences(sharedPrefsKey, Context.MODE_PRIVATE)
        val editor = sharedPreferences.edit()
        editor.putString(repeatTimeKey, repeatTime)
        editor.putString(repeatMessageKey, repeatMessage)
        editor.apply()
    }

    private fun loadAlarmData() {
        val sharedPreferences = getSharedPreferences(sharedPrefsKey, Context.MODE_PRIVATE)
        val repeatTime = sharedPreferences.getString(repeatTimeKey, "")
        val repeatMessage = sharedPreferences.getString(repeatMessageKey, "")

        binding?.tvRepeatingTime?.text = repeatTime
        binding?.edtRepeatingMessage?.setText(repeatMessage)
    }

    private fun clearAlarmData() {
        val sharedPreferences = getSharedPreferences(sharedPrefsKey, Context.MODE_PRIVATE)
        val editor = sharedPreferences.edit()
        editor.clear()
        editor.apply()
    }

    override fun onDestroy() {
        super.onDestroy()
        binding = null
    }

    companion object {
        private const val TIME_PICKER_REPEAT_TAG = "TimePickerRepeat"
    }

}