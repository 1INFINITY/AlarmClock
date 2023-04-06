package ru.mirea.ivashechkinav.alarmclock

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
) : PagingDataAdapter<AlarmUi, AlarmPagingAdapter.AlarmHolder>(ItemCallback), View.OnClickListener {

    private var selectedPosition = RecyclerView.NO_POSITION

    private fun setSelectedPosition(position: Int) {
        if(position == selectedPosition) {
            selectedPosition = RecyclerView.NO_POSITION
            notifyItemChanged(position)
            return
        }
        val oldPos = selectedPosition
        selectedPosition = position
        notifyItemChanged(position)
        if(oldPos != RecyclerView.NO_POSITION) notifyItemChanged(oldPos)
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
            R.id.checkBoxesRow -> listener.onToggleCheckBoxes(
                alarm,
                parseCheckBoxesStates(viewGroup = v as ViewGroup)
            )
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
        binding.checkBoxesRow.setOnClickListener(this)
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
            checkBoxesRow.tag = position

            tvAlarmName.text = alarm.name
            tvTime.text = alarm.timeInvoke
            tvInvokeDay.text = alarm.dayInvoke
            swAlarm.isChecked = alarm.isEnable

            // TODO: Need to fix a very strange bug. Sometimes when items change visibility it is not shown on the screen. Need(3 or more items)
            expandableLayout.visibility =
                if (selectedPosition == position)
                    View.VISIBLE
                else
                    View.GONE
        }
    }
    private fun parseCheckBoxesStates(viewGroup: ViewGroup): EnumSet<DaysOfWeek> {
        val set = EnumSet.noneOf(DaysOfWeek::class.java)
        if(viewGroup.findViewById<CheckBox>(R.id.cbMonday).isChecked) set.add(DaysOfWeek.MONDAY)
        if(viewGroup.findViewById<CheckBox>(R.id.cbTuesday).isChecked) set.add(DaysOfWeek.TUESDAY)
        if(viewGroup.findViewById<CheckBox>(R.id.cbWednesday).isChecked) set.add(DaysOfWeek.WEDNESDAY)
        if(viewGroup.findViewById<CheckBox>(R.id.cbThursday).isChecked) set.add(DaysOfWeek.THURSDAY)
        if(viewGroup.findViewById<CheckBox>(R.id.cbFriday).isChecked) set.add(DaysOfWeek.FRIDAY)
        if(viewGroup.findViewById<CheckBox>(R.id.cbSaturday).isChecked) set.add(DaysOfWeek.SATURDAY)
        if(viewGroup.findViewById<CheckBox>(R.id.cbSunday).isChecked) set.add(DaysOfWeek.SUNDAY)
        return set
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