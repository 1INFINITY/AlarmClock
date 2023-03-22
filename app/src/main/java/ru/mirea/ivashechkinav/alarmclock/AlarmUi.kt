package ru.mirea.ivashechkinav.alarmclock

import android.net.Uri
import ru.mirea.ivashechkinav.alarmclock.domain.Alarm
import ru.mirea.ivashechkinav.alarmclock.domain.DaysOfWeek

data class AlarmUi(
    override val name: String,
    override val alarmSoundUri: Uri,
    override val vibrationUri: Uri,
    override val invokeTimestamp: Long,
    override val isRepeating: Boolean,
    override val daysOfWeek: DaysOfWeek
) : Alarm {
    override val id: Int
        get() = 0
}
