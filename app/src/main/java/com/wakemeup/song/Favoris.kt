package com.wakemeup.song

import java.io.Serializable
import java.sql.Timestamp

data class Favoris(
    val index : String,
    val idSong : Song,
    val appartientA : String
) : Serializable {
}