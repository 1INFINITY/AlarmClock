package ru.mirea.ivashechkinav.alarmclock.domain.usecase.getNextMinInvokeAlarmTime

import ru.mirea.ivashechkinav.alarmclock.data.repository.AlarmRepositoryImpl
import ru.mirea.ivashechkinav.alarmclock.domain.usecase.UseCaseSuspend
import java.text.SimpleDateFormat
import java.util.*
import javax.inject.Inject

class GetNextMinInvokeAlarmTimeUseCase @Inject constructor(private val repositoryImpl: AlarmRepositoryImpl) :
    UseCaseSuspend<Long, NextMinInvokeAlarmTimeResult?> {
    override suspend fun execute(arg: Long): NextMinInvokeAlarmTimeResult? {
        val nextTimestamp = repositoryImpl.getNextMinTimestamp(currentTimestamp = arg) ?: return null

        return NextMinInvokeAlarmTimeResult(
            nextTime = parseTime(nextTimestamp - arg),
            nextDate = parseDate(nextTimestamp)
        )
    }

    private fun parseTime(timestampDiff: Long): String {
        val minutes = (timestampDiff / 1000 / 60) % 60
        val hours = (timestampDiff / 1000 / 60 / 60) % 24
        return "$hours ч. $minutes мин."
    }

    private fun parseDate(invokeTimestamp: Long): String {
        val date = Date(invokeTimestamp)
        val formatter = SimpleDateFormat("EE, dd MMM, HH:mm", Locale("ru", "RU"))
        return formatter.format(date)
    }
}