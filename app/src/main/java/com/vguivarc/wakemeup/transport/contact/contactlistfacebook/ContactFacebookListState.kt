package com.vguivarc.wakemeup.transport.contact.contactlistfacebook

import com.vguivarc.wakemeup.domain.external.entity.ContactFacebook

data class ContactFacebookListState(
    val contactFacebookList: List<ContactFacebook> = emptyList(),
    val isLoading: Boolean = false
)

sealed class ContactFacebookListSideEffect {
    data class Toast(val textResource: Int) : ContactFacebookListSideEffect()
    object Ok : ContactFacebookListSideEffect()
}
