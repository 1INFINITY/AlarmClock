package ru.mirea.ivashechkinav.alarmclock.ui.fragments

import android.database.Cursor
import android.media.RingtoneManager
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.net.toUri
import androidx.fragment.app.Fragment
import dagger.hilt.android.AndroidEntryPoint
import ru.mirea.ivashechkinav.alarmclock.databinding.FragmentRingtonePickerBinding
import ru.mirea.ivashechkinav.alarmclock.ui.adapters.RingtoneDeviceAdapter
import ru.mirea.ivashechkinav.alarmclock.ui.adapters.RingtoneUserAdapter
import ru.mirea.ivashechkinav.alarmclock.ui.models.RingtoneUi


@AndroidEntryPoint
class RingtonePickerFragment : Fragment() {

    lateinit var binding: FragmentRingtonePickerBinding

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentRingtonePickerBinding.inflate(inflater, container, false)

        initDeviceList()
        initUserList()

        return binding.root
    }

    private fun initDeviceList() {
        val deviceRingtones: List<RingtoneUi> = getRingtones().map { (title, link) ->
            RingtoneUi(name = title, uri = link.toUri())
        }
        val adapter = RingtoneDeviceAdapter(
            deviceRingtones,
            requireContext(),
            object : RingtoneDeviceAdapter.Listener {
                override fun onChooseRingtone(ringtoneUi: RingtoneUi) {
                    TODO("Not yet implemented")
                }
            })
        binding.listViewDeviceRingtones.adapter = adapter
    }

    private fun initUserList() {
        val adapter = RingtoneUserAdapter(
            listOf(),
            requireContext(),
            object : RingtoneUserAdapter.Listener {
                override fun onChooseRingtone(ringtoneUi: RingtoneUi) {
                    TODO("Not yet implemented")
                }

                override fun onAddRingtone() {
                    TODO("Not yet implemented")
                }

                override fun onDeleteRingtone() {
                    TODO("Not yet implemented")
                }
            })
        binding.listViewUserRingtones.adapter = adapter
    }

    private fun getRingtones(): Map<String, String> {
        val manager = RingtoneManager(requireActivity())
        manager.setType(RingtoneManager.TYPE_RINGTONE)
        val cursor: Cursor = manager.cursor
        val list: MutableMap<String, String> = HashMap()
        while (cursor.moveToNext()) {
            val notificationTitle: String = cursor.getString(RingtoneManager.TITLE_COLUMN_INDEX)
            val notificationUri: String =
                cursor.getString(RingtoneManager.URI_COLUMN_INDEX) + "/" + cursor.getString(
                    RingtoneManager.ID_COLUMN_INDEX
                )
            list[notificationTitle] = notificationUri
        }
        return list
    }
}