package ru.mirea.ivashechkinav.alarmclock.ui.fragments

import android.database.Cursor
import android.media.RingtoneManager
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.net.toUri
import androidx.fragment.app.Fragment
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import dagger.hilt.android.AndroidEntryPoint
import ru.mirea.ivashechkinav.alarmclock.data.repository.AlarmRepositoryImpl
import ru.mirea.ivashechkinav.alarmclock.databinding.FragmentRingtonePickerBinding
import ru.mirea.ivashechkinav.alarmclock.domain.usecase.UseCaseSuspend
import ru.mirea.ivashechkinav.alarmclock.domain.usecase.setRingtoneOnAlarm.SetRingtoneOnAlarmArgs
import ru.mirea.ivashechkinav.alarmclock.domain.usecase.setRingtoneOnAlarm.SetRingtoneOnAlarmUseCase
import ru.mirea.ivashechkinav.alarmclock.ui.adapters.RingtoneDeviceAdapter
import ru.mirea.ivashechkinav.alarmclock.ui.adapters.RingtoneUserAdapter
import ru.mirea.ivashechkinav.alarmclock.ui.models.RingtoneUi
import javax.inject.Inject


@AndroidEntryPoint
class RingtonePickerFragment : Fragment() {

    @Inject
    lateinit var repositoryImpl: AlarmRepositoryImpl

    @Inject
    lateinit var setRingtoneOnAlarmUseCaseSuspend: SetRingtoneOnAlarmUseCase

    lateinit var binding: FragmentRingtonePickerBinding
    private val args: RingtonePickerFragmentArgs by navArgs()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentRingtonePickerBinding.inflate(inflater, container, false)

        lifecycleScope.launchWhenStarted {
            initDeviceList()
            initUserList()
        }

        return binding.root
    }

    private fun chooseRingtone(ringtoneUi: RingtoneUi) {
        val args = SetRingtoneOnAlarmArgs(
            alarmId = args.alarmId,
            uri = ringtoneUi.uri!!
        )
        lifecycleScope.launchWhenStarted {
            setRingtoneOnAlarmUseCaseSuspend.execute(args)
            findNavController().popBackStack()
        }
    }

    private suspend fun initDeviceList() {
        val alarm = repositoryImpl.getAlarmById(args.alarmId)
        val deviceRingtones: List<RingtoneUi> = getRingtones().map { (title, link) ->
            RingtoneUi(name = title, uri = link.toUri())
        }

        val adapter = RingtoneDeviceAdapter(
            deviceRingtones,
            requireContext(),
            object : RingtoneDeviceAdapter.Listener {
                override fun onChooseRingtone(ringtoneUi: RingtoneUi) {
                    chooseRingtone(ringtoneUi)
                }
            },
            alarm.alarmSoundUri)

        binding.listViewDeviceRingtones.adapter = adapter
    }

    private suspend fun initUserList() {
        val alarm = repositoryImpl.getAlarmById(args.alarmId)
        val adapter = RingtoneUserAdapter(
            listOf(),
            requireContext(),
            object : RingtoneUserAdapter.Listener {
                override fun onChooseRingtone(ringtoneUi: RingtoneUi) {
                    chooseRingtone(ringtoneUi)
                }

                override fun onAddRingtone() {
                    TODO("Not yet implemented")
                }

                override fun onDeleteRingtone() {
                    TODO("Not yet implemented")
                }
            },
            alarm.alarmSoundUri)
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