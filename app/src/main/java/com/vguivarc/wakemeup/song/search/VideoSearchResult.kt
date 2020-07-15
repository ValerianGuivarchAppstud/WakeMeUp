package com.vguivarc.wakemeup.song.search

import com.vguivarc.wakemeup.song.Song

data class VideoSearchResult(
    val searchList: MutableList<Song> = mutableListOf(),
    val error: Exception? = null
)
