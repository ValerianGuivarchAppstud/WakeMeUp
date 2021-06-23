package com.vguivarc.wakemeup.domain.service

import com.vguivarc.wakemeup.domain.entity.Favorite
import io.reactivex.Completable
import io.reactivex.Single

interface FavoriteService {
    fun getFavoriteList(): Single<List<Favorite>>
    fun saveFavoriteStatus(favorite: Favorite, isFavorite: Boolean): Completable
}
