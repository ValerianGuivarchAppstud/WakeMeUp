package com.vguivarc.wakemeup.domain.entity

import java.io.Serializable

data class LienMusicMe(
    val userID : String,
    val username : String,
    val date : Long
) : Serializable{

    @Suppress("unused")
    constructor() : this("", "", 0)

}