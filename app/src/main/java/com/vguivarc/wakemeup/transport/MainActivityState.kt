package com.vguivarc.wakemeup.transport

data class MainActivityState(
    var isConnected: Boolean = false,
    var newNotification : Boolean = false,
    var newRinging : Boolean = false,
    var nbNewNotification : Int = 0,
    var nbNewRinging : Int = 0
)

sealed class MainActivitySideEffect {
    data class Toast(val textResource: Int) : MainActivitySideEffect()
    class Navigate(val route: String, val top: Boolean) : MainActivitySideEffect()
    object Ok : MainActivitySideEffect()
}
