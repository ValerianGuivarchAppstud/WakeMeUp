package com.vguivarc.wakemeup.data.entity

import com.squareup.moshi.JsonClass
import com.vguivarc.wakemeup.domain.entity.Song

@JsonClass(generateAdapter = true)
data class ContactRequest(
    val profileContactId: String,
    val status: Boolean
)
