package com.vguivarc.wakemeup.song.favori

import android.os.Parcelable
import com.vguivarc.wakemeup.song.Song
import kotlinx.android.parcel.Parcelize
import java.io.Serializable

@Parcelize
data class Favori(
    val date : String,
    val idSong : String,
    val appartientA : String
) :  Parcelable, Serializable {

    constructor() : this("", "", "")

    var song:Song?=null

/*
    private val songLiveData = MutableLiveData<Song>()
    fun getSongLiveData() : LiveData<Song> = songLiveData

    fun getSong(repo : Repository){
        repo.getSongInFirebase(idSong, songLiveData)
    }*/
}