package com.vguivarc.wakemeup.transport.account.login

// TODO add deal with error
data class AuthState(
    val mail: String = "valtest@test.com",
    val password: String = "password",
    val passwordVisibility: Boolean = false,
    val isConnected: Boolean = false,
    val isLoading: Boolean = false
)

sealed class AuthSideEffect {
    data class Toast(val textResource: Int) : AuthSideEffect()
    data class NavigationToRegister(val mail: String) : AuthSideEffect()
    object LoginFacebook : AuthSideEffect()
    object Close : AuthSideEffect()
    object Ok : AuthSideEffect()
}
