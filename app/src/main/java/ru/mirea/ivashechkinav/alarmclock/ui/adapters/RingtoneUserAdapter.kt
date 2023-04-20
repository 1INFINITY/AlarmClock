package ru.mirea.ivashechkinav.alarmclock.ui.adapters

import android.annotation.SuppressLint
import android.content.Context
import android.graphics.drawable.Drawable
import android.net.Uri
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.BaseAdapter
import androidx.core.content.ContextCompat
import ru.mirea.ivashechkinav.alarmclock.R
import ru.mirea.ivashechkinav.alarmclock.databinding.ItemRingtoneBinding
import ru.mirea.ivashechkinav.alarmclock.ui.models.RingtoneUi

class RingtoneUserAdapter(
    private val userItems: List<RingtoneUi>,
    private val context: Context,
    private val listener: Listener,
    private val selectedUri: Uri
) : BaseAdapter(), View.OnClickListener {
    private var items: List<RingtoneUi>
    private val inflater = LayoutInflater.from(context)
    private lateinit var binding: ItemRingtoneBinding

    override fun onClick(v: View) {
        val ringtoneIndex = v.tag as Int
        if (ringtoneIndex == 0) {
            listener.onAddRingtone()
            return
        }
        val ringtoneUi = items[ringtoneIndex]
        listener.onChooseRingtone(ringtoneUi)
    }

    interface Listener {
        fun onChooseRingtone(ringtoneUi: RingtoneUi)
        fun onAddRingtone()
        fun onDeleteRingtone()
    }

    init {
        items =
            mutableListOf(
                RingtoneUi(
                    name = "Добавить",
                    uri = null,
                    icon = ContextCompat.getDrawable(context, R.drawable.plus)
                )
            ).apply {
                this.addAll(userItems)
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
        binding.ivSelectedIcon.visibility = if(item.uri == selectedUri) View.VISIBLE else View.INVISIBLE
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