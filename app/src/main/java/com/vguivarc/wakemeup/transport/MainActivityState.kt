package com.vguivarc.wakemeup.transport

import com.vguivarc.wakemeup.domain.external.entity.UserProfile

data class MainActivityState(
    var isConnected: Boolean = false,
    var newNotification : Boolean = false,
    var newRinging : Boolean = false,
    var nbNewNotification : Int = 0,
    var nbNewRinging : Int = 0
)

sealed class MainActivitySideEffect {
    data class Toast(val textResource: Int) : MainActivitySideEffect()
}
