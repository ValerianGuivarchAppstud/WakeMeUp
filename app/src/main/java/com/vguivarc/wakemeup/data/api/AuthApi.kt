package com.vguivarc.wakemeup.data.api

import com.vguivarc.wakemeup.data.entity.*
import com.vguivarc.wakemeup.data.interceptor.HEADER_AUTHORIZATION
import com.vguivarc.wakemeup.domain.entity.UserProfile
import io.reactivex.Completable
import io.reactivex.Single
import retrofit2.http.*

interface AuthApi {
    @POST("v1/user/auth/register")
    fun signup(@Body credentials: AuthSignupRequest): Completable

    @POST("v1/user/auth/refresh")
    fun refresh(@Body credentials: AuthRefreshRequest): Single<UserToken>

    @POST("v1/user/auth/login/mail")
    fun login(@Body credentials: AuthRequest): Single<UserToken>

    @POST("v1/user/auth/login/facebook")
    fun loginWithSocialNetwork(@Body credentials: FacebookAuthRequest): Single<UserToken>

    @POST("v1/auth/resetPassword")
    fun resetPassword(
        @Header("Accept-Language") language: String,
        @Body credentials: ResetPasswordRequest
    ): Completable

    @PUT("v1/user/profile/me")
    fun editAccount(@Body accountInfo: AuthEditAccountRequest): Single<UserProfile>

    @PUT("v1/user/auth/firebaseToken")
    fun sendFirebaseToken(
        @Header(value = HEADER_AUTHORIZATION) authorizationToken: String,
        @Body firebaseToken: FirebaseTokenRequest
    ): Completable

    @GET("v1/user/profile/me")
    fun getAccountInfo(
        @Header(value = "Content-Type")
        contentType: String = "application/x-www-form-urlencoded"
    ): Single<UserProfile>
}
