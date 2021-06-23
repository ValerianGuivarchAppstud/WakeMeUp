package com.vguivarc.wakemeup.domain.service

import com.vguivarc.wakemeup.data.entity.UserToken
import com.vguivarc.wakemeup.domain.entity.UserProfile

/**
 * Methods used to get and set every information related to the user account or
 * his saved data.
 */
interface SessionService {
    fun setUserToken(userToken: UserToken): Boolean
    fun getUserToken(): UserToken?
    fun clearUserToken(): Boolean
    fun setUserProfile(userProfile: UserProfile): Boolean
    fun getUserProfile(): UserProfile?
    fun clearUserProfile(): Boolean
    fun saveFirebaseToken(firebaseToken: String): Boolean
    fun getFirebaseToken(): String?

}
