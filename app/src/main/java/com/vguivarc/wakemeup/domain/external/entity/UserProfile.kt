package com.vguivarc.wakemeup.domain.external.entity

import android.os.Parcelable
import com.squareup.moshi.JsonClass
import kotlinx.android.parcel.Parcelize

@Parcelize
@JsonClass(generateAdapter = true)
class UserProfile(
    val profileId: String,
    val email: String? = null,
    val username: String,
    val facebookId: String? =null,
    val imageUrl: String? = null
) : Parcelable
