package com.vguivarc.wakemeup.domain.external

import com.vguivarc.wakemeup.domain.external.entity.UserProfile
import com.vguivarc.wakemeup.domain.internal.IProfileProvider


class ProfileInteractor(private val profileProvider: IProfileProvider) {
    suspend fun editAccount(username: String, email: String) : UserProfile {
        return profileProvider.editAccount(username, email)
    }
    suspend fun getAndUpdateUserInfo(): UserProfile {
        return profileProvider.getAndUpdateUserInfo()
    }

    suspend fun getFacebookAuthToken(): String? {
        return profileProvider.getFacebookAuthToken()

    }
}
