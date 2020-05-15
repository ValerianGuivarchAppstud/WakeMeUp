package com.wakemeup.reveil

import android.app.*
import android.content.Context
import android.content.Intent
import android.graphics.Color
import android.media.RingtoneManager
import android.os.Build
import android.os.IBinder
import androidx.fragment.app.Fragment
import com.wakemeup.MainActivity
import com.wakemeup.R

class ReveilSonneService : Service() {

    //Gere les notifications
    private fun notifcation(){

        lateinit var notificationChannel : NotificationChannel
        lateinit var builder : Notification
        val channelId = "com.example.notification"
        val description = "My notification"
        var notificationManage = getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager

        var main_activity_intent = Intent(this, MainActivity::class.java)
        var pi = PendingIntent.getActivity(this,0,main_activity_intent,0)
        val defaultSoundUri = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION)

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            notificationChannel =
                NotificationChannel(channelId, description, NotificationManager.IMPORTANCE_HIGH)
            notificationChannel.enableLights(true)
            notificationChannel.lightColor = Color.BLUE
            notificationChannel.enableVibration(true)
            notificationManage.createNotificationChannel(notificationChannel)


            builder = Notification.Builder(this, channelId)
                .setContentTitle("Votre réveil social sonne!")
                .setSmallIcon(R.mipmap.ic_launcher)
                .setSound(defaultSoundUri)
                .setContentText("Clique sur moi!")
                .setContentIntent(pi)
                .setAutoCancel(true)
                .build()
        }
        else{
            builder = Notification.Builder(this)
                .setContentTitle("Votre réveil social sonne!")
                .setSmallIcon(R.mipmap.ic_launcher)
                .setSound(defaultSoundUri)
                .setContentText("Clique sur moi!")
                .setContentIntent(pi)
                .setAutoCancel(true)
                .build()
        }
        notificationManage.notify(0,builder)


    }


    override fun onStartCommand(intent: Intent?, flags: Int, startId: Int): Int {

        notifcation() //lance la notification quand le reveille sonne

        val i = Intent(this, ReveilSonneActivity::class.java)
        i.flags = Intent.FLAG_ACTIVITY_NEW_TASK
        this.startActivity(i)


        return START_NOT_STICKY
    }

    override fun onBind(intent: Intent?): IBinder? {
        return null
    }


}