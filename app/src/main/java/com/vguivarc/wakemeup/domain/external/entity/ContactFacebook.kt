package com.vguivarc.wakemeup.domain.external.entity

import android.os.Parcelable
import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass
import com.vguivarc.wakemeup.domain.external.entity.UserProfile
import kotlinx.android.parcel.Parcelize

@Parcelize
@JsonClass(generateAdapter = true)
data class ContactFacebook(
    val id: String,
    @Json(name = "id_facebook")
    val idFacebook: String,
    val name: String,
    var picture: String? = null,
    @Json(name = "is_contact")
    val isContact: Boolean
) : Parcelable
