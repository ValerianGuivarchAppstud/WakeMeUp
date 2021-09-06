package com.vguivarc.wakemeup.data.entity

import com.squareup.moshi.JsonClass
import com.vguivarc.wakemeup.domain.external.entity.ContactFacebook

@JsonClass(generateAdapter = true)
data class ContactFacebookListResponse(
    val contacts: List<ContactFacebook>
)
