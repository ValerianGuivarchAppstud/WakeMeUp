package com.vguivarc.wakemeup.connect.ui.login

import androidx.lifecycle.LiveData
import androidx.lifecycle.MediatorLiveData
import androidx.lifecycle.ViewModel
import com.facebook.AccessToken
import com.vguivarc.wakemeup.connect.ui.ConnectResult
import com.vguivarc.wakemeup.repo.Repository

class FbLoginViewModel(val repo : Repository) : ViewModel() {

    private val _loginResultState = MediatorLiveData<ConnectResult>()
    fun getLoginResultLiveData(): LiveData<ConnectResult> = _loginResultState

    init {
        _loginResultState.addSource(repo.loginResult) { loginResult ->
            _loginResultState.value = loginResult
        }
    }

    fun login(accessToken: AccessToken) {
        repo.signInWithCredential(accessToken)
    }


    fun disconnect() {
        repo.disconnect()
    }
}
