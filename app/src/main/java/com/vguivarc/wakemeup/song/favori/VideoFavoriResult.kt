package com.vguivarc.wakemeup.song.favori

data class VideoFavoriResult(
    val favoriList: MutableMap<String, Favori> = mutableMapOf(),
    val error: Exception? = null
)
