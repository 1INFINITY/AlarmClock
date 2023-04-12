package ru.mirea.ivashechkinav.alarmclock.ui.models

import android.graphics.drawable.Drawable
import android.net.Uri
import ru.mirea.ivashechkinav.alarmclock.domain.RingtoneModel

class RingtoneUi(
    override val name: String,
    override val uri: Uri?,
    val icon: Drawable? = null
) : RingtoneModel