package com.vguivarc.wakemeup.transport.account

import com.vguivarc.wakemeup.domain.external.entity.UserProfile

data class AccountState(
    val userProfile: UserProfile? = null,
    val isConnected : Boolean = true,
    val isLoading: Boolean = false
)

sealed class AccountSideEffect {
    data class Toast(val textResource: Int) : AccountSideEffect()
}
