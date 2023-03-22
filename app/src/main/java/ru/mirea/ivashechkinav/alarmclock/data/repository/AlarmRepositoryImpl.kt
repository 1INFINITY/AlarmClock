package ru.mirea.ivashechkinav.alarmclock.data.repository

import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import ru.mirea.ivashechkinav.alarmclock.data.room.AlarmDao
import ru.mirea.ivashechkinav.alarmclock.data.room.models.AlarmRoom
import ru.mirea.ivashechkinav.alarmclock.domain.Alarm
import ru.mirea.ivashechkinav.alarmclock.domain.AlarmRepository
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class AlarmRepositoryImpl @Inject constructor(
    private val alarmDao: AlarmDao
) : AlarmRepository {

    override suspend fun saveAlarm(alarm: Alarm) = withContext(Dispatchers.IO) {
        alarmDao.save(AlarmRoom(alarm))
    }

    override suspend fun deleteAlarm(alarm: Alarm) = withContext(Dispatchers.IO) {
        alarmDao.delete(AlarmRoom(alarm))
    }

    override suspend fun deleteAlarmById(alarmId: Long) = withContext(Dispatchers.IO) {
        alarmDao.deleteById(alarmId)
    }
}