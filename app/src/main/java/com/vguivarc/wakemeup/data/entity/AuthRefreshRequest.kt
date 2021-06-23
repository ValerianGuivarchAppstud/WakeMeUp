package com.vguivarc.wakemeup.data.entity

import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = true)
data class AuthRefreshRequest(
    val refreshToken: String
)
