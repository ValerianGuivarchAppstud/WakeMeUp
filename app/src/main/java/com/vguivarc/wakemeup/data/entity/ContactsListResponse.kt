package com.vguivarc.wakemeup.data.entity

import com.squareup.moshi.JsonClass
import com.vguivarc.wakemeup.domain.external.entity.Contact

@JsonClass(generateAdapter = true)
data class ContactsListResponse(
    val contacts: List<Contact>
)
