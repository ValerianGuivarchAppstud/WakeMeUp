package com.vguivarc.wakemeup.data.provider

import com.vguivarc.wakemeup.data.network.FavoriteApi
import com.vguivarc.wakemeup.data.entity.FavoriteRequest
import com.vguivarc.wakemeup.domain.external.entity.Favorite
import com.vguivarc.wakemeup.domain.external.entity.Song
import com.vguivarc.wakemeup.domain.internal.IFavoriteProvider
import io.reactivex.Single
import retrofit2.Retrofit

class FavoriteProvider(retrofit: Retrofit) : IFavoriteProvider {

    private val favoriteApi = retrofit.create(FavoriteApi::class.java)

    override suspend fun getFavoriteList(): List<Favorite> {
        return favoriteApi.getFavorites().map {
                favoriteResponse ->
                    Favorite(
                        favoriteResponse.createdAt,
                        Song(favoriteResponse.id,favoriteResponse.title, favoriteResponse.artworkUrl)
                    )
                }
    }

    override suspend fun saveFavoriteStatus(song: Song, isFavorite: Boolean): List<Favorite> {
        val favoriteRequest = FavoriteRequest(song, isFavorite)
        return favoriteApi.setFavorite(favoriteRequest).map {
                favoriteResponse ->
            Favorite(
                favoriteResponse.createdAt,
                Song(favoriteResponse.id,favoriteResponse.title, favoriteResponse.artworkUrl)
            )
        }
    }
}
