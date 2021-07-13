package com.vguivarc.wakemeup.domain.entity

import android.os.Parcelable
import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass
import kotlinx.android.parcel.Parcelize

@Parcelize
@JsonClass(generateAdapter = true)
class UserProfile(
    val id: String,
    val nickname: String,
    val email: String? = null,
    val facebookId: String?,
    val imageUrl: String
) : Parcelable