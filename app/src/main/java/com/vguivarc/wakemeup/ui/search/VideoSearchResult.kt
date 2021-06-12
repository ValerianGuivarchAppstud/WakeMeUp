package com.vguivarc.wakemeup.ui.search

import com.vguivarc.wakemeup.ui.song.Song

data class VideoSearchResult(
    val searchList: MutableList<Song> = mutableListOf(),
    val error: Exception? = null
)
