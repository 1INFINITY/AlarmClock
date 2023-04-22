package ru.mirea.ivashechkinav.alarmclock.ui.activities

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.lifecycleScope
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.MutableStateFlow
import ru.mirea.ivashechkinav.alarmclock.data.repository.AlarmRepositoryImpl
import ru.mirea.ivashechkinav.alarmclock.databinding.ActivityAlarmBinding
import ru.mirea.ivashechkinav.alarmclock.ui.AlarmServiceImpl
import ru.mirea.ivashechkinav.alarmclock.ui.NotificationService
import javax.inject.Inject


@AndroidEntryPoint
class AlarmActivity : AppCompatActivity() {

    @Inject
    lateinit var repositoryImpl: AlarmRepositoryImpl

    @Inject
    lateinit var alarmServiceImpl: AlarmServiceImpl

    @Inject
    lateinit var notificationService: NotificationService

    lateinit var binding: ActivityAlarmBinding
    var alarmId: Long? = null

    private var alarmDelayFlow = MutableStateFlow(5)

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityAlarmBinding.inflate(layoutInflater)
        setContentView(binding.root)
        initButtons()
        alarmId = intent.extras!!.getLong("alarmId")
    }

    override fun onDestroy() {
        super.onDestroy()
        alarmId?.let {
            notificationService.closeNotification(it)
        }
    }

    private fun initButtons() {
        binding.btnCancelAlarm.setOnClickListener { finish() }

        binding.btnIncreaseDelay.setOnClickListener { alarmDelayFlow.value += 5 }
        binding.btnReduceDelay.setOnClickListener { alarmDelayFlow.value -= 5 }
        binding.btnDelayAlarm.setOnClickListener { delayCurrentAlarm() }

        lifecycleScope.launchWhenStarted {
            alarmDelayFlow.collect {
                binding.btnDelayAlarm.text = "Отложить на $it минут"
                if (it <= MIN_DELAY) {
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

    companion object {
        const val MIN_DELAY = 5
        const val MAX_DELAY = 60
    }
}