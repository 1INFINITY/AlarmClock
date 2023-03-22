package ru.mirea.ivashechkinav.alarmclock

import android.app.AlarmManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import ru.mirea.ivashechkinav.alarmclock.domain.AlarmService

class AlarmServiceImpl(private val appContext: Context) : AlarmService {

    override fun stopAlarm(alarmRequestCode: Int) {
        val alarmManager = appContext.getSystemService(Context.ALARM_SERVICE) as AlarmManager
        val intent = Intent(appContext, AlarmActivity::class.java)
        val pendingFlags = PendingIntent.FLAG_UPDATE_CURRENT or PendingIntent.FLAG_IMMUTABLE
        val pendingIntent = PendingIntent.getActivity(appContext, alarmRequestCode, intent, pendingFlags)
        alarmManager.cancel(pendingIntent)
    }
    override fun setAlarm(millis: Long) {
        val alarmManager = appContext.getSystemService(Context.ALARM_SERVICE) as AlarmManager
        val alarmClockInfo = AlarmManager.AlarmClockInfo(millis, getAlarmInfoPendingIntent())

        alarmManager.setAlarmClock(alarmClockInfo, getAlarmActionPendingIntent())
    }

    private fun getAlarmInfoPendingIntent(): PendingIntent {
        val alarmInfoIntent = Intent(appContext, MainActivity::class.java)
        alarmInfoIntent.flags = Intent.FLAG_ACTIVITY_CLEAR_TOP or Intent.FLAG_ACTIVITY_NEW_TASK
        val pendingFlags = PendingIntent.FLAG_UPDATE_CURRENT or PendingIntent.FLAG_IMMUTABLE
        return PendingIntent.getActivity(appContext, 0, alarmInfoIntent, pendingFlags)
    }

    private fun getAlarmActionPendingIntent(): PendingIntent {
        val alarmActionIntent = Intent(appContext, AlarmActivity::class.java)
        alarmActionIntent.flags = Intent.FLAG_ACTIVITY_CLEAR_TOP or Intent.FLAG_ACTIVITY_NEW_TASK
        val pendingFlags = PendingIntent.FLAG_UPDATE_CURRENT or PendingIntent.FLAG_IMMUTABLE
        return PendingIntent.getActivity(appContext, 1, alarmActionIntent, pendingFlags)
    }

}