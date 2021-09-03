package com.vguivarc.wakemeup.data.repository

import com.vguivarc.wakemeup.data.api.AuthApi
import com.vguivarc.wakemeup.data.entity.*
import com.vguivarc.wakemeup.data.interceptor.HEADER_AUTHORIZATION_PREFIX
import com.vguivarc.wakemeup.domain.entity.UserProfile
import com.vguivarc.wakemeup.domain.service.AuthService
import com.vguivarc.wakemeup.domain.service.SessionService
import com.vguivarc.wakemeup.util.getDefaultLanguage
import io.reactivex.Completable
import io.reactivex.Single
import retrofit2.Retrofit

class AuthRepository(retrofit: Retrofit, private val sessionService: SessionService) : AuthService {

    override fun getAndUpdateUserInfo(): Single<UserProfile> {
        return authApi.getAccountInfo().map {
            sessionService.setUserProfile(it)
            it
        }
    }

    override fun editAccount(nickname: String, email: String): Single<UserProfile> {
        return authApi.editAccount(AuthEditAccountRequest(nickname, email)).map {
            sessionService.setUserProfile(it)
            it
        }
    }

    override fun logout() {
        sessionService.clearUserToken()
        sessionService.clearUserProfile()
    }

    private val authApi = retrofit.create(AuthApi::class.java)

    override fun login(email: String, password: String): Completable {
        var userTokenStorage: UserToken? = null
        return authApi.login(AuthRequest(email, password)).map { userToken ->
            userTokenStorage = userToken
/*        }.flatMap {
            val firebaseToken = sessionService.getFirebaseToken()
            authApi.sendFirebaseToken(
                authorizationToken = HEADER_AUTHORIZATION_PREFIX + userTokenStorage?.accessToken,
                FirebaseTokenRequest(firebaseToken ?: "")
            )
                .toSingleDefault(true)*/
        }.map {
            userTokenStorage?.let { sessionService.setUserToken(it) }
        }.ignoreElement()
    }

    override fun loginWithFacebook(token: String): Completable {
        var userTokenStorage: UserToken? = null
        return authApi.loginWithSocialNetwork(FacebookAuthRequest(facebookToken = token)).map { userToken ->
            userTokenStorage = userToken
        }.flatMap {
            val firebaseToken = sessionService.getFirebaseToken()
            authApi.sendFirebaseToken(
                authorizationToken = HEADER_AUTHORIZATION_PREFIX + userTokenStorage?.accessToken,
                FirebaseTokenRequest(firebaseToken ?: "")
            ).toSingleDefault(true)
        }.map {
            userTokenStorage?.let { sessionService.setUserToken(it) }
        }.ignoreElement()
    }

    override fun signup(email: String, password: String, nickname: String): Completable {
        return authApi.signup(AuthSignupRequest(email, password, nickname))
    }

    override fun isUserConnected(): Boolean {
        return sessionService.getUserToken() != null
    }

    override fun forgotPassword(email: String): Completable {
        return authApi.resetPassword(
            getDefaultLanguage(),
            ResetPasswordRequest(email)
        )
    }

    override fun sendFirebaseToken(token: String): Completable {
        return authApi.sendFirebaseToken(
            authorizationToken = HEADER_AUTHORIZATION_PREFIX + sessionService.getUserToken()?.accessToken,
            FirebaseTokenRequest(token = token)
        )
    }
}
