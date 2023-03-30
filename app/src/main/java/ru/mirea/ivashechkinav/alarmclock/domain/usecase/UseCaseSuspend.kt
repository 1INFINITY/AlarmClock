package ru.mirea.ivashechkinav.alarmclock.domain.usecase

interface UseCaseSuspend<Arg, out Result> {
    suspend fun execute(arg: Arg): Result
}