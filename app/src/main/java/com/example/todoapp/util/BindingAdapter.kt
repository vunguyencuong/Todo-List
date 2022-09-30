package com.example.todoapp.util

import android.graphics.Color
import android.graphics.Paint
import android.widget.TextView
import androidx.databinding.BindingAdapter
import java.security.Timestamp
import java.text.DateFormat

@BindingAdapter("setPriority")

fun setPriority(view: TextView, priority: Int) {
    when(priority){
        0 -> {
            view.text = "High Priority"
            view.setTextColor(Color.RED)
        }
        1 -> {
            view.text = "Medium Priority"
            view.setTextColor(Color.BLUE)
        }
        else -> {
            view.text = "Low Priority"
            view.setTextColor(Color.DKGRAY)
        }
    }
}

@BindingAdapter("setTimestamp")
fun setTimestamp(view: TextView, timestamp: Long){
    view.text = DateFormat.getInstance().format(timestamp)
}

@BindingAdapter("strikeThrough")
fun TextView.strikeThrough(strikeThrough: Boolean) {
    if(strikeThrough) this.paintFlags = Paint.STRIKE_THRU_TEXT_FLAG
    else this.paintFlags = 0
}