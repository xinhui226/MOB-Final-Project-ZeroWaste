package com.xinhui.mobfinalproject.data.repo.notification

import com.google.firebase.firestore.CollectionReference
import com.google.firebase.firestore.FirebaseFirestore
import com.xinhui.mobfinalproject.core.service.AuthService
import com.xinhui.mobfinalproject.data.model.Notification
import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.flow.callbackFlow
import kotlinx.coroutines.tasks.await

class NotificationRepoImpl(
    private val authService: AuthService,
    private val db: FirebaseFirestore = FirebaseFirestore.getInstance(),
): NotificationRepo {

    private fun getDBRef(): CollectionReference {
        return db.collection("notifications")
    }

    override fun getNotifications() = callbackFlow {
        val listener =
            getDBRef()
                .whereEqualTo("ownedBy", authService.getUid())
                .addSnapshotListener { value, error ->
                    if(error != null) {
                        throw error
                    }
                    val notifications = mutableListOf<Notification>()
                    value?.documents?.let { docs ->
                        for (doc in docs){
                            doc.data?.let {
                                it["id"] = doc.id
                                notifications.add(Notification.fromMap(it))
                            }
                        }
                        trySend(notifications)
                    }
                }
        awaitClose{
            listener.remove()
        }
    }

    override suspend fun addNotification(notification: Notification) {
        getDBRef().add(
            Notification(
                title = notification.title,
                notifyDateTime = notification.notifyDateTime,
                expireStatus = notification.expireStatus,
                ownedBy = notification.ownedBy
            ).toHash()
        ).await()
    }

    override suspend fun deleteNotification(id: String) {
        getDBRef().document(id).delete().await()
    }

}