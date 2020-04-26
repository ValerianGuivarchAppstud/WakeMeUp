package com.wakemeup.song

import android.os.Parcel
import android.os.Parcelable
import com.google.api.services.youtube.model.SearchResult
import com.google.api.services.youtube.model.VideoContentDetails
import java.io.Serializable


class Song(
    val id: String,
    val title: String,
    val artist: String,
    var artworkUrl: String,
    val duration: Int,
    var lancement :Int,
    private val rank: Int = 0
) : Comparable<Song>, Parcelable, Serializable {

    //val videoDetails : VideoContentDetails = VideoContentDetails()

    constructor(parcel: Parcel) : this(
        parcel.readString()!!,
        parcel.readString()!!,
        parcel.readString()!!,
        parcel.readString()!!,
        parcel.readInt(),
        parcel.readInt(),
        parcel.readInt()

    )

    override fun writeToParcel(parcel: Parcel, flags: Int) {
        parcel.writeString(id)
        parcel.writeString(title)
        parcel.writeString(artist)
        parcel.writeString(artworkUrl)
        parcel.writeInt(duration)
        parcel.writeInt(lancement)
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
        0,
        0,
        rank
    )

    override fun compareTo(other: Song): Int {
        return other.rank - this.rank
    }

    /*override fun equals(other: Any?): Boolean {

        if (other is Song && other!=null){
            return (other.id == this.id)
        }
        return false
    }
    */


    companion object CREATOR : Parcelable.Creator<Song> {
        private val serialVersionUid : Long = 123456789

        override fun createFromParcel(parcel: Parcel): Song {
            return Song(parcel)
        }

        override fun newArray(size: Int): Array<Song?> {
            return arrayOfNulls(size)
        }
    }


}