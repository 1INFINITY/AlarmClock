package ru.mirea.ivashechkinav.alarmclock.data.room

import androidx.paging.PagingSource
import androidx.room.*
import ru.mirea.ivashechkinav.alarmclock.data.room.models.AlarmRoom

@Dao
interface AlarmDao {

    @Query("SELECT * FROM alarms ORDER BY invokeTimestamp DESC")
    fun getPagingSource(): PagingSource<Int, AlarmRoom>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun save(alarmRoom: AlarmRoom): Long

    @Delete
    suspend fun delete(alarmRoom: AlarmRoom)

    @Query("DELETE FROM alarms")
    suspend fun clearAll()

    @Query("DELETE FROM alarms WHERE id = :alarmId")
    suspend fun deleteById(alarmId: Long)

    @Query("SELECT * FROM alarms WHERE id = :alarmId")
    suspend fun findById(alarmId: Long): AlarmRoom
}