package ru.mirea.ivashechkinav.alarmclock

import android.app.AlarmManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import ru.mirea.ivashechkinav.alarmclock.databinding.FragmentTimePickerBinding
import java.util.*

class TimePickerFragment : Fragment() {

    private lateinit var binding: FragmentTimePickerBinding

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        binding = FragmentTimePickerBinding.inflate(inflater, container, false)


        // Устанавливаем значения для NumberPicker для часов и минут
        val hours = (0..23).map { String.format("%02d", it) }.toTypedArray()
        binding.hourPicker.minValue = 0
        binding.hourPicker.maxValue = hours.size - 1
        binding.hourPicker.displayedValues = hours

        val minutes = (0..59).map { String.format("%02d", it) }.toTypedArray()
        binding.minutePicker.minValue = 0
        binding.minutePicker.maxValue = minutes.size - 1
        binding.minutePicker.displayedValues = minutes

        // Устанавливаем текущее время
        val cal = Calendar.getInstance()
        binding.hourPicker.value = cal.get(Calendar.HOUR_OF_DAY)
        binding.minutePicker.value = cal.get(Calendar.MINUTE)

        binding.btnSave.setOnClickListener {
            val c = Calendar.getInstance()
            c.set(Calendar.HOUR_OF_DAY, binding.hourPicker.value)
            //c.set(Calendar.MINUTE, binding.minutePicker.value)
            c.set(Calendar.SECOND, c.get(Calendar.SECOND) + 3)
            getTime()
            setAlarm(c.timeInMillis)
        }
        binding.btnCancel.setOnClickListener {
            stopAlarm(1)
        }
        return binding.root
    }

    private fun stopAlarm(alarmRequestCode: Int) {
        val alarmManager = requireContext().getSystemService(Context.ALARM_SERVICE) as AlarmManager
        val intent = Intent(requireContext(), AlarmActivity::class.java)
        val pendingFlags = PendingIntent.FLAG_UPDATE_CURRENT or PendingIntent.FLAG_IMMUTABLE
        val pendingIntent = PendingIntent.getActivity(requireContext(), alarmRequestCode, intent, pendingFlags)
        alarmManager.cancel(pendingIntent)
    }
    private fun setAlarm(millis: Long) {
        val alarmManager = requireContext().getSystemService(Context.ALARM_SERVICE) as AlarmManager
        val alarmClockInfo = AlarmManager.AlarmClockInfo(millis, getAlarmInfoPendingIntent())

        alarmManager.setAlarmClock(alarmClockInfo, getAlarmActionPendingIntent())
    }

    private fun getAlarmInfoPendingIntent(): PendingIntent {
        val alarmInfoIntent = Intent(requireContext(), MainActivity::class.java)
        alarmInfoIntent.flags = Intent.FLAG_ACTIVITY_CLEAR_TOP or Intent.FLAG_ACTIVITY_NEW_TASK
        val pendingFlags = PendingIntent.FLAG_UPDATE_CURRENT or PendingIntent.FLAG_IMMUTABLE
        return PendingIntent.getActivity(requireContext(), 0, alarmInfoIntent, pendingFlags)
    }

    private fun getAlarmActionPendingIntent(): PendingIntent {
        val alarmActionIntent = Intent(requireContext(), AlarmActivity::class.java)
        alarmActionIntent.flags = Intent.FLAG_ACTIVITY_CLEAR_TOP or Intent.FLAG_ACTIVITY_NEW_TASK
        val pendingFlags = PendingIntent.FLAG_UPDATE_CURRENT or PendingIntent.FLAG_IMMUTABLE
        return PendingIntent.getActivity(requireContext(), 1, alarmActionIntent, pendingFlags)
    }

    fun getTime() {
        // Получаем выбранное время
        val hour = binding.hourPicker.value
        val minute = binding.minutePicker.value
        val timeString = "%02d:%02d".format(hour, minute)
        // Форматируем время в строку
        Toast.makeText(requireContext(), timeString, Toast.LENGTH_LONG).show()
    }

}