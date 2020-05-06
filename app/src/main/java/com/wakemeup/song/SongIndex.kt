package com.wakemeup.song

import java.io.Serializable

data class SongIndex(
    var list : MutableList<SongHistorique> = mutableListOf(),
    var index : Int = 0
) : Serializable