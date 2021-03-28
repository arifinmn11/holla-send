package com.enigma.application.presentation.activity

import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.os.Build
import android.util.Log
import android.widget.Toast
import androidx.core.app.NotificationCompat
import androidx.core.app.NotificationManagerCompat
import androidx.localbroadcastmanager.content.LocalBroadcastManager
import com.enigma.application.R
import com.enigma.application.presentation.activity.NotificationHelper.displayNotification
import com.google.firebase.messaging.FirebaseMessagingService
import com.google.firebase.messaging.RemoteMessage
import java.util.*


class MyFirebaseMessagingService : FirebaseMessagingService() {
    private val CHANNEL_ID = "CHANNEL"
    lateinit var manager: NotificationManager

    private var broadcaster: LocalBroadcastManager? = null
    private var title: String = ""
    private var message: String = ""

    override fun onCreate() {
        broadcaster = LocalBroadcastManager.getInstance(this)
    }

    override fun onMessageReceived(remoteMessage: RemoteMessage) {
        super.onMessageReceived(remoteMessage)

        if (remoteMessage.data.isNotEmpty()) {
            message = remoteMessage.notification?.body.toString()
            Log.d(TAG, "Message data payload: " + remoteMessage.notification?.body);
        }
        if (remoteMessage.notification != null) {
            title = remoteMessage.notification?.title.toString()
            Log.d(TAG, "Message Notification Body: " + remoteMessage.notification?.title);
        }

        manager = this.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
        displayNotification(applicationContext, title, message)

        // sendNotification()
    }

    fun sendNotification() {
        var intent = Intent(applicationContext, MainActivity::class.java)
        intent.putExtra("title", title)
        intent.putExtra("message", message)
        intent.flags = Intent.FLAG_ACTIVITY_CLEAR_TOP
        intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK

//        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
//            val name = getString(R.string.channel_name)
//            val descriptionText = getString(R.string.channel_description)
//            val importance = NotificationManager.IMPORTANCE_DEFAULT
//            val channel = NotificationChannel(CHANNEL_ID, name, importance).apply {
//                description = descriptionText
//            }
//            // Register the channel with the system
//            val notificationManager: NotificationManager =
//                getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
//            notificationManager.createNotificationChannel(channel)
//        }

        val pendingIntent = PendingIntent.getActivity(
            applicationContext,
            100,
            intent,
            PendingIntent.FLAG_CANCEL_CURRENT
        )


        val fullScreenPendingIntent = PendingIntent.getActivity(
            applicationContext, 0,
            intent, PendingIntent.FLAG_UPDATE_CURRENT
        )

        var builder = NotificationCompat.Builder(this, CHANNEL_ID)
            .setSmallIcon(R.drawable.ic_baseline_fiber_new_24)
            .setContentTitle("${title}")
            .setContentText("${message}")
            .setStyle(
                NotificationCompat.BigTextStyle()
                    .bigText("${message}")
            )
            .setContentIntent(pendingIntent)
            .setAutoCancel(true)
            .setPriority(NotificationCompat.PRIORITY_HIGH)
            .setCategory(NotificationCompat.CATEGORY_MESSAGE)
            .setFullScreenIntent(fullScreenPendingIntent, false)
            .build()

        NotificationManagerCompat.from(applicationContext)
        manager.notify(0, builder)
    }

    companion object {
        private const val TAG = "MyFirebaseMessagingS"
    }
}