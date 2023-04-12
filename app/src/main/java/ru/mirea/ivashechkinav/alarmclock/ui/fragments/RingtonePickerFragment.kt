package ru.mirea.ivashechkinav.alarmclock.ui.fragments

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import dagger.hilt.android.AndroidEntryPoint
import ru.mirea.ivashechkinav.alarmclock.R
import ru.mirea.ivashechkinav.alarmclock.databinding.FragmentAlarmsListBinding
import ru.mirea.ivashechkinav.alarmclock.databinding.FragmentRingtonePickerBinding
import ru.mirea.ivashechkinav.alarmclock.ui.adapters.RingtoneDeviceAdapter
import ru.mirea.ivashechkinav.alarmclock.ui.adapters.RingtoneUserAdapter

@AndroidEntryPoint
class RingtonePickerFragment : Fragment() {

    lateinit var binding: FragmentRingtonePickerBinding

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentRingtonePickerBinding.inflate(inflater, container, false)

        binding.listViewUserRingtones.adapter = RingtoneUserAdapter(listOf(), requireContext())
        binding.listViewDeviceRingtones.adapter = RingtoneDeviceAdapter(listOf(), requireContext())

        return binding.root
    }
}