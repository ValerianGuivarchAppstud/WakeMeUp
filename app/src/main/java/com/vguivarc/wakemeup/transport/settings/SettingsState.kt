package com.vguivarc.wakemeup.transport.settings

data class SettingsState(
    val connected: Boolean = false
)

sealed class SettingsSideEffect {
    data class Toast(val textResource: Int) : SettingsSideEffect()
}
