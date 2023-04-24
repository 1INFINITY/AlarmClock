package ru.mirea.ivashechkinav.alarmclock.ui.activities

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.lifecycleScope
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.flow
import ru.mirea.ivashechkinav.alarmclock.data.repository.AlarmRepositoryImpl
import ru.mirea.ivashechkinav.alarmclock.databinding.ActivityAlarmBinding
import ru.mirea.ivashechkinav.alarmclock.domain.usecase.getParsedTimeFlowUseCase.GetParsedTimeFlowArgs
import ru.mirea.ivashechkinav.alarmclock.domain.usecase.getParsedTimeFlowUseCase.GetParsedTimeFlowUseCase
import ru.mirea.ivashechkinav.alarmclock.ui.AlarmServiceImpl
import ru.mirea.ivashechkinav.alarmclock.ui.NotificationService
import ru.mirea.ivashechkinav.alarmclock.ui.fragments.AlarmsListFragment
import javax.inject.Inject


@AndroidEntryPoint
class AlarmActivity : AppCompatActivity() {

    @Inject
    lateinit var repositoryImpl: AlarmRepositoryImpl

    @Inject
    lateinit var alarmServiceImpl: AlarmServiceImpl

    @Inject
    lateinit var notificationService: NotificationService

    @Inject
    lateinit var getParsedTimeFlowUseCase: GetParsedTimeFlowUseCase

    lateinit var binding: ActivityAlarmBinding
    var alarmId: Long? = null

    private var alarmDelayFlow = MutableStateFlow(5)

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityAlarmBinding.inflate(layoutInflater)
        setContentView(binding.root)
        initButtons()
        initCurrentTime()
        alarmId = intent.extras!!.getLong("alarmId")
    }

    override fun onDestroy() {
        super.onDestroy()
        alarmId?.let {
            notificationService.closeNotification(it)
        }
    }

    private fun initCurrentTime() {
        val currentTimeStampFlow = flow {
            repeat(Int.MAX_VALUE) {
                emit(System.currentTimeMillis())
                delay(UPDATE_TIME_INTERVAL)
            }
        }
        val args = GetParsedTimeFlowArgs(
            currentTimeStampFlow
        )
        lifecycleScope.launchWhenStarted {
            getParsedTimeFlowUseCase.execute(args).collect {
                binding.tvTime.text = it.currentTime
                binding.tvDate.text = it.currentDate
            }
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
        const val UPDATE_TIME_INTERVAL: Long = 60 * 1000
        const val MIN_DELAY = 5
        const val MAX_DELAY = 60
    }
}