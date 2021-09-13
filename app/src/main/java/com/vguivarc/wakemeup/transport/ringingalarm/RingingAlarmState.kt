package com.vguivarc.wakemeup.transport.ringingalarm

import com.vguivarc.wakemeup.domain.external.entity.Alarm
import com.vguivarc.wakemeup.domain.external.entity.Ringing

data class RingingAlarmState(
    var alarm: Alarm? = null,
    val ringing: Ringing? = null,
    var step: RingingAlarmStep = RingingAlarmStep.WaitingForNextRinging,
    var volume: Int = 50
)

sealed class RingingAlarmSideEffect {
    data class Toast(val textResource: Int) : RingingAlarmSideEffect()
}

enum class RingingAlarmStep {
    WaitingForNextRinging,
    WaitingForYoutubeReader,
    ReadyToPlay,
    Playing,
    NoNextRinging
}
