package ru.mirea.ivashechkinav.alarmclock

import android.media.Ringtone
import android.media.RingtoneManager
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.lifecycleScope
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch
import ru.mirea.ivashechkinav.alarmclock.data.repository.AlarmRepositoryImpl
import ru.mirea.ivashechkinav.alarmclock.databinding.ActivityAlarmBinding
import javax.inject.Inject

@AndroidEntryPoint
class AlarmActivity : AppCompatActivity() {

    @Inject
    lateinit var repositoryImpl: AlarmRepositoryImpl
    lateinit var binding: ActivityAlarmBinding
    lateinit var ringtone: Ringtone

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityAlarmBinding.inflate(layoutInflater)
        setContentView(binding.root)
        val requestCode: Long = intent.extras!!.getLong("requestCode")
        lifecycleScope.launchWhenStarted {
            repositoryImpl.deleteAlarmById(requestCode)
        }
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