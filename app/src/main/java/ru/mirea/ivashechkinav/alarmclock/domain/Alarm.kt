package ru.mirea.ivashechkinav.alarmclock.domain

import android.net.Uri
import java.util.*

interface Alarm {
    val name: String
    val alarmSoundUri: Uri
    val invokeTimestamp: Long
    val daysOfWeek: EnumSet<DaysOfWeek>
    val requestCode: Int
}