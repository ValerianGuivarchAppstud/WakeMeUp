package com.wakemeup.util

import android.content.Context
import com.wakemeup.MainActivity
import com.wakemeup.song.Song
import com.wakemeup.song.VideoFragment
import java.io.ObjectInputStream
import java.io.ObjectOutputStream

private val TAG = "storage"
private  const val FILENAME_FAVORI= "fav_utilisateur.txt"

fun persisteFavoris(context: Context, songs: Song?){
    val fileOutPut = context!!.openFileOutput(FILENAME_FAVORI, Context.MODE_PRIVATE)
    val outPutStream = ObjectOutputStream(fileOutPut)
    outPutStream.writeObject(songs)
    outPutStream.close()
}

fun loadFavoris(context : Context) : Song{
    val fileInput = context!!.openFileInput(FILENAME_FAVORI)
    val inputStream = ObjectInputStream(fileInput)
    val songs = inputStream.readObject() as Song
    inputStream.close()
    return songs
}