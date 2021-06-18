package com.vguivarc.wakemeup.data.repository

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.vguivarc.wakemeup.domain.entity.Alarm
import com.vguivarc.wakemeup.domain.service.AlarmService
import io.paperdb.Paper
import java.util.*

class AlarmRepository : AlarmService {

    companion object {
        private const val KEY_ALARMS = "alarms_v0_1"
    }

    private val alarms = MutableLiveData<List<Alarm>>()

    init {
        val alarmList = readAll()
        Alarm.idCount = alarmList.maxByOrNull { it -> it.idAlarms }?.idAlarms ?: 0
        alarms.value = alarmList
    }

    override fun getAlarms(): LiveData<List<Alarm>> = alarms

    override fun save(alarm: Alarm) {
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
    }

    override fun snoozeAlarm(idReveil: Int) {
        readAll()[idReveil].snooze()
    }

    override fun stopAlarm(idReveil: Int) {
        readAll()[idReveil].stop()
    }

    override fun remove(alarm: Alarm) {
        val alarms = readAll()

        val existingAlarm = alarms.find { it.idAlarms == alarm.idAlarms }
        existingAlarm?.let {
            it.cancelAlarm() // check
            alarms.remove(it)
        }
        saveAll(alarms)
    }

    override fun switchReveil(alarm: Alarm) {
        readAll().find { it.idAlarms == alarm.idAlarms }?.switch()
    }

    @Synchronized
    fun readAll(): MutableList<Alarm> {
        return Paper.book().read(KEY_ALARMS, ArrayList())
    }

    @Synchronized
    fun saveAll(alarms: List<Alarm>) {
        Paper.book().write(KEY_ALARMS, alarms)
        this.alarms.value = readAll()
    }
}
