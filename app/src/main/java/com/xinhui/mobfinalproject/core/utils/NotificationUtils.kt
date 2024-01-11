package com.xinhui.mobfinalproject.core.utils

import android.Manifest
import android.app.Notification
import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import androidx.core.app.ActivityCompat
import androidx.core.app.NotificationCompat
import androidx.core.app.NotificationManagerCompat
import androidx.core.app.TaskStackBuilder
import com.xinhui.mobfinalproject.R
import com.xinhui.mobfinalproject.ui.MainActivity

object NotificationUtil {
    fun createNotificationChannel(context: Context, notificationName: String, notificationChannelID: String){
        val channel = NotificationChannel(
            notificationChannelID,
            notificationName,
            NotificationManager.IMPORTANCE_HIGH
        )
        val notificationManager = context.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
        notificationManager.createNotificationChannel(channel)
    }

    fun createNotification(context: Context,
                           notificationTitle:String,
                           notificationText:String
    ): NotificationCompat.Builder{
        val resultIntent = Intent(context, MainActivity::class.java)

        val resultPendingIntent = TaskStackBuilder.create(context).run {
            addNextIntentWithParentStack(resultIntent)
            getPendingIntent(
                0,
                PendingIntent.FLAG_IMMUTABLE or PendingIntent.FLAG_UPDATE_CURRENT
            )
        }

        return NotificationCompat.Builder(context, Constants.expiryNotificationChannelId)
            .setSmallIcon(R.drawable.full)
            .setContentTitle(notificationTitle)
            .setContentText(notificationText)
            .setContentIntent(resultPendingIntent)
            .setPriority(NotificationCompat.PRIORITY_HIGH)
            .setAutoCancel(true)
    }

    fun notify(context: Context, notification: Notification) {
        if (ActivityCompat.checkSelfPermission(
                context,
                Manifest.permission.POST_NOTIFICATIONS
            ) != PackageManager.PERMISSION_GRANTED
        ) {
            return
        }

        NotificationManagerCompat.from(context)
            .notify(Constants.expiryNotificationId,notification)
    }
}