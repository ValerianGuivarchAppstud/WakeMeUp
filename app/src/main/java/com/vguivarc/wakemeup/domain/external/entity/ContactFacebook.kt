package com.vguivarc.wakemeup.domain.external.entity

import android.os.Parcelable
import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass
import kotlinx.android.parcel.Parcelize

@Parcelize
@JsonClass(generateAdapter = true)
data class ContactFacebook(
    @Json(name = "idProfile")
    val idProfile: String,
    @Json(name = "idFacebook")
    val idFacebook: String,
    @Json(name = "username")
    val username: String,
    @Json(name = "pictureUrl")
    var pictureUrl: String? = null,
    @Json(name = "contact")
    val contact: Boolean
) : Parcelable
