package ru.mirea.ivashechkinav.alarmclock.ui

import android.Manifest
import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.media.AudioAttributes
import android.media.RingtoneManager
import androidx.core.app.ActivityCompat
import androidx.core.app.NotificationCompat
import androidx.core.app.NotificationManagerCompat
import dagger.hilt.android.qualifiers.ApplicationContext
import ru.mirea.ivashechkinav.alarmclock.R
import ru.mirea.ivashechkinav.alarmclock.domain.Alarm
import ru.mirea.ivashechkinav.alarmclock.ui.activities.AlarmActivity
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class NotificationService @Inject constructor(@ApplicationContext val ctx: Context) {

    fun showNotification(alarm: Alarm) {
        createNotificationChannel(alarm)
        createFullScreenNotification(alarm)
    }

    fun closeNotification(alarmId: Long) {
        val notificationId = alarmId.toInt()
        NotificationManagerCompat.from(ctx).cancel(notificationId)
    }

    private fun createFullScreenNotification(alarm: Alarm) {
        val notificationId = alarm.id?.toInt() ?: throw IllegalArgumentException("Alarm must have an id")
        val fullScreenIntent = Intent(ctx, AlarmActivity::class.java)
        fullScreenIntent.putExtra("alarmId", alarm.id)
        fullScreenIntent.flags =
            Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_NO_USER_ACTION or Intent.FLAG_ACTIVITY_SINGLE_TOP
        val pendingIntent = PendingIntent.getActivity(
            ctx,
            0,
            fullScreenIntent,
            PendingIntent.FLAG_UPDATE_CURRENT
        )

        val notificationManager = NotificationManagerCompat.from(ctx)
        val notification = getFullScreenNotificationBuilder(ctx, pendingIntent, alarm).build()
        if (ActivityCompat.checkSelfPermission(
                ctx,
                Manifest.permission.POST_NOTIFICATIONS
            ) == PackageManager.PERMISSION_GRANTED
        ) {
            notificationManager.notify(
                notificationId,
                notification
            )
        }
    }

    private fun getFullScreenNotificationBuilder(
        context: Context,
        pendingIntent: PendingIntent,
        alarm: Alarm
    ): NotificationCompat.Builder {
        val channelId = createChannelId(alarm)
        return NotificationCompat.Builder(context, channelId).apply {
            setSmallIcon(R.drawable.bell_icon)
            setContentTitle("Full Screen Alarm Test")
            setContentText("This is a test")
            priority = NotificationCompat.PRIORITY_MAX
            setCategory(NotificationCompat.CATEGORY_ALARM)
            setContentIntent(pendingIntent)
            setFullScreenIntent(pendingIntent, true)
        }
    }

    private fun createNotificationChannel(alarm: Alarm) {
        val importance = NotificationManager.IMPORTANCE_HIGH
        val channelId = createChannelId(alarm)
        val channel = NotificationChannel(channelId, NOTIFICATION_CHANNEL_NAME, importance)

        val audioAttributes = AudioAttributes.Builder()
            .setContentType(AudioAttributes.CONTENT_TYPE_UNKNOWN)
            .setUsage(AudioAttributes.USAGE_ALARM)
            .build()
        channel.setSound(alarm.alarmSoundUri, audioAttributes)

        channel.description = NOTIFICATION_CHANNEL_DESCRIPTION
        if(alarm.isVibrationEnable) channel.vibrationPattern = VIBRATION_PATTERN

        val notificationManager = ctx.getSystemService(
            NotificationManager::class.java
        )
        notificationManager.createNotificationChannel(channel)
    }

    private fun createChannelId(alarm: Alarm): String {
        val nameRingtone = RingtoneManager.getRingtone(ctx, alarm.alarmSoundUri).getTitle(ctx)
        val isVibrating = alarm.isVibrationEnable
        return "${nameRingtone}_$isVibrating"
    }

    companion object {
        val VIBRATION_PATTERN = longArrayOf(1000, 1000, 1000, 1000, 1000)
        const val NOTIFICATION_CHANNEL_NAME = "alarmClockReminderChannel"
        const val NOTIFICATION_CHANNEL_DESCRIPTION = "Channel For Alarm Manager"
    }
}