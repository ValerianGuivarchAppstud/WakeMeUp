package com.vguivarc.wakemeup.data.api

import com.vguivarc.wakemeup.data.entity.FavoriteRequest
import com.vguivarc.wakemeup.data.entity.FavoritesListResponse
import io.reactivex.Completable
import io.reactivex.Single
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.Path

/**
 * Api to retrieve series information
 */
interface FavoriteApi {
    @GET("v1/favorites")
    fun getFavorites(): Single<FavoritesListResponse>

    @POST("v1/favorite/{favoriteId}")
    fun setFavorite(
        @Path("favoriteId") favoriteId: String,
        @Body favoriteRequest: FavoriteRequest
    ): Completable
}
