package com.vguivarc.wakemeup.transport.music

import com.vguivarc.wakemeup.domain.external.entity.Favorite

data class VideoFavoriResult(
    val favoriList: MutableMap<String, Favorite> = mutableMapOf(),
    val error: Exception? = null
)
