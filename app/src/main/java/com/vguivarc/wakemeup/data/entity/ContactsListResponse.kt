package com.vguivarc.wakemeup.data.entity

import com.squareup.moshi.JsonClass
import com.vguivarc.wakemeup.domain.entity.Contact

@JsonClass(generateAdapter = true)
data class ContactsListResponse(
    val contacts: List<Contact>
)
