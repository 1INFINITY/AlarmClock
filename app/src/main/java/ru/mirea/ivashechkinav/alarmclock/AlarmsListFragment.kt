package ru.mirea.ivashechkinav.alarmclock

import android.graphics.Canvas
import android.graphics.Color
import android.graphics.Paint
import android.os.Bundle
import android.text.SpannableString
import android.text.Spanned
import android.text.style.ReplacementSpan
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import androidx.paging.map
import androidx.recyclerview.widget.LinearLayoutManager
import dagger.hilt.android.AndroidEntryPoint
import ru.mirea.ivashechkinav.alarmclock.data.repository.AlarmRepositoryImpl
import ru.mirea.ivashechkinav.alarmclock.databinding.FragmentAlarmsListBinding
import ru.mirea.ivashechkinav.alarmclock.domain.Alarm
import ru.mirea.ivashechkinav.alarmclock.domain.DaysOfWeek
import ru.mirea.ivashechkinav.alarmclock.domain.DaysOfWeek.Companion.toInt
import java.util.*
import javax.inject.Inject

@AndroidEntryPoint
class AlarmsListFragment : Fragment() {

    @Inject
    lateinit var repositoryImpl: AlarmRepositoryImpl

    @Inject
    lateinit var alarmServiceImpl: AlarmServiceImpl

    lateinit var binding: FragmentAlarmsListBinding
    lateinit var adapter: AlarmPagingAdapter
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentAlarmsListBinding.inflate(inflater, container, false)

        initButtons()
        initRecyclerView()
        setSpan(DaysOfWeek.fromByte(0b00100110))
        return binding.root
    }

    private fun initButtons() {
        binding.btnAdd.setOnClickListener {
            findNavController().navigate(R.id.action_alarmsListFragment_to_timePickerFragment)
        }
    }

    private fun initRecyclerView() {
        adapter = AlarmPagingAdapter(object : AlarmPagingAdapter.Listener {
            override fun onChooseAlarm(alarm: Alarm) {
                findNavController().navigate(R.id.action_alarmsListFragment_to_timePickerFragment)
            }

            override fun onToggleSwitch(alarm: Alarm) {
                lifecycleScope.launchWhenStarted {
                    repositoryImpl.updateAlarm((alarm as AlarmUi).copy(isEnable = !alarm.isEnable))
                }
            }

            override fun onDeleteAlarm(alarm: Alarm) {
                lifecycleScope.launchWhenStarted {
                    repositoryImpl.deleteAlarm(alarm)
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

    private fun setSpan(set: EnumSet<DaysOfWeek>) {
        val spannableString = SpannableString("П В С Ч П С В ")
        val daysIndexes = set.map { (it.toInt() + 5) % 7 }
        val span = DotSpan(5f, Color.GREEN, daysIndexes)
        spannableString.setSpan(
            span,
            0,
            spannableString.length - 1,
            Spanned.SPAN_INCLUSIVE_INCLUSIVE
        )
        binding.textView.text = spannableString
    }

    class DotSpan(
        private val radius: Float,
        private val color: Int,
        private val daysIndexes: List<Int>
    ) : ReplacementSpan() {

        override fun getSize(
            paint: Paint,
            text: CharSequence,
            start: Int,
            end: Int,
            fm: Paint.FontMetricsInt?
        ): Int {
            return paint.measureText(text, start, end).toInt()
        }

        override fun draw(
            canvas: Canvas,
            text: CharSequence,
            start: Int,
            end: Int,
            x: Float,
            top: Int,
            y: Int,
            bottom: Int,
            paint: Paint
        ) {
            // сохраняем текущий цвет и стиль текста
            val oldColor = paint.color
            val oldStyle = paint.style

            // устанавливаем цвет и стиль для рисования точек
            paint.color = color
            paint.style = Paint.Style.FILL

            // рисуем точки над каждым символом текста
            for (i in start until end) {
                // находим координаты центра текущего символа
                val xPos = x + paint.measureText(
                    text.substring(
                        start,
                        i
                    )
                ) + paint.measureText(text.substring(i, i + 1)) / 2
                val yPos = top - radius

                paint.color = Color.GRAY
                // рисуем круг
                if (!Character.isWhitespace(text[i]) && daysIndexes.contains(i / 2)) {
                    paint.color = color
                    canvas.drawCircle(xPos, yPos, radius, paint)
                }


                // рисуем символы
                canvas.drawText(
                    text,
                    i,
                    i + 1,
                    xPos - paint.measureText(text.substring(i, i + 1)) / 2,
                    y.toFloat(),
                    paint
                )
            }

            // восстанавливаем цвет и стиль текста
            paint.color = oldColor
            paint.style = oldStyle
        }
    }

}