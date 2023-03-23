package ru.mirea.ivashechkinav.alarmclock

import android.media.RingtoneManager
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import dagger.hilt.android.AndroidEntryPoint
import ru.mirea.ivashechkinav.alarmclock.databinding.FragmentTimePickerBinding
import ru.mirea.ivashechkinav.alarmclock.domain.AlarmService
import ru.mirea.ivashechkinav.alarmclock.domain.DaysOfWeek
import java.util.*
import javax.inject.Inject

@AndroidEntryPoint
class TimePickerFragment : Fragment() {

    @Inject
    lateinit var alarmService: AlarmServiceImpl
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
            c.set(Calendar.MINUTE, binding.minutePicker.value)
            c.set(Calendar.SECOND, 0)
            getTime()

            val alarm = AlarmUi(
                name = binding.etAlarmName.text.toString(),
                alarmSoundUri = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_ALARM),
                invokeTimestamp = c.timeInMillis,
                daysOfWeek = getDaysOfWeek()
            )
            alarmService.setAlarm(alarm)
        }
        binding.btnCancel.setOnClickListener {
            alarmService.stopAlarm(1)
        }
        return binding.root
    }


    fun getDaysOfWeek(): EnumSet<DaysOfWeek> {
        val set = EnumSet.noneOf(DaysOfWeek::class.java)
        binding.apply {
            if(cbMonday.isChecked) set.add(DaysOfWeek.MONDAY)
            if(cbTuesday.isChecked) set.add(DaysOfWeek.TUESDAY)
            if(cbWednesday.isChecked) set.add(DaysOfWeek.WEDNESDAY)
            if(cbThursday.isChecked) set.add(DaysOfWeek.THURSDAY)
            if(cbFriday.isChecked) set.add(DaysOfWeek.FRIDAY)
            if(cbSaturday.isChecked) set.add(DaysOfWeek.SATURDAY)
            if(cbSunday.isChecked) set.add(DaysOfWeek.SUNDAY)
        }
        return set
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