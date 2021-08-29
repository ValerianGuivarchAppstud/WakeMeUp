package com.vguivarc.wakemeup.data.entity

import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = true)
data class FacebookAuthRequest(
    @Json(name = "facebookToken")
    val token: String
)
