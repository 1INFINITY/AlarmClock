package ru.mirea.ivashechkinav.alarmclock.ui

import android.Manifest
import android.app.PendingIntent
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.util.Log
import androidx.core.app.ActivityCompat
import androidx.core.app.NotificationCompat
import androidx.core.app.NotificationManagerCompat
import dagger.hilt.android.AndroidEntryPoint
import ru.mirea.ivashechkinav.alarmclock.R
import ru.mirea.ivashechkinav.alarmclock.ui.activities.AlarmActivity
import javax.inject.Inject

@AndroidEntryPoint
class AlarmReceiver : BroadcastReceiver() {
    @Inject
    lateinit var alarmServiceImpl: AlarmServiceImpl
    override fun onReceive(context: Context, intent: Intent) {
        val alarmId = intent.getLongExtra("alarmId", -1)
        if(alarmId == -1L) return
        alarmServiceImpl.alarmSwitch(alarmId)
        createFullScreenNotification(context, alarmId)
    }

    private fun createFullScreenNotification(context: Context, alarmId: Long) {
        val fullScreenIntent = Intent(context, AlarmActivity::class.java)
        fullScreenIntent.putExtra("requestCode", alarmId)
        fullScreenIntent.flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_NO_USER_ACTION or Intent.FLAG_ACTIVITY_SINGLE_TOP
        val pendingIntent = PendingIntent.getActivity(context, 0, fullScreenIntent, PendingIntent.FLAG_UPDATE_CURRENT)

        val notificationManager = NotificationManagerCompat.from(context)
        val notificationBuilder = getFullScreenNotificationBuilder(context, pendingIntent)
        if (ActivityCompat.checkSelfPermission(
                context,
                Manifest.permission.POST_NOTIFICATIONS
            ) == PackageManager.PERMISSION_GRANTED
        ) {
            notificationManager.notify(
                123,
                notificationBuilder.build()
            )
        }
    }
    private fun getFullScreenNotificationBuilder(context: Context, pendingIntent: PendingIntent): NotificationCompat.Builder {
        return NotificationCompat.Builder(context, "ivashechkinav.alarmclock")
                .setSmallIcon(R.drawable.ic_launcher_background)
                .setContentTitle("Full Screen Alarm Test")
                .setContentText("This is a test")
                .setPriority(NotificationCompat.PRIORITY_MAX)
                .setCategory(NotificationCompat.CATEGORY_ALARM)
                .setContentIntent(pendingIntent)
                .setFullScreenIntent(pendingIntent, true)
    }
}