package com.vguivarc.wakemeup.domain.internal

import com.vguivarc.wakemeup.domain.external.entity.UserProfile
import io.reactivex.Completable
import io.reactivex.Single


interface IProfileProvider {
    suspend fun editAccount(userFname: String, email: String) : UserProfile
    suspend fun getAndUpdateUserInfo(): UserProfile
    suspend fun getFacebookAuthToken(): String?
    }
