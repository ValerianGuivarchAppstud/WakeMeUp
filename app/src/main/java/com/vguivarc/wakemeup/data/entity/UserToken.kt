package com.vguivarc.wakemeup.data.entity

import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = true)
data class UserToken(

    @Json(name="accessToken")
    val accessToken: String,

    @Json(name="refreshToken")
    val refreshToken: String,

    @Json(name="accessTokenExpiration")
    val accessTokenExpiration: Long, // timestamp

    @Json(name="refreshTokenExpiration")
    val refreshTokenExpiration: Long // timestamp
)
