package com.vguivarc.wakemeup.domain.internal

import androidx.lifecycle.LiveData
import com.vguivarc.wakemeup.domain.external.entity.Alarm

interface IAlarmProvider {
    fun addAlarm(): List<Alarm>
    fun getAlarms(): List<Alarm>
    fun save(alarm: Alarm): List<Alarm>
    fun snoozeAlarm(idAlarm: Int): List<Alarm>
    fun stopAlarm(idAlarm: Int): List<Alarm>
    fun remove(alarm: Alarm): List<Alarm>
}
