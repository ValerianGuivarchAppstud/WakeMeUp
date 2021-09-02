package com.vguivarc.wakemeup.data.api

import com.vguivarc.wakemeup.data.entity.FavoriteRequest
import com.vguivarc.wakemeup.data.entity.FavoriteResponse
import com.vguivarc.wakemeup.domain.entity.Favorite
import io.reactivex.Single
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.POST

/**
 * Api to retrieve series information
 */
interface FavoriteApi {
    @GET("v1/favorites/list")
    fun getFavorites(
    ): Single<List<FavoriteResponse>>

    @POST("v1/favorites/status")
    fun setFavorite(
        @Body favoriteRequest: FavoriteRequest
    ): Single<List<FavoriteResponse>>
}
