package com.vguivarc.wakemeup.transport.favoritelist

import com.vguivarc.wakemeup.domain.external.entity.Favorite

data class FavoriteListState(
    val favoriteList: List<Favorite> = emptyList(),
    val isLoading: Boolean = false,
    val currentSong: Favorite? = null
)

sealed class FavoriteListSideEffect {
    data class Toast(val textResource: Int) : FavoriteListSideEffect()
}
