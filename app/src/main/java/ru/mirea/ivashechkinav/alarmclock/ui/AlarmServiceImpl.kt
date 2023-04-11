package ru.mirea.ivashechkinav.alarmclock.ui

import android.app.AlarmManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.util.Log
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import ru.mirea.ivashechkinav.alarmclock.data.repository.AlarmRepositoryImpl
import ru.mirea.ivashechkinav.alarmclock.domain.Alarm
import ru.mirea.ivashechkinav.alarmclock.domain.AlarmService
import ru.mirea.ivashechkinav.alarmclock.domain.DaysOfWeek
import ru.mirea.ivashechkinav.alarmclock.domain.DaysOfWeek.Companion.toInt
import ru.mirea.ivashechkinav.alarmclock.ui.activities.AlarmActivity
import ru.mirea.ivashechkinav.alarmclock.ui.activities.MainActivity
import ru.mirea.ivashechkinav.alarmclock.ui.models.AlarmUi
import java.text.SimpleDateFormat
import java.util.*
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class AlarmServiceImpl @Inject constructor(
    private val repository: AlarmRepositoryImpl,
    @ApplicationContext private val appContext: Context
) : AlarmService {

    override fun stopAlarm(alarmRequestCode: Long) {
        val alarmManager = appContext.getSystemService(Context.ALARM_SERVICE) as AlarmManager
        val intent = Intent(appContext, AlarmActivity::class.java)
        val pendingFlags = PendingIntent.FLAG_UPDATE_CURRENT or PendingIntent.FLAG_IMMUTABLE
        val pendingIntent =
            PendingIntent.getActivity(appContext, alarmRequestCode.toInt(), intent, pendingFlags)
        alarmManager.cancel(pendingIntent)
    }

    override fun setAlarm(alarm: Alarm) {
        GlobalScope.launch {
            val calendar = Calendar.getInstance()
            var editedAlarm = findNearestAlarm(calendar = calendar, alarm = alarm)
            log(editedAlarm)
            val alarmRequestCode = repository.saveAlarm(editedAlarm)
            val alarmManager = appContext.getSystemService(Context.ALARM_SERVICE) as AlarmManager
            val alarmClockInfo =
                AlarmManager.AlarmClockInfo(editedAlarm.invokeTimestamp, getAlarmInfoPendingIntent())

            alarmManager.setAlarmClock(
                alarmClockInfo,
                getAlarmActionPendingIntent(alarmRequestCode)
            )
        }
    }

    override fun alarmSwitch(alarmId: Long) {
        GlobalScope.launch {
            val alarm = repository.getAlarmById(alarmId)
            val calendar = Calendar.getInstance()
            val editedAlarm = findNearestAlarm(calendar = calendar, alarm = alarm)
            repository.updateAlarm(editedAlarm)
            val alarmManager = appContext.getSystemService(Context.ALARM_SERVICE) as AlarmManager
            val alarmClockInfo =
                AlarmManager.AlarmClockInfo(calendar.timeInMillis, getAlarmInfoPendingIntent())
            alarmManager.setAlarmClock(alarmClockInfo, getAlarmActionPendingIntent(alarmId))
        }
    }

    private fun log(alarm: Alarm) {
        val textDate = SimpleDateFormat("dd.MM hh:mm").format(Date(alarm.invokeTimestamp))
        Log.d("AlarmNextInvoke", textDate)

    }
    private fun findNearestAlarm(calendar: Calendar, alarm: Alarm): Alarm {
        var daysSet = alarm.daysOfWeek
        if (daysSet.isEmpty()) daysSet = DaysOfWeek.fromByte(0b01111111)

        val currentDay = calendar.get(Calendar.DAY_OF_WEEK)
        val currentDayName = DaysOfWeek.fromInt(currentDay)
        if (daysSet.contains(currentDayName) && alarm.invokeTimestamp > calendar.timeInMillis) {
            return alarm
        }
        val nextDay = daysSet.find {
            it.value > currentDayName.value
        }
            ?.let { it.toInt() }

        calendar.timeInMillis = alarm.invokeTimestamp
        if (nextDay == null) {
            val firstDayInSet = daysSet.first().toInt()
            calendar.set(Calendar.DAY_OF_WEEK, firstDayInSet)
            calendar.timeInMillis += 7 * 24 * 3600 * 1000
            return (alarm as AlarmUi).copy(invokeTimestamp = calendar.timeInMillis)
        }
        calendar.set(Calendar.DAY_OF_WEEK, nextDay)
        return (alarm as AlarmUi).copy(invokeTimestamp = calendar.timeInMillis)
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
        return PendingIntent.getActivity(
            appContext,
            alarmRequestCode.toInt(),
            alarmActionIntent,
            pendingFlags
        )
    }

}