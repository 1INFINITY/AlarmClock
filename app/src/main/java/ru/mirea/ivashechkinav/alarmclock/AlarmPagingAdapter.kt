package ru.mirea.ivashechkinav.alarmclock

import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.CheckBox
import androidx.paging.PagingDataAdapter
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import ru.mirea.ivashechkinav.alarmclock.databinding.ItemAlarmBinding
import ru.mirea.ivashechkinav.alarmclock.domain.Alarm
import ru.mirea.ivashechkinav.alarmclock.domain.DaysOfWeek
import java.util.*

class AlarmPagingAdapter(
    private val listener: Listener
): PagingDataAdapter<AlarmUi, AlarmPagingAdapter.AlarmHolder>(ItemCallback), View.OnClickListener {

    private var selectedPosition = RecyclerView.NO_POSITION
    private fun setSelectedPosition(position: Int) {
        if (selectedPosition != position) {
            notifyItemChanged(selectedPosition)
            selectedPosition = position
            notifyItemChanged(selectedPosition)
            return
        }
        selectedPosition = RecyclerView.NO_POSITION
    }
    interface Listener {
        fun onChooseAlarm(alarm: Alarm)
        fun onToggleSwitch(alarm: Alarm)
        fun onDeleteAlarm(alarm: Alarm)
        fun onToggleCheckBoxes(alarm: Alarm, selectedDays: EnumSet<DaysOfWeek>)
        fun onChooseRingtone(alarm: Alarm)
        fun onToggleVibration(alarm: Alarm)
    }

    class AlarmHolder(
        val binding: ItemAlarmBinding
    ) : RecyclerView.ViewHolder(binding.root)

    override fun onClick(v: View) {
        val alarmPos = v.tag as Int
        val alarm = getItem(alarmPos) ?: return
        when (v.id) {
            R.id.swAlarm -> listener.onToggleSwitch(alarm)
            R.id.ringtoneRow -> listener.onChooseRingtone(alarm)
            R.id.vibrationRow -> listener.onToggleVibration(alarm)
            R.id.deleteRow -> listener.onDeleteAlarm(alarm)
            //CheckBoxes
            //R.id.checkBoxesRow -> listener.onToggleCheckBoxes(alarm, EnumSet.noneOf(DaysOfWeek::class.java))
            R.id.cbCircleCheckbox -> listener.onToggleCheckBoxes(alarm, EnumSet.noneOf(DaysOfWeek::class.java))
//            R.id.cbTuesday -> listener.onToggleCheckBoxes(alarm, EnumSet.noneOf(DaysOfWeek::class.java))
//            R.id.cbWednesday -> listener.onToggleCheckBoxes(alarm, EnumSet.noneOf(DaysOfWeek::class.java))
//            R.id.cbThursday -> listener.onToggleCheckBoxes(alarm, EnumSet.noneOf(DaysOfWeek::class.java))
//            R.id.cbFriday -> listener.onToggleCheckBoxes(alarm, EnumSet.noneOf(DaysOfWeek::class.java))
//            R.id.cbSaturday -> listener.onToggleCheckBoxes(alarm, EnumSet.noneOf(DaysOfWeek::class.java))
//            R.id.cbSunday -> listener.onToggleCheckBoxes(alarm, EnumSet.noneOf(DaysOfWeek::class.java))
            else -> setSelectedPosition(alarmPos)
        }
    }

    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): AlarmHolder {
        val inflater = LayoutInflater.from(parent.context)
        val binding = ItemAlarmBinding.inflate(inflater, parent, false)

        binding.swAlarm.setOnClickListener(this)
        binding.root.setOnClickListener(this)
        binding.ringtoneRow.setOnClickListener(this)
        binding.vibrationRow.setOnClickListener(this)
        binding.deleteRow.setOnClickListener(this)

        //Checkboxes
        //binding.checkBoxesRow.setOnClickListener(this)
        binding.cbMonday.findViewById<CheckBox>(R.id.cbCircleCheckbox).setOnClickListener(this)
        binding.cbTuesday.findViewById<CheckBox>(R.id.cbCircleCheckbox).setOnClickListener(this)
        binding.cbWednesday.findViewById<CheckBox>(R.id.cbCircleCheckbox).setOnClickListener(this)
        binding.cbThursday.findViewById<CheckBox>(R.id.cbCircleCheckbox).setOnClickListener(this)
        binding.cbFriday.findViewById<CheckBox>(R.id.cbCircleCheckbox).setOnClickListener(this)
        binding.cbSaturday.findViewById<CheckBox>(R.id.cbCircleCheckbox).setOnClickListener(this)
        binding.cbSunday.findViewById<CheckBox>(R.id.cbCircleCheckbox).setOnClickListener(this)
        return AlarmHolder(binding)
    }

    override fun onBindViewHolder(holder: AlarmHolder, position: Int) {
        val alarm = getItem(position) ?: return
        with(holder.binding) {
            root.tag = position
            swAlarm.tag = position
            ringtoneRow.tag = position
            vibrationRow.tag = position
            deleteRow.tag = position

            //Checkboxes
            checkBoxesRow.findViewById<CheckBox>(R.id.cbCircleCheckbox).tag = position
            cbMonday.findViewById<CheckBox>(R.id.cbCircleCheckbox).tag = position
            cbTuesday.findViewById<CheckBox>(R.id.cbCircleCheckbox).tag = position
            cbWednesday.findViewById<CheckBox>(R.id.cbCircleCheckbox).tag = position
            cbThursday.findViewById<CheckBox>(R.id.cbCircleCheckbox).tag = position
            cbFriday.findViewById<CheckBox>(R.id.cbCircleCheckbox).tag = position
            cbSaturday.findViewById<CheckBox>(R.id.cbCircleCheckbox).tag = position
            cbSunday.findViewById<CheckBox>(R.id.cbCircleCheckbox).tag = position

            tvAlarmName.text = alarm.name
            tvTime.text = alarm.timeInvoke
            tvInvokeDay.text = alarm.dayInvoke
            swAlarm.isChecked = alarm.isEnable
            if(selectedPosition == position)
                expandableLayout.visibility = View.VISIBLE
            else
                expandableLayout.visibility = View.GONE
        }
    }
    object ItemCallback : DiffUtil.ItemCallback<AlarmUi>() {
        override fun areItemsTheSame(oldItem: AlarmUi, newItem: AlarmUi): Boolean {
            return oldItem.id == newItem.id
        }

        override fun areContentsTheSame(oldItem: AlarmUi, newItem: AlarmUi): Boolean {
            return oldItem == newItem
        }

    }
}