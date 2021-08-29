package com.vguivarc.wakemeup.domain.entity

import android.os.Parcelable
import com.squareup.moshi.JsonClass
import kotlinx.android.parcel.Parcelize

@Parcelize
@JsonClass(generateAdapter = true)
class Song(
    val id: String = "",
    val title: String = "",
    var artworkUrl: String = ""
) : Comparable<Song>, Parcelable {

    override fun toString(): String {
        return "$id : $title"
    }

    override fun compareTo(other: Song): Int {
        return other.title.hashCode() - this.title.hashCode()
    }
}
