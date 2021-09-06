package com.vguivarc.wakemeup.domain.external

import com.vguivarc.wakemeup.data.entity.UserToken
import com.vguivarc.wakemeup.domain.external.entity.Contact
import com.vguivarc.wakemeup.domain.external.entity.ContactFacebook
import com.vguivarc.wakemeup.domain.external.entity.UserProfile
import com.vguivarc.wakemeup.domain.internal.IContactProvider
import com.vguivarc.wakemeup.domain.internal.ISessionProvider

class SessionInteractor(private val sessionProvider: ISessionProvider) {

    suspend fun setUserToken(userToken: UserToken): Boolean {
        return sessionProvider.setUserToken(userToken)
    }
    suspend fun getUserToken(): UserProfile? {
        return sessionProvider.getUserProfileSession()
    }

    suspend fun clearUserToken(): Boolean{
        return sessionProvider.clearUserToken()
    }

    suspend fun setUserProfileSession(userProfile: UserProfile): Boolean{
        return sessionProvider.setUserProfileSession(userProfile)
    }

    suspend fun getUserProfileSession(): UserProfile?{
        return sessionProvider.getUserProfileSession()
    }

    suspend fun getFacebookAuthToken(): String?{
        return sessionProvider.getFacebookAuthToken()
    }
    suspend fun clearUserProfile(): Boolean{
        return sessionProvider.clearUserProfile()
    }

    suspend fun saveFirebaseToken(firebaseToken: String): Boolean{
        return sessionProvider.saveFirebaseToken(firebaseToken)
    }

    suspend fun getFirebaseToken(): String?{
        return sessionProvider.getFirebaseToken()
    }

    suspend fun isConnected(): Boolean{
        return sessionProvider.isConnected()
    }

}
