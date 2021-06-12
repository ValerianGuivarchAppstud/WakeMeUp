package com.vguivarc.wakemeup.ui.alarm

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.os.Build
import com.vguivarc.wakemeup.AndroidApplication
import com.vguivarc.wakemeup.domain.service.AlarmAndroidSetter.Companion.ACTION_FIRED
import com.vguivarc.wakemeup.domain.service.AlarmAndroidSetter.Companion.ACTION_INEXACT_FIRED
import com.vguivarc.wakemeup.domain.service.AlarmAndroidSetter.Companion.EXTRA_ID
import timber.log.Timber


class AlertReceiver : BroadcastReceiver() {

    companion object {
        //const val ACTION_FIRED = BuildConfig.APPLICATION_ID + ".ACTION_FIRED"
      //  const val ACTION_INEXACT_FIRED = BuildConfig.APPLICATION_ID + ".ACTION_INEXACT_FIRED"
       // const val EXTRA_ID = "intent.extra.alarm"
        //const val EXTRA_TYPE = "intent.extra.type"
    }


    override fun onReceive(context: Context, intent: Intent) {
        when (intent.action) {
            ACTION_FIRED -> {
                val id = intent.getIntExtra(EXTRA_ID, -1)
                //val calendarType = CalendarType.valueOf(intent.extras?.getString(EXTRA_TYPE)!!)
/*                alarms.getAlarm(id)?.let {
                    alarms.onAlarmFired(it, calendarType)
                }*/
            AndroidApplication.alarmAndroidSetterImpl.removeInexactAlarm(id)
            }
            ACTION_INEXACT_FIRED -> {
                val id = intent.getIntExtra(EXTRA_ID, -1)
                Timber.e( "Fired  ACTION_INEXACT_FIRED $id" )
                //alarms.getAlarm(id)?.onInexactAlarmFired()

                //val id = intent.getIntExtra(EXTRA_ID, -1)
                Timber.e("lolA")
                val intentAlarmReceiver = Intent(context, ReveilSonneService::class.java)
                intentAlarmReceiver.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP)
                intentAlarmReceiver.flags = Intent.FLAG_ACTIVITY_NEW_TASK
                intentAlarmReceiver.putExtra(EXTRA_ID, id)
                Timber.e("lolB")
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                    Timber.e("lol1")
                    context.startForegroundService(intentAlarmReceiver)
                } else {
                    Timber.e("lol2")
                    context.startService(intentAlarmReceiver)
                }
            }
           Intent.ACTION_BOOT_COMPLETED,
            Intent.ACTION_TIMEZONE_CHANGED,
            Intent.ACTION_LOCALE_CHANGED,
            Intent.ACTION_MY_PACKAGE_REPLACED -> {
                Timber.e("Refreshing alarms because of ${intent.action}" )
                //alarms.refresh()
            }
            //Intent.ACTION_TIME_CHANGED -> alarms.onTimeSet()

            /*ACTION_REQUEST_SNOOZE -> {
                val id = intent.getIntExtra(AlarmsScheduler.EXTRA_ID, -1)
                log.debug { "Snooze $id" }
                alarms.getAlarm(id)?.snooze()
            }

            PresentationToModelIntents.ACTION_REQUEST_DISMISS -> {
                val id = intent.getIntExtra(AlarmsScheduler.EXTRA_ID, -1)
                log.debug { "Dismiss $id" }
                alarms.getAlarm(id)?.dismiss()
            }

            PresentationToModelIntents.ACTION_REQUEST_SKIP -> {
                val id = intent.getIntExtra(AlarmsScheduler.EXTRA_ID, -1)
                log.debug { "RequestSkip $id" }
                alarms.getAlarm(id)?.requestSkip()
            }*/
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
