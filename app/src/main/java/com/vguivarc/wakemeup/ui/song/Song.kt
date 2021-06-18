package com.vguivarc.wakemeup.ui.song

import android.os.Parcelable
import com.hsuaxo.rxtube.YTContent
import com.squareup.moshi.JsonClass
import kotlinx.android.parcel.Parcelize


@Parcelize
@JsonClass(generateAdapter = true)
class Song(
    val id: String= "",
    val title: String ="",
    var artworkUrl: String = ""
) : Comparable<Song>, Parcelable {

    override fun toString(): String {
        return "$id : $title"
    }

    constructor(result: YTContent) : this(
        result.id(),
        result.name(),
        result.thumbnailUrl()
    )

    override fun compareTo(other: Song): Int {
        return other.title.hashCode() - this.title.hashCode()
    }
}