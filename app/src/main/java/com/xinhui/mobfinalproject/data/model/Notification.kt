package com.xinhui.mobfinalproject.data.model

import com.google.firebase.Timestamp
import com.xinhui.mobfinalproject.core.utils.Constants
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

data class Notification(
    val id: String? = null,
    val title: String,
    val expireStatus: String,
    val notifyDateTime: String,
    val ownedBy: String
){
    fun toHash(): Map<String, Any?> {
        val date: Date = SimpleDateFormat(Constants.dateTimeFormat, Locale.getDefault())
            .parse(notifyDateTime) ?: throw Exception("Invalid date time to parse")
        return hashMapOf(
            "title" to title,
            "expireStatus" to expireStatus,
            "notifyDateTime" to Timestamp(date),
            "ownedBy" to ownedBy
        )
    }

    companion object {
        fun fromMap(hash: Map<String, Any?>): Notification {
            return Notification(
                id = hash["id"].toString(),
                title = hash["title"].toString(),
                notifyDateTime = SimpleDateFormat(Constants.dateTimeFormat, Locale.getDefault())
                    .format((hash["notifyDateTime"] as Timestamp).toDate())
                    .toString(),
                expireStatus = hash["expireStatus"].toString(),
                ownedBy = hash["ownedBy"].toString()
            )
        }
    }
}