package com.wakemeup.share

import java.io.Serializable
import java.sql.Timestamp
import java.time.LocalDateTime
import java.time.LocalTime
import java.util.*

data class DemandeMusique(
    val userID : String,
    val username : String,
    val date : Timestamp
) : Serializable{
    constructor(demande: DemandeMusique) : this(demande.userID, demande.username, demande.date)
}