package com.vguivarc.wakemeup.data.entity

import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = true)
data class ShareRingingRequest(
    val contactId: Int? = null,
    val songId: Int? = null
)
