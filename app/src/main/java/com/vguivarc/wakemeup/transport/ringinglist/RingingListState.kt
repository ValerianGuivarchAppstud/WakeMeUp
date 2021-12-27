package com.vguivarc.wakemeup.transport.ringinglist

import com.vguivarc.wakemeup.domain.external.entity.Ringing

data class RingingListState(
    val ringingList: List<Ringing> = emptyList(),
    val isLoading: Boolean = false
)

sealed class RingingListSideEffect {
    data class Toast(val textResource: Int) : RingingListSideEffect()
    object Ok : RingingListSideEffect()
}
