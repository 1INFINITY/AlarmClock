package ru.mirea.ivashechkinav.alarmclock.domain.usecase.setRingtoneOnAlarm

import ru.mirea.ivashechkinav.alarmclock.data.repository.AlarmRepositoryImpl
import ru.mirea.ivashechkinav.alarmclock.domain.usecase.UseCaseSuspend
import javax.inject.Inject

class SetRingtoneOnAlarmUseCase @Inject constructor(private val repositoryImpl: AlarmRepositoryImpl) :
    UseCaseSuspend<SetRingtoneOnAlarmArgs, Unit> {
    override suspend fun execute(arg: SetRingtoneOnAlarmArgs) {
        repositoryImpl.updateAlarmUri(id = arg.alarmId, uri = arg.uri)
    }
}