package com.xinhui.mobfinalproject.core.utils

import android.app.AlertDialog
import android.app.DatePickerDialog
import android.content.Context
import android.widget.TextView
import java.util.Calendar
import android.view.LayoutInflater
import com.google.android.material.button.MaterialButton
import com.google.android.material.textfield.TextInputEditText
import com.xinhui.mobfinalproject.R
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

object ShowDialog {
    fun showDatePicker(context: Context, tvDate: TextView, selectedDate: String? = null) {
        val calendar = Calendar.getInstance()

        if (selectedDate != null) {
            calendar.time = SimpleDateFormat("dd/MM/yyyy", Locale.getDefault()).parse(selectedDate) as Date
        }

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
        datePickerDialog.datePicker.minDate = System.currentTimeMillis() + 24 * 60 * 60 * 1000L
        datePickerDialog.datePicker.maxDate =
            System.currentTimeMillis() + 6 * 30 * 24 * 60 * 60 * 1000L
        datePickerDialog.show()
    }

        fun showForgetEmailDialog(context: Context, layoutInflater: LayoutInflater, sendEmail:(String) -> Unit) {
        val dialog = AlertDialog.Builder(context).create()
        val view = layoutInflater.inflate(R.layout.dialog_forget_password, null)
        dialog.setView(view)
        val etEmail = view.findViewById<TextInputEditText>(R.id.etEmail)

        val btnSend = view.findViewById<MaterialButton>(R.id.btnSend)
        btnSend.setOnClickListener {
            sendEmail(etEmail.text.toString())
            dialog.dismiss()
        }

        val btnCancel = view.findViewById<MaterialButton>(R.id.btnCancel)
        btnCancel.setOnClickListener { dialog.dismiss() }

        dialog.show()
    }

    fun showEmailVerificationDialog(context: Context, layoutInflater: LayoutInflater) {
        val dialog = AlertDialog.Builder(context).create()
        val view = layoutInflater.inflate(R.layout.dialog_email_verify, null)
        dialog.setView(view)
        dialog.setCanceledOnTouchOutside(false)

        val btnClose = view.findViewById<MaterialButton>(R.id.btnClose)
        btnClose.setOnClickListener { dialog.dismiss() }

        dialog.show()
    }

    fun showDltConfirmationDialog(context: Context, layoutInflater: LayoutInflater, confirmDlt:()->Unit) {
        val dialog = AlertDialog.Builder(context).create()
        val view = layoutInflater.inflate(R.layout.dialog_dlt_confirmation, null)
        dialog.setView(view)

        val btnClose = view.findViewById<MaterialButton>(R.id.btnClose)
        btnClose.setOnClickListener { dialog.dismiss() }
        val btnConfirm = view.findViewById<MaterialButton>(R.id.btnConfirm)
        btnConfirm.setOnClickListener {
            confirmDlt()
            dialog.dismiss()
        }

        dialog.show()
    }
}