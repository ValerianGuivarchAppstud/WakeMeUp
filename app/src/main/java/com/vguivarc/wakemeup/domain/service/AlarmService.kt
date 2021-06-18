package com.vguivarc.wakemeup.domain.service

import androidx.lifecycle.LiveData
import com.vguivarc.wakemeup.domain.entity.Alarm

interface AlarmService {
    fun getAlarms(): LiveData<List<Alarm>>
    fun save(alarm: Alarm)
    fun snoozeAlarm(idAlarm: Int)
    fun stopAlarm(idAlarm: Int)
    fun remove(alarm: Alarm)
    fun switchReveil(alarm: Alarm)
}
