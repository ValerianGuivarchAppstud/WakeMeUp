package com.vguivarc.wakemeup.data.entity

import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = true)
data class UserToken(
    val accessToken: String,
    val refreshToken: String,
    val accessTokenExpiration: Long, // timestamp
    val refreshTokenExpiration: Long // timestamp
)
