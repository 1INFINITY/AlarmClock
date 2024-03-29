package ru.mirea.ivashechkinav.alarmclock.data.room

import android.net.Uri
import androidx.paging.PagingSource
import androidx.room.*
import kotlinx.coroutines.flow.Flow
import ru.mirea.ivashechkinav.alarmclock.data.room.models.AlarmRoom

@Dao
interface AlarmDao {

    @Query("SELECT * FROM alarms ORDER BY id")
    fun getPagingSource(): PagingSource<Int, AlarmRoom>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun save(alarmRoom: AlarmRoom): Long

    @Update
    suspend fun update(alarm: AlarmRoom)

    @Query("UPDATE alarms SET alarmSoundUri = :uri WHERE id = :id")
    suspend fun updateUri(id: Long, uri: Uri)

    @Delete
    suspend fun delete(alarmRoom: AlarmRoom)

    @Query("DELETE FROM alarms")
    suspend fun clearAll()

    @Query("DELETE FROM alarms WHERE id = :alarmId")
    suspend fun deleteById(alarmId: Long)

    @Query("SELECT * FROM alarms WHERE id = :alarmId")
    suspend fun findById(alarmId: Long): AlarmRoom

    @Query("SELECT invokeTimestamp FROM alarms WHERE isEnable = 1")
    fun getTimestampsFlow(): Flow<List<Long>>

}