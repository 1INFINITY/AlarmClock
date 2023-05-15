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
import kotlin.math.min
import kotlin.math.abs
import java.text.DateFormat
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
            val editedAlarm = findNearestAlarm(calendar = calendar, alarm = alarm)
            val alarmId = repository.saveAlarm(editedAlarm)
            alarmStart(alarmId, editedAlarm.invokeTimestamp)
        }
    }

    override fun alarmSwitch(alarmId: Long) {
        GlobalScope.launch {
            val alarm = repository.getAlarmById(alarmId)
            val calendar = Calendar.getInstance()
            val editedAlarm = findNearestAlarm(calendar = calendar, alarm = alarm)
            repository.updateAlarm(editedAlarm)
            alarmStart(alarmId, editedAlarm.invokeTimestamp)
        }
    }

    private fun alarmStart(alarmId: Long, timeInMillis: Long) {
        val receiverIntent = Intent(appContext, AlarmReceiver::class.java)
        receiverIntent.putExtra("alarmId", alarmId)
        val pendingFlags = PendingIntent.FLAG_UPDATE_CURRENT or PendingIntent.FLAG_IMMUTABLE
        val pendingIntent =
            PendingIntent.getBroadcast(appContext, alarmId.toInt(), receiverIntent, pendingFlags)
        val alarmManager = appContext.getSystemService(Context.ALARM_SERVICE) as AlarmManager
        val alarmClockInfo = AlarmManager.AlarmClockInfo(timeInMillis, pendingIntent)
        alarmManager.setAlarmClock(alarmClockInfo, pendingIntent)
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
            return AlarmUi(alarm).copy(invokeTimestamp = calendar.timeInMillis)
        }
        calendar.set(Calendar.DAY_OF_WEEK, nextDay)
        return AlarmUi(alarm).copy(invokeTimestamp = calendar.timeInMillis)
    }

    private fun findNearestAlarm2(alarm: Alarm): Alarm {
        val daysSet = alarm.daysOfWeek
        var nextTimestamp = alarm.invokeTimestamp
        val alarmCalendar = Calendar.getInstance()
        val currentDay = alarmCalendar.get(Calendar.DAY_OF_WEEK)
        alarmCalendar.timeInMillis = alarm.invokeTimestamp
        val currentAlarmDay = alarmCalendar.get(Calendar.DAY_OF_WEEK)

        if (daysSet.isEmpty()) {
            val calendar = Calendar.getInstance()
            calendar.set(Calendar.HOUR, alarmCalendar.get(Calendar.HOUR))
            calendar.set(Calendar.MINUTE, alarmCalendar.get(Calendar.MINUTE))
            calendar.set(Calendar.SECOND, alarmCalendar.get(Calendar.SECOND))
            nextTimestamp = calendar.timeInMillis + 24 * 3600 * 1000
            return AlarmUi(alarm).copy(invokeTimestamp = nextTimestamp)
        }

        val currentDayName = DaysOfWeek.fromInt(currentDay)
        val nextDay = daysSet.find {
            it.value > currentDayName.value
        }
            ?: daysSet.first()

        val daysDifference = nextDay.toInt() - currentAlarmDay
        nextTimestamp +=
            if (daysDifference >= 0)
                daysDifference * 24 * 3600 * 1000
            else
                if (currentDay < nextDay.toInt())
                    daysDifference * 24 * 3600 * 1000
                else
                    (7 + daysDifference) * 24 * 3600 * 1000
        return AlarmUi(alarm).copy(invokeTimestamp = nextTimestamp)
    }

    private fun findNearestAlarm3(alarm: Alarm): Alarm {
        val DAY_IN_MILLIS = 24 * 3600 * 1000L
        var daysSet = alarm.daysOfWeek

        val currentCalendar = Calendar.getInstance()
        var currentMillis = currentCalendar.timeInMillis
        val alarmMillis = alarm.invokeTimestamp
        // Command currentCalendar.timeInMillis - currentCalendar.timeInMillis % DAY_IN_MILLIS
        // show wrong date at 2 am, because of this offset + 3 * 3600 * 1000
        val currentMillisWithoutDays = (currentCalendar.timeInMillis + 3 * 3600 * 1000) % DAY_IN_MILLIS
        val alarmMillisWithoutDays = (alarmMillis % DAY_IN_MILLIS+ 3 * 3600 * 1000)
        val mayBeSameDay = alarmMillisWithoutDays > currentMillisWithoutDays
        if (daysSet.isEmpty()) {
            currentMillis += alarmMillisWithoutDays - currentMillisWithoutDays
            if (mayBeSameDay) return AlarmUi(alarm).copy(invokeTimestamp = currentMillis)
            currentMillis += DAY_IN_MILLIS
            return AlarmUi(alarm).copy(invokeTimestamp = currentMillis)
        }

        var currentDay = currentCalendar.get(Calendar.DAY_OF_WEEK)
        val currentDayName = DaysOfWeek.fromInt(currentDay)
        var nextDay = daysSet.find {
            it.value > currentDayName.value || ((it.value == currentDayName.value) && mayBeSameDay)
        }?.toInt()
            ?: daysSet.first().toInt()
        nextDay = (nextDay + 5) % 7 + 1
        currentDay = (currentDay + 5) % 7 + 1
        val daysDifference =
            if (nextDay == currentDay && mayBeSameDay)
                0
            else
                if (nextDay - currentDay <= 0)
                    nextDay - currentDay + 7
                else
                    nextDay - currentDay
        val offsetInMillis = daysDifference * DAY_IN_MILLIS
        currentMillis +=  alarmMillisWithoutDays - currentMillisWithoutDays
        val nextTimestamp = currentMillis + offsetInMillis
        return AlarmUi(alarm).copy(invokeTimestamp = nextTimestamp)
    }


    override fun selectAlarmDays(alarmId: Long, selectedDays: EnumSet<DaysOfWeek>) {
        GlobalScope.launch {
            val alarm = repository.getAlarmById(alarmId).copy(daysOfWeek = selectedDays)
            val nearestAlarm = findNearestAlarm3(alarm = alarm)
            repository.updateAlarm(nearestAlarm)
        }
    }
}