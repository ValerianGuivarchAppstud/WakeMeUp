package com.vguivarc.wakemeup.transport.search

import com.vguivarc.wakemeup.domain.external.entity.SearchSong
import com.vguivarc.wakemeup.transport.contact.contactlist.ContactListScreenSideEffect

data class SearchSongListState(
    val searchSongList: List<SearchSong> = emptyList(),
    val searchText: String = "",
    val isLoading: Boolean = false,
    val showBeforeSearch: Boolean = true,
    val showEmptyResult: Boolean = false,
    val currentSong: SearchSong? = null
)

sealed class SearchSongListSideEffect {
    data class Toast(val textResource: Int) : SearchSongListSideEffect()
    object Ok: SearchSongListSideEffect()
}
