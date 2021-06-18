package com.vguivarc.wakemeup.domain.service

import com.vguivarc.wakemeup.domain.entity.Favorite
import io.reactivex.Single

interface FavoriteService {
    fun getFavoriteList(): Single<List<Favorite>>
    fun addFavorite(favorite: Favorite)
    fun removeFavorite(favorite: Favorite)
}
