package ru.mirea.ivashechkinav.alarmclock.domain

interface AlarmService {
    fun stopAlarm(alarmRequestCode: Long)

    fun setAlarm(alarm: Alarm)

    fun alarmSwitch(alarmId: Long)
}