package ru.mirea.ivashechkinav.alarmclock.domain

import android.net.Uri
import java.util.*

interface Alarm {
    val id: Long?
    val name: String
    val alarmSoundUri: Uri
    val invokeTimestamp: Long
    val daysOfWeek: EnumSet<DaysOfWeek>
    val isEnable: Boolean
    val isVibrationEnable: Boolean
}