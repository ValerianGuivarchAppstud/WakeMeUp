package com.wakemeup.contact

import com.wakemeup.song.Song

import java.io.Serializable

class SonneriePassee(val sonnerieId : String, val senderId: String, val senderName : String, val receiverId : String
                     , val song: Song, val listen : Boolean) : Serializable {
/*
    constructor(sentMusic: SonnerieEnAttente) : this(
        sentMusic.senderId,
        sentMusic.senderName,
        sentMusic.receiverId,
        sentMusic.song,
        sentMusic.listen
    )*/

}

// data class Sonnerie(val song : Song, val idSender : String, val notifVu : Boolean, val utilise : Boolean)
