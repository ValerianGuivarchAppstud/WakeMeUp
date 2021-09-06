package com.vguivarc.wakemeup.domain.external.entity

import android.os.Parcelable
import com.squareup.moshi.JsonClass
import kotlinx.android.parcel.Parcelize

@Parcelize
@JsonClass(generateAdapter = true)
data class Contact(
    val userProfile: UserProfile,
    val nbSongSent: Int,
    val nbSongReceived: Int
) : Parcelable
