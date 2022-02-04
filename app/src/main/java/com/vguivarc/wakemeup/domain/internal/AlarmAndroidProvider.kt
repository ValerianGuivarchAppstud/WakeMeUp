package com.vguivarc.wakemeup.domain.internal

import android.annotation.TargetApi
import android.app.AlarmManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.os.Build
import com.vguivarc.wakemeup.BuildConfig
import com.vguivarc.wakemeup.transport.ringingalarm.RingingAlarmReceiver
import timber.log.Timber
import java.util.*

/**
 * Created by Yuriy on 24.06.2017.
 */

interface AlarmAndroidProvider {

    fun removeRTCAlarm()
    fun setUpRTCAlarm(id: Int, typeName: String, calendar: Calendar)
    fun fireNow(id: Int, typeName: String)

    fun removeInexactAlarm(id: Int)

    fun setInexactAlarm(id: Int, calendar: Calendar)



    class AlarmAndroidProviderImpl(private val am: AlarmManager, private val mContext: Context) : AlarmAndroidProvider {
        private val setAlarmStrategy: ISetAlarmStrategy

        init {
            this.setAlarmStrategy = initSetStrategyForVersion()
        }

        override fun removeRTCAlarm() {
            Timber.e("Removed all alarms")
            val pendingAlarm = PendingIntent.getBroadcast(
                mContext,
                pendingAlarmRequestCode,
                Intent(ACTION_FIRED).apply {
                    // must be here, otherwise replace does not work
                    setClass(mContext, RingingAlarmReceiver::class.java)
                },
                PendingIntent.FLAG_IMMUTABLE
            )
            am.cancel(pendingAlarm)
        }

        override fun setUpRTCAlarm(id: Int, typeName: String, calendar: Calendar) {
            val pendingAlarm = Intent(ACTION_FIRED)
                .apply {
                    setClass(mContext, RingingAlarmReceiver::class.java)
                    putExtra(EXTRA_ID, id)
                    putExtra(EXTRA_TYPE, typeName)
                }
                .let {
                    PendingIntent.getBroadcast(
                        mContext,
                        pendingAlarmRequestCode,
                        it,
                        PendingIntent.FLAG_IMMUTABLE
                    )
                }

            setAlarmStrategy.setRTCAlarm(calendar, pendingAlarm)
        }

        override fun fireNow(id: Int, typeName: String) {
            val intent = Intent(ACTION_FIRED).apply {
                putExtra(EXTRA_ID, id)
                putExtra(EXTRA_TYPE, typeName)
            }
            mContext.sendBroadcast(intent)
        }

        override fun setInexactAlarm(id: Int, calendar: Calendar) {
            val pendingAlarm = Intent(ACTION_INEXACT_FIRED)
                .apply {
                    setClass(mContext, RingingAlarmReceiver::class.java)
                    putExtra(EXTRA_ID, id)
                }
                .let {
                    PendingIntent.getBroadcast(
                        mContext,
                        id,
                        it,

                        PendingIntent.FLAG_IMMUTABLE
                    )
                }

          /*  val icon: Int = R.drawable.main_logo_white
            val tickerText: CharSequence = "Music Me!"
            val `when` = System.currentTimeMillis()
            val notification = Notification(icon, tickerText, `when`)

// Intent qui lancera vers l'activité MainActivity

// Intent qui lancera vers l'activité MainActivity
            val notificationIntent = Intent(this@MainActivity, MainActivity::class.java)
            notificationIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)

            val contentIntent =
                PendingIntent.getActivity(this@MainActivity, 0, notificationIntent, 0)

            notification.setLatestEventInfo(this@MainActivity, "Titre", "Texte", contentIntent)*/

            setAlarmStrategy.setInexactAlarm(calendar, pendingAlarm)
        }

        override fun removeInexactAlarm(id: Int) {
            Timber.e("removeInexactAlarm id: $id")
            val pendingAlarm = PendingIntent.getBroadcast(
                mContext,
                id,
                Intent(ACTION_INEXACT_FIRED).apply {
                    // must be here, otherwise replace does not work
                    setClass(mContext, RingingAlarmReceiver::class.java)
                },

                PendingIntent.FLAG_IMMUTABLE
            )
            am.cancel(pendingAlarm)
        }

        private fun initSetStrategyForVersion(): ISetAlarmStrategy {
            Timber.i("SDK is %s", Build.VERSION.SDK_INT)
            return when {
                Build.VERSION.SDK_INT >= 26 -> OreoSetter()
                else -> MarshmallowSetter()
                //Build.VERSION.SDK_INT >= 23 -> MarshmallowSetter()
                //else -> IceCreamSetter()
            }
        }

        /*private inner class IceCreamSetter : ISetAlarmStrategy {
            override fun setRTCAlarm(calendar: Calendar, pendingIntent: PendingIntent) {
                am.set(AlarmManager.RTC_WAKEUP, calendar.timeInMillis, pendingIntent)
            }

            override fun setInexactAlarm(calendar: Calendar, pendingIntent: PendingIntent) {
                am.set(AlarmManager.RTC_WAKEUP, calendar.timeInMillis, pendingIntent)
            }
        }*/

        @TargetApi(23)
        private inner class MarshmallowSetter : ISetAlarmStrategy {
            override fun setRTCAlarm(calendar: Calendar, pendingIntent: PendingIntent) {
                am.setExactAndAllowWhileIdle(
                    AlarmManager.RTC_WAKEUP,
                    calendar.timeInMillis,
                    pendingIntent
                )
            }
        }

        /** 8.0  */
        @TargetApi(Build.VERSION_CODES.O)
        private inner class OreoSetter : ISetAlarmStrategy {
            override fun setRTCAlarm(calendar: Calendar, pendingIntent: PendingIntent) {
                // TODO IMPORTANT check Android 8.0  (probablement tout passer en inexact)
                Timber.e("lol")
            /*   val pendingShowList = PendingIntent.getActivity(
                        mContext,
                        100500,
                        Intent(mContext, AlarmsListActivity::class.java),
                        PendingIntent.FLAG_UPDATE_CURRENT
                )
                am.setAlarmClock(AlarmManager.AlarmClockInfo(calendar.timeInMillis, pendingShowList), pendingIntent)*/
            }

            override fun setInexactAlarm(calendar: Calendar, pendingIntent: PendingIntent) {
                am.setExactAndAllowWhileIdle(
                    AlarmManager.RTC_WAKEUP,
                    calendar.timeInMillis,
                    pendingIntent
                )
            }
        }

        private interface ISetAlarmStrategy {
            fun setRTCAlarm(calendar: Calendar, pendingIntent: PendingIntent)
            fun setInexactAlarm(calendar: Calendar, pendingIntent: PendingIntent) {
                setRTCAlarm(calendar, pendingIntent)
            }
        }

        companion object {
            private const val pendingAlarmRequestCode = 0
        }
    }

    companion object {
        const val ACTION_FIRED = BuildConfig.APPLICATION_ID + ".ACTION_FIRED"
        const val ACTION_INEXACT_FIRED = BuildConfig.APPLICATION_ID + ".ACTION_INEXACT_FIRED"
        const val EXTRA_ID = "intent.extra.alarm"
        const val EXTRA_TYPE = "intent.extra.type"
    }
}
