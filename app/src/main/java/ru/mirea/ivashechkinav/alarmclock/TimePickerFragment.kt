package ru.mirea.ivashechkinav.alarmclock

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.NumberPicker
import android.widget.Toast
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
            getTime()
        }
        return binding.root
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