package ru.mirea.ivashechkinav.alarmclock.domain

import java.util.*

interface AlarmService {
    fun stopAlarm(alarmRequestCode: Long)

    fun setAlarm(alarm: Alarm)

    fun alarmSwitch(alarmId: Long)

    fun selectAlarmDays(alarmId: Long, selectedDays: EnumSet<DaysOfWeek>)
}