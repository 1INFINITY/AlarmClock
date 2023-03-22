package ru.mirea.ivashechkinav.alarmclock.domain

interface AlarmRepository {

    suspend fun saveAlarm(alarm: Alarm): Long

    suspend fun deleteAlarm(alarm: Alarm)

    suspend fun deleteAlarmById(alarmId: Long)
}