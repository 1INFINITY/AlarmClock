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

    private var selectedPosition = RecyclerView.NO_POSITION
    private fun setSelectedPosition(position: Int) {
        if (selectedPosition != position) {
            notifyItemChanged(selectedPosition)
            selectedPosition = position
            notifyItemChanged(selectedPosition)
        }
    }
    interface Listener {
        fun onChooseAlarm(alarm: Alarm)
        fun onToggleSwitch(alarm: Alarm)
        fun onDeleteAlarm(alarm: Alarm)
    }

    class AlarmHolder(
        val binding: ItemAlarmBinding
    ) : RecyclerView.ViewHolder(binding.root)

    override fun onClick(v: View) {
        val alarmPos = v.tag as Int
        val alarm = getItem(alarmPos) ?: return
        when (v.id) {
            R.id.swAlarm -> listener.onToggleSwitch(alarm)
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

        return AlarmHolder(binding)
    }

    override fun onBindViewHolder(holder: AlarmHolder, position: Int) {
        val alarm = getItem(position) ?: return
        with(holder.binding) {
            root.tag = position
            swAlarm.tag = position

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