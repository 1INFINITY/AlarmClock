package ru.mirea.ivashechkinav.alarmclock.domain.usecase.getNextMinInvokeAlarmTime

import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.transform
import ru.mirea.ivashechkinav.alarmclock.data.repository.AlarmRepositoryImpl
import ru.mirea.ivashechkinav.alarmclock.domain.usecase.UseCaseSuspend
import java.text.SimpleDateFormat
import java.util.*
import javax.inject.Inject

class GetNextMinInvokeAlarmTimeUseCase @Inject constructor(private val repositoryImpl: AlarmRepositoryImpl) :
    UseCaseSuspend<NextMinInvokeAlarmTimeArgs, Flow<NextMinInvokeAlarmTimeResult?>> {
    override suspend fun execute(arg: NextMinInvokeAlarmTimeArgs): Flow<NextMinInvokeAlarmTimeResult?> {
        val firstFlow = arg.currentTimestampFlow
        val secondFlow = repositoryImpl.getTimestampsFlow()
        return secondFlow.combine(firstFlow) { timestampList, currentTimestamp ->
            val nextTimeStamp = getNextMinTimestamp(timestampList, currentTimestamp) ?: return@combine(null)
            return@combine (
                NextMinInvokeAlarmTimeResult(
                    nextTime = parseTime(nextTimeStamp - currentTimestamp),
                    nextDate = parseDate(nextTimeStamp)
                )
            )
        }
    }

    private fun getNextMinTimestamp(timestampList: List<Long>, currentTimestamp: Long): Long? {
        return timestampList.sorted().find { it > currentTimestamp }
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