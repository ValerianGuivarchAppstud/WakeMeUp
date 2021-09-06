package com.vguivarc.wakemeup.data.network

import com.vguivarc.wakemeup.data.entity.*
import com.vguivarc.wakemeup.data.interceptor.HEADER_AUTHORIZATION
import com.vguivarc.wakemeup.domain.external.entity.UserProfile
import io.reactivex.Completable
import io.reactivex.Single
import retrofit2.http.*

interface ProfileApi {

    @PUT("v1/user/profile/me")
    suspend fun editAccount(@Body accountInfo: AuthEditAccountRequest): UserProfile

    @GET("v1/user/profile/me")
    suspend fun getAccountInfo(
        @Header(value = "Content-Type")
        contentType: String = "application/x-www-form-urlencoded"
    ): UserProfile
}
