package ru.mirea.ivashechkinav.alarmclock.ui.fragments

import android.app.TimePickerDialog
import android.media.RingtoneManager
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import androidx.paging.map
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.android.material.snackbar.Snackbar
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.delay
import ru.mirea.ivashechkinav.alarmclock.R
import ru.mirea.ivashechkinav.alarmclock.data.repository.AlarmRepositoryImpl
import ru.mirea.ivashechkinav.alarmclock.databinding.FragmentAlarmsListBinding
import ru.mirea.ivashechkinav.alarmclock.domain.Alarm
import ru.mirea.ivashechkinav.alarmclock.domain.DaysOfWeek
import ru.mirea.ivashechkinav.alarmclock.domain.usecase.getNextMinInvokeAlarmTime.GetNextMinInvokeAlarmTimeUseCase
import ru.mirea.ivashechkinav.alarmclock.ui.adapters.AlarmPagingAdapter
import ru.mirea.ivashechkinav.alarmclock.ui.AlarmServiceImpl
import ru.mirea.ivashechkinav.alarmclock.ui.models.AlarmUi
import java.util.*
import javax.inject.Inject

@AndroidEntryPoint
class AlarmsListFragment : Fragment() {

    @Inject
    lateinit var repositoryImpl: AlarmRepositoryImpl

    @Inject
    lateinit var alarmServiceImpl: AlarmServiceImpl

    @Inject
    lateinit var getNextMinInvokeAlarmTimeUseCase: GetNextMinInvokeAlarmTimeUseCase

    lateinit var binding: FragmentAlarmsListBinding
    lateinit var adapter: AlarmPagingAdapter

    private val timePickerDialogListener: TimePickerDialog.OnTimeSetListener =
        TimePickerDialog.OnTimeSetListener { view, hourOfDay, minute ->
            val c = Calendar.getInstance()
            c.set(Calendar.HOUR_OF_DAY, hourOfDay)
            c.set(Calendar.MINUTE, minute)
            c.set(Calendar.SECOND, 0)

            saveAlarm(c.timeInMillis)
            val formattedTime: String = String.format("%02d:%02d", hourOfDay, minute)

            Snackbar.make(requireContext(), requireView(), formattedTime, Snackbar.LENGTH_LONG).show()
        }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentAlarmsListBinding.inflate(inflater, container, false)

        initHeader()
        initButtons()
        initRecyclerView()
        return binding.root
    }

    private fun saveAlarm(timeInMillis: Long) {
        val alarm = AlarmUi(
            id = null,
            name = "",
            alarmSoundUri = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_ALARM),
            invokeTimestamp = timeInMillis,
            daysOfWeek = EnumSet.noneOf(DaysOfWeek::class.java),
            isEnable = true,
            isVibrationEnable = true
        )
        alarmServiceImpl.setAlarm(alarm)
    }
    private fun initHeader() {
        lifecycleScope.launchWhenStarted {
            repeat(Int.MAX_VALUE) {
                updateHeaderState()
                delay(60 * 1000)
            }
        }
    }

    private suspend fun updateHeaderState() {
        val currentTimestamp = Calendar.getInstance().timeInMillis
        val result = getNextMinInvokeAlarmTimeUseCase.execute(currentTimestamp)
        if (result == null) {
            binding.tvAlarmsState.text = "Следующего будильника нет"
            binding.tvAlarmsStateTime.text = ""
            return
        }
        binding.tvAlarmsState.text = "Будильник через\n${result.nextTime}"
        binding.tvAlarmsStateTime.text = result.nextDate
    }

    private fun initButtons() {
        binding.btnAdd.setOnClickListener {
            val timePicker: TimePickerDialog = TimePickerDialog(
                requireContext(),
                timePickerDialogListener,
                12,
                10,
                true
            )
            timePicker.show()
        }
    }

    private fun initRecyclerView() {
        adapter = AlarmPagingAdapter(object : AlarmPagingAdapter.Listener {

            override fun onToggleSwitch(alarm: Alarm) {
                Log.d(this@AlarmsListFragment::class.simpleName, "Вызвана функция ${object{}.javaClass.enclosingMethod?.name}")
                lifecycleScope.launchWhenStarted {
                    repositoryImpl.updateAlarm((alarm as AlarmUi).copy(isEnable = !alarm.isEnable))
                }
            }

            override fun onDeleteAlarm(alarm: Alarm) {
                Log.d(this@AlarmsListFragment::class.simpleName, "Вызвана функция ${object{}.javaClass.enclosingMethod?.name}")
                lifecycleScope.launchWhenStarted {
                    repositoryImpl.deleteAlarm(alarm)
                }
            }

            override fun onToggleCheckBoxes(alarm: Alarm, selectedDays: EnumSet<DaysOfWeek>) {
                val newAlarm = (alarm as AlarmUi).copy(daysOfWeek = selectedDays)
                lifecycleScope.launchWhenStarted {
                    repositoryImpl.updateAlarm(newAlarm)
                }
            }

            override fun onChooseRingtone(alarm: Alarm) {
                Log.d(this@AlarmsListFragment::class.simpleName, "Вызвана функция ${object{}.javaClass.enclosingMethod?.name}")
                findNavController().navigate(R.id.action_alarmsListFragment_to_ringtonePickerFragment)
            }

            override fun onToggleVibration(alarm: Alarm) {
                Log.d(this@AlarmsListFragment::class.simpleName, "Вызвана функция ${object{}.javaClass.enclosingMethod?.name}")
                val newAlarm =(alarm as AlarmUi).copy(isVibrationEnable = !alarm.isVibrationEnable)
                lifecycleScope.launchWhenStarted {
                    repositoryImpl.updateAlarm(newAlarm)
                }
            }

        })
        lifecycleScope.launchWhenStarted {
            repositoryImpl.getAlarms().collect { pagingData ->
                adapter.submitData(
                    pagingData.map { AlarmUi(it) }
                )
            }
        }
        binding.recyclerViewAlarm.layoutManager = LinearLayoutManager(requireContext())
        binding.recyclerViewAlarm.adapter = adapter
    }
}