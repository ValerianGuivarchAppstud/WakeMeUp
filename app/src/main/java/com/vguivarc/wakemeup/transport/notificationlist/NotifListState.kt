package com.vguivarc.wakemeup.transport.notificationlist

import com.vguivarc.wakemeup.domain.external.entity.Notif

data class NotifListState(
    val notificationList: List<Notif> = emptyList(),
    val isLoading: Boolean = false
)

sealed class NotifListSideEffect {
    data class Toast(val textResource: Int) : NotifListSideEffect()
    object Ok : NotifListSideEffect()
}
