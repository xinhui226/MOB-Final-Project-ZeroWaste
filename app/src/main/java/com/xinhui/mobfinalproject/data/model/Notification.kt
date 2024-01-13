package com.xinhui.mobfinalproject.data.model

data class Notification(
    val id: String? = null,
    val title: String,
    val expireStatus: String,
    val notifyDateTime: String,
    val ownedBy: String
){
    fun toHash(): Map<String, Any?> {
        return hashMapOf(
            "title" to title,
            "expireStatus" to expireStatus,
            "notifyDateTime" to notifyDateTime,
            "ownedBy" to ownedBy
        )
    }

    companion object {
        fun fromMap(hash: Map<String, Any?>): Notification {
            return Notification(
                id = hash["id"].toString(),
                title = hash["title"].toString(),
                notifyDateTime = hash["notifyDateTime"].toString(),
                expireStatus = hash["expireStatus"].toString(),
                ownedBy = hash["ownedBy"].toString()
            )
        }
    }
}