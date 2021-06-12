package com.vguivarc.wakemeup.ui.connect.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MediatorLiveData
import androidx.lifecycle.ViewModel
import com.facebook.AccessToken
import com.vguivarc.wakemeup.data.repository.Repository

/**
 * Fb login view model
 *
 * @property repo
 * @constructor Create empty Fb login view model
 */
class FbLoginViewModel(val repo : Repository) : ViewModel() {

    private val _loginResultState = MediatorLiveData<ConnectResult>()

    /**
     * Get login result live data
     *
     * @return
     */
    fun getLoginResultLiveData(): LiveData<ConnectResult> = _loginResultState

    init {
        _loginResultState.addSource(repo.loginResult) { loginResult ->
            _loginResultState.value = loginResult
        }
    }

    /**
     * Login
     *
     * @param accessToken
     */
    fun login(accessToken: AccessToken) {
        repo.signInWithCredential(accessToken)
    }


    /**
     * Disconnect
     *
     */
    fun disconnect() {
        repo.disconnect()
    }
}
