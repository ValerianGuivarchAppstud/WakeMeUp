package com.vguivarc.wakemeup.data.api

import com.vguivarc.wakemeup.data.entity.*
import com.vguivarc.wakemeup.data.interceptor.HEADER_AUTHORIZATION
import com.vguivarc.wakemeup.domain.entity.UserProfile
import io.reactivex.Completable
import io.reactivex.Single
import retrofit2.http.*

interface AuthApi {
    @POST("v1/auth/login/email")
    fun login(@Body credentials: AuthRequest): Single<UserToken>

    @POST("v1/auth/login/social")
    fun loginWithSocialNetwork(@Body credentials: FacebookAuthRequest): Single<UserToken>

    @POST("v1/auth/refresh")
    fun refresh(@Body credentials: AuthRefreshRequest): Single<UserToken>

    @POST("v1/auth/resetPassword")
    fun resetPassword(
        @Header("Accept-Language") language: String,
        @Body credentials: ResetPasswordRequest
    ): Completable

    @POST("v1/auth/register/email")
    fun signup(@Body credentials: AuthSignupRequest): Completable

    @PUT("v1/auth/me")
    fun editAccount(@Body accountInfo: AuthEditAccountRequest): Single<UserProfile>

    @PUT("v1/auth/me/firebaseToken")
    fun sendFirebaseToken(
        @Header(value = HEADER_AUTHORIZATION) authorizationToken: String,
        @Body firebaseToken: FirebaseTokenRequest
    ): Completable

    @GET("v1/auth/me")
    fun getAccountInfo(
        @Header(value = "Content-Type")
        contentType: String = "application/x-www-form-urlencoded"
    ): Single<UserProfile>
}
