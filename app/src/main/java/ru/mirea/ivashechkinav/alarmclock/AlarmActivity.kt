package ru.mirea.ivashechkinav.alarmclock

import android.media.Ringtone
import android.media.RingtoneManager
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import ru.mirea.ivashechkinav.alarmclock.databinding.ActivityAlarmBinding

class AlarmActivity : AppCompatActivity() {

    lateinit var binding: ActivityAlarmBinding
    lateinit var ringtone: Ringtone

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityAlarmBinding.inflate(layoutInflater)
        setContentView(binding.root)
        playRingtone()
    }

    override fun onDestroy() {
        super.onDestroy()
        stopRingtone()
    }

    private fun playRingtone() {
        val notification = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_ALARM)
        ringtone = RingtoneManager.getRingtone(this, notification)
        ringtone.play()
    }

    private fun stopRingtone() {
        if (!this::ringtone.isInitialized) throw java.lang.IllegalStateException("Ringtone must be initialized first")

        if (ringtone.isPlaying)
            ringtone.stop()
    }
}