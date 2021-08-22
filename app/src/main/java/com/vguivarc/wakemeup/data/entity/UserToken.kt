package com.vguivarc.wakemeup.data.entity

import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = true)
data class UserToken(

    @Json(name="access_token")
    val accessToken: String,

    @Json(name="refresh_token")
    val refreshToken: String,

    @Json(name="access_token_expiration")
    val accessTokenExpiration: Long, // timestamp

    @Json(name="refresh_token_expiration")
    val refreshTokenExpiration: Long // timestamp
)
