package com.vguivarc.wakemeup.data.entity

import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass
import com.vguivarc.wakemeup.domain.entity.Favorite

@JsonClass(generateAdapter = true)
data class FavoritesListResponse(
    @Json(name = "favorites")
    val favorites: List<Favorite>
)
