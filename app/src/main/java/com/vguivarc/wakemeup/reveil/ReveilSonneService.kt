package com.vguivarc.wakemeup.reveil

import android.app.Service
import android.content.Intent
import android.os.Build
import android.os.IBinder
import androidx.core.app.NotificationCompat

class ReveilSonneService : Service() {

    companion object{
        const val CHANNEL_ID = "ReveilChannelId"
    }

    private var idReveil : Int = 0

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
        val notificationId = (System.currentTimeMillis() % 10000).toInt()
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            startForeground(notificationId, NotificationCompat.Builder(this, CHANNEL_ID).build())
        }

    }

    override fun onBind(intent: Intent?): IBinder? {
        return null
    }


}