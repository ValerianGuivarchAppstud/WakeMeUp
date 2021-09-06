package com.vguivarc.wakemeup.domain.external.entity

data class SearchSong(
    val song: Song,
    var isFavorite: Boolean = false
)
