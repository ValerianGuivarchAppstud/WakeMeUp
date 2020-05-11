package com.wakemeup.reveil

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent


class AlertReceiver : BroadcastReceiver() {

    override fun onReceive(context: Context?, intent: Intent?) {
        val i = Intent(context, ReveilSonneService::class.java)
        i.flags = Intent.FLAG_ACTIVITY_NEW_TASK
        context!!.startService(i)
    }
}