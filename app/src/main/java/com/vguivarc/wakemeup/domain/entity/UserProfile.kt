package com.vguivarc.wakemeup.domain.entity

import android.os.Parcelable
import com.squareup.moshi.JsonClass
import kotlinx.android.parcel.Parcelize

@Parcelize
@JsonClass(generateAdapter = true)
class UserProfile(
    val profileId: String,
    val nickname: String,
    val email: String? = null,
    val facebookId: String? =null,
    val imageUrl: String? = null
) : Parcelable
