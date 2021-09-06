package com.vguivarc.wakemeup.domain.internal

import com.vguivarc.wakemeup.domain.external.entity.Favorite
import com.vguivarc.wakemeup.domain.external.entity.Song
import io.reactivex.Single

interface IFavoriteProvider {
    suspend fun getFavoriteList(): List<Favorite>
    suspend fun saveFavoriteStatus(song: Song, isFavorite: Boolean): List<Favorite>
}
