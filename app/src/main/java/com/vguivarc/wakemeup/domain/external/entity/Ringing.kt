package com.vguivarc.wakemeup.domain.external.entity

import android.os.Parcelable
import com.squareup.moshi.JsonClass
import kotlinx.android.parcel.Parcelize
import java.time.ZonedDateTime

@Parcelize
@JsonClass(generateAdapter = true)
data class Ringing(
    val id: String,
    var song: Song?,
    //TODO add (mais là ça marche pas) val dateSent: ZonedDateTime,
    val listened: Boolean,
    val seen: Boolean,
    val receiverId: String,
    val senderId: String?,
    val senderName: String

    // var notificationRecu: Boolean = false
) : Parcelable {
    //TODO delete ?
//    lateinit var idSonnerie: String
  //  var sender: UserProfile? = null
}
