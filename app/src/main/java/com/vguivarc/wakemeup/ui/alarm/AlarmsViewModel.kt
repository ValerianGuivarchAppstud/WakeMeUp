package com.vguivarc.wakemeup.ui.alarm

import androidx.lifecycle.LiveData
import androidx.lifecycle.MediatorLiveData
import com.vguivarc.wakemeup.base.BaseViewModel
import com.vguivarc.wakemeup.domain.entity.Alarm
import com.vguivarc.wakemeup.domain.service.AlarmService

class AlarmsViewModel(private val alarmService: AlarmService) : BaseViewModel() {

    private var _alarmsList = MediatorLiveData<List<Alarm>>()

    val alarmsList: LiveData<List<Alarm>> = _alarmsList

    init {
        _alarmsList.addSource(alarmService.getAlarms()) { alarms ->
            _alarmsList.value = alarms
        }
    }

    fun save(alarm: Alarm) {
        alarmService.save(alarm)
    }

    fun remove(alarm: Alarm) {
        alarmService.remove(alarm)
    }

    fun switchAlarm(alarm: Alarm) {
        alarmService.switchReveil(alarm)
    }

    fun snoozeAlarm(idAlarm: Int) {
        alarmService.snoozeAlarm(idAlarm)
    }

    fun stopAlarm(idAlarm: Int) {
        alarmService.stopAlarm(idAlarm)
    }
}
