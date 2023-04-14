package ru.mirea.ivashechkinav.alarmclock.domain.usecase.setRingtoneOnAlarm

import android.net.Uri

class SetRingtoneOnAlarmArgs(
    val alarmId: Long,
    val uri: Uri
)