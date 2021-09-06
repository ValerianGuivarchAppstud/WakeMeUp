package com.vguivarc.wakemeup.data.network

import com.vguivarc.wakemeup.data.entity.FavoriteRequest
import com.vguivarc.wakemeup.data.entity.FavoriteResponse
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.POST

/**
 * Api to retrieve series information
 */
interface FavoriteApi {
    @GET("v1/favorites/list")
    suspend fun getFavorites(
    ): List<FavoriteResponse>

    @POST("v1/favorites/status")
    suspend fun setFavorite(
        @Body favoriteRequest: FavoriteRequest
    ): List<FavoriteResponse>
}
