package com.vguivarc.wakemeup.transport.search

import com.vguivarc.wakemeup.domain.external.entity.SearchSong

data class SearchSongListState(
    val searchSongList: List<SearchSong> = emptyList(),
    val isLoading: Boolean = false,
    val showBeforeSearch: Boolean = true,
    val showEmptyResult: Boolean = false,
    val currentSong: SearchSong? = null
)

sealed class SearchSongListSideEffect {
    data class Toast(val textResource: Int) : SearchSongListSideEffect()
}
