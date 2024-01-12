package com.xinhui.mobfinalproject.data.repo.notification

import com.xinhui.mobfinalproject.data.model.Notification
import kotlinx.coroutines.flow.Flow

interface NotificationRepo {
    fun getNotifications(): Flow<List<Notification>>
    suspend fun addNotification(notification: Notification)
    suspend fun deleteNotification(id: String)
}