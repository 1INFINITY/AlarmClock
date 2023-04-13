package ru.mirea.ivashechkinav.alarmclock.ui.adapters

import android.annotation.SuppressLint
import android.content.Context
import android.media.RingtoneManager
import android.net.Uri
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.BaseAdapter
import androidx.core.content.ContextCompat
import ru.mirea.ivashechkinav.alarmclock.R
import ru.mirea.ivashechkinav.alarmclock.databinding.ItemRingtoneBinding
import ru.mirea.ivashechkinav.alarmclock.ui.models.RingtoneUi


class RingtoneDeviceAdapter(private val deviceItems: List<RingtoneUi>, private val context: Context, private val listener: Listener) : BaseAdapter(), View.OnClickListener {
    private var items: List<RingtoneUi>
    private val inflater = LayoutInflater.from(context)
    private lateinit var binding: ItemRingtoneBinding

    override fun onClick(v: View) {
        val ringtoneIndex = v.tag as Int
        val ringtoneUi = items[ringtoneIndex]
        listener.onChooseRingtone(ringtoneUi)
    }

    interface Listener {
        fun onChooseRingtone(ringtoneUi: RingtoneUi)
    }

    init {
        val defaultRingtoneUri: Uri = RingtoneManager.getActualDefaultRingtoneUri(
            context,
            RingtoneManager.TYPE_RINGTONE
        )
        val defaultRingtone = RingtoneManager.getRingtone(context, defaultRingtoneUri)
        items =
            mutableListOf(
                RingtoneUi(
                    name = "Без звука",
                    uri = null,
                    icon =  ContextCompat.getDrawable(context, R.drawable.checked_circle)
                ),
                RingtoneUi(
                    name = "По умолчанию (${defaultRingtone.getTitle(context)})",
                    uri = defaultRingtoneUri,
                    icon =  ContextCompat.getDrawable(context, R.drawable.checked_circle)
                )
            ).apply {
                this.addAll(deviceItems)
            }
    }
    @SuppressLint("ViewHolder")
    override fun getView(position: Int, convertView: View?, parent: ViewGroup?): View {
        binding = ItemRingtoneBinding.inflate(inflater, parent, false)
        binding.root.tag = position
        binding.root.setOnClickListener(this)

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