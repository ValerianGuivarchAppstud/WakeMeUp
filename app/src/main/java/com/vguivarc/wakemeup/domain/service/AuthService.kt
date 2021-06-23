package com.vguivarc.wakemeup.domain.service

import com.vguivarc.wakemeup.domain.entity.UserProfile
import io.reactivex.Completable
import io.reactivex.Single

/**
 * Auth related API requests
 */
interface AuthService {
    fun login(email: String, password: String): Completable
    fun signup(email: String, password: String, nickname: String): Completable
    fun isUserConnected(): Boolean
    fun loginWithFacebook(token: String): Completable
    fun logout()
    fun editAccount(nickname: String, email: String): Single<UserProfile>
    fun getAndUpdateUserInfo(): Single<UserProfile>
    fun forgotPassword(email: String): Completable
    fun sendFirebaseToken(token: String): Completable
}
