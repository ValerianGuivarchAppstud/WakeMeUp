package com.vguivarc.wakemeup.reveil

import android.app.Notification
import android.app.Service
import android.content.Intent
import android.os.Build
import android.os.IBinder
import androidx.core.app.NotificationCompat
import com.vguivarc.wakemeup.AppWakeUp
import com.vguivarc.wakemeup.reveil.adds.AlarmSetter.Companion.EXTRA_ID
import timber.log.Timber

class ReveilSonneService : Service() {

    companion object{
        const val CHANNEL_ID = "ReveilChannelId"
    }

    private var idReveil : Int = 0


    var notificationId : Int = 0
    lateinit var not : Notification

    override fun onCreate() {
        super.onCreate()
        notificationId = (System.currentTimeMillis() % 10000).toInt()
        Timber.e("onCreate1")
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            not = NotificationCompat.Builder(AppWakeUp.appContext, CHANNEL_ID).build()
            Timber.e("onCreate2")
            Timber.e(not.toString())
            startForeground(notificationId, not)
        }
        stopSelf()

    }

    override fun onStartCommand(intent: Intent?, flags: Int, startId: Int): Int {
        idReveil = intent!!.getIntExtra(EXTRA_ID, -1)
        Timber.e("onStartCommand1 $idReveil")
        val i = Intent(this, ReveilSonneActivity::class.java)
        i.putExtra(EXTRA_ID, idReveil)
        i.flags = Intent.FLAG_ACTIVITY_NEW_TASK
        Timber.e("onStartCommand2")
        this.startActivity(i)
        return START_NOT_STICKY
    }


    override fun onBind(intent: Intent?): IBinder? {
        return null
    }


}