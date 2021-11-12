package com.vguivarc.wakemeup.domain.external

import com.vguivarc.wakemeup.domain.internal.IAuthProvider

class AuthInteractor(private val authProvider: IAuthProvider) {
    suspend fun loginByEmail(email: String, password: String){
        authProvider.login(email, password)
    }
    suspend fun signup(email: String, password: String, username: String) {
        authProvider.signup(email, password, username)
    }
    suspend fun isUserConnected(): Boolean{
        return authProvider.isUserConnected()
    }

    suspend fun loginWithFacebook(token: String){
        authProvider.loginWithFacebook(token)
    }

    suspend fun logout(){
        authProvider.logout()
    }

    suspend fun forgotPassword(email: String){
        authProvider.forgotPassword(email)
    }

    suspend fun sendFirebaseToken(token: String){
        authProvider.sendFirebaseToken(token)
    }

}
