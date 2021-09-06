package com.vguivarc.wakemeup.data.provider

import com.vguivarc.wakemeup.data.entity.AuthEditAccountRequest
import com.vguivarc.wakemeup.data.network.ProfileApi
import com.vguivarc.wakemeup.domain.external.entity.UserProfile
import com.vguivarc.wakemeup.domain.internal.IProfileProvider
import com.vguivarc.wakemeup.domain.internal.ISessionProvider
import retrofit2.Retrofit

class ProfileProvider(retrofit: Retrofit, private val sessionService: ISessionProvider) :
    IProfileProvider {

    private val profileApi = retrofit.create(ProfileApi::class.java)

    override suspend fun getAndUpdateUserInfo(): UserProfile {
        return profileApi.getAccountInfo()
    }

    override suspend fun editAccount(username: String, email: String): UserProfile {
        return profileApi.editAccount(AuthEditAccountRequest(username, email))
    }

}
