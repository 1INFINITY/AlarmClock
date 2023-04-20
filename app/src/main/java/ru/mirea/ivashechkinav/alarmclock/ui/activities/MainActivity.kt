package ru.mirea.ivashechkinav.alarmclock.ui.activities

import android.app.NotificationChannel
import android.app.NotificationManager
import android.os.Build
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import dagger.hilt.android.AndroidEntryPoint
import ru.mirea.ivashechkinav.alarmclock.databinding.ActivityMainBinding

@AndroidEntryPoint
class MainActivity : AppCompatActivity() {

    lateinit var binding: ActivityMainBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
        createNotificationChannel()
    }

    private fun createNotificationChannel() {

        val name: CharSequence = "alarmClockReminderChannel"
        val description = "Channel For Alarm Manager"
        val importance = NotificationManager.IMPORTANCE_HIGH
        val channel = NotificationChannel("ivashechkinav.alarmclock", name, importance)
        channel.description = description
        val notificationManager = getSystemService(
            NotificationManager::class.java
        )

        notificationManager.createNotificationChannel(channel)
    }
}