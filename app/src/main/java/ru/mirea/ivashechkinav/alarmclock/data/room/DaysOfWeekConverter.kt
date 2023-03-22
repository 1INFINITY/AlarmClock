package ru.mirea.ivashechkinav.alarmclock.data.room

import androidx.room.TypeConverter
import ru.mirea.ivashechkinav.alarmclock.domain.DaysOfWeek
import java.util.*

class DaysOfWeekConverter {
    @TypeConverter
    fun fromDays(daysOfWeek: EnumSet<DaysOfWeek>): Byte {
        return DaysOfWeek.toByte(daysOfWeek)
    }
    @TypeConverter
    fun toDays(byte: Byte): EnumSet<DaysOfWeek> {
        return DaysOfWeek.fromByte(byte)
    }
}
