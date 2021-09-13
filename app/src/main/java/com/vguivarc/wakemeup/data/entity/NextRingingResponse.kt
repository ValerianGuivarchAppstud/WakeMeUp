package com.vguivarc.wakemeup.data.entity

import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass
import com.vguivarc.wakemeup.domain.external.entity.Ringing

@JsonClass(generateAdapter = true)
data class NextRingingResponse(
    @Json(name = "nextRinging")
    val nextRinging: Ringing?
)
