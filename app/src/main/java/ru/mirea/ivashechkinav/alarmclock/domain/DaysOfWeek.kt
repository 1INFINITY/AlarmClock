package ru.mirea.ivashechkinav.alarmclock.domain

import java.util.*
import kotlin.experimental.and
import kotlin.experimental.or

enum class DaysOfWeek(val value: Byte) {
    MONDAY(0b00000001),
    TUESDAY(0b00000010),
    WEDNESDAY(0b00000100),
    THURSDAY(0b00001000),
    FRIDAY(0b00010000),
    SATURDAY(0b00100000),
    SUNDAY(0b01000000);

    fun has(day: DaysOfWeek): Boolean {
        return value and day.value != 0.toByte()
    }

    fun getVal(): Byte = value

    companion object {
        @JvmStatic
        fun fromInt(day: Int): DaysOfWeek {
            return when (day) {
                1 -> SUNDAY
                2 -> MONDAY
                3 -> TUESDAY
                4 -> WEDNESDAY
                5 -> THURSDAY
                6 -> FRIDAY
                7 -> SATURDAY
                else -> throw IllegalArgumentException("current day of week must be in interval 1..7")
            }
        }

        @JvmStatic
        fun toInt(day: DaysOfWeek): Int {
            return when (day) {
                SUNDAY -> 1
                MONDAY -> 2
                TUESDAY -> 3
                WEDNESDAY -> 4
                THURSDAY -> 5
                FRIDAY -> 6
                SATURDAY -> 7
                else -> throw IllegalArgumentException("current day of week undefined")
            }
        }

        @JvmStatic
        fun fromByte(value: Byte): EnumSet<DaysOfWeek> {
            val days = EnumSet.noneOf(DaysOfWeek::class.java)
            enumValues<DaysOfWeek>().forEach {
                if (value and it.value != 0.toByte())
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