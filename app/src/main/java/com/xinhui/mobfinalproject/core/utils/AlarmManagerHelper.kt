package com.xinhui.mobfinalproject.core.utils

import android.app.AlarmManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import androidx.core.content.ContextCompat
import com.xinhui.mobfinalproject.R
import com.xinhui.mobfinalproject.core.receiver.NotifyBroadcastReceiver
import java.security.MessageDigest
import java.text.SimpleDateFormat
import java.time.LocalDate
import java.time.format.DateTimeFormatter
import java.time.temporal.ChronoUnit
import java.util.Date
import java.util.Locale

object AlarmManagerHelper {
    private const val ACTION_SHOW_NOTIFICATION = "com.xinhui.mobfinalproject.ACTION_SHOW_NOTIFICATION_EXPIRED"
    val usedRequestCodes = mutableSetOf<Int>()
    val itemIdToRequestCodeMap = mutableMapOf<String, Int>()

    private fun generateHashCode(itemId: String): Int {
        val bytes = itemId.toByteArray()
        val md = MessageDigest.getInstance("SHA-256")
        val digest = md.digest(bytes)
        var code = digest.fold(0) { acc, byte -> (acc shl 8) or (byte.toInt() and 0xFF) }
        var retries = 0

        while (usedRequestCodes.contains(code) && retries < 5) {
            code = digest.fold(0) { acc, byte -> (acc shl 8) or (byte.toInt() and 0xFF) }
            retries++
        }

        return code
    }

    private fun setAlarm(context: Context, itemId: String, time: Long, status: String) {
        val requestCode = generateHashCode("$itemId$status")

        usedRequestCodes.add(requestCode)
        itemIdToRequestCodeMap["$itemId$status"] = requestCode

        val alarmManager = context.getSystemService(Context.ALARM_SERVICE) as AlarmManager
        val intent = Intent(context, NotifyBroadcastReceiver::class.java)
        intent.action = ACTION_SHOW_NOTIFICATION
        intent.putExtra(Constants.intentId, itemId)
        intent.putExtra(Constants.intentStatus, status)

        val pendingIntent = PendingIntent.getBroadcast(context, requestCode, intent, PendingIntent.FLAG_IMMUTABLE)

        alarmManager.set(
            AlarmManager.RTC_WAKEUP,
            time,
            pendingIntent
        )
    }

    fun setAlarms(context: Context, itemId: String, expiryDate: String) {
        val sdf = SimpleDateFormat(Constants.dateFormat, Locale.getDefault())
        if (sdf.isLenient) {
            val formatter = DateTimeFormatter.ofPattern(Constants.dateFormat)
            val expiryLocalDate = LocalDate.parse(expiryDate, formatter)
            val today = LocalDate.now()
            val daysUntilExpired = ChronoUnit.DAYS.between(today, expiryLocalDate)

            val date: Date = sdf.parse(expiryDate)!!

            if (daysUntilExpired > 1) {
                val first = date.time - 2 * 24 * 60 * 60 * 1000
                setAlarm(
                    context,
                    itemId,
                    first,
                    ContextCompat.getString(context, R.string.expiring_in_2_days)
                )
            }
            val sec = date.time - 24 * 60 * 60 * 1000
            setAlarm(context, itemId, sec, ContextCompat.getString(context, R.string.expiring_in_1_day))
            setAlarm(context, itemId, date.time, ContextCompat.getString(context, R.string.expired))
        } else {
            throw Exception("Invalid date format to set an alarm")
        }
    }

    fun cancelAlarms(context: Context, id:String) {
        cancelAlarm(context, "$id${ContextCompat.getString(context, R.string.expiring_in_2_days)}")
        cancelAlarm(context, "$id${ContextCompat.getString(context, R.string.expiring_in_1_day)}")
        cancelAlarm(context, "$id${ContextCompat.getString(context, R.string.expired)}")
    }

    private fun cancelAlarm(context: Context, idStatus: String) {
        itemIdToRequestCodeMap[idStatus]?.let { requestCode ->
            usedRequestCodes.remove(requestCode)
            itemIdToRequestCodeMap.remove(idStatus)

            val alarmManager = context.getSystemService(Context.ALARM_SERVICE) as AlarmManager
            val intent = Intent(context, NotifyBroadcastReceiver::class.java)
            intent.action = ACTION_SHOW_NOTIFICATION

            val pendingIntent = PendingIntent.getBroadcast(
                context,
                requestCode,
                intent,
                PendingIntent.FLAG_NO_CREATE or PendingIntent.FLAG_IMMUTABLE
            )

            if (pendingIntent != null) { alarmManager.cancel(pendingIntent) }
        }
    }
}