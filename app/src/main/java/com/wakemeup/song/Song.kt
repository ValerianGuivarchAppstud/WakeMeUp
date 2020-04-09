package com.wakemeup.song

import android.os.Parcel
import android.os.Parcelable
import com.google.api.services.youtube.model.SearchResult


class Song(
    val id: String,
    val title: String,
    val artist: String,
    val artworkUrl: String,
    val duration: Int,
    private val rank: Int = 0
) : Comparable<Song>, Parcelable {
    constructor(parcel: Parcel) : this(
        parcel.readString()!!,
        parcel.readString()!!,
        parcel.readString()!!,
        parcel.readString()!!,
        parcel.readInt(),
        parcel.readInt()
    )

    override fun writeToParcel(parcel: Parcel, flags: Int) {
        parcel.writeString(id)
        parcel.writeString(title)
        parcel.writeString(artist)
        parcel.writeString(artworkUrl)
        parcel.writeInt(duration)
        parcel.writeInt(rank)
    }

    override fun describeContents(): Int {
        return 0
    }

    constructor(result: SearchResult, rank: Int) : this(
        result.id.videoId,
        result.snippet.title,
        result.snippet.channelTitle,
        result.snippet.thumbnails.medium.url,
        300,
        rank
    )

    override fun compareTo(other: Song): Int {
        return other.rank - this.rank
    }

    companion object CREATOR : Parcelable.Creator<Song> {
        override fun createFromParcel(parcel: Parcel): Song {
            return Song(parcel)
        }

        override fun newArray(size: Int): Array<Song?> {
            return arrayOfNulls(size)
        }
    }


}