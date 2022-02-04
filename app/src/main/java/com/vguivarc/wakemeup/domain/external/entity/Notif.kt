package com.vguivarc.wakemeup.domain.external.entity

import android.os.Parcelable
import com.squareup.moshi.JsonClass
import kotlinx.android.parcel.Parcelize
import java.time.ZonedDateTime

@Parcelize
@JsonClass(generateAdapter = true)
data class Notif(
    val id: String,
    val createdDate: ZonedDateTime?,
    val seen: Boolean,
    val type: NotifType,
    val senderId: String = "",
    val receiverId: String = ""
) : Parcelable {
}

enum class NotifType {
    RINGING_USED,
    RINGING_RECEIVED
}