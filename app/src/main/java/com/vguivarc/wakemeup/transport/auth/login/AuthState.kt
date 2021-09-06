package com.vguivarc.wakemeup.transport.auth.login

import com.vguivarc.wakemeup.data.entity.UserToken
import com.vguivarc.wakemeup.domain.external.entity.UserProfile

data class AuthState(
    val isConnected: Boolean = false,
    val isLoading: Boolean = false
)

sealed class AuthSideEffect {
    data class Toast(val textResource: Int) : AuthSideEffect()
}
