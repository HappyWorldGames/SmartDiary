package ru.HappyWorldGames.SmartDiary

import android.content.Context
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import kotlinx.android.synthetic.main.activity_setting.*

class SettingActivity : AppCompatActivity() {

    val sharedPreference by lazy { getSharedPreferences("setting", Context.MODE_PRIVATE) }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_setting)

        notification.isChecked = sharedPreference.getBoolean("notification", false)
        notification.setOnCheckedChangeListener { _, isChecked ->
            sharedPreference.edit().putBoolean("notification", isChecked).apply()
            if(!NotificationService.running && isChecked) NotificationService.startService(this)
            else if(NotificationService.running && !isChecked) NotificationService.stopService(this)
        }
    }
}