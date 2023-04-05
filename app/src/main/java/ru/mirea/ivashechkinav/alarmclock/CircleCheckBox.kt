package ru.mirea.ivashechkinav.alarmclock

import android.content.Context
import android.util.AttributeSet
import android.widget.CheckBox
import android.widget.FrameLayout
import android.widget.TextView

class CircleCheckBox(context: Context, attrs: AttributeSet): FrameLayout(context, attrs) {

    init {
        inflate(context, R.layout.circle_checkbox_layout, this)

        val textView: TextView = findViewById(R.id.tvCircleCheckbox)
        val attributes = context.obtainStyledAttributes(attrs, R.styleable.CircleCheckBox)
        textView.text = attributes.getString(R.styleable.CircleCheckBox_letter)
        attributes.recycle()
    }
}