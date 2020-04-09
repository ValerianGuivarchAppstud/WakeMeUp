package com.wakemeup.notification

import android.app.NotificationManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.media.RingtoneManager
import android.os.Bundle
import android.util.Log
import androidx.core.app.NotificationCompat
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.messaging.FirebaseMessagingService
import com.google.firebase.messaging.RemoteMessage
import com.wakemeup.AppWakeUp
import com.wakemeup.main.MainActivity


class MyFirebaseMessagingService : FirebaseMessagingService() {


    /*   override fun onNewToken(s: String) {
          super.onNewToken(s)

           if(AppWakeUp.auth.currentUser!=null){
               val token = Token(s)
               AppWakeUp.database.getReference("Tokens").child(AppWakeUp.auth.currentUser!!.uid).setValue(token)
           }
      }*/


    override fun onNewToken(token: String) {
        Log.e("MyFirebaseMS", "Refreshed token: $token")
        registerInstanceOfUser(Token(token), AppWakeUp.auth.currentUser)
    }

    companion object {
        fun registerInstanceOfUser(token: Token?, user: FirebaseUser?) {
            Log.e("MyFirebaseMS", "TRY")
            Log.e("MyFirebaseMS-Token", (token == null).toString())
            Log.e("MyFirebaseMS-user", (user == null).toString())
            if (token != null && user != null) {
                Log.e(
                    "MyFirebaseMS",
                    "sendRegistrationTokenToServer(${token.token}) for user ${AppWakeUp.auth.currentUser!!.uid}"
                )
                AppWakeUp.database.getReference("Tokens").child(AppWakeUp.auth.currentUser!!.uid)
                    .setValue(token)
            }
        }
    }

    override fun onMessageReceived(remoteMessage: RemoteMessage) {

        Log.e("Notif", "A")
        super.onMessageReceived(remoteMessage)
        Log.e("Notif", "B")
        val sented = remoteMessage.data["sented"]
        if (AppWakeUp.auth.currentUser != null && sented.equals(AppWakeUp.auth.currentUser!!.uid)) {
            Log.e("Notif", "C")
            sendNotifications(remoteMessage)
        }
    }

    private fun sendNotifications(remoteMessage: RemoteMessage) {
        val user = remoteMessage.data["user"]!!
        val icon = remoteMessage.data["icon"]!!
        val title = remoteMessage.data["title"]!!
        val body = remoteMessage.data["body"]!!
        Log.e("Notif", "D")
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
        Log.e("Notif", "E")
        val i = if (j > 0) {
            j
        } else {
            0
        }
        noti.notify(i, builder.build())


    }
}