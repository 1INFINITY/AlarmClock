package ru.mirea.ivashechkinav.alarmclock.ui.activities

import android.media.Ringtone
import android.media.RingtoneManager
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.lifecycleScope
import dagger.hilt.android.AndroidEntryPoint
import ru.mirea.ivashechkinav.alarmclock.data.repository.AlarmRepositoryImpl
import ru.mirea.ivashechkinav.alarmclock.databinding.ActivityAlarmBinding
import ru.mirea.ivashechkinav.alarmclock.domain.Alarm
import ru.mirea.ivashechkinav.alarmclock.ui.AlarmServiceImpl
import javax.inject.Inject

@AndroidEntryPoint
class AlarmActivity : AppCompatActivity() {

    @Inject
    lateinit var repositoryImpl: AlarmRepositoryImpl
    @Inject
    lateinit var alarmServiceImpl: AlarmServiceImpl

    lateinit var binding: ActivityAlarmBinding
    lateinit var ringtone: Ringtone

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityAlarmBinding.inflate(layoutInflater)
        setContentView(binding.root)
        val requestCode: Long = intent.extras!!.getLong("requestCode")
        lifecycleScope.launchWhenStarted {
            val alarm: Alarm = repositoryImpl.getAlarmById(alarmId = requestCode)
            ringtone = RingtoneManager.getRingtone(this@AlarmActivity, alarm.alarmSoundUri)
            alarmServiceImpl.alarmSwitch(requestCode)
            playRingtone()
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        stopRingtone()
    }

    private fun playRingtone() {
        if (!this::ringtone.isInitialized) throw IllegalStateException("Ringtone must be initialized first")
        ringtone.play()
    }

    private fun stopRingtone() {
        if (!this::ringtone.isInitialized) throw IllegalStateException("Ringtone must be initialized first")

        if (ringtone.isPlaying)
            ringtone.stop()
    }
}