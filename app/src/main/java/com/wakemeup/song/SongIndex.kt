package com.wakemeup.song

import java.io.Serializable

data class SongIndex(
    var list : MutableList<SongHistorique> = mutableListOf(),
    var index : Int = 0
) : Serializable {

    fun remove(currentSong: Song) {
        var shToRemove : SongHistorique? = null
           for(sh in list){
               if(sh.song==currentSong)
               {
                   shToRemove=sh
               }
           }
        list.remove(shToRemove)
    }
}