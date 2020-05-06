package com.wakemeup.contact

import com.wakemeup.song.Song
import java.io.Serializable

data class SonnerieRecue(
    val senderId: String,
    val senderName : String,
    val receiverId: String,
    val song: Song,
    val listen : Boolean

    //var notificationRecu: Boolean = false
) : Serializable {

    constructor(sentMusic: SonnerieRecue) : this(sentMusic.senderId, sentMusic.senderName, sentMusic.receiverId, sentMusic.song, sentMusic.listen)

   // constructor(sentMusic: SonnerieRecue) : this(sentMusic.sender,  sentMusic.senderName, sentMusic.receiverId,
        //sentMusic.son, sentMusic.listen)

}

// data class Sonnerie(val song : Song, val idSender : String, val notifVu : Boolean, val utilise : Boolean)
