package com.vguivarc.wakemeup.data.entity

import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = true)
data class FacebookAuthRequest(
    val type: String = "FACEBOOK",
    @Json(name = "socialToken")
    val token: String
)
