package com.vguivarc.wakemeup.domain.external

import com.vguivarc.wakemeup.domain.external.entity.Alarm
import com.vguivarc.wakemeup.domain.external.entity.Contact
import com.vguivarc.wakemeup.domain.internal.IAlarmProvider
import com.vguivarc.wakemeup.domain.internal.IContactProvider

class AlarmInteractor(private val alarmProvider: IAlarmProvider) {
    fun getAlarms(): List<Alarm> {
        return alarmProvider.getAlarms()
    }

    fun save(alarm: Alarm): List<Alarm> {
        return alarmProvider.save(alarm)
    }

    fun add(): List<Alarm> {
        return alarmProvider.addAlarm()
    }

    fun remove(alarm: Alarm): List<Alarm> {
        return alarmProvider.remove(alarm)
    }

    fun switchReveil(alarm: Alarm): List<Alarm> {
        return alarmProvider.switchReveil(alarm)
    }

    fun actionDaySelected(alarm: Alarm, day: Alarm.DaysWeek): List<Alarm> {
        if (alarm.listActifDays.contains(day)) {
            alarm.listActifDays.remove(day)
        } else {
            alarm.listActifDays.add(day)
        }
        return alarmProvider.save(alarm)
    }

    fun actionRepeatSelected(alarm: Alarm, checked: Boolean): List<Alarm> {
        alarm.isRepeated = checked
        return alarmProvider.save(alarm)
    }
}
