package com.vguivarc.wakemeup.domain.entity

import android.os.Parcelable
import com.squareup.moshi.JsonClass
import kotlinx.android.parcel.Parcelize

@Parcelize
@JsonClass(generateAdapter = true)
data class Contact(
    val contact: UserProfile,
    val nbSongSent: Int,
    val nbSongReceived: Int
) : Parcelable
