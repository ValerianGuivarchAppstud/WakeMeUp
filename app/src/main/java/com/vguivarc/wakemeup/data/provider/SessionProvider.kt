package com.vguivarc.wakemeup.data.provider

import android.content.Context
import com.facebook.AccessToken
import com.squareup.moshi.Moshi
import com.vguivarc.wakemeup.BuildConfig
import com.vguivarc.wakemeup.data.entity.UserToken
import com.vguivarc.wakemeup.domain.external.entity.UserProfile
import com.vguivarc.wakemeup.domain.internal.ISessionProvider

private const val SHARED_PREFERENCES = BuildConfig.APPLICATION_ID
private const val SHARED_PREFERENCES_TOKEN = "session:token"
private const val SHARED_PREFERENCES_USER_PROFILE = "session:user"
private const val SHARED_PREFERENCES_FIREBASE_TOKEN = "session:firebase-token"


class SessionProvider(private val context: Context, private val moshi: Moshi) : ISessionProvider {

    private val sharedPreferences =
        context.getSharedPreferences(SHARED_PREFERENCES, Context.MODE_PRIVATE)

    override fun getUserProfileSession(): UserProfile?  = get(SHARED_PREFERENCES_USER_PROFILE)

    override fun getFacebookAuthToken(): String? {
        return AccessToken.getCurrentAccessToken()?.token
    }

    override fun setUserProfileSession(userProfile: UserProfile): Boolean {
        return set(SHARED_PREFERENCES_USER_PROFILE, userProfile)
    }

    override fun clearUserProfile(): Boolean =
        set<UserProfile?>(SHARED_PREFERENCES_USER_PROFILE, null)

    override fun clearUserToken(): Boolean {
        return context
            .getSharedPreferences(SHARED_PREFERENCES, Context.MODE_PRIVATE)
            .edit()
            .remove(SHARED_PREFERENCES_TOKEN)
            .commit()
    }
    override fun setUserToken(userToken: UserToken): Boolean =
        set(SHARED_PREFERENCES_TOKEN, userToken)

    override fun getUserToken(): UserToken? = get(SHARED_PREFERENCES_TOKEN)

    override fun saveFirebaseToken(firebaseToken: String): Boolean =
        set(SHARED_PREFERENCES_FIREBASE_TOKEN, firebaseToken)

    override fun getFirebaseToken(): String? = get(SHARED_PREFERENCES_FIREBASE_TOKEN)
    override fun isConnected(): Boolean {
        return getUserToken() != null
    }

    private inline fun <reified T> get(nameInDB: String): T? {
        val adapter = moshi.adapter(T::class.java).nullSafe()
        return adapter.fromJson(
            sharedPreferences.getString(nameInDB, null) ?: "null"
        )
    }

    private inline fun <reified T> set(nameInDB: String, objectToSave: T?): Boolean {
        return sharedPreferences.edit()
            .putString(nameInDB, moshi.adapter(T::class.java).nullSafe().toJson(objectToSave))
            .commit()
    }
}
