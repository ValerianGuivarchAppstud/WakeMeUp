package com.wakemeup.alarmclock

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent


class AlertReceiver : BroadcastReceiver() {

    override fun onReceive(context: Context?, intent: Intent?) {
        val i = Intent(context, ReveilSonneActivity::class.java)
        i.flags = Intent.FLAG_ACTIVITY_NEW_TASK
        context!!.startActivity(i)
    }
}