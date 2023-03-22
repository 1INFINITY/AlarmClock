package ru.mirea.ivashechkinav.alarmclock.domain

interface AlarmRepository {

    suspend fun saveAlarm(alarm: Alarm)

    suspend fun deleteAlarm(alarm: Alarm)
}