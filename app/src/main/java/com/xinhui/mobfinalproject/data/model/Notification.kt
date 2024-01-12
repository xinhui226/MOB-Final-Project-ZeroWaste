package com.xinhui.mobfinalproject.data.model

data class Notification(
    val id: String? = null,
    val productName: String,
    val expireStatus: String,
    val notifyDateTime: String,
    val ownedBy: String? = null
){
    fun toHash(): Map<String, Any?> {
        return hashMapOf(
            "productName" to productName,
            "expireStatus" to expireStatus,
            "notifyDateTime" to notifyDateTime,
            "ownedBy" to ownedBy
        )
    }

    companion object {
        fun fromMap(hash: Map<String, Any?>): Notification {
            return Notification(
                id = hash["id"].toString(),
                productName = hash["productName"].toString(),
                notifyDateTime = hash["notifyDateTime"].toString(),
                expireStatus = hash["expireStatus"].toString(),
                ownedBy = hash["ownedBy"].toString()
            )
        }
    }
}