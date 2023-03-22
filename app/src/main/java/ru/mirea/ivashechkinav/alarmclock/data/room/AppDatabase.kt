package ru.mirea.ivashechkinav.alarmclock.data.room

import androidx.room.Database
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import ru.mirea.ivashechkinav.alarmclock.data.room.models.AlarmRoom

@Database(
    version = 1,
    entities = [
        AlarmRoom::class,
    ]
)
@TypeConverters(UriConverter::class, DaysOfWeekConverter::class)
abstract class AppDatabase : RoomDatabase() {

    abstract fun getAlarmDao(): AlarmDao

}