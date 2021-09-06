package com.vguivarc.wakemeup.domain.internal

import com.vguivarc.wakemeup.domain.external.entity.UserProfile
import io.reactivex.Completable
import io.reactivex.Single


interface IAuthProvider {
    suspend fun login(email: String, password: String)
    suspend fun signup(email: String, password: String, username: String)
    suspend fun isUserConnected(): Boolean
    suspend fun loginWithFacebook(token: String)
    suspend fun logout()
    suspend fun forgotPassword(email: String)
    suspend fun sendFirebaseToken(token: String)
}
