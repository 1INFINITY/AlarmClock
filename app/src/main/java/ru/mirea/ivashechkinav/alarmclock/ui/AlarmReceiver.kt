package ru.mirea.ivashechkinav.alarmclock.ui

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import ru.mirea.ivashechkinav.alarmclock.data.repository.AlarmRepositoryImpl
import javax.inject.Inject

@AndroidEntryPoint
class AlarmReceiver : BroadcastReceiver() {
    @Inject
    lateinit var alarmServiceImpl: AlarmServiceImpl

    @Inject
    lateinit var alarmRepositoryImpl: AlarmRepositoryImpl

    @Inject
    lateinit var notificationService: NotificationService

    override fun onReceive(context: Context, intent: Intent) {
        GlobalScope.launch {
            val alarmId = intent.getLongExtra("alarmId", -1)
            if (alarmId == -1L) return@launch

            alarmServiceImpl.alarmSwitch(alarmId)

            val alarm = alarmRepositoryImpl.getAlarmById(alarmId)
            notificationService.showNotification(alarm)
        }
    }
}