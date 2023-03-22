package ru.mirea.ivashechkinav.alarmclock

import android.app.AlarmManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import ru.mirea.ivashechkinav.alarmclock.data.repository.AlarmRepositoryImpl
import ru.mirea.ivashechkinav.alarmclock.domain.Alarm
import ru.mirea.ivashechkinav.alarmclock.domain.AlarmRepository
import ru.mirea.ivashechkinav.alarmclock.domain.AlarmService
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class AlarmServiceImpl @Inject constructor(
    private val repository: AlarmRepositoryImpl,
    @ApplicationContext private val appContext: Context
) : AlarmService {

    override fun stopAlarm(alarmRequestCode: Int) {
        val alarmManager = appContext.getSystemService(Context.ALARM_SERVICE) as AlarmManager
        val intent = Intent(appContext, AlarmActivity::class.java)
        val pendingFlags = PendingIntent.FLAG_UPDATE_CURRENT or PendingIntent.FLAG_IMMUTABLE
        val pendingIntent =
            PendingIntent.getActivity(appContext, alarmRequestCode, intent, pendingFlags)
        alarmManager.cancel(pendingIntent)
    }

    override fun setAlarm(alarm: Alarm) {
        GlobalScope.launch {
            val alarmRequestCode = repository.saveAlarm(alarm)
            val alarmManager = appContext.getSystemService(Context.ALARM_SERVICE) as AlarmManager
            val alarmClockInfo = AlarmManager.AlarmClockInfo(alarm.invokeTimestamp, getAlarmInfoPendingIntent())

            alarmManager.setAlarmClock(alarmClockInfo, getAlarmActionPendingIntent(alarmRequestCode))
        }
    }

    private fun getAlarmInfoPendingIntent(): PendingIntent {
        val alarmInfoIntent = Intent(appContext, MainActivity::class.java)
        alarmInfoIntent.flags = Intent.FLAG_ACTIVITY_CLEAR_TOP or Intent.FLAG_ACTIVITY_NEW_TASK
        val pendingFlags = PendingIntent.FLAG_UPDATE_CURRENT or PendingIntent.FLAG_IMMUTABLE
        return PendingIntent.getActivity(appContext, 0, alarmInfoIntent, pendingFlags)
    }

    private fun getAlarmActionPendingIntent(alarmRequestCode: Long): PendingIntent {
        val alarmActionIntent = Intent(appContext, AlarmActivity::class.java)
        alarmActionIntent.putExtra("requestCode", alarmRequestCode)
        alarmActionIntent.flags = Intent.FLAG_ACTIVITY_CLEAR_TOP or Intent.FLAG_ACTIVITY_NEW_TASK
        val pendingFlags = PendingIntent.FLAG_UPDATE_CURRENT or PendingIntent.FLAG_IMMUTABLE
        return PendingIntent.getActivity(appContext, alarmRequestCode.toInt(), alarmActionIntent, pendingFlags)
    }

}