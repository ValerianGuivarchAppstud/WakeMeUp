package com.vguivarc.wakemeup.connect.ui.login

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MediatorLiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.vguivarc.wakemeup.AppWakeUp
import com.vguivarc.wakemeup.R
import com.vguivarc.wakemeup.connect.ui.ConnectResult
import com.vguivarc.wakemeup.repo.Repository

class LoginViewModel(val repo : Repository) : ViewModel() {

    private val _loginForm = MutableLiveData<LoginFormState>()
    val loginFormState: LiveData<LoginFormState> = _loginForm


    private val _loginResultState = MediatorLiveData<ConnectResult>()
    fun getLoginResultLiveData(): LiveData<ConnectResult> = _loginResultState

    init {
        _loginResultState.addSource(repo.loginResult) { loginResult ->
            _loginResultState.value = loginResult
        }
    }

    fun login(mail: String, password: String) {
        repo.signInWithEmailAndPassword(mail, password)
    }


    fun loginDataChanged(username: String, password1: String) {
        if (!isUserNameValid(username)) {
            _loginForm.value =
                LoginFormState(usernameLoginError = R.string.invalid_username)
        } else if (!isPasswordValid(password1)) {
            _loginForm.value =
                LoginFormState(passwordLoginError = R.string.invalid_password)
        } else {
            _loginForm.value = LoginFormState(isDataValid = true)
        }
    }

    // A placeholder username validation check
    private fun isUserNameValid(username: String): Boolean {
        return (username.length >= 6) && !username.contains(" ")
    }

    // A placeholder password validation check
    private fun isPasswordValid(password: String): Boolean {
        return password.length >= 6
    }

}
