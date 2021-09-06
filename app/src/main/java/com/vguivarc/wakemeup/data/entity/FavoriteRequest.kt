package com.vguivarc.wakemeup.data.entity

import com.squareup.moshi.JsonClass
import com.vguivarc.wakemeup.domain.external.entity.Song

@JsonClass(generateAdapter = true)
data class FavoriteRequest(
    val song: Song,
    val status: Boolean
)
