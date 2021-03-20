package com.enigma.application.presentation.activity

import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.util.Log
import android.widget.Toast
import androidx.core.app.NotificationCompat
import androidx.localbroadcastmanager.content.LocalBroadcastManager
import com.google.firebase.messaging.FirebaseMessagingService
import com.google.firebase.messaging.RemoteMessage
import java.util.*


class MyFirebaseMessagingService : FirebaseMessagingService() {
    private val CHANNEL_ID = "CHANNEL"
    lateinit var manager: NotificationManager

    private var broadcaster: LocalBroadcastManager? = null
    private val processLater = false

    private var title: String = ""
    private var message: String = ""

    override fun onCreate() {
        broadcaster = LocalBroadcastManager.getInstance(this)
    }


    override fun onMessageReceived(remoteMessage: RemoteMessage) {
        super.onMessageReceived(remoteMessage)

        title = remoteMessage.data?.get("title")!!
        message = remoteMessage.data?.get("message")!!

        if (message == null) {
            message = Objects.requireNonNull(remoteMessage.notification!!.body)!!
        }

        manager = this.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager

        sendNotification()
    }

    fun sendNotification() {
        var intent = Intent(applicationContext, MainActivity::class.java)

        intent.putExtra("title", title)
        intent.putExtra("message", message)

        intent.flags = Intent.FLAG_ACTIVITY_CLEAR_TOP
        intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK

        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.O) {
            var channel =
                NotificationChannel(
                    CHANNEL_ID,
                    "pushnotificaiton",
                    NotificationManager.IMPORTANCE_HIGH
                )
            manager.createNotificationChannel(channel)
        }

        var builder = NotificationCompat.Builder(this, CHANNEL_ID)
            .setContentTitle(title)
            .setContentText(message)

        var pendingIntent =
            PendingIntent.getActivity(applicationContext, 0, intent, PendingIntent.FLAG_ONE_SHOT)

        builder.setContentIntent(pendingIntent)
//        manager.notify(0, builder.build())
        Log.d("$title", "$message")
    }

//    fun getManager(): NotificationManager {
//        var manager = this.getSystemService(Context.NOTIFICATION_SERVICE)
//        return (manager as NotificationManager)
//    }


//    override fun onNewToken(token: String) {
//        Log.d(TAG, "Refreshed token: $token")
//
//        getSharedPreferences("_", MODE_PRIVATE).edit().putString("fcm_token", token).apply()
//    }


//    private fun handleNow(remoteMessage: RemoteMessage) {
//        val handler = Handler(Looper.getMainLooper())
//
//        handler.post {
//            Toast.makeText(
//                baseContext,
//                getString(R.string.handle_notification_now),
//                Toast.LENGTH_LONG
//            ).show()
//
//            remoteMessage.notification?.let {
//                val intent = Intent("MyData")
//                intent.putExtra("message", remoteMessage.data["text"])
//                broadcaster?.sendBroadcast(intent)
//            }
//        }
//    }

    companion object {
        private const val TAG = "MyFirebaseMessagingS"
    }
}