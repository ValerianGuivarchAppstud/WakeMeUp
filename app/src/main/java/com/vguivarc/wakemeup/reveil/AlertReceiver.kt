package com.vguivarc.wakemeup.reveil

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.os.Build
import com.vguivarc.wakemeup.BuildConfig


class AlertReceiver : BroadcastReceiver() {

    companion object {
        //const val ACTION_FIRED = BuildConfig.APPLICATION_ID + ".ACTION_FIRED"
        const val ACTION_INEXACT_FIRED = BuildConfig.APPLICATION_ID + ".ACTION_INEXACT_FIRED"
        const val EXTRA_ID = "intent.extra.alarm"
        //const val EXTRA_TYPE = "intent.extra.type"
    }


    override fun onReceive(context: Context, intent: Intent) {
        when (intent.action) {
         /*   AlarmSetter.ACTION_FIRED -> {
                val id = intent.getIntExtra(AlarmsScheduler.EXTRA_ID, -1)
                val calendarType = CalendarType.valueOf(intent.extras?.getString(AlarmsScheduler.EXTRA_TYPE)!!)
                log.debug { "Fired $id $calendarType" }
                alarms.getAlarm(id)?.let {
                    alarms.onAlarmFired(it, calendarType)
                }
            }*/
            ACTION_INEXACT_FIRED -> {
                val id = intent.getIntExtra(EXTRA_ID, -1)
                val i = Intent(context, ReveilSonneService::class.java)
                i.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP)
                i.flags = Intent.FLAG_ACTIVITY_NEW_TASK
                i.putExtra(EXTRA_ID, id)
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                    context.startForegroundService(i)
                } else {
                    context.startService(i)
                }
            }
           /* Intent.ACTION_BOOT_COMPLETED,
            Intent.ACTION_TIMEZONE_CHANGED,
            Intent.ACTION_LOCALE_CHANGED,
            Intent.ACTION_MY_PACKAGE_REPLACED -> {
                log.debug { "Refreshing alarms because of ${intent.action}" }
                alarms.refresh()
            }
            Intent.ACTION_TIME_CHANGED -> alarms.onTimeSet()*/
        }
    }

  /*  override fun onReceive(context: Context?, intent: Intent?) {
        val i = Intent(context, ReveilSonneService::class.java)
        i.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP)
        i.flags = Intent.FLAG_ACTIVITY_NEW_TASK
        i.putExtra("idReveil", intent!!.getIntExtra("idReveil", -1))


        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            context!!.startForegroundService(i)
        } else {
            context!!.startService(i)
        }

    }
*/
}
