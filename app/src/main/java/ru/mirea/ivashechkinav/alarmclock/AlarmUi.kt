package ru.mirea.ivashechkinav.alarmclock

import android.annotation.SuppressLint
import android.net.Uri
import ru.mirea.ivashechkinav.alarmclock.domain.Alarm
import ru.mirea.ivashechkinav.alarmclock.domain.DaysOfWeek
import java.text.SimpleDateFormat
import java.util.*

data class AlarmUi(
    override val id: Long?,
    override val name: String,
    override val alarmSoundUri: Uri,
    override val invokeTimestamp: Long,
    override val daysOfWeek: EnumSet<DaysOfWeek>,
    override val isEnable: Boolean,
) : Alarm {
    val timeInvoke: String
        get() = parseTime()
    val dayInvoke: String
        get() = parseDay()

    constructor(alarm: Alarm) : this(
        id = alarm.id,
        name = alarm.name,
        alarmSoundUri = alarm.alarmSoundUri,
        invokeTimestamp = alarm.invokeTimestamp,
        daysOfWeek = alarm.daysOfWeek,
        isEnable = alarm.isEnable
    )

    private fun parseTime(): String {
        val date = Date(invokeTimestamp)
        val formatter = SimpleDateFormat("hh:mm", Locale("ru", "RU"))
        return formatter.format(date)
    }

    private fun parseDay(): String {
        val date = Date(invokeTimestamp)
        val formatter = SimpleDateFormat("EE, dd MMM", Locale("ru", "RU"))
        return formatter.format(date)
    }
}
