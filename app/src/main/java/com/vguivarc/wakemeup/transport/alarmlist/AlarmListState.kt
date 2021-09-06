package com.vguivarc.wakemeup.transport.alarmlist

import com.vguivarc.wakemeup.domain.external.entity.Alarm

data class AlarmListState(
    val alarmList: List<Alarm> = emptyList(),
    val currentEditingAlarm: Alarm? = null
)

sealed class AlarmListSideEffect {
    data class Toast(val textResource: Int) : AlarmListSideEffect()
}
