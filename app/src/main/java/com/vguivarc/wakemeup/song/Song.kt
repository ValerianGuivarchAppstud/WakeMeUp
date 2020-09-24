package com.vguivarc.wakemeup.song

import android.os.Parcel
import android.os.Parcelable
import com.google.api.services.youtube.model.SearchResult
import java.io.Serializable


class Song(
    val id: String= "",
    val title: String ="",
    var artworkUrl: String = ""
) : Comparable<Song>, Parcelable, Serializable {

    override fun toString(): String {
        return "$id : $title"
    }

    constructor(parcel: Parcel) : this(
        parcel.readString()!!,
        parcel.readString()!!,
        parcel.readString()!!
    )



    override fun writeToParcel(parcel: Parcel, flags: Int) {
        parcel.writeString(id)
        parcel.writeString(title)
        parcel.writeString(artworkUrl)
        //parcel.writeInt(lancement)
        //parcel.writeInt(rank)
    }

    override fun describeContents(): Int {
        return 0
    }

    constructor(result: SearchResult) : this(
        result.id.videoId,
        result.snippet.title,
        result.snippet.thumbnails.medium.url
    )

    override fun compareTo(other: Song): Int {
        return other.title.hashCode() - this.title.hashCode()
    }

    /*override fun equals(other: Any?): Boolean {

        if (other is Song && other!=null){
            return (other.id == this.id)
        }
        return false
    }
    */


    companion object CREATOR : Parcelable.Creator<Song> {
        private const val serialVersionUid : Long = 123456789

        override fun createFromParcel(parcel: Parcel): Song {
            return Song(parcel)
        }

        override fun newArray(size: Int): Array<Song?> {
            return arrayOfNulls(size)
        }
    }


}