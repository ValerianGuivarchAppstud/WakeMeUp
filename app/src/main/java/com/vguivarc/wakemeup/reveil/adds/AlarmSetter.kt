package com.vguivarc.wakemeup.reveil.adds

/**
 * Created by Yuriy on 24.06.2017.
 */
/*
interface AlarmSetter {

  //  fun removeRTCAlarm()

   // fun setUpRTCAlarm(id: Int, typeName: String, calendar: Calendar)

  //  fun fireNow(id: Int, typeName: String)

    fun removeInexactAlarm(id: Int)

    fun setInexactAlarm(id: Int, calendar: Calendar)

    class AlarmSetterImpl(private val am: AlarmManager, private val mContext: Context) : AlarmSetter {
        private val setAlarmStrategy: ISetAlarmStrategy

        init {
            this.setAlarmStrategy = initSetStrategyForVersion()
        }

       /* override fun removeRTCAlarm() {
            Timber.e( "Removed all alarms" )
            val pendingAlarm = PendingIntent.getBroadcast(
                    mContext,
                    pendingAlarmRequestCode,
                Intent(ACTION_FIRED).apply {
                        // must be here, otherwise replace does not work
                        setClass(mContext, AlertReceiver::class.java)
                    },
                    PendingIntent.FLAG_UPDATE_CURRENT
            )
            am.cancel(pendingAlarm)
        }

        override fun setUpRTCAlarm(id: Int, typeName: String, calendar: Calendar) {
            val pendingAlarm = Intent(ACTION_FIRED)
                    .apply {
                        setClass(mContext, AlertReceiver::class.java)
                        putExtra(EXTRA_ID, id)
                        putExtra(EXTRA_TYPE, typeName)
                    }
                    .let { PendingIntent.getBroadcast(mContext, pendingAlarmRequestCode, it, PendingIntent.FLAG_UPDATE_CURRENT) }

            setAlarmStrategy.setRTCAlarm(calendar, pendingAlarm)
        }

        override fun fireNow(id: Int, typeName: String) {
            val intent = Intent(ACTION_FIRED).apply {
                putExtra(EXTRA_ID, id)
                putExtra(EXTRA_TYPE, typeName)
            }
            mContext.sendBroadcast(intent)
        }*/

        override fun setInexactAlarm(id: Int, calendar: Calendar) {
            val pendingAlarm = Intent(ACTION_INEXACT_FIRED)
                    .apply {
                        setClass(mContext, AlertReceiver::class.java)
                        putExtra(EXTRA_ID, id)
                    }
                    .let { PendingIntent.getBroadcast(mContext, id, it, PendingIntent.FLAG_UPDATE_CURRENT) }

            setAlarmStrategy.setInexactAlarm(calendar, pendingAlarm)
        }

        override fun removeInexactAlarm(id: Int) {
            Timber.e( "removeInexactAlarm id: $id" )
            val pendingAlarm = PendingIntent.getBroadcast(
                    mContext,
                    id,
                Intent(ACTION_INEXACT_FIRED).apply {
                        // must be here, otherwise replace does not work
                        setClass(mContext, AlertReceiver::class.java)
                    },
                    PendingIntent.FLAG_UPDATE_CURRENT
            )
            am.cancel(pendingAlarm)
        }

        private fun initSetStrategyForVersion(): ISetAlarmStrategy {
            Timber.e( "SDK is %s", Build.VERSION.SDK_INT )
            return when {
                Build.VERSION.SDK_INT >= 26 -> OreoSetter()
                Build.VERSION.SDK_INT >= 23 -> MarshmallowSetter()
                Build.VERSION.SDK_INT >= 19 -> KitKatSetter()
                else -> IceCreamSetter()
            }
        }

        private inner class IceCreamSetter : ISetAlarmStrategy {
            override fun setRTCAlarm(calendar: Calendar, pendingIntent: PendingIntent) {
                am.set(AlarmManager.RTC_WAKEUP, calendar.timeInMillis, pendingIntent)
            }

            override fun setInexactAlarm(calendar: Calendar, pendingIntent: PendingIntent) {
                am.set(AlarmManager.RTC_WAKEUP, calendar.timeInMillis, pendingIntent)
            }
        }

        @TargetApi(Build.VERSION_CODES.KITKAT)
        private inner class KitKatSetter : ISetAlarmStrategy {
            override fun setRTCAlarm(calendar: Calendar, pendingIntent: PendingIntent) {
                am.setExact(AlarmManager.RTC_WAKEUP, calendar.timeInMillis, pendingIntent)
            }

            override fun setInexactAlarm(calendar: Calendar, pendingIntent: PendingIntent) {
                am.setExact(AlarmManager.RTC_WAKEUP, calendar.timeInMillis, pendingIntent)
            }
        }

        @TargetApi(23)
        private inner class MarshmallowSetter : ISetAlarmStrategy {
            override fun setRTCAlarm(calendar: Calendar, pendingIntent: PendingIntent) {
                am.setExactAndAllowWhileIdle(AlarmManager.RTC_WAKEUP, calendar.timeInMillis, pendingIntent)
            }
        }

        /** 8.0  */
        @TargetApi(Build.VERSION_CODES.O)
        private inner class OreoSetter : ISetAlarmStrategy {
            override fun setRTCAlarm(calendar: Calendar, pendingIntent: PendingIntent) {
               /* val pendingShowList = PendingIntent.getActivity(
                        mContext,
                        100500,
                        Intent(mContext, AlarmsListActivity::class.java),
                        PendingIntent.FLAG_UPDATE_CURRENT
                )
                am.setAlarmClock(AlarmManager.AlarmClockInfo(calendar.timeInMillis, pendingShowList), pendingIntent)*/
            }

            override fun setInexactAlarm(calendar: Calendar, pendingIntent: PendingIntent) {
                am.setExactAndAllowWhileIdle(AlarmManager.RTC_WAKEUP, calendar.timeInMillis, pendingIntent)
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
        //const val ACTION_FIRED = BuildConfig.APPLICATION_ID + ".ACTION_FIRED"
        const val ACTION_INEXACT_FIRED = BuildConfig.APPLICATION_ID + ".ACTION_INEXACT_FIRED"
        const val EXTRA_ID = "intent.extra.alarm"
        const val EXTRA_TYPE = "intent.extra.type"
    }
}
*/