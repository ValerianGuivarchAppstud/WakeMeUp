package com.vguivarc.wakemeup.domain.entity

import android.os.Parcelable
import com.squareup.moshi.JsonClass
import kotlinx.android.parcel.Parcelize

@Parcelize
@JsonClass(generateAdapter = true)
data class Ringing(
    var song: Song?,
    val dateSent: Long,
    val listened: Boolean,
    val idReceiver: String,
    val senderId: String,
    val senderName: String

    // var notificationRecu: Boolean = false
) : Parcelable {

    constructor(sentMusic: Ringing) : this(
        sentMusic.song,
        sentMusic.dateSent,
        sentMusic.listened,
        sentMusic.idReceiver,
        sentMusic.senderId,
        sentMusic.senderName
    )

    constructor() : this(
        null, 0, false, "", "", ""
    )

    lateinit var idSonnerie: String
    var sender: UserProfile? = null
}
