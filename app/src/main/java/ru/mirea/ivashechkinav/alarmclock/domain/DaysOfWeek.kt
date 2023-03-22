package ru.mirea.ivashechkinav.alarmclock.domain

import kotlin.experimental.and

enum class DaysOfWeek(private val value: Byte) {
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
}