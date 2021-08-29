package com.vguivarc.wakemeup.ui.search

import com.hsuaxo.rxtube.YTContent
import com.vguivarc.wakemeup.domain.entity.Song

data class SearchSong(
    val song: Song,
    var isFavorite: Boolean = false
)
