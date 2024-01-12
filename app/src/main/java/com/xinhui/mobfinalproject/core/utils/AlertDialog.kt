package com.xinhui.mobfinalproject.core.utils

import android.app.DatePickerDialog
import android.content.Context
import android.widget.TextView
import java.util.Calendar

object AlertDialog {
    fun showDatePicker(context: Context, tvDate: TextView) {
        val calendar = Calendar.getInstance()
        val year = calendar.get(Calendar.YEAR)
        val month = calendar.get(Calendar.MONTH)
        val dayOfMonth = calendar.get(Calendar.DAY_OF_MONTH)

        val datePickerDialog = DatePickerDialog(
            context,
            { _, selectedYear, selectedMonth, selectedDay ->
                tvDate.text =
                    String.format("%02d/%02d/%04d", selectedDay, selectedMonth + 1, selectedYear)
            },
            year, month, dayOfMonth
        )
        datePickerDialog.datePicker.minDate = System.currentTimeMillis() - 1000
        datePickerDialog.datePicker.maxDate = System.currentTimeMillis() + 6 * 30 * 24 * 60 * 60 * 1000L
        datePickerDialog.show()
    }
}