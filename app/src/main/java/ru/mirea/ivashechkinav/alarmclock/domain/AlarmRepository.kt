package ru.mirea.ivashechkinav.alarmclock.domain

interface AlarmRepository {

    suspend fun saveAlarm(alarm: Alarm): Long

    suspend fun updateAlarm(alarm: Alarm)

    suspend fun deleteAlarm(alarm: Alarm)

    suspend fun deleteAlarmById(alarmId: Long)

    suspend fun getAlarmById(alarmId: Long): Alarm
}