package com.vguivarc.wakemeup.notification

import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.media.RingtoneManager
import android.os.Build
import android.os.Bundle
import androidx.core.app.NotificationCompat
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.messaging.FirebaseMessagingService
import com.google.firebase.messaging.RemoteMessage
import com.vguivarc.wakemeup.AppWakeUp
import com.vguivarc.wakemeup.MainActivity


class MyFirebaseMessagingService : FirebaseMessagingService() {


    companion object {
        fun registerInstanceOfUser(token: Token?, user: FirebaseUser?, userId : String) {
            if (token != null && user != null) {
                AppWakeUp.repository.database.getReference("Tokens").child(userId)
                    .setValue(token)
            }
        }
    }

    //TODO énorme todo, ici ! a remettre quand je ferai les notifs

/*    override fun onNewToken(token: String) {
        registerInstanceOfUser(Token(token), AppWakeUp.auth.currentUser)
    }


    override fun onMessageReceived(remoteMessage: RemoteMessage) {
        super.onMessageReceived(remoteMessage)
        val sented = remoteMessage.data["sented"]
        if (AppWakeUp.auth.currentUser != null && sented.equals(AppWakeUp.auth.currentUser!!.uid)) {
            sendNotifications(remoteMessage)
        }
    }

    //TODO utiliser la notif dans AppWakeUp à la place
    private fun sendNotifications(remoteMessage: RemoteMessage) {
        val user = remoteMessage.data["user"]!!
        val icon = remoteMessage.data["icon"]!!
        val title = remoteMessage.data["title"]!!
        val body = remoteMessage.data["body"]!!
        val notification = remoteMessage.notification
        val j = (user.replace("[\\D]", "")).toInt()
        val intent = Intent(this, MainActivity::class.java)
        val bundle = Bundle()
        bundle.putString("userid", AppWakeUp.auth.currentUser!!.uid)
        intent.putExtras(bundle)
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP)
        val pendingIntent = PendingIntent.getActivity(this, j, intent, PendingIntent.FLAG_ONE_SHOT)
        val defaultSound = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION)
        val builder = NotificationCompat.Builder(this).setSmallIcon(icon.toInt())
            .setContentTitle(title).setContentText(body).setAutoCancel(true).setSound(defaultSound)
            .setContentIntent(pendingIntent)
        val noti = getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
        val i = if (j > 0) {
            j
        } else {
            0
        }
        noti.notify(i, builder.build())
    }


    //methode pour créer des notifications, à utiliser et améliorer le jour où les notifications seront mises en place
    private fun createNotificationChannel() {
        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.O) {
            return
        }
        val name = "WakeMeUp Notification"
        val descriptionText = "Description Wake Me Up Notification"
        val importance = NotificationManager.IMPORTANCE_DEFAULT
        val channel =
            NotificationChannel(AppWakeUp.NOTIFICATION_CHANNEL_ID, name, importance).apply {
                description = descriptionText
            }
        val notificationManager =
            getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
        notificationManager.createNotificationChannel(channel)

/*      EXEMPLE :
            val builder = NotificationCompat.Builder(this, AppWakeUp.NOTIFICATION_CHANNEL_ID)
                .setSmallIcon(R.mipmap.ic_launcher)
                .setContentTitle("testNotifTitre")
                .setAutoCancel(true)

            NotificationManagerCompat.from(this)
                .notify(AppWakeUp.NOTIFICATION_Test, builder.build())*/


    }*/
}