package com.vguivarc.wakemeup.data.provider

import com.vguivarc.wakemeup.domain.external.entity.Alarm
import com.vguivarc.wakemeup.domain.internal.IAlarmProvider
import io.paperdb.Paper
import java.util.*

class AlarmProvider : IAlarmProvider {

    companion object {
        private const val KEY_ALARMS = "alarms_v0_2"
    }

    private val alarms = mutableListOf<Alarm>()

    init {
        val alarmList = readAll()
        Alarm.idCount = alarmList.maxByOrNull { it.idAlarms }?.idAlarms ?: 0
        alarms.addAll(alarmList)
    }

    override fun getAlarms(): List<Alarm> = alarms

    override fun save(alarm: Alarm): List<Alarm> {
        val alarms = readAll()

        val existingAlarm = alarms.find { it.idAlarms == alarm.idAlarms }
        existingAlarm?.let {
            it.cancelAlarm() // check
            val existingIndex = alarms.indexOf(existingAlarm)
            alarms.set(existingIndex, alarm)
        } ?: run {
            alarms.add(alarm)
        }
        saveAll(alarms)
        alarm.calculeNextCalendar()
        alarm.startAlarm()
        return getAlarms()
    }

    override fun snoozeAlarm(idAlarm: Int): List<Alarm> {
        readAll()[idAlarm].snooze()
        return getAlarms()
    }

    override fun stopAlarm(idAlarm: Int): List<Alarm> {
        readAll()[idAlarm].stop()
        return getAlarms()
    }

    override fun remove(alarm: Alarm): List<Alarm> {
        val alarms = readAll()

        val existingAlarm = alarms.find { it.idAlarms == alarm.idAlarms }
        existingAlarm?.let {
            it.cancelAlarm() // check
            alarms.remove(it)
        }
        saveAll(alarms)
        return getAlarms()
    }

    override fun switchReveil(alarm: Alarm): List<Alarm> {
        readAll().find { it.idAlarms == alarm.idAlarms }?.switch()
        save(alarm)
        return getAlarms()
    }

    @Synchronized
    fun readAll(): MutableList<Alarm> {
        return Paper.book().read(KEY_ALARMS, ArrayList())
    }

    @Synchronized
    fun saveAll(alarms: List<Alarm>) {
        Paper.book().write(KEY_ALARMS, alarms)
        this.alarms.clear()
        this.alarms.addAll(readAll())
    }
}
