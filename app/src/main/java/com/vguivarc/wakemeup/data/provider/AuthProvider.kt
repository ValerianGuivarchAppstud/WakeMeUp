package com.vguivarc.wakemeup.data.provider

import com.vguivarc.wakemeup.data.network.AuthApi
import com.vguivarc.wakemeup.data.entity.*
import com.vguivarc.wakemeup.data.interceptor.HEADER_AUTHORIZATION_PREFIX
import com.vguivarc.wakemeup.domain.external.entity.UserProfile
import com.vguivarc.wakemeup.domain.internal.IAuthProvider
import com.vguivarc.wakemeup.domain.internal.ISessionProvider
import com.vguivarc.wakemeup.util.getDefaultLanguage
import retrofit2.Retrofit

class AuthProvider(retrofit: Retrofit, private val sessionService: ISessionProvider) : IAuthProvider {

    private val authApi = retrofit.create(AuthApi::class.java)

    override suspend fun logout() {
        sessionService.clearUserToken()
        sessionService.clearUserProfile()
    }

    override suspend fun login(email: String, password: String) {
        val userTokenStorage: UserToken= authApi.login(AuthRequest(email, password))
/*        }.flatMap {
            val firebaseToken = sessionService.getFirebaseToken()
            authApi.sendFirebaseToken(
                authorizationToken = HEADER_AUTHORIZATION_PREFIX + userTokenStorage?.accessToken,
                FirebaseTokenRequest(firebaseToken ?: "")
            )
                .toSingleDefault(true)*/

            userTokenStorage.let { sessionService.setUserToken(it) }
    }

    override suspend fun loginWithFacebook(token: String) {
        val userTokenStorage: UserToken = authApi.loginWithSocialNetwork(FacebookAuthRequest(facebookToken = token))
            val firebaseToken = sessionService.getFirebaseToken()
            authApi.sendFirebaseToken(
                authorizationToken = HEADER_AUTHORIZATION_PREFIX + userTokenStorage?.accessToken,
                FirebaseTokenRequest(firebaseToken ?: "")
            )
            userTokenStorage.let { sessionService.setUserToken(it) }
    }

    override suspend fun signup(email: String, password: String, username: String) {
        authApi.signup(AuthSignupRequest(email, password, username))
    }

    override suspend fun isUserConnected(): Boolean {
        return sessionService.getUserToken() != null
    }

    override suspend fun forgotPassword(email: String) {
        return authApi.resetPassword(
            getDefaultLanguage(),
            ResetPasswordRequest(email)
        )
    }

    override suspend fun sendFirebaseToken(token: String) {
        return authApi.sendFirebaseToken(
            authorizationToken = HEADER_AUTHORIZATION_PREFIX + sessionService.getUserToken()?.accessToken,
            FirebaseTokenRequest(token = token)
        )
    }
}
