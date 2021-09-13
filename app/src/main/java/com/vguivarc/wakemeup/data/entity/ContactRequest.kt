package com.vguivarc.wakemeup.data.entity

import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass
import com.vguivarc.wakemeup.domain.external.entity.Song

@JsonClass(generateAdapter = true)
data class ContactRequest(
    @Json(name = "profileContactId")
    val profileContactId: String,
    @Json(name = "status")
    val status: Boolean
)
