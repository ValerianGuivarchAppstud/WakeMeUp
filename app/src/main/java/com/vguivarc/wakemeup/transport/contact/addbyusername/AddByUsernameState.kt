package com.vguivarc.wakemeup.transport.contact.addbyusername

import com.vguivarc.wakemeup.domain.external.entity.UserProfile

data class AddByUsernameState(
    val searchText: String = "test1",
    val isSearchLoading: Boolean = false,
    val isContactLoading: Boolean = false,
    val showBeforeSearch: Boolean = true,
    val showEmptyResult: Boolean = false,
    val user: UserProfile? = null,
    val isContact: Boolean = false
)

sealed class AddByUsernameSideEffect {
    data class Toast(val textResource: Int) : AddByUsernameSideEffect()
    object Ok : AddByUsernameSideEffect()
}
