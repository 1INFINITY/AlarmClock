package ru.mirea.ivashechkinav.alarmclock.domain.usecase.getNextMinInvokeAlarmTime

import ru.mirea.ivashechkinav.alarmclock.data.repository.AlarmRepositoryImpl
import ru.mirea.ivashechkinav.alarmclock.domain.usecase.UseCaseSuspend
import java.text.SimpleDateFormat
import java.util.*
import javax.inject.Inject

class GetNextMinInvokeAlarmTimeUseCase @Inject constructor(private val repositoryImpl: AlarmRepositoryImpl) :
    UseCaseSuspend<Long, NextMinInvokeAlarmTimeResult?> {
    override suspend fun execute(arg: Long): NextMinInvokeAlarmTimeResult? {
        val nextTimestamp =
            repositoryImpl.getNextMinTimestamp(currentTimestamp = arg) ?: return null

        return NextMinInvokeAlarmTimeResult(
            nextTime = parseTime(nextTimestamp - arg),
            nextDate = parseDate(nextTimestamp)
        )
    }

    private fun parseTime(timestampDiff: Long): String {
        val days = ((timestampDiff / 1000 / 60 / 60) / 24).toInt()
        val hours = ((timestampDiff / 1000 / 60 / 60) % 24).toInt()
        val minutes = (timestampDiff / 1000 / 60) % 60
        if (days > 0) {
            //Days cannot be greater than 7
            val dayWord = if (days == 1) "день" else if (days in 2..4) "дня" else "дней"
            return "$days $dayWord"
        }
        if (hours == 0) {
            return "$minutes мин."
        }
        return "$hours ч. $minutes мин."
    }

    private fun parseDate(invokeTimestamp: Long): String {
        val date = Date(invokeTimestamp)
        val formatter = SimpleDateFormat("EE, dd MMM, HH:mm", Locale("ru", "RU"))
        return formatter.format(date)
    }
}