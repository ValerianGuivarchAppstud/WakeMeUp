package com.vguivarc.wakemeup.ui.search

import com.hsuaxo.rxtube.YTContent

data class SearchSong(
    val ytContent: YTContent,
    val isFavorite: Boolean = false
)
