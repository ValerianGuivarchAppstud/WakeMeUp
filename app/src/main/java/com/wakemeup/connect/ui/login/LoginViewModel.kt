package com.wakemeup.connect.ui.login

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.wakemeup.AppWakeUp
import com.wakemeup.R
import com.wakemeup.connect.ui.ConnectResult

class LoginViewModel : ViewModel() {

    private val _loginForm = MutableLiveData<LoginFormState>()
    val loginFormState: LiveData<LoginFormState> = _loginForm

    private val _loginResult = MutableLiveData<ConnectResult>()
    val loginResult: LiveData<ConnectResult> = _loginResult

    fun login(mail: String, password: String) {

        AppWakeUp.auth.signInWithEmailAndPassword(mail, password)
            .addOnCompleteListener {
                if (it.isSuccessful) {
                    _loginResult.value =
                        ConnectResult(AppWakeUp.auth.currentUser)

                } else {
                    _loginResult.value =
                        ConnectResult(error = R.string.login_failed)
                }
            }
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
