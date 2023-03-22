package ru.mirea.ivashechkinav.alarmclock.data.room.models

import android.net.Uri
import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import androidx.room.TypeConverters
import ru.mirea.ivashechkinav.alarmclock.data.room.DaysOfWeekConverter
import ru.mirea.ivashechkinav.alarmclock.data.room.UriConverter
import ru.mirea.ivashechkinav.alarmclock.domain.Alarm
import ru.mirea.ivashechkinav.alarmclock.domain.DaysOfWeek
import java.util.*

@Entity(tableName = "alarms")
data class AlarmRoom(
    override val name: String,
    override val alarmSoundUri: Uri,
    override val invokeTimestamp: Long,
    override val daysOfWeek: EnumSet<DaysOfWeek>,
) : Alarm {
    @PrimaryKey(autoGenerate = true)
    var id: Int? = null

    override val requestCode: Int
        get() = id!!

    constructor(alarm: Alarm) : this(
        name = alarm.name,
        alarmSoundUri = alarm.alarmSoundUri,
        invokeTimestamp = alarm.invokeTimestamp,
        daysOfWeek = alarm.daysOfWeek
    )
}