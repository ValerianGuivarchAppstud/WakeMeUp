package com.vguivarc.wakemeup.contact

import com.google.firebase.auth.FirebaseUser
import java.io.Serializable


data class Contact(
    val idContact: String,
    val ajoutePar: String
) : Serializable {

    constructor() : this("", "")

    var user: FirebaseUser? = null
}