package com.xinhui.mobfinalproject.core.utils

import android.content.Context
import android.view.LayoutInflater
import androidx.appcompat.app.AlertDialog
import com.google.android.material.button.MaterialButton
import com.google.android.material.textfield.TextInputEditText
import com.xinhui.mobfinalproject.R

object AlertDialog {
    fun showForgetEmailDialog(context: Context, layoutInflater: LayoutInflater, sendEmail:(String) -> Unit) {
        val dialog = AlertDialog.Builder(context).create()
        val view = layoutInflater.inflate(R.layout.dialog_forget_password, null)
        dialog.setView(view)
        val etEmail = view.findViewById<TextInputEditText>(R.id.etEmail)

        val btnSend = view.findViewById<MaterialButton>(R.id.btnSend)
        btnSend.setOnClickListener {
            // TODO: send email
            sendEmail(etEmail.text.toString())
            dialog.dismiss()
        }

        val btnCancel = view.findViewById<MaterialButton>(R.id.btnCancel)
        btnCancel.setOnClickListener { dialog.dismiss() }

        dialog.show()
    }
}