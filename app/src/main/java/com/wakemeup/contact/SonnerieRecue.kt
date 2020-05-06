package com.wakemeup.contact

import java.io.Serializable

data class SonnerieRecue(
    val sender: String,
    val receiver: String,
    val song: String,
    var notificationRecu: Boolean = false
) : Serializable {

    constructor(sentMusic: SonnerieRecue) : this(sentMusic.sender, sentMusic.receiver, sentMusic.song)

}

// data class Sonnerie(val song : Song, val idSender : String, val notifVu : Boolean, val utilise : Boolean)
