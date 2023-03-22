package ru.mirea.ivashechkinav.alarmclock

import android.app.AlarmManager
import android.app.AlarmManager.AlarmClockInfo
import android.app.DatePickerDialog
import android.app.PendingIntent
import android.app.TimePickerDialog
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import ru.mirea.ivashechkinav.alarmclock.databinding.ActivityMainBinding
import java.sql.Time
import java.text.SimpleDateFormat
import java.util.*
import kotlin.math.min

class MainActivity : AppCompatActivity() {

    lateinit var binding: ActivityMainBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.btnSetAlarm.setOnClickListener {
            //showDatePickerDialog()
            val newFragment = TimePickerFragment()
            supportFragmentManager
                .beginTransaction()
                .add(R.id.fragmentContainerView, newFragment)
                .addToBackStack(null)
                .commit()
        }
    }

    private fun setAlarm(millis: Long) {
        val alarmManager = getSystemService(Context.ALARM_SERVICE) as AlarmManager
        val alarmClockInfo = AlarmClockInfo(millis, getAlarmInfoPendingIntent())

        alarmManager.setAlarmClock(alarmClockInfo, getAlarmActionPendingIntent())
    }

    private fun showDatePickerDialog() {
        val c = Calendar.getInstance()
        val year = c.get(Calendar.YEAR)
        val month = c.get(Calendar.MONTH)
        val day = c.get(Calendar.DAY_OF_MONTH)
        val hour = c.get(Calendar.HOUR)
        val minute = c.get(Calendar.MINUTE)
        val second = c.get(Calendar.SECOND)

        val timeListener = TimePickerDialog.OnTimeSetListener { view, hour, minute ->
            // Display Selected date in textbox
            val calendar =
                Calendar.getInstance().also {
                    it.set(year, month, day, hour, minute)
                }
            val date = Date(calendar.timeInMillis)
            val format = SimpleDateFormat("HH:mm:ss", Locale.getDefault())
            val dateString = format.format(date)
            Toast.makeText(this, "Set alarm! on $dateString", Toast.LENGTH_LONG).show()
            setAlarm(calendar.timeInMillis)
        }
        TimePickerDialog(
            this,
            timeListener,
            hour,
            minute,
            true
        ).show()

    }

    private fun getAlarmInfoPendingIntent(): PendingIntent {
        val alarmInfoIntent = Intent(this, MainActivity::class.java)
        alarmInfoIntent.flags = Intent.FLAG_ACTIVITY_CLEAR_TOP or Intent.FLAG_ACTIVITY_NEW_TASK
        val pendingFlags = PendingIntent.FLAG_UPDATE_CURRENT or PendingIntent.FLAG_IMMUTABLE
        return PendingIntent.getActivity(this, 0, alarmInfoIntent, pendingFlags)
    }

    private fun getAlarmActionPendingIntent(): PendingIntent {
        val alarmActionIntent = Intent(this, AlarmActivity::class.java)
        alarmActionIntent.flags = Intent.FLAG_ACTIVITY_CLEAR_TOP or Intent.FLAG_ACTIVITY_NEW_TASK
        val pendingFlags = PendingIntent.FLAG_UPDATE_CURRENT or PendingIntent.FLAG_IMMUTABLE
        return PendingIntent.getActivity(this, 1, alarmActionIntent, pendingFlags)
    }
}