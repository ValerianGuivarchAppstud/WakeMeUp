package com.vguivarc.wakemeup.domain.external.entity

import android.os.Parcelable
import androidx.work.Data
import com.squareup.moshi.JsonClass
import com.vguivarc.wakemeup.AndroidApplication
import com.vguivarc.wakemeup.domain.internal.AlarmAndroidProvider.Companion.EXTRA_ID
import kotlinx.android.parcel.Parcelize
import timber.log.Timber
import java.util.*
import kotlin.system.exitProcess

@Parcelize
@JsonClass(generateAdapter = true)
class Alarm(
    val idAlarm: Int,
    var listActifDays: MutableList<DaysWeek> = mutableListOf(
        DaysWeek.Monday,
        DaysWeek.Tuesday,
        DaysWeek.Wednesday,
        DaysWeek.Thursday,
        DaysWeek.Friday
    ),
    var nextAlarmCalendar: Calendar = Calendar.getInstance().apply {
        set(Calendar.DAY_OF_YEAR, Calendar.getInstance().get(Calendar.DAY_OF_YEAR) + 1)
        set(Calendar.HOUR_OF_DAY, 9)
        set(Calendar.MINUTE, 0)
        set(Calendar.SECOND, 0)
        set(Calendar.MILLISECOND, 0)
    },
    var isActif: Boolean = true,
    var isRepeated: Boolean = true
) : Parcelable {

    var hour: Int
        get() {
            return nextAlarmCalendar.get(Calendar.HOUR_OF_DAY)
        }
        set(value) {
            nextAlarmCalendar.set(Calendar.HOUR_OF_DAY, value)
        }

    var minute: Int
        get() {
            return nextAlarmCalendar.get(Calendar.MINUTE)
        }
        set(value) {
            nextAlarmCalendar.set(Calendar.MINUTE, value)
        }

    fun getHeureTexte(): String {
        var texteHeure = "" + nextAlarmCalendar.get(Calendar.HOUR_OF_DAY) + ":"
        if (nextAlarmCalendar.get(Calendar.MINUTE) < 10) {
            texteHeure += "0"
        }
        return texteHeure + nextAlarmCalendar.get(Calendar.MINUTE)
    }

    fun getJoursTexte(): String {
        return if (isRepeated) {
            // TODO déterminer "aujourdhui ou demain"
            "[Pas de répétition]"
        } else {
            if (listActifDays.size == 7) {
                "[Tous les jours]"
            } else {
                listActifDays.toString()
            }
        }
    }

    enum class DaysWeek {
        Monday, Tuesday, Wednesday, Thursday, Friday, Saturday, Sunday
    }

    companion object {

        const val DUREE_SNOOZE: Int = 5

        private val listDaysInWeek = listOf(
            DaysWeek.Sunday,
            DaysWeek.Monday,
            DaysWeek.Tuesday,
            DaysWeek.Wednesday,
            DaysWeek.Thursday,
            DaysWeek.Friday,
            DaysWeek.Saturday
        )

        fun getTextNextClock(time: Long): String {
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

    fun startAlarm(snoozing: Boolean = false) {
        calculeNextCalendar()

        val data = Data.Builder()
        data.putString(EXTRA_ID, idAlarm.toString())
        val nextAlarmCalendarToUse = nextAlarmCalendar
        if (snoozing) {
            nextAlarmCalendarToUse.add(Calendar.MINUTE, DUREE_SNOOZE)
        }
/*        val calTemp = Calendar.getInstance()
        calTemp.add(Calendar.SECOND, 3)
        AppWakeUp.repository.alarmSetter.setInexactAlarm(idReveil, calTemp)*/

        val nextAlarmCalendarToUseBis = Calendar.getInstance()
        nextAlarmCalendarToUseBis.add(Calendar.SECOND, 3)


        AndroidApplication.alarmAndroidProviderImpl.setInexactAlarm(idAlarm, nextAlarmCalendarToUseBis)

/*        val t = nextAlarmCalendarToUse.timeInMillis/1000

        val request = OneTimeWorkRequest.Builder(AlarmWorker::class.java)
           .setInitialDelay(t- Timestamp.now().seconds, TimeUnit.SECONDS)
          //  .setInitialDelay(5, TimeUnit.SECONDS)
            .setInputData(data.build())
            .build()

        WorkManager.getInstance(AppWakeUp.appContext).beginUniqueWork(idReveil.toString(), ExistingWorkPolicy.REPLACE, request).enqueue()

*/
    }

    fun snooze() {
        startAlarm(true)
    }

    fun stop() {
        if (isRepeated) {
            calculeNextCalendar()
            startAlarm()
        } else {
            // TODO pas fait ici, faut supprimer
            //   AndroidApplication.repository.switchReveil(idReveil)
        }
    }

    fun switch() {
        isActif = !isActif
        if (isActif)
            startAlarm()
        else
            cancelAlarm()
    }
    fun cancelAlarm() {
        /*
        val alarmManager =
            AppWakeUp.appContext.getSystemService(Context.ALARM_SERVICE) as AlarmManager
        val intent = Intent(AppWakeUp.appContext, AlertReceiver::class.java)
        val pendingIntent =
            PendingIntent.getBroadcast(AppWakeUp.appContext, this.idReveil, intent, 0)
        //pendingIntent doit être le même qu'avant
        alarmManager.cancel(pendingIntent)*/
        AndroidApplication.alarmAndroidProviderImpl.removeInexactAlarm(idAlarm)
        // TODO urgent, volume pas bon
    }

    fun calculeNextCalendarWithNewHours(_hour: Int, _minute: Int) {
        hour = _hour
        minute = _minute
        calculeNextCalendar()
    }

    // TODO notif pour le.s prochain.s reveil.s (snooze ou pas)
    fun calculeNextCalendar() {
        val now = Calendar.getInstance()
        nextAlarmCalendar = Calendar.getInstance()
        nextAlarmCalendar.set(Calendar.HOUR_OF_DAY, hour)
        nextAlarmCalendar.set(Calendar.MINUTE, minute)
        nextAlarmCalendar.set(Calendar.SECOND, 0)
        nextAlarmCalendar.set(Calendar.MILLISECOND, 0)
        if (this.listActifDays.isEmpty()) {
            Timber.e("ListActifDays empty")
            exitProcess(0)
        }

        if (isRepeated) {
            while (now > nextAlarmCalendar) {
                nextAlarmCalendar.set(Calendar.DAY_OF_YEAR, nextAlarmCalendar.get(Calendar.DAY_OF_YEAR) + 1)
            }
        } else {
            while (now > nextAlarmCalendar || !this.listActifDays.contains(
                    listDaysInWeek[
                        nextAlarmCalendar.get(
                                Calendar.DAY_OF_WEEK
                            ) - 1
                    ]
                )
            ) {
                nextAlarmCalendar.set(
                    Calendar.DAY_OF_YEAR,
                    nextAlarmCalendar.get(Calendar.DAY_OF_YEAR) + 1
                )
            }
        }

        // TODO REMOVE
/*        nextAlarmCalendar=Calendar.getInstance()
        nextAlarmCalendar.add(Calendar.SECOND, 3)*/
    }

    fun getText(): String {
        var s = "" + listDaysInWeek[nextAlarmCalendar.get(Calendar.DAY_OF_WEEK) - 1] + " à " + nextAlarmCalendar.get(Calendar.HOUR_OF_DAY) + ":"
        s += if (nextAlarmCalendar.get(Calendar.MINUTE) <10) {
            "0" + nextAlarmCalendar.get(Calendar.MINUTE)
        } else {
            nextAlarmCalendar.get(Calendar.MINUTE)
        }
        return s
    }
}
