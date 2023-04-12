package ru.mirea.ivashechkinav.alarmclock.ui.models

import android.annotation.SuppressLint
import android.net.Uri
import android.text.SpannableString
import ru.mirea.ivashechkinav.alarmclock.domain.Alarm
import ru.mirea.ivashechkinav.alarmclock.domain.DaysOfWeek
import ru.mirea.ivashechkinav.alarmclock.domain.usecase.GetSpannableDaysStringUseCase
import java.text.SimpleDateFormat
import java.util.*
import javax.inject.Inject

data class AlarmUi(
    override val id: Long?,
    override val name: String,
    override val alarmSoundUri: Uri,
    override val invokeTimestamp: Long,
    override val daysOfWeek: EnumSet<DaysOfWeek>,
    override val isEnable: Boolean,
    override val isVibrationEnable: Boolean
) : Alarm {
    val getSpannableUseCase = GetSpannableDaysStringUseCase()

    val timeInvoke: String
        get() = parseTime()
    val dayInvoke: SpannableString
        get() = getSpannableUseCase.execute(daysOfWeek) ?: parseDay()

    constructor(alarm: Alarm) : this(
        id = alarm.id,
        name = alarm.name,
        alarmSoundUri = alarm.alarmSoundUri,
        invokeTimestamp = alarm.invokeTimestamp,
        daysOfWeek = alarm.daysOfWeek,
        isEnable = alarm.isEnable,
        isVibrationEnable = alarm.isVibrationEnable
    )

    private fun parseTime(): String {
        val date = Date(invokeTimestamp)
        val formatter = SimpleDateFormat("HH:mm", Locale("ru", "RU"))
        return formatter.format(date)
    }

    private fun parseDay(): SpannableString {
        val date = Date(invokeTimestamp)
        val formatter = SimpleDateFormat("EE, dd MMM", Locale("ru", "RU"))
        return SpannableString(formatter.format(date))
    }
}
