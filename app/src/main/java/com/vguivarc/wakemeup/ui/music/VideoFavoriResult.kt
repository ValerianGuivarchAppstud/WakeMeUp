package com.vguivarc.wakemeup.ui.music

import com.vguivarc.wakemeup.domain.entity.Favorite

data class VideoFavoriResult(
    val favoriList: MutableMap<String, Favorite> = mutableMapOf(),
    val error: Exception? = null
)
