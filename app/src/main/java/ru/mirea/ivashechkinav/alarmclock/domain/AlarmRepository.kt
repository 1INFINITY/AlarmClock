package ru.mirea.ivashechkinav.alarmclock.domain

import androidx.paging.PagingData
import kotlinx.coroutines.flow.Flow

interface AlarmRepository {

    fun getAlarms(): Flow<PagingData<Alarm>>

    suspend fun saveAlarm(alarm: Alarm): Long

    suspend fun updateAlarm(alarm: Alarm)

    suspend fun deleteAlarm(alarm: Alarm)

    suspend fun deleteAlarmById(alarmId: Long)

    suspend fun getAlarmById(alarmId: Long): Alarm

    suspend fun getNextMinTimestamp(currentTimestamp: Long): Long?
}