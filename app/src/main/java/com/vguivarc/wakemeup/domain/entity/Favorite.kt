package com.vguivarc.wakemeup.domain.entity

import android.os.Parcelable
import com.squareup.moshi.JsonClass
import com.vguivarc.wakemeup.ui.song.Song
import kotlinx.android.parcel.Parcelize

@Parcelize
@JsonClass(generateAdapter = true)
data class Favorite(
    val id: String,
    val date: String,
    val song: Song?,
    val belongsTo: String
) : Parcelable
