package com.wakemeup.util

import android.content.Context
import com.wakemeup.song.Song
import java.io.FileInputStream
import java.io.ObjectInputStream
import java.io.ObjectOutputStream

private val TAG = "storage"
private  const val FILENAME_FAVORI= "fav_util.fav"
private  const val FILENAME_HISTORIQUE= "hist_util.hist"
private  const val FILENAME_HISTORIQUEVIDEO= "hist_video_util.hist"

//charge n'importe quel type d'objet du fichier filename
fun <T> load(context: Context, filename : String) : T?{
    val fileInput : FileInputStream?
    var songs : T?
    val inputStream : ObjectInputStream
    try {
        fileInput = context.openFileInput(filename)
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


    songs = inputStream.readObject() as T?
    inputStream.close()

    return songs
}

//Sauvegarde n'importe quel type d'objet dans un fichier filename
fun <T> persiste(context: Context, obj : T, filename: String){
    val fileOutPut = context.openFileOutput(filename, Context.MODE_PRIVATE)
    val outPutStream = ObjectOutputStream(fileOutPut)
    outPutStream.writeObject(obj)
    outPutStream.close()
}

//--------------------------------------------------------------------------------------


//Charge les favoris utilisateurs du fichier "fav_util.fav"
fun loadFavoris(context : Context) : MutableList<Song>?{
    return load(context, FILENAME_FAVORI)
}

//Sauvegarde les favoris utilisateur dans un fichier "fav_util.fav"
fun persisteFavoris(context: Context, songs: MutableList<Song>){
    persiste(context, songs, FILENAME_FAVORI)
}


//reset le fichier des favoris
fun resetFavoris(context: Context){
    context.deleteFile(FILENAME_FAVORI)
}

//--------------------------------------------------------------------------------------

//Charge les recherches utilisateurs du fichier "hist_util.hist"
fun loadHistorique(context : Context) : MutableList<String>?{
    return load(context, FILENAME_HISTORIQUE)
}

//Sauvegarde les recheche utilisateur dans un fichier "hist_util.hist"
fun persisteHistorique(context: Context, historique: MutableList<String>){
    persiste(context, historique, FILENAME_HISTORIQUE)
}

//reset le fichier des historiques
fun resetHistorique(context: Context){
    context.deleteFile(FILENAME_HISTORIQUE)
}

//--------------------------------------------------------------------------------------


//Charge les recherches utilisateurs du fichier "hist_video_util.hist"
fun loadHistoriqueVideo(context : Context) : MutableList<Song>?{
    return load(context, FILENAME_HISTORIQUEVIDEO)
}

//Sauvegarde les recheche utilisateur dans un fichier "hist_video_util.hist"
fun persisteHistoriqueVideo(context: Context, historiqueVideo: MutableList<Song>){
    persiste(context, historiqueVideo, FILENAME_HISTORIQUEVIDEO)
}

//reset le fichier des historiques des videos
fun resetHistoriqueVideo(context: Context){
    context.deleteFile(FILENAME_HISTORIQUEVIDEO)
}

//--------------------------------------------------------------------------------------