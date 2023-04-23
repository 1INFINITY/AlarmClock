package ru.mirea.ivashechkinav.alarmclock.data.repository

import android.net.Uri
import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.paging.PagingData
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
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

    override fun getAlarms(): Flow<PagingData<Alarm>> {
        return Pager(
            config = PagingConfig(
                pageSize = PAGE_SIZE,
                initialLoadSize = PAGE_SIZE,
                enablePlaceholders = false
            ),
            pagingSourceFactory = { alarmDao.getPagingSource() }
        ).flow.map { it as PagingData<Alarm> }
    }

    override suspend fun saveAlarm(alarm: Alarm) = withContext(Dispatchers.IO) {
        alarmDao.save(AlarmRoom(alarm))
    }

    override suspend fun updateAlarm(alarm: Alarm) = withContext(Dispatchers.IO) {
        alarmDao.update(AlarmRoom(alarm))
    }

    override suspend fun updateAlarmUri(id: Long, uri: Uri) = withContext(Dispatchers.IO) {
        return@withContext alarmDao.updateUri(id, uri)
    }

    override suspend fun deleteAlarm(alarm: Alarm) = withContext(Dispatchers.IO) {
        alarmDao.delete(AlarmRoom(alarm))
    }

    override suspend fun deleteAlarmById(alarmId: Long) = withContext(Dispatchers.IO) {
        alarmDao.deleteById(alarmId)
    }

    override suspend fun getAlarmById(alarmId: Long) = withContext(Dispatchers.IO) {
        return@withContext alarmDao.findById(alarmId)
    }
    override suspend fun getTimestampsFlow() = withContext(Dispatchers.IO) {
        return@withContext alarmDao.getTimestampsFlow()
    }

    companion object {
        const val PAGE_SIZE = 10
    }
}