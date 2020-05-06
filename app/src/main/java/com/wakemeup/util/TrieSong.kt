package com.wakemeup.util

import android.content.Context
import com.wakemeup.song.SongHistorique
import com.wakemeup.song.SongIndex

fun trieAlphabetique(historiqueVideoListe : MutableList<SongHistorique>, context : Context){
    historiqueVideoListe.sortWith(object: Comparator<SongHistorique>{
        override fun compare(songH1 : SongHistorique, songH2 : SongHistorique) : Int =
            songH1.song.title.compareTo(songH2.song.title)
    })
    //resetHistoriqueVideo(context)
    //persisteHistoriqueVideo(context,historiqueVideoListe)
}

fun trieDateAjout(historiqueVideoListe : MutableList<SongHistorique>, context : Context){
    historiqueVideoListe.sortWith(object: Comparator<SongHistorique>{
        override fun compare(songH1 : SongHistorique, songH2 : SongHistorique) : Int =
            songH2.index.compareTo(songH1.index)
    })
    //resetHistoriqueVideo(context)
    //persisteHistoriqueVideo(context,historiqueVideoListe)
}