package ru.mirea.ivashechkinav.alarmclock.domain.usecase

import android.graphics.Canvas
import android.graphics.Color
import android.graphics.Paint
import android.text.SpannableString
import android.text.Spanned
import android.text.style.ReplacementSpan
import ru.mirea.ivashechkinav.alarmclock.domain.DaysOfWeek
import ru.mirea.ivashechkinav.alarmclock.domain.DaysOfWeek.Companion.toInt
import java.util.*
import javax.inject.Inject
import javax.inject.Singleton

class GetSpannableDaysStringUseCase @Inject constructor() :
    UseCase<EnumSet<DaysOfWeek>, SpannableString?> {
    override fun execute(arg: EnumSet<DaysOfWeek>): SpannableString? {
        if (arg.size == daysInWeek)
            return SpannableString(everyday)
        if (arg.isNotEmpty())
            return getSpannableString(arg)

        return null
    }

    private fun getSpannableString(set: EnumSet<DaysOfWeek>): SpannableString {
        val spannableString = SpannableString(daysString)
        val daysIndexes = set.map { (it.toInt() + 5) % 7 }
        val span = DotSpan(5f, Color.GREEN, daysIndexes)
        spannableString.setSpan(
            span,
            0,
            spannableString.length - 1,
            Spanned.SPAN_INCLUSIVE_INCLUSIVE
        )
        return spannableString
    }

    companion object {
        private const val daysString = "П В С Ч П С В "
        private const val everyday = "Ежедневно"
        private const val daysInWeek = 7

        private class DotSpan(
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
}