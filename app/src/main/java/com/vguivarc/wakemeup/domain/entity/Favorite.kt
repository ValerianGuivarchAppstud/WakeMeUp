package com.vguivarc.wakemeup.domain.entity

import android.os.Parcelable
import com.squareup.moshi.JsonClass
import kotlinx.android.parcel.Parcelize

@Parcelize
@JsonClass(generateAdapter = true)
data class Favorite(
    val createdAt: String,
    val song: Song
) : Parcelable
