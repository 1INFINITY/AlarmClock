package ru.mirea.ivashechkinav.alarmclock.domain.usecase.getNextMinInvokeAlarmTime

import kotlinx.coroutines.flow.Flow

data class NextMinInvokeAlarmTimeArgs(
    val currentTimestampFlow: Flow<Long>
)