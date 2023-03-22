package ru.mirea.ivashechkinav.alarmclock.domain

import java.util.EnumSet
import kotlin.experimental.and
import kotlin.experimental.or

enum class DaysOfWeek(val value: Byte) {
    MONDAY(0b00000001),
    TUESDAY(0b00000010),
    WEDNESDAY(0b00000100),
    THURSDAY(0b00001000),
    FRIDAY(0b00010000),
    SATURDAY(0b00100000),
    SUNDAY(0b01000000),
    ALL(0b01111111);

    fun has(day: DaysOfWeek): Boolean {
        return value and day.value != 0.toByte()
    }

    fun getVal(): Byte = value

    companion object {
        @JvmStatic
        fun fromByte(value: Byte): EnumSet<DaysOfWeek> {
            val days = EnumSet.noneOf(DaysOfWeek::class.java)
            enumValues<DaysOfWeek>().forEach {
                if(value and it.value != 0.toByte())
                    days.add(it)
            }
            return days
        }

        @JvmStatic
        fun toByte(days: EnumSet<DaysOfWeek>): Byte {
            var byte = 0.toByte()
            days.forEach {
                byte = byte or it.value
            }
            return byte
        }
    }
}