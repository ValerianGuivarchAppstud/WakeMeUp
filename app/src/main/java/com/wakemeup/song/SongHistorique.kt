package com.wakemeup.song

import java.io.Serializable

data class SongHistorique(
    val index : Int,
    val song : Song
) : Serializable