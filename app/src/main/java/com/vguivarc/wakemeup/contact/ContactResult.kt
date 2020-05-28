package com.vguivarc.wakemeup.contact

data class ContactResult(
    val contactList: MutableMap<String, Contact> = mutableMapOf(),
    val error: Exception? = null
)
