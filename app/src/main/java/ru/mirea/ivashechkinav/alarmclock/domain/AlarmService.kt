package ru.mirea.ivashechkinav.alarmclock.domain

interface AlarmService {
    fun stopAlarm(alarmRequestCode: Int)

    fun setAlarm(millis: Long)
}