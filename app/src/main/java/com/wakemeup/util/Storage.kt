package com.wakemeup.util

import android.content.Context
import com.wakemeup.song.Song
import java.io.FileInputStream
import java.io.ObjectInputStream
import java.io.ObjectOutputStream

private val TAG = "storage"
private  const val FILENAME_FAVORI= "fav_util.fav"


//Sauvegarde les favoris utilisateur dans un fichier "fav_utilisateur.txt"
fun persisteFavoris(context: Context, songs: MutableList<Song>){
    val fileOutPut = context.openFileOutput(FILENAME_FAVORI, Context.MODE_PRIVATE)
    val outPutStream = ObjectOutputStream(fileOutPut)
    outPutStream.writeObject(songs)
    outPutStream.close()
}

//Charge les favoris utilisateurs du fichier "fav_utilisateur.txt"
fun loadFavoris(context : Context) : MutableList<Song>?{

    //TODO FAIRE UN PUTAIN DE TRY CATCH



    val fileInput : FileInputStream?
    var songs : MutableList<Song>?
    val inputStream : ObjectInputStream
    try {
        fileInput = context.openFileInput(FILENAME_FAVORI)
        inputStream = ObjectInputStream(fileInput)
    }
    catch(e1 : java.lang.reflect.InvocationTargetException){
        return  null
    }
    catch(e2 : java.io.EOFException){
        return  null
    }
    catch(e3 : android.system.ErrnoException){
        return  null
    }
    catch(e4 : java.io.FileNotFoundException){
        return null
    }


    songs = inputStream.readObject() as MutableList<Song>?
    inputStream.close()

    return songs


}

//reset le fichier des favoris
fun resetFavoris(context: Context){
    context.deleteFile(FILENAME_FAVORI)
}