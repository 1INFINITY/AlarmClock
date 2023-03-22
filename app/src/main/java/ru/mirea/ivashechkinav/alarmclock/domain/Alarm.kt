package ru.mirea.ivashechkinav.alarmclock.domain

import android.net.Uri

interface Alarm {
    val id: Int
    val name: String
    val alarmSoundUri: Uri
    val vibrationUri: Uri
    val invokeTimestamp: Long
    val isRepeating: Boolean
    val daysOfWeek: DaysOfWeek
}