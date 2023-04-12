package ru.mirea.ivashechkinav.alarmclock.ui.adapters

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.BaseAdapter
import androidx.core.content.ContextCompat
import ru.mirea.ivashechkinav.alarmclock.R
import ru.mirea.ivashechkinav.alarmclock.databinding.ItemRingtoneBinding
import ru.mirea.ivashechkinav.alarmclock.ui.models.RingtoneUi

class RingtoneDeviceAdapter(private val deviceItems: List<RingtoneUi>, private val context: Context) : BaseAdapter() {
    private var items: List<RingtoneUi>
    private val inflater = LayoutInflater.from(context)
    private lateinit var binding: ItemRingtoneBinding

    init {
        items =
            mutableListOf(
                RingtoneUi(
                    name = "Без звука",
                    uri = null,
                    icon =  ContextCompat.getDrawable(context, R.drawable.checked_circle)
                ),
                RingtoneUi(
                    name = "По умолчанию",
                    uri = null,
                    icon =  ContextCompat.getDrawable(context, R.drawable.checked_circle)
                )
            ).apply {
                this.addAll(deviceItems)
            }
    }
    override fun getView(position: Int, convertView: View?, parent: ViewGroup?): View {
        if (convertView == null) {
            binding = ItemRingtoneBinding.inflate(inflater, parent, false)
            binding.root.tag = binding
        } else {
            binding = convertView.tag as ItemRingtoneBinding
        }

        val item = items[position]
        binding.tvRingtoneName.text = item.name
        item.icon?.let {
            binding.ivRingtoneIcon.setImageDrawable(it)
        }

        return binding.root
    }

    override fun getItem(position: Int): Any {
        return items[position]
    }

    override fun getItemId(position: Int): Long {
        return position.toLong()
    }

    override fun getCount(): Int {
        return items.size
    }
}