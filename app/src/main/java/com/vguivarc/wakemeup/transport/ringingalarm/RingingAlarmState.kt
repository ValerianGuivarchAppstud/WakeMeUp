package com.vguivarc.wakemeup.transport.ringingalarm

import com.vguivarc.wakemeup.domain.external.entity.Alarm

data class RingingAlarmState(
    val ringingAlarm: Alarm? = null
)

sealed class RingingAlarmSideEffect {
    data class Toast(val textResource: Int) : RingingAlarmSideEffect()
}
