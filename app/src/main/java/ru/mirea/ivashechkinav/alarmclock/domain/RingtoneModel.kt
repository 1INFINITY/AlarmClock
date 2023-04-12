package ru.mirea.ivashechkinav.alarmclock.domain

import android.net.Uri

interface RingtoneModel {
    val name: String
    val uri: Uri?
}