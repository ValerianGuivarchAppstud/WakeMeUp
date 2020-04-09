package com.wakemeup.alarmclock

import android.app.Activity
import android.app.AlarmManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.os.Parcelable
import android.util.Log
import android.widget.Toast
import kotlinx.android.parcel.Parcelize
import java.io.Serializable
import java.util.*

@Parcelize
class Reveil(
    var listActifDays: MutableList<DaysWeek> = mutableListOf(
        DaysWeek.lundi,
        DaysWeek.mardi,
        DaysWeek.mercredi,
        DaysWeek.jeudi,
        DaysWeek.vendredi
    ),
    var nextAlarm: Calendar = Calendar.getInstance().apply {
        set(Calendar.DAY_OF_YEAR, Calendar.getInstance().get(Calendar.DAY_OF_YEAR) + 1)
        set(Calendar.HOUR_OF_DAY, 9)
        set(Calendar.MINUTE, 0)
        set(Calendar.SECOND, 0)
        set(Calendar.MILLISECOND, 0)
    }, var isActif: Boolean = true, val idReveil: Int = getUnicId()
) : Parcelable, Serializable {

    fun getHeureTexte(): String {
        var texteHeure = "" + nextAlarm.get(Calendar.HOUR_OF_DAY) + ":"
        if (nextAlarm.get(Calendar.MINUTE) < 10) {
            texteHeure += "0"
        }
        return texteHeure + nextAlarm.get(Calendar.MINUTE)
    }

    fun getJoursTexte(): String {
        return listActifDays.toString()
    }

    enum class DaysWeek {
        lundi, mardi, mercredi, jeudi, vendredi, samedi, dimanche
    }


    companion object {
        const val CREATE_REQUEST_CODE = 1
        const val EDIT_REQUEST_CODE = 2
        const val NUM_REVEIL: String = "NUM_REVEIL"
        const val REVEIL: String = "REVEIL"
        const val DELETE: String = "DELETE"

        private var idCount = 0

        fun getUnicId(): Int {
            Log.e("GetUnicId", "new -> " + idCount)
            idCount = idCount + 1
            Log.e("GetUnicId", "new -> " + idCount)
            return idCount
        }

        fun chargement(listeReveils: MutableMap<Int, Reveil>) {
            idCount = listeReveils.keys.max() ?: 0
        }
    }

    fun startAlarm(activity: Activity) {
        toastNextClock(activity)
        val alarmManager = activity.getSystemService(Context.ALARM_SERVICE) as AlarmManager
        val intent = Intent(activity, AlertReceiver::class.java)
        val sender = PendingIntent.getBroadcast(
            activity,
            this.idReveil,
            intent,
            PendingIntent.FLAG_UPDATE_CURRENT
        )
        //TODO exact inutile
        alarmManager.setExact(
            AlarmManager.RTC_WAKEUP,
            this.nextAlarm.timeInMillis,
            sender
        )//System.currentTimeMillis()+5000, sender)
    }

    fun cancelAlarm(activity: Activity) {
        val alarmManager = activity.getSystemService(Context.ALARM_SERVICE) as AlarmManager
        val intent = Intent(activity, AlertReceiver::class.java)
        val pendingIntent = PendingIntent.getBroadcast(activity, this.idReveil, intent, 0)
        //pendingIntent doit être le même qu'avant
        alarmManager.cancel(pendingIntent)
    }

    private fun toastNextClock(activity: Activity) {
        var textClock = "Réveil prévu dans"
        var nextClockMinute =
            (this.nextAlarm.timeInMillis / 1000 / 60) - (Calendar.getInstance().timeInMillis / 1000 / 60)

        val jour = nextClockMinute / 1440
        if (jour > 0) {
            textClock += " $jour jour"
            if (jour > 1) {
                textClock += "s"
            }
        }

        nextClockMinute %= 1440
        val heur = nextClockMinute / 60
        if (heur > 0) {
            textClock += " $heur heure"
            if (heur > 1) {
                textClock += "s"
            }
        }
        val minute = nextClockMinute % 60
        if (minute > 0) {
            textClock += " $minute minute"
            if (minute > 1) {
                textClock += "s"
            }
        }
        Toast.makeText(activity, textClock, Toast.LENGTH_LONG).show()
    }
}