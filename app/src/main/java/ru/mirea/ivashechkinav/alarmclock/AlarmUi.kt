package ru.mirea.ivashechkinav.alarmclock

import android.net.Uri
import ru.mirea.ivashechkinav.alarmclock.domain.Alarm
import ru.mirea.ivashechkinav.alarmclock.domain.DaysOfWeek
import java.util.*

data class AlarmUi(
    override val name: String,
    override val alarmSoundUri: Uri,
    override val invokeTimestamp: Long,
    override val daysOfWeek: EnumSet<DaysOfWeek>
) : Alarm {
    override val requestCode: Int
        get() = 0
}
