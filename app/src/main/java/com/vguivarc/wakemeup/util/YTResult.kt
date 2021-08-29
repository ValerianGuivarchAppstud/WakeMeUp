package com.vguivarc.wakemeup.util

import com.hsuaxo.rxtube.YTContent
import com.vguivarc.wakemeup.domain.entity.Song

fun YTContent.toSong(): Song {
    return Song(id(), name(), thumbnailUrl())
}