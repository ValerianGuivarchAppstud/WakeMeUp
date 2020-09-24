package com.vguivarc.wakemeup.share

import java.io.Serializable

data class LienMusicMe(
    val userID : String,
    val username : String,
    val date : Long
) : Serializable{
    constructor() : this("", "", 0)

}