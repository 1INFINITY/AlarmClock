package ru.mirea.ivashechkinav.alarmclock.ui.activities

import android.media.Ringtone
import android.media.RingtoneManager
import android.os.Bundle
import android.util.Log
import android.view.WindowManager
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.lifecycleScope
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.collect
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

    private var alarmDelayFlow = MutableStateFlow(5)

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityAlarmBinding.inflate(layoutInflater)
        setContentView(binding.root)
        initRingtone()
        initButtons()
    }

    override fun onDestroy() {
        super.onDestroy()
        stopRingtone()
    }

    private fun initButtons(){
        binding.btnCancelAlarm.setOnClickListener { finish() }

        binding.btnIncreaseDelay.setOnClickListener { alarmDelayFlow.value += 5 }
        binding.btnReduceDelay.setOnClickListener { alarmDelayFlow.value -= 5 }
        binding.btnDelayAlarm.setOnClickListener { delayCurrentAlarm() }

        lifecycleScope.launchWhenStarted {
            alarmDelayFlow.collect {
                binding.btnDelayAlarm.text = "Отложить на $it минут"
                if(it <= MIN_DELAY) {
                    binding.btnReduceDelay.isEnabled = false
                    binding.btnIncreaseDelay.isEnabled = true
                } else if (it >= MAX_DELAY) {
                    binding.btnReduceDelay.isEnabled = true
                    binding.btnIncreaseDelay.isEnabled = false
                }
            }
        }
    }
    private fun delayCurrentAlarm() {
        // TODO: Write an implementation of func
        finish()
    }
    private fun initRingtone() {
        val currentAlarmId = intent.extras!!.getLong("requestCode")
        lifecycleScope.launchWhenStarted {
            val alarm: Alarm = repositoryImpl.getAlarmById(alarmId = currentAlarmId)
            ringtone = RingtoneManager.getRingtone(this@AlarmActivity, alarm.alarmSoundUri)
            playRingtone()
        }
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
    companion object {
        const val MIN_DELAY = 5
        const val MAX_DELAY = 60
    }
}