package com.vguivarc.wakemeup.data.entity

import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = true)
data class AuthEmailNotifParamRequest(
    val status: Boolean
)
