package com.vguivarc.wakemeup.data.repository

import com.vguivarc.wakemeup.domain.entity.Favorite
import com.vguivarc.wakemeup.domain.service.FavoriteService
import com.vguivarc.wakemeup.ui.song.Song
import io.reactivex.Single

class FavoriteRepository : FavoriteService {

    val mockList = mutableListOf<Favorite>()

    override fun getFavoriteList(): Single<List<Favorite>> {
        return Single.just(mockList)
    }

    override fun addFavorite(favorite: Favorite) {
        mockList.add(favorite)
    }

    override fun removeFavorite(favorite: Favorite) {
        mockList.remove(favorite)
    }

}