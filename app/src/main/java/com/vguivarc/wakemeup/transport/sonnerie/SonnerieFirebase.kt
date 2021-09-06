package com.vguivarc.wakemeup.transport.sonnerie

import java.io.Serializable

data class SonnerieFirebase(
    val IdSong: String = "",
    val dateEnvoie: String = "",
    val ecoutee: Boolean = false,
    val idReceiver: String = "",
    val senderId: String = "",
    val senderName: String = ""

    // var notificationRecu: Boolean = false
) : Serializable {

    constructor(sentMusic: SonnerieFirebase) : this(
        sentMusic.IdSong,
        sentMusic.dateEnvoie,
        sentMusic.ecoutee,
        sentMusic.idReceiver,
        sentMusic.senderId,
        sentMusic.senderName

    )
}
