package com.vguivarc.wakemeup.transport.contact.contactlist

import com.vguivarc.wakemeup.domain.external.entity.Contact

data class ContactListState(
    val contactList: List<Contact> = emptyList(),
    val isLoading: Boolean = false,
    val isFabOpen: Boolean = false
)

sealed class ContactListScreenSideEffect {
    data class NavigateToContactDetail(val contact: Contact) : ContactListScreenSideEffect()
    object NavigateToAddFacebookContact: ContactListScreenSideEffect()
    data class Toast(val textResource: Int) : ContactListScreenSideEffect()
    object Ok: ContactListScreenSideEffect()

}
