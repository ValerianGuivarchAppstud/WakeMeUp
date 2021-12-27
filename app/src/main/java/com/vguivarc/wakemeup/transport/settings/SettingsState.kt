package com.vguivarc.wakemeup.transport.settings

enum class SETTINGS(
    val text: String
){
    LOGIN("Se connecter"),
    ACCOUNT("Mon compte"),
    LOGOUT("Se déconnecter")
}

data class SettingsState(
    val connected: Boolean = false
)

sealed class SettingsSideEffect {
    data class Toast(val textResource: Int) : SettingsSideEffect()
    data class NavigationAction(val route: String) : SettingsSideEffect()
    object Ok : SettingsSideEffect()

}
