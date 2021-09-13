package com.vguivarc.wakemeup.domain.external.entity

import android.os.Parcelable
import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass
import kotlinx.android.parcel.Parcelize

@Parcelize
@JsonClass(generateAdapter = true)
data class Contact(
    @Json(name = "idProfile")
    val idProfile: String,
    @Json(name = "username")
    val username: String,
    @Json(name = "picture")
    var pictureUrl: String? = null,
    @Json(name = "nbRingingSent")
    val nbSongSent: Int,
    @Json(name = "nbRingingReceived")
    val nbSongReceived: Int,
) : Parcelable
