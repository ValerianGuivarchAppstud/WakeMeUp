package com.vguivarc.wakemeup.util.old

import com.vguivarc.wakemeup.domain.external.entity.Song

data class VideoSearchResult(
    val searchList: MutableList<Song> = mutableListOf(),
    val error: Exception? = null
)
