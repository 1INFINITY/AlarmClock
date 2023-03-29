package ru.mirea.ivashechkinav.alarmclock.domain.usecase

interface UseCase<Arg, out Result> {
    fun execute(arg: Arg): Result
}