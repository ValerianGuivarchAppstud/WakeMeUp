package com.vguivarc.wakemeup.domain.service

import com.vguivarc.wakemeup.domain.entity.Favorite
import com.vguivarc.wakemeup.domain.entity.Song
import io.reactivex.Completable
import io.reactivex.Single

interface FavoriteService {
    fun getFavoriteList(): Single<List<Favorite>>
    fun saveFavoriteStatus(song: Song, isFavorite: Boolean): Single<List<Favorite>>
}
