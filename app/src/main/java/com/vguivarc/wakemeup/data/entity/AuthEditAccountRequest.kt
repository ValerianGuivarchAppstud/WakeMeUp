package com.vguivarc.wakemeup.data.entity

import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = true)
data class AuthEditAccountRequest(
    @Json(name = "pseudo")
    val username: String,
    val email: String
)
