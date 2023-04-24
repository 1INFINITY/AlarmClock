package ru.mirea.ivashechkinav.alarmclock.domain.usecase.getParsedTimeFlowUseCase

import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import ru.mirea.ivashechkinav.alarmclock.domain.usecase.UseCase
import java.text.SimpleDateFormat
import java.util.*
import javax.inject.Inject

class GetParsedTimeFlowUseCase @Inject constructor(): UseCase<GetParsedTimeFlowArgs, Flow<GetParsedTimeFlowResult>> {
    override fun execute(arg: GetParsedTimeFlowArgs): Flow<GetParsedTimeFlowResult> {
        return arg.currentTimestampFlow.map {
            GetParsedTimeFlowResult(
                currentTime = parseTime(it),
                currentDate = parseDate(it)
            )
        }
    }

    private fun parseTime(timestamp: Long): String {
        val date = Date(timestamp)
        val formatter = SimpleDateFormat("HH:mm", Locale("ru", "RU"))
        return formatter.format(date)
    }

    private fun parseDate(invokeTimestamp: Long): String {
        val date = Date(invokeTimestamp)
        val formatter = SimpleDateFormat("EE, dd MMM", Locale("ru", "RU"))
        return formatter.format(date)
    }
}