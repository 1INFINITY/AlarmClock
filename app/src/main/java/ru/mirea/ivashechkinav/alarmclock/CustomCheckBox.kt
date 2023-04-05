package ru.mirea.ivashechkinav.alarmclock

import android.annotation.SuppressLint
import android.content.Context
import android.graphics.Canvas
import android.graphics.Paint
import android.util.AttributeSet
import android.view.MotionEvent

class CustomCheckBox(context: Context, attrs: AttributeSet): androidx.appcompat.widget.AppCompatCheckBox(context, attrs) {

    @SuppressLint("ClickableViewAccessibility")
    override fun onTouchEvent(event: MotionEvent?): Boolean {
        event?.let {
            if (it.action == MotionEvent.ACTION_DOWN)
                performClick()
        }
        return false
    }
}