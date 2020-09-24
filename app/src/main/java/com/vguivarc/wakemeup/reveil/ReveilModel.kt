package com.vguivarc.wakemeup.reveil

import android.app.AlarmManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.os.Parcelable
import androidx.work.Data
import androidx.work.ExistingWorkPolicy
import androidx.work.OneTimeWorkRequest
import androidx.work.WorkManager
import com.google.firebase.Timestamp
import com.vguivarc.wakemeup.AppWakeUp
import kotlinx.android.parcel.Parcelize
import timber.log.Timber
import java.io.Serializable
import java.util.*
import java.util.concurrent.TimeUnit
import kotlin.system.exitProcess


@Parcelize
class ReveilModel(
    var listActifDays: MutableList<DaysWeek> = mutableListOf(
        DaysWeek.Lundi,
        DaysWeek.Mardi,
        DaysWeek.Mercredi,
        DaysWeek.Jeudi,
        DaysWeek.Vendredi
    ),
    var nextAlarmCalendar: Calendar = Calendar.getInstance().apply {
        set(Calendar.DAY_OF_YEAR, Calendar.getInstance().get(Calendar.DAY_OF_YEAR) + 1)
        set(Calendar.HOUR_OF_DAY, 9)
        set(Calendar.MINUTE, 0)
        set(Calendar.SECOND, 0)
        set(Calendar.MILLISECOND, 0)
    },
    private var hour: Int = 9,
    private var minute: Int = 0,
    var isActif: Boolean = true,
    var repetition: ReveilMode = ReveilMode.Semaine,
    val idReveil: Int = getUnicId()
) : Parcelable, Serializable {

    enum class ReveilMode {
        PasDeRepetition, Semaine, TousLesJours, Personnalise
    }

    fun getHeureTexte(): String {
        var texteHeure = "" + nextAlarmCalendar.get(Calendar.HOUR_OF_DAY) + ":"
        if (nextAlarmCalendar.get(Calendar.MINUTE) < 10) {
            texteHeure += "0"
        }
        return texteHeure + nextAlarmCalendar.get(Calendar.MINUTE)
    }

    fun getJoursTexte(): String {
        return if(repetition == ReveilMode.PasDeRepetition){
            "[Pas de répétition]"
        } else if (listActifDays.size == 7) {
            "[Tous les jours]"
        } else  {
            listActifDays.toString()
        }
    }

    enum class DaysWeek {
        Lundi, Mardi, Mercredi, Jeudi, Vendredi, Samedi, Dimanche
    }


    companion object {
        const val CREATE_REQUEST_CODE = 1
        const val EDIT_REQUEST_CODE = 2
        const val NUM_REVEIL: String = "NUM_REVEIL"
        const val REVEIL: String = "REVEIL"
        const val DELETE: String = "DELETE"
        const val DUREE_SNOOZE : Int = 5

        private val listDaysInWeek = listOf(
            DaysWeek.Dimanche,
            DaysWeek.Lundi,
            DaysWeek.Mardi,
            DaysWeek.Mercredi,
            DaysWeek.Jeudi,
            DaysWeek.Vendredi,
            DaysWeek.Samedi
        )


        private var idCount = 1

        fun getUnicId(): Int {
            idCount += 1
            return idCount - 1
        }

        fun chargement(listeReveils: MutableMap<Int, ReveilModel>) {
            idCount = listeReveils.keys.maxOrNull() ?: 0
            idCount++
        }

        fun getTextNextClock(time : Long) : String{
            var textClock = "Réveil prévu dans"
            var nextClockMinute =
                (time / 1000 / 60) - (Calendar.getInstance().timeInMillis / 1000 / 60)

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
            return textClock
        }
    }

    fun startAlarm(snoozing : Boolean = false) {

   val data = Data.Builder()
        data.putString("idReveil", idReveil.toString())
        val nextAlarmCalendarToUse = nextAlarmCalendar
            if(snoozing){
                nextAlarmCalendarToUse.add(Calendar.MINUTE, DUREE_SNOOZE)
            }
/*        val calTemp = Calendar.getInstance()
        calTemp.add(Calendar.SECOND, 3)
        AppWakeUp.repository.alarmSetter.setInexactAlarm(idReveil, calTemp)*/


        val t = nextAlarmCalendarToUse.timeInMillis/1000

        val request = OneTimeWorkRequest.Builder(AlarmWorker::class.java)
           .setInitialDelay(t- Timestamp.now().seconds, TimeUnit.SECONDS)
          //  .setInitialDelay(5, TimeUnit.SECONDS)
            .setInputData(data.build())
            .build()

        WorkManager.getInstance(AppWakeUp.appContext).beginUniqueWork(idReveil.toString(), ExistingWorkPolicy.REPLACE, request).enqueue()
    }

    fun snooze(){
        startAlarm(true)
    }


    fun stop(){
        if(this.repetition!=ReveilMode.PasDeRepetition) {
            calculeNextCalendar()
            startAlarm()
        } else {
            //TODO pas fait ici, faut supprimer
            AppWakeUp.repository.switchReveil(idReveil)
        }
    }
    fun cancelAlarm() {
        val alarmManager =
            AppWakeUp.appContext.getSystemService(Context.ALARM_SERVICE) as AlarmManager
        val intent = Intent(AppWakeUp.appContext, AlertReceiver::class.java)
        val pendingIntent =
            PendingIntent.getBroadcast(AppWakeUp.appContext, this.idReveil, intent, 0)
        //pendingIntent doit être le même qu'avant
        alarmManager.cancel(pendingIntent)
        //TODO urgent, volume pas bon
    }

    fun calculeNextCalendarWithNewHours(_hour : Int, _minute : Int) {
        hour = _hour
        minute = _minute
        calculeNextCalendar()
    }

    //TODO notif pour le.s prochain.s reveil.s (snooze ou pas)
    fun calculeNextCalendar(){
        val now = Calendar.getInstance()
        nextAlarmCalendar = Calendar.getInstance()
        nextAlarmCalendar.set(Calendar.HOUR_OF_DAY, hour)
        nextAlarmCalendar.set(Calendar.MINUTE, minute)
        nextAlarmCalendar.set(Calendar.SECOND, 0)
        nextAlarmCalendar.set(Calendar.MILLISECOND, 0)
        if (this.listActifDays.isEmpty()) {
            Timber.e( "ListActifDays empty")
            exitProcess(0)
        }


        if(repetition==ReveilMode.PasDeRepetition){
            while (now > nextAlarmCalendar) {
                nextAlarmCalendar.set(Calendar.DAY_OF_YEAR, nextAlarmCalendar.get(Calendar.DAY_OF_YEAR) + 1)
            }
        } else {
            while (now > nextAlarmCalendar || !this.listActifDays.contains(
                    listDaysInWeek[nextAlarmCalendar.get(
                        Calendar.DAY_OF_WEEK
                    ) - 1]
                )
            ) {
                nextAlarmCalendar.set(
                    Calendar.DAY_OF_YEAR,
                    nextAlarmCalendar.get(Calendar.DAY_OF_YEAR) + 1
                )
            }
        }

        //TODO REMOVE
/*        nextAlarmCalendar=Calendar.getInstance()
        nextAlarmCalendar.add(Calendar.SECOND, 3)*/
    }

    fun getText(): String {
        var s =  ""+listDaysInWeek[nextAlarmCalendar.get(Calendar.DAY_OF_WEEK)-1]+" à "+nextAlarmCalendar.get(Calendar.HOUR_OF_DAY)+":"
        s += if(nextAlarmCalendar.get(Calendar.MINUTE)<10) {
            "0"+nextAlarmCalendar.get(Calendar.MINUTE)
        } else {
            nextAlarmCalendar.get(Calendar.MINUTE)
        }
        return s
    }

}