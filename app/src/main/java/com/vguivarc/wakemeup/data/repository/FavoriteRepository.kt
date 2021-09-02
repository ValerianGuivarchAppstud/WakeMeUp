package com.vguivarc.wakemeup.data.repository

import com.vguivarc.wakemeup.data.api.FavoriteApi
import com.vguivarc.wakemeup.data.entity.FavoriteRequest
import com.vguivarc.wakemeup.domain.entity.Favorite
import com.vguivarc.wakemeup.domain.entity.Song
import com.vguivarc.wakemeup.domain.service.FavoriteService
import io.reactivex.Single
import retrofit2.Retrofit

class FavoriteRepository(retrofit: Retrofit) : FavoriteService {

    private val favoriteApi = retrofit.create(FavoriteApi::class.java)

    override fun getFavoriteList(): Single<List<Favorite>> {
        return favoriteApi.getFavorites().map {
                favoritesListResponse -> favoritesListResponse.map { favoriteResponse ->
                    Favorite(
                        favoriteResponse.createdAt,
                        Song(favoriteResponse.id,favoriteResponse.title, favoriteResponse.artworkUrl)
                    )
                }
        }
    }

    override fun saveFavoriteStatus(song: Song, isFavorite: Boolean): Single<List<Favorite>> {
        val favoriteRequest = FavoriteRequest(song, isFavorite)
        return favoriteApi.setFavorite(favoriteRequest).map {
                favoritesListResponse -> favoritesListResponse.map { favoriteResponse ->
            Favorite(
                favoriteResponse.createdAt,
                Song(favoriteResponse.id,favoriteResponse.title, favoriteResponse.artworkUrl)
            )
        }
        }
    }
}
