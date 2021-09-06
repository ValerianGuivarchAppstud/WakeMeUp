package com.vguivarc.wakemeup.domain.internal

import com.vguivarc.wakemeup.data.entity.UserToken
import com.vguivarc.wakemeup.domain.external.entity.UserProfile

/**
 * Methods used to get and set every information related to the user account or
 * his saved data.
 */
interface ISessionProvider {
    fun setUserToken(userToken: UserToken): Boolean
    fun getUserToken(): UserToken?
    fun clearUserToken(): Boolean
    fun setUserProfileSession(userProfile: UserProfile): Boolean
    fun getUserProfileSession(): UserProfile?
    fun getFacebookAuthToken(): String?
    fun clearUserProfile(): Boolean
    fun saveFirebaseToken(firebaseToken: String): Boolean
    fun getFirebaseToken(): String?
    fun isConnected(): Boolean
}
