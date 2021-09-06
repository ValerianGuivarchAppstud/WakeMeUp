package com.vguivarc.wakemeup.data.network

import com.vguivarc.wakemeup.data.entity.*
import com.vguivarc.wakemeup.data.interceptor.HEADER_AUTHORIZATION
import io.reactivex.Single
import retrofit2.http.Body
import retrofit2.http.Header
import retrofit2.http.POST
import retrofit2.http.PUT

interface AuthApi {
    @POST("v1/user/auth/register")
    suspend fun signup(@Body credentials: AuthSignupRequest)

    @POST("v1/user/auth/refresh")
    fun refresh(@Body credentials: AuthRefreshRequest): Single<UserToken>

    @POST("v1/user/auth/login/mail")
    suspend fun login(@Body credentials: AuthRequest): UserToken

    @POST("v1/user/auth/login/facebook")
    suspend fun loginWithSocialNetwork(@Body credentials: FacebookAuthRequest): UserToken

    @POST("v1/user/auth/resetPassword")
    suspend fun resetPassword(
        @Header("Accept-Language") language: String,
        @Body credentials: ResetPasswordRequest
    )

    @PUT("v1/user/auth/firebaseToken")
    suspend fun sendFirebaseToken(
        @Header(value = HEADER_AUTHORIZATION) authorizationToken: String,
        @Body firebaseToken: FirebaseTokenRequest
    )
}
