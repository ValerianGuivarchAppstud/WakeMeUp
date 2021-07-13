package com.vguivarc.wakemeup.data.entity

import com.squareup.moshi.JsonClass
import com.vguivarc.wakemeup.domain.entity.Favorite

@JsonClass(generateAdapter = true)
data class FavoriteRequest(
    val favorite: Favorite,
    val status: Boolean
)
