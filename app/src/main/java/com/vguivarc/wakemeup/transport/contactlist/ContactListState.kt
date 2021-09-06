package com.vguivarc.wakemeup.transport.contactlist

import com.vguivarc.wakemeup.domain.external.entity.Contact

data class ContactListState(
    val contactList: List<Contact> = emptyList<Contact>(),
    val isLoading: Boolean = false,
    val isFabOpen: Boolean = false
)

sealed class ContactListSideEffect {
    data class Toast(val textResource: Int) : ContactListSideEffect()
}
