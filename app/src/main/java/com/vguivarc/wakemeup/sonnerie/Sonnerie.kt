package com.vguivarc.wakemeup.sonnerie

import com.vguivarc.wakemeup.connect.UserModel
import com.vguivarc.wakemeup.song.Song
import java.io.Serializable

data class Sonnerie(
    var idSong: String,
    val dateEnvoie: Long,
    val ecoutee : Boolean,
    val idReceiver: String,
    val senderId: String,
    val senderName : String

    //var notificationRecu: Boolean = false
) : Serializable {

    constructor(sentMusic: Sonnerie) : this(
        sentMusic.idSong,
        sentMusic.dateEnvoie,
        sentMusic.ecoutee,
        sentMusic.idReceiver,
        sentMusic.senderId,
        sentMusic.senderName)

    constructor() : this(
        "", 0, false, "", "", ""
    )
    lateinit var idSonnerie:String
    var song:Song?=null
    var sender: UserModel?=null


   // constructor(sentMusic: SonnerieRecue) : this(sentMusic.sender,  sentMusic.senderName, sentMusic.receiverId,
        //sentMusic.son, sentMusic.listen)

}

// data class Sonnerie(val song : Song, val idSender : String, val notifVu : Boolean, val utilise : Boolean)
