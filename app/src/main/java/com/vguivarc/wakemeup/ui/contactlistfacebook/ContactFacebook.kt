package com.vguivarc.wakemeup.ui.contactlistfacebook

import android.os.Parcelable
import com.squareup.moshi.JsonClass
import com.vguivarc.wakemeup.domain.entity.UserProfile
import kotlinx.android.parcel.Parcelize

@Parcelize
@JsonClass(generateAdapter = true)
data class ContactFacebook(
    val idFacebook: String,
    val isContact: Boolean
) : Parcelable
