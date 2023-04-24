package ru.mirea.ivashechkinav.alarmclock.domain.usecase.getParsedTimeFlowUseCase

import kotlinx.coroutines.flow.Flow

class GetParsedTimeFlowArgs(
    val currentTimestampFlow: Flow<Long>
)