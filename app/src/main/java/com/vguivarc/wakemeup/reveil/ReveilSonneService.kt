package com.vguivarc.wakemeup.reveil

import android.app.*
import android.content.ContentValues
import android.content.Context
import android.content.Intent
import android.graphics.Color
import android.media.RingtoneManager
import android.os.Build
import android.os.IBinder
import androidx.core.app.NotificationCompat
import androidx.core.content.ContextCompat
import com.vguivarc.wakemeup.MainActivity
import com.vguivarc.wakemeup.R

class ReveilSonneService : Service() {

    private var idReveil : Int = 0
    //Gere les notifications
    private fun notification(){

        lateinit var notificationChannel : NotificationChannel
        lateinit var builder : Notification
        val channelId = "com.example.notification"
        val description = "My notification"
        val notificationManage = getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager

        val main_activity_intent = Intent(this, MainActivity::class.java)
        val pi = PendingIntent.getActivity(this,0,main_activity_intent,0)
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
        idReveil = intent!!.getIntExtra("idReveil", -1)
        val i = Intent(this, ReveilSonneActivity::class.java)
        i.putExtra("idReveil", idReveil)
        i.flags = Intent.FLAG_ACTIVITY_NEW_TASK
      //  this.startActivity(i)
        return START_STICKY
    }

    override fun onCreate() {
        super.onCreate()
        val NOTIFICATION_ID = (System.currentTimeMillis() % 10000).toInt()
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            startForeground(NOTIFICATION_ID, NotificationCompat.Builder(this).build())
        }


        // Do whatever you want to do here

        // notification() //lance la notification quand le reveille sonne


    }

    override fun onBind(intent: Intent?): IBinder? {
        return null
    }


}