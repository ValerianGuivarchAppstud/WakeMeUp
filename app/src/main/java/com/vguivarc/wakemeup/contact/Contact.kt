package com.vguivarc.wakemeup.contact

import com.vguivarc.wakemeup.connect.UserModel
import java.io.Serializable


data class Contact(
    val idContact: String,
    val ajoutePar: String
) : Serializable {

    constructor() : this("", "")

    var user: UserModel? = null
}