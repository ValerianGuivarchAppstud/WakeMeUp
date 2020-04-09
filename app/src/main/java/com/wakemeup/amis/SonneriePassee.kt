package com.wakemeup.amis

import java.io.Serializable


class SonneriePassee(val sender: String, val receiver: String, val song: String) : Serializable {

    constructor(sentMusic: SonnerieEnAttente) : this(
        sentMusic.sender,
        sentMusic.receiver,
        sentMusic.song
    )

}

// data class Sonnerie(val song : Song, val idSender : String, val notifVu : Boolean, val utilise : Boolean)
