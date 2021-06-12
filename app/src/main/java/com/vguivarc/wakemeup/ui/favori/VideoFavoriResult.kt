package com.vguivarc.wakemeup.ui.favori

import com.vguivarc.wakemeup.domain.entity.Favorite

data class VideoFavoriResult(
    val favoriList: MutableMap<String, Favorite> = mutableMapOf(),
    val error: Exception? = null
)
