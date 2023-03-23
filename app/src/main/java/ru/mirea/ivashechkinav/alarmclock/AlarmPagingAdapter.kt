package ru.mirea.ivashechkinav.alarmclock

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.paging.PagingDataAdapter
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import ru.mirea.ivashechkinav.alarmclock.databinding.ItemAlarmBinding
import ru.mirea.ivashechkinav.alarmclock.domain.Alarm

class AlarmPagingAdapter(
    private val listener: Listener
): PagingDataAdapter<AlarmUi, AlarmPagingAdapter.AlarmHolder>(ItemCallback), View.OnClickListener {


    interface Listener {
        fun onChooseAlarm(alarm: Alarm)
        fun onToggleSwitch(alarm: Alarm)
        fun onDeleteAlarm(alarm: Alarm)
    }

    class AlarmHolder(
        val binding: ItemAlarmBinding
    ) : RecyclerView.ViewHolder(binding.root)

    override fun onClick(v: View) {
        val alarm = v.tag as Alarm
        when (v.id) {
            R.id.swAlarm -> listener.onToggleSwitch(alarm)
            else -> listener.onChooseAlarm(alarm)
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

        return AlarmHolder(binding)
    }

    override fun onBindViewHolder(holder: AlarmHolder, position: Int) {
        val alarm = getItem(position) ?: return
        with(holder.binding) {
            root.tag = alarm
            swAlarm.tag = alarm

            tvAlarmName.text = alarm.name
            tvTime.text = alarm.timeInvoke
            tvInvokeDay.text = alarm.dayInvoke
            swAlarm.isChecked = alarm.isEnable
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