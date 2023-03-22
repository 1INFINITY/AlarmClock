package ru.mirea.ivashechkinav.alarmclock.data.room

import androidx.paging.PagingSource
import androidx.room.*
import ru.mirea.ivashechkinav.alarmclock.data.room.models.AlarmRoom

@Dao
interface AlarmDao {

    @Query("SELECT * FROM alarms ORDER BY invokeTimestamp DESC")
    fun getPagingSource(): PagingSource<Int, AlarmRoom>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun save(alarmRoom: AlarmRoom)

    @Delete
    suspend fun delete(alarmRoom: AlarmRoom)
}