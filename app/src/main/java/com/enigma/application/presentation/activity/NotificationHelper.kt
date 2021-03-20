package com.enigma.application.presentation.activity

import android.app.NotificationChannel
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import androidx.core.app.NotificationCompat
import androidx.core.app.NotificationManagerCompat
import com.enigma.application.R
import com.enigma.application.data.repository.NewTaskRepository

object NotificationHelper {
    val CHANNEL_ID = "CHANNEL"

    fun displayNotification(context: Context, title: String, body: String) {

        val intent = Intent(context, MainActivity::class.java)

        val fullScreenPendingIntent = PendingIntent.getActivity(
            context, 0,
            intent, PendingIntent.FLAG_UPDATE_CURRENT
        )


        val pendingIntent = PendingIntent.getActivity(
            context,
            100,
            intent,
            PendingIntent.FLAG_CANCEL_CURRENT
        )

        val mBuilder = NotificationCompat.Builder(context, CHANNEL_ID)
            .setSmallIcon(R.drawable.ic_launcher_foreground)
            .setContentTitle(title)
            .setContentText(body)
            .setContentIntent(pendingIntent)
            .setAutoCancel(true)
            .setPriority(NotificationCompat.PRIORITY_HIGH)
            .setCategory(NotificationCompat.CATEGORY_MESSAGE)
            .setFullScreenIntent(fullScreenPendingIntent, false)
            .build()


        val mNotificationMgr = NotificationManagerCompat.from(context)
        mNotificationMgr.notify(1, mBuilder)

    }

}

