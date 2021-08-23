package com.vguivarc.wakemeup.data.repository

import com.vguivarc.wakemeup.data.api.FavoriteApi
import com.vguivarc.wakemeup.data.entity.FavoriteRequest
import com.vguivarc.wakemeup.domain.entity.Favorite
import com.vguivarc.wakemeup.domain.service.FavoriteService
import io.reactivex.Completable
import io.reactivex.Single
import retrofit2.Retrofit

class FavoriteRepository(retrofit: Retrofit) : FavoriteService {

    private val favoriteApi = retrofit.create(FavoriteApi::class.java)

    override fun getFavoriteList(): Single<List<Favorite>> {
        return favoriteApi.getFavorites().map { it.favorites }
    }

    override fun saveFavoriteStatus(favorite: Favorite, isFavorite: Boolean): Completable {
        val favoriteRequest = FavoriteRequest(favorite, isFavorite)
        return favoriteApi.setFavorite(favoriteRequest)
    }
}
