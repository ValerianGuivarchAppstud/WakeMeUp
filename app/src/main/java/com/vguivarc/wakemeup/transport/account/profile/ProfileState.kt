package com.vguivarc.wakemeup.transport.account.profile

import com.vguivarc.wakemeup.domain.external.entity.UserProfile

data class AccountState(
    val userProfile: UserProfile? = null,
    val isLoading: Boolean = true
)

sealed class AccountSideEffect {
    data class Toast(val textResource: Int) : AccountSideEffect()
    object Close : AccountSideEffect()
}
