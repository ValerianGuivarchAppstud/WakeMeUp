package com.vguivarc.wakemeup.data.entity

import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = true)
data class AuthSignupRequest(
    val email: String,
    val password: String,
    @Json(name = "pseudo")
    val nickname: String
)
