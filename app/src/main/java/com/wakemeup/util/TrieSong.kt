package com.wakemeup.util

import android.content.Context
import com.wakemeup.song.Favoris
import com.wakemeup.song.SongHistorique
import com.wakemeup.song.SongIndex

fun trieAlphabetique(historiqueVideoListe : MutableList<SongHistorique>, context : Context){
    historiqueVideoListe.sortWith(object: Comparator<SongHistorique>{
        override fun compare(songH1 : SongHistorique, songH2 : SongHistorique) : Int =
            songH1.song.title.compareTo(songH2.song.title)
    })
}

fun trieDateAjout(historiqueVideoListe : MutableList<SongHistorique>, context : Context){
    historiqueVideoListe.sortWith(object: Comparator<SongHistorique>{
        override fun compare(songH1 : SongHistorique, songH2 : SongHistorique) : Int =
            songH2.index.compareTo(songH1.index)
    })
}

fun trieAlphabetiqueFavoris(favorisVideoListe : MutableList<Favoris>, context : Context){
    favorisVideoListe.sortWith(object: Comparator<Favoris>{
        override fun compare(songH1 : Favoris, songH2 : Favoris) : Int =
            songH1.idSong.title.compareTo(songH2.idSong.title)
    })
}

fun trieDateAjoutFavoris(favorisVideoListe : MutableList<Favoris>, context : Context){
    favorisVideoListe.sortWith(object: Comparator<Favoris>{
        override fun compare(songH1 : Favoris, songH2 : Favoris) : Int =
            songH2.index.compareTo(songH1.index)
    })
}